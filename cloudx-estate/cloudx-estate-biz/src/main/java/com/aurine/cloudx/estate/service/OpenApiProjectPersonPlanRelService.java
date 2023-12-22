package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectPersonPlanRel;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Author: wrm
 * @Date: 2022/06/28 11:11
 * @Package: com.aurine.cloudx.estate.service.impl
 * @Version: 1.0
 * @Remarks:
 **/
public interface OpenApiProjectPersonPlanRelService extends IService<ProjectPersonPlanRel> {
    /**
     * 保存或更新用户通行方案
     *
     * @param projectPersonPlanRel
     * @return
     */
    boolean saveOrUpdatePersonPlan(ProjectPersonPlanRel projectPersonPlanRel);



}
