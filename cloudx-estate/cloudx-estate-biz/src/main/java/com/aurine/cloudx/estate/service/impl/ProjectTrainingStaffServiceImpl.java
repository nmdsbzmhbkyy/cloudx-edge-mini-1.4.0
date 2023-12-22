package com.aurine.cloudx.estate.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aurine.cloudx.estate.mapper.ProjectTrainingStaffMapper;
import com.aurine.cloudx.estate.entity.ProjectTrainingStaff;
import com.aurine.cloudx.estate.service.ProjectTrainingStaffService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 培训员工表(ProjectTrainingStaff)表服务实现类
 *
 * @author makejava
 * @since 2021-01-13 14:34:29
 */
@Service
public class ProjectTrainingStaffServiceImpl extends ServiceImpl<ProjectTrainingStaffMapper, ProjectTrainingStaff> implements ProjectTrainingStaffService {

    @Override
    public Integer staffTotalCount(List<String> trainingIds) {
        return baseMapper.staffTotalCount(trainingIds);
    }
}