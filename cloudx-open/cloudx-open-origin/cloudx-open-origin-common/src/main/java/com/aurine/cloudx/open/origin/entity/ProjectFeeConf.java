package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 费用设置(ProjectFeeConf)表实体类
 *
 * @author makejava
 * @since 2020-07-20 16:09:18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "费用设置(ProjectFeeConf)")
public class ProjectFeeConf extends OpenBasePo<ProjectFeeConf> {

    private static final long serialVersionUID = -33966051520394836L;


    /**
     * 费用id
     */
    @ApiModelProperty(value = "费用id")
    @TableId(type = IdType.ASSIGN_UUID)
    private String feeId;


    /**
     * 费用名称
     */
    @ApiModelProperty(value = "费用名称")
    private String feeName;
    /**
     * 状态 0 已停用 1 正常
     */
    @ApiModelProperty(value = "状态 0 已停用 1 正常")
    private String status;


    /**
     * 费用类型，参考字典fee_type
     */
    @ApiModelProperty(value = "费用类型，参考字典fee_type")
    private String feeType;


    /**
     * 收费周期类型 1 固定 2 临时
     */
    @ApiModelProperty(value = "收费周期类型 1 固定 2 临时")
    private String feeCycleType;



    /**
     * 收费周期 1 1个月 2 2个月 3 3个月 6 半年 12 一年
     */
    @ApiModelProperty(value = "收费周期 1 1个月 2 2个月 3 3个月 4 半年 5 一年")
    private String feeCycle;


    /**
     * 计费周期类型 1 提前一个周期 2 收取当前周期
     */
    @ApiModelProperty(value = "计费周期类型 1 提前一个周期 2 收取当前周期")
    private String billCycleType;


    /**
     * 计费起始日类型 1 同房屋入住日 2 固定日
     */
    @ApiModelProperty(value = "计费起始日类型 1 同房屋入住日 2 固定日")
    private String feeStartDayType;


    /**
     * 计费起始日，仅类型为固定日时生效
     */
    @ApiModelProperty(value = "计费起始日，仅类型为固定日时生效")
    private String feeStartDay;


    /**
     * 是否定额收费 1 是 0 否
     */
    @ApiModelProperty(value = "是否定额收费 1 是 0 否")
    @TableField("isFixAmount")
    private String fixAmountOrNot;


    /**
     * 定额费用
     */
    @ApiModelProperty(value = "定额费用")
    private BigDecimal fixAmount;


    /**
     * 是否分阶计费 1 是 0 否
     */
    @ApiModelProperty(value = "是否分阶计费 1 是 0 否")
    @TableField("isDifferential")
    private String differentialOrNot;


    /**
     * 公式 1 单价*建筑面积 2 单价*套内面积
     */
    @ApiModelProperty(value = "公式 1 单价*建筑面积 2 单价*套内面积")
    private String formula;
    /**
     * 单位
     */
    @ApiModelProperty("单位")
    private String unit;
    /**
     * 单价
     */
    @ApiModelProperty("单价")
    private BigDecimal unitPrice;


    /**
     * 项目id
     */
    @ApiModelProperty("项目id")
    private Integer projectId ;


}