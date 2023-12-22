package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.ProjectPatrolDetail;
import com.aurine.cloudx.estate.entity.ProjectPatrolPerson;
import com.aurine.cloudx.estate.entity.ProjectStaff;
import com.aurine.cloudx.estate.mapper.ProjectPatrolPersonMapper;
import com.aurine.cloudx.estate.service.ProjectPatrolPersonPointService;
import com.aurine.cloudx.estate.service.ProjectPatrolPersonService;
import com.aurine.cloudx.estate.util.NoticeUtil;
import com.aurine.cloudx.estate.vo.ProjectPatrolPersonPointVo;
import com.aurine.cloudx.estate.vo.ProjectPatrolPersonVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 巡更人员分配表(ProjectPatrolPerson)表服务实现类
 *
 * @author 黄阳光 <huangyg@aurine.cn>
 * @since 2020-09-11 16:04:22
 */
@Service("projectPatrolPersonService")
public class ProjectPatrolPersonServiceImpl extends ServiceImpl<ProjectPatrolPersonMapper, ProjectPatrolPerson> implements ProjectPatrolPersonService {

    @Resource
    ProjectPatrolPersonPointService projectPatrolPersonPointService;
    @Resource
    ProjectPatrolPersonMapper projectPatrolPersonMapper;

    @Resource
    NoticeUtil noticeUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignPerson(String patrolId, List<ProjectStaff> staffList) {


        return this.assignPerson(patrolId, staffList, null);
    }

    /**
     * 发送员工任务消息通知
     *
     * @param staffList
     */
    private void sendAssignNotice(List<ProjectPatrolPerson> staffList) {
        try {
            for (ProjectPatrolPerson projectPatrolPerson : staffList) {
                noticeUtil.send(true, "巡更通知", "您有新的巡更任务,请查收", projectPatrolPerson.getStaffId());
            }

        } catch (Exception e) {
            log.warn("消息发送异常");
        }
    }

    /**
     * <p>
     * 指派人员（指派执行人）
     * </p>
     *
     * @param patrolId   巡更记录ID
     * @param staffList  所指派的人员对象列表
     * @param detailList
     * @return 是否指派成功
     * @author: 王良俊
     */
    @Override
    public boolean assignPerson(String patrolId, List<ProjectStaff> staffList, List<ProjectPatrolDetail> detailList) {
        List<String> newStaffIdList = staffList.stream().map(ProjectStaff::getStaffId).collect(Collectors.toList());
        // 这里取出旧的人员列表如果有的话
        List<ProjectPatrolPerson> oldStaffList = this.list(new QueryWrapper<ProjectPatrolPerson>()
                .lambda().eq(ProjectPatrolPerson::getPatrolId, patrolId).eq(ProjectPatrolPerson::getStaffType, "2"));
        List<String> oldStaffIdList = oldStaffList.stream().map(ProjectPatrolPerson::getStaffId).collect(Collectors.toList());
        List<String> beSaveStaffIdList = new ArrayList<>(newStaffIdList);
        List<String> beRemoveStaffIdList = new ArrayList<>(oldStaffIdList);
        if (CollUtil.isNotEmpty(oldStaffIdList)) {
            // 这里筛选出本次新指派的员工ID列表
            beSaveStaffIdList.removeAll(oldStaffIdList);
            // 这里筛选出本次被删除的员工ID列表
            beRemoveStaffIdList.removeAll(newStaffIdList);
            this.removeBatch(patrolId, beRemoveStaffIdList);
        }
        List<ProjectPatrolPerson> patrolPersonList = new ArrayList<>();
        if (CollUtil.isNotEmpty(staffList)) {
            staffList.forEach(staff -> {
                if (beSaveStaffIdList.contains(staff.getStaffId())) {
                    ProjectPatrolPerson patrolPerson = new ProjectPatrolPerson();
                    patrolPerson.setStaffId(staff.getStaffId());
                    patrolPerson.setStaffName(staff.getStaffName());
                    patrolPerson.setStaffType("2");//执行人
                    patrolPerson.setPatrolId(patrolId);
                    patrolPerson.setOperator(1);
                    patrolPerson.setPatrolPersonId(UUID.randomUUID().toString().replaceAll("-", ""));
                    patrolPersonList.add(patrolPerson);
                }
            });
        }
        boolean saveBatch = this.saveBatch(patrolPersonList);

        List<String> patrolPersonIdList = patrolPersonList.stream().map(ProjectPatrolPerson::getPatrolPersonId).collect(Collectors.toList());
        if (detailList != null) {
            projectPatrolPersonPointService.saveRelationship(patrolId, patrolPersonIdList, detailList);


        } else {
            projectPatrolPersonPointService.saveRelationship(patrolId, patrolPersonIdList);
        }
        //
        sendAssignNotice(patrolPersonList);
        return saveBatch;
    }

    /**
     * 增加一个指派人
     *
     * @param patrolId
     * @param staff
     * @return
     * @author: 王伟
     * @since: 2020-10-29 11:14
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignPerson(String patrolId, ProjectStaff staff) {

        //检查指派人是否已在任务中
        int count = this.count(new QueryWrapper<ProjectPatrolPerson>().lambda().eq(ProjectPatrolPerson::getPatrolId, patrolId)
                .eq(ProjectPatrolPerson::getStaffType, "2")
                .eq(ProjectPatrolPerson::getStaffId, staff.getStaffId()));

        if (count >= 1) {
            throw new RuntimeException(staff.getStaffName() + "已在当前巡更任务中，请勿重复添加");
        }


        //添加指派
        ProjectPatrolPerson patrolPerson = new ProjectPatrolPerson();
        patrolPerson.setStaffId(staff.getStaffId());
        patrolPerson.setStaffName(staff.getStaffName());
        patrolPerson.setStaffType("2");//执行人
        patrolPerson.setPatrolId(patrolId);
        this.save(patrolPerson);

        //添加巡更点签到卡
        List<String> patrolPersonIdList = new ArrayList<>();
        patrolPersonIdList.add(patrolPerson.getPatrolPersonId());


        return projectPatrolPersonPointService.saveRelationship(patrolId, patrolPersonIdList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeBatch(String patrolId, List<String> staffIdList) {
        if (StrUtil.isNotBlank(patrolId) && CollUtil.isNotEmpty(staffIdList)) {
            projectPatrolPersonPointService.removeBatch(patrolId, staffIdList);
            return this.remove(new QueryWrapper<ProjectPatrolPerson>().lambda()
                    .eq(ProjectPatrolPerson::getPatrolId, patrolId)
                    .in(ProjectPatrolPerson::getStaffId, staffIdList));
        }
        return true;
    }

    @Override
    public List<ProjectPatrolPersonVo> listByDetailId(String detailId) {
        return projectPatrolPersonMapper.listByDetailId(detailId);
    }

    @Override
    public Integer countUnpatrolByPatrolId(String patrolId) {
        return projectPatrolPersonMapper.countPatrolBypatrolId(patrolId, true);
    }

    @Override
    public Integer countPatrolByPatrolId(String patrolId) {
        return projectPatrolPersonMapper.countPatrolBypatrolId(patrolId, false);
    }

    @Override
    public Integer countTimeOutByPatrolId(String patrolId, String checkInStatus) {
        return projectPatrolPersonMapper.countTimeOutByPatrolId(patrolId, checkInStatus);
    }

    @Override
    public Integer countNormalByPatrolId(String patrolId, String patrolResult) {
        return projectPatrolPersonMapper.countNormalByPatrolId(patrolId, patrolResult);
    }

    /**
     * 根据线路id，获取当前任务的计划内人员（参与人）
     *
     * @param patrolId 任务ID
     * @return
     * @author: 王伟
     * @since: 2020-10-29 8:52
     */
    @Override
    public List<ProjectPatrolPerson> listStaffInPlan(String patrolId) {
        return this.list(new QueryWrapper<ProjectPatrolPerson>().lambda().eq(ProjectPatrolPerson::getPatrolId, patrolId).eq(ProjectPatrolPerson::getStaffType, "1"));
    }

    /**
     * 根据线路id，获取当前任务的被指派人
     *
     * @param patrolId
     * @return
     * @author: 王伟
     * @since: 2020-10-29 8:52
     */
    @Override
    public List<ProjectPatrolPerson> listStaffOnJob(String patrolId) {
        return this.list(new QueryWrapper<ProjectPatrolPerson>().lambda().eq(ProjectPatrolPerson::getPatrolId, patrolId).eq(ProjectPatrolPerson::getStaffType, "2"));

    }

    /**
     * 将可执行人（计划人员）覆盖存储到快任务照数据中
     *
     * @param patrolId
     * @param staffList
     * @return
     * @author: 王伟
     * @since: 2020-10-29 8:52
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveStaffInPlan(String patrolId, List<ProjectStaff> staffList) {
        ProjectPatrolPerson projectPatrolPerson;
        List<ProjectPatrolPerson> patrolPeopleList = new ArrayList<>();

        for (ProjectStaff staff : staffList) {
            projectPatrolPerson = new ProjectPatrolPerson();
            projectPatrolPerson.setStaffType("1");//计划人员
            projectPatrolPerson.setStaffId(staff.getStaffId());
            projectPatrolPerson.setStaffName(staff.getStaffName());
            projectPatrolPerson.setPatrolId(patrolId);
            projectPatrolPerson.setOperator(1);
            projectPatrolPerson.setPatrolPersonId(UUID.randomUUID().toString().replaceAll("-", ""));

            patrolPeopleList.add(projectPatrolPerson);
        }

        return this.saveBatch(patrolPeopleList);

    }

    @Override
    public List<ProjectPatrolPersonPointVo> getDetailByPatrolId(String patrolId) {
        return baseMapper.getDetailByPatrolId(patrolId);
    }
}