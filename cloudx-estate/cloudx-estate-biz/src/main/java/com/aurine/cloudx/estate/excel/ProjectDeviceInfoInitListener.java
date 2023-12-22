package com.aurine.cloudx.estate.excel;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.aurine.cloudx.estate.constant.LoadStatusConstants;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectDeviceLoadLogDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  先读取整个Excel文件先把要添加的数据记录到
 * </p>
 * @author : 王良俊
 * @date : 2021-06-03 11:43:20
 */
@Slf4j
public class ProjectDeviceInfoInitListener extends AnalysisEventListener {

    // 导入的批次ID
    String batchId;

    // 导入每行的初始对象
    List<ProjectDeviceLoadLogDetail> loadLogDetailList;

    public ProjectDeviceInfoInitListener(String batchId, List<ProjectDeviceLoadLogDetail> loadLogDetailList) {
        if (StrUtil.isEmpty(batchId)) {
            throw new RuntimeException("设备导入缺少批次ID");
        }
        this.batchId = batchId;
        this.loadLogDetailList = loadLogDetailList == null ? new ArrayList<>() : loadLogDetailList;
    }

    @Override
    public void onException(Exception exception, AnalysisContext context) {
    }

    /**
     * 单行数据解析调用方法
     *
     * @param t               导入的实体
     * @param analysisContext 分析上下文内容
     */
    @Override
    public void invoke(Object t, AnalysisContext analysisContext) {
        ProjectDeviceInfo deviceInfo = new ProjectDeviceInfo();
        BeanUtils.copyProperties(t, deviceInfo);
        ProjectDeviceLoadLogDetail deviceLoadLogDetail = new ProjectDeviceLoadLogDetail();
        deviceLoadLogDetail.setDeviceName(deviceInfo.getDeviceName());
        deviceLoadLogDetail.setSN(deviceInfo.getSn());
        deviceLoadLogDetail.setBatchId(batchId);
        deviceLoadLogDetail.setLoadStatus(LoadStatusConstants.NOT_IMPORT);
        deviceLoadLogDetail.setRowNo(analysisContext.readRowHolder().getRowIndex());
        loadLogDetailList.add(deviceLoadLogDetail);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }

}
