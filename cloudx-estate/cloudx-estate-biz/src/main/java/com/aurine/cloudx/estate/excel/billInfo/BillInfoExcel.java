package com.aurine.cloudx.estate.excel.billInfo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.aurine.cloudx.estate.excel.converter.LocalDateTimeConverter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BillInfoExcel {
    @ExcelProperty(value = "账单名称", index = 0)
    @ColumnWidth(value = 20)
    private String billMonthName;
    @ExcelProperty(value = "费用名称", index = 1)
    @ColumnWidth(value = 20)
    private String feeName;
    @ExcelProperty(value = "缴费状态", index = 2)
    @ColumnWidth(value = 20)
    private String payStatus;
    @ExcelProperty(value = "实收金额", index = 3)
    @ColumnWidth(value = 20)
    private BigDecimal actAmount;
    @ExcelProperty(value = "优惠金额", index = 4)
    @ColumnWidth(value = 20)
    private BigDecimal promotionAmount;
    @ExcelProperty(value = "应收金额", index = 5)
    @ColumnWidth(value = 20)
    private BigDecimal payAbleAmount;
    @ExcelProperty(value = "计费标准", index = 6)
    @ColumnWidth(value = 20)
    private String unitString;
    @ExcelProperty(value = "用量", index = 7)
    @ColumnWidth(value = 20)
    private String houseArea;
    @ExcelProperty(value = "计费周期", index = 8)
    @ColumnWidth(value = 30)
    private String feeCycle;

    @ExcelProperty(value = "创建时间", index = 9,converter = LocalDateTimeConverter.class)
    @ColumnWidth(value = 20)
    private LocalDateTime createTime;
}
