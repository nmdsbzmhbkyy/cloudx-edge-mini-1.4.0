

package com.aurine.cloudx.open.origin.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>收入来源分析总计</p>
 *
 * @ClassName: ProjectParkBillingSourceSearchTotalConditionVo
 * @author: 黄阳光 <huangyg@aurine.cn>
 * @date: 2020/7/13 15:30
 * @Copyright:
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "收入来源分析总计")
public class ProjectParkBillingSourceSearchTotalConditionVo extends Model<ProjectParkBillingSourceSearchTotalConditionVo> {
    private static final long serialVersionUID = 1L;

    /**
     * 停车场ID
     */
    @ApiModelProperty(value = "停车场ID")
    private String parkId;

    /**
     * 支付时间
     */
    @ApiModelProperty(value = "支付时间")
    private LocalDate payTime;

    /**
     * 现金/转账
     */
    @ApiModelProperty(value = "现金/转账")
    private BigDecimal cashTotal;

    /**
     * 线下微信
     */
    @ApiModelProperty(value = "线下微信")
    private BigDecimal offlineWeChatTotal;

    /**
     * 线下支付宝
     */
    @ApiModelProperty(value = "线下支付宝")
    private BigDecimal offlineAliPayTotal;

    /**
     * 微信
     */
    @ApiModelProperty(value = "微信")
    private BigDecimal weChatTotal;

    /**
     * 支付宝
     */
    @ApiModelProperty(value = "支付宝")
    private BigDecimal aliPayTotal;

    /**
     * 其他
     */
    @ApiModelProperty(value = "其他")
    private BigDecimal otherTotal;

    /**
     * 每日收入来源分析
     */
    @ApiModelProperty(value = "每日收入来源分析")
    private IPage<ProjectParkBillingSourceSearchConditionVo> dailyIncome;

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
