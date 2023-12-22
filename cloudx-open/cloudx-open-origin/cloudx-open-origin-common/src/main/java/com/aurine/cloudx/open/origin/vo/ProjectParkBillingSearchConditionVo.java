

package com.aurine.cloudx.open.origin.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>总收入分析VO</p>
 *
 * @ClassName: ProjectParkBillingSearchConditionVo
 * @author: 黄阳光 <huangyg@aurine.cn>
 * @date: 2020/7/13 15:30
 * @Copyright:
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "总收入分析")
public class ProjectParkBillingSearchConditionVo extends Model<ProjectParkBillingSearchConditionVo> {
    private static final long serialVersionUID = 1L;

    /**
     * 临时车缴费总计
     */
    @ApiModelProperty(value = "临时车缴费")
    private BigDecimal temp;

    /**
     * 月租车缴费总计
     */
    @ApiModelProperty(value = "月租车缴费")
    private BigDecimal monthly;

    /**
     * 支付时间
     */
    @ApiModelProperty(value = "支付时间")
    private LocalDateTime payTime;

    /**
     * 项目id
     */
    @ApiModelProperty("项目id")
    private Integer projectId;

    /**
     *
     */
    @ApiModelProperty(value = "", hidden = true)
    @TableField(value = "tenant_id")
    private Integer tenantId;

}
