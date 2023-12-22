package com.aurine.cloudx.estate.excel.projectAttendance;

import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.util.StyleUtil;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.estate.excel.ProjectAttendanceExcel;
import org.apache.poi.ss.usermodel.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;

/**
 * @Author
 * @Date
 * @Desc 拦截处理单元格创建
 */
public class CellColorSheetWriteHandler implements CellWriteHandler {
    /**
     * map
     * key：第i行
     * value：第i行中单元格索引集合
     */
    private HashMap<Integer, List<Integer>> map;

    private List<ProjectAttendanceExcel> list;
    /**
     * 颜色
     */
    private Short colorIndex;

    /**
     * 有参构造
     */
    public CellColorSheetWriteHandler(List<ProjectAttendanceExcel> list, Short colorIndex) {
        this.list = list;
        this.colorIndex = colorIndex;
    }

    /**
     * 无参构造
     */
    public CellColorSheetWriteHandler() {

    }

    /**
     * 在创建单元格之前调用
     */
    @Override
    public void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Head head, Integer columnIndex, Integer relativeRowIndex, Boolean isHead) {
    }

    /**
     * 在单元格创建后调用
     */
    @Override
    public void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
    }

    @Override
    public void afterCellDataConverted(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, CellData cellData, Cell cell, Head head, Integer integer, Boolean aBoolean) {

    }

    /**
     * 在单元上的所有操作完成后调用
     */
    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<CellData> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        /**
         * 考虑到导出数据量过大的情况，不对每一行的每一个单元格进行样式设置，只设置必要行中的某个单元格的样式
         */
        //当前行的第i列
        //不处理第一行
        if (0 != cell.getRowIndex()) {
            int i = cell.getColumnIndex();
            if (list != null && list.size() > 0) {
                LocalTime checkInTime = list.get(cell.getRowIndex() - 1).getCheckInTime();
                LocalTime checkOutTime = list.get(cell.getRowIndex() - 1).getCheckOutTime();
                LocalTime workTime = list.get(cell.getRowIndex() - 1).getWorkTime();
                LocalTime offworkTime = list.get(cell.getRowIndex() - 1).getOffworkTime();
                String result = list.get(cell.getRowIndex() - 1).getResult();
                Workbook workbook = cell.getSheet().getWorkbook();
                WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
                WriteFont cellWriteFont = new WriteFont();
                // 设置字体大小
                cellWriteFont.setFontHeightInPoints((short) 11);
                //设置字体颜色
                cellWriteFont.setColor(colorIndex);
                contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
                contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
                contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
                contentWriteCellStyle.setBorderTop(BorderStyle.THIN);
                //单元格颜色
                contentWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                contentWriteCellStyle.setWriteFont(cellWriteFont);
                CellStyle cellStyle = StyleUtil.buildHeadCellStyle(workbook, contentWriteCellStyle);
                if (i == 4 && null != checkInTime) {
                    if (checkInTime.isAfter(workTime)) {
                        cell.getRow().getCell(i).setCellStyle(cellStyle);
                    }
                }
                if (i == 6 && null != checkOutTime) {
                    if (checkOutTime.isBefore(offworkTime)) {
                        cell.getRow().getCell(i).setCellStyle(cellStyle);
                    }
                }
                if (i == 7 && StringUtil.isNotEmpty(result)) {
                    switch (Integer.valueOf(result)) {
                        case 0:
                            result = "排休";
                            break;
                        case 1:
                            result = "正常";
                            break;
                        case 2:
                            result = "迟到";
                            break;
                        case 3:
                            result = "早退";
                            break;
                        case 4:
                            result = "迟到、早退";
                            break;
                        case 5:
                            result = "旷工";
                            break;
                        case 6:
                            result = "下班漏打卡";
                            break;
                        case 7:
                            result = "上班漏打卡";
                            break;
                        case 8:
                            result = "迟到、漏打卡";
                            break;
                        case 9:
                            result = "早退、漏打卡";
                            break;

                    }
                    cell.getRow().getCell(i).setCellValue(result);
                }
            }
        }
    }
}






