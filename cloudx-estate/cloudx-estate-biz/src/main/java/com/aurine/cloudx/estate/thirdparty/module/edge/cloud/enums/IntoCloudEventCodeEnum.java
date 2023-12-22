package com.aurine.cloudx.estate.thirdparty.module.edge.cloud.enums;

import com.aurine.cloudx.estate.thirdparty.module.edge.cloud.constants.IntoCloudServiceIdConstant;
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
    UNBIND_SUCCESS(IntoCloudServiceIdConstant.INTO_CLOUD_REQUEST_SERVICE, "1004", "边缘网关已解绑"),
    /**
     * <p>取消入云申请成功</p>
     */
    CANCEL_INTO_CLOUD_REQUEST_SUCCESS(IntoCloudServiceIdConstant.INTO_CLOUD_REQUEST_SERVICE, "1005", "撤销入云申请成功"),
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
    /**
     * <p>取消入云申请失败</p>
     */
    CANCEL_INTO_CLOUD_REQUEST_FAILED(IntoCloudServiceIdConstant.INTO_CLOUD_REQUEST_SERVICE, "1016", "撤销入云申请失败"),
    /**
     * <p>入云申请失败-云端项目未完成解绑后的数据清空操作</p>
     */
    REQUEST_FAILED_CLOUD_DATA_NOT_CLEARED(IntoCloudServiceIdConstant.INTO_CLOUD_REQUEST_SERVICE, "1017", "入云申请失败，存在云端项目未清空数据"),
    /**
     * <p>入云申请失败，请勿重复申请同个云端项目</p>
     */
    REQUEST_FAILED_REQUEST_EXIST(IntoCloudServiceIdConstant.INTO_CLOUD_REQUEST_SERVICE, "1018", "入云申请失败，请勿重复申请同个云端项目"),

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
