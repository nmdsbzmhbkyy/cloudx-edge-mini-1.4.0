package com.aurine.cloudx.open.origin.service;

import com.aurine.cloudx.open.origin.entity.ProjectLiftEvent;
import com.aurine.cloudx.open.origin.vo.ProjectEventSearchCondition;
import com.aurine.cloudx.open.origin.vo.ProjectLiftEventVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 乘梯事件记录
 *
 *  @author zy
 *  @date 2022-07-15 10:49:26
 */
public interface ProjectLiftEventService extends IService<ProjectLiftEvent> {

    IPage<ProjectLiftEventVo> page(Page page, ProjectEventSearchCondition param);
}
