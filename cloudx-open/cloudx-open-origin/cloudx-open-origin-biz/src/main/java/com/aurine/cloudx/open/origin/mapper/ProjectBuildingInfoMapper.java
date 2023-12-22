package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.common.entity.vo.BuildingInfoVo;
import com.aurine.cloudx.open.origin.entity.ProjectBuildingInfo;
import com.aurine.cloudx.open.origin.vo.ProjectBuildingInfoVo;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 楼栋
 *
 * @author 王伟
 * @date 2020-05-07 16:52:22
 */
@Mapper
//@CacheNamespace
public interface ProjectBuildingInfoMapper extends BaseMapper<ProjectBuildingInfo> {


    List<ProjectBuildingInfo> listWithGroup(@Param("name") String name);

    /**
     * 通过框架号获取楼栋对象
     *
     * @param buildingCode
     * @param projectId
     * @return
     */
    @SqlParser(filter = true)
    ProjectBuildingInfoVo getByCode(@Param("buildingCode") String buildingCode, @Param("projectId") Integer projectId);

    @SqlParser(filter = true)
    Integer saveBuilding(@Param("buildingInfo") ProjectBuildingInfoVo buildingInfoVo);

    @SqlParser(filter = true)
    Boolean updateFloorTotal(String buildingId);

    /**
     * 多条件分页查询
     *
     * @param page
     * @param po
     * @return
     */
    Page<BuildingInfoVo> page(Page page, @Param("query") ProjectBuildingInfo po);
}
