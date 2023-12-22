package com.aurine.cloudx.estate.openapi.sync.service.impl;

import cn.hutool.core.collection.ListUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.config.SyncConfig;
import com.aurine.cloudx.estate.constant.enums.TableNameEnum;
import com.aurine.cloudx.estate.entity.EdgeCascadeProcessMaster;
import com.aurine.cloudx.estate.entity.OpenApiEntity;
import com.aurine.cloudx.estate.entity.ProjectDeviceParamInfo;
import com.aurine.cloudx.estate.openapi.enums.OpenApiCascadeTypeEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenApiOperateTypeEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenApiServiceNameEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenPushSubscribeCallbackTypeEnum;
import com.aurine.cloudx.estate.openapi.service.OpenApiMessageService;
import com.aurine.cloudx.estate.openapi.sync.service.SyncService;
import com.aurine.cloudx.estate.openapi.sync.util.SqlUtils;
import com.aurine.cloudx.estate.openapi.sync.util.SyncUtil;
import com.aurine.cloudx.estate.service.EdgeCascadeProcessMasterService;
import com.aurine.cloudx.estate.service.ProjectDeviceParamInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 设备参数表
 *
 * @author：zouyu
 * @data: 2022-07-28 10:07:25
 */
@Service
public class SyncDeviceParamInfoServiceImpl implements SyncService {

    @Resource
    private ProjectDeviceParamInfoService projectDeviceParamInfoService;

    @Resource
    private SyncConfig syncConfig;

    @Resource
    private OpenApiMessageService openApiMessageService;

    @Resource
    private EdgeCascadeProcessMasterService edgeCascadeProcessMasterService;

    @Override
    public void perform(Integer projectId, String projectUUID) {
        LambdaQueryWrapper<ProjectDeviceParamInfo> wrapper = Wrappers.lambdaQuery(ProjectDeviceParamInfo.class);

        int count = projectDeviceParamInfoService.count(wrapper);
        int current = (int) Math.ceil(((double) count / syncConfig.getNumber()));
        for (int i = 1; i <= current; i++) {
            Page<ProjectDeviceParamInfo> page = projectDeviceParamInfoService.page(new Page<>(i, syncConfig.getNumber()));
            page.getRecords().forEach(e -> {
                //设备参数表另外处理 用sql方式传输
                String sql = SqlUtils.joinBatchInsert(ListUtil.toList(e), "aurine.project_device_param_info");
                Map map = new HashMap();
                map.put("sql",sql);
                OpenApiEntity openApiEntity = new OpenApiEntity();
                openApiEntity.setServiceType(OpenPushSubscribeCallbackTypeEnum.OTHER.name);
                openApiEntity.setServiceName(OpenApiServiceNameEnum.DEVICE_PARAM_INFO.name);
                openApiEntity.setOperateType(OpenApiOperateTypeEnum.ADD.name);
                openApiEntity.setData(map);
                openApiEntity.setEntityId(null);
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
        return TableNameEnum.DEVICE_PARAM_INFO.code;
    }
}
