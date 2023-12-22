package com.aurine.cloudx.estate.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * @version 1.0
 * @author： 林功鑫
 * @date： 2022-02-08 15:23
 */
public class ExcelExportUtil {
    public void exportExcel(String excelName, Class head, List excelData, HttpServletResponse httpServletResponse) {
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        //内容策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        //设置背景颜色
        headWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        //设置 水平居中
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        //设置头居中
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);


//        CellColorSheetWriteHandler writeHandler = new CellColorSheetWriteHandler(excelData, IndexedColors.RED.getIndex());
//.registerWriteHandler(writeHandler)
        EasyExcel.write(getOutputStream(excelName, httpServletResponse), head).registerWriteHandler(horizontalCellStyleStrategy).sheet(excelName).doWrite(excelData);
//        EasyExcel.write(excelName, head).sheet().doWrite(excelData);
    }
    private static OutputStream getOutputStream(String fileName, HttpServletResponse response) {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            return response.getOutputStream();
        } catch (Exception e) {
            return null;
        }
    }
}
