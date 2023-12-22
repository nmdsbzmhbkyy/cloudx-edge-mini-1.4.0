

package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.origin.entity.ProjectHouseDesign;
import com.aurine.cloudx.open.common.entity.vo.HouseDesignVo;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 项目户型配置表
 *
 * @author pigx code generator
 * @date 2020-05-06 15:22:42
 */
@Mapper
public interface ProjectHouseDesignMapper extends BaseMapper<ProjectHouseDesign> {

    /**
     * 获取首个户型
     *
     * @param projectId
     * @return
     */
    @SqlParser(filter = true)
    ProjectHouseDesign getTopOne(@Param("projectId") Integer projectId);

    /**
     * 多条件分页查询
     *
     * @param page
     * @param po
     * @return
     */
    Page<HouseDesignVo> page(Page page, @Param("query") ProjectHouseDesign po);
}
