package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.vo.ProjectTrainingFormVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aurine.cloudx.estate.entity.ProjectTrainingFileDb;

import java.util.List;

/**
 * 培训资料库(ProjectTrainingFileDb)表服务接口
 *
 * @author makejava
 * @since 2021-01-12 10:25:55
 */
public interface ProjectTrainingFileDbService extends IService<ProjectTrainingFileDb> {

    /**
     * 查询
     *
     * @param projectTrainingFileDb
     * @return
     */
    List<ProjectTrainingFileDb> selectDir(ProjectTrainingFileDb projectTrainingFileDb);

    /**
     * 分页查询文件
     * @param page
     * @param projectTrainingFileDb
     * @return
     */
    Page<ProjectTrainingFileDb> selectFile(Page page,ProjectTrainingFileDb projectTrainingFileDb);

    /**
     * 删除文件或目录
     *
     * @param id
     * @return
     */
    boolean deleteFile(String id);


}