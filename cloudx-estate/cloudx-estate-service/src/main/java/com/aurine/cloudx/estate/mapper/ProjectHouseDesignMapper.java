

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectHouseDesign;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
}
