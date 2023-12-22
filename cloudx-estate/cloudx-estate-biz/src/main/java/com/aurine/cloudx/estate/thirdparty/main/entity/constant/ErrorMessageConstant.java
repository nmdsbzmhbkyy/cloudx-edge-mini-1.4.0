package com.aurine.cloudx.estate.thirdparty.main.entity.constant;

/**
 * <p>异常信息常量</p>
 *
 * @ClassName: ErrorConstant
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-17 9:21
 * @Copyright:
 */
public class ErrorMessageConstant {
    /**
     * 普通失败
     */
    public static final String ERROR = "操作失败，请联系管理员";
    /**
     * 连接超时
     */
    public static final String ERROR_IO_TIME_OUT = "连接超时，请稍后再试";
    /**
     * 地址错误
     */
    public static final String ERROR_URL_WRONG = "连接地址错误，请联系管理员进行配置";
    /**
     * 其他错误
     */
    public static final String ERROR_OTHER = "第三方连接发生异常，错误代码：";

    /**
     * 设备异常：当前设备未在系统中绑定或登记
     */
    public static final String ERROR_DEVICE_NONE = "未找到设备或通行凭证,请确保设备已联网";
    
    /**
     * WR20网关已离线
     */
    public static final String ERROR_WR20_GATEWAY_OFFLINE = "网关离线，请联系管理员";

}
