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
 * 人脸信息Vo
 *
 * @author : Qiu
 * @date : 2021 12 20 16:35
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "人脸信息Vo")
public class FaceInfoVo extends OpenBaseVo {

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
     * 人脸id
     */
    @JsonProperty("faceId")
    @JSONField(name = "faceId")
    @ApiModelProperty(value = "人脸id")
    @Null(message = "人脸id（faceId）新增时需要为空", groups = {InsertGroup.class})
    @NotBlank(message = "人脸id（faceId）不能为空", groups = {UpdateGroup.class})
    @Size(max = 32, message = "人脸id（faceId）长度不能超过32")
    private String faceId;

    /**
     * 人脸编号，第三方传入
     */
    @JsonProperty("faceCode")
    @JSONField(name = "faceCode")
    @ApiModelProperty(value = "人脸编号，第三方传入")
    @Size(max = 32, message = "人脸编号（faceCode）长度不能超过32")
    private String faceCode;

    /**
     * 人脸名称
     */
    @JsonProperty("faceName")
    @JSONField(name = "faceName")
    @ApiModelProperty(value = "人脸名称")
    @Size(max = 50, message = "人脸名称（faceName）长度不能超过50")
    private String faceName;

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
     * 图片地址
     */
    @JsonProperty("picUrl")
    @JSONField(name = "picUrl")
    @ApiModelProperty(value = "图片地址")
    @NotBlank(message = "图片地址（picUrl）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "图片地址（picUrl）长度不能小于1")
    @Size(max = 255, message = "图片地址（picUrl）长度不能超过255")
    private String picUrl;

    /**
     * 图片来源 1 web端 2 小程序 3 app
     */
    @JsonProperty("origin")
    @JSONField(name = "origin")
    @ApiModelProperty(value = "图片来源 1 web端 2 小程序 3 app")
    @NotBlank(message = "图片来源（origin）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "图片来源（origin）长度不能小于1")
    @Size(max = 1, message = "图片来源（origin）长度不能超过1")
    private String origin;

    /**
     * 状态 1 正常 2 冻结
     */
    @JsonProperty("status")
    @JSONField(name = "status")
    @ApiModelProperty(value = "状态 1 正常 2 冻结")
    @NotBlank(message = "状态（status）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "状态（status）长度不能小于1")
    @Size(max = 5, message = "状态（status）长度不能超过5")
    private String status;

    /**
     * 图片Base64（自定义，非数据库字段）
     */
    @JsonProperty("picBase64")
    @JSONField(name = "picBase64")
    @ApiModelProperty(value = "图片Base64（自定义，非数据库字段）")
    private String picBase64;
}
