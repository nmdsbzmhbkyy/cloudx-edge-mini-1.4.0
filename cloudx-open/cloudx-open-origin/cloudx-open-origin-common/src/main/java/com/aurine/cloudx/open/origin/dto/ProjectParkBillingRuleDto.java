package com.aurine.cloudx.open.origin.dto;

import com.aurine.cloudx.open.origin.entity.ProjectParkBillingRule;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Classname ProjectParkBillingRuleRecordDto
 * @Description 计费规则
 * @Date 2022/5/12 15:25
 * @Created by linlx
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ProjectParkBillingRuleDto extends ProjectParkBillingRule {
    private static final long serialVersionUID = 1L;
    /**
     * 停车场ID
     */
    private String parkId;
    /**
     * 停车场名称
     */
    private String parkName;

    /**
     * 规则ID
     */
    private String ruleId;
    /**
     * 第三方规则id
     */
    private String ruleCode;
    /**
     * 规则名称
     */
    private String ruleName;
    /**
     * 规则类型 1 免费车 2 月租车 3 临时车
     */
    private String ruleType;
    /**
     * 是否禁用 0 启用 1 禁用
     */
    private String isDisable;

    /**
     * 是否默认
     */
    private Integer isDefault;
    /**
     * （仅适用规则为临时）计费模板 1 标准24小时收费 2 标准分段收费
     */
    private String ruleTemplate;

    /**
     * 车辆类型ID
     */
    private String carTypeId;

    /**
     * 车辆类型
     */
    private Integer carType;

    /**
     * 车辆类型名称
     */
    private String typeName;

    /**
     * 月租费用（元）
     */
    private BigDecimal monthlyRent;

    /**
     * 操作人
     */
    private Integer operator;
    /**
     * 操作时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
