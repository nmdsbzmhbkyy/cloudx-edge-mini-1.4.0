package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.origin.entity.ProjectConfig;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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

    /**
     * 根据项目id查询
     * @param projectId
     */
    @SqlParser(filter = true)
    ProjectConfig getByProjectId(@Param("projectId") Integer projectId);

    /**
     * 更新项目增值服务到期时间
     * @param projectId
     */
    @SqlParser(filter = true)
    void updateServiceExpTime(@Param("projectId") Integer projectId, @Param("serviceExpTime") String serviceExpTime);

    /**
     * 更新阿里对接设置
     * @param projectId
     * @param aliProjectCode
     */
    @SqlParser(filter = true)
    void updateAliProjectCode(@Param("projectId") Integer projectId, @Param("aliProjectCode") String aliProjectCode);

    /**
     * 修改视频接入设置
     * @param projectId
     * @param totalMonitorDevNo
     */
    @SqlParser(filter = true)
    void updateTotalMonitorDevNo(@Param("projectId") Integer projectId, @Param("totalMonitorDevNo") Integer totalMonitorDevNo);

    /**
     * 更新open配置
     * @param projectId
     * @param clientId
     * @param clientSecret
     */
    @SqlParser(filter = true)
    void updateOpen(@Param("projectId") Integer projectId, @Param("clientId") String clientId, @Param("clientSecret") String clientSecret);
}
