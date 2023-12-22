package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.aurine.cloudx.estate.constant.enums.PatrolStatusEnum;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.mapper.ProjectPatrolInfoMapper;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 项目巡更记录(ProjectPatrolInfo)表服务实现类
 *
 * @author 黄阳光 <huangyg@aurine.cn>
 * @since 2020-09-09 08:56:19
 */
@Service
public class ProjectPatrolInfoServiceImpl extends ServiceImpl<ProjectPatrolInfoMapper, ProjectPatrolInfo> implements ProjectPatrolInfoService {


    @Resource
    ProjectPatrolDetailService projectPatrolDetailService;
    @Resource
    ProjectPatrolPersonService projectPatrolPersonService;
    @Resource
    ProjectPatrolRouteConfService projectPatrolRouteConfService;
    @Resource
    ProjectPatrolPointConfService projectPatrolPointConfService;
    @Resource
    ProjectPatrolRoutePointConfService projectPatrolRoutePointConfService;
    @Resource
    ProjectPatrolRouteStaffService projectPatrolRouteStaffService;
    @Resource
    ProjectPatrolPersonPointService projectPatrolPersonPointService;
    @Resource
    ProjectStaffService projectStaffService;

    @Override
    public ProjectStaffWorkVo getCount(String staffId, String date) {
        ProjectStaffWorkVo staffWorkVo = new ProjectStaffWorkVo();
        staffWorkVo.setAllTaskNum(baseMapper.getCount(staffId, null, date));
        if (staffWorkVo.getAllTaskNum().equals(0)) {
            staffWorkVo.setCompletedTaskNum(0);
            staffWorkVo.setUnCompletedTaskNum(0);
        } else {
            staffWorkVo.setCompletedTaskNum(baseMapper.getCount(staffId, "2", date));
            staffWorkVo.setUnCompletedTaskNum(staffWorkVo.getAllTaskNum() - staffWorkVo.getCompletedTaskNum());
        }
        return staffWorkVo;
    }

    /**
     * 分页查询
     *
     * @param page
     * @param vo
     */
    @Override
    public IPage<ProjectPatrolInfoVo> pageByPatrolInfo(Page<ProjectPatrolInfoVo> page, ProjectPatrolInfoSearchCondition vo) {
        baseMapper.updateAllTimeOut();
        IPage<ProjectPatrolInfoVo> result = baseMapper.page(page, vo);
        /*
        保存巡更点未巡数量
         */
        for (ProjectPatrolInfoVo patrolInfoVo : result.getRecords()) {
            if (patrolInfoVo != null) {
                if (patrolInfoVo.getAlreadyPatrolled() != null) {
                    patrolInfoVo.setNotPatrolled(patrolInfoVo.getShouldPatrolled() - patrolInfoVo.getAlreadyPatrolled());
                }
            }
        }
        return result;
    }

    /**
     * 判断巡更记录是否过期
     *
     * @return
     */
//    public boolean timeOutCheck() {
//        List<ProjectPatrolInfo> patrolInfoList = this.list();
//        for (ProjectPatrolInfo po : patrolInfoList) {
//            String[] timeArray = po.getPatrolTime().split(",");
//            if (timeArray == null || timeArray.length < 2) {
//                throw new RuntimeException("请处理脏数据");
//            }
//            LocalTime time = LocalTime.parse(timeArray[1]);
//            if (po.getStatus().equals(PatrolStatusEnum.NOPETROL.code) && time.compareTo(LocalTime.now()) < 0) {
//                po.setStatus(PatrolStatusEnum.OVERDUE.code);
//                ProjectPatrolInfo patrolInfo = baseMapper.selectById(po.getPatrolId());
//                patrolInfo.setStatus(PatrolStatusEnum.OVERDUE.code);
//                this.updateById(patrolInfo);
//            }
//        }
//        return true;
//    }

    @Override
    public IPage<ProjectPatrolInfoVo> pageByPatrolInfoToDo(Page<ProjectPatrolInfoVo> page, String staffId, ProjectPatrolInfoSearchCondition vo) {
        baseMapper.updateAllTimeOut();
        return baseMapper.pageByPatrolInfoToDo(page, staffId, vo);
    }

    @Override
    public Page<ProjectPatrolInfoVo> selectDateToDo(Page page, String StaffId, String date) {
        return baseMapper.selectDateToDo(page,StaffId,date);
    }

    @Override
    public IPage<ProjectPatrolInfoVo> pageByPatrolInfoForMe(Page<ProjectPatrolInfoVo> page, String staffId, ProjectPatrolInfoSearchCondition vo) {
        baseMapper.updateAllTimeOut();
        return baseMapper.pageByPatrolInfoForMe(page, staffId, vo);
    }


    /**
     * 认领巡更任务
     *
     * @param StaffId
     * @param patrolId
     * @return
     */
    @Override
    public boolean joinPatrol(String StaffId, String patrolId) {
        ProjectStaff staff = projectStaffService.getById(StaffId);
        if (staff == null) {
            log.error("认领巡更任务出错：未找到StaffId=" + StaffId + " 的员工");
            throw new RuntimeException("该员工不存在");
        }

        return projectPatrolPersonService.assignPerson(patrolId, staff);
    }

    /**
     * 处理超时任务状态
     *
     * @return
     */
    @Override
    public boolean dealTimeOut() {
        baseMapper.updateAllTimeOut();
        return true;
    }

    @Override
    public ProjectPatrolInfoOnDetailVo getVoById(String patrolId) {
        ProjectPatrolInfoOnDetailVo p = new ProjectPatrolInfoOnDetailVo();
        ProjectPatrolInfo projectPatrolInfo = baseMapper.selectById(patrolId);
        BeanUtils.copyProperties(projectPatrolInfo, p);
        List<ProjectPatrolPersonPointVo> pointVos = projectPatrolPersonService.getDetailByPatrolId(patrolId);
        p.setPointVos(pointVos);
        return p;
    }

    /**
     * 通过patrolId获取巡更记录详细信息
     *
     * @param patrolId
     */
    @Override
    public ProjectPatrolInfoVo getPatrolById(String patrolId) {
        /*
        通过id获取巡更记录
         */
        ProjectPatrolInfoVo vo = baseMapper.selectByVoId(patrolId);
        if (vo != null && vo.getAlreadyPatrolled() != null) {
            vo.setNotPatrolled(vo.getShouldPatrolled() - vo.getAlreadyPatrolled());
        }
        /*
        通过patrolId获取巡更信息明细
         */
        List<ProjectPatrolDetail> detailList = projectPatrolDetailService.list(new QueryWrapper<ProjectPatrolDetail>().lambda().eq(ProjectPatrolDetail::getPatrolId, patrolId));
        if (vo != null && detailList != null) {
            vo.setPointList(detailList);
        }
        /*
        通过patrolId获取巡更人员
         */
        List<ProjectPatrolPerson> personList = projectPatrolPersonService.list(new QueryWrapper<ProjectPatrolPerson>().lambda().eq(ProjectPatrolPerson::getPatrolId, patrolId));
        if (vo != null && personList != null) {
            vo.setPersonList(personList);
        }
        return vo;
    }

    /**
     * 自动生成巡更记录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean savePatrolInfo() {
        /*
        获取巡更路线
         */
        List<ProjectPatrolRouteConf> routeList = projectPatrolRouteConfService.list(new QueryWrapper<ProjectPatrolRouteConf>().lambda().eq(ProjectPatrolRouteConf::getStatus, "1"));
        if (routeList == null) {
            return false;
        }
        List<ProjectPatrolInfo> patrolInfoList = new ArrayList<>();
        /*
        根据每个巡更id进行生成巡更记录
         */
        for (ProjectPatrolRouteConf route : routeList) {
            /*
             获取日期保存至巡更日期
            */
            LocalDate time = LocalDate.now();

            /*
             判断巡更时间类型，获取不同时间
            */
            if (route.getPatrolTimeType().equals("1")) {
                String regular = route.getRegularTime();
                patrolInfoList = handlePatrol(patrolInfoList, regular, route, time);
            } else if (route.getPatrolTimeType().equals("2")) {
                int week = time.getDayOfWeek().getValue();
                String day = "";
                if (week == 1) {
                    day = route.getMon();
                } else if (week == 2) {
                    day = route.getTue();
                } else if (week == 3) {
                    day = route.getWed();
                } else if (week == 4) {
                    day = route.getThu();
                } else if (week == 5) {
                    day = route.getFri();
                } else if (week == 6) {
                    day = route.getSat();
                } else if (week == 7) {
                    day = route.getSun();
                }

                patrolInfoList = handlePatrol(patrolInfoList, day, route, time);

            }

            /*
              通过巡更路线ID获取排班计划，再通过排班计划ID获取员工表，并保存至巡更人员分配表
             */

            /*List<ProjectShiftPlanStaff> planStaffList = projectShiftPlanStaffService.list(new QueryWrapper<ProjectShiftPlanStaff>().lambda()
                    .eq(ProjectShiftPlanStaff::getPlanId, route.getSchedulePlanId()));
            List<ProjectPatrolPerson> patrolPersonList = new ArrayList<>();
            for (ProjectShiftPlanStaff planStaff : planStaffList) {
                ProjectStaff staff = projectStaffService.getById(planStaff.getStaffId());
                ProjectPatrolPerson patrolPerson = new ProjectPatrolPerson();
                patrolPerson.setPatrolId(route.getPatrolRouteId());
                patrolPerson.setStaffId(staff.getStaffId());
                patrolPerson.setStaffName(staff.getStaffName());
                patrolPerson.setProjectId(projectId);
                patrolPerson.setTenantId(tenantId);

                patrolPersonList.add(patrolPerson);
            }
            projectPatrolPersonService.saveBatch(patrolPersonList);*/
        }
        return this.saveBatch(patrolInfoList);
    }

    /**
     * 保存下一天的巡更数据
     *
     * @return
     * @author: 王伟
     * @since:2020-10-28 9:03
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveNextDayPatrolInfo() {
        //巡更线路
        List<ProjectPatrolRouteConf> routeList = projectPatrolRouteConfService.list(new QueryWrapper<ProjectPatrolRouteConf>().lambda().eq(ProjectPatrolRouteConf::getStatus, "1"));

        if (CollUtil.isEmpty(routeList)) {
            return true;
        }

        List<ProjectPatrolInfo> patrolInfoList = new ArrayList<>();

        //根据每个巡更id进行生成巡更记录
        for (ProjectPatrolRouteConf route : routeList) {

            //获取日期保存至巡更日期
            LocalDate time = DateUtil.toLocalDateTime(DateUtil.tomorrow()).toLocalDate();
            /*
             判断巡更时间类型，获取不同时间
            */
            if (route.getPatrolTimeType().equals("1")) {
                String regular = route.getRegularTime();
                patrolInfoList = handlePatrol(patrolInfoList, regular, route, time);
            } else if (route.getPatrolTimeType().equals("2")) {
                int week = time.getDayOfWeek().getValue();
                String day = "";
                if (week == 1) {
                    day = route.getMon();
                } else if (week == 2) {
                    day = route.getTue();
                } else if (week == 3) {
                    day = route.getWed();
                } else if (week == 4) {
                    day = route.getThu();
                } else if (week == 5) {
                    day = route.getFri();
                } else if (week == 6) {
                    day = route.getSat();
                } else if (week == 7) {
                    day = route.getSun();
                }

                patrolInfoList = handlePatrol(patrolInfoList, day, route, time);

            }

            /*
              通过巡更路线ID获取排班计划，再通过排班计划ID获取员工表，并保存至巡更人员分配表
             */

            /*List<ProjectShiftPlanStaff> planStaffList = projectShiftPlanStaffService.list(new QueryWrapper<ProjectShiftPlanStaff>().lambda()
                    .eq(ProjectShiftPlanStaff::getPlanId, route.getSchedulePlanId()));
            List<ProjectPatrolPerson> patrolPersonList = new ArrayList<>();
            for (ProjectShiftPlanStaff planStaff : planStaffList) {
                ProjectStaff staff = projectStaffService.getById(planStaff.getStaffId());
                ProjectPatrolPerson patrolPerson = new ProjectPatrolPerson();
                patrolPerson.setPatrolId(route.getPatrolRouteId());
                patrolPerson.setStaffId(staff.getStaffId());
                patrolPerson.setStaffName(staff.getStaffName());
                patrolPerson.setProjectId(projectId);
                patrolPerson.setTenantId(tenantId);

                patrolPersonList.add(patrolPerson);
            }
            projectPatrolPersonService.saveBatch(patrolPersonList);*/
        }
        return this.saveBatch(patrolInfoList);
    }

    @Override
    public boolean deleteById(String patrolId) {
        /*
        通过ID删除巡更巡更人员分配记录
         */
        projectPatrolPersonService.remove(new QueryWrapper<ProjectPatrolPerson>().lambda()
                .eq(ProjectPatrolPerson::getPatrolId, patrolId));

        /*
        通过ID删除巡更明细记录
         */
        projectPatrolDetailService.remove(new QueryWrapper<ProjectPatrolDetail>().lambda()
                .eq(ProjectPatrolDetail::getPatrolId, patrolId));

        /*
        通过ID删除巡更记录
         */
        return this.removeById(patrolId);
    }

    @Override
    @Transactional
    public boolean batchRemove(List<String> ids) {
        List<ProjectPatrolDetail> details = projectPatrolDetailService.list(new QueryWrapper<ProjectPatrolDetail>().lambda().in(ProjectPatrolDetail::getPatrolId, ids));
        List<ProjectPatrolPerson> personList = projectPatrolPersonService.list(new QueryWrapper<ProjectPatrolPerson>().lambda().in(ProjectPatrolPerson::getPatrolId, ids));
        List<String> detailIds = new ArrayList<>();
        for (ProjectPatrolDetail detail : details) {
            detailIds.add(detail.getPatrolDetailId());
        }
        List<String> personIds = new ArrayList<>();
        for (ProjectPatrolPerson person : personList) {
            personIds.add(person.getStaffId());
        }

        projectPatrolDetailService.removeByIds(details);
        projectPatrolPersonService.removeByIds(personList);
        projectPatrolPersonPointService.remove(new QueryWrapper<ProjectPatrolPersonPoint>().lambda().in(ProjectPatrolPersonPoint::getPatrolDetailId, detailIds));
        projectPatrolPersonPointService.remove(new QueryWrapper<ProjectPatrolPersonPoint>().lambda().in(ProjectPatrolPersonPoint::getPatrolPersonId, personIds));

        return super.removeByIds(ids);
    }

    private List<ProjectPatrolInfo> handlePatrol(List<ProjectPatrolInfo> patrolInfoList, String timeString, ProjectPatrolRouteConf route, LocalDate time) {
        if (timeString == null) {
            return patrolInfoList;
        }
        timeString = timeString.replace("[", "").replace("]", "");
        if (timeString.equals("")) {
            return patrolInfoList;
        }
        String[] timeArray = timeString.split(",");
        for (String patrolTime : timeArray) {
            ProjectPatrolInfo patrolInfo = setPatrol(route, time, patrolTime);
            List<ProjectPatrolDetail> detailList = setDetailList(route, patrolInfo.getPatrolId());
            if (detailList == null) {
                continue;
            }

            /**
             * 取消排班计划，变更为参与人
             * @author: 王伟
             * @since： 2020-10-29 8:43
             */
//                        /*
//                          通过排班计划ID获取排班计划
//                        */
//            ProjectShiftPlan plan = projectShiftPlanService.getById(route.getSchedulePlanId());
//            if (plan == null) {
//                continue;
//            }
//
//            patrolInfo.setPlanName(plan.getPlanName());

            //将可指派人员快照到明细表
            List<String> staffIdList = projectPatrolRouteStaffService.getPatrolStaffsIdList(patrolInfo.getPatrolRouteId());

            if (CollUtil.isNotEmpty(staffIdList)) {
                //为快照保存可选人
                projectPatrolPersonService.saveStaffInPlan(patrolInfo.getPatrolId(), projectStaffService.listByIds(staffIdList));

                //如果是自动分配，则根据设置的人数，分配排班人员
                List<String> personIdList = new ArrayList<>();
                if (route.getAssignType().equals("2")) {//系统轮询
                    personIdList = projectPatrolRouteStaffService.getNextStaffsIdList(route.getPatrolRouteId(), route.getPersonNumber());
                    if (CollUtil.isNotEmpty(personIdList)) {
                        List<ProjectStaff> staffList = projectStaffService.listByIds(personIdList);
                        projectPatrolPersonService.assignPerson(patrolInfo.getPatrolId(), staffList, detailList);
                    }
                }
            }


//            List<ProjectPatrolPerson> personList = new ArrayList<>();
//            if (personIdList != null && personIdList.size() > 0) {
//                for (String personId : personIdList) {
//                    ProjectPatrolPerson person = new ProjectPatrolPerson();
//                    person.setPatrolId(patrolInfo.getPatrolId());
//                    person.setStaffId(personId);
//                    person.setStaffType(PatrolPersonTypeEnum.PATROLCOMPLETED.code);
//                    personList.add(person);
//                }
//                projectPatrolPersonService.saveBatch(personList);
////                patrolInfo.setp
//            }

            patrolInfoList.add(patrolInfo);

            projectPatrolDetailService.saveBatch(detailList);
        }
        return patrolInfoList;
    }

    private ProjectPatrolInfo setPatrol(ProjectPatrolRouteConf route, LocalDate date, String patrolTime) {
        /*
        转换巡更时间并保存
         */
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime startTime = LocalTime.parse(patrolTime.trim());
        LocalTime endTime = startTime.plusMinutes(route.getPatrolDurationLimit());

        ProjectPatrolInfo patrolInfo = new ProjectPatrolInfo();
        BeanUtils.copyProperties(route, patrolInfo);
        patrolInfo.setPatrolId(UUID.randomUUID().toString().replace("-", ""));
        patrolInfo.setPatrolDate(date);
        patrolInfo.setPatrolTime(startTime.toString() + "," + endTime.toString());
        patrolInfo.setPatrolStartTime(startTime);
        patrolInfo.setPatrolEndTime(endTime);
        patrolInfo.setStatus("0");
        patrolInfo.setPlanName("");
        patrolInfo.setAlreadyPatrolled(0);//已寻数量=0
        patrolInfo.setResult("");
        patrolInfo.setCheckInStatus("");
        patrolInfo.setOperator(1);
        return patrolInfo;
    }

    private List<ProjectPatrolDetail> setDetailList(ProjectPatrolRouteConf route, String patrolId) {
        /*
                      通过巡更路线ID获取巡更点集合，并保存至巡更明细表
                    */
        List<ProjectPatrolRoutePointConf> patrolRoutePointList = projectPatrolRoutePointConfService.list(
                new QueryWrapper<ProjectPatrolRoutePointConf>().lambda()
                        .eq(ProjectPatrolRoutePointConf::getPatrolRouteId, route.getPatrolRouteId()));
        /*
         * 获取关联集合中的pointId，并保存为集合 以获取巡更点集合
         */
        if (patrolRoutePointList.size() < 1) {
            return null;
        }
        List<String> pointIdList = new ArrayList<>();
        for (ProjectPatrolRoutePointConf result : patrolRoutePointList) {
            pointIdList.add(result.getPatrolPointId());
        }
        List<ProjectPatrolPointConf> pointPoList = projectPatrolPointConfService.list(new QueryWrapper<ProjectPatrolPointConf>().lambda()
                .in(ProjectPatrolPointConf::getPointId, pointIdList));
        List<ProjectPatrolDetail> detailList = new ArrayList<>();
        for (ProjectPatrolPointConf point : pointPoList) {
            if (point.getStatus().equals("0")) {
                continue;
            }
            ProjectPatrolDetail detail = new ProjectPatrolDetail();
            BeanUtils.copyProperties(point, detail);
            detail.setPatrolId(patrolId);
            detail.setPatrolDetailId(UUID.randomUUID().toString().replaceAll("-", ""));
            detailList.add(detail);
        }
        return detailList;
    }
}