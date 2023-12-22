package com.aurine.cloudx.estate.excel.invoke.entity;

import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoVo;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>Excel每行的解析结果</p>
 * @author : 王良俊
 * @date : 2021-09-02 10:40:15
 */
@Data
@NoArgsConstructor
public class RowInvokeResult {

    /**
     * 该行是否导入失败
     * */
    private boolean failed;

    /**
     * 该行导入失败的原因
     * */
    private String failedResult;

    /**
     * 每行解析完的设备信息对象
     * */
    private ProjectDeviceInfoVo deviceInfoVo;


    public RowInvokeResult(Boolean failed, ProjectDeviceInfoVo deviceInfoVo) {
        this.failed = failed;
        this.deviceInfoVo = deviceInfoVo;
    }

    public RowInvokeResult(Boolean failed, String failedResult) {
        this.failed = failed;
        this.failedResult = failedResult;
    }

    public RowInvokeResult(boolean failed) {
        this.failed = failed;
    }

    public static RowInvokeResult failed(String failedResult) {
        return new RowInvokeResult(true, failedResult);
    }

    public static RowInvokeResult success(ProjectDeviceInfoVo deviceInfoVo) {
        return new RowInvokeResult(false, deviceInfoVo);
    }

    public static RowInvokeResult success() {
        return new RowInvokeResult(false);
    }

}
