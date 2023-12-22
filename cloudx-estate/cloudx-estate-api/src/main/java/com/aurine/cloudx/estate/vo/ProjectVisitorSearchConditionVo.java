

package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 访客查询条件VO
 * @author 王良俊
 * @date 2020-06-04 11:51:11
 */
@Data
@ApiModel(value = "访客查询条件")
public class ProjectVisitorSearchConditionVo {
    private static final long serialVersionUID = 1L;

    /**
     * 是否签离 1 是 0 否 (也就是页面上的状态)
     */
    @ApiModelProperty(value = "是否签离 1 是 0 否")
    private String isLeave;

    /**
     * 是否是审核页面 1是 0否
     */
    @ApiModelProperty(value = "是否是审核页面 1是 0否")
    private Integer isAuditPage;
    /**
     * 访客类型 1 亲情人员 2 快递人员 3 外卖人员 4 住户服务
     */
    @ApiModelProperty(value="访客类型 1 亲情人员 2 快递人员 3 外卖人员 4 住户服务")
    private String visitorType;
    /**
     * 访客名
     */
    @ApiModelProperty(value = "访客名")
    private String visitorName;
    /**
     * 被访人
     */
    @ApiModelProperty(value = "被访人名")
    private String beVisitorName;
    /**
     * 筛选时间范围
     */
    @ApiModelProperty(value = "筛选时间范围")
    private String[] dateRange;
    /**
     * 审核状态
     */
    @ApiModelProperty(value = "审核状态 1 待审核 2 已通过 9 未通过 字典wechat_audit_status")
    private String auditStatus;

    @ApiModelProperty(value = "被房屋id")
    private List<String> houseIds;
}
