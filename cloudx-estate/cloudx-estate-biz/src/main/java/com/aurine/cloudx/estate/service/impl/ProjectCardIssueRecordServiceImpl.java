package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.estate.entity.ProjectCardIssueRecord;
import com.aurine.cloudx.estate.mapper.ProjectCardIssueRecordMapper;
import com.aurine.cloudx.estate.service.ProjectCardIssueRecordService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Author: wrm
 * @Date: 2022/10/19 17:17
 * @Package: com.aurine.cloudx.estate.service.impl
 * @Version: 1.0
 * @Remarks:
 **/
@Service
public class ProjectCardIssueRecordServiceImpl extends ServiceImpl<ProjectCardIssueRecordMapper, ProjectCardIssueRecord> implements ProjectCardIssueRecordService {

    @Override
    public Boolean saveCardIssueRecord(ProjectCardIssueRecord projectCardIssueRecord) {
        return this.save(projectCardIssueRecord);
    }

    @Override
    public Page<ProjectCardIssueRecord> pageCardIssueRecord(Page page, ProjectCardIssueRecord projectCardIssueRecord) {
        return baseMapper.pageCardIssueRecord(page, projectCardIssueRecord);
    }
}
