package com.aurine.cloudx.estate.excel.payment;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.aurine.cloudx.estate.excel.converter.LocalDateTimeConverter;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentRecordExcel {
    @ExcelProperty(value = "支付单号", index = 0)
    @ColumnWidth(value = 40)
    private String payOrderNo;
    @ExcelProperty(value = "账单号", index = 1)
    @ColumnWidth(value = 40)
    private String billingNo;
    @ExcelProperty(value = "类型", index = 2)
    @ColumnWidth(value = 20)
    private String payType;
    @ExcelProperty(value = "金额（元）", index = 3)
    @ColumnWidth(value = 20)
    private BigDecimal actAmount;
    @ExcelProperty(value = "余额 (元)", index = 4)
    @ColumnWidth(value = 20)
    private BigDecimal balance;
    @ExcelProperty(value = "时间", index = 5, converter = LocalDateTimeConverter.class)
    @ColumnWidth(value = 20)
    private LocalDateTime createTime;
}
