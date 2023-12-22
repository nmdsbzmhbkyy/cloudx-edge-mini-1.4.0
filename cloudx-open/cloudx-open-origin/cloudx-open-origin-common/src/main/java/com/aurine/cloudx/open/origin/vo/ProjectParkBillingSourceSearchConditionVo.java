

package com.aurine.cloudx.open.origin.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <p>收入来源分析</p>
 *
 * @ClassName: ProjectParkBillingSourceSearchConditionVo
 * @author: 黄阳光 <huangyg@aurine.cn>
 * @date: 2020/7/13 15:30
 * @Copyright:
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "收入来源分析")
public class ProjectParkBillingSourceSearchConditionVo extends Model<ProjectParkBillingSourceSearchConditionVo> {
    private static final long serialVersionUID = 1L;

    /**
     * 现金/转账
     */
    @ApiModelProperty(value = "现金/转账")
    private BigDecimal cash;

    /**
     * 线下微信
     */
    @ApiModelProperty(value = "线下微信")
    private BigDecimal offlineWeChat;

    /**
     * 线下支付宝
     */
    @ApiModelProperty(value = "线下支付宝")
    private BigDecimal offlineAliPay;

    /**
     * 微信
     */
    @ApiModelProperty(value = "微信")
    private BigDecimal weChat;

    /**
     * 支付宝
     */
    @ApiModelProperty(value = "支付宝")
    private BigDecimal aliPay;

    /**
     * 其他
     */
    @ApiModelProperty(value = "其他")
    private BigDecimal other;

    /**
     * 支付时间
     */
    @ApiModelProperty(value = "支付时间")
    private LocalDate payTime;

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
