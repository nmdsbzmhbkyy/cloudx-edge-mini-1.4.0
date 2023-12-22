package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 优惠关联费用表(ProjectPromotionFeeRel)表实体类
 *
 * @author makejava
 * @since 2020-07-20 16:43:48
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "优惠关联费用表(ProjectPromotionFeeRel)")
public class ProjectPromotionFeeRel extends OpenBasePo<ProjectPromotionFeeRel> {

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


}