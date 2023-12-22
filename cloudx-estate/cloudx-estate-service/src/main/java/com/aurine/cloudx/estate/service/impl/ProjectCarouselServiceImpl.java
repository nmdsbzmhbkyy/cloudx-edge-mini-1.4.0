package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.aurine.cloudx.estate.entity.ProjectCarousel;
import com.aurine.cloudx.estate.mapper.ProjectCarouselMapper;
import com.aurine.cloudx.estate.service.ProjectCarouselService;
import com.aurine.cloudx.estate.vo.ProjectCarouselVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 轮播图管理
 *
 * @author DW
 * @version 1.0.0
 * @date 2021/1/12 15:38
 */
@Service
@AllArgsConstructor
public class ProjectCarouselServiceImpl extends ServiceImpl<ProjectCarouselMapper, ProjectCarousel>
        implements ProjectCarouselService {

    @Override
    public Page<ProjectCarouselVo> pageVo(Page page, ProjectCarouselVo projectCarouselVo) {
        return this.baseMapper.pageVo(page, projectCarouselVo);
    }

    @Override
    public List<ProjectCarouselVo> listVo(String type) {
        List<ProjectCarouselVo> list = this.baseMapper.listVo(type);
        List<ProjectCarouselVo> newList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            boolean hasVal = false;
            for (ProjectCarouselVo projectCarouselVo : list) {
                if (projectCarouselVo.getSort().equals(i)) {
                    newList.add(projectCarouselVo);
                    hasVal = true;
                    break;
                }
            }
            if (!hasVal) {
                ProjectCarouselVo projectCarouselVo = new ProjectCarouselVo();
                projectCarouselVo.setType(type);
                projectCarouselVo.setSort(i);
                newList.add(projectCarouselVo);
            }
        }
        return newList;
    }

    @Override
    public List<ProjectCarouselVo> listByType(String type) {
        List<ProjectCarouselVo> list = this.baseMapper.listVo(type);
        List<ProjectCarouselVo> newList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            boolean hasVal = false;
            for (ProjectCarouselVo projectCarouselVo : list) {
                if (projectCarouselVo.getSort().equals(i)) {
                    newList.add(projectCarouselVo);
                    hasVal = true;
                    break;
                }
            }
        }
        return newList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void upObj(String id) {
        ProjectCarousel projectCarousel = getById(id);
        ProjectCarousel newProjectCarousel = this.getOne(Wrappers.lambdaQuery(ProjectCarousel.class)
                .eq(ProjectCarousel::getSort, projectCarousel.getSort() + 1)
                .eq(ProjectCarousel::getType, projectCarousel.getType()));
        if (ObjectUtil.isEmpty(newProjectCarousel)) {
            projectCarousel.setSort(projectCarousel.getSort() + 1);
            updateById(projectCarousel);
        } else {
            newProjectCarousel.setSort(projectCarousel.getSort());
            projectCarousel.setSort(projectCarousel.getSort() + 1);
            updateById(projectCarousel);
            updateById(newProjectCarousel);
        }
    }

    @Override
    public void downObj(String id) {
        ProjectCarousel projectCarousel = getById(id);
        ProjectCarousel newProjectCarousel = this.getOne(Wrappers.lambdaQuery(ProjectCarousel.class)
                .eq(ProjectCarousel::getSort, projectCarousel.getSort() - 1)
                .eq(ProjectCarousel::getType, projectCarousel.getType()));
        if (ObjectUtil.isEmpty(newProjectCarousel)) {
            projectCarousel.setSort(projectCarousel.getSort() - 1);
            updateById(projectCarousel);
        } else {
            newProjectCarousel.setSort(projectCarousel.getSort());
            projectCarousel.setSort(projectCarousel.getSort() - 1);
            updateById(projectCarousel);
            updateById(newProjectCarousel);
        }
    }

}
