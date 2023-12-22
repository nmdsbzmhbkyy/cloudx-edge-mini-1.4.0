package com.aurine.cloudx.wjy.service;

import com.aurine.cloudx.wjy.entity.Project;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author ： huangjj
 * @date ： 2021/4/22
 * @description： 项目配置服务
 */
public interface ProjectService extends IService<Project> {
    Project getByPid(String pid);
    Project getByProjectId(Integer projectId);
    List<Project> getList();
    void initProjectToWjy(Integer projectId);
}
