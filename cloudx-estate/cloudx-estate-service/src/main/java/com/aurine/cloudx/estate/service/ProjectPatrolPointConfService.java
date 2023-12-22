package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectPatrolPointConf;
import com.aurine.cloudx.estate.vo.ProjectPatrolPointConfVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 项目巡更点配置(ProjectPatrolPointConf)表服务接口
 *
 * @author 黄阳光 huangyg@aurine.cn
 * @since 2020-07-28 08:52:45
 */
public interface ProjectPatrolPointConfService extends IService<ProjectPatrolPointConf> {

    IPage<ProjectPatrolPointConfVo> page(Page page, ProjectPatrolPointConfVo vo);

    boolean save(ProjectPatrolPointConfVo vo);

    boolean updateStatusById(String pointId);

}