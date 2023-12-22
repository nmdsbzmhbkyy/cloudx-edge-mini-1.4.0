

package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 项目户型配置表
 *
 * @author pigx code generator
 * @date 2020-05-06 15:22:42
 */
@Data
@TableName("project_house_design")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "项目户型配置表")
public class ProjectHouseDesign extends OpenBasePo<ProjectHouseDesign> {
    private static final long serialVersionUID = 1L;

//    /**
//     * 序列
//     */
//    @ApiModelProperty(value="序列")
//    private Integer seq;
    /**
     * 项目编码
     */
    @ApiModelProperty(value = "项目编码")
    private Integer projectId;
    /**
     * 户型配置编号
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "户型配置编号")
    private String designId;
    /**
     * 房间数量
     */
    @ApiModelProperty(value = "房间数量")
    private Integer roomTotal;
    /**
     * 客厅数量
     */
    @ApiModelProperty(value = "客厅数量")
    private Integer hallTotal;
    /**
     * 卫生间数量
     */
    @ApiModelProperty(value = "卫生间数量")
    private Integer bathroomTotal;
    /**
     * 厨房数量
     */
    @ApiModelProperty(value = "厨房数量")
    private Integer kitchenTotal;
    /**
     * 户型描述
     */
    @ApiModelProperty(value = "户型描述")
    private String desginDesc;
    /**
     * 面积
     */
    @ApiModelProperty(value = "面积")
    private BigDecimal area;
}
