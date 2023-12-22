package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 优惠活动设置(ProjectPromotionConf)表实体类
 *
 * @author makejava
 * @since 2020-07-20 16:43:48
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "优惠活动设置(ProjectPromotionConf)")
public class ProjectPromotionConf extends OpenBasePo<ProjectPromotionConf> {

    private static final long serialVersionUID = -26610478091859716L;


    /**
     * 优惠活动id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "优惠活动id")
    private String promotionId;


    /**
     * 优惠活动名称
     */
    @ApiModelProperty(value = "优惠活动名称")
    private String promotionName;


    /**
     * 生效时间
     */
    @ApiModelProperty(value = "生效时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate effTime;


    /**
     * 失效时间
     */
    @ApiModelProperty(value = "失效时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expTime;


    /**
     * 优惠类型 1 预存优惠 2 普通优惠
     */
    @ApiModelProperty(value = "优惠类型 1 预存优惠 2 普通优惠")
    private String promotionType;


    /**
     * 欠缴情况要求 1 仅允许上个账期未缴 2 任何账期都不存在未缴 3 不限制
     */
    @ApiModelProperty(value = "欠缴情况要求 1 仅允许上个账期未缴 2 任何账期都不存在未缴 3 不限制")
    private String claim;


    /**
     * 预存月数
     */
    @ApiModelProperty(value = "预存月数")
    private Integer preStoreMonth;


    /**
     * 减免月数
     */
    @ApiModelProperty(value = "减免月数")
    private Integer reduceMonth;
    /**
     * 减免类型
     */
    @ApiModelProperty(value = "减免类型")
    private String discountType;


    /**
     * 折扣
     */
    @ApiModelProperty(value = "折扣")
    private BigDecimal discount;


}