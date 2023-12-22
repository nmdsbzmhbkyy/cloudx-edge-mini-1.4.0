package com.aurine.cloudx.estate.service;
import com.aurine.cloudx.estate.entity.ProjectDeviceLoadLogDetail;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 设备导入日志明细(ProjectDeviceLoadLogDetail)表服务接口
 *
 * @author 王良俊
 * @since 2021-06-03 10:49:52
 */
public interface ProjectDeviceLoadLogDetailService extends IService<ProjectDeviceLoadLogDetail> {

    /**
    * <p>
    * 这里更新所有非导入完成的为导入失败，并加上统一的失败原因（不包括导入成功和失败两种状态）
    * </p>
    *
    * @param batchId 导入记录的批次ID
    * @author: 王良俊
    */
    void updateToFailed(String batchId, String errorMessage);
}
