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
import java.math.BigDecimal;

/**
 * 户型信息Vo
 *
 * @author : Qiu
 * @date : 2021 12 16 17:16
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "户型信息Vo")
public class HouseDesignVo extends OpenBaseVo {

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
     * 户型配置编号
     */
    @JsonProperty("designId")
    @JSONField(name = "designId")
    @ApiModelProperty(value = "户型配置编号")
    @Null(message = "户型配置编号（designId）新增时需要为空", groups = {InsertGroup.class})
    @NotBlank(message = "户型配置编号（designId）不能为空", groups = {UpdateGroup.class})
    @Size(max = 32, message = "户型配置编号（designId）长度不能超过32")
    private String designId;

    /**
     * 房间数量
     */
    @JsonProperty("roomTotal")
    @JSONField(name = "roomTotal")
    @ApiModelProperty(value = "房间数量")
    @Max(value = Integer.MAX_VALUE, message = "房间数量（roomTotal）数值过大")
    private Integer roomTotal;

    /**
     * 客厅数量
     */
    @JsonProperty("hallTotal")
    @JSONField(name = "hallTotal")
    @ApiModelProperty(value = "客厅数量")
    @Max(value = Integer.MAX_VALUE, message = "客厅数量（hallTotal）数值过大")
    private Integer hallTotal;

    /**
     * 卫生间数量
     */
    @JsonProperty("bathroomTotal")
    @JSONField(name = "bathroomTotal")
    @ApiModelProperty(value = "卫生间数量")
    @Max(value = Integer.MAX_VALUE, message = "卫生间数量（bathroomTotal）数值过大")
    private Integer bathroomTotal;

    /**
     * 厨房数量
     */
    @JsonProperty("kitchenTotal")
    @JSONField(name = "kitchenTotal")
    @ApiModelProperty(value = "厨房数量")
    @Max(value = Integer.MAX_VALUE, message = "厨房数量（kitchenTotal）数值过大")
    private Integer kitchenTotal;

    /**
     * 户型描述
     * （因为数据库中就叫desginDesc，所以这里做一一对应）
     */
    @JsonProperty("desginDesc")
    @JSONField(name = "desginDesc")
    @ApiModelProperty(value = "户型描述")
    @NotBlank(message = "户型描述（desginDesc）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "户型描述（desginDesc）长度不能小于1")
    @Size(max = 64, message = "户型描述（desginDesc）长度不能超过64")
    private String desginDesc;

    /**
     * 面积
     */
    @JsonProperty("area")
    @JSONField(name = "area")
    @ApiModelProperty(value = "面积")
    @NotNull(message = "面积（area）不能为空", groups = {InsertGroup.class})
    @Digits(integer = 8, fraction = 2, message = "面积（area）格式不正确，整数最多8位，小数最多2位")
    @DecimalMin(value = "0.00", message = "面积（area）格式不正确，不能小于0")
    private BigDecimal area;
}

