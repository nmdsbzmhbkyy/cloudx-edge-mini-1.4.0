package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectPatrolDetail;
import com.aurine.cloudx.estate.entity.ProjectPatrolPersonPoint;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDate;
import java.util.List;

/**
 * 人员巡更巡点记录(ProjectPatrolPersonPoint)表服务接口
 *
 * @author 王良俊
 * @since 2020-09-17 10:29:22
 */
public interface ProjectPatrolPersonPointService extends IService<ProjectPatrolPersonPoint> {

    /**
     * <p>
     * 保存人员和巡更明细的关系
     * </p>
     *
     * @param patrolId     巡更记录ID
     * @param patrolPersonIdList 人员分配ID
     * @return 是否保存成功
     */
    boolean saveRelationship(String patrolId, List<String> patrolPersonIdList);

    /**
     * <p>
     * 保存人员和巡更明细的关系
     * </p>
     *
     * @param patrolId     巡更记录ID
     * @param patrolPersonIdList 人员分配ID
     * @return 是否保存成功
     */
    boolean saveRelationship(String patrolId, List<String> patrolPersonIdList,List<ProjectPatrolDetail> detailList);

    /**
     * <p>
     *  批量删除人员和巡更明细的关系
     * </p>
     *
     * @param patrolId 巡更记录ID
     * @param patrolPersonIdList 人员分配ID列表
     * @return 是否删除成功
     * @author: 王良俊
    */
    boolean removeBatch(String patrolId, List<String> patrolPersonIdList);

    /**
     * 根据巡更任务ID，清空当前任务的人员配置详情
     * @param patrolId
     * @return
     */
    boolean removeByPatrolId(String patrolId);

    /**
     * 巡更点签到接口
     * @param projectPatrolPersonPoint
     */
    void sign(ProjectPatrolPersonPoint projectPatrolPersonPoint);

    /**
     * <p>
     *  根据传入的状态集合和员工ID获取到这些状态的数量
     * </p>
     *
     * @param date 所要查询的日期 （指定月份 如 2020-07）
     * @param staffId 所要查询的日期
     * @param status 所要查询的状态集合
    */
    Integer countStatusByStaffId(String staffId, List<String> status, LocalDate date);


    /**
     * <p>
     * 获取到已完成的数量 根据员工ID
     * </p>
     *
     * @param staffId 要统计对应状态数量的员工ID
     * @author: 王良俊
     */
    Integer countCompleteByStaffId(String staffId, LocalDate date);


    /**
     * <p>
     * 获取到未完成的数量 根据员工ID
     * </p>
     *
     * @param staffId 要统计对应状态数量的员工ID
     * @author: 王良俊
     */
    Integer countUnCompleteByStaffId(String staffId, LocalDate date);


    /**
     * <p>
     * 获取到包含已完成和未完成的数量 根据员工ID
     * </p>
     *
     * @param staffId 要统计对应状态数量的员工ID
     * @author: 王良俊
     */
    Integer countAllByStaffId(String staffId, LocalDate date);

}