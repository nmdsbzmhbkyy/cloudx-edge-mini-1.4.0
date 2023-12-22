package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.constant.enums.DeviceTypeEnum;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.CallTypeEnum;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.mapper.ProjectDeviceCallEventMapper;
import com.aurine.cloudx.estate.service.ProjectDeviceCallEventService;
import com.aurine.cloudx.estate.service.ProjectDeviceInfoService;
import com.aurine.cloudx.estate.service.ProjectPersonInfoService;
import com.aurine.cloudx.estate.service.ProjectStaffService;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.EventDeviceNoticeConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.TopicConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.EventDeviceNoticeDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.vo.HuaWeiCallEventVo;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.vo.ObjInfoVo;
import com.aurine.cloudx.estate.thirdparty.transport.mq.kafka.KafkaProducer;
import com.aurine.cloudx.estate.vo.ProjectDeviceCallEventVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 项目呼叫事件(ProjectDeviceCallEvent)ServiceImpl
 *
 * @author : Qiu
 * @date : 2020 12 16 9:42
 */
@Service
@Slf4j
public class ProjectDeviceCallEventServiceImpl extends ServiceImpl<ProjectDeviceCallEventMapper, ProjectDeviceCallEvent> implements ProjectDeviceCallEventService {

    @Resource
    private ProjectStaffService projectStaffService;
    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;
    @Resource
    private ProjectPersonInfoService projectPersonInfoService;

    /**
     * 分页查询呼叫记录
     * @param page
     * @param projectDeviceCallEventVo
     * @return
     */
    @Override
    public Page<ProjectDeviceCallEventVo> pageCallEvent(Page page, ProjectDeviceCallEventVo projectDeviceCallEventVo) {
        return baseMapper.pageCallEvent(page, projectDeviceCallEventVo);
    }

    /**
     * 分页查询项目呼叫事件(查询当前登录用户所在项目的呼叫记录)
     * @param page
     * @param projectDeviceCallEventVo
     * @return
     */
    @Override
    public Page<ProjectDeviceCallEventVo> pageCallEventByProject(Page page, ProjectDeviceCallEventVo projectDeviceCallEventVo) {
        projectDeviceCallEventVo.setProjectId(ProjectContextHolder.getProjectId());
        return baseMapper.pageCallEventByProject(page, projectDeviceCallEventVo);
    }

    /**
     * 分页查询呼叫记录(查询当前登录用户的员工ID为接收方的呼叫记录)
     * @param page
     * @param projectDeviceCallEventVo
     * @return
     */
    @Override
    public Page<ProjectDeviceCallEventVo> pageCallEventByStaff(Page page, ProjectDeviceCallEventVo projectDeviceCallEventVo) {
        ProjectStaff staff = projectStaffService.getOne(new QueryWrapper<ProjectStaff>().lambda()
                .eq(ProjectStaff::getUserId, SecurityUtils.getUser().getId())
                .eq(ProjectStaff::getProjectId, ProjectContextHolder.getProjectId()));
        if (staff == null) {
            return new Page<>();
        }
        projectDeviceCallEventVo.setStaffId(staff.getStaffId());
        return baseMapper.pageCallEventByStaff(page, projectDeviceCallEventVo);
    }

    @Override
    public R saveCallEvent(JSONObject jsonObject) {
        log.info("[华为中台] - 接收到呼叫事件开始解析:{}", jsonObject);
        HuaWeiCallEventVo huaWeiCallEventVo = new HuaWeiCallEventVo();
        ObjInfoVo objInfoVo;
        // 呼叫方设备类型
        String callDeviceType = "";
        //数据转换
        try {
            List<ProjectPersonInfo> initiatorPersonInfo = new ArrayList<>();
            List<ProjectPersonInfo> answerPersonInfo = new ArrayList<>();
            List<ProjectStaff> projectStaffList = new ArrayList<>();

            huaWeiCallEventVo = JSONObject.parseObject(JSONObject.toJSONString(jsonObject), HuaWeiCallEventVo.class);
            objInfoVo = JSONObject.parseObject(JSONObject.toJSONString(huaWeiCallEventVo.getData().getObjInfo()), ObjInfoVo.class);

            //注入线程id
            TenantContextHolder.setTenantId(1);
            Integer projectId = Integer.valueOf(objInfoVo.getCallTargetProject().replace("S", ""));
            ProjectContextHolder.setProjectId(projectId);

            //实体对象
            ProjectDeviceCallEvent projectDeviceCallEvent = new ProjectDeviceCallEvent();

            LocalDateTime callTime = LocalDateTime.ofEpochSecond(Long.parseLong(objInfoVo.getCallTime()), 0, ZoneOffset.ofHours(8));
            projectDeviceCallEvent.setCallTime(callTime);
            projectDeviceCallEvent.setCallDuration(objInfoVo.getTalkTime());
            projectDeviceCallEvent.setCallResponse(objInfoVo.getTalkTime() > 0 ? "1" : "0");

            SysUser sysUserInitiator = baseMapper.getPhone(huaWeiCallEventVo.getSourceId());
            if (sysUserInitiator == null) {
                return R.failed(Boolean.FALSE, "查询不到发起人");
            }
            String initiator = sysUserInitiator.getPhone();
            //发起方  如果包含/就是设备
            if (initiator.contains("/")) {
                ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getOne(Wrappers.lambdaQuery(ProjectDeviceInfo.class)
                        .eq(ProjectDeviceInfo::getSn, initiator.split("/")[1]));
                callDeviceType = deviceInfo.getDeviceType();
                projectDeviceCallEvent.setCaller(deviceInfo.getDeviceId());
                projectDeviceCallEvent.setCallerType("1");
                projectDeviceCallEvent.setCallerName(deviceInfo.getDeviceName());
            } else {
                initiatorPersonInfo = projectPersonInfoService.list(Wrappers.lambdaQuery(ProjectPersonInfo.class).eq(ProjectPersonInfo::getUserId, huaWeiCallEventVo.getSourceId()));
                //initiatorPersonInfo = baseMapper.getPerson(sysUserInitiator.getPhone(), Integer.valueOf(huaWeiCallEventVo.getSourceId()), projectId);
            }

            /**
             * callMode: 0:按房间呼叫   1:指定用户id呼叫  2:物业的用户id  4:指定设备sn呼叫
             */
            if ("4".equals(objInfoVo.getCallMode())) {
                ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getOne(Wrappers.lambdaQuery(ProjectDeviceInfo.class).eq(ProjectDeviceInfo::getSn, objInfoVo.getCallTarget()));
                projectDeviceCallEvent.setAnswerer(deviceInfo.getDeviceId());
                projectDeviceCallEvent.setAnswererType("1");
                projectDeviceCallEvent.setAnswererName(deviceInfo.getDeviceName());
                if (DeviceTypeEnum.CENTER_DEVICE.getCode().equals(deviceInfo.getDeviceType())) {
                    // 如果接听设备是中心机则通话类型是呼叫中心
                    projectDeviceCallEvent.setCallType(CallTypeEnum.CALL_CENTER.code);
                }
                if (DeviceTypeEnum.INDOOR_DEVICE.getCode().equals(deviceInfo.getDeviceType())) {
                    // 如果接听设备是室内机则通话类型是呼叫业主
                    projectDeviceCallEvent.setCallType(CallTypeEnum.CALL_OWNER.code);
                }
            } else if ("1".equals(objInfoVo.getCallMode())) {
                SysUser sysUserAnswer = baseMapper.getPhone(objInfoVo.getCallTarget());
                String answer = sysUserAnswer.getPhone();
                //接听方 如果包含/就是设备
                if (answer.contains("/")) {
                    ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getOne(Wrappers.lambdaQuery(ProjectDeviceInfo.class)
                            .eq(ProjectDeviceInfo::getSn, answer.split("/")[1]));
                    projectDeviceCallEvent.setAnswerer(deviceInfo.getDeviceId());
                    projectDeviceCallEvent.setAnswererType("1");
                    projectDeviceCallEvent.setAnswererName(deviceInfo.getDeviceName());
                    if (DeviceTypeEnum.CENTER_DEVICE.getCode().equals(deviceInfo.getDeviceType())) {
                        // 如果接听设备是中心机则通话类型是呼叫中心
                        projectDeviceCallEvent.setCallType(CallTypeEnum.CALL_CENTER.code);
                    }
                    if (DeviceTypeEnum.INDOOR_DEVICE.getCode().equals(deviceInfo.getDeviceType())) {
                        // 如果接听设备是室内机则通话类型是呼叫业主
                        projectDeviceCallEvent.setCallType(CallTypeEnum.CALL_OWNER.code);
                    }
                } else {
                    if (CollUtil.isNotEmpty(initiatorPersonInfo)) {
                        projectStaffList = projectStaffService.list(Wrappers.lambdaQuery(ProjectStaff.class).eq(ProjectStaff::getUserId, objInfoVo.getCallTarget()));
                    } else {
                        answerPersonInfo = baseMapper.getPerson(sysUserAnswer.getPhone(), Integer.valueOf(objInfoVo.getCallTarget()), projectId);
                    }
                }
            } else if ("0".equals(objInfoVo.getCallMode())) {
                projectId = Integer.valueOf(objInfoVo.getCallTargetProject().replace("S", ""));
                ProjectHouseInfo projectHouseInfo = baseMapper.getHouse(objInfoVo.getCallTarget(), projectId);
                // 呼叫房间的通话类型是呼叫业主
                projectDeviceCallEvent.setCallType(CallTypeEnum.CALL_OWNER.code);
                projectDeviceCallEvent.setAnswerer(projectHouseInfo.getHouseId());
                projectDeviceCallEvent.setAnswererType("2");
                projectDeviceCallEvent.setAnswererName(projectHouseInfo.getHouseName());
                // 如果是中心机呼叫业主
                if (DeviceTypeEnum.CENTER_DEVICE.getCode().equals(callDeviceType)) {
                    // 未接(不用判断接通情况，如果接通则不是按房间呼叫而是呼叫业主)
                    if ("0".equals(projectDeviceCallEvent.getCallResponse())) {
                        // 接收方名称为楼栋+单元+房间
                        String houseName = baseMapper.getFrameNameByHouseId(projectHouseInfo.getHouseId());
                        projectDeviceCallEvent.setAnswererName(houseName);
                    }
                }
                //呼叫管家或者中心机
            } else if ("2".equals(objInfoVo.getCallMode())) {
                SysUser sysUserAnswer = baseMapper.getPhone(objInfoVo.getCallTarget());
                String answer = sysUserAnswer.getPhone();
                //接听方 如果包含/就是中心机设备
                if (answer.contains("/")) {
                    ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getOne(Wrappers.lambdaQuery(ProjectDeviceInfo.class)
                            .eq(ProjectDeviceInfo::getSn, answer.split("/")[1]));
                    projectDeviceCallEvent.setAnswerer(deviceInfo.getDeviceId());
                    projectDeviceCallEvent.setAnswererType("1");
                    projectDeviceCallEvent.setAnswererName(deviceInfo.getDeviceName());
                    projectId = deviceInfo.getProjectId();
                    projectDeviceCallEvent.setCallType(CallTypeEnum.CALL_CENTER.code);
                } else {
                    projectStaffList = projectStaffService.list(Wrappers.lambdaQuery(ProjectStaff.class).eq(ProjectStaff::getUserId, objInfoVo.getCallTarget()));

                }
            }

            //比较两个用户的所在项目 取出呼叫方和被呼叫方所在的同一个小区id 只针对业主端呼叫物业端场景 其他不会走进这个判断
            if (CollUtil.isNotEmpty(initiatorPersonInfo) && CollUtil.isNotEmpty(projectStaffList)) {
                // 通话类型是呼叫物业
                projectDeviceCallEvent.setCallType(CallTypeEnum.CALL_PROPERTY.code);
                List<Integer> initiatorProjectId = initiatorPersonInfo.stream().map(ProjectPersonInfo::getProjectId).collect(Collectors.toList());
                List<Integer> answerProjectId = projectStaffList.stream().map(ProjectStaff::getProjectId).collect(Collectors.toList());

                Set<Integer> set = new HashSet<>(answerProjectId);
                List<Integer> projectList = initiatorProjectId.stream().filter(e -> !set.add(e)).collect(Collectors.toList());

                initiatorPersonInfo.forEach(e -> {
                    if (e.getProjectId().equals(projectList.get(0))) {
                        projectDeviceCallEvent.setCaller(e.getPersonId());
                        projectDeviceCallEvent.setCallerType("2");
                        projectDeviceCallEvent.setCallerName(e.getPersonName());
                    }
                });

                projectStaffList.forEach(e -> {
                    if (e.getProjectId().equals(projectList.get(0))) {
                        projectDeviceCallEvent.setAnswerer(e.getStaffId());
                        projectDeviceCallEvent.setAnswererType("2");
                        projectDeviceCallEvent.setAnswererName(e.getStaffName());
                    }
                });
                projectId = projectList.get(0);
                //如果initiatorPersonInfo这个不为空 代表是用户呼叫设备
            } else if (CollUtil.isNotEmpty(initiatorPersonInfo)) {
                for (ProjectPersonInfo personInfo : initiatorPersonInfo) {
                    if (personInfo.getProjectId().equals(projectId)) {
                        projectDeviceCallEvent.setCaller(personInfo.getPersonId());
                        projectDeviceCallEvent.setCallerType("2");
                        projectDeviceCallEvent.setCallerName(personInfo.getPersonName());
                    }
                }
                //如果answerPersonInfo不为空 代表是设备呼叫用户
            } else if (CollUtil.isNotEmpty(answerPersonInfo)) {
                for (ProjectPersonInfo personInfo : answerPersonInfo) {
                    if (personInfo.getProjectId().equals(projectId)) {
                        projectDeviceCallEvent.setAnswerer(personInfo.getPersonId());
                        projectDeviceCallEvent.setAnswererType("2");
                        projectDeviceCallEvent.setAnswererName(personInfo.getPersonName());
                        // 呼叫业主
                        projectDeviceCallEvent.setCallType(CallTypeEnum.CALL_OWNER.code);
                    }
                }
                //如果projectStaffList不为空,代表设备呼叫管家
            } else if (CollUtil.isNotEmpty(projectStaffList)) {
                for (ProjectStaff projectStaff : projectStaffList) {
                    if (projectStaff.getProjectId().equals(projectId)) {
                        projectDeviceCallEvent.setAnswerer(projectStaff.getStaffId());
                        projectDeviceCallEvent.setAnswererType("2");
                        projectDeviceCallEvent.setAnswererName(projectStaff.getStaffName());
                        // 呼叫业主
                        projectDeviceCallEvent.setCallType(CallTypeEnum.CALL_PROPERTY.code);
                    }
                }
            }

//            projectDeviceCallEvent.setProjectId(projectId);

            EventDeviceNoticeDTO eventDeviceNoticeDTO = new EventDeviceNoticeDTO();
            eventDeviceNoticeDTO.setDeviceCallEvent(projectDeviceCallEvent);
            eventDeviceNoticeDTO.setAction(EventDeviceNoticeConstant.ACTION_DEVICE_CALL_EVENT);
            KafkaProducer.sendMessage(TopicConstant.SDI_EVENT_DEVICE_NOTICE, eventDeviceNoticeDTO);
            log.info("[华为中台] - 解析结果{}", JSONObject.toJSONString(eventDeviceNoticeDTO));
        } catch (Exception e) {
            log.info("[华为中台] - 解析出现异常:{},{}", jsonObject, e.getMessage());
            e.printStackTrace();
            return R.failed(Boolean.FALSE, "保存失败");
        }
        return R.ok(Boolean.TRUE, "保存成功");
    }
}
