package com.aurine.cloudx.open.biz.util;

import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.core.constant.enums.OpenFieldEnum;
import com.aurine.cloudx.open.common.core.util.ReflectUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 开放接口请求头信息注入工具类
 *
 * @author : Qiu
 * @date : 2022 01 20 21:57
 */

public class OpenApiHeaderInjectUtils {

    public static<T> T inject(OpenApiHeader header, T object) {
        @NotBlank(message = "应用ID（appId）不能为空") @Size(max = 32, message = "应用ID（appId）长度不能超过32") String appId = header.getAppId();
        String projectUUID = header.getProjectUUID();
        Integer tenantId = header.getTenantId();
        Integer projectId = header.getProjectId();

        ReflectUtils.findAndSetFieldByName(object, OpenFieldEnum.APP_ID.name, appId);
        ReflectUtils.findAndSetFieldByName(object, OpenFieldEnum.PROJECT_UUID.name, projectUUID);
        ReflectUtils.findAndSetFieldByName(object, OpenFieldEnum.TENANT_ID.name, tenantId);
        ReflectUtils.findAndSetFieldByName(object, OpenFieldEnum.PROJECT_ID.name, projectId);

        return object;
    }
}
