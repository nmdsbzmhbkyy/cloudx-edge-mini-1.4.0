package com.aurine.cloudx.estate.vo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@ApiModel("优惠账单查询vo")
public class AppBillPromotionVo {

    @ApiModelProperty("月份分组优惠后未缴费账单")
    private HashMap<String, Map<String, List<ProjectBillPromotionVo>>> mapList;
}
