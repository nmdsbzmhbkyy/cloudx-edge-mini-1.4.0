package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectInspectDetailDevice;
import com.aurine.cloudx.estate.entity.ProjectInspectTaskDetail;
import com.aurine.cloudx.estate.vo.ProjectInspectDetailDeviceVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 巡检任务明细设备列表(ProjectInspectDetailDevice)表服务接口
 *
 * @author 王良俊
 * @since 2020-07-28 17:06:31
 */
public interface ProjectInspectDetailDeviceService extends IService<ProjectInspectDetailDevice> {

    /**
     * <p>
     * 初始化巡检任务明细设备数据
     * </p>
     *
     * @param detailList 巡检任务明细数据
     * @return 处理结果
     */
    boolean initDetailDevice(List<ProjectInspectTaskDetail> detailList);

    /**
     * <p>
     * 删除巡检任务明细设备数据
     * </p>
     *
     * @param detailIdList 巡检任务明细id列表
     * @return 处理结果
     */
    boolean removeDetailDeviceByDetailId(List<String> detailIdList);

    /**
     * <p>
     * 根据任务明细id获取到该任务明细的设备列表
     * </p>
     *
     * @param detailId 巡检任务明细id
     * @return 巡检任务明细设备vo对象列表
     */
    List<ProjectInspectDetailDeviceVo> listDetailDeviceByDetailId(String detailId);

}