package com.aurine.cloudx.estate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aurine.cloudx.estate.entity.ProjectConfig;

import java.util.List;

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
}
