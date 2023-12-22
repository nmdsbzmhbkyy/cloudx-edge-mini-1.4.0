package com.aurine.cloudx.estate.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aurine.cloudx.estate.entity.ProjectConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 项目参数设置
 *
 * @author guhl.@aurine.cn
 * @date 2020-07-10 10:06:39
 */
@Mapper
public interface ProjectConfigMapper extends BaseMapper<ProjectConfig> {

    /**
     * 初始化项目参数设置
     * @param projectConfig
     */
    @SqlParser(filter = true)
    void init(@Param("entity") ProjectConfig projectConfig);
}
