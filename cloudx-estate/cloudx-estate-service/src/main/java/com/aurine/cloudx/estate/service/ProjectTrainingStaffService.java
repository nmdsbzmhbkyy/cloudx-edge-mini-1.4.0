package com.aurine.cloudx.estate.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aurine.cloudx.estate.entity.ProjectTrainingStaff;

import java.util.List;

/**
 * 培训员工表(ProjectTrainingStaff)表服务接口
 *
 * @author makejava
 * @since 2021-01-13 14:34:29
 */
public interface ProjectTrainingStaffService extends IService<ProjectTrainingStaff> {

    /**
     * 获取参与培训的总人数
     * @param trainingIds
     * @return
     */
    Integer staffTotalCount(List<String> trainingIds);
}