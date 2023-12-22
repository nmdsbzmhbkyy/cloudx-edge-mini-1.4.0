package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectPaymentRecord;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

/**
 * (ProjectPaymentRecordFormVo)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/8/13 14:40
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("交易记录表单")
public class ProjectPaymentRecordFormVo extends ProjectPaymentRecord {
    @ApiModelProperty("开始时间字符串")
    private String startTimeString;
    @ApiModelProperty("结束时间字符串")
    private String endTimeString;
    @ApiModelProperty(hidden = true)
    private LocalDate startTime;
    @ApiModelProperty(hidden = true)
    private LocalDate endTime;
    @ApiModelProperty("0 为收入 1 为支出")
    private String paidType;
    @ApiModelProperty("账户id")
    private String accountId;
    @ApiModelProperty("多选导出项目")
    private List<ProjectPaymentRecord> selectRow;
}
