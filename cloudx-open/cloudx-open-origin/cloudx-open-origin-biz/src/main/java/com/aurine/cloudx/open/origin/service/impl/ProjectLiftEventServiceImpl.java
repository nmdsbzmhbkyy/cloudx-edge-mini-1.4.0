package com.aurine.cloudx.open.origin.service.impl;

import com.aurine.cloudx.open.origin.entity.ProjectLiftEvent;
import com.aurine.cloudx.open.origin.mapper.ProjectLiftEventMapper;
import com.aurine.cloudx.open.origin.service.ProjectLiftEventService;
import com.aurine.cloudx.open.origin.vo.ProjectEventSearchCondition;
import com.aurine.cloudx.open.origin.vo.ProjectLiftEventVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


/**
 * 乘梯事件记录
 *
 * @author zy
 * @date 2022-07-15 10:49:26
 */
@Service
public class ProjectLiftEventServiceImpl extends ServiceImpl<ProjectLiftEventMapper, ProjectLiftEvent> implements ProjectLiftEventService {


    @Override
    public IPage<ProjectLiftEventVo> page(Page page, ProjectEventSearchCondition param) {
        return baseMapper.select(page, param);
    }


}
