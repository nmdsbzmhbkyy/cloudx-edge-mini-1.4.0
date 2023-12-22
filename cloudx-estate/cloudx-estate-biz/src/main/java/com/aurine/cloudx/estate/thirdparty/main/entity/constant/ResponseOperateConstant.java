package com.aurine.cloudx.estate.thirdparty.main.entity.constant;

import lombok.Data;

/**
 * @description: 指令下发结果 DTO
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-06
 * @Copyright:
 */

@Data
public class ResponseOperateConstant {

    /**
     * 更新设备基本参数
     * IP地址等
     */
    public static final String ACTION_UPDATE_DEVICE_PARAM_BASIC = "UPDATE_DEVICE_PARAM_BASIC";

    /**
     * 更新设备的额外参数
     * 音量等不再设备信息表的数据，存储到项目设备参数信息表的json字段中
     */
    public static final String ACTION_UPDATE_DEVICE_PARAM_OTHER = "UPDATE_DEVICE_PARAM_OTHER";

    /**
     * 非区口或梯口机参数更新（暂定Iot设备）
     * 音量等不再设备信息表的数据，存储到项目设备参数信息表的json字段中
     */
    public static final String ACTION_UPDATE_DEVICE_PARAM_IOT = "ACTION_UPDATE_DEVICE_PARAM_IOT";

    /**
     * 这里的设备状态不包括设备在线状态
     */
    public static final String ACTION_UPDATE_DEVICE_STATUS = "UPDATE_DEVICE_STATUS";

    /**
     * 更新设备框架号
     */
    public static final String ACTION_UPDATE_DEVICE_PARAM_FRAME = "UPDATE_DEVICE_PARAM_FRAME";

    /**
     * 修改凭证状态
     */
    public static final String ACTION_UPDATE_CERT_STATUS = "UPDATE_CERT_STATUS";

    /**
     * 修改多个凭证状态
     */
    public static final String ACTION_UPDATE_CERT_STATUS_LIST = "UPDATE_CERT_STATUS_LIST";

    /**
     * 报警事件状态修改（一般是由未处理改为已处理）
     */
    public static final String ACTION_UPDATE_ALARM_STATUS = "UPDATE_ALARM_STATUS";
}
