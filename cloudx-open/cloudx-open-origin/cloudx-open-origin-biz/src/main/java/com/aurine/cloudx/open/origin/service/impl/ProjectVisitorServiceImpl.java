package com.aurine.cloudx.open.origin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.open.common.core.util.MessageTextUtil;
import com.aurine.cloudx.open.common.entity.vo.VisitorInfoVo;
import com.aurine.cloudx.open.origin.constant.ProjectConfigConstant;
import com.aurine.cloudx.open.origin.constant.VisitorIsLeaveConstant;
import com.aurine.cloudx.open.origin.constant.enums.AuditStatusEnum;
import com.aurine.cloudx.open.origin.constant.enums.DataOriginExEnum;
import com.aurine.cloudx.open.origin.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.open.origin.constant.enums.RegisterTypeEnum;
import com.aurine.cloudx.open.origin.dto.ProjectPersonDeviceDTO;
import com.aurine.cloudx.open.origin.entity.*;
import com.aurine.cloudx.open.origin.mapper.ProjectVisitorMapper;
import com.aurine.cloudx.open.origin.service.*;
import com.aurine.cloudx.open.origin.util.NoticeUtil;
import com.aurine.cloudx.open.origin.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.admin.api.feign.RemoteUserService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * <p>
 * 访客业务实现
 * </p>
 *
 * @ClassName: ProjectVisitorServiceImpl
 * @author: 王良俊
 * @date: 2020/6/4 10:11
 * @Copyright:
 */
@Service
@Slf4j
@Primary
public class ProjectVisitorServiceImpl extends ServiceImpl<ProjectVisitorMapper, ProjectVisitor> implements ProjectVisitorService {

    @Resource
    private ProjectVisitorMapper projectVisitorMapper;
    @Resource
    private ProjectVisitorHisService projectVisitorHisService;
    @Resource
    private ProjectCardService projectCardService;
    @Resource
    private ProjectFaceResourcesService projectFaceResourcesService;
    @Resource
    private ProjectPersonDeviceService projectPersonDeviceService;
    @Resource
    private ProjectConfigService projectConfigService;
    @Resource
    private ProjectDeviceInfoProxyService projectDeviceInfoProxyService;

    @Resource
    private ProjectRightDeviceService projectRightDeviceService;
    @Resource
    private RemoteUserService remoteUserService;
    @Resource
    private ProjectStaffService projectStaffService;
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
//    @Resource
//    private TaskUtil taskUtil;

    @Resource
    private NoticeUtil noticeUtil;
//    @Resource
//    private AbstractProjectVisitorService abstractWebProjectVisitorService;


    private static final String TIME_FORMAT3 = "yyyy-MM-dd HH:mm";

    // 访客审核菜单ID
    private final Integer visitorAuditMenuId = 10678;

    @Override
    public void init() {
//        List<ProjectVisitorRecordVo> unLeaveList = this.getAllUnLeaveListToday();
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        unLeaveList.forEach(record -> {
//            LocalDateTime startTime = LocalDateTime.parse(record.getStartTime(), dateTimeFormatter);
//            LocalDateTime endTime = LocalDateTime.parse(record.getEndTime(), dateTimeFormatter);
//            if (TaskUtil.isToday(startTime)) {
//                taskUtil.addDelayTaskAsync(new VisitorDelayTask(ProjectContextHolder.getProjectId(), record.getVisitId(), startTime, DelayTaskTopicEnum.visitorSendCert));
//            }
//            if (TaskUtil.isToday(endTime)) {
//                taskUtil.addDelayTaskAsync(new VisitorDelayTask(ProjectContextHolder.getProjectId(), record.getVisitId(), endTime, DelayTaskTopicEnum.visitorCheckOut));
//            }
//
//        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_UNCOMMITTED)
    public boolean register(ProjectVisitorVo projectVisitorVo) {
        // 这里先生成访客记录的ID
        String visitId = UUID.randomUUID().toString().replaceAll("-", "");
        ProjectVisitor projectVisitor = new ProjectVisitor();
        BeanUtil.copyProperties(projectVisitorVo, projectVisitor);

        //如果是来源于访客则直接保存访客的userId
        if (DataOriginExEnum.FK.code.equals(projectVisitorVo.getOriginEx())) {
            projectVisitor.setUserId(SecurityUtils.getUser().getId());

        } else {
            //否则 根据手机号查询账号设置userId 如果没有则不做处理
            R<SysUser> requestUser = remoteUserService.user(projectVisitorVo.getMobileNo());
            if (requestUser.getCode() == 0 && ObjectUtil.isNotEmpty(requestUser.getData())) {
                projectVisitor.setUserId(requestUser.getData().getUserId());
            }
        }

        if (RegisterTypeEnum.independentApplication.code.equals(projectVisitorVo.getRegisterType()) && StrUtil.isEmpty(projectVisitorVo.getVisitPersonId())) {
            List<ProjectDeviceInfo> deviceList = projectDeviceInfoProxyService.listAllGateDevice();
            List<String> deviceIdList = deviceList.stream().map(ProjectDeviceInfo::getDeviceId).collect(Collectors.toList());
            String[] arr = new String[deviceIdList.size()];
            projectVisitorVo.setDeviceIdArray(deviceIdList.toArray(arr));
        }
        boolean visitorSave = true;
        // 判断这个手机号是否已经有访客了，如果有的话不添加访客而是更新访客信息
        List<ProjectVisitor> visitorList = this.list(new QueryWrapper<ProjectVisitor>().lambda()
                .eq(ProjectVisitor::getMobileNo, projectVisitorVo.getMobileNo()));
        if (CollUtil.isEmpty(visitorList)) {
            visitorSave = save(projectVisitor);
        } else {
            projectVisitor.setVisitorId(visitorList.get(0).getVisitorId());
            updateById(projectVisitor);
        }
        String visitorId = projectVisitor.getVisitorId();
        // 如果访客保存失败直接返回
        if (!visitorSave) {
            return false;
        }

        String[] timeRange = projectVisitorVo.getTimeRange();
        String eff = timeRange[0];
        String exp = timeRange[1];
        DateTimeFormatter df = DateTimeFormatter.ofPattern(TIME_FORMAT3);
        LocalDateTime effTime = LocalDateTime.parse(eff, df);
        LocalDateTime expTime = LocalDateTime.parse(exp, df);

        // 保存人和设备关系 （这里访客的权限适合记录绑定的不在和访客绑定了）
        projectPersonDeviceService.savePersonDevice(visitId, PersonTypeEnum.VISITOR, projectVisitorVo.getDeviceIdArray(), effTime, expTime);

        List<ProjectCard> cardList = projectVisitorVo.getCardList();
        List<ProjectFaceResources> faceList = projectVisitorVo.getFaceList();
//        List<ProjectPasswd> passwordList = projectVisitorVo.getPasswordList();

        // 初始化要保存的访客记录数据
        ProjectVisitorHis projectVisitorHis = new ProjectVisitorHis();
        BeanUtil.copyProperties(projectVisitorVo, projectVisitorHis);
        projectVisitorHis.setVisitorId(visitorId);
        projectVisitorHis.setVisitorName(projectVisitorVo.getPersonName());
        projectVisitorHis.setPassBeginTime(effTime);
        projectVisitorHis.setPassEndTime(expTime);
        projectVisitorHis.setCreateTime(null);
        projectVisitorHis.setUpdateTime(null);
        ProjectConfig config = projectConfigService.getConfig();
        if (StringUtils.isBlank(projectVisitorVo.getAuditStatus())) {
            projectVisitorHis.setAuditStatus(AuditStatusEnum.waitAudit.code);
        }
        projectVisitorHis.setIsLeave(VisitorIsLeaveConstant.UNLEAVE);
        projectVisitorHis.setVisitId(visitId);

        boolean save = projectVisitorHisService.save(projectVisitorHis);


        // 对三种介质进行增加或删除操作 （这里介质适合访客记录绑定的）
        if (CollUtil.isNotEmpty(projectVisitorVo.getCardList())) {
            projectCardService.operateCard(cardList, visitId);
        }
        if (CollUtil.isNotEmpty(projectVisitorVo.getFaceList())) {
            projectFaceResourcesService.operateFace(faceList, visitId);
        }
        /*if (CollUtil.isNotEmpty(projectVisitorVo.getPasswordList())) {
            projectPasswdService.operatePassword(passwordList, visitorId);
        }*/

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        // 如果是物业登记直接执行通过审核操作
        if (RegisterTypeEnum.propertyRegistration.code.equals(projectVisitorVo.getRegisterType())) {
//            abstractWebProjectVisitorService.passAudit(visitId, AuditStatusEnum.pass);
        }
        List<String> staffIdList = projectStaffService.getStaffIdListByMenuId(visitorAuditMenuId);
        //如果是业主登记且是系统审核 直接通过
        if (RegisterTypeEnum.residentApplication.code.equals(projectVisitorVo.getRegisterType())) {
            if (config.getVisitorAudit().equals(ProjectConfigConstant.SYSTEM_IDENTITY)) {
//                abstractWebProjectVisitorService.passAudit(visitId, AuditStatusEnum.pass);
            } else {
                // 如果没有系统审核这里就需要通知物业进行审核
                if (CollUtil.isNotEmpty(staffIdList)) {
                    String message = MessageTextUtil.init()
                            .append("有新的访客申请，请尽快审核")
                            .p("访客姓名：%s", projectVisitorHis.getVisitorName() == null ? "" : projectVisitorHis.getVisitorName())
                            .p("来访日期：%s", DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH时mm分").format(projectVisitorHis.getPassBeginTime()))
                            .p("来访事由：%s", projectVisitorHis.getReason() == null ? "" : projectVisitorHis.getReason())
                            .toString();
                    noticeUtil.send(true, "访客预约来访审核提醒", message, staffIdList);
                }
            }
        } else if (RegisterTypeEnum.independentApplication.code.equals(projectVisitorVo.getRegisterType())) {

            if (StringUtils.isBlank(projectVisitorVo.getVisitPersonId())
                    && config.getVisitorAudit().equals(ProjectConfigConstant.SYSTEM_IDENTITY)) {
//                abstractWebProjectVisitorService.passAudit(visitId, AuditStatusEnum.pass);
            } else if (StringUtils.isBlank(projectVisitorVo.getVisitPersonId()) && !config.getVisitorAudit().equals(ProjectConfigConstant.SYSTEM_IDENTITY)) {
                if (CollUtil.isNotEmpty(staffIdList)) {
                    String message = MessageTextUtil.init()
                            .append("有新的访客申请，请尽快审核")
                            .p("访客姓名：%s", projectVisitorHis.getVisitorName() == null ? "" : projectVisitorHis.getVisitorName())
                            .p("来访日期：%s", DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH时mm分").format(projectVisitorHis.getPassBeginTime()))
                            .p("来访事由：%s", projectVisitorHis.getReason() == null ? "" : projectVisitorHis.getReason())
                            .toString();
                    noticeUtil.send(true, "访客预约来访审核提醒", message, staffIdList);
                }
            } else if (StringUtils.isNotEmpty(projectVisitorVo.getVisitPersonId())) {
                String message = MessageTextUtil.init()
                        .append("您有新的访客，请尽快确认")
                        .p("访客姓名：%s", projectVisitorHis.getVisitorName() == null ? "" : projectVisitorHis.getVisitorName())
                        .p("来访日期：%s", DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH时mm分").format(projectVisitorHis.getPassBeginTime()))
                        .p("来访事由：%s", projectVisitorHis.getReason() == null ? "" : projectVisitorHis.getReason())
                        .toString();
                noticeUtil.send(false, "访客预约来访审核提醒", message, projectVisitorHis.getVisitPersonId());
            }
        }

        return save;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean passAudit(String visitId, AuditStatusEnum auditStatus) {
        List<ProjectVisitorHis> visitorHisList = projectVisitorHisService.list(new QueryWrapper<ProjectVisitorHis>()
                .lambda().eq(ProjectVisitorHis::getVisitId, visitId));
        if (CollUtil.isNotEmpty(visitorHisList)) {
            ProjectVisitorHis projectVisitorHis = visitorHisList.get(0);
            projectVisitorHis.setAuditStatus(auditStatus.code);
            boolean updateById = projectVisitorHisService.updateById(projectVisitorHis);

            LocalDateTime passBeginTime = projectVisitorHis.getPassBeginTime();

            // 这里获取到通行结束时间的LocalDateTime对象 (延时任务添加)
            LocalDateTime passEndDateTime = projectVisitorHis.getPassEndTime();
//            if (TaskUtil.isToday(passEndDateTime)) {
//                // 如果是今天就签离的就要添加到延时任务里面了
//                VisitorDelayTask visitorDelayTask = new VisitorDelayTask(ProjectContextHolder.getProjectId(),
//                        projectVisitorHis.getVisitId(), passEndDateTime, DelayTaskTopicEnum.visitorCheckOut);
//                taskUtil.addDelayTask(visitorDelayTask);
////                DelayTaskUtil.instance().addDelayTask(new DelayTask(passEndDateTime, ProjectContextHolder.getProjectId(), DelayTaskTopicEnum.checkOutVisitor));
//            }
//            // 只有今天的才添加到任务中
//            if (TaskUtil.isToday(passBeginTime)) {
//                VisitorDelayTask visitorDelayTask = new VisitorDelayTask(ProjectContextHolder.getProjectId(),
//                        projectVisitorHis.getVisitId(), passBeginTime, DelayTaskTopicEnum.visitorSendCert);
//                taskUtil.addDelayTask(visitorDelayTask);
////                DelayTaskUtil.instance().addDelayTask(new DelayTask(passBeginTime, ProjectContextHolder.getProjectId(), DelayTaskTopicEnum.visitorSendCert));
//            }
            // 这里对通行开始时间进行判断是否要直接进行下发
            /*LocalDateTime minus = passBeginTime.minus(10, ChronoUnit.MINUTES);
            Duration between = Duration.between(LocalDateTime.now(), minus);

            TimeCircleUtil timeCircleUtil = new TimeCircleUtil(jobConfig.getCircle());
            // 这里如果判断为依据错过了任务周期就会直接下发(这里如果这个申请已经在现在之前的时间了也是直接下发)
            if (between.toMinutes() <= 0 || timeCircleUtil.checkHasBeenMissed(passBeginTime.toLocalTime())) {
                this.sendCert(projectVisitorHis);
            }*/
            this.sendMessage(projectVisitorHis, auditStatus);
            return updateById;
        }
        return true;
    }

    private void sendMessage(ProjectVisitorHis visitorHis, AuditStatusEnum auditStatus) {
        // 业主申请的
        if (visitorHis.getOriginEx().equals(DataOriginExEnum.YZ.code) && VisitorIsLeaveConstant.UNLEAVE.equals(visitorHis.getIsLeave())) {
            try {
                ProjectVisitorVo projectVisitor = getDataById(visitorHis.getVisitId());
                if (auditStatus.equals(AuditStatusEnum.pass)) {
                    noticeUtil.send(false, "审核结果通知",
                            MessageTextUtil.init()
                                    .append("您邀请的访客：%s，已通过审核", projectVisitor.getVisitorName() == null ? "" : projectVisitor.getVisitorName())
                                    .p("审核结果：已通过")
                                    .p("审核时间：%s", DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH时mm分").format(LocalDateTime.now()))
                                    .p("备注：")
                                    .toString()
                            , projectVisitor.getVisitPersonId());
                }
            } catch (Exception e) {
                log.warn("消息发送异常", e);
            }
        }
        // 访客自主申请的
        if (visitorHis.getOriginEx().equals(DataOriginExEnum.FK.code) && RegisterTypeEnum.independentApplication.code.equals(visitorHis.getRegisterType())) {
            try {
                ProjectVisitorVo projectVisitor = getDataById(visitorHis.getVisitId());
                if (auditStatus.equals(AuditStatusEnum.pass)) {
                    noticeUtil.send(false, "访客来访已通过", "您的访客来访审核已通过", projectVisitor.getVisitPersonId());
                } else if (auditStatus.equals(AuditStatusEnum.inAudit)) {
                    // 这里是业主审核通过后需要进行物业审核，所以审核状态仍然是审核中
                    List<String> staffIdList = projectStaffService.getStaffIdListByMenuId(visitorAuditMenuId);
                    if (CollUtil.isNotEmpty(staffIdList)) {
                        String message = MessageTextUtil.init()
                                .append("有新的访客申请，请尽快审核")
                                .p("访客姓名：%s", visitorHis.getVisitorName() == null ? "" : visitorHis.getVisitorName())
                                .p("来访日期：%s", DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH时mm分").format(visitorHis.getPassBeginTime()))
                                .p("来访事由：%s", visitorHis.getReason() == null ? "" : visitorHis.getReason())
                                .toString();
                        noticeUtil.send(true, "访客预约来访审核提醒", message, staffIdList);
                    }
                }
            } catch (Exception e) {
                log.warn("消息发送异常", e);
            }
        }
    }

    @Override
    public boolean signOff(String visitId) {
        List<ProjectVisitorHis> visitorHisList = projectVisitorHisService.list(new QueryWrapper<ProjectVisitorHis>()
                .lambda().eq(ProjectVisitorHis::getVisitId, visitId));
        if (CollUtil.isNotEmpty(visitorHisList)) {
            ProjectVisitorHis projectVisitorHis = visitorHisList.get(0);
            projectVisitorHis.setLeaveTime(LocalDateTime.now());
            // 设置为签离状态
            projectVisitorHis.setIsLeave(VisitorIsLeaveConstant.LEAVE);
            // 移除人员和人员介质的设备权限
            removeAuthByVisitId(visitId);
            // 这里删除访客的介质
            projectCardService.remove(new QueryWrapper<ProjectCard>().lambda()
                    .eq(ProjectCard::getPersonId, visitId));
            projectFaceResourcesService.remove(new QueryWrapper<ProjectFaceResources>().lambda()
                    .eq(ProjectFaceResources::getPersonId, visitId));
            return projectVisitorHisService.updateById(projectVisitorHis);
        }
        return true;
    }

    /**
     * <p>
     * 申请被签离 删除人员和设备的权限包括其介质的权限
     * </p>
     *
     * @param visitId 记录id这里作为人员ID使用
     * @author: 王良俊
     */
    private void removeAuthByVisitId(String visitId) {
        projectRightDeviceService.removeCertDeviceAuthorize(visitId);
        // 删除人员设备权限 签离操作应该不需要删除任何设备权限
//        projectPersonDeviceService.deleteByPersobaseHousePersonRelbaseHousePersonRelnId(visitorId);
    }

    @Override
    public Boolean passAuditBatch(List<ProjectVisitorVo> visitorVoList) {
        if (CollUtil.isNotEmpty(visitorVoList)) {
            List<String> visitIdList = visitorVoList.stream().map(ProjectVisitorVo::getVisitId).collect(Collectors.toList());
            List<ProjectVisitorHis> projectVisitorHis = projectVisitorHisService.listByIds(visitIdList);
//            visitorVoList.forEach(visitorVo -> {
//                ProjectVisitorVo dataById = this.getDataById(visitorVo.getVisitId());
//                abstractWebProjectVisitorService.passAudit(dataById.getVisitId(), AuditStatusEnum.pass);
//            });

            Integer projectId = ProjectContextHolder.getProjectId();

            threadPoolTaskExecutor.execute(() -> {
                ProjectContextHolder.setProjectId(projectId);
                projectVisitorHis.forEach(visitorHis -> {
                    this.sendMessage(visitorHis, AuditStatusEnum.pass);
                });
            });
        }
        return true;
    }


    @Override
    public List<String> signOffAll() {
        // 这里的查询出来的就已经是应该要被签离的了
        List<ProjectVisitorRecordVo> allUnLeaveList = projectVisitorMapper.getAllUnLeaveList();
        log.info("开始签离项目ID：{}，数量：{}个", ProjectContextHolder.getProjectId(), allUnLeaveList.size());
        if (CollUtil.isNotEmpty(allUnLeaveList)) {
            List<String> visitIdList = allUnLeaveList.stream().map(ProjectVisitorRecordVo::getVisitId).collect(Collectors.toList());
            log.info("本次签离{}个访客", visitIdList.size());
            for (String visitId : visitIdList) {
                try {
                    // 这里对未审核的也进行签离
                    this.signOffByTask(visitId);
                } catch (Exception e) {
                    // 这里可能出现设备不存在导致异常这里直接跳过
                    log.info("设备不存在跳过本次签离操作，访客申请ID（visitId）：{}", visitId);
                }
            }
            return visitIdList;
        }
        return new ArrayList<>();
    }

    @Override
    public R signOffByTask(String visitId) {
        List<ProjectVisitorHis> visitorHisList = projectVisitorHisService.list(new QueryWrapper<ProjectVisitorHis>()
                .lambda().eq(ProjectVisitorHis::getVisitId, visitId));
        boolean timeout = false;
        if (CollUtil.isNotEmpty(visitorHisList)) {
            ProjectVisitorHis projectVisitorHis = visitorHisList.get(0);
            // 如果物业审核未通过则说明审核失败
            if (AuditStatusEnum.inAudit.code.equals(projectVisitorHis.getAuditStatus()) || AuditStatusEnum.waitAudit.code.equals(projectVisitorHis.getAuditStatus())) {
                projectVisitorHis.setAuditStatus(AuditStatusEnum.notPass.code);
                projectVisitorHis.setRejectReason("超期未审核");
                timeout = true;
            }
            projectVisitorHis.setLeaveTime(LocalDateTime.now());
            // 设置为签离状态
            projectVisitorHis.setIsLeave(VisitorIsLeaveConstant.LEAVE);
            // 移除人员和人员介质的设备权限
            removeAuthByVisitId(visitId);
            // 这里删除访客的介质
            projectCardService.remove(new QueryWrapper<ProjectCard>().lambda()
                    .eq(ProjectCard::getPersonId, visitId));
            projectFaceResourcesService.remove(new QueryWrapper<ProjectFaceResources>().lambda()
                    .eq(ProjectFaceResources::getPersonId, visitId));
            projectVisitorHisService.updateById(projectVisitorHis);
            if (timeout) {
                return R.failed("超期未审核");
            }
            try {
                noticeUtil.send(false, "已自动签离", "系统已自动签离你的访客申请", projectVisitorHis.getVisitorId());
            } catch (Exception e) {
                log.warn("消费发送异常", e);
            }

        }

        return R.ok();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delay(String visitId, String[] timeRange, String[] cycleRange) {
        ProjectVisitorHis visitorHis = projectVisitorHisService.getOne(new QueryWrapper<ProjectVisitorHis>()
                .lambda().eq(ProjectVisitorHis::getVisitId, visitId));
//        String visitorId = visitorHis.getVisitorId();
        LocalDateTime leaveTime = visitorHis.getLeaveTime();
        // 判断是否是在签离已经超过12小时
        /*if (BeanUtil.isNotEmpty(leaveTime)) {
            long between = DateUtil.between(Date.from(leaveTime.atZone(ZoneId.systemDefault()).toInstant()), DateUtil.date(), DateUnit.HOUR);
            if (between > 12) {
                return false;
            }
        }*/
        if (StrUtil.isNotBlank(visitId)) {
            List<ProjectPersonDevice> projectPersonDeviceList = projectPersonDeviceService.listByPersonId(visitId);
            List<String> deviceIdList = projectPersonDeviceList.stream().map(ProjectPersonDevice::getDeviceId).collect(Collectors.toList());

            DateTimeFormatter df = DateTimeFormatter.ofPattern(TIME_FORMAT3);
            LocalDateTime effTime = LocalDateTime.parse(timeRange[0], df);
            LocalDateTime expTime = LocalDateTime.parse(timeRange[1], df);

            ProjectVisitorHis projectVisitorHis = new ProjectVisitorHis();
            projectVisitorHis.setPassBeginTime(effTime);
            projectVisitorHis.setPassEndTime(expTime);
            projectVisitorHis.setVisitId(visitId);
            projectVisitorHis.setIsLeave(VisitorIsLeaveConstant.UNLEAVE);
            // 由于设置了延长访问时所以将离开时间设置为空
            projectVisitorHis.setLeaveTime(null);
            // 待审核状态
//            projectVisitorHis.setAuditStatus(AuditStatusEnum.waitAudit.code);
            projectRightDeviceService.authPersonCertmdiaDevice(deviceIdList, visitId);
            return projectVisitorHisService.updateById(projectVisitorHis);
        }
        return false;
    }

    @Override
    public Boolean rejectAudit(String visitId, String rejectReason) {
        ProjectVisitorHis projectVisitorHis = new ProjectVisitorHis();
        projectVisitorHis.setVisitId(visitId);
        projectVisitorHis.setRejectReason(rejectReason);
        projectVisitorHis.setAuditStatus(AuditStatusEnum.notPass.code);

        ProjectVisitorVo projectVisitorVo = projectVisitorMapper.getDataById(visitId);
//        removeAuthByVisitId(visitId);
        Boolean r = projectVisitorHisService.updateById(projectVisitorHis);
        //物业审核发送消息
        if (projectVisitorVo != null && AuditStatusEnum.inAudit.code.equals(projectVisitorVo.getAuditStatus())) {
            String text = "您邀请的访客：";
            if (DataOriginExEnum.FK.code.equals(projectVisitorVo.getOriginEx())) {
                text = "访客：";
            }
            try {
                String message = MessageTextUtil.init()
                        .append(text).append("%s，未通过审核", projectVisitorVo.getVisitorName() == null ? "" : projectVisitorVo.getVisitorName())
                        .p("审核结果：未通过")
                        .p("审核时间：【%s】", DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH时mm分").format(LocalDateTime.now()))
                        .p("备注：【%s】", StrUtil.isEmpty(rejectReason) ? "物业所填未通过原因" : rejectReason)
                        .toString();

                noticeUtil.send(false, "审核结果通知", message, projectVisitorVo.getVisitPersonId());

            } catch (Exception e) {
                log.warn("消费发送异常", e);
            }

        }
        return r;
    }

    @Override
    public void sendCert(ProjectVisitorHis hisData) {

        LocalDateTime effTime = hisData.getPassBeginTime();
        LocalDateTime expTime = hisData.getPassEndTime();

        List<ProjectPassDeviceVo> deviceVoList = projectPersonDeviceService.listDeviceByPersonId(hisData.getVisitId());
        List<String> deviceIdList = deviceVoList.stream().map(ProjectPassDeviceVo::getDeviceId).collect(Collectors.toList());
        String[] deviceArr = new String[deviceIdList.size()];
        String[] deviceIdArr = deviceIdList.toArray(deviceArr);
        ProjectPersonDeviceDTO projectPersonDeviceDTO = new ProjectPersonDeviceDTO();
        projectPersonDeviceDTO.setDeviceIdArray(deviceIdArr);
        projectPersonDeviceDTO.setPersonId(hisData.getVisitId());
        projectPersonDeviceDTO.setOperator(1);
        projectPersonDeviceDTO.setPersonType(PersonTypeEnum.VISITOR.code);
        projectPersonDeviceDTO.setEffTime(effTime);
        projectPersonDeviceDTO.setExpTime(expTime);
        projectPersonDeviceService.savePersonDevice(projectPersonDeviceDTO);
    }

    @Override
    public void sendCert(ProjectVisitorHis hisData, Integer operator) {

        LocalDateTime effTime = hisData.getPassBeginTime();
        LocalDateTime expTime = hisData.getPassEndTime();

        List<ProjectPassDeviceVo> deviceVoList = projectPersonDeviceService.listDeviceByPersonId(hisData.getVisitId());
        List<String> deviceIdList = deviceVoList.stream().map(ProjectPassDeviceVo::getDeviceId).collect(Collectors.toList());
        String[] deviceArr = new String[deviceIdList.size()];
        String[] deviceIdArr = deviceIdList.toArray(deviceArr);
        ProjectPersonDeviceDTO projectPersonDeviceDTO = new ProjectPersonDeviceDTO();
        projectPersonDeviceDTO.setDeviceIdArray(deviceIdArr);
        projectPersonDeviceDTO.setPersonId(hisData.getVisitId());
        projectPersonDeviceDTO.setPersonType(PersonTypeEnum.VISITOR.code);
        projectPersonDeviceDTO.setEffTime(effTime);
        projectPersonDeviceDTO.setOperator(operator);
        projectPersonDeviceDTO.setExpTime(expTime);
        projectPersonDeviceService.savePersonDevice(projectPersonDeviceDTO);
    }

    @Override
    public void sendCert(String time) {
        List<String> idList = projectVisitorHisService.getAllUnSendCertList(time);
        log.info("进行介质下发，项目ID：{},数量：{}", ProjectContextHolder.getProjectId(), idList.size());
        if (CollUtil.isNotEmpty(idList)) {
            List<ProjectVisitorHis> visitorHisList = projectVisitorHisService.list(new QueryWrapper<ProjectVisitorHis>()
                    .lambda().in(ProjectVisitorHis::getVisitId, idList));
            if (CollUtil.isNotEmpty(visitorHisList)) {
                visitorHisList.forEach(this::sendCert);
            }
        }
    }

    @Override
    public void sendCertBatch() {
        List<ProjectVisitorHis> sendCertHisList = projectVisitorMapper.getSendCertHisList();
        if (CollUtil.isNotEmpty(sendCertHisList)) {
            sendCertHisList.forEach(this::sendCert);
        }
    }


    @Override
    public Page<ProjectVisitorRecordVo> getPage(Page page, ProjectVisitorSearchConditionVo projectVisitorSearchConditionVo) {
//        this.signOffAll();
        return projectVisitorMapper.selectPage(page, projectVisitorSearchConditionVo);
    }

    @Override
    public Page<ProjectVisitorRecordVo> getPageByPerson(Page page, String personId, String status) {
        return projectVisitorMapper.getPageByPerson(page, status, personId, false);
    }

    @Override
    public Page<ProjectVisitorRecordVo> getPageByVisitor(Page page, Integer userId, String status, String date) {
        LocalDate localDate = null;
        if (StringUtils.isNotBlank(date)) {
            localDate = LocalDate.parse(date);
        }

        return projectVisitorMapper.getPageByVisitor(page, status, userId, localDate);
    }

    @Override
    public Page<ProjectVisitorRecordVo> getPageByCreate(Page page, String personId, String status) {

        return projectVisitorMapper.getPageByPerson(page, status, personId, true);
    }

    @Override
    public ProjectVisitor getVisitorByOwner() {
        return getOne(Wrappers.lambdaQuery(ProjectVisitor.class).eq(ProjectVisitor::getUserId, SecurityUtils.getUser().getId()));
    }

    @Override
    public void updatePhoneByUserId(String phone, Integer userId) {
        baseMapper.updatePhoneByUserId(phone, userId);
    }

    @Override
    public void updateUserIdByPhone(String phone, Integer userId) {

        baseMapper.updateUserIdByPhone(phone, userId);
    }

    @Override
    public List<ProjectVisitorRecordVo> getAllUnLeaveListToday() {
        return projectVisitorMapper.getAllUnLeaveListToday();
    }

    @Override
    public ProjectVisitorVo getDataById(String visitId) {
        ProjectVisitorVo reAuditDataById = projectVisitorMapper.getDataById(visitId);
        List<ProjectPassDeviceVo> projectPassDeviceVoList = projectPersonDeviceService.listDeviceByPersonId(visitId);
        if (CollUtil.isNotEmpty(projectPassDeviceVoList)) {
            String[] deviceIds = new String[projectPassDeviceVoList.size()];
            for (int i = 0; i < projectPassDeviceVoList.size(); i++) {
                deviceIds[i] = projectPassDeviceVoList.get(i).getDeviceId();
            }
            reAuditDataById.setDeviceIdArray(deviceIds);
        }
        String[] timeRange = {reAuditDataById.getPassBeginTime(), reAuditDataById.getPassEndTime()};
        reAuditDataById.setTimeRange(timeRange);
        ProjectFaceResourcesAppVo projectFaceResourcesAppVo = projectFaceResourcesService.getByPersonIdForApp(visitId, PersonTypeEnum.VISITOR.code);
        reAuditDataById.setPicUrl(projectFaceResourcesAppVo.getPicUrl());
        return reAuditDataById;
    }

    @Override
    public Page<VisitorInfoVo> page(Page page, VisitorInfoVo vo) {
        ProjectVisitor po = new ProjectVisitor();
        BeanUtils.copyProperties(vo, po);

        return baseMapper.page(page, po);
    }

}
