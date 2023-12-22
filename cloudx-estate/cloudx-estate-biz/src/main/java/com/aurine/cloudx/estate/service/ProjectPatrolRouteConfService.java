package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectPatrolRouteConf;
import com.aurine.cloudx.estate.vo.ProjectPatrolRouteConfVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 项目巡更路线设置(ProjectPatrolRouteConf)表服务接口
 *
 * @author 黄阳光 huangyg@aurine.cn
 * @since 2020-07-24 12:00:07
 */
public interface ProjectPatrolRouteConfService extends IService<ProjectPatrolRouteConf> {

    IPage<ProjectPatrolRouteConfVo> page(Page page, ProjectPatrolRouteConfVo vo);

    boolean save(ProjectPatrolRouteConfVo vo);

    boolean updatePatrolRouteConfById(ProjectPatrolRouteConfVo vo);

    boolean updateStatusById(String patrolRouteId);

    ProjectPatrolRouteConfVo getVoById(String patrolRouteId);

    List<String> getPersonNameList(String patrolRouteId);

}