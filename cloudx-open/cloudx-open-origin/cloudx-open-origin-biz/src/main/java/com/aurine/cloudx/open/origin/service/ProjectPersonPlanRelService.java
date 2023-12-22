

package com.aurine.cloudx.open.origin.service;

import com.aurine.cloudx.open.common.entity.vo.PersonPlanRelVo;
import com.aurine.cloudx.open.origin.entity.ProjectPersonPlanRel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 人员方案关系管理
 * 同时也存储人员的可通行时间、是否停用等信息
 * </p>
 *
 * @ClassName: ProjectPersonPlanRelService
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/22 10:02
 * @Copyright:
 */
public interface ProjectPersonPlanRelService extends IService<ProjectPersonPlanRel> {


    /**
     * 根据personId，保存或更新方案
     *
     * @param projectPersonPlanRel
     * @return
     */
    boolean saveOrUpdateByPersonId(ProjectPersonPlanRel projectPersonPlanRel);

    /**
     * 验证所选通行方案是否有变更
     *
     * @param personId
     * @param planId
     * @return
     */
    boolean checkChange(String personId, String planId);

    /**
     * 根据personId获取该人员使用的方案
     *
     * @param personId
     * @return
     */
    String getPlanIdByPersonId(String personId);

    /**
     * 返回当前项目过期的人员
     *
     * @return
     */
    List<ProjectPersonPlanRel> listPersonPlanRelExpDate();

    /**
     * 根据personId获取该人员使用的方案
     *
     * @param personId
     * @return
     */
    ProjectPersonPlanRel getPoByPersonId(String personId);

    /**
     * 根据PersonId删除人员方案关系
     *
     * @param personId
     * @return
     */
    boolean deleteByPersonId(String personId);

    /**
     * 根据方案ID,查询到使用方案的用户
     *
     * @param planId
     * @return
     */
    List<ProjectPersonPlanRel> listByPlanId(String planId);

    /**
     * 核对方案是否已经被使用
     *
     * @param planId
     * @return
     */
    boolean checkUsed(String planId);

    /**
     * 启用person通行
     *
     * @param personId
     * @return
     */
    boolean enablePerson(String personId);


    /**
     * 禁用person通行
     *
     * @param personId
     * @return
     */
    boolean disablePerson(String personId);

    /**
     * <p>
     * 生成今天要进行过期处理的任务
     * </p>
     */
    void initTodayTask();

    /**
     * <p>
     * 处理这个时间之前的所有需要删除的通行权限
     * </p>
     *
     * @param timeStr 2020-01-02 10:11
     * @author: 王良俊
     */
    void handleAllTimeOutRight(String timeStr);

    /**
     * 分页查询
     *
     * @param page
     * @param vo
     * @return
     */
    Page<PersonPlanRelVo> page(Page page, PersonPlanRelVo vo);

}
