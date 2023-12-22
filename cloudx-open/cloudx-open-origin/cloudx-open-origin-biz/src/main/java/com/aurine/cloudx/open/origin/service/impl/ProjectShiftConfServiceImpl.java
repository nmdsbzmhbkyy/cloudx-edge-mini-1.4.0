package com.aurine.cloudx.open.origin.service.impl;

import com.aurine.cloudx.open.origin.mapper.ProjectShiftConfMapper;
import com.aurine.cloudx.open.origin.entity.ProjectShiftConf;
import com.aurine.cloudx.open.origin.vo.ProjectShiftConfPageVo;
import com.aurine.cloudx.open.origin.service.ProjectShiftConfService;
import com.aurine.cloudx.open.origin.service.ProjectShiftPlanService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 班次配置
 *
 * @author guhl@aurine.cn
 * @date 2020-07-23 08:36:54
 */
@Service
@AllArgsConstructor
public class ProjectShiftConfServiceImpl extends ServiceImpl<ProjectShiftConfMapper, ProjectShiftConf> implements ProjectShiftConfService {

    private final ProjectShiftPlanService projectShiftPlanService;

    @Override
    public Page<ProjectShiftConfPageVo> pageShiftConf(Page<ProjectShiftConfPageVo> page, ProjectShiftConfPageVo projectShiftConfPageVo) {
        return baseMapper.pageShiftConf(page, projectShiftConfPageVo);
    }

    @Override
    public ProjectShiftConf getByShiftName(String shiftName, String shiftId) {
        if (StringUtils.isEmpty(shiftId)){
            return super.getOne(Wrappers.lambdaQuery(ProjectShiftConf.class).eq(ProjectShiftConf::getShiftName, shiftName));
        }
        return super.getOne(Wrappers.lambdaQuery(ProjectShiftConf.class)
                .eq(ProjectShiftConf::getShiftName, shiftName)
                .ne(ProjectShiftConf::getShiftId, shiftId));
    }

    @Override
    public Boolean updateProjectShiftConf(ProjectShiftConf projectShiftConf) {
        List<ProjectShiftConf> list = super.list(Wrappers.lambdaQuery(ProjectShiftConf.class).eq(ProjectShiftConf::getShiftName, projectShiftConf.getShiftName()))
                .stream().filter(e -> !projectShiftConf.getShiftId().equals(e.getShiftId()))
                .collect(Collectors.toList());
        if (list.size() > 0) {
            throw new RuntimeException("班次名称已存在");
        }
        return updateById(projectShiftConf);
    }

    @Override
    public Boolean removeByShiftId(String shiftId) {
/*        Integer count = projectShiftPlanService.count(Wrappers.lambdaQuery(ProjectShiftPlan.class).eq(ProjectShiftPlan::getShiftId, shiftId));
        if (count != 0) {
            throw new RuntimeException("该班次已被使用，不可删除");
        }*/
        return removeById(shiftId);
    }
}
