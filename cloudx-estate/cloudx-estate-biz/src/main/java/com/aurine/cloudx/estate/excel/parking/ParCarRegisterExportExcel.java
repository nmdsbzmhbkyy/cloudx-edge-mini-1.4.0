package com.aurine.cloudx.estate.excel.parking;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.aurine.cloudx.estate.excel.converter.LocalDateConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @version 1.0
 * @author： 林功鑫
 * @date： 2022-02-07 11:35
 */
@Data
public class ParCarRegisterExportExcel implements Serializable {
    /**
     * 车场名
     */
    @ExcelProperty(value = "车场名", index = 0)
    @ColumnWidth(value = 20)
    private String parkName;
    /**
     * 车牌号
     */
    @ExcelProperty(value = "车牌号", index = 1)
    @ColumnWidth(value = 20)
    private String plateNumber;
    /**
     * 关系类型 0 闲置(公共) 1 产权 2 租赁
     */
    @ExcelProperty(value = "车位类型", index = 2)
    @ColumnWidth(value = 20)
    private String relType;
    /**
     * 车位地址
     */
    @ExcelProperty(value = "车位地址", index = 3)
    @ColumnWidth(value = 20)
    private String parkPlaceName;
    /**
     * 月租车收费规则id，关联project_park_billing_rule 因为一位多车调整导致收费规则需要与车辆直接绑定，而不是原本的车位
     */
    @ExcelProperty(value = "计费方式", index = 4)
    @ColumnWidth(value = 20)
    private String ruleName;
    /**
     * 车主名
     */
    @ExcelProperty(value = "车主名", index = 5)
    @ColumnWidth(value = 20)
    private String personName;
    /**
     * 开始日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd ")
    @ExcelProperty(value = "开始日期", index = 6, converter = LocalDateConverter.class)
    @ColumnWidth(value = 20)
    private LocalDate startTime;
    /**
     * 结束日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd ")
    @ExcelProperty(value = "结束日期",  index = 7,converter = LocalDateConverter.class)
    @ColumnWidth(value = 20)
    private LocalDate endTime;
}
