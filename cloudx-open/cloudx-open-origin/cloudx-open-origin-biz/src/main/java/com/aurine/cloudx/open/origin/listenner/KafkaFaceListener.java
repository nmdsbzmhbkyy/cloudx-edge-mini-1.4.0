package com.aurine.cloudx.open.origin.listenner;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.open.origin.constant.KafkaConstant;
import com.aurine.cloudx.open.origin.constant.enums.DataOriginEnum;
import com.aurine.cloudx.open.origin.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.open.origin.entity.ProjectFaceResources;
import com.aurine.cloudx.open.origin.service.ProjectFaceResourcesService;
import com.aurine.cloudx.open.origin.service.ProjectHousePersonRelService;
import com.aurine.cloudx.open.origin.vo.ProjectFaceResourcesAppVo;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class KafkaFaceListener {


    @Resource
   private ProjectFaceResourcesService projectFaceResourcesService;
    @Resource
   private ProjectHousePersonRelService projectHousePersonRelService;


    @KafkaListener(topics = KafkaConstant.HUMANFACE_ISSUED, errorHandler = "")
     void receiveNoticeConsumer(String json) {
        log.info("进入消息队列开始下发人脸异步请求");
        JSONObject jsonObject = JSONObject.parseObject(json);
        ProjectContextHolder.setProjectId(jsonObject.getInteger("projectId"));
        TenantContextHolder.setTenantId(1);
        for (int i = 1; i < 20; i++) {

            if (projectHousePersonRelService.findSaveFace(jsonObject.getString("relaId"))==null) {
                try {
                    log.info("处理人脸下发异步请求结果");
                    TimeUnit.SECONDS.sleep(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            log.info("开始下发人脸");
            ProjectFaceResources projectFaceResources = new ProjectFaceResources();
            projectFaceResources.setPersonId(jsonObject.getString("personId"));
            projectFaceResources.setPersonType(PersonTypeEnum.PROPRIETOR.code);
            projectFaceResources.setOrigin(DataOriginEnum.WECHAT.code);
            projectFaceResources.setPicUrl(jsonObject.getString("picUrl"));
            projectFaceResources.setStatus("1");
            ProjectFaceResourcesAppVo projectFaceResourcesAppVo = new ProjectFaceResourcesAppVo();
            BeanUtils.copyProperties(projectFaceResources,projectFaceResourcesAppVo );
            projectFaceResourcesService.addFaceFromApp(projectFaceResourcesAppVo);
            break;
        }

    }


}
