package com.aurine.cloudx.open.origin.constant.enums;

import com.aurine.cloudx.open.origin.constant.EdgeCascadeServiceIdConstant;
import lombok.AllArgsConstructor;

/**
 * <p>级联申请状态枚举</p>
 * @author : 王良俊
 * @date : 2021-12-02 17:43:53
 */
@AllArgsConstructor
public enum EdgeCascadeEventCodeEnum {
    /**
     * 级联申请提交成功
     */
    REQUEST_SUCCESS(EdgeCascadeServiceIdConstant.CASCADE_REQUEST_SERVICE, "1001", "级联申请提交成功"),
    /**
     * 已入云
     */
    REQUEST_SUCCESS_HAS_CASCADE(EdgeCascadeServiceIdConstant.CASCADE_REQUEST_SERVICE, "1002", "已级联"),
    /**
     * 解绑成功（级联解绑无需确认）
     */
    UNBIND_SUCCESS(EdgeCascadeServiceIdConstant.CASCADE_REQUEST_SERVICE, "1003", "解绑成功"),
    /**
     * 取消级联申请成功
     */
    CANCEL_CASCADE_REQUEST_SUCCESS(EdgeCascadeServiceIdConstant.CASCADE_REQUEST_SERVICE, "1004", "取消级联申请成功"),
    /**
     * 级联申请失败，缺少必要参数
     */
    REQUEST_FAILED_MISSING_PARAMETERS(EdgeCascadeServiceIdConstant.CASCADE_REQUEST_SERVICE, "1011", "级联申请失败，缺少必要参数"),
    /**
     * 级联申请失败，MQTT账号创建失败
     */
    REQUEST_FAILED_MQTT(EdgeCascadeServiceIdConstant.CASCADE_REQUEST_SERVICE, "1012", "级联申请失败，MQTT账号创建失败"),
    /**
     * 级联申请失败，级联码错误
     */
    REQUEST_FAILED_CODE_ERROR(EdgeCascadeServiceIdConstant.CASCADE_REQUEST_SERVICE, "1013", "级联申请失败，级联码错误"),
    /**
     * 当前边缘网关是从边缘网关，不允许级联其他从边缘网关
     */
    REQUEST_FAILED_IS_SLAVE(EdgeCascadeServiceIdConstant.CASCADE_REQUEST_SERVICE, "1014", "级联申请失败，申请的边缘网关不是（主）边缘网关，不接受级联申请"),
    /**
     * 取消级联申请成功
     */
    CANCEL_CASCADE_REQUEST_FAILED(EdgeCascadeServiceIdConstant.CASCADE_REQUEST_SERVICE, "1015", "取消级联申请失败"),
    ;
    public String serviceId;
    public String code;
    public String desc;

    public static EdgeCascadeEventCodeEnum getEnumByCode(String code) {
        EdgeCascadeEventCodeEnum[] values = values();
        for (EdgeCascadeEventCodeEnum item : values) {
            if (item.code.equals(code)) {
                return item;
            }
        }
        return null;
    }
}
