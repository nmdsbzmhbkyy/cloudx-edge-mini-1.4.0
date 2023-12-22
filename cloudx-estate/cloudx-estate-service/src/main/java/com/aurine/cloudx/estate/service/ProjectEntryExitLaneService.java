package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectEntryExitLane;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 出入口车道信息(ProjectEntryExitLane)表服务接口
 *
 * @author 王良俊
 * @since 2020-08-17 11:58:43
 */
public interface ProjectEntryExitLaneService extends IService<ProjectEntryExitLane> {

    /**
     * <p>
     *  根据出入口id和车道id列表更新其车道对应关系
     * </p>
     *
     * @param entryId 出入口ID
     * @param laneIdList 车道ID列表
     * @return 更新是否成功
     */
    boolean updateEntryExitLaneRel(String[] laneIdList, String entryId);

    /**
     * <p>
     *  根据出入口id删除其与车道的关系
     * </p>
     *
     * @param entryId 出入口ID
     * @return 是否成功
     */
    boolean removeEntryExitLaneRel(String entryId);

    /**
     * <p>
     *  根据车场ID获取到这个车场的车道列表
     * </p>
     *
     * @param parkId 车场id
     * @return 车道列表
    */
    List<ProjectEntryExitLane> listByParkId(String parkId);

    /**
     * <p>
     *  根据车场ID和出入口ID获取到车道列表
     * </p>
     *
     * @param parkId 车场id
     * @return 车道列表
    */
    List<ProjectEntryExitLane> listByParkId(String parkId, String entryId);


}