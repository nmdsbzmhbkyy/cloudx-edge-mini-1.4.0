package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectPatrolInfo;
import com.aurine.cloudx.estate.vo.ProjectPatrolInfoOnDetailVo;
import com.aurine.cloudx.estate.vo.ProjectPatrolInfoSearchCondition;
import com.aurine.cloudx.estate.vo.ProjectPatrolInfoVo;
import com.aurine.cloudx.estate.vo.ProjectStaffWorkVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 项目巡更记录(ProjectPatrolInfo)表服务接口
 *
 * @author 黄阳光 <huangyg@aurine.cn>
 * @since 2020-09-09 08:56:18
 */
public interface ProjectPatrolInfoService extends IService<ProjectPatrolInfo> {

    ProjectStaffWorkVo getCount(String staffId, String date);

    IPage<ProjectPatrolInfoVo> pageByPatrolInfo(Page<ProjectPatrolInfoVo> page, ProjectPatrolInfoSearchCondition vo);

    ProjectPatrolInfoVo getPatrolById(String patrolId);

    boolean savePatrolInfo();

    /**
     * 保存下一天的巡更数据
     *
     * @return
     * @author: 王伟
     * @since:2020-10-28 9:03
     */
    boolean saveNextDayPatrolInfo();

    boolean deleteById(String patrolId);

    boolean batchRemove(List<String> ids);

//    boolean timeOutCheck();

    /**
     * 查询可签到的任务
     * @param page
     * @param StaffId
     * @param vo
     * @return
     */
    IPage<ProjectPatrolInfoVo> pageByPatrolInfoToDo(Page<ProjectPatrolInfoVo> page,String StaffId, ProjectPatrolInfoSearchCondition vo);

    /**
     * 按时间查询完成的任务
     * @param page
     * @param StaffId
     * @param date
     * @return
     */
    Page<ProjectPatrolInfoVo> selectDateToDo(Page page,String StaffId, String date);
    /**
     * 查询可认领的接口
     * @param page
     * @param StaffId
     * @param vo
     * @return
     */
    IPage<ProjectPatrolInfoVo> pageByPatrolInfoForMe(Page<ProjectPatrolInfoVo> page,String StaffId, ProjectPatrolInfoSearchCondition vo);


    /**
     * 认领巡更任务
     * @param StaffId
     * @param patrolId
     * @return
     */
    boolean joinPatrol(String StaffId,String patrolId);

    //签到

    /**
     * 处理超时任务状态
     * @return
     */
    boolean dealTimeOut();

    ProjectPatrolInfoOnDetailVo getVoById(String patrolId);
}