package com.aurine.cloudx.open.common.entity.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.aurine.cloudx.open.common.entity.base.OpenBaseVo;
import com.aurine.cloudx.open.common.validate.annotation.SizeCustom;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * 人行事件Vo
 *
 * @author : Qiu
 * @date : 2021 12 20 16:35
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "人行事件Vo")
public class PersonEntranceVo extends OpenBaseVo {

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
     * 人员类型 1 住户 2 员工 3 访客
     */
    @JsonProperty("personType")
    @JSONField(name = "personType")
    @ApiModelProperty(value = "人员类型 1 住户 2 员工 3 访客")
    @NotBlank(message = "人员类型（personType）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "人员类型（personType）长度不能小于1")
    @Size(max = 1, message = "人员类型（personType）长度不能超过1")
    private String personType;

    /**
     * 人员id, 根据人员类型取对应表id
     */
    @JsonProperty("personId")
    @JSONField(name = "personId")
    @ApiModelProperty(value = "人员id, 根据人员类型取对应表id")
    @NotBlank(message = "人员id（personId）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "人员id（personId）长度不能小于1")
    @Size(max = 32, message = "人员id（personId）长度不能超过32")
    private String personId;

    /**
     * 人员姓名
     */
    @JsonProperty("personName")
    @JSONField(name = "personName")
    @ApiModelProperty(value = "人员姓名")
    @Size(max = 64, message = "人员姓名（personName）长度不能超过64")
    private String personName;

    /**
     * 事件时间
     */
    @JsonProperty("eventTime")
    @JSONField(name = "eventTime", format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "事件时间")
    private LocalDateTime eventTime;

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
     * 设备区域名称
     */
    @JsonProperty("deviceRegionName")
    @JSONField(name = "deviceRegionName")
    @ApiModelProperty(value = "设备区域名称")
    @Size(max = 64, message = "设备区域名称（deviceRegionName）长度不能超过64")
    private String deviceRegionName;

    /**
     * 出入方向 1 入  2 出
     */
    @JsonProperty("entranceType")
    @JSONField(name = "entranceType")
    @ApiModelProperty(value = "出入方向 1 入  2 出")
    @Size(max = 1, message = "出入方向（entranceType）长度不能超过1")
    private String entranceType;

    /**
     * 事件类型 1 一般事件 2 异常事件 3 报警事件
     */
    @JsonProperty("eventType")
    @JSONField(name = "eventType")
    @ApiModelProperty(value = "事件类型 1 一般事件 2 异常事件 3 报警事件")
    @NotBlank(message = "事件类型（eventType）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "事件类型（eventType）长度不能小于1")
    @Size(max = 1, message = "事件类型（eventType）长度不能超过1")
    private String eventType;

    /**
     * 认证介质 1 指纹 2 人脸 3 卡
     */
    @JsonProperty("certMedia")
    @JSONField(name = "certMedia")
    @ApiModelProperty(value = "认证介质 1 指纹 2 人脸 3 卡")
    @Size(max = 5, message = "认证介质（certMedia）长度不能超过5")
    private String certMedia;

    /**
     * 图片路径
     */
    @JsonProperty("picUrl")
    @JSONField(name = "picUrl")
    @ApiModelProperty(value = "图片路径")
    @Size(max = 255, message = "图片路径（picUrl）长度不能超过255")
    private String picUrl;

    /**
     * 事件描述
     */
    @JsonProperty("eventDesc")
    @JSONField(name = "eventDesc")
    @ApiModelProperty(value = "事件描述")
    @Size(max = 128, message = "事件描述（eventDesc）长度不能超过128")
    private String eventDesc;

    /**
     * 事件状态 0 未处理 1 处理中 2 已完成
     */
    @JsonProperty("eventStatus")
    @JSONField(name = "eventStatus")
    @ApiModelProperty(value = "事件状态 0 未处理 1 处理中 2 已完成")
    @Size(max = 5, message = "事件状态（eventStatus）长度不能超过5")
    private String eventStatus;

    /**
     * 图片Base64（自定义，非数据库字段）
     */
    @JsonProperty("picBase64")
    @JSONField(name = "picBase64")
    @ApiModelProperty(value = "图片Base64（自定义，非数据库字段）")
    private String picBase64;
}
