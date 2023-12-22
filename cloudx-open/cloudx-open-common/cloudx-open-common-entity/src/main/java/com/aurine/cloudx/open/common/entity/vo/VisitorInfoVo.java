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
 * 访客信息Vo
 *
 * @author : Qiu
 * @date : 2021 12 20 16:35
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "访客信息Vo")
public class VisitorInfoVo extends OpenBaseVo {

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
     * 访客id
     */
    @JsonProperty("visitorId")
    @JSONField(name = "visitorId")
    @ApiModelProperty(value = "访客id")
    @Null(message = "访客id（visitorId）新增时需要为空", groups = {InsertGroup.class})
    @NotBlank(message = "访客id（visitorId）不能为空", groups = {UpdateGroup.class})
    @Size(max = 32, message = "访客id（visitorId）长度不能超过32")
    private String visitorId;

    /**
     * 姓名
     */
    @JsonProperty("personName")
    @JSONField(name = "personName")
    @ApiModelProperty(value = "姓名")
    @NotBlank(message = "姓名（personName）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "姓名（personName）长度不能小于1")
    @Size(max = 64, message = "姓名（personName）长度不能超过64")
    private String personName;

    /**
     * 姓名
     */
    @JsonProperty("gender")
    @JSONField(name = "gender")
    @ApiModelProperty(value = "性别")
    @Size(max = 5, message = "性别（gender）长度不能超过5")
    private String gender;

    /**
     * 证件类型
     */
    @JsonProperty("credentialType")
    @JSONField(name = "credentialType")
    @ApiModelProperty(value = "证件类型")
    @Size(max = 5, message = "证件类型（credentialType）长度不能超过5")
    private String credentialType;

    /**
     * 证件号码
     */
    @JsonProperty("credentialNo")
    @JSONField(name = "credentialNo")
    @ApiModelProperty(value = "证件号码")
    @Size(max = 32, message = "证件号码（credentialNo）长度不能超过32")
    private String credentialNo;

    /**
     * 手机号
     */
    @JsonProperty("mobileNo")
    @JSONField(name = "mobileNo")
    @ApiModelProperty(value = "手机号")
    @Size(max = 20, message = "手机号（mobileNo）长度不能超过20")
    private String mobileNo;

    /**
     * 照片
     */
    @JsonProperty("picUrl")
    @JSONField(name = "picUrl")
    @ApiModelProperty(value = "照片")
    @Size(max = 255, message = "照片（picUrl）长度不能超过255")
    private String picUrl;

    /**
     * 证件照正面
     */
    @JsonProperty("credentialPicFront")
    @JSONField(name = "credentialPicFront")
    @ApiModelProperty(value = "证件照正面")
    @Size(max = 255, message = "证件照正面（credentialPicFront）长度不能超过255")
    private String credentialPicFront;

    /**
     * 证件照背面
     */
    @JsonProperty("credentialPicBack")
    @JSONField(name = "credentialPicBack")
    @ApiModelProperty(value = "证件照背面")
    @Size(max = 255, message = "证件照背面（credentialPicBack）长度不能超过255")
    private String credentialPicBack;

    /**
     * 用户id
     */
    @JsonProperty("userId")
    @JSONField(name = "userId")
    @ApiModelProperty("用户Id")
    private Integer userId;

    /**
     * 图片Base64（自定义，非数据库字段）
     */
    @JsonProperty("picBase64")
    @JSONField(name = "picBase64")
    @ApiModelProperty(value = "图片Base64（自定义，非数据库字段）")
    private String picBase64;

    /**
     * 证件照正面图片Base64（自定义，非数据库字段）
     */
    @JsonProperty("credentialPicFrontBase64")
    @JSONField(name = "credentialPicFrontBase64")
    @ApiModelProperty(value = "证件照正面图片Base64（自定义，非数据库字段）")
    private String credentialPicFrontBase64;

    /**
     * 证件照背面图片Base64（自定义，非数据库字段）
     */
    @JsonProperty("credentialPicBackBase64")
    @JSONField(name = "credentialPicBackBase64")
    @ApiModelProperty(value = "证件照背面图片Base64（自定义，非数据库字段）")
    private String credentialPicBackBase64;
}
