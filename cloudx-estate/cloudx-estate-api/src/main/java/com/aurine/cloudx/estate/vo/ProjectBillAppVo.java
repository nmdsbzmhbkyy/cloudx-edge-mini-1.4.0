package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 前端返回数据
 */
@Data
@ApiModel("账单类型与金额总和")
public class ProjectBillAppVo {



    /**
     * 费用类型
     */
    @ApiModelProperty("费用类型")
    private String feeType;

    /**
     * 账单类型
     */
    @ApiModelProperty("类型总和")
    private  String moneySum;

}
