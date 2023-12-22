package com.aurine.cloudx.estate.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.aurine.cloudx.estate.excel.entity.access.CenterDeviceExcel;
import com.aurine.cloudx.estate.excel.projectAttendance.CellColorSheetWriteHandler;
import lombok.Data;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-07-01
 * @Copyright:
 */
public class ExcelUtil {

    public <T> void exportExcel(String fileName, String sheetName, List dataList, HttpServletResponse httpServletResponse, DefaultExportStrategy<T> excelConfig) {
        EasyExcel.write(getOutputStream(fileName, httpServletResponse), excelConfig.getHeadList().get(0).getClass()).registerWriteHandler(excelConfig.getHorizontalCellStyleStrategy()).registerWriteHandler(excelConfig.getWriteHandler()).sheet(sheetName).doWrite(dataList);
    }

    public <T> void exportExcel(String fileName, String sheetName, List dataList, HttpServletResponse httpServletResponse, DefaultExportStrategy<T> excelConfig,Class clazz) {
        EasyExcel.write(getOutputStream(fileName, httpServletResponse), clazz).registerWriteHandler(excelConfig.getHorizontalCellStyleStrategy()).registerWriteHandler(excelConfig.getWriteHandler()).sheet(sheetName).doWrite(dataList);
    }

    public void main(String[] args) {
        ExcelUtil excelUtil = new ExcelUtil();
        List<Object> dataList = new ArrayList<>();
        HttpServletResponse httpServletResponse = null;
        List<CenterDeviceExcel> headList = null;

        excelUtil.exportExcel("", "", dataList, httpServletResponse, new DefaultExportStrategy<CenterDeviceExcel>(headList));


    }


    /**
     * @description:导出策略
     * @author: 王伟 <wangwei@aurine.cn>
     * @date: 2021-07-01
     * @Copyright:
     */
    @Data
    public abstract class AbstractExportStrategy<T> {

        CellColorSheetWriteHandler writeHandler;
        HorizontalCellStyleStrategy horizontalCellStyleStrategy;
        List<T> headList;
    }


    /**
     * @description:
     * @author: 王伟 <wangwei@aurine.cn>
     * @date: 2021-07-01
     * @Copyright:
     */
    @Data
    public class  DefaultExportStrategy<T> extends AbstractExportStrategy {
        public DefaultExportStrategy(List<T> headList) {
            WriteCellStyle headWriteCellStyle = new WriteCellStyle();
            //设置背景颜色
            headWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            //设置头字体
            WriteFont headWriteFont = new WriteFont();
            headWriteFont.setFontHeightInPoints((short) 13);
            headWriteFont.setBold(true);
            headWriteCellStyle.setWriteFont(headWriteFont);
            //设置头居中
            headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
            //内容策略
            WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
            //设置 水平居中
            contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
            horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);

            this.setHeadList(headList);
        }
        public DefaultExportStrategy(){

        }


    }

    private OutputStream getOutputStream(String fileName, HttpServletResponse response) {
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
