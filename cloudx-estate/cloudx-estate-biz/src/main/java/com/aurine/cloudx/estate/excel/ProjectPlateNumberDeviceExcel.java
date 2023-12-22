package com.aurine.cloudx.estate.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *  设备车牌号下发情况表
 * </p>
 *
 * @author 王良俊
 * @since 2022/5/26 14:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@HeadRowHeight(20)
public class ProjectPlateNumberDeviceExcel implements Serializable {

    /*
     * 车牌号
     **/
    @ColumnWidth(40)
    @ExcelProperty("车牌号")
    private String plateNumber;

    /*
     * 车牌状态
     **/
    @ColumnWidth(40)
    @ExcelProperty("车牌状态")
    private String plateNumberStatusCh;

    /*
     * 下载状态 0 下载成功 1 下载失败
     **/
    @ColumnWidth(40)
    @ExcelProperty("下载状态")
    private String dlStatusCh;

    /*
     * 下发时间
     **/
    @ColumnWidth(40)
    @ExcelProperty("下发时间")
    private String sendTime;

}
