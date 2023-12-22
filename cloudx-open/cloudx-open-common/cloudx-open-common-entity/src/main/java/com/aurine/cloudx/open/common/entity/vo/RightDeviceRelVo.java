package com.aurine.cloudx.open.common.entity.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.aurine.cloudx.open.common.entity.base.OpenBaseVo;
import com.aurine.cloudx.open.common.validate.annotation.SizeCustom;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;

/**
 * 权限设备关系Vo
 *
 * @author : Qiu
 * @date : 2022 01 26 10:10
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "权限设备关系Vo")
public class RightDeviceRelVo extends OpenBaseVo {

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
    @ApiModelProperty(value = "项目id", required = true, position = -2)
    @NotNull(message = "项目id（projectId）不能为空", groups = {InsertGroup.class})
    @Max(value = Integer.MAX_VALUE, message = "项目id（projectId）数值过大")
    private Integer projectId;

    /**
     * uid
     */
    @JsonProperty("uid")
    @JSONField(name = "uid")
    @ApiModelProperty(value = "uid")
    @Null(message = "uid（uid）新增时需要为空", groups = {InsertGroup.class})
    @NotBlank(message = "uid（uid）不能为空", groups = {UpdateGroup.class})
    @Size(max = 32, message = "uid（uid）长度不能超过32")
    private String uid;

    /**
     * 设备id
     */
    @JsonProperty("deviceId")
    @JSONField(name = "deviceId")
    @ApiModelProperty(value = "设备id")
    @NotBlank(message = "设备id（deviceId）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "设备id（deviceId）长度不能小于1")
    @Size(max = 32, message = "设备id（deviceId）长度不能超过32")
    private String deviceId;

    /**
     * 认证介质 1 指纹 2 人脸 3 卡 4 密码
     */
    @JsonProperty("certMedia")
    @JSONField(name = "certMedia")
    @ApiModelProperty(value = "认证介质 1 指纹 2 人脸 3 卡 4 密码")
    @NotBlank(message = "认证介质（certMedia）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "认证介质（certMedia）长度不能小于1")
    @Size(max = 5, message = "认证介质（certMedia）长度不能超过5")
    private String certMedia;

    /**
     * 认证介质id
     */
    @JsonProperty("certMediaId")
    @JSONField(name = "certMediaId")
    @ApiModelProperty(value = "认证介质id")
    @NotBlank(message = "认证介质id（certMediaId）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "认证介质id（certMediaId）长度不能小于1")
    @Size(max = 32, message = "认证介质id（certMediaId）长度不能超过32")
    private String certMediaId;

    /**
     * 下载状态 0 未下载 1 已下载 2 下载失败
     */
    @JsonProperty("dlStatus")
    @JSONField(name = "dlStatus")
    @ApiModelProperty(value = "下载状态 0 未下载 1 已下载 2 下载失败")
    @NotBlank(message = "下载状态（dlStatus）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "下载状态（dlStatus）长度不能小于1")
    @Size(max = 5, message = "下载状态（dlStatus）长度不能超过5")
    private String dlStatus;

    /**
     * 接口请求状态（适用于异步接口） 1 请求中 2 请求失败 3 请求成功
     */
    @JsonProperty("reqStatus")
    @JSONField(name = "reqStatus")
    @ApiModelProperty(value = "接口请求状态（适用于异步接口） 1 请求中 2 请求失败 3 请求成功")
    @Size(max = 5, message = "接口请求状态（reqStatus）长度不能超过5")
    private String reqStatus;

    /**
     * 接口返回信息
     */
    @JsonProperty("errMsg")
    @JSONField(name = "errMsg")
    @ApiModelProperty(value = "接口返回信息")
    @Size(max = 255, message = "接口返回信息（errMsg）长度不能超过255")
    private String errMsg;

    /**
     * 凭证具体信息，如人脸地址url、卡号等
     */
    @JsonProperty("certMediaInfo")
    @JSONField(name = "certMediaInfo")
    @ApiModelProperty(value = "凭证具体信息，如人脸地址url、卡号等")
    @Size(max = 512, message = "凭证具体信息（certMediaInfo）长度不能超过512")
    private String certMediaInfo;

    /**
     * 凭证编码，第三方编码
     */
    @JsonProperty("certMediaCode")
    @JSONField(name = "certMediaCode")
    @ApiModelProperty(value = "凭证编码，第三方编码")
    @Size(max = 32, message = "凭证编码（certMediaCode）长度不能超过32")
    private String certMediaCode;

    /**
     * 人员类型  1 住户 2 员工 3 访客
     */
    @JsonProperty("personType")
    @JSONField(name = "personType")
    @ApiModelProperty(value = "人员类型  1 住户 2 员工 3 访客")
    @Size(max = 1, message = "人员类型（personType）长度不能超过1")
    private String personType;

    /**
     * 人员id
     */
    @JsonProperty("personId")
    @JSONField(name = "personId")
    @ApiModelProperty(value = "人员id")
    @Size(max = 32, message = "人员id（personId）长度不能超过32")
    private String personId;

    /**
     * 姓名
     */
    @JsonProperty("personName")
    @JSONField(name = "personName")
    @ApiModelProperty(value = "姓名")
    @Size(max = 64, message = "姓名（personName）长度不能超过64")
    private String personName;

    /**
     * 电话
     */
    @JsonProperty("mobileNo")
    @JSONField(name = "mobileNo")
    @ApiModelProperty(value = "电话")
    @Size(max = 20, message = "电话（mobileNo）长度不能超过20")
    private String mobileNo;
}
