package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectCardIssueRecord;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Author: wrm
 * @Date: 2022/10/19 17:15
 * @Package: com.aurine.cloudx.estate.service
 * @Version: 1.0
 * @Remarks:
 **/
public interface ProjectCardIssueRecordService extends IService<ProjectCardIssueRecord> {

    /**
     * 保存发卡记录
     * @param projectCardIssueRecord
     * @return
     */
    Boolean saveCardIssueRecord(ProjectCardIssueRecord projectCardIssueRecord);

    /**
     * 分页查询发卡记录
     * @param page
     * @param projectCardIssueRecord
     * @return
     */
    Page<ProjectCardIssueRecord> pageCardIssueRecord(Page page, ProjectCardIssueRecord projectCardIssueRecord);
}
