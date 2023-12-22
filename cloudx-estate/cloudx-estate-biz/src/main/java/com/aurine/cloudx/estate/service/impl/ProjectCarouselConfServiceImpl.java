package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.estate.entity.ProjectCarouselConf;
import com.aurine.cloudx.estate.mapper.ProjectCarouselConfMapper;
import com.aurine.cloudx.estate.service.ProjectCarouselConfService;
import com.aurine.cloudx.estate.vo.ProjectCarouselConfQuery;
import com.aurine.cloudx.estate.vo.ProjectCarouselConfVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 轮播图资讯管理(ProjectCarouseInfoconf)表服务实现类
 *
 * @author 王良俊
 * @since 2021-01-12 11:43:03
 */
@Service
public class ProjectCarouselConfServiceImpl extends ServiceImpl<ProjectCarouselConfMapper, ProjectCarouselConf> implements ProjectCarouselConfService {

    @Override
    public Page<ProjectCarouselConfVo> fetchList(Page<ProjectCarouselConf> page, ProjectCarouselConfQuery carouselConfQuery) {
        return this.baseMapper.fetchList(page, carouselConfQuery);
    }
}