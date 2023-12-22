

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.dto.ProjectUserHouseDTO;
import com.aurine.cloudx.estate.entity.ProjectPersonDevice;
import com.aurine.cloudx.estate.entity.ProjectPersonPlanRel;
import com.aurine.cloudx.estate.openapi.ToOpenApi;
import com.aurine.cloudx.estate.openapi.enums.OpenApiServiceNameEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenPushSubscribeCallbackTypeEnum;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <<<<<<< Updated upstream
 * 人员设备权限关系表
 *
 * @author 王良俊
 * @date 2020-05-22 11:32:57
 */
@ToOpenApi(serviceType = OpenPushSubscribeCallbackTypeEnum.OPERATE, serviceName = OpenApiServiceNameEnum.PERSON_DEVICE_REL)
@Mapper
public interface ProjectPersonDeviceMapper extends BaseMapper<ProjectPersonDevice> {
    /**
     * 查询当前项目已配置权限的用户列表
     *
     * @return
     */
    List<String> findPerson(@Param("planIdList") List<String> planId, @Param("buildingId") String buildingId, @Param("unitId") String unitId, @Param("groupId") String groupId);

    /*  *//**
     * 查询住户权限列表 车位为主
     *
     * @param page
     * @param searchCondition
     * @return
     *//*
    IPage<ProjectProprietorDeviceRecordVo> findProprietorDeviceParkingPage(IPage<ProjectProprietorDeviceRecordVo> page,
                                                                           @Param("searchCondition") ProjectProprietorDeviceSearchCondition searchCondition,
                                                                           @Param("groupAddressPark") String groupAddressPark,
                                                                           @Param("projectId") Integer projectId);

    *//**
     * 查询住户权限列表 房屋为主
     *
     * @param page
     * @param searchCondition
     * @return
     *//*
    IPage<ProjectProprietorDeviceRecordVo> findProprietorDeviceHousePage(IPage<ProjectProprietorDeviceRecordVo> page,
                                                                         @Param("searchCondition") ProjectProprietorDeviceSearchCondition searchCondition,
                                                                         @Param("groupAddressHouse") String groupAddressHouse,
                                                                         @Param("projectId") Integer projectId);

    *//**
     * 查询住户权限列表  其他情况
     *
     * @param page
     * @param searchCondition
     * @return
     *//*
    IPage<ProjectProprietorDeviceRecordVo> findProprietorDevicePage(IPage<ProjectProprietorDeviceRecordVo> page,
                                                                    @Param("searchCondition") ProjectProprietorDeviceSearchCondition searchCondition,
                                                                    @Param("groupAddressPark") String groupAddressPark,
                                                                    @Param("groupAddressHouse") String groupAddressHouse,
                                                                    @Param("projectId") Integer projectId);
*/

    /**
     * 分页查询住户通行权限
     *
     * @param page            分页对象
     * @param searchCondition 查询条件
     * @return 分页数据
     * @author: 王良俊
     */
    @SqlParser(filter = true)
    IPage<ProjectProprietorDeviceRecordVo> fetchList(IPage<ProjectProprietorDeviceRecordVo> page,
                                                     @Param("searchCondition") ProjectProprietorDeviceSearchCondition searchCondition,
                                                     @Param("projectId") Integer projectId, @Param("isGroup") boolean isGroup);

    /**
     * 查询员工权限列表
     *
     * @param page
     * @param searchCondition
     * @return
     */
    IPage<ProjectStaffDeviceRecordVo> findStaffDevicePage(IPage page, @Param("searchCondition") ProjectStaffDeviceSearchConditionVo searchCondition);


    /**
     * 根据人员id获取该人员可通行设备
     *
     * @param personId         人物id
     * @param isGate           是否为获取全部区口机
     * @param isFrameGate      是否为楼栋所在区域区口机
     * @param isBuildingLadder 是否为本楼栋梯口机
     * @param isUnitLadder     是否未本单元梯口机
     * @param onlyPlan         是否只获取方案中的设备
     * @return
     */
    List<ProjectPassDeviceVo> listByPerson(
            @Param("personId") String personId,
            @Param("planId") String planId,
            @Param("isGate") boolean isGate,
            @Param("isFrameGate") boolean isFrameGate,
            @Param("isBuildingLadder") boolean isBuildingLadder,
            @Param("isUnitLadder") boolean isUnitLadder,
            @Param("onlyPlan") boolean onlyPlan
    );


    /**
     * @param personId
     * @return
     */
    Page<ProjectPassDeviceVo> pageByPerson(
            Page page,
            @Param("personId") String personId,
            @Param("isGate") boolean isGate,
            @Param("isFrameGate") boolean isFrameGate,
            @Param("isBuildingLadder") boolean isBuildingLadder,
            @Param("isUnitLadder") boolean isUnitLadder,
            @Param("onlyPlan") boolean onlyPlan
    );



    /**
     * <p>
     *  获取到今天过期的住户权限记录
     * </p>
     *
     * @author: 王良俊
     */
    List<ProjectPersonPlanRel> getTodayExpList();

    /**
     * 根据框架查询当前框架下可以访问的住户列表
     * 参数均为空，则返回所有开启了云对讲服务的住户列表
     *
     * @param groupId
     * @param buildingId
     * @param unitId
     * @param useHouseName 是否使用房间号作为roomNo，梯口设备传true，其他为false
     * @return
     */
    @SqlParser(filter = true)
    List<ProjectUserHouseDTO> listOriginByFrame(@Param("projectId") Integer projectId, @Param("groupId") String groupId, @Param("buildingId") String buildingId,
                                                @Param("unitId") String unitId, @Param("useHouseName") boolean useHouseName);
    /**
     * 根据房屋id和personid，获取可以使用的设备信息
     * 用于新增住户时，获取可插入的数据
     *
     * @param houseId
     * @param personId
     * @return
     */
    @SqlParser(filter = true)
    List<ProjectUserHouseDTO> listOriginByHousePerson(@Param("houseId") String houseId, @Param("personId") String personId);

}
