package com.aurine.cloudx.open.origin.service;

import com.aurine.cloudx.open.origin.entity.EdgeCascadeConf;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>项目入云连接码服务</p>
 * @author : 王良俊
 * @date : 2021-12-10 15:40:10
 */
public interface EdgeCascadeConfService extends IService<EdgeCascadeConf> {

    /**
     * <p>根据项目ID生成项目入云连接配置</p>
     *
     * @param projectId 项目ID
     * @return 是否生成成功
     */
    String genCloudConnectCode(Integer projectId);

    /**
     * <p>获取当前项目入云连接码</p>
     *
     * @param projectId 项目ID
     * @return 入云连接码
     */
    String getCloudConnectCode(Integer projectId);

    /**
     * <p>获取入云连接码对应的项目ID</p>
     *
     * @param connectCode 入云连接码
     * @return 入云连接码对应的项目ID
     */
    Integer getProjectId(String connectCode);

    /**
     * <p>删除项目入云连接码信息</p>
     *
     * @param projectId 项目ID
     */
    void removeByProjectId(Integer projectId);

}