package com.aurine.cloudx.estate.openapi;

import com.aurine.cloudx.estate.openapi.enums.OpenApiCascadeTypeEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenApiCommandTypeEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenApiOperateTypeEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenApiServiceNameEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenPushSubscribeCallbackTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ToOpenApi注解
 * 标识需要发给开放平台
 *
 * @author : Qiu
 * @date : 2022 02 07 17:23
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ToOpenApi {

    /**
     * 服务类型
     * @return
     */
    OpenPushSubscribeCallbackTypeEnum serviceType();

    /**
     * 服务名称
     * @return
     */
    OpenApiServiceNameEnum serviceName() default OpenApiServiceNameEnum.NULL;

    /**
     * 操作
     * @return
     */
    OpenApiOperateTypeEnum operateType() default OpenApiOperateTypeEnum.NULL;

    /**
     * 命令
     * @return
     */
    OpenApiCommandTypeEnum commandType() default OpenApiCommandTypeEnum.NULL;

    /**
     * 级联
     * @return
     */
    OpenApiCascadeTypeEnum cascadeType() default OpenApiCascadeTypeEnum.NULL;
}
