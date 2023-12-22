package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * (ProjectComplainRecordResultFormVo)投诉处理结果表单视图
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/9/2 8:38
 */
@ApiModel("报修处理结果表单")
@Data
public class AppRepairRecordResultFormVo {
    /**
     * 投诉单号，uuid
     */
    @ApiModelProperty(value = "投诉单号，uuid", required = true)
    private String repairId;
    /**
     * 回复内容
     */
    @ApiModelProperty(value = "回复内容")
    private String remark;
    /**
     * 完成图片
     */
    @ApiModelProperty(value = "图片列表 Base64")
    private List<String> picBase64List;

    /**
     * 维修时间
     */
    @ApiModelProperty(value = "维修时间")
    private LocalDateTime maintainTime;

    /**
     * 维修费用
     */
    @ApiModelProperty(value = "维修费用")
    private BigDecimal feeAmount;

    /**
     * 维修类型
     */
    @ApiModelProperty(value = "维修类型")
    private String maintainType;


}
