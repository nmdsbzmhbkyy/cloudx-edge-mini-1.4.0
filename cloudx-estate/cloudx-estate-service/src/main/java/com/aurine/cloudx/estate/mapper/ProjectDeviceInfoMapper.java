

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
public interface ProjectDeviceInfoMapper extends BaseMapper<ProjectDeviceInfo> {

    /**
     * 分页查询设备信息(中心机,梯口机,区口机,室内机,编码设备)
     *
     * @param page
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
    ProjectDeviceInfoProxyVo getByDeviceCode(@Param("thirdPartyCode") String thirdPartyCode);
    
    /**
     * 通过thirdPartyCode获取完整设备信息
     *
     * @param thirdPartyCode
     * @return
     * @author: 王伟
     * @since： 2020-12-23 15:50
     */
    @SqlParser(filter = true)
    ProjectDeviceInfoProxyVo getByThirdPartyCode(@Param("thirdPartyCode") String thirdPartyCode);
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
     * @author: 王伟
     * @since：2020-12-23 9:13
     *
     * @param deviceInfo
     * @return
     */
    @SqlParser(filter = true)
    Integer saveDeviceProxy(@Param("deviceInfo") ProjectDeviceInfoProxyVo deviceInfo);

    /**
     * 查询支持富文本的设备列表
     * @param projectDeviceInfoFormVo
     * @return
     */
    List<ProjectDeviceInfo> findRichByType(@Param("param") ProjectDeviceInfoFormVo projectDeviceInfoFormVo);

    /**
     * 根据sn查询设备
     * @param sn
     * @return
     */
    @SqlParser(filter = true)
    Integer countBySn(@Param("sn") String sn,@Param("deviceId")String deviceId);

    /**
     * <p>
     *  用来查询是否在别的项目有添加相同的设备
     * </p>
     *
     * @param thirdPartyCode 设备第三方编码
    */
    @SqlParser(filter = true)
    Integer countThirdPartyCode(String thirdPartyCode);
}
