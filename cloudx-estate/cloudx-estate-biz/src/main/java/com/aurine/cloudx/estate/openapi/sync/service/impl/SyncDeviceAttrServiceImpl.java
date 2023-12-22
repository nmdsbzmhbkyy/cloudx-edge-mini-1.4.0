package com.aurine.cloudx.estate.openapi.sync.service.impl;

import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.config.SyncConfig;
import com.aurine.cloudx.estate.constant.enums.TableNameEnum;
import com.aurine.cloudx.estate.entity.EdgeCascadeProcessMaster;
import com.aurine.cloudx.estate.entity.OpenApiEntity;
import com.aurine.cloudx.estate.entity.ProjectDeviceAttr;
import com.aurine.cloudx.estate.openapi.enums.OpenApiCascadeTypeEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenApiOperateTypeEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenApiServiceNameEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenPushSubscribeCallbackTypeEnum;
import com.aurine.cloudx.estate.openapi.service.OpenApiMessageService;
import com.aurine.cloudx.estate.openapi.sync.service.SyncService;
import com.aurine.cloudx.estate.openapi.sync.util.SyncUtil;
import com.aurine.cloudx.estate.service.EdgeCascadeProcessMasterService;
import com.aurine.cloudx.estate.service.ProjectDeviceAttrService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 设备拓展属性
 *
 * @author：zouyu
 * @data: 2023-05-04 13:49:32
 */
@Service
public class SyncDeviceAttrServiceImpl implements SyncService {

    @Resource
    private ProjectDeviceAttrService projectDeviceAttrService;

    @Resource
    private SyncConfig syncConfig;

    @Resource
    private OpenApiMessageService openApiMessageService;

    @Resource
    private EdgeCascadeProcessMasterService edgeCascadeProcessMasterService;

    @Override
    public void perform(Integer projectId, String projectUUID) {
        ProjectContextHolder.setProjectId(projectId);
        LambdaQueryWrapper<ProjectDeviceAttr> wrapper = Wrappers.lambdaQuery(ProjectDeviceAttr.class).eq(ProjectDeviceAttr::getProjectId, projectId);

        int count = projectDeviceAttrService.count(wrapper);
        int current = (int) Math.ceil(((double) count / syncConfig.getNumber()));
        for (int i = 1; i <= current; i++) {
            Page<ProjectDeviceAttr> page = projectDeviceAttrService.page(new Page<>(i, syncConfig.getNumber()), wrapper);
            page.getRecords().forEach(e -> {
                OpenApiEntity openApiEntity = SyncUtil.getOpenApiEntityByOperate(projectUUID, OpenPushSubscribeCallbackTypeEnum.OPERATE.name, OpenApiOperateTypeEnum.ADD.name, OpenApiServiceNameEnum.DEVICE_ATTR.name, e);
                openApiMessageService.sendOpenApiMessage(openApiEntity);
            });
        }
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
        return TableNameEnum.DEVICE_ATTR.code;
    }
}
