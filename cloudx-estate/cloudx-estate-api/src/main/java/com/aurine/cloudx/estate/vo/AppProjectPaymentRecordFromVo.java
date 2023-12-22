package com.aurine.cloudx.estate.vo;


import com.aurine.cloudx.estate.entity.ProjectPaymentRecord;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "账单信息查询条件")
    public class AppProjectPaymentRecordFromVo extends Page {

    /**
     * 输入框搜索条件
     */
    @ApiModelProperty("输入框搜索条件")
    private String textString;

    /**
     * 缴费时间(app端查询条件)
     */
    @ApiModelProperty("缴费时间(日期传所选月份1号)")
    private String payDateString;


    /**
     * 订单状态
     */
    @ApiModelProperty("订单状态")
    private String orderStatus;

}
