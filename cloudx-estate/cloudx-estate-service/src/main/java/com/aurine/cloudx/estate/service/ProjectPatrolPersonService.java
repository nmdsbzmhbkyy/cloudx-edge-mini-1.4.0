package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectPatrolDetail;
import com.aurine.cloudx.estate.entity.ProjectPatrolPerson;
import com.aurine.cloudx.estate.entity.ProjectPatrolPersonPoint;
import com.aurine.cloudx.estate.entity.ProjectStaff;
import com.aurine.cloudx.estate.vo.ProjectPatrolDetailVo;
import com.aurine.cloudx.estate.vo.ProjectPatrolPersonPointVo;
import com.aurine.cloudx.estate.vo.ProjectPatrolPersonVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 巡更人员分配表(ProjectPatrolPerson)表服务接口
 *
 * @author 黄阳光 <huangyg@aurine.cn>
 * @since 2020-09-11 16:04:22
 */
public interface ProjectPatrolPersonService extends IService<ProjectPatrolPerson> {

    /**
     * <p>
     * 指派人员（指派执行人）
     * </p>
     *
     * @param patrolId  巡更记录ID
     * @param staffList 所指派的人员对象列表
     * @return 是否指派成功
     * @author: 王良俊
     */
    boolean assignPerson(String patrolId, List<ProjectStaff> staffList);
    /**
     * <p>
     * 指派人员（指派执行人）
     * </p>
     *
     * @param patrolId  巡更记录ID
     * @param staffList 所指派的人员对象列表
     * @return 是否指派成功
     * @author: 王良俊
     */
    boolean assignPerson(String patrolId, List<ProjectStaff> staffList,List<ProjectPatrolDetail> detailList);

    /**
     * 增加一个指派人
     * @param patrolId
     * @param staff
     * @author: 王伟
     * @since: 2020-10-29 11:16
     * @return
     */
    boolean assignPerson(String patrolId, ProjectStaff staff);

    /**
     * <p>
     * 批量删除人员
     * </p>
     *
     * @param patrolId    巡更记录id
     * @param staffIdList 员工ID列表
     * @return 是否删除成功
     * @author: 王良俊
     */
    boolean removeBatch(String patrolId, List<String> staffIdList);

    /**
     * <p>
     * 根据巡更明细ID查询巡更人员分配列表
     * </p>
     * <p>
     *
     * @param detailId 巡更明细ID
     * @return 巡更人员分配列表
     * @author: 王良俊
     */
    List<ProjectPatrolPersonVo> listByDetailId(String detailId);

    /**
     * <p>
     * 根据详情ID获取该详情未巡的数量
     * </p>
     *
     * @param patrolId 巡更详情ID
     * @return 未巡数
     * @author: 王良俊
     */
    Integer countUnpatrolByPatrolId(String patrolId);

    /**
     * <p>
     * 根据详情ID获取该详情已巡的数量
     * </p>
     *
     * @param patrolId 巡更详情ID
     * @return 未巡数
     * @author: 王良俊
     */
    Integer countPatrolByPatrolId(String patrolId);

    /**
     * <p>
     * 根据详情ID获取该详情的超时数量
     * </p>
     *
     * @param patrolId 巡更详情ID
     * @return 未巡数
     * @author: 王良俊
     */
    Integer countTimeOutByPatrolId(String patrolId, String checkInStatus);

    /**
     * <p>
     * 根据详情ID获取该详情的巡更结果为正常或异常的数量
     * </p>
     *
     * @param patrolId 巡更详情ID
     * @return 未巡数
     * @author: 王良俊
     */
    Integer countNormalByPatrolId(String patrolId, String patrolResult);

    /**
     * 根据任务id，获取当前任务的计划内人员（参与人）
     *
     * @param patrolId 任务ID
     * @return
     * @author: 王伟
     * @since: 2020-10-29 8:52
     */
    List<ProjectPatrolPerson> listStaffInPlan(String patrolId);

    /**
     * 根据任务id，获取当前任务的被指派人
     *
     * @param patrolId
     * @return
     * @author: 王伟
     * @since: 2020-10-29 8:52
     */
    List<ProjectPatrolPerson> listStaffOnJob(String patrolId);

    /**
     * 任务生成时，保存参与人，将可执行人（计划人员）覆盖存储到快任务照数据中
     * @param patrolId
     * @param staffList
     * @return
     * @author: 王伟
     * @since: 2020-10-29 8:52
     */
    boolean saveStaffInPlan(String patrolId, List<ProjectStaff> staffList);

    /**
     * 根据任务id获取签到明细
     * @param patrolId
     * @return
     */
    List<ProjectPatrolPersonPointVo> getDetailByPatrolId(String patrolId);


}