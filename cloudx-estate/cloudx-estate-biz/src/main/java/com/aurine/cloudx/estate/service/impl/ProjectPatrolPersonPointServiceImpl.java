package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.estate.constant.InspectTaskCheckInStatusConstant;
import com.aurine.cloudx.estate.constant.PatrolStatus;
import com.aurine.cloudx.estate.entity.ProjectPatrolDetail;
import com.aurine.cloudx.estate.entity.ProjectPatrolInfo;
import com.aurine.cloudx.estate.entity.ProjectPatrolPerson;
import com.aurine.cloudx.estate.entity.ProjectPatrolPersonPoint;
import com.aurine.cloudx.estate.mapper.ProjectPatrolPersonPointMapper;
import com.aurine.cloudx.estate.service.ProjectPatrolDetailService;
import com.aurine.cloudx.estate.service.ProjectPatrolInfoService;
import com.aurine.cloudx.estate.service.ProjectPatrolPersonPointService;
import com.aurine.cloudx.estate.service.ProjectPatrolPersonService;
import com.aurine.cloudx.estate.vo.ProjectPatrolPersonPointVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 人员巡更巡点记录(ProjectPatrolPersonPoint)表服务实现类
 *
 * @author 王良俊
 * @since 2020-09-17 10:29:22
 */
@Service
public class ProjectPatrolPersonPointServiceImpl extends ServiceImpl<ProjectPatrolPersonPointMapper, ProjectPatrolPersonPoint> implements ProjectPatrolPersonPointService {

    @Resource
    ProjectPatrolDetailService projectPatrolDetailService;
    @Resource
    ProjectPatrolPersonService projectPatrolPersonService;
    @Resource
    ProjectPatrolInfoService projectPatrolInfoService;

    @Override
    public boolean saveRelationship(String patrolId, List<String> patrolPersonIdList) {

        List<ProjectPatrolPersonPoint> patrolPersonPointList = new ArrayList<>();
        List<ProjectPatrolDetail> detailList = projectPatrolDetailService.list(new QueryWrapper<ProjectPatrolDetail>()
                .lambda().eq(ProjectPatrolDetail::getPatrolId, patrolId));
        if (CollUtil.isNotEmpty(detailList) && CollUtil.isNotEmpty(patrolPersonIdList)) {
            detailList.forEach(patrolDetail -> {
                patrolPersonIdList.forEach(patrolPersonId -> {
                    ProjectPatrolPersonPoint patrolPersonPoint = new ProjectPatrolPersonPoint();
                    patrolPersonPoint.setPatrolPersonId(patrolPersonId);
                    patrolPersonPoint.setPatrolDetailId(patrolDetail.getPatrolDetailId());
                    patrolPersonPoint.setPatrolResult("0");
                    patrolPersonPoint.setCheckInStatus("");
                    patrolPersonPointList.add(patrolPersonPoint);
                });
            });
        }
        return this.saveBatch(patrolPersonPointList);
    }

    /**
     * <p>
     * 保存人员和巡更明细的关系
     * </p>
     *
     * @param patrolId           巡更记录ID
     * @param patrolPersonIdList 人员分配ID
     * @param detailList
     * @return 是否保存成功
     */
    @Override
    public boolean saveRelationship(String patrolId, List<String> patrolPersonIdList, List<ProjectPatrolDetail> detailList) {
        List<ProjectPatrolPersonPoint> patrolPersonPointList = new ArrayList<>();
        if (CollUtil.isNotEmpty(detailList) && CollUtil.isNotEmpty(patrolPersonIdList)) {
            detailList.forEach(patrolDetail -> {
                patrolPersonIdList.forEach(patrolPersonId -> {
                    ProjectPatrolPersonPoint patrolPersonPoint = new ProjectPatrolPersonPoint();
                    patrolPersonPoint.setPatrolPersonId(patrolPersonId);
                    patrolPersonPoint.setPatrolDetailId(patrolDetail.getPatrolDetailId());
                    patrolPersonPoint.setCheckInStatus("");
                    patrolPersonPoint.setCheckInPic("");
                    patrolPersonPoint.setPatrolResult("0");
                    patrolPersonPoint.setResultDesc("");
                    patrolPersonPoint.setPatrolResultPic1("");
                    patrolPersonPoint.setPatrolResultPic2("");
                    patrolPersonPoint.setPatrolResultPic3("");
                    patrolPersonPoint.setOperator(1);

                    patrolPersonPointList.add(patrolPersonPoint);
                });
            });
        }
        return this.saveBatch(patrolPersonPointList);
    }

    @Override
    public boolean removeBatch(String patrolId, List<String> staffIdList) {
        if (CollUtil.isNotEmpty(staffIdList)) {
            List<ProjectPatrolDetail> detailList = projectPatrolDetailService.list(new QueryWrapper<ProjectPatrolDetail>().lambda()
                    .eq(ProjectPatrolDetail::getPatrolId, patrolId));
            List<ProjectPatrolPerson> patrolPersonList = projectPatrolPersonService.list(new QueryWrapper<ProjectPatrolPerson>().lambda()
                    .eq(ProjectPatrolPerson::getPatrolId, patrolId)
                    .in(ProjectPatrolPerson::getStaffId, staffIdList));
            if (CollUtil.isNotEmpty(patrolPersonList)) {
                List<String> patrolPersonIdList = patrolPersonList.stream().map(ProjectPatrolPerson::getPatrolPersonId).collect(Collectors.toList());
                List<String> detailIdList = detailList.stream().map(ProjectPatrolDetail::getPatrolDetailId).collect(Collectors.toList());
                return this.remove(new QueryWrapper<ProjectPatrolPersonPoint>().lambda()
                        .in(ProjectPatrolPersonPoint::getPatrolDetailId, detailIdList)
                        .in(ProjectPatrolPersonPoint::getPatrolPersonId, patrolPersonIdList));
            }
        }
        return true;
    }

    /**
     * 删除巡更任务ID，清空当前任务的配置
     *
     * @param patrolId
     * @return
     */
    @Override
    public boolean removeByPatrolId(String patrolId) {
        List<ProjectPatrolDetail> detailList = projectPatrolDetailService.list(new QueryWrapper<ProjectPatrolDetail>().lambda().eq(ProjectPatrolDetail::getPatrolId, patrolId));
        if (CollUtil.isNotEmpty(detailList)) {
            List<String> detailIdList = detailList.stream().map(ProjectPatrolDetail::getPatrolDetailId).collect(Collectors.toList());
            return this.remove(new QueryWrapper<ProjectPatrolPersonPoint>().lambda().in(ProjectPatrolPersonPoint::getPatrolDetailId, detailIdList));
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sign(ProjectPatrolPersonPoint projectPatrolPersonPoint) {


        //获取签到明细信息
        ProjectPatrolDetail projectPatrolDetail = projectPatrolDetailService.getById(projectPatrolPersonPoint.getPatrolDetailId());
        //获取所有签到明细
        List<ProjectPatrolPersonPointVo> projectPatrolPerson = projectPatrolPersonService.getDetailByPatrolId(projectPatrolDetail.getPatrolId());
        //异常数量
        int errorNum = 0;
        //未巡更数量
        int num = 0;

        for (ProjectPatrolPersonPointVo p : projectPatrolPerson) {
            // 一个事务里面更新不会影响到查询故这里进行重新赋值
            if (p.getSeq().equals(projectPatrolPersonPoint.getSeq())) {
                BeanUtils.copyProperties(projectPatrolPersonPoint, p);
            }
            //可能这个状态值有坑在里面 以防万一 设置多个可能存在的条件判断
            if (StringUtils.isBlank(p.getPatrolResult()) || "0".equals(p.getPatrolResult())) {
                num++;
            }
            if ("2".equals(p.getPatrolResult())) {
                errorNum++;
            }

        }



        ProjectPatrolInfo projectPatrolInfo = projectPatrolInfoService.getById(projectPatrolDetail.getPatrolId());
        projectPatrolInfo.setAlreadyPatrolled(projectPatrolPerson.size() - num);
        projectPatrolInfo.setStatus(PatrolStatus.PATROLLING);
        LocalDateTime planEndTime = LocalDateTime.of(projectPatrolInfo.getPatrolDate(), projectPatrolInfo.getPatrolEndTime());
        if (LocalDateTime.now().isAfter(planEndTime)) {
            projectPatrolPersonPoint.setCheckInStatus(InspectTaskCheckInStatusConstant.TIMEOUT);
        } else {
            projectPatrolPersonPoint.setCheckInStatus(InspectTaskCheckInStatusConstant.NORMAL);
        }
        projectPatrolPersonPoint.setUpdateTime(LocalDateTime.now());
        baseMapper.updateById(projectPatrolPersonPoint);

        if (num == 0) {
            if (errorNum > 0) {
                projectPatrolInfo.setResult("2");
            } else {
                projectPatrolInfo.setResult("1");
            }

            if (LocalDateTime.now().isAfter(planEndTime)) {
                projectPatrolInfo.setCheckInStatus(InspectTaskCheckInStatusConstant.TIMEOUT);
            } else {
                projectPatrolInfo.setCheckInStatus(InspectTaskCheckInStatusConstant.NORMAL);
            }
            projectPatrolInfo.setStatus(PatrolStatus.FINISH);
        }

        projectPatrolInfoService.updateById(projectPatrolInfo);
    }


    @Override
    public Integer countStatusByStaffId(String staffId, List<String> status, LocalDate date) {
        if (StringUtil.isEmpty(staffId) || CollUtil.isEmpty(status)) {
            throw new RuntimeException("未获取到员工ID和状态");
        }
        return baseMapper.countStatusByStaffId(staffId, status, date);
    }

    @Override
    public Integer countCompleteByStaffId(String staffId, LocalDate date) {
        List<String> status = new ArrayList<>();
        // 已完成
        status.add("2");
        return this.countStatusByStaffId(staffId, status, date);
    }

    @Override
    public Integer countUnCompleteByStaffId(String staffId, LocalDate date) {
        List<String> status = new ArrayList<>();
        // 待巡更
        status.add("0");
        // 巡更中
        status.add("1");
        // 已过期
        status.add("3");
        return this.countStatusByStaffId(staffId, status, date);
    }

    @Override
    public Integer countAllByStaffId(String staffId, LocalDate date) {
        List<String> status = new ArrayList<>();
        // 待巡更
        status.add("0");
        // 已完成
        status.add("2");
        // 巡更中
        status.add("1");
        // 已过期
        status.add("3");
        return this.countStatusByStaffId(staffId, status, date);
    }

}