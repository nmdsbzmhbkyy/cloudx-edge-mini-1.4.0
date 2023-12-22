package com.aurine.cloudx.open.common.entity.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.aurine.cloudx.open.common.entity.base.OpenBaseVo;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * 报警事件Vo
 *
 * @author : Qiu
 * @date : 2021 12 20 16:35
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "报警事件Vo")
public class AlarmEventVo extends OpenBaseVo {

    /**
     * 序列
     */
    @JsonProperty("seq")
    @JSONField(name = "seq")
    @ApiModelProperty(value = "序列", hidden = true)
    @Null(message = "序列（seq）需要为空")
    private Integer seq;

    /**
     * 项目id
     */
    @JsonProperty("projectId")
    @JSONField(name = "projectId")
    @ApiModelProperty(value = "项目id", required = true, position = -1)
    @NotNull(message = "项目id（projectId）不能为空", groups = {InsertGroup.class})
    @Max(value = Integer.MAX_VALUE, message = "项目id（projectId）数值过大")
    private Integer projectId;

    /**
     * 事件id
     */
    @JsonProperty("eventId")
    @JSONField(name = "eventId")
    @ApiModelProperty(value = "事件id")
    @Null(message = "事件id（eventId）新增时需要为空", groups = {InsertGroup.class})
    @NotBlank(message = "事件id（eventId）不能为空", groups = {UpdateGroup.class})
    @Size(max = 32, message = "事件id（eventId）长度不能超过32")
    private String eventId;

    /**
     * 事件第三方id
     */
    @JsonProperty("eventCode")
    @JSONField(name = "eventCode")
    @ApiModelProperty(value = "事件第三方id")
    @Max(value = Integer.MAX_VALUE, message = "事件第三方id（eventCode）数值过大")
    private Integer eventCode;

    /**
     * 人员类型
     */
    @JsonProperty("personType")
    @JSONField(name = "personType")
    @ApiModelProperty(value = "人员类型")
    @Size(max = 5, message = "人员类型（personType）长度不能超过5")
    private String personType;

    /**
     * 姓名
     */
    @JsonProperty("personName")
    @JSONField(name = "personName")
    @ApiModelProperty(value = "姓名")
    @Size(max = 64, message = "姓名（personName）长度不能超过64")
    private String personName;

    /**
     * 设备id
     */
    @JsonProperty("deviceId")
    @JSONField(name = "deviceId")
    @ApiModelProperty(value = "设备id")
    @Size(max = 32, message = "设备id（deviceId）长度不能超过32")
    private String deviceId;

    /**
     * 设备名称
     */
    @JsonProperty("deviceName")
    @JSONField(name = "deviceName")
    @ApiModelProperty(value = "设备名称")
    @Size(max = 64, message = "设备名称（deviceName）长度不能超过64")
    private String deviceName;

    /**
     * 设备类型名称
     */
    @JsonProperty("deviceTypeName")
    @JSONField(name = "deviceTypeName")
    @ApiModelProperty(value = "设备类型名称")
    @Size(max = 64, message = "设备类型名称（deviceTypeName）长度不能超过64")
    private String deviceTypeName;

    /**
     * 设备区域名称
     */
    @JsonProperty("deviceRegionName")
    @JSONField(name = "deviceRegionName")
    @ApiModelProperty(value = "设备区域名称")
    @Size(max = 64, message = "设备区域名称（deviceRegionName）长度不能超过64")
    private String deviceRegionName;

    /**
     * 事件描述
     */
    @JsonProperty("eventDesc")
    @JSONField(name = "eventDesc")
    @ApiModelProperty(value = "事件描述")
    @Size(max = 128, message = "事件描述（eventDesc）长度不能超过128")
    private String eventDesc;

    /**
     * 事件时间
     */
    @JsonProperty("eventTime")
    @JSONField(name = "eventTime", format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "事件时间")
    private LocalDateTime eventTime;

    /**
     * 抓拍图片
     */
    @JsonProperty("picUrl")
    @JSONField(name = "picUrl")
    @ApiModelProperty(value = "抓拍图片")
    @Size(max = 255, message = "抓拍图片（picUrl）长度不能超过255")
    private String picUrl;

    /**
     * 状态
     */
    @JsonProperty("status")
    @JSONField(name = "status")
    @ApiModelProperty(value = "状态")
    @Size(max = 5, message = "状态（status）长度不能超过5")
    private String status;

    /**
     * 事件类型id
     */
    @JsonProperty("eventTypeId")
    @JSONField(name = "eventTypeId")
    @ApiModelProperty(value = "事件类型id")
    @Size(max = 10, message = "事件类型id（eventTypeId）长度不能超过10")
    private String eventTypeId;

    /**
     * 报警级别
     */
    @JsonProperty("eventLevel")
    @JSONField(name = "eventLevel")
    @ApiModelProperty(value = "报警级别")
    @Size(max = 1, message = "报警级别（eventLevel）长度不能超过1")
    private String eventLevel;

    /**
     * 防区号
     */
    @JsonProperty("areaNo")
    @JSONField(name = "areaNo")
    @ApiModelProperty(value = "防区号")
    @Max(value = Integer.MAX_VALUE, message = "防区号（areaNo）数值过大")
    private Integer areaNo;

    /**
     * 图片Base64（自定义，非数据库字段）
     */
    @JsonProperty("picBase64")
    @JSONField(name = "picBase64")
    @ApiModelProperty(value = "图片Base64（自定义，非数据库字段）")
    private String picBase64;
}
