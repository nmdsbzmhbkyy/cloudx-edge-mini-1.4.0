

package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.entity.ProjectEntranceEvent;
import com.aurine.cloudx.estate.entity.ProjectLiftEvent;
import com.aurine.cloudx.estate.vo.ProjectAppEventVo;
import com.aurine.cloudx.estate.vo.ProjectEventSearchCondition;
import com.aurine.cloudx.estate.vo.ProjectEventVo;
import com.aurine.cloudx.estate.vo.ProjectLiftEventVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 乘梯事件记录
 *
 * @author pigx code generator
 * @date 2020-05-20 13:27:59
 */
public interface ProjectLiftEventService extends IService<ProjectLiftEvent> {

    IPage<ProjectLiftEventVo> page(Page page, ProjectEventSearchCondition param);
    /**
     * 添加事件
     * @author: 黄健杰
     * @param vo
     * @return
     */
    boolean addEvent(ProjectEventVo vo);

}
