package com.aurine.cloudx.estate.thirdparty.main.entity.dto;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description: 门禁通行事件 DTO
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-06
 * @Copyright:
 */
@Data
public class EventDeviceGatePassDTO extends BaseDTO {

    /**
     * 事件描述
     */
    private String desc;

    /**
     * 设备的第三方编码
     */
    private String thirdPartyCode;

    /**
     * 设备的SN
     */
    private String deviceSn;

    /**
     * 住户、房屋关系 第三方编码
     */
    private String personCode;
    /**
     * 第三方提供的住户姓名
     */
    private String personName;
    /**
     * 人员类型
     */
    private String personType;

    /**
     * 通行证件类型
     */
    private String certMediaType;
    /**
     * 通行证第三方编码
     */
    private String certMediaCode;
    /**
     * 通行凭证UID
     */
    private String certMediaId;
    /**
     * 通行凭证值
     */
    private String certMediaValue;
    /**
     * 第三方介质id
     */
    private String thirdCertMediaId;
    /**
     * 二维码字符串
     */
    @ApiModelProperty(value = "二维码字符串")
    private String qrcode;

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
     * 事件原数据
     */
    private JSONObject jsonObject;

    /**
     * 所属项目
     */
    private Integer projectId;

    private String deviceType;

    /**
     * 开门方式
     */
    private String openMode;

    /**
     * 体温
     * OpenMode值为4．人脸识别开门时才可能有该值
     */
    private String temperature;

    /**
     * 对应人员表和访客记录表的seq
     */
    private Integer userId;

    /**
     * 扩展字段，用于存储json字符串
     */
    private String extStr;

    /**
     * 地址描述
     */
    private String addrDesc;
}
