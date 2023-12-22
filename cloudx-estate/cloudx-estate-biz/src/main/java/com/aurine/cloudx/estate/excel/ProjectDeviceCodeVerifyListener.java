package com.aurine.cloudx.estate.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.service.ProjectDeviceInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
public class ProjectDeviceCodeVerifyListener extends AnalysisEventListener {

    private final ProjectDeviceInfoService projectDeviceInfoService;

    // 导入每行的初始对象
    private List<String> codeList = new ArrayList<>();

    public List<String> getList(){
        return codeList;
    }

    public ProjectDeviceCodeVerifyListener(ProjectDeviceInfoService projectDeviceInfoService){
        this.projectDeviceInfoService = projectDeviceInfoService;
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
        ProjectDeviceInfo codeDevice = projectDeviceInfoService.getOne(new LambdaQueryWrapper<>(ProjectDeviceInfo.class)
                .eq(ProjectDeviceInfo::getDeviceCode, deviceInfo.getDeviceCode()));
        if(codeDevice != null){
            codeList.add(deviceInfo.getDeviceCode());
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }

}
