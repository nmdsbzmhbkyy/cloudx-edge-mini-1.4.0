

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectParkingPlace;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 车位
 *
 * @author 王伟
 * @date 2020-05-08 10:24:42
 */
@Mapper
public interface ProjectParkingPlaceMapper extends BaseMapper<ProjectParkingPlace> {
    IPage<ProjectParkingPlaceVo> select(IPage<?> page, @Param("projectId") int projectId,
                                        @Param("placeName") String placeName,
                                        @Param("isUse") int isUse,
                                        @Param("parkId") String parkId);

    /**
     * <p>
     *  获取车位归属信息
     * </p>
     *
     * @param placeId 车位id
     * @param projectId 项目id
     * @return
     * @throws
    */
    ProjectParkingPlaceAssignmentVo getParkingOwnershipInformation(@Param("projectId") int projectId, @Param("placeId") String placeId);

    /**
     * <p>
     *  获取车位使用历史信息
     * </p>
     *
     * @param placeId 车位id
     * @param projectId 项目id
     * @return
     * @throws
    */
    List<ProjectParkingPlaceHisVo> getParkingHistory(@Param("projectId") int projectId, @Param("placeId") String placeId);

    /**
     * <p>
     *  车位归属管理分页查询
     * </p>
     * 
     * @param page 分页信息
     * @param page 查询条件
     * @return
     * @throws 
    */
    IPage<ProjectParkingPlaceManageRecordVo> selectParkingPlaceManage(IPage<?> page, @Param("query") ProjectParkingPlaceManageSearchConditionVo query);

    /**
     * <p>
     *  用于使用车场名-车位区域名-车位号 查询出车位ID
     * </p>
     *
     * @param placeAddress 包括车场到车位的完整名称使用-分隔
    */
    List<String> listPlaceIdByAddress(String placeAddress);

    /**
     * <p>
     *  根据车场ID获取到空置公共车位数
     * </p>
     *
     * @param parkId 车场ID
     * @return 空闲公共车位数
     * @author: 王良俊
    */
    int getPublicFreePlaceNum(@Param("parkId") String parkId);

    List<ProjectParkingPlace> listByParkRegionId(String parkRegionId);
}
