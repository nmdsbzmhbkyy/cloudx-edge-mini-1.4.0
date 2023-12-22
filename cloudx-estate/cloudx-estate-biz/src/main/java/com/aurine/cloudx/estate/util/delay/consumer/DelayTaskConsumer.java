package com.aurine.cloudx.estate.util.delay.consumer;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.estate.constant.VisitorIsLeaveConstant;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.service.adapter.AbstractProjectPersonDeviceService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.constant.AurineConstant;
import com.aurine.cloudx.estate.util.ObjectMapperUtil;
import com.aurine.cloudx.estate.util.delay.constants.DelayTaskTopicConstant;
import com.aurine.cloudx.estate.util.delay.aop.annotations.AutoConfigProjectId;
import com.aurine.cloudx.estate.util.delay.aop.annotations.AutoRemoveDelayTask;
import com.aurine.cloudx.estate.util.delay.entity.MediaAdDelayTask;
import com.aurine.cloudx.estate.util.delay.entity.PersonDelayTask;
import com.aurine.cloudx.estate.util.delay.entity.VisitorDelayTask;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_UNCOMMITTED)
public class DelayTaskConsumer {

    @Resource
    private ProjectVisitorService projectVisitorService;

    @Resource
    private ProjectVisitorHisService projectVisitorHisService;

    @Resource
    private ProjectPersonPlanRelService projectPersonPlanRelService;

    @Resource
    private AbstractProjectPersonDeviceService abstractWebProjectPersonDeviceService;

    @Resource
    private ProjectRightDeviceService projectRightDeviceService;

    @Resource
    private ProjectMediaAdService projectMediaAdService;

    @Resource
    private ProjectMediaAdDevCfgService projectMediaAdDevCfgService;

    @Resource
    private ProjectMediaRepoService projectMediaRepoService;
    @Resource
    private ProjectPersonLiftRelService projectPersonLiftRelService;

    private static ObjectMapper objectMapper = ObjectMapperUtil.instance();


    @AutoConfigProjectId
    @AutoRemoveDelayTask
    @KafkaListener(groupId = AurineConstant.KAFKA_GROUP, topics = DelayTaskTopicConstant.VISITOR_SEND_CERT)
    public void sendVisitorCert(ConsumerRecord<?, ?> record) throws JsonProcessingException {
        log.info("[延时任务] 获取到访客介质下发任务：{}", record.value().toString());
        VisitorDelayTask visitorDelayTask = objectMapper.readValue(record.value().toString(), VisitorDelayTask.class);
        ProjectVisitorHis visitorHis = projectVisitorHisService.getById(visitorDelayTask.getVisitId());
        int count = projectVisitorHisService.count(new LambdaQueryWrapper<>(ProjectVisitorHis.class)
                .eq(ProjectVisitorHis::getVisitId, visitorDelayTask.getVisitId())
                .eq(ProjectVisitorHis::getIsLeave, VisitorIsLeaveConstant.LEAVE));
        if (count == 0 && visitorHis != null) {
            projectVisitorService.sendCert(visitorHis);
        } else {
            log.info("访客已被签离，无需自动下发：{}", visitorDelayTask.toString());
        }
    }

    @AutoConfigProjectId
    @AutoRemoveDelayTask
    @KafkaListener(groupId = AurineConstant.KAFKA_GROUP, topics = DelayTaskTopicConstant.VISITOR_CHECK_OUT)
    public void singOffVisitor(ConsumerRecord<?, ?> record) throws JsonProcessingException {
        log.info("[延时任务] 获取到访客签离任务：{}", record.value().toString());
        VisitorDelayTask visitorDelayTask = objectMapper.readValue(record.value().toString(), VisitorDelayTask.class);
        // 避免时间向后变更后仍然执行过期操作
        int count = projectVisitorHisService.count(new LambdaQueryWrapper<>(ProjectVisitorHis.class)
                .eq(ProjectVisitorHis::getVisitId, visitorDelayTask.getVisitId())
                .eq(ProjectVisitorHis::getIsLeave, VisitorIsLeaveConstant.LEAVE));
        if (count == 0) {
            ProjectVisitorHis visitorHis = projectVisitorHisService.getById(visitorDelayTask.getVisitId());
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN);
            LocalDateTime passEndDateTime = LocalDateTime.parse(visitorHis.getPassEndTime(), dateTimeFormatter);
            if(passEndDateTime.equals(visitorDelayTask.getOriginDelayTime())){
                projectVisitorService.signOff(visitorDelayTask.getVisitId());
            }else {
                log.info("[延时任务] 访客已被延期，无需自动签离：{}", visitorDelayTask.toString());
            }
        } else {
            log.info("访客已被签离，无需自动签离：{}", visitorDelayTask.toString());
        }
    }

    @AutoConfigProjectId
    @AutoRemoveDelayTask
    @KafkaListener(groupId = AurineConstant.KAFKA_GROUP, topics = DelayTaskTopicConstant.HOUSE_HOLDER_EXP)
    public void expHouseholder(ConsumerRecord<?, ?> record) throws JsonProcessingException {
        log.info("[延时任务] 获取到住户/员工授权过期任务：{}", record.value().toString());
        PersonDelayTask personDelayTask = objectMapper.readValue(record.value().toString(), PersonDelayTask.class);
        // 避免时间向后变更后仍然执行过期操作
        int count = projectPersonPlanRelService.count(new LambdaQueryWrapper<ProjectPersonPlanRel>().eq(ProjectPersonPlanRel::getPersonId, personDelayTask.getPersonId())
                .eq(ProjectPersonPlanRel::getExpTime, personDelayTask.getOriginDelayTime()));
        if (count != 0) {
            //projectPersonLiftRelService.disablePassRight(personDelayTask.getPersonTypeEnum().code, personDelayTask.getPersonId());
            abstractWebProjectPersonDeviceService.disablePassRight(personDelayTask.getPersonTypeEnum().code, personDelayTask.getPersonId());
        }
    }
/*

    @AutoConfigProjectId
    @AutoRemoveDelayTask
    @KafkaListener(groupId = AurineConstant.KAFKA_GROUP, topics = DelayTaskTopicConstant.MEDIA_AD_SEND)
    public void adSend(ConsumerRecord<?, ?> record) throws JsonProcessingException {
        log.info("[延时任务] 获取到设备媒体广告下发任务：{}", record.value().toString());
        MediaAdDelayTask mediaAdDelayTask = objectMapper.readValue(record.value().toString(), MediaAdDelayTask.class);
        if (mediaAdDelayTask.getAdSeq() == null) {
            log.error("[延时任务] 缺少必要参数已跳过本次媒体广告下发任务：{}", record.value().toString());
        }
        Integer faildNum = projectMediaAdService.sendMediaAd(mediaAdDelayTask.getAdSeq());
        log.info("[延时任务] 本次广告下发失败数：{} 任务信息：{}", faildNum, record.value().toString());
    }

    @AutoConfigProjectId
    @AutoRemoveDelayTask
    @KafkaListener(groupId = AurineConstant.KAFKA_GROUP, topics = DelayTaskTopicConstant.MEDIA_AD_REMOVE)
    public void adRemove(ConsumerRecord<?, ?> record) throws JsonProcessingException {
        log.info("[延时任务] 获取到设备媒体广告移除任务：{}", record.value().toString());
        MediaAdDelayTask mediaAdDelayTask = objectMapper.readValue(record.value().toString(), MediaAdDelayTask.class);
//        boolean b = projectMediaAdService.removeMediaAd(mediaAdDelayTask.getAdSeq());
    }
*/

}