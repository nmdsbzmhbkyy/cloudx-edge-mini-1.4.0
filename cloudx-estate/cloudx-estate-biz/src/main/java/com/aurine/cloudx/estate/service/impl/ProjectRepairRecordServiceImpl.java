package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.util.NoticeUtil;
import com.aurine.cloudx.estate.constant.enums.ApproveStatusEnum;
import com.aurine.cloudx.estate.entity.ProjectFrameInfo;
import com.aurine.cloudx.estate.entity.ProjectPersonInfo;
import com.aurine.cloudx.estate.entity.ProjectRepairRecord;
import com.aurine.cloudx.estate.entity.ProjectStaff;
import com.aurine.cloudx.estate.mapper.ProjectRepairRecordMapper;
import com.aurine.cloudx.estate.util.WebSocketNotifyUtil;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目报修服务记录
 *
 * @author guhl@aurine.cn
 * @date 2020-07-20 13:36:01
 */
@Service
@Slf4j
public class ProjectRepairRecordServiceImpl extends ServiceImpl<ProjectRepairRecordMapper, ProjectRepairRecord> implements ProjectRepairRecordService {
    @Resource
    private ProjectFrameInfoService projectFrameInfoService;

    @Resource
    private ProjectHouseInfoService projectHouseInfoService;
    @Resource
    private ProjectPersonInfoService projectPersonInfoService;
    @Resource
    private ProjectStaffService projectStaffService;
    @Resource
    private ProjectWebSocketService projectWebSocketService;
    @Resource
    NoticeUtil noticeUtil;


    @Override
    public Page<ProjectRepairRecordPageVo> pageRepairRecord(Page<ProjectRepairRecordPageVo> page, ProjectRepairRecordPageVo projectRepairRecordPageVo) {
        return baseMapper.pageRepairRecord(page, projectRepairRecordPageVo);
    }

    @Override
    public ProjectRepairRecordInfoVo getByRepairId(String repairId) {
        ProjectRepairRecord projectRepairRecord = super.getById(repairId);
        if (BeanUtil.isEmpty(projectRepairRecord)) {
            return null;
        }
        ProjectRepairRecordInfoVo projectRepairRecordInfoVo = new ProjectRepairRecordInfoVo();
        BeanUtils.copyProperties(projectRepairRecord, projectRepairRecordInfoVo);
        String houseId = projectRepairRecord.getHouseId();
        String groupName = null;
        //获取房屋实体
        ProjectFrameInfo projectFrameInfo = projectFrameInfoService.getById(houseId);
        //当前房屋地址字段拼接
        if (BeanUtil.isNotEmpty(projectFrameInfo)) {
            //获取单元实体
            ProjectFrameInfo projectFrameInfoUnit = projectFrameInfoService.getById(projectFrameInfo.getPuid());
            //获取楼栋实体
            ProjectFrameInfo projectFrameInfoBuilding = projectFrameInfoService.getById(projectFrameInfoUnit.getPuid());
            //获取组团信息
            groupName = projectFrameInfoService.getFrameNameById(projectFrameInfoBuilding.getEntityId());
            //如果存在组团 对组团进行拼接
            if (StrUtil.isNotEmpty(groupName)) {
                List<String> groupNameList = CollUtil.reverse(StrUtil.split(groupName, ','));
                String buildingName = groupNameList.toString().replace("[", "")
                        .replace("]", "").replace(",", "-").replace(" ", "");
                projectRepairRecordInfoVo.setCurrentHouseName(buildingName + projectFrameInfoUnit.getEntityName() + projectFrameInfo.getEntityName());
            } else {
                projectRepairRecordInfoVo.setCurrentHouseName(projectFrameInfoBuilding.getEntityName() + projectFrameInfoUnit.getEntityName() + projectFrameInfo.getEntityName());
            }
        }
        if (StrUtil.isNotEmpty(projectRepairRecord.getHandler())) {
            ProjectStaff projectStaff = projectStaffService.getById(projectRepairRecord.getHandler());
            if (BeanUtil.isNotEmpty(projectStaff)) {
                projectRepairRecordInfoVo.setHandlerName(projectStaff.getStaffName());
                projectRepairRecordInfoVo.setPhone(projectStaff.getMobile());
            }
        }
        return projectRepairRecordInfoVo;
    }

    @Override
    public Page<ProjectRepairRecordVo> pageByType(Page page, String type, String status, String handler, String phone, String date) {
        return baseMapper.pageByType(page, type, status, handler, phone, date);
    }

    @Override
    public R setHandler(String recordId, String handler) {

        ProjectRepairRecord projectRepairRecord = getById(recordId);
        ProjectRepairRecordInfoVo repairRecordInfoVo = getByRepairId(recordId);

        if (ObjectUtil.isNotEmpty(projectRepairRecord)) {
            projectRepairRecord.setHandler(handler);
            projectRepairRecord.setOrderTime(LocalDateTime.now());
            projectRepairRecord.setStatus(ApproveStatusEnum.processing.code);
            baseMapper.updateById(projectRepairRecord);
            String stausTyp = getStatusByType(projectRepairRecord.getStatus(), "maintain_status");
            try {
                //noticeUtil.send(true, "工单通知", "您有指派的报修工单，请尽快处理", handler);


                noticeUtil.send(true, "维修任务提醒",

                        "您有被指派的报修工单，请尽快处理<br>" +
                                "联系人：" + repairRecordInfoVo.getPersonName() + "<br>" +
                                "联系电话：" + repairRecordInfoVo.getPhoneNumber() + "<br>" +
                                "派单时间：" + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()) + "<br>" +
                                "报修内容：" + projectRepairRecord.getContent() + "<br>"
                        , handler);


                ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getByTelephone(projectRepairRecord.getPhoneNumber());
                String format="";
                if (projectRepairRecord.getReserveTimeEnd()!=null){
                    format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(projectRepairRecord.getReserveTimeEnd());
                }
                if (projectPersonInfo==null){
                    WebSocketNotifyUtil.sendMessgae(ProjectContextHolder.getProjectId().toString(), JSONObject.toJSONString(projectWebSocketService.findNumByProjectId()));
                    return R.failed("该手机号业主不存在");
                }

                noticeUtil.send(false, "报修处理进展通知",

                        "报修处理完成 <br>" +
                                "<br>报修位置：  " + repairRecordInfoVo.getCurrentHouseName() +
                                "<br>报修主题：  " + projectRepairRecord.getContent() +
                                "<br>报修时间：  " + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(projectRepairRecord.getCreateTime()) +
                                "<br>当前进展：  " + stausTyp +
                                "<br> 预计完成日期: "+format
                        , projectPersonInfo.getPersonId());


            } catch (Exception e) {
                log.warn("消息发送异常", e);
            }
            return R.ok();

        } else {
            return R.failed("没有对应的报修单据");
        }
    }

    @Override
    public R setResult(ProjectRepairRecordResultFormVo formVo) {
        ProjectRepairRecord projectRepairRecord = getById(formVo.getRepairId());
        ProjectStaffVo projectStaffVo = projectStaffService.getStaffByOwner();
        ProjectRepairRecordInfoVo repairRecordInfoVo = getByRepairId(formVo.getRepairId());

        if (ObjectUtil.isEmpty(projectStaffVo)) {
            return R.failed("找不到当前项目下的员工信息");
        }
        if (!(projectStaffVo.getStaffId().equals(projectRepairRecord.getHandler()))) {
            return R.failed("必须是处理人才能处理该订单");
        }
        if (ObjectUtil.isNotEmpty(projectRepairRecord)) {
            BeanUtils.copyProperties(formVo, projectRepairRecord);
            projectRepairRecord.setDoneTime(LocalDateTime.now());
            projectRepairRecord.setStatus(ApproveStatusEnum.finish.code);
            baseMapper.updateById(projectRepairRecord);
            String stausTyp = getStatusByType(projectRepairRecord.getStatus(), "maintain_status");
            try {
                ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getByTelephone(projectRepairRecord.getPhoneNumber());

                String format="";
                if (projectRepairRecord.getReserveTimeEnd()!=null){
                    format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(projectRepairRecord.getReserveTimeEnd());
                }
                noticeUtil.send(false, "报修处理进展通知",

                        "报修处理完成" +
                                "报修位置：  " + repairRecordInfoVo.getCurrentHouseName() + "<br />"+
                                "报修主题：  " + projectRepairRecord.getContent() +"<br />"+
                                "报修时间：  " + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(projectRepairRecord.getCreateTime()) +"<br />"+
                                "当前进展：  " + stausTyp +"<br />"+
                                " 预计完成日期: "+ format+ "<br />"
                        , projectPersonInfo.getPersonId());


                //noticeUtil.send(false, "报修状态变更", "您的报修请求已处理完成", projectPersonInfo.getPersonId());


            } catch (Exception e) {
                log.warn("消息发送异常", e);
            }
            return R.ok();
        } else {
            return R.failed("没有对应的报修单据");
        }
    }


    @Override

    public R request(ProjectRepairRecordRequestFormVo formVo) {
        ProjectRepairRecord projectRepairRecord = new ProjectRepairRecord();
        BeanUtils.copyProperties(formVo, projectRepairRecord);
        //用户名即为电话号码通过电话关联业主游客信息
        ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getPersonByOwner();
        if (ObjectUtil.isEmpty(projectPersonInfo)) {
            return R.failed("用户在当前项目下不存在");
        }
        projectRepairRecord.setPersonName(projectPersonInfo.getPersonName());
        projectRepairRecord.setPhoneNumber(projectPersonInfo.getTelephone());
        projectRepairRecord.setStatus(ApproveStatusEnum.untreated.code);
        baseMapper.insert(projectRepairRecord);
        return R.ok();

    }

    @Override
    public Integer countByMonth(String date) {
        return baseMapper.countByMonth(date);
    }

    @Override
    public Integer countByOff() {
        Integer projectId = ProjectContextHolder.getProjectId();
        return baseMapper.countByOff();
    }

    @Override
    public Integer countStatusByStaffId(String staffId, List<String> status, LocalDate date) {
        if (StringUtil.isEmpty(staffId) || CollUtil.isEmpty(status)) {
            throw new RuntimeException("未获取到原ID和状态");
        }
        return baseMapper.countStatusByStaffId(staffId, status, date);
    }

    @Override
    public Integer countCompleteByStaffId(String staffId, LocalDate date) {
        List<String> status = new ArrayList<>();
        status.add(ApproveStatusEnum.finish.code);
        return this.countStatusByStaffId(staffId, status, date);
    }

    @Override
    public Integer countUnCompleteByStaffId(String staffId, LocalDate date) {
        List<String> status = new ArrayList<>();
        status.add(ApproveStatusEnum.processing.code);
        return this.countStatusByStaffId(staffId, status, date);
    }

    @Override
    public Integer countAllByStaffId(String staffId, LocalDate date) {
        List<String> status = new ArrayList<>();
        status.add(ApproveStatusEnum.finish.code);
        status.add(ApproveStatusEnum.processing.code);
        return this.countStatusByStaffId(staffId, status, date);
    }

    @Override
    public ProjectStaffWorkVo getCount(String staffId, String date) {
        if (StringUtil.isEmpty(staffId)) {
            throw new RuntimeException("未获取到员工ID和状态");
        }
        ProjectStaffWorkVo staffWorkVo = new ProjectStaffWorkVo();
        staffWorkVo.setCompletedTaskNum(baseMapper.getCount(staffId, ApproveStatusEnum.finish.code, date));
        staffWorkVo.setUnCompletedTaskNum(baseMapper.getCount(staffId, ApproveStatusEnum.processing.code, date));
        staffWorkVo.setAllTaskNum(staffWorkVo.getCompletedTaskNum() + staffWorkVo.getUnCompletedTaskNum());
        return staffWorkVo;
    }


    @Override
    public String getStatusByType(String status, String type) {
        return baseMapper.getStatusByType(status, type);
    }

    @Override
    public R receivingOrders(String recordId, String handler) {
        ProjectRepairRecord projectRepairRecord = getById(recordId);
        ProjectRepairRecordInfoVo repairRecordInfoVo = getByRepairId(recordId);
        if (ObjectUtil.isNotEmpty(projectRepairRecord)) {
            projectRepairRecord.setHandler(handler);
            projectRepairRecord.setOrderTime(LocalDateTime.now());
            projectRepairRecord.setStatus(ApproveStatusEnum.processing.code);
            baseMapper.updateById(projectRepairRecord);
            String stausTyp = getStatusByType(projectRepairRecord.getStatus(), "maintain_status");
            try {
                ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getByTelephone(projectRepairRecord.getPhoneNumber());
                //noticeUtil.send(false, "报修状态变更", "您的报修请求正在处理", projectPersonInfo.getPersonId());
                String format="";
                if (projectRepairRecord.getReserveTimeEnd()!=null){
                     format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(projectRepairRecord.getReserveTimeEnd());
                }

                if (projectPersonInfo==null){
                    WebSocketNotifyUtil.sendMessgae(ProjectContextHolder.getProjectId().toString(), JSONObject.toJSONString(projectWebSocketService.findNumByProjectId()));
                    return R.failed("该手机号业主不存在");
                }
                noticeUtil.send(false, "报修处理进展通知",


                        "报修状态变更 " +"<br /> "+
                                "报修位置： " + repairRecordInfoVo.getCurrentHouseName() +"<br /> "+
                                "报修主题：  " + projectRepairRecord.getContent() +"<br /> " +
                                "报修时间：  " + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(projectRepairRecord.getCreateTime()) + "<br /> " +
                                "当前进展：  " + stausTyp +"<br /> " +
                                " 预计完成日期: " + format+ "<br />"
                        , projectPersonInfo.getPersonId());


                // noticeUtil.send(false, "报修状态变更", "您的报修请求正在处理", projectPersonInfo.getPersonId());
            } catch (Exception e) {
                e.printStackTrace();
                log.warn("消息发送异常", e);
            }
            return R.ok();
        } else {
            return R.failed("没有对应的报修单据");
        }
    }

}
