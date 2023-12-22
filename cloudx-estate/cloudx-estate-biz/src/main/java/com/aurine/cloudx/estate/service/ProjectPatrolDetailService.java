package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectPatrolDetail;
import com.aurine.cloudx.estate.vo.ProjectPatrolDetaiInfolVo;
import com.aurine.cloudx.estate.vo.ProjectPatrolDetailVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 巡更明细表(ProjectPatrolDetail)表服务接口
 *
 * @author 黄阳光 <huangyg@aurine.cn>
 * @since 2020-09-11 11:57:27
 */
public interface ProjectPatrolDetailService extends IService<ProjectPatrolDetail> {

    /**
     * <p>
     *  根据巡更记录ID获取其巡更明细列表
     * </p>
     *
     * @param patrolId 巡更记录ID
     * @return 巡更明细VO对象列表
     * @author: 王良俊
    */
    ProjectPatrolDetaiInfolVo getDetailListByPatrolId(String patrolId);



}