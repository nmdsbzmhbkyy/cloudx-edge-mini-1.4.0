package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 优惠关联费用表(ProjectPromotionFeeRel)表实体类
 *
 * @author makejava
 * @since 2020-07-20 16:43:48
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "优惠关联费用表(ProjectPromotionFeeRel)")
public class ProjectPromotionFeeRel extends Model<ProjectPromotionFeeRel> {

    private static final long serialVersionUID = -22688152899605153L;


    /**
     * 关联id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "关联id")
    private String relId;


    /**
     * 优惠活动id
     */
    @ApiModelProperty(value = "优惠活动id")
    private String promotionId;


    /**
     * 费用id
     */
    @ApiModelProperty(value = "费用id")
    private String feeId;


    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Integer operator;


    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;


}