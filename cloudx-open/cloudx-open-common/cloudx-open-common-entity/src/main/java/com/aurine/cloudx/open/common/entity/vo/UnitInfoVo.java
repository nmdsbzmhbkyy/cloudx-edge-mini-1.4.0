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
 * 单元信息Vo
 *
 * @author : Qiu
 * @date : 2021 12 16 17:16
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "单元信息Vo")
public class UnitInfoVo extends OpenBaseVo {

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
     * 单元id
     */
    @JsonProperty("unitId")
    @JSONField(name = "unitId")
    @ApiModelProperty(value = "单元id")
    @Null(message = "单元id（unitId）新增时需要为空", groups = {InsertGroup.class})
    @NotBlank(message = "单元id（unitId）不能为空", groups = {UpdateGroup.class})
    @Size(max = 32, message = "单元id（unitId）长度不能超过32")
    private String unitId;

    /**
     * 单元编码
     */
    @JsonProperty("unitCode")
    @JSONField(name = "unitCode")
    @ApiModelProperty(value = "单元编码")
    @Size(max = 64, message = "单元编码（unitCode）长度不能超过64")
    private String unitCode;

    /**
     * 框架编码
     */
    @JsonProperty("frameNo")
    @JSONField(name = "frameNo")
    @ApiModelProperty(value = "框架编码")
    @Size(max = 20, message = "框架编码（frameNo）长度不能超过20")
    private String frameNo;

    /**
     * 标准地址串(应该就是前端的地址编码了)
     */
    @JsonProperty("standardAddress")
    @JSONField(name = "standardAddress")
    @ApiModelProperty(value = "标准地址串")
    @Size(max = 128, message = "标准地址串（standardAddress）长度不能超过128")
    private String standardAddress;

    /**
     * 单元名称
     */
    @JsonProperty("unitName")
    @JSONField(name = "unitName")
    @ApiModelProperty(value = "单元名称")
    @NotBlank(message = "单元名称（unitName）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "单元名称（unitName）长度不能小于1")
    @Size(max = 32, message = "单元名称（unitName）长度不能超过32")
    private String unitName;

    /**
     * 地址名称
     */
    @JsonProperty("addressName")
    @JSONField(name = "addressName")
    @ApiModelProperty(value = "地址名称")
    @Size(max = 128, message = "地址名称（addressName）长度不能超过128")
    private String addressName;

    /**
     * 图片1
     */
    @JsonProperty("picUrl1")
    @JSONField(name = "picUrl1")
    @ApiModelProperty(value = "图片1")
    @Size(max = 128, message = "图片1（picUrl1）长度不能超过128")
    private String picUrl1;

    /**
     * 图片2
     */
    @JsonProperty("picUrl2")
    @JSONField(name = "picUrl2")
    @ApiModelProperty(value = "图片2")
    @Size(max = 128, message = "图片2（picUrl2）长度不能超过128")
    private String picUrl2;

    /**
     * 图片3
     */
    @JsonProperty("picUrl3")
    @JSONField(name = "picUrl3")
    @ApiModelProperty(value = "图片3")
    @Size(max = 128, message = "图片3（picUrl3）长度不能超过128")
    private String picUrl3;

    /**
     * 备注
     */
    @JsonProperty("remark")
    @JSONField(name = "remark")
    @ApiModelProperty(value = "备注")
    @Size(max = 128, message = "备注（remark）长度不能超过128")
    private String remark;

    /**
     * 图片1Base64（自定义，非数据库字段）
     */
    @JsonProperty("pic1Base64")
    @JSONField(name = "pic1Base64")
    @ApiModelProperty(value = "图片1Base64（自定义，非数据库字段）")
    private String pic1Base64;

    /**
     * 图片2Base64（自定义，非数据库字段）
     */
    @JsonProperty("pic2Base64")
    @JSONField(name = "pic2Base64")
    @ApiModelProperty(value = "图片2Base64（自定义，非数据库字段）")
    private String pic2Base64;

    /**
     * 图片3Base64（自定义，非数据库字段）
     */
    @JsonProperty("pic3Base64")
    @JSONField(name = "pic3Base64")
    @ApiModelProperty(value = "图片3Base64（自定义，非数据库字段）")
    private String pic3Base64;
}

