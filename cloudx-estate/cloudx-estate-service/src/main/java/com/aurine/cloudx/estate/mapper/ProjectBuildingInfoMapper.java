

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectBuildingInfo;
import com.aurine.cloudx.estate.vo.ProjectBuildingInfoVo;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
}
