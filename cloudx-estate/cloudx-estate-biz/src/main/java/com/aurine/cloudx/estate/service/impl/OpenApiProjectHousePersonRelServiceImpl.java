package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.common.core.exception.OpenApiServiceException;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.IsFocusPersonConstants;
import com.aurine.cloudx.estate.constant.enums.AuditStatusEnum;
import com.aurine.cloudx.estate.constant.enums.HouseHoldTypeEnum;
import com.aurine.cloudx.estate.constant.enums.HouseParkingHistoryActionEnum;
import com.aurine.cloudx.estate.constant.enums.OriginTypeEnum;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.dto.OpenApiProjectCardDto;
import com.aurine.cloudx.estate.dto.OpenApiProjectFaceResourcesDto;
import com.aurine.cloudx.estate.dto.OpenApiProjectHousePersonRelDto;
import com.aurine.cloudx.estate.entity.ProjectFocusPersonAttr;
import com.aurine.cloudx.estate.entity.ProjectHousePersonChangeHis;
import com.aurine.cloudx.estate.entity.ProjectHousePersonRel;
import com.aurine.cloudx.estate.entity.ProjectPassPlan;
import com.aurine.cloudx.estate.entity.ProjectPersonInfo;
import com.aurine.cloudx.estate.feign.OpenApiRemoteUserService;
import com.aurine.cloudx.estate.mapper.ProjectHousePersonRelMapper;
import com.aurine.cloudx.estate.service.OpenApiProjectCardService;
import com.aurine.cloudx.estate.service.OpenApiProjectFaceResourcesService;
import com.aurine.cloudx.estate.service.OpenApiProjectHousePersonRelService;
import com.aurine.cloudx.estate.service.OpenApiProjectPassPlanService;
import com.aurine.cloudx.estate.service.OpenApiProjectPersonInfoService;
import com.aurine.cloudx.estate.service.ProjectFocusPersonAttrService;
import com.aurine.cloudx.estate.service.ProjectHousePersonChangeHisService;
import com.aurine.cloudx.estate.service.ProjectHousePersonRelService;
import com.aurine.cloudx.estate.service.ProjectHouseServiceService;
import com.aurine.cloudx.estate.service.ProjectPersonInfoService;
import com.aurine.cloudx.estate.service.ProjectPersonPlanRelService;
import com.aurine.cloudx.estate.thirdparty.module.intercom.factory.IntercomFactoryProducer;
import com.aurine.cloudx.estate.thirdparty.module.wr20.entity.enums.WR20CredentialTypeEnum;
import com.aurine.cloudx.estate.vo.ProjectHouseServiceInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codingapi.tx.annotation.TxTransaction;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @Author: wrm
 * @Date: 2022/05/23 17:15
 * @Package: com.aurine.openv2.service
 * @Version: 1.0
 * @Remarks:
 **/
@Slf4j
@Service
public class OpenApiProjectHousePersonRelServiceImpl extends ServiceImpl<ProjectHousePersonRelMapper, ProjectHousePersonRel> implements OpenApiProjectHousePersonRelService {

    @Resource
    private OpenApiProjectPersonInfoService openApiProjectPersonInfoService;

    @Resource
    private ProjectPersonInfoService projectPersonInfoService;

    @Resource
    private OpenApiRemoteUserService openApiRemoteUserService;

    @Resource
    private ProjectFocusPersonAttrService projectFocusPersonAttrService;

    @Resource
    private OpenApiProjectCardService openApiProjectCardService;

    @Resource
    private OpenApiProjectFaceResourcesService openApiProjectFaceResourcesService;

    @Resource
    private ProjectHousePersonRelService projectHousePersonRelService;

    @Resource
    private ProjectHousePersonChangeHisService projectHousePersonChangeHisService;

    @Resource
    private OpenApiProjectPassPlanService openApiProjectPassPlanService;

    @Resource
    private ProjectPersonPlanRelService projectPersonPlanRelService;

    @Resource
    private ProjectHouseServiceService projectHouseServiceService;

    private static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String RENT_START_HOUR = " 00:00:00";
    private static final String RENT_STOP_HOUR = " 23:59:59";

    @Override
    @TxTransaction(isStart = true)
    @Transactional(rollbackFor = Exception.class)
    public R<OpenApiProjectHousePersonRelDto> saveHousehold(OpenApiProjectHousePersonRelDto projectHousePersonRelDto) {
        ProjectPersonInfo personInfo = null;

        // 业主类型判断该房屋是否存在业主
        ProjectHousePersonRel housePersonRel = this.getOne(Wrappers.lambdaQuery(ProjectHousePersonRel.class)
                .eq(ProjectHousePersonRel::getHouseId, projectHousePersonRelDto.getHouseId())
                .eq(ProjectHousePersonRel::getAuditStatus, AuditStatusEnum.pass.code)
                .eq(ProjectHousePersonRel::getHouseholdType, HouseHoldTypeEnum.OWNER.code));

        if (HouseHoldTypeEnum.OWNER.code.equals(projectHousePersonRelDto.getHouseholdType()) && ObjectUtil.isNotEmpty(housePersonRel)) {
            // 存在业主则返回错误
            throw new OpenApiServiceException("房屋已有业主，若要新增请先删除原业主");
        }

        // 根据手机号获取用户信息
        R<ProjectPersonInfo> personInfoRo = openApiProjectPersonInfoService.getPersonInfoByPhone(projectHousePersonRelDto.getMobile());
        if (personInfoRo.getCode() == CommonConstants.SUCCESS) {
            personInfo = personInfoRo.getData();
        }

        String personId;
        // 检查当前项目下住户信息是否存在
        if (ObjectUtil.isNotEmpty(personInfo)) {
            personId = personInfo.getPersonId();

            //检查该人员是否已经存在当前房屋内
            int num = this.count(new QueryWrapper<ProjectHousePersonRel>().lambda()
                    .eq(ProjectHousePersonRel::getPersonId, personId)
                    .eq(ProjectHousePersonRel::getHouseId, projectHousePersonRelDto.getHouseId())
                    .ne(ProjectHousePersonRel::getAuditStatus, AuditStatusEnum.notPass.code));

            boolean havePersonInHouse = num != 0;

            if (havePersonInHouse) {
                throw new OpenApiServiceException("该用户已经在当前房屋下");
            }

            // 查询是否存在手机账号对应的userId 更新对应用户信息
            R<SysUser> requestUser = openApiRemoteUserService.innerGetUserByUserNmae(projectHousePersonRelDto.getMobile(), "Y");

            if (requestUser.getCode() == CommonConstants.SUCCESS && ObjectUtil.isNotEmpty(requestUser.getData())) {
                personInfo.setUserId(requestUser.getData().getUserId());
            }

            personInfo.setPersonName(projectHousePersonRelDto.getPersonName());
            personInfo.setCredentialType(WR20CredentialTypeEnum.JMSFZ.cloudCode);

            boolean updatePersonInfoResult = openApiProjectPersonInfoService.updateById(personInfo);

            if (!updatePersonInfoResult) {
                throw new OpenApiServiceException("更新住户信息失败");
            }

        } else {
            personInfo = new ProjectPersonInfo();

            // 查询是否存在手机账号对应的userId 更新对应用户信息
            R<SysUser> requestUser = openApiRemoteUserService.innerGetUserByUserNmae(projectHousePersonRelDto.getMobile(), "Y");

            if (requestUser.getCode() == CommonConstants.SUCCESS && ObjectUtil.isNotEmpty(requestUser.getData())) {
                personInfo.setUserId(requestUser.getData().getUserId());
            }

            personId = UUID.randomUUID().toString().replaceAll("-", "");

            BeanUtil.copyProperties(projectHousePersonRelDto, personInfo);

            personInfo.setPersonId(personId);
            personInfo.setTelephone(projectHousePersonRelDto.getMobile());
            personInfo.setCredentialPicBack(null);
            personInfo.setCredentialPicFront(null);
            personInfo.setPStatus("1");

            if (StringUtils.isEmpty(projectHousePersonRelDto.getCredentialType())) {
                // 默认身份证类型居民身份证
                personInfo.setCredentialType(WR20CredentialTypeEnum.JMSFZ.cloudCode);
            }

            boolean savePersonInfoResult = openApiProjectPersonInfoService.save(personInfo);

            if (!savePersonInfoResult) {
                throw new OpenApiServiceException("住户新增失败");
            }
        }

        // 将人员信息植入人屋关系中
        ProjectHousePersonRel projectHousePersonRel = new ProjectHousePersonRel();
        BeanUtil.copyProperties(projectHousePersonRelDto, projectHousePersonRel);

        projectHousePersonRel.setAuditStatus(AuditStatusEnum.pass.code);
        projectHousePersonRel.setPersonId(personId);
        projectHousePersonRel.setStatus("1");
        projectHousePersonRel.setOrigin(OriginTypeEnum.OPEN_API.code);

        // 过滤无用数据
        if (HouseHoldTypeEnum.OWNER.code.equals(projectHousePersonRelDto.getHouseholdType())) {
            projectHousePersonRel.setMemberType(null);
            projectHousePersonRel.setRentStartTime(null);
            projectHousePersonRel.setRentStopTime(null);
        } else if (HouseHoldTypeEnum.FAMILY.code.equals(projectHousePersonRelDto.getHouseholdType())) {
            projectHousePersonRel.setRentStartTime(null);
            projectHousePersonRel.setRentStopTime(null);
        } else if (HouseHoldTypeEnum.TENANT.code.equals(projectHousePersonRelDto.getHouseholdType())) {
            projectHousePersonRel.setMemberType(null);
            // 访客类型 - 设置租赁时间格式
            DateTimeFormatter df = DateTimeFormatter.ofPattern(TIME_FORMAT);
            LocalDateTime effTime = LocalDateTime.parse(projectHousePersonRelDto.getRentStartTime() + RENT_START_HOUR, df);
            LocalDateTime expTime = LocalDateTime.parse(projectHousePersonRelDto.getRentStopTime() + RENT_STOP_HOUR, df);
            projectHousePersonRel.setRentStartTime(effTime);
            projectHousePersonRel.setRentStopTime(expTime);
        }

        if (StringUtils.isNotEmpty(projectHousePersonRelDto.getPlanId())) {
            // 查询通行方案是否存在，不存在返回不存在报错
            ProjectPassPlan passPlan = openApiProjectPassPlanService.getById(projectHousePersonRelDto.getPlanId());

            if (ObjectUtil.isEmpty(passPlan)) {
                throw new OpenApiServiceException(String.format("传入的通行方案Id[%s]不存在", projectHousePersonRelDto.getPlanId()));
            }
        }

        // 保存住户关系
        boolean saveHousePersonRelResult = super.save(projectHousePersonRel);

        if (!saveHousePersonRelResult) {
            throw new OpenApiServiceException("住户关系新增失败");
        }

        projectHousePersonRelDto.setPersonId(projectHousePersonRel.getPersonId());

        // 更新重点人员信息
        this.operateFocusAttr(projectHousePersonRelDto);

        // 记录历史数据
        ProjectHousePersonChangeHis projectHousePersonChangeHis = new ProjectHousePersonChangeHis();
        BeanUtil.copyProperties(projectHousePersonRelDto, projectHousePersonChangeHis);

        projectHousePersonChangeHis.setAction(HouseParkingHistoryActionEnum.IN.code);
        projectHousePersonChangeHis.setPersonId(personId);
        projectHousePersonChangeHis.setEffTime(projectHousePersonRel.getRentStartTime());
        projectHousePersonChangeHis.setExpTime(projectHousePersonRel.getRentStopTime());
        if (StrUtil.isEmpty(projectHousePersonChangeHis.getPersonName())) {
            projectHousePersonChangeHis.setPersonName(personInfo.getPersonName());
        }

        projectHousePersonChangeHisService.save(projectHousePersonChangeHis);

        // 对接远端增值服务, 云对讲
        List<ProjectHouseServiceInfoVo> projectHouseServiceInfoVos = projectHouseServiceService.getHouseServiceByHouseId(projectHousePersonRel.getHouseId());

        projectHouseServiceInfoVos.forEach(e -> {
            IntercomFactoryProducer.getFactory(e.getServiceCode()).getIntercomService().addPerson(projectHousePersonRel, ProjectContextHolder.getProjectId());
        });

        // 构建返回值
        OpenApiProjectHousePersonRelDto respHousePersonRelDto = new OpenApiProjectHousePersonRelDto();
        BeanUtil.copyProperties(projectHousePersonRel, respHousePersonRelDto);

        respHousePersonRelDto.setMobile(projectHousePersonRelDto.getMobile());
        respHousePersonRelDto.setPersonName(personInfo.getPersonName());

        // 存在门禁卡保存门禁卡关系并下发到设备
        if (StringUtils.isNotEmpty(projectHousePersonRelDto.getCardNo())) {
            OpenApiProjectCardDto projectCardDto = new OpenApiProjectCardDto();

            projectCardDto.setPersonId(personId);
            projectCardDto.setCardNo(projectHousePersonRelDto.getCardNo());
            projectCardDto.setPersonType(PersonTypeEnum.PROPRIETOR.code);
            projectCardDto.setPlanId(projectHousePersonRelDto.getPlanId());

            R<OpenApiProjectCardDto> saveCardResp = openApiProjectCardService.saveCardInfo(projectCardDto);

            if (saveCardResp.getCode() == CommonConstants.SUCCESS) {
                respHousePersonRelDto.setCardNo(saveCardResp.getData().getCardNo());
            } else {
                throw new OpenApiServiceException(saveCardResp.getMsg());
            }
        }

        // 存在人脸保存人脸关系并下发到设备
        if (StringUtils.isNotEmpty(projectHousePersonRelDto.getPicUrl())) {
            OpenApiProjectFaceResourcesDto faceResourcesDto = new OpenApiProjectFaceResourcesDto();

            faceResourcesDto.setPersonId(personId);
            faceResourcesDto.setPicUrl(projectHousePersonRelDto.getPicUrl());
            faceResourcesDto.setPersonType(PersonTypeEnum.PROPRIETOR.code);
            faceResourcesDto.setPlanId(projectHousePersonRelDto.getPlanId());

            R<OpenApiProjectFaceResourcesDto> saveFaceResp = openApiProjectFaceResourcesService.saveFaceInfo(faceResourcesDto);

            if (saveFaceResp.getCode() == CommonConstants.SUCCESS) {
                respHousePersonRelDto.setFaceUrl(saveFaceResp.getData().getPicUrl());
            } else {
                throw new OpenApiServiceException(saveFaceResp.getMsg());
            }
        }

        ProjectPersonInfo projectPersonInfo = openApiProjectPersonInfoService.getById(respHousePersonRelDto.getPersonId());

        respHousePersonRelDto.setCredentialType(projectPersonInfo.getCredentialType());
        respHousePersonRelDto.setCredentialNo(projectPersonInfo.getCredentialNo());

        return R.ok(respHousePersonRelDto);
    }

    private void saveHousePersonChangeHis(OpenApiProjectHousePersonRelDto projectHousePersonRelDto) {

    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<OpenApiProjectHousePersonRelDto> updateById(OpenApiProjectHousePersonRelDto projectHousePersonRelDto) {
        OpenApiProjectHousePersonRelDto respHousePersonRelDto = new OpenApiProjectHousePersonRelDto();

        // 修改人员信息
        if (ObjectUtil.isNotEmpty(projectHousePersonRelDto.getPersonId())) {
            ProjectPersonInfo projectPersonInfo = new ProjectPersonInfo();
            BeanUtil.copyProperties(projectHousePersonRelDto, projectPersonInfo);

            boolean updateHousePersonRelResult = openApiProjectPersonInfoService.updateById(projectPersonInfo);
            if (!updateHousePersonRelResult) {
                throw new OpenApiServiceException(String.format("修改人员[%s]信息失败", projectHousePersonRelDto.getPersonId()));
            }

            ProjectPersonInfo byId = projectPersonInfoService.getById(projectPersonInfo.getPersonId());

            respHousePersonRelDto.setCredentialType(byId.getCredentialType());
            respHousePersonRelDto.setCredentialNo(byId.getCredentialNo());
            respHousePersonRelDto.setMobile(byId.getTelephone());
            respHousePersonRelDto.setPersonName(byId.getPersonName());

        }
        // 更新重点人员信息
        this.operateFocusAttr(projectHousePersonRelDto);

        // 更新住户-房屋信息
        ProjectHousePersonRel projectHousePersonRel = new ProjectHousePersonRel();
        BeanUtil.copyProperties(projectHousePersonRelDto, projectHousePersonRel);

        // 过滤无用数据
        if (HouseHoldTypeEnum.OWNER.code.equals(projectHousePersonRelDto.getHouseholdType())) {
            projectHousePersonRel.setMemberType(null);
            projectHousePersonRel.setRentStartTime(null);
            projectHousePersonRel.setRentStopTime(null);
        } else if (HouseHoldTypeEnum.FAMILY.code.equals(projectHousePersonRelDto.getHouseholdType())) {
            projectHousePersonRel.setRentStartTime(null);
            projectHousePersonRel.setRentStopTime(null);
        } else if (HouseHoldTypeEnum.TENANT.code.equals(projectHousePersonRelDto.getHouseholdType())) {
            // 访客类型 - 设置租赁结束时间格式
            DateTimeFormatter df = DateTimeFormatter.ofPattern(TIME_FORMAT);
            LocalDateTime expTime = LocalDateTime.parse(projectHousePersonRelDto.getRentStopTime() + RENT_STOP_HOUR, df);
            projectHousePersonRel.setRentStopTime(expTime);
        }
        this.updateById(projectHousePersonRel);

        ProjectHousePersonRel byId = this.getByRelaId(projectHousePersonRel.getRelaId());
        BeanUtil.copyProperties(byId, respHousePersonRelDto, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));

        return R.ok(respHousePersonRelDto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<String> removeHousePersonRel(String realId) {
        ProjectHousePersonRel housePersonRel = projectHousePersonRelService.getById(realId);

        if (housePersonRel != null) {
            projectPersonInfoService.checkPersonAssets(housePersonRel.getPersonId());
        }

        // 根据关系id获取住户id
        ProjectHousePersonRel byId = this.getById(realId);

        // 删除重点人员关系
        projectFocusPersonAttrService.removeFocusPersonAttrByPersonId(byId.getPersonId());

        // 删除住户方案
        projectPersonPlanRelService.deleteByPersonId(byId.getPersonId());

        // 删除云对讲
        List<ProjectHouseServiceInfoVo> projectHouseServiceInfoVos = projectHouseServiceService.getHouseServiceByHouseId(housePersonRel.getHouseId());

        if (CollUtil.isNotEmpty(projectHouseServiceInfoVos)) {
            log.info("开始删除增值服务" + projectHouseServiceInfoVos + "{}房屋id:" + housePersonRel.getHouseId());
            projectHouseServiceInfoVos.forEach(e -> {
                // 移除远端的该住户在此房屋的增值服务
                IntercomFactoryProducer.getFactory(e.getServiceCode()).getIntercomService().delPerson(realId);
            });
        }

        boolean removeHousePersonRelResult = projectHousePersonRelService.removeHousePersonRelById(realId);

        if (!removeHousePersonRelResult) {
            throw new OpenApiServiceException("删除住户失败");
        }

        return R.ok(byId.getPersonId());
    }

    @Override
    public Boolean checkPersonOwnHouse(String personId, String houseId) {
        int count = this.count(new QueryWrapper<ProjectHousePersonRel>().lambda()
                .eq(ProjectHousePersonRel::getPersonId, personId)
                .eq(ProjectHousePersonRel::getHouseId, houseId)
        );
        return count >= 1;
    }

    /**
     * <p>
     * 对重点人员信息进行更新或删除或添加操作
     * </p>
     *
     * @param housePersonRelDto 人物关系vo对象
     * @author: copy王良俊
     */
    private void operateFocusAttr(OpenApiProjectHousePersonRelDto housePersonRelDto) {
        if (IsFocusPersonConstants.YES.equals(housePersonRelDto.getIsFocusPerson())) {
            String[] focusCategoryArr = housePersonRelDto.getFocusCategoryArr();
            String focusCategory = "";

            if (ArrayUtil.isNotEmpty(focusCategoryArr)) {
                focusCategory = Arrays.toString(focusCategoryArr);
                focusCategory = focusCategory.substring(1, focusCategory.length() - 1);
            }

            ProjectFocusPersonAttr focusPersonAttr = new ProjectFocusPersonAttr();
            BeanUtil.copyProperties(housePersonRelDto, focusPersonAttr);

            focusPersonAttr.setFocusCategory(focusCategory);

            projectFocusPersonAttrService.saveOrUpdateFocusPersonAttrByPersonId(focusPersonAttr);
        } else {
            if (StrUtil.isNotBlank(housePersonRelDto.getPersonId())) {
                projectFocusPersonAttrService.removeFocusPersonAttrByPersonId(housePersonRelDto.getPersonId());
            }
        }
    }

    private ProjectHousePersonRel getByRelaId(String relaId) {
        return this.getOne(new QueryWrapper<ProjectHousePersonRel>().lambda()
                .eq(ProjectHousePersonRel::getRelaId, relaId));
    }

}
