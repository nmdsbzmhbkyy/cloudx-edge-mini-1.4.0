package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.origin.dto.ProjectEntryExitLaneDto;
import com.aurine.cloudx.open.origin.dto.ProjectParkingInfoDto;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceInfo;
import com.aurine.cloudx.open.origin.entity.ProjectEntryExitLane;
import com.aurine.cloudx.open.origin.vo.PlateNumberInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * 出入口车道信息(ProjectEntryExitLane)表数据库访问层
 *
 * @author makejava
 * @since 2020-08-17 11:58:43
 */
@Mapper
public interface ProjectEntryExitLaneMapper extends BaseMapper<ProjectEntryExitLane> {

    Page<ProjectEntryExitLaneDto> pageLane(Page page, @Param("query") ProjectEntryExitLaneDto projectEntryExitLaneDto, @Param("parkId") String parkId);

    ProjectEntryExitLaneDto getLaneById(@Param("laneId") String laneId);

    List<ProjectEntryExitLaneDto> getByParkId(@Param("parkId") String parkId);
    List<ProjectEntryExitLaneDto> listVideoUrlByParkId(@Param("parkId") String parkId);
    List<ProjectDeviceInfo> getDeviceList(@Param("checkedLanes") List<String> checkedLanes);
    List<ProjectParkingInfoDto> getParkingList(@Param("checkedLanes") List<String> checkedLanes);

    List<ProjectEntryExitLaneDto> listLaneById(@Param("laneIdList") Collection<String> laneIdList);

    /**
     * <p>
     * 根据车道ID获取车牌凭证信息
     * </p>
     *
     * @param laneId 车道ID
     * @author: 王良俊
     * @return 车牌凭证信息
     */
    List<PlateNumberInfo> listPlateNumberInfoByLaneId(@Param("laneId") String laneId);
}