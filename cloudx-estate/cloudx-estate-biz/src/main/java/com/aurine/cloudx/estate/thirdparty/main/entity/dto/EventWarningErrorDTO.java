package com.aurine.cloudx.estate.thirdparty.main.entity.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description: 异常告警 DTO
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-06
 * @Copyright:
 */

@Data
public class EventWarningErrorDTO extends BaseDTO {


    /**
     * 事件描述
     */
    private String desc;

    /**
     * 设备的第三方编码
     */
    private String thirdPartyCode;

    /**
     * sn
     */
    private String deviceSn;

    /**
     * 事件发生的事件
     */
    private LocalDateTime eventTime;

    /**
     * 设备网关的第三方编码，对于WR20既为WR20网关
     */
    private String gatewayCode;

    /**
     * 抓拍图片地址
     */
    private String imgUrl;
    /**
     * 异常类型
     */
    private String errorType;

    /**
     * 4.0报警类型
     */
    private String eventTypeId;

    /**
     * 4.0报警级别
     */
    private String eventLevel;

    /**
     * 第三方报警类型ID
     */
    private String thirdEventTypeId;

    /**
     * 异常设备（子设备，通道等ID）
     */
    private String errorDeviceId;

    /**
     * 事件原数据
     */
    private JSONObject jsonObject;
    /**
     * 中台传来是事件code
     */
    private Integer eventCode;

    /**
     * 防区号
     */
    private Integer areaNo;
}
