package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.exception.OpenApiServiceException;
import com.aurine.cloudx.estate.constant.PersonConstant;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.dto.OpenApiProjectCardDto;
import com.aurine.cloudx.estate.dto.OpenApiProjectFaceResourcesDto;
import com.aurine.cloudx.estate.dto.OpenApiProjectStaffDto;
import com.aurine.cloudx.estate.entity.ProjectPassPlan;
import com.aurine.cloudx.estate.entity.ProjectStaff;
import com.aurine.cloudx.estate.entity.ProjectUrgedPayment;
import com.aurine.cloudx.estate.feign.OpenApiRemoteNewUserService;
import com.aurine.cloudx.estate.feign.OpenApiRemoteRoleService;
import com.aurine.cloudx.estate.feign.OpenApiRemoteUserService;
import com.aurine.cloudx.estate.mapper.ProjectStaffMapper;
import com.aurine.cloudx.estate.service.OpenApiProjectCardService;
import com.aurine.cloudx.estate.service.OpenApiProjectFaceResourcesService;
import com.aurine.cloudx.estate.service.OpenApiProjectPassPlanService;
import com.aurine.cloudx.estate.service.OpenApiProjectStaffService;
import com.aurine.cloudx.estate.service.ProjectInspectPlanShift3Service;
import com.aurine.cloudx.estate.service.ProjectPersonAttrService;
import com.aurine.cloudx.estate.service.ProjectPersonDeviceService;
import com.aurine.cloudx.estate.service.ProjectPersonPlanRelService;
import com.aurine.cloudx.estate.service.ProjectRightDeviceService;
import com.aurine.cloudx.estate.thirdparty.module.wr20.entity.enums.WR20CredentialTypeEnum;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codingapi.tx.annotation.TxTransaction;
import com.pig4cloud.pigx.admin.api.dto.CxUserDTO;
import com.pig4cloud.pigx.admin.api.entity.SysRole;
import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * 项目员工信息表
 *
 * @author lingang
 * @date 2020-05-11 13:38:09
 */
@Service
@Primary
@Slf4j
public class OpenApiProjectStaffServiceImpl extends ServiceImpl<ProjectStaffMapper, ProjectStaff> implements OpenApiProjectStaffService {
    @Resource
    private OpenApiRemoteUserService openApiRemoteUserService;
    @Resource
    private ProjectPersonAttrService projectPersonAttrService;
    @Resource
    private ProjectRightDeviceService projectRightDeviceService;
    @Resource
    private ProjectPersonDeviceService projectPersonDeviceService;
    @Resource
    private ProjectInspectPlanShift3Service projectInspectPlanShift3Service;

    @Resource
    private OpenApiRemoteRoleService openApiRemoteRoleService;

    @Resource
    private OpenApiProjectPassPlanService openApiProjectPassPlanService;

    @Resource
    private OpenApiProjectCardService openApiProjectCardService;

    @Resource
    private OpenApiProjectFaceResourcesService openApiProjectFaceResourcesService;

    @Resource
    private OpenApiRemoteNewUserService openApiRemoteNewUserService;

    @Resource
    private ProjectPersonPlanRelService projectPersonPlanRelService;

    /**
     * 保存人员信息，有传入人脸卡信息保存信息并下发到设备
     *
     * @param projectStaffDto
     * @return
     */
    @Override
    @TxTransaction(isStart = true)
    @Transactional(rollbackFor = Exception.class)
    public R<OpenApiProjectStaffDto> saveStaff(OpenApiProjectStaffDto projectStaffDto) {

        // 查询是否手机号已注册
        R<String> staffIdByPhone = this.getStaffIdByPhone(projectStaffDto.getMobile());

        if (staffIdByPhone.getCode() == CommonConstants.SUCCESS) {
            throw new OpenApiServiceException(String.format("该手机号[%s]已被使用", projectStaffDto.getMobile()));
        }

        // 设置默认职位
        projectStaffDto.setGrade("1");

        // 默认可删除
        projectStaffDto.setIsDel("0");

        // 未传入身份证默认身份证类型居民身份证
        if (StringUtils.isEmpty(projectStaffDto.getCredentialType())) {
            projectStaffDto.setCredentialType(WR20CredentialTypeEnum.JMSFZ.cloudCode);
        }

        // 获取默认角色
        R<List<SysRole>> roleR = openApiRemoteRoleService.innerGetByDeptId(projectStaffDto.getProjectId(), "Y");

        if (roleR.getCode() != CommonConstants.SUCCESS) {
            throw new OpenApiServiceException("未设置默认部门");
        }

        if (roleR.getData().size() == 0) {
            throw new OpenApiServiceException("默认部门下未分配角色");
        }

        // 角色默认查项目下最后一个角色赋值
        //SysRole role = roleR.getData().stream().max(Comparator.comparing(SysRole::getRoleId)).get();
        // 角色默认查项目下其它人员角色赋值
        SysRole role = roleR.getData().stream().filter(r -> r.getRoleCode().equals("OTHER_MANAGER")).findFirst().get();
        if(role == null){
            role = roleR.getData().stream().max(Comparator.comparing(SysRole::getRoleId)).get();
        }
        projectStaffDto.setNewRoleId(role.getRoleId());
        projectStaffDto.setRoleId(role.getRoleId());

        if (StringUtils.isNotEmpty(projectStaffDto.getPlanId())) {
            // 查询通行方案是否存在，不存在返回不存在报错
            ProjectPassPlan passPlan = openApiProjectPassPlanService.getById(projectStaffDto.getPlanId());
            if (ObjectUtil.isEmpty(passPlan)) {
                throw new OpenApiServiceException(String.format("传入的通行方案Id[%s]不存在", projectStaffDto.getPlanId()));
            }
        }

        // 返回的新增对象内容
        OpenApiProjectStaffDto respStaffDto = new OpenApiProjectStaffDto();

        String staffId = UUID.randomUUID().toString().replace("-", "");
        projectStaffDto.setStaffId(staffId);

        CxUserDTO user = parseUser(projectStaffDto);

        user.setPassword(PersonConstant.PASSWORD);
        user.setDeptId(projectStaffDto.getDepartmentId());
        user.setUsername(projectStaffDto.getMobile());
        user.setPhone(projectStaffDto.getMobile());

        // 调动pigx接口保存员工权限数据
        R<Integer> userR = openApiRemoteNewUserService.innerSaveUserRoleByStaff(user, "Y");

        if (userR.getCode() != CommonConstants.SUCCESS) {
            log.error(String.format("pigx 保存员工角色权限数据失败,obj = %s", JSONObject.toJSONString(user)));
            throw new OpenApiServiceException("服务器异常");
        }

        projectStaffDto.setUserId(userR.getData());

        ProjectStaff projectStaff = new ProjectStaff();
        BeanUtils.copyProperties(projectStaffDto, projectStaff);

        // 保存员工信息
        boolean save = this.save(projectStaff);

        // 构建返回参数
        BeanUtils.copyProperties(projectStaff, respStaffDto);
        respStaffDto.setStaffId(staffId);

        // 存在门禁卡保存门禁卡关系并下发到设备
        if (StringUtils.isNotEmpty(projectStaffDto.getCardNo())) {
            OpenApiProjectCardDto projectCardDto = new OpenApiProjectCardDto();

            projectCardDto.setPersonId(staffId);
            projectCardDto.setCardNo(projectStaffDto.getCardNo());
            projectCardDto.setPersonType(PersonTypeEnum.STAFF.code);
            projectCardDto.setPlanId(projectStaffDto.getPlanId());

            R<OpenApiProjectCardDto> saveCardResp = openApiProjectCardService.saveCardInfo(projectCardDto);

            if (saveCardResp.getCode() == CommonConstants.SUCCESS) {
                respStaffDto.setCardNo(saveCardResp.getData().getCardNo());
            } else {
                throw new OpenApiServiceException(saveCardResp.getMsg());
            }
        }

        // 存在人脸保存人脸关系并下发到设备
        if (StringUtils.isNotEmpty(projectStaffDto.getPicUrl())) {
            OpenApiProjectFaceResourcesDto faceResourcesDto = new OpenApiProjectFaceResourcesDto();

            faceResourcesDto.setPersonId(staffId);
            faceResourcesDto.setPicUrl(projectStaffDto.getPicUrl());
            faceResourcesDto.setPersonType(PersonTypeEnum.STAFF.code);
            faceResourcesDto.setPlanId(projectStaffDto.getPlanId());

            R<OpenApiProjectFaceResourcesDto> saveFaceResp = openApiProjectFaceResourcesService.saveFaceInfo(faceResourcesDto);

            if (saveFaceResp.getCode() == CommonConstants.SUCCESS) {
                respStaffDto.setFaceUrl(saveFaceResp.getData().getPicUrl());
            } else {
                throw new OpenApiServiceException(saveFaceResp.getMsg());
            }
        }

        return R.ok(respStaffDto);
    }

    @Override
    public R<OpenApiProjectStaffDto> updateStaff(OpenApiProjectStaffDto projectStaffDto) {
        // 更新员工对象
        ProjectStaff projectStaff = new ProjectStaff();

        BeanUtils.copyProperties(projectStaffDto, projectStaff);

        boolean res = super.updateById(projectStaff);

        if (!res) {
            throw new OpenApiServiceException(String.format("更新id[%s]的员工数据失败", projectStaffDto.getStaffId()));
        }

        return R.ok(projectStaffDto);
    }

    @Override
    @TxTransaction(isStart = true)
    @Transactional(rollbackFor = Exception.class)
    public R<String> removeStaff(String staffId) {
        ProjectStaff staff = this.getById(staffId);

        if (ObjectUtils.isEmpty(staff)) {
            throw new OpenApiServiceException(String.format("找不到id[%s]对应的员工", staffId));
        }

        if (staff.getUserId() == null) {
            this.removeById(staffId);
            return R.ok(staffId);
        } else {
            R<Boolean> r = openApiRemoteUserService.innerRemoveUserRole(staff.getUserId(), staff.getRoleId(), "Y");

            if (r.getCode() == CommonConstants.SUCCESS) {

                // 删除用户通行方案
                projectPersonPlanRelService.deleteByPersonId(staffId);

                // 迁出介质权限
                projectRightDeviceService.removeCertDeviceAuthorize(staffId);

                // 更新权限
                projectPersonDeviceService.refreshByPersonId(staffId, PersonTypeEnum.STAFF);

                // 删除拓展属性值
                projectPersonAttrService.removePersonAttrList(staffId);

                boolean removeResult = this.removeById(staffId);

                if (removeResult) {
                    projectInspectPlanShift3Service.removePerson(staffId);

                    return R.ok(staffId);
                } else {
                    throw new OpenApiServiceException("员工删除失败");
                }

            } else {
                throw new OpenApiServiceException(r.getMsg());
            }
        }
    }

    /**
     * 内部调用方法，根据部门Id获取部门员工数
     *
     * @param deptId 部门Id
     * @return
     */
    @Override
    public Integer getStaffCountByDeptId(Integer deptId) {
        return this.count(new QueryWrapper<ProjectStaff>().lambda()
                .eq(ProjectStaff::getDepartmentId, deptId));
    }

    /**
     * staff to user
     *
     * @param staff
     * @return
     */
    protected CxUserDTO parseUser(OpenApiProjectStaffDto staff) {
        CxUserDTO user = new CxUserDTO();

        user.setUserId(staff.getUserId());
        user.setAvatar(staff.getPicUrl());
        user.setSex(staff.getSex());
        user.setCredentialType(staff.getCredentialType());
        user.setCredentialNo(staff.getCredentialNo());
        user.setTrueName(staff.getStaffName());
        user.setNewRoleId(staff.getNewRoleId());
        user.setRoleId(staff.getOldRoleId());
        user.setRoleExpTime(staff.getRoleExpTime());

        return user;
    }

    /**
     * 内部调用方法，根据员工手机号获取员工Id
     *
     * @param mobile 手机号
     * @return
     */
    public R<String> getStaffIdByPhone(String mobile) {
        // 根据手机号获取员工表员工id
        ProjectStaff staff = this.getOne(new QueryWrapper<ProjectStaff>().lambda()
                .eq(ProjectStaff::getMobile, mobile));

        if (BeanUtil.isEmpty(staff)) {
            return R.failed(String.format("找不到手机号[%s]对应的员工信息", mobile));
        }

        return R.ok(staff.getStaffId());
    }

}

