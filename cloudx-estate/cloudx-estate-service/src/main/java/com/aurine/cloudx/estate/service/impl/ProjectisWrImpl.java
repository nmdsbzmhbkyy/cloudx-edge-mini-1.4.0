package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.DataOriginEnum;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.entity.ProjectFaceResources;
import com.aurine.cloudx.estate.entity.ProjectPatrolDetail;
import com.aurine.cloudx.estate.feign.RemoteFaceResourcesService;
import com.aurine.cloudx.estate.feign.RemoteHousePersonRelService;
import com.aurine.cloudx.estate.mapper.ProjectPatrolDetailMapper;
import com.aurine.cloudx.estate.service.ProjectPatrolDetailService;
import com.aurine.cloudx.estate.service.ProjectPatrolPersonService;
import com.aurine.cloudx.estate.service.ProjectisWrService;
import com.aurine.cloudx.estate.vo.ProjectPatrolDetaiInfolVo;
import com.aurine.cloudx.estate.vo.ProjectPatrolDetailVo;
import com.aurine.cloudx.estate.vo.ProjectPatrolPersonVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 巡更明细表(ProjectPatrolDetail)表服务实现类
 *
 * @author 黄阳光 <huangyg@aurine.cn>
 * @since 2020-09-11 11:57:27
 */
@Service
@Slf4j
public class ProjectisWrImpl implements ProjectisWrService {

    @Resource
    private RemoteHousePersonRelService remoteHousePersonRelService;
    @Resource
    private RemoteFaceResourcesService remoteFaceResourcesService;

    @LoadBalanced
    private RestTemplate restTemplate =new RestTemplate();

    @Async
    @Override
    public void findSaveFace(String relaId, String personId, String picUrl, Integer projectId) {
        ProjectContextHolder.setProjectId(projectId);
        TenantContextHolder.setTenantId(1);
        log.info("子线程==============:"+Thread.currentThread().getName());
        for (int i = 1; i < 20; i++) {

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("relaId",relaId);
            String url = "http://cloudx-estate-biz/baseHousePersonRel/findSaveFace/{relaId}";
            ResponseEntity<JSONObject> forEntity = restTemplate.getForEntity(url, JSONObject.class, hashMap);
            if (forEntity.getStatusCode().is2xxSuccessful()){
                Integer code = forEntity.getBody().getInteger("code");
                if (code==1) {
                    try {
                        log.info("处理人脸下发异步请求结果");
                        TimeUnit.SECONDS.sleep(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
            }else {
                log.info("接口访问失败==="+forEntity.getBody());
            }
   /*         if (remoteHousePersonRelService.findSaveFace(relaId).getCode()==1) {
                try {
                    log.info("处理人脸下发异步请求结果");
                    TimeUnit.SECONDS.sleep(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }*/
            log.info("开始下发人脸");
            ProjectFaceResources projectFaceResources = new ProjectFaceResources();
            projectFaceResources.setPersonId(personId);
            projectFaceResources.setPersonType(PersonTypeEnum.PROPRIETOR.code);
            projectFaceResources.setOrigin(DataOriginEnum.WECHAT.code);
            projectFaceResources.setPicUrl(picUrl);
            projectFaceResources.setStatus("1");
            remoteFaceResourcesService.saveFaceByApp(projectFaceResources);
            break;
        }

    }
}