package com.aurine.cloudx.estate.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@ApiModel("app已缴费账单vo")
public class AppBillingInfoVo extends Page {

    @ApiModelProperty("app月份分组已缴费账单")
    private Map<String, Map<String, List<ProjectBillingInfoVo>>> mapList;


    @ApiModelProperty("对照mapList中List注解")
    private List<ProjectBillingInfoVo> list;

}
