package com.aurine.cloudx.open.origin.service;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceLoadLog;
import com.aurine.cloudx.open.origin.vo.ProjectDeviceLoadLogVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;

/**
 * 设备导入日志(ProjectDeviceLoadLog)表服务接口
 *
 * @author 王良俊
 * @since 2021-06-03 10:49:35
 */
public interface ProjectDeviceLoadLogService extends IService<ProjectDeviceLoadLog> {


    /**
    * <p>
    * 获取设备导入日志分页数据
    * </p>
    *
    * @param deviceType 根据设备类型进行区分
    * @author: 王良俊
    */
    Page<ProjectDeviceLoadLogVo> fetchList(Page<ProjectDeviceLoadLogVo> page, String deviceType);


    /**
     * <p>
     * 获取导入失败的Excel文件的临时下载链接
     * </p>
     *
     * @param batchId 批次ID
     * @author: 王良俊
     */
    void getFailedExcelLink(String batchId, HttpServletResponse response);
}
