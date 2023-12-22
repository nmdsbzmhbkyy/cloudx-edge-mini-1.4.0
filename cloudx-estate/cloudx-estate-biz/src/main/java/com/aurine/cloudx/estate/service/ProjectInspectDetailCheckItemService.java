package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectInspectDetailCheckItem;
import com.aurine.cloudx.estate.entity.ProjectInspectDetailDevice;
import com.aurine.cloudx.estate.entity.ProjectInspectTaskDetail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 巡检任务明细检查项列表(ProjectInspectDetailCheckItem)表服务接口
 *
 * @author 王良俊
 * @since 2020-07-28 17:06:12
 */
public interface ProjectInspectDetailCheckItemService extends IService<ProjectInspectDetailCheckItem> {

    /**
     * <p>
     * 初始化巡检任务明细检查项数据
     * </p>
     *
     * @param taskDetailList 巡检任务明细设备对象列表
     * @return 处理结果
     */
    boolean initDetailCheckItem(List<ProjectInspectDetailDevice> taskDetailList);

    /**
     * <p>
     * 初始化巡检任务明细检查项数据
     * </p>
     *
     * @param detailDeviceIdList 明细设备ID列表
     * @return 处理结果
     */
    boolean removeDetailCheckItemByDetailDeviceIdList(List<String> detailDeviceIdList);

}