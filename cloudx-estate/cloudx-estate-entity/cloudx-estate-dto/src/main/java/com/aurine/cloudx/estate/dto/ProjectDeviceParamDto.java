package com.aurine.cloudx.estate.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * @description: 设备参数DTO
 * @author: wangwei
 * @date: 2022/5/23 16:38
 **/
@Data
public class ProjectDeviceParamDto {


    /**
     * 设备id
     */
    private String deviceId;
    /**
     * 设备第三方id
     */
    private String thirdPartyCode;
    /**
     * 参数服务名称
     */
    private String serviceName;

    /**
     * JSON格式参数属性与参数值
     */
    private JSONObject paramJson;


}
