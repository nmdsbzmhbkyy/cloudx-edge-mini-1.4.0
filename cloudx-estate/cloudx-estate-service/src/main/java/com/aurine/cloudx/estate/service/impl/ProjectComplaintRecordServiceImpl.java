package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.feign.RemoteWebSocketService;
import com.aurine.cloudx.estate.util.NoticeUtil;
import com.aurine.cloudx.estate.constant.enums.ApproveStatusEnum;
import com.aurine.cloudx.estate.entity.ProjectComplaintCommunicateHis;
import com.aurine.cloudx.estate.entity.ProjectComplaintRecord;
import com.aurine.cloudx.estate.entity.ProjectFrameInfo;
import com.aurine.cloudx.estate.entity.ProjectPersonInfo;
import com.aurine.cloudx.estate.entity.ProjectStaff;
import com.aurine.cloudx.estate.mapper.ProjectComplaintRecordMapper;
import com.aurine.cloudx.estate.service.ProjectBuildingInfoService;
import com.aurine.cloudx.estate.service.ProjectComplaintCommunicateHisService;
import com.aurine.cloudx.estate.service.ProjectComplaintRecordService;
import com.aurine.cloudx.estate.service.ProjectFrameInfoService;
import com.aurine.cloudx.estate.service.ProjectPersonInfoService;
import com.aurine.cloudx.estate.service.ProjectStaffService;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.api.feign.RemoteUserService;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目投诉服务记录
 *
 * @author guhl@aurine.cn
 * @date 2020-07-20 13:34:42
 */
@Service
@AllArgsConstructor
@Slf4j
public class ProjectComplaintRecordServiceImpl extends ServiceImpl<ProjectComplaintRecordMapper, ProjectComplaintRecord> implements ProjectComplaintRecordService {
    @Resource
    private ProjectComplaintCommunicateHisService projectComplaintCommunicateHisService;
    @Resource
    private ProjectFrameInfoService projectFrameInfoService;
    @Resource
    private RemoteUserService remoteUserService;
    @Resource
    private ProjectBuildingInfoService projectBuildingInfoService;
    @Resource
    private ProjectStaffService projectStaffService;
    @Resource
    private ProjectPersonInfoService projectPersonInfoService;
    @Resource
    NoticeUtil noticeUtil;
    @Resource
    private RemoteWebSocketService remoteWebSocketService;

    @Override
    public Page<ProjectComplaintRecordPageVo> pageComplaintRecord(Page<ProjectComplaintRecordPageVo> page, ProjectComplaintRecordPageVo projectComplaintRecordPageVo) {
        return baseMapper.pageComplaintRecord(page, projectComplaintRecordPageVo);
    }

    @Override
    public ProjectComplaintRecordInfoVo getByComplainId(String complaintId) {
        ProjectComplaintRecord projectComplaintRecord = super.getById(complaintId);
        if (BeanUtil.isEmpty(projectComplaintRecord)) {
            return null;
        }
        ProjectComplaintRecordInfoVo projectComplaintRecordInfoVo = new ProjectComplaintRecordInfoVo();
        BeanUtils.copyProperties(projectComplaintRecord, projectComplaintRecordInfoVo);
        String houseId = projectComplaintRecord.getHouseId();
        //获取房屋实体
        ProjectFrameInfo projectFrameInfo = projectFrameInfoService.getById(houseId);
        if (BeanUtil.isNotEmpty(projectFrameInfo)) {
            //获取单元实体
            ProjectFrameInfo projectFrameInfoUnit = projectFrameInfoService.getById(projectFrameInfo.getPuid());
            //获取楼栋实体
            ProjectFrameInfo projectFrameInfoBuilding = projectFrameInfoService.getById(projectFrameInfoUnit.getPuid());
            //获取该楼栋地址
            String address = projectBuildingInfoService.getById(projectFrameInfoBuilding.getEntityId()).getAddress();
            //获取组团信息
            String groupName = projectFrameInfoService.getFrameNameById(projectFrameInfoBuilding.getEntityId());
            //如果存在组团 对组团进行拼接
            if (StrUtil.isNotEmpty(groupName)) {
                List<String> groupNameList = CollUtil.reverse(StrUtil.split(groupName, ','));
                String buildingName = groupNameList.toString().replace("[", "")
                        .replace("]", "").replace(",", "-").replace(" ", "");
                projectComplaintRecordInfoVo.setCurrentHouseName(buildingName + projectFrameInfoUnit.getEntityName() + projectFrameInfo.getEntityName());
            } else {
                projectComplaintRecordInfoVo.setCurrentHouseName(projectFrameInfoBuilding.getEntityName() + projectFrameInfoUnit.getEntityName() + projectFrameInfo.getEntityName());
            }
        }
        if (StringUtil.isNotEmpty(projectComplaintRecord.getHandler())) {
            ProjectStaff projectStaff = projectStaffService.getById(projectComplaintRecord.getHandler());
            if (BeanUtil.isNotEmpty(projectStaff)) {
                projectComplaintRecordInfoVo.setHandlerName(projectStaff.getStaffName());
                projectComplaintRecordInfoVo.setPhone(projectStaff.getMobile());
            }
        }
        return projectComplaintRecordInfoVo;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean removeByComplainId(String complaintId) {
        List<ProjectComplaintCommunicateHis> list = projectComplaintCommunicateHisService.list(Wrappers.lambdaQuery(ProjectComplaintCommunicateHis.class)
                .eq(ProjectComplaintCommunicateHis::getComplaintId, complaintId));
        List<String> recordIds = new ArrayList<>();
        list.forEach(e -> {
            recordIds.add(e.getRecordId());
        });
        projectComplaintCommunicateHisService.removeByIds(recordIds);
        return removeById(complaintId);
    }

    @Override
    public Page<ProjectComplaintRecordVo> pageByType(Page page, String type, String status, String handler, String phone, String date) {
        return baseMapper.pageByType(page, type, status, handler, phone, date);
    }

    @Override
    public R setHandler(String complaintId, String handler) {
        ProjectComplaintRecord projectComplaintRecord = getById(complaintId);

        if (ObjectUtil.isNotEmpty(projectComplaintRecord)) {
            projectComplaintRecord.setHandler(handler);
            projectComplaintRecord.setOrderTime(LocalDateTime.now());
            projectComplaintRecord.setStatus(ApproveStatusEnum.processing.code);
            baseMapper.updateById(projectComplaintRecord);
            try {
                ProjectStaffVo projectStaffVo = projectStaffService.getStaffByOwner();
                if (!handler.equals(projectStaffVo.getStaffId())) {
                    noticeUtil.send(true, "工单通知", "您有指派的投诉工单，请尽快处理", handler);
                }
                ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getByTelephone(projectComplaintRecord.getPhoneNumber());
                noticeUtil.send(false, "投诉状态变更", "您的投诉意见正在处理", projectPersonInfo.getPersonId());

            } catch (Exception e) {
                log.warn("消息发送异常", e);
            }

            return R.ok();
        } else {
            return R.failed("没有对应的投诉单据");
        }
    }

    @Override
    public R receivingOrders(String complaintId, String handler) {
        ProjectComplaintRecord projectComplaintRecord = getById(complaintId);

        if (ObjectUtil.isNotEmpty(projectComplaintRecord)) {
            projectComplaintRecord.setHandler(handler);
            projectComplaintRecord.setOrderTime(LocalDateTime.now());
            projectComplaintRecord.setStatus(ApproveStatusEnum.processing.code);
            baseMapper.updateById(projectComplaintRecord);
            return R.ok();
        } else {
            return R.failed("没有对应的投诉单据");
        }
    }

    @Override
    public R setResult(ProjectComplainRecordResultFormVo formVo) {
        ProjectComplaintRecord projectComplaintRecord = getById(formVo.getComplaintId());

        ProjectStaffVo projectStaffVo = projectStaffService.getStaffByOwner();
        if (ObjectUtil.isEmpty(projectStaffVo)) {
            return R.failed("找不到当前项目下的员工信息");
        }
        if (!(projectStaffVo.getStaffId().equals(projectComplaintRecord.getHandler()))) {
            return R.failed("必须是处理人才能处理该订单");
        }


        if (ObjectUtil.isNotEmpty(projectComplaintRecord)) {
            BeanUtils.copyProperties(formVo, projectComplaintRecord);
            projectComplaintRecord.setDoneTime(LocalDateTime.now());
            projectComplaintRecord.setStatus(ApproveStatusEnum.finish.code);
            baseMapper.updateById(projectComplaintRecord);
            try {
                ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getByTelephone(projectComplaintRecord.getPhoneNumber());
                //业主投诉消息完成通知
                noticeUtil.send(false, "投诉状态变更", "您的投诉意见已处理完成", projectPersonInfo.getPersonId());

            } catch (Exception e) {
                log.warn("消息发送异常", e);
            }
            return R.ok();
        } else {
            return R.failed("没有对应的投诉单据");
        }
    }

    @Override
    public R request(ProjectComplainRecordRequestFormVo formVo) {
        ProjectComplaintRecord projectComplaintRecord = new ProjectComplaintRecord();
        BeanUtils.copyProperties(formVo, projectComplaintRecord);
        //用户名即为电话号码通过电话关联业主游客信息
        ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getPersonByOwner();
        if (ObjectUtil.isEmpty(projectPersonInfo)) {
            return R.failed("用户在当前项目下不存在");
        }
        projectComplaintRecord.setPersonName(projectPersonInfo.getPersonName());
        projectComplaintRecord.setPhoneNumber(projectPersonInfo.getTelephone());
        projectComplaintRecord.setStatus(ApproveStatusEnum.untreated.code);
        baseMapper.insert(projectComplaintRecord);
        remoteWebSocketService.transferSocket(ProjectContextHolder.getProjectId().toString());
        return R.ok();

    }

    @Override
    public Integer countByMonth(String date) {
        return baseMapper.countByMonth(date);
    }

    @Override
    public Integer countByOff() {
        return baseMapper.countByOff();
    }

    @Override
    public Integer countStatusByStaffId(String staffId, List<String> status, LocalDate date) {
        if (StringUtil.isEmpty(staffId) || CollUtil.isEmpty(status)) {
            throw new RuntimeException("未获取到员工ID和状态");
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
        staffWorkVo.setCompletedTaskNum(baseMapper.getCount(staffId, ApproveStatusEnum.finish.code,  date));
        staffWorkVo.setUnCompletedTaskNum(baseMapper.getCount(staffId, ApproveStatusEnum.processing.code,  date));
        staffWorkVo.setAllTaskNum(staffWorkVo.getCompletedTaskNum() + staffWorkVo.getUnCompletedTaskNum());
        return staffWorkVo;
    }

}
