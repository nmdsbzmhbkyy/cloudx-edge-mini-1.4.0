package com.aurine.cloudx.open.origin.service;

import com.aurine.cloudx.open.origin.entity.ProjectConfig;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 项目参数设置
 *
 * @author guhl.@aurine.cn
 * @date 2020-07-10 10:06:39
 */
public interface ProjectConfigService extends IService<ProjectConfig> {

    /**
     * 获取项目配置参数
     *
     * @return
     */
    ProjectConfig getConfig();

    /**
     * 初始化项目配置信息
     * @param projectId
     * @param tenantId
     */
    void initDefaultData(Integer projectId, Integer tenantId);

    /**
     * 根据项目id查询项目配置
     * @param projectId
     * @return
     */
    ProjectConfig getByProjectId(Integer projectId);

    /**
     * 更新项目增值服务到期时间
     * @param projectId
     * @param serviceExpTime
     */
    void updateServiceExpTime(Integer projectId, String serviceExpTime);

    /**
     * 是否开启一车多位
     * @param projectId
     */
    boolean isEnableMultiCarsPerPlace(Integer projectId);

    /**
     * 更新阿里对接设置
     * @param projectId
     * @param aliProjectCode
     */
    void updateAliProjectCode(Integer projectId, String aliProjectCode);

    /**
     * 修改视频接入设置
     * @param projectId
     * @param totalMonitorDevNo
     */
    void updateTotalMonitorDevNo(Integer projectId, Integer totalMonitorDevNo);

    /**
     * 更新开放平台配置
     * @param projectConfig
     */
    void updateOpen(ProjectConfig projectConfig);

    /**
     * 车辆信息
     *
     * @author 王伟
     * @date 2020-07-08 14:33:58
     */
}

