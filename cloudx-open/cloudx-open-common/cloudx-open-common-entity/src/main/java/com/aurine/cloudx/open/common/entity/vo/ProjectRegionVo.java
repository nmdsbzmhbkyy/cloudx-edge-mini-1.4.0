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
import java.time.LocalDateTime;

/**
 * 区域信息Vo
 *
 * @author : Qiu
 * @date : 2021 12 20 16:35
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "区域信息Vo")
public class ProjectRegionVo extends OpenBaseVo {

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
     * 区域id
     */
    @JsonProperty("regionId")
    @JSONField(name = "regionId")
    @ApiModelProperty(value = "区域id")
    @Null(message = "区域id（regionId）新增时需要为空", groups = {InsertGroup.class})
    @NotBlank(message = "区域id（regionId）不能为空", groups = {UpdateGroup.class})
    @Size(max = 32, message = "区域id（regionId）长度不能超过32")
    private String regionId;

    /**
     * 区域编码, 第三方
     */
    @JsonProperty("regionCode")
    @JSONField(name = "regionCode")
    @ApiModelProperty(value = "区域编码, 第三方")
    @Size(max = 64, message = "区域编码（regionCode）长度不能超过64")
    private String regionCode;

    /**
     * 区域名称
     */
    @JsonProperty("regionName")
    @JSONField(name = "regionName")
    @ApiModelProperty(value = "区域名称")
    @NotBlank(message = "区域名称（regionName）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "区域名称（regionName）长度不能小于1")
    @Size(max = 64, message = "区域名称（regionName）长度不能超过64")
    private String regionName;

    /**
     * 上级区域
     */
    @JsonProperty("parRegionId")
    @JSONField(name = "parRegionId")
    @ApiModelProperty(value = "上级区域")
    @NotBlank(message = "上级区域（parRegionId）不能为空，默认为1", groups = {InsertGroup.class})
    @SizeCustom(message = "上级区域（parRegionId）长度不能小于1")
    @Size(max = 32, message = "上级区域（parRegionId）长度不能超过32")
    private String parRegionId;

    /**
     * 平面图名称
     */
    @JsonProperty("picName")
    @JSONField(name = "picName")
    @ApiModelProperty(value = "平面图名称")
    @Size(max = 50, message = "平面图名称（picName）长度不能超过50")
    private String picName;

    /**
     * 平面图url
     */
    @JsonProperty("picUrl")
    @JSONField(name = "picUrl")
    @ApiModelProperty(value = "平面图url")
    @Size(max = 128, message = "平面图url（平面图url）长度不能超过128")
    private String picUrl;

    /**
     * 上传时间
     */
    @JsonProperty("uploadTime")
    @JSONField(name = "uploadTime", format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "上传时间")
    private LocalDateTime uploadTime;

    /**
     * 上传人
     */
    @JsonProperty("uploadBy")
    @JSONField(name = "uploadBy")
    @ApiModelProperty(value = "上传人")
    @Max(value = Integer.MAX_VALUE, message = "上传人（uploadBy）数值过大")
    private Integer uploadBy;

    /**
     * 区域层级
     */
    @JsonProperty("regionLevel")
    @JSONField(name = "regionLevel")
    @ApiModelProperty(value = "区域层级")
    @Max(value = Integer.MAX_VALUE, message = "区域层级（regionLevel）数值过大")
    private Integer regionLevel;

    /**
     * 排序
     */
    @JsonProperty("sort")
    @JSONField(name = "sort")
    @ApiModelProperty(value = "排序")
    @Max(value = Integer.MAX_VALUE, message = "排序（sort）数值过大")
    private Integer sort;

    /**
     * 是否默认区域， 1是，0否
     */
    @JsonProperty("isDefault")
    @JSONField(name = "isDefault")
    @ApiModelProperty(value = "是否默认区域， 1是，0否")
    @NotBlank(message = "是否默认区域（isDefault）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "是否默认区域（isDefault）长度不能小于1")
    @Size(max = 1, message = "是否默认区域（isDefault）长度不能超过1")
    private String isDefault;

    /**
     * 图片Base64（自定义，非数据库字段）
     */
    @JsonProperty("picBase64")
    @JSONField(name = "picBase64")
    @ApiModelProperty(value = "图片Base64（自定义，非数据库字段）")
    private String picBase64;
}
