package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.common.entity.vo.DeviceInfoVo;
import com.aurine.cloudx.open.origin.dto.ProjectParkingDeviceInfoDto;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceInfo;
import com.aurine.cloudx.open.origin.entity.ProjectNoticeDevice;
import com.aurine.cloudx.open.origin.vo.*;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备信息表
 *
 * @author xull@aurine.cn
 * @date 2020-05-20 10:38:59
 */
@Mapper
//@CacheNamespace
public interface ProjectDeviceInfoMapper extends BaseMapper<ProjectDeviceInfo> {

    /**
     * 分页查询设备信息(中心机,梯口机,区口机,室内机,编码设备)
     *
     * @param page
     * @param projectDeviceInfoPageFormVo
     * @param projectDeviceInfoPageFormVo
     * @return
     */
    Page<ProjectDeviceInfoPageVo> pageVo(Page page, @Param("query") ProjectDeviceInfoPageFormVo projectDeviceInfoPageFormVo, @Param("projectId") Integer projectId);

    /**
     * 分页查询监控设备信息
     *
     * @param page
     * @param projectDeviceInfoFormVo
     * @return
     */
    Page<ProjectDeviceCameraVo> pageCameraVo(Page page, @Param("query") ProjectDeviceInfoPageFormVo projectDeviceInfoFormVo);

    /**
     * 分页查询报警主机信息
     *
     * @param page
     * @param projectDeviceInfoFormVo
     * @return
     */
    Page<ProjectAlarmHostVo> pageAlarmVo(Page page, @Param("query") ProjectDeviceInfoPageFormVo projectDeviceInfoFormVo);


    /**
     * 分页查询智能水表信息
     *
     * @param page
     * @param projectDeviceInfoPageFormVo
     * @return
     */
    Page<ProjectDeviceVo> pageWaterMeterVo(Page page, @Param("query") ProjectDeviceInfoPageFormVo projectDeviceInfoPageFormVo);

    /**
     * 分页查询设备信息(液位计，烟感，智能井盖，智能路灯)
     *
     * @param page
     * @param projectDeviceInfoFormVo
     * @return
     */
    Page<ProjectDeviceVo> pageDeviceVo(Page page, @Param("query") ProjectDeviceInfoPageFormVo projectDeviceInfoFormVo);


    /**
     * 获取设备信息
     *
     * @param id
     * @return
     */
    ProjectDeviceInfoVo getProjectDeviceInfoById(@Param("id") String id);

    /**
     * 根据id获取到该巡检点绑定的设备列表
     *
     * @param pointId 巡检点uid
     * @return
     * @author 王良俊
     */
    List<ProjectPointDeviceVo> listDeviceByPointId(@Param("pointId") String pointId);

    /**
     * 通过thirdPartyCode获取完整设备信息
     *
     * @param thirdPartyCode
     * @return
     * @author: 王伟
     * @since： 2020-08-11
     */
    @SqlParser(filter = true)
    ProjectDeviceInfoProxyVo getByThirdPartyCode(@Param("thirdPartyCode") String thirdPartyCode);

    /**
     * 通过thirdPartyNo获取完整设备信息
     *
     * @param thirdPartyNo
     * @return
     * @author: 王伟
     * @since： 2021-07-08 10:18
     */
    @SqlParser(filter = true)
    ProjectDeviceInfoProxyVo getByThirdPartyNo(@Param("thirdPartyNo") String thirdPartyNo);

//
//    /**
//     * thirdpartyCode
//     *
//     * @param thirdpartyCode
//     * @return
//     * @author: 王伟
//     * @since： 2020-11-26 17:42
//     */
//    ProjectDeviceInfoProxyVo getByThirdpartyCode(@Param("thirdpartyCode") String thirdpartyCode);

    /**
     * 通过deviceSn获取完整设备信息
     *
     * @param deviceSn
     * @return
     * @author: 王伟
     * @since： 2020-08-19
     */
    @SqlParser(filter = true)
    ProjectDeviceInfoProxyVo getByDeviceSn(@Param("deviceSn") String deviceSn);

    /**
     * 通过deviceId获取完整设备信息
     *
     * @param deviceId
     * @return
     * @author: 王伟
     * @since： 2020-08-12
     */
    @SqlParser(filter = true)
    ProjectDeviceInfoProxyVo getByDeviceId(@Param("deviceId") String deviceId);

    /**
     * 查询项目下所有设备ProxyVo
     *
     * @param projectId
     * @return
     * @author: 王伟
     * @since： 2020-08-12
     */
    @SqlParser(filter = true)
    List<ProjectDeviceInfoProxyVo> listByProjectId(@Param("projectId") Integer projectId);

    /**
     * 根据设备类型查询该项目下的设备
     *
     * @param projectId
     * @param type
     * @return
     */
    @SqlParser(filter = true)
    List<ProjectDeviceInfo> listByDeviceType(@Param("projectId") Integer projectId, @Param("type") String type);

    /**
     * <p>
     * 根据楼栋ID获取到该楼栋下所有的设备ID-用于移动楼栋区域时使用
     * </p>
     *
     * @param buildingIdList 楼栋ID集合
     */
    List<String> listByBuildingIdList(@Param("buildingIdList") List<String> buildingIdList);

    /**
     * 保存设备信息
     *
     * @param deviceInfo
     * @return
     * @author: 王伟
     * @since：2020-12-23 9:13
     */
    @SqlParser(filter = true)
    Integer saveDeviceProxy(@Param("deviceInfo") ProjectDeviceInfoProxyVo deviceInfo);

    /**
     * 查询支持富文本的设备列表
     *
     * @param projectDeviceInfoFormVo
     * @return
     */
    List<ProjectDeviceInfo> findRichByType(@Param("param") ProjectDeviceInfoFormVo projectDeviceInfoFormVo);

    /**
     * 根据sn查询设备
     *
     * @param sn
     * @return
     */
    @SqlParser(filter = true)
    Integer countBySn(@Param("sn") String sn, @Param("deviceId") String deviceId);

    /**
     * <p>
     * 用来查询是否在别的项目有添加相同的设备
     * </p>
     *
     * @param thirdpartyCode 设备第三方编码
     */
    Integer countThirdPartyCode(String thirdpartyCode);


    /**
     * <p>
     * 根据设备ID获取设备编号前缀
     * </p>
     *
     * @param deviceId 设备ID
     * @return 设备编号楼栋单元房屋编号部分
     * @author: 王良俊
     */
    String getDeviceNoPreByDeviceId(@Param("deviceId") String deviceId);

    /**
     * <p>
     * 分页查询设备参数设备列表
     * </p>
     *
     * @param page  分页对象
     * @param query 查询条件
     * @author: 王良俊
     */
//    Page<ProjectDeviceInfoPageVo> selectParamPage(Page page, @Param("query") ProjectDeviceInfoPageFormVo query);

    /**
     * <p>
     * 获取路灯的设备列表
     * </p>
     *
     * @param projectDeviceInfoFormVo 查询条件
     * @return ProjectIotDeviceInfoVo 带路灯未处理报警名的vo对象
     * @author: 王良俊
     */
    List<ProjectIotDeviceInfoVo> getIotDeviceByType(@Param("param") ProjectDeviceInfoFormVo projectDeviceInfoFormVo);

    /**
     * <p>
     * 获取路灯设备列表
     * </p>
     *
     * @param projectDeviceInfoFormVo 查询条件
     * @return ProjectIotDeviceInfoVo带设备参数的设备信息列表
     * @author: 王良俊
     */
    List<ProjectIotDeviceInfoVo> getStreetLightList(@Param("param") ProjectDeviceInfoFormVo projectDeviceInfoFormVo);

    /**
     * 查询品牌的型号
     *
     * @param deviceTypeId
     * @return
     */
    List<ProjectDeviceProductNameVo> getDeviceBrand(String deviceTypeId);

    /**
     * 查询控制器IMEL
     *
     * @param deviceType
     * @return
     */
    List<ProjectDeviceProductNameVo> getControllerIMEL(String deviceType);

    /**
     * 根据设备id获取设备产品id
     *
     * @return
     * @author: 谢泽毅
     * @since: 2021-08-30
     */
    String getProductIdByDeviceSn(@Param("deviceSn") String deviceSn);

    /**
     * 获取监控设备列表
     *
     * @param deviceId
     * @return
     */
    List<ProjectDeviceInfoPageVo> getMonitoring(@Param("deviceId") String deviceId);

    /**
     * <p>
     * 获取到与这台设备存在参数冲突的设备列表
     * 这里如果使用mybatis自动注入会导致异常所以手动限制项目
     * </p>
     *
     * @param deviceId  以台设备为基础来查询与它存在异常关系的所有设备（包括这台设备）
     * @param projectId 项目ID
     * @return 异常设备列表
     * @author: 王良俊
     */
    @SqlParser(filter = true)
    List<ProjectDeviceInfoAbnormalVo> listAbnormalDevice(@Param("deviceId") String deviceId, @Param("projectId") Integer projectId);

    /**
     * 根据Id查询设备的楼栋和单元
     *
     * @param deviceId
     * @return
     */
    ProjectDeviceBuildingUnitNameVo getBuildingAndUnitByDeviceId(@Param("deviceId") String deviceId);

    /**
     * <p>获取到与这台设备存在参数冲突的设备列表</p>
     *
     * @param
     * @return
     * @author: 王良俊
     */
    List<ProjectDeviceInfoAbnormalVo> listAbnormalDevice(@Param("deviceId") String deviceId);

    /**
     * 查询设备的所属模块
     *
     * @param deviceId
     * @param projectId
     * @return
     */
    String getModule(@Param("deviceId") String deviceId, @Param("projectId") Integer projectId);

    /**
     * 查询异常的设备数
     *
     * @param type
     * @param deviceRegionId
     * @return
     */
    Integer getAbnormalCount(@Param("type") String type, @Param("deviceRegionId") String deviceRegionId);

    ProjectNoticeDevice getDeviceNameAndBuildingNameAndUnitName(@Param("deviceId") String deviceId);

    /**
     * 多条件分页查询
     *
     * @param page
     * @param po
     * @return
     */
    Page<DeviceInfoVo> page(Page page, @Param("query") ProjectDeviceInfo po);

    Page<ProjectParkingDeviceInfoDto> pageDeviceInfo(Page page, @Param("projectId")Integer projectId, @Param("laneId")String laneId, @Param("query") ProjectParkingDeviceInfoDto projectParkingDeviceInfoDto);
}
