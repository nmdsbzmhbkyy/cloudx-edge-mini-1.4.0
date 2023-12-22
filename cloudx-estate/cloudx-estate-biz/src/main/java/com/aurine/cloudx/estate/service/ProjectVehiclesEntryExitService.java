package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectVehiclesEntryExit;
import com.aurine.cloudx.estate.vo.ProjectVehiclesEntryExitTreeVo;
import com.aurine.cloudx.estate.vo.ProjectVehiclesEntryExitVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 车辆出入口信息表(ProjectVehiclesEntryExit)表服务接口
 *
 * @author 王良俊
 * @since 2020-08-17 10:08:53
 */
public interface ProjectVehiclesEntryExitService extends IService<ProjectVehiclesEntryExit> {

    /**
     * <p>
     *  根据id出入口id获取出入口信息
     * </p>
     *
     * @param entryId 出入口ID
     * @return 出入口对象
    */
    ProjectVehiclesEntryExitVo getByEntryId(String entryId);

    /**
     * <p>
     *  根据id出入口id获取出入口信息树形数据
     * </p>
     *
     * @param parkId 停车场ID
     * @return 出入口树形数据
    */
    List<ProjectVehiclesEntryExitTreeVo> getEntryExitTreeByParkId(String parkId);

    /**
     * <p>
     *  保存出入口信息
     * </p>
     *
     * @param entryExitVo 出入口信息vo对象
     * @return 保存是否成功
    */
    boolean saveEntryExit(ProjectVehiclesEntryExitVo entryExitVo);

    /**
     * <p>
     *  保存出入口信息
     * </p>
     *
     * @param entryExitVo 出入口信息vo对象
     * @return 更新是否成功
    */
    boolean updateEntryExit(ProjectVehiclesEntryExitVo entryExitVo);

    /**
     * <p>
     *  根据出入口id删除出入口信息以及和车道的关联
     * </p>
     * 
     * @param entryId 出入口ID
     * @return 是否删除成功
    */
    boolean removeEntryExit(String entryId);
    
}