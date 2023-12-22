package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectThirdpartyInfo;
import com.aurine.cloudx.estate.vo.ProjectInfoVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>边缘网关项目信息服务</p>
 * @author : 王良俊
 * @date : 2021-12-02 17:31:32
 */
public interface ProjectThirdpartyInfoService extends IService<ProjectThirdpartyInfo> {

    /**
     * <p>保存或是更新第三方项目信息</p>
     *
     * @param projectInfoVo 项目信息VO对象
     * @param projectId 项目ID
     * @param requestId 申请ID 外键关联
     * @author: 王良俊
     */
    void saveOrUpdateThirdpartyInfo(ProjectInfoVo projectInfoVo, Integer projectId, String requestId);
}