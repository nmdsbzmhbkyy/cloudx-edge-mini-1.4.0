
package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectDeviceRegion;
import com.aurine.cloudx.estate.vo.ProjectBuildingRegionInfoVo;
import com.aurine.cloudx.estate.vo.ProjectDeviceRegionVo;
import com.aurine.cloudx.estate.vo.ProjectRegionManagerVo;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备区域表
 *
 * @author xull@aurine.cn
 * @date 2020-05-25 08:50:24
 */
@Mapper
public interface ProjectDeviceRegionMapper extends BaseMapper<ProjectDeviceRegion> {

    List<ProjectDeviceRegionVo> findByDeviceType( String type);

    @SqlParser(filter=true)
    ProjectDeviceRegionVo selectByDefault(Integer projectId);

    @SqlParser(filter=true)
    ProjectDeviceRegionVo selectByTemplate();

    @SqlParser(filter=true)
    boolean initInsert(@Param("param") ProjectDeviceRegionVo vo, @Param("projectId") Integer projectId, @Param("tenantId") Integer tenantId);

    /**
     * <p>
     *  获取楼栋区域信息的分页数据
     * </p>
     * @author: 王良俊
    */
    Page<ProjectBuildingRegionInfoVo> pageBuildingRegionInfo(Page page);

    /**
     * <p>
     *  获取楼栋区域信息的分页数据(带组团)
     * </p>
     * @author: 王良俊
    */
    Page<ProjectBuildingRegionInfoVo> pageBuildingRegionInfoGroup(Page page);

    /**
     * <p>
     *  获取楼栋区域信息的分页数据(带组团)
     * </p>
     * @author: 王良俊
    */
    Page<ProjectRegionManagerVo> pageRegionManager(Page page);

    /**
     * <p>
     *  根据区域ID获取区域下的楼栋
     * </p>
     *
     * @param regionId 区域ID
     * @param isGroup 是否开启组团
     * @author: 王良俊
    */
    List<String> listBuildingByRegionId(@Param("regionId") String regionId, @Param("isGroup") boolean isGroup, @Param("isPublicRegion") boolean isPublicRegion);

    /**
     * <p>
     *  获取到这个区域的所有子孙区域ID（包括自己）
     * </p>
     *
     * @param regionId 所要查询的父区域ID
     * @author: 王良俊
    */
    String getChildRegionIdList(@Param("regionId") String regionId);


    /**
     * <p>
     *  根据区域ID获取到区口设备名列表
     * </p>
     *
     * @param regionId 区域ID
     * @author:  王良俊
    */
    List<String> listRegionDeviceByRegionId(@Param("isPublicRegion") boolean isPublicRegion, @Param("regionId") String regionId);
}
