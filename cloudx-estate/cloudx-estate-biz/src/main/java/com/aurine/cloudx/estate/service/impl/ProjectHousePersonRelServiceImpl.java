package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.DeviceExcelConstant;
import com.aurine.cloudx.estate.constant.IsFocusPersonConstants;
import com.aurine.cloudx.estate.constant.ProjectConfigConstant;
import com.aurine.cloudx.estate.constant.SysDeptConstant;
import com.aurine.cloudx.estate.constant.enums.*;
import com.aurine.cloudx.estate.dto.ProjectHouseDTO;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.excel.ProjectHousePersonRelListener;
import com.aurine.cloudx.estate.excel.person.HousePersonRelExcel;
import com.aurine.cloudx.estate.feign.RemoteHousePersonRelService;
import com.aurine.cloudx.estate.mapper.ProjectHousePersonRelMapper;
import com.aurine.cloudx.estate.openapi.enums.OpenApiCommandTypeEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenApiServiceNameEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenPushSubscribeCallbackTypeEnum;
import com.aurine.cloudx.estate.openapi.service.OpenApiMessageService;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.service.adapter.AbstractProjectHousePersonRelService;
import com.aurine.cloudx.estate.thirdparty.module.wr20.factory.WR20Factory;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.pig4cloud.pigx.admin.api.dto.UserDTO;
import com.pig4cloud.pigx.admin.api.dto.UserInfo;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.admin.api.feign.RemoteUserService;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.exception.ValidateCodeException;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * 住户
 *
 * @author pigx code generator
 * @date 2020-05-11 08:17:43
 */
@Service
@Primary
@Slf4j
public class ProjectHousePersonRelServiceImpl extends ServiceImpl<ProjectHousePersonRelMapper, ProjectHousePersonRel> implements ProjectHousePersonRelService {

    @Resource
    private ProjectPersonLabelService projectPersonLabelService;
    /**
     * 人员信息
     */
    @Resource
    private ProjectPersonInfoService projectPersonInfoService;
    @Resource
    private ProjectPersonAttrService projectPersonAttrService;
//    @Resource
//    private ProjectHouseServiceService projectHouseServiceService;
    @Resource
    private RedisTemplate<String, String> redisTemplateAurine;
    @Resource
    private ProjectEntityLevelCfgService projectEntityLevelCfgService;
    @Resource
    private ProjectFrameInfoService projectFrameInfoService;
    @Resource
    private ProjectHousePersonChangeHisService projectHousePersonChangeHisService;
    @Resource
    private ProjectRightDeviceService projectRightDeviceService;
    @Resource
    private RemoteUserService remoteUserService;
//    @Resource
//    private NoticeUtil noticeUtil;
    @Resource
    private ProjectPersonDeviceService projectPersonDeviceService;
//    @Resource
//    private ProjectHouseInfoService projectHouseInfoService;
    @Resource
    private AbstractProjectHousePersonRelService abstractWebProjectHousePersonRelService;
    @Resource
    private ProjectHousePersonRelMapper projectHousePersonRelMapper;
    @Resource
    private ProjectFocusPersonAttrService projectFocusPersonAttrService;
    @Resource
    private ProjectConfigService projectConfigService;
    @Resource
    private ProjectParkingPlaceService projectParkingPlaceService;
    @Resource
    private ProjectParCarRegisterService projectParCarRegisterService;
    @Resource
    private RemoteHousePersonRelService remoteHousePersonRelService;

    @Resource
    private ProjectPersonPlanRelService projectPersonPlanRelService;

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Resource
    private OpenApiMessageService openApiMessageService;
    @Resource
    private EdgeCloudRequestService edgeCloudRequestService;
    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;
    /*@Resource
    private AbstractProjectHousePersonRelService adapterProjectHousePersonRelServiceImpl;*/

    /*需要调整-已注释消息发送相关代码*/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProjectHousePersonRel verify(ProjectHousePersonRelVo projectHousePersonRelVo) {

        ProjectHousePersonChangeHis projectHousePersonChangeHis = new ProjectHousePersonChangeHis();

        log.info("主线程的ProjectId:" + ProjectContextHolder.getProjectId());
        if (projectHousePersonRelVo.getAuditStatus().equals(AuditStatusEnum.pass.code)) {
            // 用户提交身份证照片，保存到presonInfo中
            ProjectPersonInfo personInfo = projectPersonInfoService.getById(projectHousePersonRelVo.getPersonId());
            if (StrUtil.isNotBlank(projectHousePersonRelVo.getCredentialPicBack()) &&
                    StrUtil.isNotBlank(projectHousePersonRelVo.getCredentialPicFront())) {
                personInfo.setCredentialPicBack(projectHousePersonRelVo.getCredentialPicBack());
                personInfo.setCredentialPicFront(projectHousePersonRelVo.getCredentialPicFront());
                projectPersonInfoService.updateById(personInfo);
            }

            //当前房屋下审核成功的所有人员集合
            List<ProjectHousePersonRelVo> list = baseMapper.getHousePersonRel(projectHousePersonRelVo.getHouseId());
            if (CollUtil.isNotEmpty(list)) {
                for (ProjectHousePersonRelVo projectPersonInfo : list) {
                    if ("1".equals(projectHousePersonRelVo.getHouseholdType()) &&
                            "1".equals(projectPersonInfo.getHouseholdType())) {
                        verifyRemoveHousePersonRelById(projectPersonInfo.getRelaId());
                    } else if (projectPersonInfo.getPersonName().equals(projectHousePersonRelVo.getPersonName()) &&
                            !projectPersonInfo.getTelephone().equals(projectHousePersonRelVo.getTelephone()) &&
                            !"1".equals(projectPersonInfo.getHouseholdType())) {
                        verifyRemoveHousePersonRelById(projectPersonInfo.getRelaId());
                    }

                }
            }
            //添加房屋人员变更日志
            projectHousePersonChangeHis = new ProjectHousePersonChangeHis();
            projectHousePersonChangeHis.setHouseId(projectHousePersonRelVo.getHouseId());
            projectHousePersonChangeHis.setPersonId(projectHousePersonRelVo.getPersonId());
            projectHousePersonChangeHis.setHousePeopleRel(projectHousePersonRelVo.getHousePeopleRel());
            projectHousePersonChangeHis.setHouseholdType(projectHousePersonRelVo.getHouseholdType());
            projectHousePersonChangeHis.setMemberType(projectHousePersonRelVo.getMemberType());
            projectHousePersonChangeHis.setAction(HouseParkingHistoryActionEnum.VERIFYIN.code);
            projectHousePersonChangeHis.setPersonName(projectHousePersonRelVo.getPersonName());
            projectHousePersonChangeHisService.save(projectHousePersonChangeHis);
        }


        ProjectHousePersonRel projectHousePersonRel = new ProjectHousePersonRel();
        BeanUtils.copyProperties(projectHousePersonRelVo, projectHousePersonRel);
        boolean updateById = this.updateById(projectHousePersonRel);



        /*
         * 房屋变动，应改变权限配置
         * @since: 2020-09-16
         * @author: 王良俊
         *
         */
        if (projectHousePersonRelVo.getAuditStatus().equals(AuditStatusEnum.pass.code) && updateById) {
            projectPersonDeviceService.refreshByPersonId(projectHousePersonRel.getPersonId(), PersonTypeEnum.PROPRIETOR);
        }

        //房屋消息发送
//        sendNotice(projectHousePersonRelVo);
        return projectHousePersonRel;
    }

    /*需要调整-已注释和第三方相关的代码*/
    /**
     * 迁入住户
     *
     * @param projectHousePersonRelVo
     * @return
     */
    @Override
    public ProjectHousePersonRel saveRel(ProjectHousePersonRelVo projectHousePersonRelVo) {

        //检查人员是否存在，如果不存在则添加人员
        ProjectPersonInfo personInfo = projectPersonInfoService.getByTelephone(projectHousePersonRelVo.getTelephone());
        String personId = "";

        Integer userId = null;
        //查询是否存在手机账号对应的userId 更新对应用户信息
        R<SysUser> requestUser = remoteUserService.user(projectHousePersonRelVo.getTelephone());
        if (requestUser.getCode() == 0 && ObjectUtil.isNotEmpty(requestUser.getData())) {
            userId = requestUser.getData().getUserId();
        }

        if (HouseHoldTypeEnum.OWNER.code.equals(projectHousePersonRelVo.getHouseholdType()) && this.haveOwner(projectHousePersonRelVo.getHouseId())) {
            throw new RuntimeException("业主已存在");
        }
        if (personInfo == null) {
            // 人员不存在,则新增人员
            personInfo = new ProjectPersonInfo();
            personId = UUID.randomUUID().toString().replaceAll("-", "");
            BeanUtils.copyProperties(projectHousePersonRelVo, personInfo);
            personInfo.setPersonId(personId);
            personInfo.setUserId(userId);
            projectPersonInfoService.saveFromSystem(personInfo);

        } else {
            //否则，更新人员信息
            personId = personInfo.getPersonId();

            //检查该人员是否已经存在当前房屋内
            boolean havePersonInHouse = this.checkPersonExits(personId, projectHousePersonRelVo.getHouseId());
            if (havePersonInHouse) {
                throw new RuntimeException("住户已存在");
            }
            // 如果是重点人员就在重点人员信息表中添加数据（否则删除重点人员信息如果有的话）

            BeanUtils.copyProperties(projectHousePersonRelVo, personInfo);
            personInfo.setUserId(userId);
            projectPersonInfoService.updateById(personInfo);
        }
        projectHousePersonRelVo.setPersonId(personId);
        this.operateFocusAttr(projectHousePersonRelVo);

        //存储人员标签
        projectPersonLabelService.addLabel(projectHousePersonRelVo.getTagArray(), personId);

        //将人员信息植入人屋关系中
        ProjectHousePersonRel projectHousePersonRel = new ProjectHousePersonRel();
        BeanUtils.copyProperties(projectHousePersonRelVo, projectHousePersonRel);
        projectHousePersonRel.setPersonId(personId);

        //记录历史数据
        ProjectHousePersonChangeHis projectHousePersonChangeHis = new ProjectHousePersonChangeHis();
        BeanUtils.copyProperties(projectHousePersonRelVo, projectHousePersonChangeHis);

        projectHousePersonChangeHis.setAction(HouseParkingHistoryActionEnum.IN.code);
        projectHousePersonChangeHis.setPersonId(personId);
        projectHousePersonChangeHis.setHouseId(projectHousePersonRelVo.getHouseId());
        projectHousePersonChangeHis.setCheckInTime(projectHousePersonRelVo.getCheckInTime());
        projectHousePersonChangeHis.setEffTime(projectHousePersonRelVo.getRentStartTime());
        projectHousePersonChangeHis.setExpTime(projectHousePersonRelVo.getRentStopTime());
        projectHousePersonChangeHis.setPersonName(projectHousePersonRelVo.getPersonName());

        String uid = UUID.randomUUID().toString().replaceAll("-", "");
        projectHousePersonRel.setRelaId(uid);

        projectHousePersonChangeHisService.save(projectHousePersonChangeHis);

        ProjectPersonAttrFormVo projectPersonAttrFormVo = new ProjectPersonAttrFormVo();
        projectPersonAttrFormVo.setProjectPersonAttrList(projectHousePersonRelVo.getProjectPersonAttrList());
        projectPersonAttrFormVo.setPersonId(personId);
        projectPersonAttrFormVo.setProjectId(ProjectContextHolder.getProjectId());
        projectPersonAttrFormVo.setType(PersonTypeEnum.PROPRIETOR.code);
        projectPersonAttrService.updatePersonAttrList(projectPersonAttrFormVo);

        projectHousePersonRel.setAuditStatus(AuditStatusEnum.pass.code);
        /**
         * 支持对接第三方业务
         * @since 2020-12-14
         * @author: 王伟
         */
        abstractWebProjectHousePersonRelService.save(projectHousePersonRel);
//        this.save(projectHousePersonRel);

//        /**
//         * 对接WR20
//         * @since 2020-08-07
//         * @author: 王伟
//         */
//        WR20Factory.getFactoryInstance().getPersonService(ProjectContextHolder.getProjectId()).addPerson(ProjectContextHolder.getProjectId(), projectHousePersonRel);

        /**
         * 房屋变动，应改变权限配置
         * @since: 2020-09-16
         * @author: 王伟
         *
         */
        projectPersonDeviceService.refreshByPersonId(personId, PersonTypeEnum.PROPRIETOR);

        certMultiUser(personId);
        return projectHousePersonRel;
    }


    public void certMultiUser(String personId){
        try {
            //按设备分组的凭证列表
            Map<String,List<ProjectRightDevice>> map = new HashMap<>();
            //当前住户所有下发成功的凭证数据
            List<ProjectRightDevice> projectRightDeviceList = projectRightDeviceService.getCertMultiUserList(personId);
            if(CollUtil.isEmpty(projectRightDeviceList)){
                return;
            }
            Set<String> deviceIdSet = projectRightDeviceList.stream().map(ProjectRightDevice::getDeviceId).collect(Collectors.toSet());
            deviceIdSet.forEach(deviceId->{
                map.put(deviceId,projectRightDeviceList.stream().filter(projectRightDevice -> projectRightDevice.getDeviceId().equals(deviceId)).collect(Collectors.toList()));
            });
            for (Map.Entry<String, List<ProjectRightDevice>> entry : map.entrySet()) {
                String deviceId = entry.getKey();
                List<ProjectRightDevice> value = entry.getValue();
                ProjectDeviceInfo projectDeviceInfo = projectDeviceInfoService.getById(deviceId);
                value.forEach(e->{
                    projectDeviceInfoService.sendCertAndHouseRelation(projectDeviceInfo,e);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("下发一凭证多户出现异常 人员Id:{}",personId);
        }
    }

    /*需要调整-已注释第三方相关代码*/
    /**
     * 微信迁入住户
     *
     * @param projectHousePersonRelVo
     * @return
     */
    @Override
    public ProjectHousePersonRel wechatSaveRel(ProjectHousePersonRelVo projectHousePersonRelVo) {
        //当前房屋下审核成功的所有人员集合
        List<ProjectHousePersonRelVo> list = baseMapper.getHousePersonRel(projectHousePersonRelVo.getHouseId());
        if (CollUtil.isNotEmpty(list)) {
            for (ProjectHousePersonRelVo projectPersonInfo : list) {
                //判断是否存在同名或者相同手机号的住户
                if (("1".equals(projectHousePersonRelVo.getIsReplace()) &&
                        projectPersonInfo.getPersonName().equals(projectHousePersonRelVo.getPersonName())) ||
                        projectPersonInfo.getTelephone().equals(projectHousePersonRelVo.getTelephone()) ||
                        ("1".equals(projectPersonInfo.getHouseholdType()) &&
                                "1".equals(projectHousePersonRelVo.getHouseholdType()))) {
                    //迁出该房屋姓名相同的租客/家属人屋关系
                    removeHousePersonRelById(projectPersonInfo.getRelaId());
                }
            }
        }

        String personId = projectHousePersonRelVo.getPersonId();

        //检查该人员是否已经存在当前房屋内
        boolean havePersonInHouse = this.checkPersonExits(personId, projectHousePersonRelVo.getHouseId());
        if (havePersonInHouse) {
            throw new RuntimeException("该房屋下已有此用户，请前往WEB端进行编辑");
        }
        ProjectPersonInfo personInfo = new ProjectPersonInfo();
        BeanUtils.copyProperties(projectHousePersonRelVo, personInfo);
        projectPersonInfoService.updateById(personInfo);

        this.operateFocusAttr(projectHousePersonRelVo);

        //存储人员标签
        projectPersonLabelService.addLabel(projectHousePersonRelVo.getTagArray(), personId);

        //将人员信息植入人屋关系中
        ProjectHousePersonRel projectHousePersonRel = new ProjectHousePersonRel();
        BeanUtils.copyProperties(projectHousePersonRelVo, projectHousePersonRel);
        projectHousePersonRel.setPersonId(personId);

        //记录历史数据
        ProjectHousePersonChangeHis projectHousePersonChangeHis = new ProjectHousePersonChangeHis();
        BeanUtils.copyProperties(projectHousePersonRelVo, projectHousePersonChangeHis);

        projectHousePersonChangeHis.setAction(HouseParkingHistoryActionEnum.IN.code);
        projectHousePersonChangeHis.setPersonId(personId);
        projectHousePersonChangeHis.setHouseId(projectHousePersonRelVo.getHouseId());
        projectHousePersonChangeHis.setCheckInTime(projectHousePersonRelVo.getCheckInTime());
        projectHousePersonChangeHis.setEffTime(projectHousePersonRelVo.getRentStartTime());
        projectHousePersonChangeHis.setExpTime(projectHousePersonRelVo.getRentStopTime());
        projectHousePersonChangeHis.setPersonName(projectHousePersonRelVo.getPersonName());

        String uid = UUID.randomUUID().toString().replaceAll("-", "");
        projectHousePersonRel.setRelaId(uid);

        projectHousePersonChangeHisService.save(projectHousePersonChangeHis);

        ProjectPersonAttrFormVo projectPersonAttrFormVo = new ProjectPersonAttrFormVo();
        projectPersonAttrFormVo.setProjectPersonAttrList(projectHousePersonRelVo.getProjectPersonAttrList());
        projectPersonAttrFormVo.setPersonId(personId);
        projectPersonAttrFormVo.setProjectId(ProjectContextHolder.getProjectId());
        projectPersonAttrFormVo.setType(PersonTypeEnum.PROPRIETOR.code);
        projectPersonAttrService.updatePersonAttrList(projectPersonAttrFormVo);

        projectHousePersonRel.setAuditStatus(AuditStatusEnum.pass.code);
        /**
         * 支持对接第三方业务
         * @since 2020-12-14
         * @author: 王伟
         */
        abstractWebProjectHousePersonRelService.save(projectHousePersonRel);
//        this.save(projectHousePersonRel);

//        /**
//         * 对接WR20
//         * @since 2020-08-07
//         * @author: 王伟
//         */
//        WR20Factory.getFactoryInstance().getPersonService(ProjectContextHolder.getProjectId()).addPerson(ProjectContextHolder.getProjectId(), projectHousePersonRel);

        /**
         * 房屋变动，应改变权限配置
         * @since: 2020-09-16
         * @author: 王伟
         *
         */
        projectPersonDeviceService.refreshByPersonId(personId, PersonTypeEnum.PROPRIETOR);
        return projectHousePersonRel;
    }

    /*无需调整*/
    /**
     * 根据第三方ID,保存或更新住户信息
     *
     * @param projectHousePersonRelVo
     * @return
     * @author: 王伟
     * @since: 2020-12-23 18:08
     */
    @Override
    public String saveOrUpdateRelByWR20(ProjectHousePersonRelVo projectHousePersonRelVo) {
        //查询是否已存在
        List<ProjectHousePersonRel> housePersonRelList = this.list(new QueryWrapper<ProjectHousePersonRel>().lambda().eq(ProjectHousePersonRel::getRelaCode, projectHousePersonRelVo.getRelaCode()));
        ProjectPersonInfo personInfo = projectPersonInfoService.getByTelephone(projectHousePersonRelVo.getTelephone());


        //第三方人员和住户关系进行绑定，无论是否存在人员，均在4.0中添加一个新的人员。
        String personId = "";
        Integer userId = null;

        //personInfo校验
        if (personInfo == null) {
            // 人员不存在,则新增人员，并分配账号
            personInfo = new ProjectPersonInfo();
            personId = UUID.randomUUID().toString().replaceAll("-", "");
            BeanUtils.copyProperties(projectHousePersonRelVo, personInfo);
            personInfo.setPersonId(personId);
            personInfo.setUserId(userId);
            projectPersonInfoService.saveFromSystem(personInfo);

            //手机号校验
            if (StringUtils.isNotEmpty(projectHousePersonRelVo.getTelephone())) {
                //查询是否存在手机账号对应的userId 更新对应用户信息
                R<UserInfo> requestUser = remoteUserService.info(projectHousePersonRelVo.getTelephone(), SecurityConstants.FROM_IN);
                if (requestUser.getCode() == 0 && ObjectUtil.isNotEmpty(requestUser.getData())) {
                    userId = requestUser.getData().getSysUser().getUserId();
                } else {
                    UserDTO userDto = new UserDTO();
                    userDto.setDeptId(SysDeptConstant.AURINE_ID);
                    userDto.setRole(Lists.newArrayList(5));//TODO: 默认角色暂定为5
                    userDto.setPhone(projectHousePersonRelVo.getTelephone());
                    userDto.setUsername(projectHousePersonRelVo.getTelephone());
                    userDto.setPassword("123456");
//                        remoteUserService.save(userDto,SecurityConstants.FROM_IN);
                    R<Integer> user = remoteUserService.saveUserRetId(userDto, SecurityConstants.FROM_IN);
                    userId = user.getData();
                }

            }
        } else {
            //如果人员已经存在，同名、同手机号的人员不再保存手机号、不再配置userID,同时更新人员信息
            projectHousePersonRelVo.setTelephone(null);
            personId = personInfo.getPersonId();
            personInfo.setPersonName(projectHousePersonRelVo.getPersonName());
            projectPersonInfoService.updateById(personInfo);
        }


        //住户信息保存或更新
        if (CollUtil.isNotEmpty(housePersonRelList)) {
            //更新住户信息
            ProjectHousePersonRel housePersonRel = housePersonRelList.get(0);
            housePersonRel.setPersonId(personId);
            this.updateById(housePersonRel);
            return personId;
        } else {
            //保存新的住户信息
            projectHousePersonRelVo.setPersonId(personId);

            //将人员信息植入人屋关系中
            ProjectHousePersonRel projectHousePersonRel = new ProjectHousePersonRel();
            BeanUtils.copyProperties(projectHousePersonRelVo, projectHousePersonRel);
            projectHousePersonRel.setPersonId(personId);

            //记录历史数据
            ProjectHousePersonChangeHis projectHousePersonChangeHis = new ProjectHousePersonChangeHis();
            BeanUtils.copyProperties(projectHousePersonRelVo, projectHousePersonChangeHis);

            projectHousePersonChangeHis.setAction(HouseParkingHistoryActionEnum.IN.code);
            projectHousePersonChangeHis.setPersonId(personId);
            projectHousePersonChangeHis.setHouseId(projectHousePersonRelVo.getHouseId());
            projectHousePersonChangeHis.setCheckInTime(projectHousePersonRelVo.getCheckInTime());
            projectHousePersonChangeHis.setEffTime(projectHousePersonRelVo.getRentStartTime());
            projectHousePersonChangeHis.setExpTime(projectHousePersonRelVo.getRentStopTime());
            projectHousePersonChangeHis.setPersonName(projectHousePersonRelVo.getPersonName());

            String uid = UUID.randomUUID().toString().replaceAll("-", "");
            projectHousePersonRel.setRelaId(uid);
            projectHousePersonRel.setAuditStatus(AuditStatusEnum.pass.code);

            this.save(projectHousePersonRel);
            projectHousePersonChangeHisService.save(projectHousePersonChangeHis);

            return personId;
        }

    }

    /*需要调整-已注释云对讲（增值服务）相关代码*/
    @Override
    public boolean passAll(List<String> relaIds) {
        List<ProjectHousePersonRel> projectHousePersonRels = this.listByIds(relaIds);
        for (ProjectHousePersonRel projectHousePersonRel : projectHousePersonRels) {
            ProjectHousePersonRelVo projectHousePersonRelVo = this.getVoById(projectHousePersonRel.getRelaId());
            projectHousePersonRelVo.setAuditStatus(AuditStatusEnum.pass.code);
            ProjectHousePersonRel housePersonRel = this.verify(projectHousePersonRelVo);
            BeanUtils.copyProperties(projectHousePersonRelVo, projectHousePersonRel);
            this.updateById(projectHousePersonRel);
            //当前房屋存在的增值服务
            /*List<ProjectHouseServiceInfoVo> projectHouseServiceInfoVos = projectHouseServiceService.getHouseServiceByHouseId(housePersonRel.getHouseId());
            projectHouseServiceInfoVos.forEach(e -> {
                IntercomFactoryProducer.getFactory(e.getServiceCode()).getIntercomService().addPerson(housePersonRel, ProjectContextHolder.getProjectId());
            });*/
        }
        Set<String> personIdSet = projectHousePersonRels.stream().map(ProjectHousePersonRel::getPersonId).collect(Collectors.toSet());
        /*
         * 房屋变动，应改变权限配置
         * @since: 2020-09-16
         *
         */
        personIdSet.forEach(personId -> {
            projectPersonDeviceService.refreshByPersonId(personId, PersonTypeEnum.PROPRIETOR);
        });

        return true;
    }


    /*无需调整*/
    @Override
    public List<ProjectHousePersonRelVo> saveRelBatch(List<ProjectHousePersonRelVo> projectHousePersonRelVoList) {
        // 这里获取到的人屋关系列表以及是新建完住户后并且在对象中存入住户ID的列表
        // 这里的传入的人屋关系已经在导入Excel的时候进行了过滤，排除掉了哪些已经在房屋下的情况所以无需对是否在房屋下进行判断
        List<ProjectHousePersonRelVo> housePersonRelVoList = projectPersonInfoService.initHousePersonRelPersonId(projectHousePersonRelVoList);
        // 对重点人员信息进行批量处理
        this.operateFocusAttr(housePersonRelVoList);
        List<ProjectHousePersonRel> housePersonRelList = new ArrayList<>();
        List<ProjectHousePersonChangeHis> housePersonChangeHisList = new ArrayList<>();
        List<ProjectPersonAttrFormVo> personAttrFormVoList = new ArrayList<>();
        projectHousePersonRelVoList.forEach(housePersonRelVo -> {
            ProjectHousePersonRel housePersonRel = new ProjectHousePersonRel();
            BeanUtils.copyProperties(housePersonRelVo, housePersonRel);

            housePersonRel.setPersonId(housePersonRelVo.getPersonId());
            String uid = UUID.randomUUID().toString().replaceAll("-", "");
            housePersonRel.setRelaId(uid);
            housePersonRel.setAuditStatus(AuditStatusEnum.pass.code);
            housePersonRelList.add(housePersonRel);

            ProjectHousePersonChangeHis housePersonChangeHis = new ProjectHousePersonChangeHis();
            housePersonChangeHis.setAction(HouseParkingHistoryActionEnum.IN.code);
            housePersonChangeHis.setPersonId(housePersonRelVo.getPersonId());
            housePersonChangeHis.setHouseId(housePersonRelVo.getHouseId());
            housePersonChangeHis.setCheckInTime(housePersonRelVo.getCheckInTime());
            housePersonChangeHis.setEffTime(housePersonRelVo.getRentStartTime());
            housePersonChangeHis.setExpTime(housePersonRelVo.getRentStopTime());
            housePersonChangeHis.setPersonName(housePersonRelVo.getPersonName());
            housePersonChangeHisList.add(housePersonChangeHis);

            ProjectPersonAttrFormVo projectPersonAttrFormVo = new ProjectPersonAttrFormVo();
            projectPersonAttrFormVo.setProjectPersonAttrList(housePersonRelVo.getProjectPersonAttrList());
            projectPersonAttrFormVo.setPersonId(housePersonRelVo.getPersonId());
            projectPersonAttrFormVo.setProjectId(ProjectContextHolder.getProjectId());
            projectPersonAttrFormVo.setType(PersonTypeEnum.PROPRIETOR.code);
            personAttrFormVoList.add(projectPersonAttrFormVo);
        });

        if (CollUtil.isNotEmpty(housePersonChangeHisList)) {
            projectHousePersonChangeHisService.saveBatch(housePersonChangeHisList);
        }
        projectPersonAttrService.updatePersonAttrList(personAttrFormVoList);
        if (CollUtil.isNotEmpty(housePersonRelList)) {
            this.saveBatch(housePersonRelList);
        }
        /*
         * 房屋变动，应改变权限配置
         * @since: 2020-09-16
         */
        Integer projectId = ProjectContextHolder.getProjectId();
        threadPoolTaskExecutor.execute(() -> {
            // 实时性要求不高的都放在这里执行
            ProjectContextHolder.setProjectId(projectId);

            Set<String> personIdSet = housePersonRelList.stream().map(ProjectHousePersonRel::getPersonId).collect(Collectors.toSet());
            personIdSet.forEach(personId -> {
                projectPersonDeviceService.refreshByPersonId(personId, PersonTypeEnum.PROPRIETOR);
                certMultiUser(personId);
            });

            WR20Factory.getFactoryInstance().getPersonService(ProjectContextHolder.getProjectId())
                    .addPersonBatch(ProjectContextHolder.getProjectId(), housePersonRelList);
        });
        return projectHousePersonRelVoList;

    }

    /*无需调整*/
    /**
     * 根据人员id，获取该人员的所有住户关系
     *
     * @param personId
     * @return
     */
    @Override
    public List<ProjectHousePersonRel> listHousePersonByPersonId(String personId) {
        return this.list(new QueryWrapper<ProjectHousePersonRel>().lambda().eq(ProjectHousePersonRel::getPersonId, personId));
    }

    /*需要调整-因为消息通知已注释整个方法*/
    /*private void sendNotice(ProjectHousePersonRelVo projectHousePersonRelVo) {
        try {
            ProjectHouseAddressVo projectHouseAddressVo = baseMapper.getAddress(projectHousePersonRelVo.getHouseId());
            List<String> address = getAddress(projectHouseAddressVo.getAddress());

            Date date = new Date();
            String time = DateFormatUtils.format(date.getTime(), "yyyy年MM月dd日 HH时mm分");

            String sendFor = projectHousePersonRelVo.getPersonId();
            ProjectHousePersonRel projectHousePersonRel = this.getById(projectHousePersonRelVo.getRelaId());
            if (ObjectUtil.isNotEmpty(projectHousePersonRel.getRemark())) {
                if (!projectHousePersonRelVo.getPersonId().equals(projectHousePersonRel.getRemark())) {
                    sendFor = projectHousePersonRel.getRemark();
                }
            }
            String title = "审核结果通知";
            String content = "";

            ProjectPersonInfo projectPersonInfo = projectPersonInfoService.
                    getOne(Wrappers.lambdaQuery(ProjectPersonInfo.class).eq(ProjectPersonInfo::getPersonId, projectHousePersonRelVo.getPersonId()));
            boolean bool = StringUtils.isEmpty(projectPersonInfo.getRemark());

            if (AuditStatusEnum.pass.code.equals(projectHousePersonRelVo.getAuditStatus())) {
                if (bool) {
                    content = "您申请入住" + address.get(0) + ",已通过审核" + "<br/>" +
                            "审核结果：已通过" + "<br/>" +
                            "审核时间：" + time + "" + "<br/>" +
                            "备注：";
                } else {
                    if (HouseHoldTypeEnum.FAMILY.code.equals(projectHousePersonRelVo.getHouseholdType())) {
                        content = "您申请的家属" + projectHousePersonRelVo.getPersonName() + "，入住" + address.get(0) + ",已通过审核" + "<br/>" +
                                "审核结果：已通过" + "<br/>" +
                                "审核时间：" + time + "" + "<br/>" +
                                "备注：";
                    }
                    if (HouseHoldTypeEnum.TENANT.code.equals(projectHousePersonRelVo.getHouseholdType())) {
                        content = "您申请的租客" + projectHousePersonRelVo.getPersonName() + "，入住" + address.get(0) + ",已通过审核" + "<br/>" +
                                "审核结果：已通过" + "<br/>" +
                                "审核时间：" + time + "" + "<br/>" +
                                "备注：";
                    }
                }
            }
            if (AuditStatusEnum.notPass.code.equals(projectHousePersonRelVo.getAuditStatus())) {
                if (bool) {
                    content = "您申请入住 " + address.get(0) + " ,未通过审核" + "<br/>" +
                            "审核结果：未通过" + "<br/>" +
                            "审核时间：" + time + "" + "<br/>" +
                            "备注：" + (StrUtil.isEmpty(projectHousePersonRelVo.getAuditReason())
                            ? "" : "" + projectHousePersonRelVo.getAuditReason());
                } else {
                    if (HouseHoldTypeEnum.FAMILY.code.equals(projectHousePersonRelVo.getHouseholdType())) {
                        content = "您申请的家属" + projectHousePersonRelVo.getPersonName() + "，入住" + address.get(0) + ",未通过审核" + "<br/>" +
                                "审核结果：未通过" + "<br/>" +
                                "审核时间：" + time + "<br/>" +
                                "备注：" + (StrUtil.isEmpty(projectHousePersonRelVo.getAuditReason())
                                ? "" : projectHousePersonRelVo.getAuditReason());
                    }
                    if (HouseHoldTypeEnum.TENANT.code.equals(projectHousePersonRelVo.getHouseholdType())) {
                        content = "您申请的租客" + projectHousePersonRelVo.getPersonName() + "，入住" + address.get(0) + ",未通过审核" + "<br/>" +
                                "审核结果：未通过" + "<br/>" +
                                "审核时间：" + time + "<br/>" +
                                "备注：" + (StrUtil.isEmpty(projectHousePersonRelVo.getAuditReason())
                                ? "" : projectHousePersonRelVo.getAuditReason());
                    }


                }
            }
            noticeUtil.send(false, title, content, sendFor);
        } catch (
                Exception e) {
            log.warn("消息发送异常", e);
        }

    }*/

    /*无需调整*/
    /**
     * 处理房屋地址的方法
     *
     * @param oldAddress
     * @return
     */
    @Override
    public List<String> getAddress(String oldAddress) {
        int one = oldAddress.lastIndexOf("-");
        int two = oldAddress.lastIndexOf("-", one - 1);
        int three = oldAddress.lastIndexOf("-", two - 1);

        int length = oldAddress.length() - (oldAddress.replaceAll("-", "")).length();

        String newAddress;
        StringBuilder sb = new StringBuilder(oldAddress);
        if (length >= 3) {
            newAddress = sb.replace(one, one + 1, "").replace(two, two + 1, "").replace(three, three + 1, " ").toString();
        } else {
            newAddress = sb.replace(one, one + 1, "").replace(two, two + 1, "").toString();
        }
        Integer projectId = ProjectContextHolder.getProjectId();
        String projectName = baseMapper.getProjectName(projectId);
        String address = projectName + " " + newAddress;
        String house = oldAddress.substring(one + 1);
        List<String> list = new ArrayList<>();
        list.add(address);
        list.add(house);
        return list;
    }

    /*需要调整-已注释增值服务相关代码*/
    /**
     * 审核迁出住户
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean verifyRemoveHousePersonRelById(String id) {

//        projectHousePersonRelMapper.delete(new QueryWrapper<ProjectHousePersonRel>().lambda().eq(ProjectHousePersonRel::getRelaId, id));

        //获取住户信息
        ProjectHousePersonRel projectHousePersonRel = this.getById(id);

        //住户完全迁出，才清理该用户通行凭证
        int houseCount = this.count(new QueryWrapper<ProjectHousePersonRel>().lambda().eq(ProjectHousePersonRel::getPersonId, projectHousePersonRel.getPersonId()));

        if (houseCount <= 1) {
            //迁出介质权限
            projectRightDeviceService.removeCertDeviceAuthorize(projectHousePersonRel.getPersonId());
        }


        //记录历史数据
        ProjectHousePersonChangeHis projectHousePersonChangeHis = new ProjectHousePersonChangeHis();
        BeanUtils.copyProperties(projectHousePersonRel, projectHousePersonChangeHis);
        projectHousePersonChangeHis.setAction(HouseParkingHistoryActionEnum.VERIFYOUT.code);
        projectHousePersonChangeHis.setEffTime(projectHousePersonRel.getRentStartTime());
        projectHousePersonChangeHis.setExpTime(projectHousePersonRel.getRentStopTime());
        projectHousePersonChangeHis.setPersonId(projectHousePersonRel.getPersonId());

        ProjectPersonInfo personInfo = projectPersonInfoService.getOne(Wrappers.lambdaQuery(ProjectPersonInfo.class)
                .eq(ProjectPersonInfo::getPersonId, projectHousePersonRel.getPersonId()));
        if(personInfo !=null){
            projectHousePersonChangeHis.setPersonName(personInfo.getPersonName());
        }
        projectHousePersonChangeHis.setPersonId(projectHousePersonRel.getPersonId());
        projectHousePersonChangeHisService.save(projectHousePersonChangeHis);


        /*List<ProjectHouseServiceInfoVo> projectHouseServiceInfoVos = projectHouseServiceService.getHouseServiceByHouseId(projectHousePersonRel.getHouseId());
        projectHouseServiceInfoVos.forEach(e -> {
            //移除远端的该住户在此房屋的增值服务
            IntercomFactoryProducer.getFactory(e.getServiceCode()).getIntercomService().delPerson(id);
        });*/

        //删除关系数据
        boolean result = abstractWebProjectHousePersonRelService.removeById(id);
//        //删除拓展属性
//        projectPersonAttrService.removePersonAttrList(id);
//        /**
//         * 对接WR20
//         * @since 2020-08-07
//         * @author: 王伟
//         */
//        WR20Factory.getFactoryInstance().getPersonService(ProjectContextHolder.getProjectId()).delPerson(ProjectContextHolder.getProjectId(), projectHousePersonRel);

        /**
         * 房屋变动，应改变权限配置
         * @since: 2020-09-16
         * @author: 王伟
         *
         */
        projectPersonDeviceService.refreshByPersonId(projectHousePersonRel.getPersonId(), PersonTypeEnum.PROPRIETOR);


        return result;
    }

    /*需要调整-已注释增值服务相关代码*/
    /**
     * 迁出住户
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeHousePersonRelById(String id) {
        int count = edgeCloudRequestService.count(Wrappers.lambdaQuery(EdgeCloudRequest.class)
                .eq(EdgeCloudRequest::getProjectId, ProjectContextHolder.getProjectId())
                .in(EdgeCloudRequest::getCloudStatus, IntoCloudStatusEnum.INTO_CLOUD.code, IntoCloudStatusEnum.UNBINDING.code));
        if(count > 0) {
            OpenApiEntity openApiEntity = new OpenApiEntity();
            openApiEntity.setServiceType(OpenPushSubscribeCallbackTypeEnum.COMMAND.name);
            openApiEntity.setCommandType(OpenApiCommandTypeEnum.DELETE_HOUSE_PERSON.name);
            openApiEntity.setServiceName(OpenApiServiceNameEnum.HOUSE_PERSON_INFO.name);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("uuid",id);
            openApiEntity.setData(jsonObject);
            openApiMessageService.sendOpenApiMessage(openApiEntity);
        }


//        projectHousePersonRelMapper.delete(new QueryWrapper<ProjectHousePersonRel>().lambda().eq(ProjectHousePersonRel::getRelaId, id));

        //获取住户信息
        ProjectHousePersonRel projectHousePersonRel = this.getById(id);

        //住户完全迁出，才清理该用户通行凭证
        int houseCount = this.countHouseByPersonId(projectHousePersonRel.getPersonId());

        if (houseCount <= 1) {
            //迁出介质权限
            projectRightDeviceService.removeCertDeviceAuthorize(projectHousePersonRel.getPersonId());
            projectPersonPlanRelService.deleteByPersonId(projectHousePersonRel.getPersonId());
        }


        //记录历史数据
        ProjectHousePersonChangeHis projectHousePersonChangeHis = new ProjectHousePersonChangeHis();
        BeanUtils.copyProperties(projectHousePersonRel, projectHousePersonChangeHis);
        projectHousePersonChangeHis.setAction(HouseParkingHistoryActionEnum.OUT.code);
        projectHousePersonChangeHis.setEffTime(projectHousePersonRel.getRentStartTime());
        projectHousePersonChangeHis.setExpTime(projectHousePersonRel.getRentStopTime());
        projectHousePersonChangeHis.setPersonId(projectHousePersonRel.getPersonId());

        ProjectPersonInfo personInfo = projectPersonInfoService.getOne(Wrappers.lambdaQuery(ProjectPersonInfo.class)
                .eq(ProjectPersonInfo::getPersonId, projectHousePersonRel.getPersonId()));
        if(personInfo !=null){
            projectHousePersonChangeHis.setPersonName(personInfo.getPersonName());
        }

        projectHousePersonChangeHisService.save(projectHousePersonChangeHis);
        /*List<ProjectHouseServiceInfoVo> projectHouseServiceInfoVos = projectHouseServiceService.getHouseServiceByHouseId(projectHousePersonRel.getHouseId());

        if (CollUtil.isNotEmpty(projectHouseServiceInfoVos)){
            log.info("开始删除增值服务"+projectHouseServiceInfoVos+"{}房屋id:"+projectHousePersonRel.getHouseId());
            projectHouseServiceInfoVos.forEach(e -> {
                //移除远端的该住户在此房屋的增值服务
                IntercomFactoryProducer.getFactory(e.getServiceCode()).getIntercomService().delPerson(id);
            });
        }*/

        //删除关系数据
        boolean result = abstractWebProjectHousePersonRelService.removeById(id);
//        //删除拓展属性
//        projectPersonAttrService.removePersonAttrList(id);
//        /**
//         * 对接WR20
//         * @since 2020-08-07
//         * @author: 王伟
//         */
//        WR20Factory.getFactoryInstance().getPersonService(ProjectContextHolder.getProjectId()).delPerson(ProjectContextHolder.getProjectId(), projectHousePersonRel);

        /**
         * 房屋变动，应改变权限配置
         * @since: 2020-09-16
         * @author: 王伟
         *
         */
        projectPersonDeviceService.refreshByPersonId(projectHousePersonRel.getPersonId(), PersonTypeEnum.PROPRIETOR);

        certMultiUser(projectHousePersonRel.getPersonId());
        return result;
    }


    /*需要调整-已注释增值服务相关代码*/
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean removeAll(List<String> relaIds) {
//        List<ProjectHousePersonRel> housePersonRelList = this.listByIds(relaIds);
//
//
//        List<ProjectHousePersonChangeHis> projectHousePersonChangeHisList = new ArrayList<>();
//        List<String> personIdList = new ArrayList<>();
//        housePersonRelList.forEach(projectHousePersonRel -> {
//            /*List<ProjectHouseServiceInfoVo> projectHouseServiceInfoVos = projectHouseServiceService.getHouseServiceByHouseId(projectHousePersonRel.getHouseId());
//            projectHouseServiceInfoVos.forEach(e -> {
//                //移除远端增值服务
//                IntercomFactoryProducer.getFactory(e.getServiceCode()).getIntercomService().delPerson(projectHousePersonRel.getRelaId());
//            });*/
//            ProjectHousePersonChangeHis projectHousePersonChangeHis = new ProjectHousePersonChangeHis();
//            BeanUtils.copyProperties(projectHousePersonRel, projectHousePersonChangeHis);
//            projectHousePersonChangeHis.setAction(HouseParkingHistoryActionEnum.OUT.code);
//            projectHousePersonChangeHis.setEffTime(projectHousePersonRel.getRentStartTime());
//            projectHousePersonChangeHis.setExpTime(projectHousePersonRel.getRentStopTime());
//            projectHousePersonChangeHis.setPersonId(projectHousePersonRel.getPersonId());
//            projectHousePersonChangeHisList.add(projectHousePersonChangeHis);
//            personIdList.add(projectHousePersonRel.getPersonId());
//        });
//        //迁出介质权限
//        projectRightDeviceService.removeCertDeviceAuthorize(personIdList);
//
//        /**
//         * 房屋变动，应改变权限配置
//         * @since: 2020-09-16
//         *
//         */
//        personIdList.forEach(personId -> {
//            projectPersonDeviceService.refreshByPersonId(personId, PersonTypeEnum.PROPRIETOR);
//        });
//
//        projectHousePersonChangeHisService.saveBatch(projectHousePersonChangeHisList);
//
//        return this.remove(new QueryWrapper<ProjectHousePersonRel>().lambda().in(ProjectHousePersonRel::getRelaId, relaIds));
        relaIds.forEach(id -> this.removeHousePersonRelById(id));
        return true;
    }

    /*需要调整-已注释增值服务相关代码*/
    @Override
    public R importExcel(MultipartFile file, String type) {
        HousePersonRelExcelEnum housePersonRelExcelEnum = HousePersonRelExcelEnum.getEnum(type);
        ExcelResultVo excelResultVo = new ExcelResultVo();
        try {
            if (housePersonRelExcelEnum == null) {
                return R.failed("不存在该类型的文件");
            }
            EasyExcel.read(file.getInputStream(), HousePersonRelExcel.class,
                    new ProjectHousePersonRelListener<HousePersonRelExcel>(projectEntityLevelCfgService.checkIsEnabled(), projectFrameInfoService,
                            this, projectPersonInfoService,
                            housePersonRelExcelEnum, excelResultVo, redisTemplateAurine)).sheet().doRead();
        } catch (IOException e) {
            return R.failed("文件读取异常");
        } catch (Exception e) {
            throw e;
//            return R.failed(e.getMessage());
        }
        //对接远端住户增值服务
        /*List<ProjectHousePersonRelVo> projectHousePersonRelVos = excelResultVo.getProjectHousePersonRelVos();

        if (CollUtil.isNotEmpty(projectHousePersonRelVos)) {
            projectHousePersonRelVos.forEach(housePersonRelVo -> {
                ProjectHousePersonRel housePersonRel = new ProjectHousePersonRel();
                BeanUtils.copyProperties(housePersonRelVo, housePersonRel);
                List<ProjectHouseServiceInfoVo> projectHouseServiceInfoVos = projectHouseServiceService.getHouseServiceByHouseId(housePersonRel.getHouseId());
                projectHouseServiceInfoVos.forEach(e -> {
                    //新增远端增值服务
                    IntercomFactoryProducer.getFactory(e.getServiceCode()).getIntercomService().addPerson(housePersonRel, ProjectContextHolder.getProjectId());
                });
            });
        }*/

        return R.ok(excelResultVo);
    }

    /*无需调整*/
    @Override
    public void errorExcel(String name, HttpServletResponse httpServletResponse) throws IOException {
        String dataString = redisTemplateAurine.opsForValue().get(name);
        String[] keys = name.split("-");
        HousePersonRelExcelEnum housePersonRelExcelEnum = HousePersonRelExcelEnum.getEnum(keys[0]);
        List data = JSONUtil.toList(JSONUtil.parseArray(dataString), housePersonRelExcelEnum.getClazz());
        String excelPath = DeviceExcelConstant.XLSX_PATH + housePersonRelExcelEnum.getName();
        ClassPathResource classPathResource = new ClassPathResource(excelPath);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        String fileName = "失败名单:" + housePersonRelExcelEnum.getName();
        httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        EasyExcel.write(httpServletResponse.getOutputStream(), housePersonRelExcelEnum.getClazz())
                .withTemplate(classPathResource.getStream()).sheet(0).doFill(data);
    }

    /*无需调整*/
    @Override
    public void modelExcel(String type, HttpServletResponse httpServletResponse) throws IOException {
        HousePersonRelExcelEnum housePersonRelExcelEnum = HousePersonRelExcelEnum.getEnum(type);
        List<HousePersonRelExcel> data = new ArrayList<>();
        HousePersonRelExcel housePersonRelExcel = new HousePersonRelExcel();
        housePersonRelExcel.setBuildingName("");
        data.add(housePersonRelExcel);
        String excelPath = DeviceExcelConstant.XLSX_PATH + housePersonRelExcelEnum.getName();
        ClassPathResource classPathResource = new ClassPathResource(excelPath);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        String fileName = housePersonRelExcelEnum.getName();
        httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        EasyExcel.write(httpServletResponse.getOutputStream(), housePersonRelExcelEnum
                .getClazz()).withTemplate(classPathResource.getStream()).sheet(0).doFill(data);
    }

    /*无需调整*/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByRelId(String id) {
        //获取住户信息
        ProjectHousePersonRel projectHousePersonRel = this.getById(id);
        if (AuditStatusEnum.pass.equals(projectHousePersonRel.getAuditStatus())) {
            throw new ValidateCodeException("已审核通过无法直接取消申请");
        }
        //删除关系数据
        this.removeById(id);
        //删除拓展属性
        projectPersonAttrService.removePersonAttrList(id);

        /**
         * 房屋变动，应改变权限配置
         * @since: 2020-09-16
         * @author: 王伟
         *
         */
        projectPersonDeviceService.refreshByPersonId(projectHousePersonRel.getPersonId(), PersonTypeEnum.PROPRIETOR);

    }


    /*无需调整*/
    /**
     * 查询住户
     *
     * @param page
     * @param searchConditionVo 查询条件
     * @return
     */
    @Override
    public IPage<ProjectHousePersonRelRecordVo> findPage(IPage<ProjectHousePersonRelRecordVo> page, ProjectHousePersonRelSearchConditionVo searchConditionVo) {
        // 查询住户信息，如果是租客，且租赁时间到期，则不显示。
        // 数据采用伪删除，0迁出，1迁入
        return projectHousePersonRelMapper.select(
                page,
                searchConditionVo.getBuildingName(),
                searchConditionVo.getUnitName(),
                searchConditionVo.getHouseName(),
                searchConditionVo.getPersonName(),
                searchConditionVo.getBuildingId(),
                searchConditionVo.getUnitId(),
                searchConditionVo.getHouseId(),
                searchConditionVo.getAuditStatus(),
                searchConditionVo.getHousePeopleRel(),
                searchConditionVo.getPersonId(),
                ProjectContextHolder.getProjectId()
        );
    }

    /*无需调整*/
    @Override
    public IPage<ProjectHousePersonRelRecordVo> findPageAll(IPage<ProjectHousePersonRelRecordVo> page, ProjectHousePersonRelSearchConditionVo searchConditionVo) {
        // 查询住户信息，如果是租客，且租赁时间到期，则不显示。
        // 数据采用伪删除，0迁出，1迁入
        return projectHousePersonRelMapper.selectAll(
                page,
                searchConditionVo.getBuildingName(),
                searchConditionVo.getUnitName(),
                searchConditionVo.getHouseName(),
                searchConditionVo.getPersonName(),
                searchConditionVo.getBuildingId(),
                searchConditionVo.getUnitId(),
                searchConditionVo.getHouseId(),
                searchConditionVo.getAuditStatus(),
                searchConditionVo.getHousePeopleRel(),
                searchConditionVo.getPersonId(),
                ProjectContextHolder.getProjectId()
        );
    }


    /*需要调整-无需调整*/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R request(ProjectHousePersonRelRequestVo projectHousePersonRelRequestVo) {
        ProjectPersonInfo personInfo = projectPersonInfoService.getByTelephone(projectHousePersonRelRequestVo.getTelephone());
        String personId;


        //检查当前项目下业主信息是否存在
        if (ObjectUtil.isNotEmpty(personInfo)) {

            personId = personInfo.getPersonId();
            //检查该人员是否已经存在当前房屋内
            boolean havePersonInHouse = this.checkPersonExits(personId, projectHousePersonRelRequestVo.getHouseId());
            if (havePersonInHouse) {
                return R.failed("该用户已经在当前房屋下了");
            }
            //查询是否存在手机账号对应的userId 更新对应用户信息
            R<SysUser> requestUser = remoteUserService.user(projectHousePersonRelRequestVo.getTelephone());
            if (requestUser.getCode() == 0 && ObjectUtil.isNotEmpty(requestUser.getData())) {
                personInfo.setUserId(requestUser.getData().getUserId());
            }
            personInfo.setCredentialPicBack(projectHousePersonRelRequestVo.getCredentialPicBack());
            personInfo.setCredentialPicFront(projectHousePersonRelRequestVo.getCredentialPicFront());
            personInfo.setPersonName(projectHousePersonRelRequestVo.getPersonName());
            personInfo.setCredentialType("111");

            projectPersonInfoService.updateById(personInfo);
        } else {
            // 人员不存在,则新增人员
            personInfo = new ProjectPersonInfo();
            //查询是否存在手机账号对应的userId 更新对应用户信息
            R<SysUser> requestUser = remoteUserService.user(projectHousePersonRelRequestVo.getTelephone());
            if (requestUser.getCode() == 0 && ObjectUtil.isNotEmpty(requestUser.getData())) {
                personInfo.setUserId(requestUser.getData().getUserId());
            }
            personId = UUID.randomUUID().toString().replaceAll("-", "");
            BeanUtils.copyProperties(projectHousePersonRelRequestVo, personInfo);
            personInfo.setPersonId(personId);
            personInfo.setTelephone(projectHousePersonRelRequestVo.getTelephone());
            personInfo.setPStatus("1");
            personInfo.setCredentialType("111");

            projectPersonInfoService.saveFromSystem(personInfo);
        }

        //将人员信息植入人屋关系中
        ProjectHousePersonRel projectHousePersonRel = new ProjectHousePersonRel();
        BeanUtils.copyProperties(projectHousePersonRelRequestVo, projectHousePersonRel);
        ProjectConfig config = projectConfigService.getConfig();
        if (ProjectConfigConstant.SYSTEM_IDENTITY.equals(config.getAuthAudit())) {
            projectHousePersonRel.setAuditStatus(AuditStatusEnum.pass.code);
            // 该房屋是否存在业主
            ProjectHousePersonRel housePersonRel = this.getOne(Wrappers.lambdaQuery(ProjectHousePersonRel.class)
                    .eq(ProjectHousePersonRel::getHouseId, projectHousePersonRel.getHouseId())
                    .eq(ProjectHousePersonRel::getAuditStatus, AuditStatusEnum.pass.code)
                    .eq(ProjectHousePersonRel::getHouseholdType, HouseHoldTypeEnum.OWNER.code));
            if (HouseHoldTypeEnum.OWNER.code.equals(projectHousePersonRel.getHouseholdType()) && ObjectUtil.isNotEmpty(housePersonRel)) {
                remoteHousePersonRelService.removeById(housePersonRel.getRelaId());
                projectPersonInfoService.checkPersonAssets(housePersonRel.getPersonId());
            }
        } else {
            projectHousePersonRel.setAuditStatus(AuditStatusEnum.inAudit.code);
        }
        projectHousePersonRel.setPersonId(personId);
        projectHousePersonRel.setOrigin(OriginTypeEnum.WECHAT.code);

        //租凭开始若为空则设置为当前时间
        if (HouseHoldTypeEnum.TENANT.code.equals(projectHousePersonRel.getHouseholdType()) && ObjectUtil.isNull(projectHousePersonRel.getRentStartTime())) {
            projectHousePersonRel.setRentStartTime(LocalDateTime.now());
        }
        if (StringUtils.isBlank(projectHousePersonRel.getHousePeopleRel())) {
            projectHousePersonRel.setHousePeopleRel(HousePersonRelTypeEnum.PROPRIETOR.code);
        }
        this.save(projectHousePersonRel);
        return R.ok();
    }

    /*无需调整*/
    @Override
    public List<ProjectHouseHisRecordVo> findByName(String name) {
        return baseMapper.findByName(name, ProjectContextHolder.getProjectId());
    }


    /*无需调整*/
    @Override
    public ProjectHousePersonRelVo checkHouseRel(String houseId, String personName, String houseHoldType, String personId) {
        ProjectHousePersonRelVo projectHousePersonRelVo = new ProjectHousePersonRelVo();
        //如果是业主
        if (houseHoldType.equals(HouseHoldTypeEnum.OWNER.code)) {
            //当前房屋下的业主
            ProjectHousePersonRel projectHousePersonRel = getOne(Wrappers.lambdaQuery(ProjectHousePersonRel.class)
                    .eq(ProjectHousePersonRel::getHouseId, houseId)
                    .eq(ProjectHousePersonRel::getHouseholdType, houseHoldType)
                    .eq(ProjectHousePersonRel::getAuditStatus, AuditStatusEnum.pass.code)
                    .ne(ProjectHousePersonRel::getPersonId, personId));
            if (BeanUtil.isNotEmpty(projectHousePersonRel)) {
                ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getById(projectHousePersonRel.getPersonId());
                BeanUtils.copyProperties(projectPersonInfo, projectHousePersonRelVo);
                projectHousePersonRelVo.setHouseholdType(houseHoldType);
                return projectHousePersonRelVo;
            } else {
                ProjectHousePersonChangeHis projectHousePersonChangeHis = baseMapper.getToReplace(personId, houseId);
                if (projectHousePersonChangeHis == null) {
                    return null;
                } else {
                    return baseMapper.getReplaceUser(houseId, personName, personId, houseHoldType);
                }
            }
        } else {
            //当前房屋下家属或租客集合
            List<ProjectHousePersonRel> list = list(Wrappers.lambdaQuery(ProjectHousePersonRel.class)
                    .eq(ProjectHousePersonRel::getHouseId, houseId)
                    .eq(ProjectHousePersonRel::getAuditStatus, AuditStatusEnum.pass.code)
                    .ne(ProjectHousePersonRel::getHouseholdType, HouseHoldTypeEnum.OWNER.code)
                    .ne(ProjectHousePersonRel::getPersonId, personId));
            List<String> personIds = list.stream().map(ProjectHousePersonRel::getPersonId).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(personIds)) {
                List<ProjectPersonInfo> projectPersonInfos = projectPersonInfoService.listByIds(personIds);
                String pid = null;
                for (ProjectPersonInfo projectPersonInfo : projectPersonInfos) {
                    if (projectPersonInfo.getPersonName().equals(personName)) {
                        pid = projectPersonInfo.getPersonId();
                    }
                }
                if (pid != null) {
                    ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getById(pid);
                    ProjectHousePersonRel housePersonRel = getOne(Wrappers.lambdaQuery(ProjectHousePersonRel.class)
                            .eq(ProjectHousePersonRel::getPersonId, pid)
                            .eq(ProjectHousePersonRel::getHouseId, houseId));
                    BeanUtils.copyProperties(projectPersonInfo, projectHousePersonRelVo);
                    projectHousePersonRelVo.setHouseholdType(housePersonRel.getHouseholdType());
                    projectHousePersonRelVo.setMemberType(housePersonRel.getMemberType());
                    return projectHousePersonRelVo;
                } else {
                    ProjectHousePersonChangeHis projectHousePersonChangeHis = baseMapper.getToReplace(personId, houseId);
                    if (projectHousePersonChangeHis == null) {
                        return null;
                    } else {
                        return baseMapper.getReplaceUser(houseId, personName, personId, houseHoldType);
                    }
                }
            } else {
                ProjectHousePersonChangeHis projectHousePersonChangeHis = baseMapper.getToReplace(personId, houseId);
                if (projectHousePersonChangeHis == null) {
                    return null;
                } else {
                    return baseMapper.getReplaceUser(houseId, personName, personId, houseHoldType);
                }
            }
        }
    }


    /*无需调整*/
    @Override
    public IPage<ProjectHousePersonRelRecordVo> pageIdentity(IPage<ProjectHousePersonRelRecordVo> page, ProjectHousePersonRelSearchConditionVo searchConditionVo) {
        return projectHousePersonRelMapper.pageIdentity(
                page,
                searchConditionVo.getBuildingName(),
                searchConditionVo.getUnitName(),
                searchConditionVo.getHouseName(),
                searchConditionVo.getPersonName(),
                searchConditionVo.getHouseId(),
                searchConditionVo.getPhone(),
                searchConditionVo.getAuditStatus(),
                ProjectContextHolder.getProjectId());
    }


    /*无需调整*/
    /**
     * 更新住户信息
     *
     * @param projectHousePersonRelVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(ProjectHousePersonRelVo projectHousePersonRelVo) {

        //更新住户-房屋信息
        ProjectHousePersonRel projectHousePersonRel = new ProjectHousePersonRel();
        BeanUtils.copyProperties(projectHousePersonRelVo, projectHousePersonRel);

        //修改人员信息
        if (ObjectUtil.isNotEmpty(projectHousePersonRel.getPersonId())) {
            ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getById(projectHousePersonRel.getPersonId());
            BeanUtils.copyProperties(projectHousePersonRelVo, projectPersonInfo);
            projectPersonInfoService.updateById(projectPersonInfo);

            //更新人员标签
            projectPersonLabelService.addLabel(projectHousePersonRelVo.getTagArray(), projectPersonInfo.getPersonId());
        }


        // 更新重点人员信息
        this.operateFocusAttr(projectHousePersonRelVo);

        //设置拓展属性
        ProjectPersonAttrFormVo projectPersonAttrFormVo = new ProjectPersonAttrFormVo();
        projectPersonAttrFormVo.setProjectPersonAttrList(projectHousePersonRelVo.getProjectPersonAttrList());
        projectPersonAttrFormVo.setPersonId(projectHousePersonRelVo.getPersonId());
        projectPersonAttrFormVo.setProjectId(ProjectContextHolder.getProjectId());
        projectPersonAttrFormVo.setType(PersonTypeEnum.PROPRIETOR.code);
        projectPersonAttrService.updatePersonAttrList(projectPersonAttrFormVo);
        return this.updateById(projectHousePersonRel);

    }

    /*无需调整*/
    /**
     * 获取 身份认证详情
     *
     * @param id
     * @return
     */
    @Override
    public ProjectHousePersonRelVo getHousePersonVerifyInfo(String id) {


        //获取人屋关系信息
        ProjectHousePersonRel projectHousePersonRel = this.getById(id);

        //获取人员信息
        ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getById(projectHousePersonRel.getPersonId());

        //拼装VO
        ProjectHousePersonRelVo projectHousePersonRelVo = new ProjectHousePersonRelVo();

        //转换tag
        projectHousePersonRelVo.setTagArray(StringUtils.split(projectPersonInfo.getTag(), ','));

        //转换人员标签
        List<ProjectPersonLabel> lableList = projectPersonLabelService.listByPersonId(projectHousePersonRel.getPersonId());
        String[] lableArray = lableList.stream().map(ProjectPersonLabel::getLabelId).collect(Collectors.toList()).toArray(new String[lableList.size()]);
        projectHousePersonRelVo.setTagArray(lableArray);

        // 获取重点人员信息（如果有的话）
        ProjectFocusPersonAttr focusPersonAttr = projectFocusPersonAttrService.getFocusPersonAttrByPersonId(projectHousePersonRel.getPersonId());

        // 这里focusPersonAttr因为没有personId会把projectHousePersonRelVo中的personId覆盖掉变成了没有personId
        BeanUtil.copyProperties(focusPersonAttr, projectHousePersonRelVo);
        BeanUtils.copyProperties(projectPersonInfo, projectHousePersonRelVo);
        BeanUtils.copyProperties(projectHousePersonRel, projectHousePersonRelVo);
        String focusCategory = focusPersonAttr.getFocusCategory();
        if (StrUtil.isNotBlank(focusCategory)) {
            String[] focusCategoryArr = focusCategory.split(",");
            projectHousePersonRelVo.setFocusCategoryArr(focusCategoryArr);
        }
        //获取用户拓展属性
        List<ProjectPersonAttrListVo> projectPersonAttrListVos = projectPersonAttrService
                .getPersonAttrListVo(ProjectContextHolder.getProjectId(), PersonTypeEnum.PROPRIETOR.code, id);
        projectHousePersonRelVo.setProjectPersonAttrList(projectPersonAttrListVos);
        projectHousePersonRelVo.setCredentialPicFront(projectHousePersonRel.getCredentialPicFront());
        projectHousePersonRelVo.setCredentialPicBack(projectHousePersonRel.getCredentialPicBack());
        return projectHousePersonRelVo;
    }

    /*无需调整*/
    /**
     * 获取
     *
     * @param id
     * @return
     */
    @Override
    public ProjectHousePersonRelVo getVoById(String id) {
        //获取人屋关系信息
        ProjectHousePersonRel projectHousePersonRel = this.getById(id);
        //获取人员信息
        ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getById(projectHousePersonRel.getPersonId());
        //拼装VO
        ProjectHousePersonRelVo projectHousePersonRelVo = new ProjectHousePersonRelVo();
        //转换tag
        projectHousePersonRelVo.setTagArray(StringUtils.split(projectPersonInfo.getTag(), ','));
        //转换人员标签
        List<ProjectPersonLabel> lableList = projectPersonLabelService.listByPersonId(projectHousePersonRel.getPersonId());
        String[] lableArray = lableList.stream().map(ProjectPersonLabel::getLabelId).collect(Collectors.toList()).toArray(new String[lableList.size()]);
        projectHousePersonRelVo.setTagArray(lableArray);
        // 获取重点人员信息（如果有的话）
        ProjectFocusPersonAttr focusPersonAttr = projectFocusPersonAttrService.getFocusPersonAttrByPersonId(projectHousePersonRel.getPersonId());
        // 这里focusPersonAttr因为没有personId会把projectHousePersonRelVo中的personId覆盖掉变成了没有personId
        BeanUtil.copyProperties(focusPersonAttr, projectHousePersonRelVo);
        BeanUtils.copyProperties(projectHousePersonRel, projectHousePersonRelVo);
        BeanUtils.copyProperties(projectPersonInfo, projectHousePersonRelVo);
        String focusCategory = focusPersonAttr.getFocusCategory();
        if (StrUtil.isNotBlank(focusCategory)) {
            String[] focusCategoryArr = focusCategory.split(",");
            projectHousePersonRelVo.setFocusCategoryArr(focusCategoryArr);
        }
        //获取用户拓展属性
        List<ProjectPersonAttrListVo> projectPersonAttrListVos = projectPersonAttrService
                .getPersonAttrListVo(ProjectContextHolder.getProjectId(), PersonTypeEnum.PROPRIETOR.code, id);
        projectHousePersonRelVo.setProjectPersonAttrList(projectPersonAttrListVos);
        projectHousePersonRelVo.setCredentialPicFront(projectPersonInfo.getCredentialPicFront());
        projectHousePersonRelVo.setCredentialPicBack(projectPersonInfo.getCredentialPicBack());
        return projectHousePersonRelVo;
    }

    /*无需调整*/
    @Override
    public boolean checkPersonExits(String personId, String houseId) {
        int num = this.count(new QueryWrapper<ProjectHousePersonRel>().lambda()
                .eq(ProjectHousePersonRel::getPersonId, personId)
                .eq(ProjectHousePersonRel::getHouseId, houseId)
                .eq(ProjectHousePersonRel::getAuditStatus, AuditStatusEnum.pass.code)
        );
        return num != 0;
    }

    /*无需调整*/
    /**
     * 根据住户ID获取所在的房屋
     *
     * @param personId
     * @return
     */
    @Override
    public List<ProjectHouseDTO> listHouseByPersonId(String personId) {
//        ProjectHouseDTO projectHouseDTO;
//        List<ProjectHouseDTO> dtolist = new ArrayList<>();
//        ProjectFrameInfo houseInfo;
//        ProjectFrameInfo unitInfo;
//        ProjectFrameInfo buildingInfo;
//
//        List<ProjectHousePersonRel> housePersonRelsList = this.list(new QueryWrapper<ProjectHousePersonRel>().lambda().eq(ProjectHousePersonRel::getPersonId, personId));
//
//        for (ProjectHousePersonRel housePersonRel : housePersonRelsList) {
//            projectHouseDTO = new ProjectHouseDTO();
//
//            projectHouseDTO.setHouseId(housePersonRel.getHouseId());
//            houseInfo = projectFrameInfoService.getById(housePersonRel.getHouseId());
//            unitInfo = projectFrameInfoService.getById(houseInfo.getPuid());
////            buildingInfo =  projectFrameInfoService.getById(unitInfo.getPuid());
//
//            projectHouseDTO.setUnitId(houseInfo.getPuid());
//            projectHouseDTO.setBuildingId(unitInfo.getPuid());
//
//            dtolist.add(projectHouseDTO);
//        }
        return baseMapper.listHouseByPersonId(personId, ProjectContextHolder.getProjectId());
    }

    /*无需调整*/
    /**
     * 根据第三方编号获取人屋关系
     *
     * @param relaCode
     * @return
     */
    @Override
    public ProjectHousePersonRel getByRelaCode(String relaCode) {
        List<ProjectHousePersonRel> housePersonRelList = this.list(new QueryWrapper<ProjectHousePersonRel>().lambda().eq(ProjectHousePersonRel::getRelaCode, relaCode));
        if (CollectionUtil.isNotEmpty(housePersonRelList)) {
            return housePersonRelList.get(0);
        }
        return null;
    }

    /*无需调整*/
    /**
     * 检查一个房间内是否存在业主
     *
     * @param houseId
     * @return
     */
    @Override
    public boolean haveOwner(String houseId) {
        int count = count(new QueryWrapper<ProjectHousePersonRel>().lambda()
                .eq(ProjectHousePersonRel::getHouseId, houseId)
                .eq(ProjectHousePersonRel::getHouseholdType, HouseHoldTypeEnum.OWNER.code)
                .eq(ProjectHousePersonRel::getAuditStatus, AuditStatusEnum.pass.code)
        );
        return count >= 1;
    }

    /*无需调整*/
    /**
     * <p>
     * 对重点人员信息进行更新或删除或添加操作
     * </p>
     *
     * @param housePersonRelVo 人物关系vo对象
     * @author: 王良俊
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void operateFocusAttr(ProjectHousePersonRelVo housePersonRelVo) {
        if (IsFocusPersonConstants.YES.equals(housePersonRelVo.getIsFocusPerson())) {
            String[] focusCategoryArr = housePersonRelVo.getFocusCategoryArr();
            String focusCategory = "";
            if (ArrayUtil.isNotEmpty(focusCategoryArr)) {
                focusCategory = focusCategoryArr.toString();
                focusCategory = focusCategory.substring(1, focusCategory.length() - 1);
            }
            ProjectFocusPersonAttr focusPersonAttr = new ProjectFocusPersonAttr();
            BeanUtil.copyProperties(housePersonRelVo, focusPersonAttr);
            focusPersonAttr.setFocusCategory(focusCategory);
            projectFocusPersonAttrService.saveOrUpdateFocusPersonAttrByPersonId(focusPersonAttr);
        } else {
            if (StrUtil.isNotBlank(housePersonRelVo.getPersonId())) {
                projectFocusPersonAttrService.removeFocusPersonAttrByPersonId(housePersonRelVo.getPersonId());
            }
        }
    }

    /*无需调整*/
    /**
     * <p>
     * 对重点人员信息进行更新或删除或添加操作（批量操作）
     * </p>
     *
     * @param housePersonRelVoList 人物关系vo对象列表
     * @author: 王良俊
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void operateFocusAttr(List<ProjectHousePersonRelVo> housePersonRelVoList) {
        List<ProjectFocusPersonAttr> focusPersonAttrList = new ArrayList<>();
        List<String> removeFocusPersonIdList = new ArrayList<>();
        for (ProjectHousePersonRelVo housePersonRelVo : housePersonRelVoList) {
            if (IsFocusPersonConstants.YES.equals(housePersonRelVo.getIsFocusPerson())) {
                String[] focusCategoryArr = housePersonRelVo.getFocusCategoryArr();
                String focusCategory = "";
                if (ArrayUtil.isNotEmpty(focusCategoryArr)) {
                    focusCategory = focusCategoryArr.toString();
                    focusCategory = focusCategory.substring(1, focusCategory.length() - 1);
                }
                ProjectFocusPersonAttr focusPersonAttr = new ProjectFocusPersonAttr();
                BeanUtil.copyProperties(housePersonRelVo, focusPersonAttr);
                focusPersonAttr.setFocusCategory(focusCategory);
                focusPersonAttr.setPersonId(housePersonRelVo.getPersonId());
                focusPersonAttrList.add(focusPersonAttr);
            } else {
                if (StrUtil.isNotBlank(housePersonRelVo.getPersonId())) {
                    removeFocusPersonIdList.add(housePersonRelVo.getPersonId());
                }
            }
        }
        // 这里要改成批量操作的方法
        projectFocusPersonAttrService.saveOrUpdateFocusPersonAttrByPersonId(focusPersonAttrList);
        projectFocusPersonAttrService.removeFocusPersonAttrByPersonId(removeFocusPersonIdList);

    }


    /*无需调整*/
    @Override
    public List<ProjectHousePersonRelRecordVo> listByHouseId(String id) {
        return baseMapper.findPageByHouseId(id, ProjectContextHolder.getProjectId());
    }

    /*无需调整*/
    @Override
    public Page<ProjectHousePersonRelRecordVo> findPageById(Page page, String personId) {
        return baseMapper.findPageById(page, personId, ProjectContextHolder.getProjectId());
    }

    /*无需调整*/
    @Override
    public Page<ProjectHousePersonRelRecordVo> filterPageById(Page page, String personId, String status) {
        return baseMapper.filterPageById(page, personId, ProjectContextHolder.getProjectId(), status);
    }

    /*无需调整*/
    @Override
    public ProjectHousePersonRel findByPersonIdAndHouseId(String personId, String houseId) {
        return getOne(Wrappers.lambdaQuery(ProjectHousePersonRel.class)
                .eq(ProjectHousePersonRel::getPersonId, personId)
                .eq(ProjectHousePersonRel::getHouseId, houseId)
                .eq(ProjectHousePersonRel::getAuditStatus, AuditStatusEnum.pass.code));
    }

    /*无需调整*/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R requestAgain(ProjectHousePersonRelRequestAgainVo projectHousePersonRel) {

        ProjectHousePersonRel thisProjectHousePersonRel = getById(projectHousePersonRel.getRelaId());
        if (ObjectUtil.isNotEmpty(projectHousePersonRel.getRentStartTime())) {
            thisProjectHousePersonRel.setRentStartTime(projectHousePersonRel.getRentStartTime());
        }
        if (ObjectUtil.isNotEmpty(projectHousePersonRel.getRentStopTime())) {
            thisProjectHousePersonRel.setRentStopTime(projectHousePersonRel.getRentStopTime());
        }
        //重新申请重置审核状态
        thisProjectHousePersonRel.setAuditStatus(AuditStatusEnum.inAudit.code);
        thisProjectHousePersonRel.setAuditReason("");
        updateById(thisProjectHousePersonRel);
        ProjectPersonInfo personInfo = projectPersonInfoService.getById(thisProjectHousePersonRel.getPersonId());
        personInfo.setCredentialPicFront(projectHousePersonRel.getCredentialPicFront());
        personInfo.setCredentialPicBack(projectHousePersonRel.getCredentialPicBack());
        personInfo.setPersonName(projectHousePersonRel.getPersonName());
        projectPersonInfoService.updateById(personInfo);
        return R.ok();
    }


    /*无需调整*/
    @Override
    public ProjectHouseParkPlaceInfoVo getInfo(String id) {
        //获取房屋人员关系表
        ProjectHousePersonRel projectHousePersonRel = baseMapper.selectById(id);
        //获取人员信息
        ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getById(projectHousePersonRel.getPersonId());
        //设置人员信息
        ProjectHouseParkPlaceInfoVo parkPlaceInfoVo = new ProjectHouseParkPlaceInfoVo();
        parkPlaceInfoVo.setPersonId(projectPersonInfo.getPersonId());
        parkPlaceInfoVo.setGender(projectPersonInfo.getGender());
        parkPlaceInfoVo.setPersonName(projectPersonInfo.getPersonName());
        parkPlaceInfoVo.setTelephone(projectPersonInfo.getTelephone());
        parkPlaceInfoVo.setPeopleTypeCode(projectPersonInfo.getPeopleTypeCode());
        parkPlaceInfoVo.setHouseholdType(projectHousePersonRel.getHouseholdType());

        //获取关联的停车场信息
        List<ProjectParkingPlace> parkingPlaces = projectParkingPlaceService.getByPersonId(projectHousePersonRel.getPersonId());
        //存在可能没有关联停车场的情况 故这里做一层非空判断
        if (ObjectUtil.isNotEmpty(parkingPlaces) && parkingPlaces.size() > 0) {
            //获取停车场id列表
            List<String> placeIds = parkingPlaces.stream().map(e -> e.getPlaceId()).collect(Collectors.toList());
            //获取与停车场关联的车辆信息
            List<ProjectParCarRegister> parCarRegisters = projectParCarRegisterService.list(
                    Wrappers.lambdaQuery(ProjectParCarRegister.class).in(ProjectParCarRegister::getParkPlaceId, placeIds));
            parkPlaceInfoVo.setParkingPlaces(parkingPlaces);
            parkPlaceInfoVo.setParCarRegisters(parCarRegisters);
        }

        return parkPlaceInfoVo;
    }

    /*无需调整*/
    /**
     * 获取不同住户类型的人数，忽略手机号相同的住户。
     * 1 业主（产权人） 2 家属 3 租客
     *
     * @param houseHoldType
     * @return
     */
    @Override
    public Integer countHousePersonRel(String houseHoldType) {
        return this.baseMapper.countByHouseHoldType(houseHoldType, ProjectContextHolder.getProjectId());
    }

    /*无需调整*/
    /*主要目的是检查给定的电话号码和房屋ID是否已经预注册。*/
    @Override
    public ProjectHousePersonRel checkHasPreRegister(String telephone, String houseId) {
        List<ProjectPersonInfo> personInfoList = projectPersonInfoService.list(new LambdaQueryWrapper<ProjectPersonInfo>()
                .eq(ProjectPersonInfo::getTelephone, telephone)
                .last("limit 1"));
        if (CollUtil.isNotEmpty(personInfoList)) {
            ProjectPersonInfo personInfo = personInfoList.get(0);
            String personId = personInfo.getPersonId();
            List<ProjectHousePersonRel> housePersonRelList = this.list(new LambdaQueryWrapper<ProjectHousePersonRel>()
                    .eq(ProjectHousePersonRel::getPersonId, personId)
                    .eq(ProjectHousePersonRel::getHouseId, houseId)
                    .eq(ProjectHousePersonRel::getAuditStatus, "1")
                    .last("limit 1"));
            if (CollUtil.isNotEmpty(housePersonRelList)) {
                return housePersonRelList.get(0);
            }
        }
        return null;
    }

    /*无需调整*/
    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_UNCOMMITTED)
    public List<ProjectHousePersonNumVo> getHousePersonNumMapping() {
        return this.baseMapper.getHousePersonNumMapping(ProjectContextHolder.getProjectId(), TenantContextHolder.getTenantId());
    }

    /*无需调整*/
    @Override
    public Long incrementPersonNum(String redisKey, String houseId, String personId) {
        Long num = 1L;

        if (redisTemplateAurine.hasKey(redisKey)) {

            if (redisTemplateAurine.opsForHash().hasKey(redisKey, houseId + personId)) {
                num = redisTemplateAurine.opsForHash().increment(redisKey, houseId + personId, 1);
            } else {
                redisTemplateAurine.opsForHash().put(redisKey, houseId + personId, "1");
            }
        } else {
            this.initHousePersonNumMapping(redisKey);
            num = this.incrementPersonNum(redisKey, houseId, personId);
        }
        return num;
    }

    /*无需调整*/
    @Override
    public void initHousePersonNumMapping(String redisKey) {
        if (redisTemplateAurine.hasKey(redisKey)) {
            redisTemplateAurine.delete(redisKey);
        }
        List<ProjectHousePersonNumVo> housePersonNumMapping = this.getHousePersonNumMapping();
        Map<String, String> housePersonNumMap;
        if (CollUtil.isNotEmpty(housePersonNumMapping)) {
            housePersonNumMap = housePersonNumMapping.stream().filter(projectHousePersonNumVo -> StrUtil.isNotEmpty(projectHousePersonNumVo.getHousePersonId()))
                    .collect(Collectors.toMap(ProjectHousePersonNumVo::getHousePersonId, ProjectHousePersonNumVo::getNum, (num1, num2) -> num2));
        } else {
            housePersonNumMap = new HashMap<>();
            housePersonNumMap.put("1", "1");
        }
        redisTemplateAurine.opsForHash().putAll(redisKey, housePersonNumMap);
        redisTemplateAurine.expire(redisKey, 2, TimeUnit.HOURS);
    }

    /*无需调整*/
    @Override
    public void deleteHousePersonNumCache(String redisKey) {
        redisTemplateAurine.delete(redisKey);
    }

    /*无需调整*/
    @Override
    public List<ProjectHousePersonRel> getHousePersonRelListByHouseId(String houseId) {
        QueryWrapper<ProjectHousePersonRel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("houseId", houseId);
        return projectHousePersonRelMapper.selectList(queryWrapper);
    }

    /*@Override
    public void decreasePersonNum(String redisKey, String houseId, String personId) {
        if (redisTemplateAurine.opsForHash().hasKey(redisKey, houseId + personId)) {
            redisTemplateAurine.opsForHash().increment(redisKey, houseId + personId, -1);
        }
    }*/

    /*无需调整*/
    @Override
    public boolean reSaveRel(String housePersonRelId) {
        return true;
    }

    /*无需调整*/
    @Override
    public String findSaveFace(String relaId) {
        String saveFace = baseMapper.findSaveFace(relaId);

        return saveFace;
    }

    /*无需调整*/
    @Override
    public Integer countByOff() {
        return baseMapper.countByOff();
    }

    @Override
    public Integer countHouseByPersonId(String personId) {
        return this.count(new QueryWrapper<ProjectHousePersonRel>().lambda().eq(ProjectHousePersonRel::getPersonId, personId));
    }

    @Override
    public String getVisitorHouseName(String visitHouseId) {
        return baseMapper.getVisitorHouseName(visitHouseId);
    }
}
