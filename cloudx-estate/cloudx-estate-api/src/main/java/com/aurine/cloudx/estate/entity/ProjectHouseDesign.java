

package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
public class ProjectHouseDesign extends Model<ProjectHouseDesign> {
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
    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    @TableField(fill = FieldFill.INSERT)
    private Integer operator;
    /**
     * 操作时间，东八区
     */
    @ApiModelProperty(value = "操作时间，东八区")
    private LocalDateTime createTime;
    /**
     * 更新时间，东八区
     */
    @ApiModelProperty(value = "更新时间，东八区")
    private LocalDateTime updateTime;
}
