package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectCardIssueRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: wrm
 * @Date: 2022/10/19 17:19
 * @Package: com.aurine.cloudx.estate.mapper
 * @Version: 1.0
 * @Remarks:
 **/
@Mapper
public interface ProjectCardIssueRecordMapper extends BaseMapper<ProjectCardIssueRecord> {
    /**
     * 分页查询发卡记录
     * @param page
     * @param projectCardIssueRecord
     * @return
     */
    Page<ProjectCardIssueRecord> pageCardIssueRecord(Page<ProjectCardIssueRecord> page, @Param("query")ProjectCardIssueRecord projectCardIssueRecord);
}
