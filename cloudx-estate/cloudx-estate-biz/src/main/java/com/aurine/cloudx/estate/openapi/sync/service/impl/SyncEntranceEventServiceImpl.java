package com.aurine.cloudx.estate.openapi.sync.service.impl;

import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.config.SyncConfig;
import com.aurine.cloudx.estate.constant.enums.TableNameEnum;
import com.aurine.cloudx.estate.entity.EdgeCascadeProcessMaster;
import com.aurine.cloudx.estate.entity.OpenApiEntity;
import com.aurine.cloudx.estate.openapi.enums.OpenApiCascadeTypeEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenApiServiceNameEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenPushSubscribeCallbackTypeEnum;
import com.aurine.cloudx.estate.openapi.service.OpenApiMessageService;
import com.aurine.cloudx.estate.openapi.sync.service.SyncService;
import com.aurine.cloudx.estate.openapi.sync.util.SyncUtil;
import com.aurine.cloudx.estate.service.EdgeCascadeProcessMasterService;
import com.aurine.cloudx.estate.service.ProjectEntranceEventService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 人行通行事件表
 *
 * @author：zouyu
 * @data: 2022/3/22 16:52
 */
@Service
public class SyncEntranceEventServiceImpl implements SyncService {

    @Resource
    private ProjectEntranceEventService projectEntranceEventService;

    @Resource
    private SyncConfig syncConfig;

    @Resource
    private OpenApiMessageService openApiMessageService;

    @Resource
    private EdgeCascadeProcessMasterService edgeCascadeProcessMasterService;

    @Override
    public void perform(Integer projectId, String projectUUID) {
        ProjectContextHolder.setProjectId(projectId);
        //TODO: 初始化全量同步取消发送事件消息
//        LambdaQueryWrapper<ProjectEntranceEvent> wrapper = Wrappers.lambdaQuery(ProjectEntranceEvent.class).eq(ProjectEntranceEvent::getProjectId, projectId);
//        int count = projectEntranceEventService.count(wrapper);
//        int current = (int) Math.ceil(((double) count / syncConfig.getNumber()));
//        for (int i = 1; i <= current; i++) {
//            Page<ProjectEntranceEvent> page = projectEntranceEventService.page(new Page<>(i, syncConfig.getNumber()), wrapper);
//            page.getRecords().forEach(e -> {
//                OpenApiEntity openApiEntity = SyncUtil.getOpenApiEntityByOperate(projectUUID, OpenPushSubscribeCallbackTypeEnum.OPERATE.name, OpenApiOperateTypeEnum.ADD.name, OpenApiServiceNameEnum.PERSON_ENTRANCE.name, e);
//                openApiMessageService.sendOpenApiMessage(openApiEntity);
//            });
//        }
        //更新状态
        EdgeCascadeProcessMaster edgeCascadeProcessMaster = edgeCascadeProcessMasterService.getOne(Wrappers.lambdaQuery(EdgeCascadeProcessMaster.class)
                .eq(EdgeCascadeProcessMaster::getServiceName, getVersion())
                .orderByDesc(EdgeCascadeProcessMaster::getCreateTime)
                .last("limit 1"));
        edgeCascadeProcessMaster.setStatus("1");
        edgeCascadeProcessMasterService.updateById(edgeCascadeProcessMaster);

        //传输对象
        OpenApiEntity openApiEntity = SyncUtil.getOpenApiEntityByOperate(projectUUID, OpenPushSubscribeCallbackTypeEnum.OPERATE.name, OpenApiCascadeTypeEnum.UPDATE.name, OpenApiServiceNameEnum.CASCADE_PROCESS_MASTER.name, edgeCascadeProcessMaster);
        openApiMessageService.sendOpenApiMessage(openApiEntity);
    }

    @Override
    public String getVersion() {
        return TableNameEnum.PERSON_ENTRANCE.code;
    }
}
