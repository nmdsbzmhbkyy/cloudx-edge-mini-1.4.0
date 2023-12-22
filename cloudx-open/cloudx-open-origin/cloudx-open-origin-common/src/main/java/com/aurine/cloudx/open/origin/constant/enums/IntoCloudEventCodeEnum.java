package com.aurine.cloudx.open.origin.constant.enums;

import com.aurine.cloudx.open.origin.constant.IntoCloudServiceIdConstant;
import lombok.AllArgsConstructor;

/**
 * <p>入云申请状态枚举</p>
 * @author : 王良俊
 * @date : 2021-12-02 17:43:53
 */
@AllArgsConstructor
public enum IntoCloudEventCodeEnum {
    /**
     * 入云申请提交成功
     */
    REQUEST_SUCCESS(IntoCloudServiceIdConstant.INTO_CLOUD_REQUEST_SERVICE, "1001", "入云申请提交成功"),
    /**
     * 已入云
     */
    REQUEST_SUCCESS_HAS_INTO_CLOUD(IntoCloudServiceIdConstant.INTO_CLOUD_REQUEST_SERVICE, "1002", "已入云"),
    /**
     * 解绑成功
     */
    REQUEST_UNBIND_SUCCESS(IntoCloudServiceIdConstant.INTO_CLOUD_REQUEST_SERVICE, "1003", "申请解绑成功"),
    /**
     * 边缘网关已解绑
     */
    UNBIND_SUCCESS(IntoCloudServiceIdConstant.INTO_CLOUD_REQUEST_SERVICE, "1003", "边缘网关已解绑"),
    /**
     * 入云申请失败，缺少必要参数
     */
    REQUEST_FAILED_MISSING_PARAMETERS(IntoCloudServiceIdConstant.INTO_CLOUD_REQUEST_SERVICE, "1011", "入云申请失败，缺少必要参数"),
    /**
     * 入云申请失败，云平台已接入其他边缘网关设备
     */
    REQUEST_FAILED_EXIST_CLOUD(IntoCloudServiceIdConstant.INTO_CLOUD_REQUEST_SERVICE, "1012", "入云申请失败，云平台已接入其他边缘网关设备"),
    /**
     * 入云申请失败，MQTT账号创建失败
     */
    REQUEST_FAILED_MQTT(IntoCloudServiceIdConstant.INTO_CLOUD_REQUEST_SERVICE, "1013", "入云申请失败，MQTT账号创建失败"),
    /**
     * 入云申请失败，连接码错误
     */
    REQUEST_FAILED_CODE_ERROR(IntoCloudServiceIdConstant.INTO_CLOUD_REQUEST_SERVICE, "1014", "入云申请失败，连接码错误"),
    /**
     * 入云申请失败，已和其他云端项目绑定
     */
    REQUEST_FAILED_BOUND_OTHER(IntoCloudServiceIdConstant.INTO_CLOUD_REQUEST_SERVICE, "1015", "入云申请失败，已和其他云端项目绑定"),

    ;
    public String serviceId;
    public String code;
    public String desc;

    public static IntoCloudEventCodeEnum getEnumByCode(String code) {
        IntoCloudEventCodeEnum[] values = values();
        for (IntoCloudEventCodeEnum item : values) {
            if (item.code.equals(code)) {
                return item;
            }
        }
        return null;
    }
}
