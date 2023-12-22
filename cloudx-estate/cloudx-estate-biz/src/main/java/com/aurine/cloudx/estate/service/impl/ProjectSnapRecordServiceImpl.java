package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.mapper.ProjectSnapRecordMapper;
import com.aurine.cloudx.estate.service.ProjectDeviceRegionService;
import com.aurine.cloudx.estate.service.ProjectInfoService;
import com.aurine.cloudx.estate.service.ProjectSnapRecordService;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.TopicConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.EventSubTypeEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.EventTypeEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.yushi.entity.FaceInfo;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.yushi.entity.ImageInfo;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.yushi.entity.PersonInfo;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.yushi.entity.YushiCallBackObj;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.yushi.enums.RuleTypeEnum;
import com.aurine.cloudx.estate.thirdparty.transport.mq.kafka.KafkaProducer;
import com.aurine.cloudx.estate.util.ImgConvertUtil;
import com.aurine.cloudx.estate.vo.ProjectSnapRecordVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class ProjectSnapRecordServiceImpl extends ServiceImpl<ProjectSnapRecordMapper, ProjectSnapRecord> implements ProjectSnapRecordService {

    @Resource
    private ProjectDeviceRegionService projectDeviceRegionService;
    @Resource
    private ProjectInfoService projectInfoService;
    @Resource
    private ImgConvertUtil imgUtil;

    @Override
    @SneakyThrows
    public Boolean saveRecord(YushiCallBackObj callBackObj, ProjectDeviceInfo deviceInfo) {
        List<ProjectSnapRecord> recordList = new ArrayList<>();
        ProjectDeviceRegion region = projectDeviceRegionService.getById(deviceInfo.getDeviceRegionId());
        ProjectInfo projectInfo = projectInfoService.getById(deviceInfo.getProjectId());

        ProjectSnapRecord.ProjectSnapRecordBuilder builder = ProjectSnapRecord.builder();
        builder.deviceId(deviceInfo.getDeviceId())
                .snapNo(deviceInfo.getThirdpartyCode())
                .areaNo(projectInfo == null ? "" : projectInfo.getProjectCode())
                .deviceName(deviceInfo.getDeviceName())
                .deviceRegionName(region == null ? "" : region.getRegionName())
//                .eventType(EventTypeEnum.A006002.eventTypeId)
//                .eventSubType(EventSubTypeEnum.FACE_CAPTURE.typeString)
                .source("社区边缘一体机")
                .snapTime(LocalDateTime.ofEpochSecond(callBackObj.getTimeStamp(), 0, ZoneOffset.ofHours(8)))
                .projectId(deviceInfo.getProjectId())
                .reference(deviceInfo.getIpv4());

        if (callBackObj.getStructureInfo().getObjInfo().getFaceNum() > 0) {
            this.faceToRecordList(callBackObj, recordList, builder);
        } else if (callBackObj.getStructureInfo().getObjInfo().getPersonNum() > 0) {
            this.personToRecordList(callBackObj, recordList, builder);
        } else {
            return false;
        }

        for (ProjectSnapRecord record : recordList) {
            this.save(record);
        }
        return true;
//        return this.saveBatch(recordList);
    }

    @Override
    public IPage<ProjectSnapRecordVo> page(Page page, ProjectSnapRecordVo vo) {
        IPage<ProjectSnapRecordVo> projectSnapRecordIPage = baseMapper.pageRecord(page, vo);
        projectSnapRecordIPage.getRecords().forEach(record -> {
            record.setEventTypeName(EventTypeEnum.getByEventType(record.getEventType()).eventTypeName);
            record.setEventSubTypeName(EventSubTypeEnum.getByType(record.getEventSubType()).typeName);
        });
        return projectSnapRecordIPage;
    }

    private void faceToRecordList(YushiCallBackObj callBackObj, List<ProjectSnapRecord> recordList, ProjectSnapRecord.ProjectSnapRecordBuilder builder) throws IOException {
        List<FaceInfo> list = callBackObj.getStructureInfo().getObjInfo().getFaceInfoList();

        builder.eventType(EventTypeEnum.A006002.eventTypeId)
                .eventSubType(EventSubTypeEnum.FACE_CAPTURE.typeString);

        Map<Long, ImageInfo> imageInfoMap = callBackObj.getStructureInfo().getImageInfoList()
                .stream().collect(Collectors.toMap(ImageInfo::getIndex, Function.identity(), (s, s2) -> s2));
        list.forEach(v -> {
            String smallImg = null;
            String bigImg = null;
            try {
                smallImg = imgUtil.base64ToMinio(imageInfoMap.get(v.getSmallPicAttachIndex()).getData());
                bigImg = imgUtil.base64ToMinio(imageInfoMap.get(v.getLargePicAttachIndex()).getData());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            builder.snapSmalImage(smallImg)
                    .snapBigImage(bigImg);
            ProjectSnapRecord record = builder.build();
            KafkaProducer.sendMessage(TopicConstant.PEOPLE_FACE_RECORD, record);
            recordList.add(record);
        });
    }

    private void personToRecordList(YushiCallBackObj callBackObj, List<ProjectSnapRecord> recordList, ProjectSnapRecord.ProjectSnapRecordBuilder builder) throws IOException {
        List<PersonInfo> list = callBackObj.getStructureInfo().getObjInfo().getPersonInfoList();

        Map<Long, ImageInfo> imageInfoMap = callBackObj.getStructureInfo().getImageInfoList()
                .stream().collect(Collectors.toMap(ImageInfo::getIndex, Function.identity(), (s, s2) -> s2));
        list.forEach(v -> {
            String smallImg = null;
            String bigImg = null;
            try {
                smallImg = imgUtil.base64ToMinio(imageInfoMap.get(v.getSmallPicAttachIndex()).getData());
                bigImg = imgUtil.base64ToMinio(imageInfoMap.get(v.getLargePicAttachIndex()).getData());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            EventSubTypeEnum eventSubTypeEnum = RuleTypeEnum.getEventSubTypeByType(v.getRuleInfo().getRuleType());
            builder.eventType(eventSubTypeEnum.eventTypeEnum.eventTypeId)
                    .eventSubType(eventSubTypeEnum.typeString)
                    .snapSmalImage(smallImg)
                    .snapBigImage(bigImg);
            ProjectSnapRecord record = builder.build();
//            KafkaProducer.sendMessage(TopicConstant.PEOPLE_FACE_RECORD, record);
            recordList.add(record);
        });
    }

}
