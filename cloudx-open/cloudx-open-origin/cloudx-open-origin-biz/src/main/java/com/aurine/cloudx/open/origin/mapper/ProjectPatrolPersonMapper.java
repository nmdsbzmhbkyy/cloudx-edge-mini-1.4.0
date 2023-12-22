package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.origin.entity.ProjectPatrolPerson;
import com.aurine.cloudx.open.origin.vo.ProjectPatrolPersonPointVo;
import com.aurine.cloudx.open.origin.vo.ProjectPatrolPersonVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 项目巡更人员分配(ProjectPatrolInfo)表数据库访问层
 *
 * @author 黄阳光 <huangyg@aurine.cn>
 * @since 2020-09-09 09:01:23
 */
@Mapper
public interface ProjectPatrolPersonMapper extends BaseMapper<ProjectPatrolPerson> {

    /**
     * <p>
     *  根据巡更明细ID查询巡更人员分配列表
     * </p>
     *
     * @param partrolDetailId 巡更明细ID
     * @return 巡更人员分配列表
     * @author: 王良俊
     */
    List<ProjectPatrolPersonVo> listByDetailId(String partrolDetailId);

    /**
     * <p>
     *  根据详情ID获取该详情已巡或未巡的数量
     * </p>
     *
     * @param patrolId 巡更详情ID
     * @param isUnPatrol 是否是未巡
     * @return 未巡数
     * @author: 王良俊
     */
    Integer countPatrolBypatrolId(@Param("patrolId") String patrolId, @Param("isUnPatrol") boolean isUnPatrol);

    /**
     * <p>
     *  根据详情ID获取超时或未超时数量
     * </p>
     *
     * @param patrolId 巡更详情ID
     * @return 未巡数
     * @author: 王良俊
     */
    Integer countTimeOutByPatrolId(@Param("patrolId") String patrolId, @Param("checkInStatus") String checkInStatus);

    /**
     * <p>
     *  根据详情ID获取正常或异常数量
     * </p>
     *
     * @param patrolId 巡更详情ID
     * @return 未巡数
     * @author: 王良俊
     */
    Integer countNormalByPatrolId(@Param("patrolId") String patrolId, @Param("patrolResult") String patrolResult);

    /**
     * 根据任务id获取签到明细
     * @param patrolId
     * @return
     */
    List<ProjectPatrolPersonPointVo> getDetailByPatrolId(@Param("patrolId") String patrolId);
}