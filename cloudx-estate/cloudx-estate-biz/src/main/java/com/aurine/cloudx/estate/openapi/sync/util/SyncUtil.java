package com.aurine.cloudx.estate.openapi.sync.util;

import com.aurine.cloudx.estate.entity.OpenApiEntity;
import com.aurine.cloudx.estate.util.EntityIdUtil;

/**
 * @author：zouyu
 * @data: 2022/3/23 11:50
 */
public class SyncUtil {


    /**
     * 级联入云类
     *
     * @param projectUUID
     * @param serviceType
     * @param cascadeType
     * @param serviceName
     * @param data
     * @return
     */
    public static OpenApiEntity getOpenApiEntityByCascade(String projectUUID, String serviceType, String cascadeType, String serviceName, Object data) {
        OpenApiEntity openApiEntity = new OpenApiEntity();
        openApiEntity.setTenantId(1);
        openApiEntity.setEntityId(EntityIdUtil.getId(data));
        openApiEntity.setProjectUUID(projectUUID);
        openApiEntity.setServiceType(serviceType);
        openApiEntity.setCascadeType(cascadeType);
        openApiEntity.setServiceName(serviceName);
        openApiEntity.setData(data);
        return openApiEntity;
    }


    /**
     * 指令类
     *
     * @param projectUUID
     * @param serviceType
     * @param commandType
     * @param serviceName
     * @param data
     * @return
     */
    public static OpenApiEntity getOpenApiEntityByCommand(String projectUUID, String serviceType, String commandType, String serviceName, Object data) {
        OpenApiEntity openApiEntity = new OpenApiEntity();
        openApiEntity.setTenantId(1);
        openApiEntity.setEntityId(EntityIdUtil.getId(data));
        openApiEntity.setProjectUUID(projectUUID);
        openApiEntity.setServiceType(serviceType);
        openApiEntity.setCommandType(commandType);
        openApiEntity.setServiceName(serviceName);
        openApiEntity.setData(data);
        return openApiEntity;
    }


    /**
     * 操作类
     *
     * @param projectUUID
     * @param serviceType
     * @param operateType
     * @param serviceName
     * @param data
     * @return
     */
    public static OpenApiEntity getOpenApiEntityByOperate(String projectUUID, String serviceType, String operateType, String serviceName, Object data) {
        OpenApiEntity openApiEntity = new OpenApiEntity();
        openApiEntity.setTenantId(1);
        openApiEntity.setEntityId(EntityIdUtil.getId(data));
        openApiEntity.setProjectUUID(projectUUID);
        openApiEntity.setServiceType(serviceType);
        openApiEntity.setOperateType(operateType);
        openApiEntity.setServiceName(serviceName);
        openApiEntity.setData(data);
        return openApiEntity;
    }
}
