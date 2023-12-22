

package com.aurine.cloudx.open.origin.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
 * @ClassName: ProjectParkBillingTotalSearchConditionVo
 * @author: 黄阳光 <huangyg@aurine.cn>
 * @date: 2020/7/13 15:30
 * @Copyright:
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "总收入分析")
public class ProjectParkBillingTotalSearchConditionVo extends Model<ProjectParkBillingTotalSearchConditionVo> {
    private static final long serialVersionUID = 1L;

    /**
     * 停车场ID
     */
    @ApiModelProperty(value = "停车场ID")
    private String parkId;

    /**
     * 总收入
     */
    @ApiModelProperty(value = "总收入")
    private BigDecimal totalRevenue;

    /**
     * 临时车缴费总计
     */
    @ApiModelProperty(value = "临时车缴费总计")
    private BigDecimal totalTemp;

    /**
     * 月租车缴费总计
     */
    @ApiModelProperty(value = "月租车缴费总计")
    private BigDecimal totalMonthly;

    /**
     * 每日缴费
     */
    @ApiModelProperty(value = "每日缴费")
    private IPage<ProjectParkBillingSearchConditionVo> dailyPayment;

    /**
     * 时间检索开始
     */
    @ApiModelProperty(value = "时间检索开始")
    private LocalDateTime effTime;

    /**
     * 时间检索结束
     */
    @ApiModelProperty(value = "时间检索结束")
    private LocalDateTime expTime;

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
