package com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.constant;

import lombok.AllArgsConstructor;

/**
 * <p>冠林中台返回状态码</p>
 *
 * @ClassName: AurineErrorEnum
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-13 11:01
 * @Copyright:
 */
@AllArgsConstructor
public enum AurineErrorEnum {
    /**
     * 请求格式错误
     */
    WRONG_FORMATE("10001", "请求格式错误"),
    /**
     * APPID不存在
     */
    WRONG_APPID("10002", "APPID不存在"),
    /**
     * 不支持HTTP请求方法（如GET）
     */
    WRONG_MEDTH("10003", "不支持HTTP请求方法（如GET）"),
    /**
     * 请求头签名错误
     */
    WRONG_HEAD("10004", "请求头签名错误"),
    /**
     * 请求头签名错误
     */
    WRONG_AUTH_SIGN("10005", "请求参数签名错误"),
    /**
     * Token错误
     */
    WRONG_TOKEN("10006", "非法访问"),
    /**
     * IP地址未授权
     */
    WRONG_IP("10010", "IP地址未授权"),
    /**
     * 请求对象不存在，设备不存在
     */
    WRONG_OBJECT("10020", "请求对象不存在"),
    /**
     * 请求对象不存在，设备不存在
     */
    WRONG_PARAM("10030", "参数格式或类型错误"),
    /**
     * 对象操作数量限制
     */
    WRONG_OBJECT_COUNT("10032", "对象操作数量限制"),
    /**
     * 操作对象未授权
     */
    WRONG_OBJECT_AUTH("10033", "操作对象未授权"),
    /**
     * 单次请求只允许操作相同类型的对象
     */
    WRONG_ONE_TIME_OPERATE("10034", "单次请求只允许操作相同类型的对象");

    public String code;
    public String desc;

}
