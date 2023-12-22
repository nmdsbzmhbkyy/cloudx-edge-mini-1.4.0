package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.common.entity.vo.UnitInfoVo;
import com.aurine.cloudx.open.origin.entity.ProjectUnitInfo;
import com.aurine.cloudx.open.origin.vo.ProjectUnitInfoCountVo;
import com.aurine.cloudx.open.origin.vo.ProjectUnitInfoVo;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 单元
 *
 * @author 王伟
 * @date 2020-06-10 11:10:40
 */
@Mapper
public interface ProjectUnitInfoMapper extends BaseMapper<ProjectUnitInfo> {

    /**
     * 通过框架号获取楼栋对象
     *
     * @param unitCode
     * @param projectId
     * @return
     */
    @SqlParser(filter = true)
    ProjectUnitInfoVo getByCode(@Param("unitCode") String unitCode, @Param("projectId") Integer projectId);

    @SqlParser(filter = true)
    Integer saveUnit(@Param("unitInfo") ProjectUnitInfoVo unitInfo);

    List<ProjectUnitInfoCountVo> listUnitInfo(@Param("buildingId") String buildingId);

    /**
     * 多条件分页查询
     *
     * @param page
     * @param po
     * @return
     */
    Page<UnitInfoVo> page(Page page, @Param("query") ProjectUnitInfo po);
}
