

package com.aurine.cloudx.open.origin.service;

import com.aurine.cloudx.open.common.entity.vo.PersonEntranceVo;
import com.aurine.cloudx.open.origin.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.open.origin.entity.ProjectEntranceEvent;
import com.aurine.cloudx.open.origin.vo.ProjectAppEventVo;
import com.aurine.cloudx.open.origin.vo.ProjectEventSearchCondition;
import com.aurine.cloudx.open.origin.vo.ProjectEventVo;
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
     *
     * @param vo
     * @return
     * @author: 王伟
     */
    boolean addEvent(ProjectEventVo vo);

    boolean updateByEventStatus(ProjectEventVo vo);

    /**
     * 根据人员类型获取当天的访问数量
     *
     * @param personTypeEnum
     * @return
     */
    Integer countOneDayByPersonType(PersonTypeEnum personTypeEnum);

    /**
     * App相关接口
     *
     * @param page
     * @param personId
     * @return
     */
    IPage<ProjectAppEventVo> getPageByPersonId(Page page, String personId, String date);

    /**
     * 分页查询
     *
     * @param page
     * @param vo
     * @return
     */
    Page<PersonEntranceVo> page(Page page, PersonEntranceVo vo);

}
