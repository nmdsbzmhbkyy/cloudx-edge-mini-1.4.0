

package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.entity.ProjectEntranceEvent;
import com.aurine.cloudx.estate.vo.ProjectAppEventVo;
import com.aurine.cloudx.estate.vo.ProjectEventSearchCondition;
import com.aurine.cloudx.estate.vo.ProjectEventVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 通行事件记录
 *
 * @author pigx code generator
 * @date 2020-05-20 13:27:59
 */
public interface ProjectEntranceEventService extends IService<ProjectEntranceEvent> {

    IPage<ProjectEventVo> page(Page page, ProjectEventSearchCondition param);

    List<ProjectEventVo> getEventTypeNum(Integer projectId, Integer tenantId);

    boolean add(ProjectEventVo vo);

    /**
     * 添加事件
     * @author: 王伟
     * @param vo
     * @return
     */
    boolean addEvent(ProjectEventVo vo);

    boolean updateByEventStatus(ProjectEventVo vo);

    /**
     * 根据人员类型获取当天的访问数量
     * @param personTypeEnum
     * @return
     */
    Integer countOneDayByPersonType(PersonTypeEnum personTypeEnum);

    /**
     * App相关接口
     * @param page
     * @param personId
     * @return
     */
    IPage<ProjectAppEventVo> getPageByPersonId(Page page, String personId, String date);
}
