package com.aurine.cloudx.estate.open.vistor.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("访客查询条件")
@Data
public class ProjectVisitorSearchConditionPage extends Page{
    @ApiModelProperty("是否签离 1 是 0 否")
    private String isLeave;
    @ApiModelProperty("是否是审核页面 1是 0否")
    private Integer isAuditPage;
    @ApiModelProperty("访客类型 1 亲情人员 2 快递人员 3 外卖人员 4 住户服务")
    private String visitorType;
    @ApiModelProperty("访客名")
    private String visitorName;
    @ApiModelProperty("被访人名")
    private String beVisitorName;
    @ApiModelProperty("筛选时间范围")
    private String[] dateRange;
    @ApiModelProperty("审核状态 字典 audit_status")
    private String auditStatus;
    @ApiModelProperty("被房屋id")
    private List<String> houseIds;
}
