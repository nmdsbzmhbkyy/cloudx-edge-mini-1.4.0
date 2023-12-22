package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.common.core.constant.DataConstants;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.ProjectPatrolPointConf;
import com.aurine.cloudx.estate.entity.ProjectPatrolRouteConf;
import com.aurine.cloudx.estate.entity.ProjectPatrolRoutePointConf;
import com.aurine.cloudx.estate.entity.ProjectStaff;
import com.aurine.cloudx.estate.mapper.ProjectPatrolRouteConfMapper;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.vo.ProjectPatrolPointConfVo;
import com.aurine.cloudx.estate.vo.ProjectPatrolRouteConfVo;
import com.aurine.cloudx.estate.vo.ProjectPatrolRoutePointConfVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


/**
 * 项目巡更路线设置(ProjectPatrolRouteConf)表服务实现类
 *
 * @author 黄阳光 huangyg@aurine.cn
 * @since 2020-07-24 12:00:08
 */
@Service
public class ProjectPatrolRouteConfServiceImpl extends ServiceImpl<ProjectPatrolRouteConfMapper, ProjectPatrolRouteConf> implements ProjectPatrolRouteConfService {

    @Resource
    ProjectPatrolRouteConfMapper projectPatrolRouteConfMapper;

    @Resource
    ProjectPatrolRoutePointConfService projectPatrolRoutePointConfService;

    @Resource
    ProjectStaffService projectStaffService;
    @Resource
    ProjectPatrolPointConfService projectPatrolPointConfService;
    @Resource
    ProjectPatrolRouteStaffService projectPatrolRouteStaffService;

    @Override
    public IPage<ProjectPatrolRouteConfVo> page(Page page, ProjectPatrolRouteConfVo vo) {
        vo.setProjectId(ProjectContextHolder.getProjectId());
        return projectPatrolRouteConfMapper.selectAllList(page, vo);
    }

    /**
     * 新增巡更路线
     *
     * @param vo
     * @return
     */
    @Override
    public boolean save(ProjectPatrolRouteConfVo vo) {
        //保存项目与租户id
        vo.setProjectId(ProjectContextHolder.getProjectId());

        //生成 patrolRouteId，用于保存巡更路线点关联配置中的 patrolRouteId
        String uid = UUID.randomUUID().toString().replace("-", "");
        vo.setPatrolRouteId(uid);
        vo.setStatus("1");

        //判断巡更点的数量是否为0
        if (vo.getRoutePointList() == null || vo.getRoutePointList().size() < 1) {
            throw new RuntimeException("请选择巡更点");
        }
        /*
         *  将传递的 pointId 保存至巡更路线点关联配置中的 patrolPointId
         */
        List<ProjectPatrolRoutePointConf> routePointList = new ArrayList<>();
        for (ProjectPatrolRoutePointConfVo projectPatrolRoutePointConfVo : vo.getRoutePointList()) {
            //保存关联的 patrolRouteId
            projectPatrolRoutePointConfVo.setPatrolRouteId(uid);
            ProjectPatrolRoutePointConf pointPo = new ProjectPatrolRoutePointConf();
            BeanUtils.copyProperties(projectPatrolRoutePointConfVo, pointPo);
            pointPo.setPatrolPointId(projectPatrolRoutePointConfVo.getPointId());
            routePointList.add(pointPo);
        }

        projectPatrolRoutePointConfService.saveBatch(routePointList);

        /**
         * 增加 存储参与人 功能
         * @since 2020-10-27 17:05
         * @author: 王伟
         */
        projectPatrolRouteStaffService.savePatrolStaffs(Arrays.asList(vo.getPatrolRoutePersonArray()), uid);

        ProjectPatrolRouteConf routePo = new ProjectPatrolRouteConf();
        BeanUtils.copyProperties(vo, routePo);
        //将时间格式转为字符串并保存
        return this.save(dateChange(vo, routePo));
    }

    /**
     * 根据ID修改巡更路线
     *
     * @param vo
     * @return
     */
    @Override
    @Transactional
    public boolean updatePatrolRouteConfById(ProjectPatrolRouteConfVo vo) {
        vo.setProjectId(ProjectContextHolder.getProjectId());
        //判断巡更点的数量是否为0
        if (vo.getRoutePointList() == null || vo.getRoutePointList().size() < 1) {
            throw new RuntimeException("请选择巡更点");
        }

        /*
         * 清空巡更路线点关联配置
         */
        projectPatrolRoutePointConfService.remove(new QueryWrapper<ProjectPatrolRoutePointConf>().lambda()
                .eq(ProjectPatrolRoutePointConf::getPatrolRouteId, vo.getPatrolRouteId()));

        /*
         *  将传递的 pointId 保存至巡更路线点关联配置中的 patrolPointId
         */
        List<ProjectPatrolRoutePointConf> routePointList = new ArrayList<>();
        for (ProjectPatrolRoutePointConfVo projectPatrolRoutePointConfVo : vo.getRoutePointList()) {
            ProjectPatrolRoutePointConf pointPo = new ProjectPatrolRoutePointConf();
            BeanUtils.copyProperties(projectPatrolRoutePointConfVo, pointPo);
            pointPo.setPatrolRouteId(vo.getPatrolRouteId());
            pointPo.setPatrolPointId(projectPatrolRoutePointConfVo.getPointId());
            routePointList.add(pointPo);
        }

        projectPatrolRoutePointConfService.saveBatch(routePointList);

        /**
         * 增加 存储参与人 功能
         * @since 2020-10-27 17:05
         * @author: 王伟
         */
        projectPatrolRouteStaffService.savePatrolStaffs(Arrays.asList(vo.getPatrolRoutePersonArray()), vo.getPatrolRouteId());

        ProjectPatrolRouteConf routePo = new ProjectPatrolRouteConf();
        BeanUtils.copyProperties(vo, routePo);
        //将时间格式转为字符串并保存
        return this.updateById(dateChange(vo, routePo));
    }

    /**
     * 修改巡更路线状态
     *
     * @param patrolRouteId
     * @return
     */
    @Override
    public boolean updateStatusById(String patrolRouteId) {
        ProjectPatrolRouteConf po = this.getById(patrolRouteId);
        if (po.getStatus().equals("1")) {
            po.setStatus("0");
        } else {
            po.setStatus("1");
        }
        return this.updateById(po);
    }

    /**
     * 使用patrolRouteId获取巡更路线数据
     *
     * @param patrolRouteId
     * @return
     */
    @Override
    public ProjectPatrolRouteConfVo getVoById(String patrolRouteId) {
        //使用patrolRouteId获取巡更路线数据
        ProjectPatrolRouteConfVo vo = projectPatrolRouteConfMapper.select(patrolRouteId, ProjectContextHolder.getProjectId());
        if (vo.getPatrolTimeType().equals("1")) {
            vo.setRegularTimeList(timeList(vo.getRegularTime()));
        } else {
            if (vo.getMonTime() != null) {
                vo.setMon(timeList(vo.getMonTime()));
            }
            if (vo.getTueTime() != null) {
                vo.setTue(timeList(vo.getTueTime()));
            }
            if (vo.getWedTime() != null) {
                vo.setWed(timeList(vo.getWedTime()));
            }
            if (vo.getThuTime() != null) {
                vo.setThu(timeList(vo.getThuTime()));
            }
            if (vo.getFriTime() != null) {
                vo.setFri(timeList(vo.getFriTime()));
            }
            if (vo.getSatTime() != null) {
                vo.setSat(timeList(vo.getSatTime()));
            }
            if (vo.getSunTime() != null) {
                vo.setSun(timeList(vo.getSunTime()));
            }
        }
        /*
         * 使用patrolRouteId获取 巡更路线点关联 的集合
         */
        List<ProjectPatrolRoutePointConf> patrolRoutePointList = projectPatrolRoutePointConfService.list(
                new QueryWrapper<ProjectPatrolRoutePointConf>().lambda()
                        .eq(ProjectPatrolRoutePointConf::getPatrolRouteId, patrolRouteId));

        if (patrolRoutePointList.size() < 1) {
            return null;
        }
        /*
         * 获取关联集合中的pointId，并保存为集合 以获取巡更点集合
         */
        List<String> pointIdList = new ArrayList<>();
        for (ProjectPatrolRoutePointConf result : patrolRoutePointList) {
            pointIdList.add(result.getPatrolPointId());
        }

        List<ProjectPatrolPointConf> pointPoList = projectPatrolPointConfService.list(new QueryWrapper<ProjectPatrolPointConf>().lambda()
                .in(ProjectPatrolPointConf::getPointId, pointIdList));

        /*
         * 将巡更路线点关联集合中的顺序保存至巡更点集合
         */
        List<ProjectPatrolPointConfVo> pointVoList = new ArrayList<>();
        for (ProjectPatrolPointConf projectPatrolPointConf : pointPoList) {
            if (projectPatrolPointConf.getStatus().equals("0")) {
                continue;
            }
            ProjectPatrolPointConfVo pointVo = new ProjectPatrolPointConfVo();
            BeanUtils.copyProperties(projectPatrolPointConf, pointVo);
            pointVoList.add(pointVo);
        }
        for (ProjectPatrolRoutePointConf routePoint : patrolRoutePointList) {
            for (ProjectPatrolPointConfVo pointVo : pointVoList) {
                if (routePoint.getPatrolPointId().equals(pointVo.getPointId()) && routePoint.getSort() != null) {
                    pointVo.setSort(routePoint.getSort());
                }
            }
        }

        vo.setPointList(pointVoList);

        /*
         * 获得参与人
         */
        List<String> staffList = projectPatrolRouteStaffService.getPatrolStaffsIdList(vo.getPatrolRouteId());
        vo.setPatrolRoutePersonArray(staffList.toArray(new String[0]));

        /**
         * 修正无法获取顺序巡更间隔
         * @author: 王伟
         * @since：2020-09-21
         */
        List<ProjectPatrolRoutePointConfVo> projectPatrolRoutePointConfVoList = new ArrayList<>();
        ProjectPatrolRoutePointConfVo patrolRoutePointConfVo = null;
        for (ProjectPatrolRoutePointConf patrolRoutePointConf : patrolRoutePointList) {
            patrolRoutePointConfVo = new ProjectPatrolRoutePointConfVo();
            BeanUtils.copyProperties(patrolRoutePointConf, patrolRoutePointConfVo);
            projectPatrolRoutePointConfVoList.add(patrolRoutePointConfVo);
        }
        vo.setRoutePointList(projectPatrolRoutePointConfVoList);

        return vo;
    }

    /**
     * 获取参与人姓名
     * @param patrolRouteId 巡更路线ID
     * @return 参与人姓名
     */
    @Override
    public List<String> getPersonNameList(String patrolRouteId) {
        List<ProjectStaff> staffList = projectStaffService.list(new QueryWrapper<ProjectStaff>().lambda()
                .eq(ProjectStaff::getProjectId,ProjectContextHolder.getProjectId())
//                .eq(ProjectStaff::getStaffType, DataConstants.TRUE)
                );
        List<String> staffIdList = projectPatrolRouteStaffService.getPatrolStaffsIdList(patrolRouteId);
//        ProjectPatrolRouteConfVo routeConfVo = projectPatrolRouteConfMapper.select(patrolRouteId, ProjectContextHolder.getProjectId());

        List<String> personNameList = new ArrayList<>();
        for (String personId : staffIdList) {
            for (ProjectStaff staff : staffList) {
                if (staff.getStaffId().equals(personId)) {
                    personNameList.add(staff.getStaffName());
                }
            }
        }
        return personNameList;
    }

    /**
     * 巡更时间段转换为字符串
     *
     * @param vo
     * @param po
     * @return
     */
    private ProjectPatrolRouteConf dateChange(ProjectPatrolRouteConfVo vo, ProjectPatrolRouteConf po) {
        //判断巡更时间类型，为每天则保存时间字符串后直接返回
        if (vo.getPatrolTimeType().equals("1")) {

            po.setRegularTime(vo.getRegularTimeList().toString());
            return po;
        }
        //清空周一巡更时间
        //为每周则分别保存每天的巡更时间
        if (vo.getMon() != null && vo.getMon().size() > 0) {
            po.setMon(vo.getMon().toString().replaceAll(" ", ""));
        } else {
            po.setMon("");
        }
        if (vo.getTue() != null && vo.getTue().size() > 0) {
            po.setTue(vo.getTue().toString().replaceAll(" ", ""));
        } else {
            po.setTue("");
        }
        if (vo.getWed() != null && vo.getWed().size() > 0) {
            po.setWed(vo.getWed().toString().replaceAll(" ", ""));
        } else {
            po.setWed("");
        }
        if (vo.getThu() != null && vo.getThu().size() > 0) {
            po.setThu(vo.getThu().toString().replaceAll(" ", ""));
        } else {
            po.setThu("");
        }
        if (vo.getFri() != null && vo.getFri().size() > 0) {
            po.setFri(vo.getFri().toString().replaceAll(" ", ""));
        } else {
            po.setFri("");
        }
        if (vo.getSat() != null && vo.getSat().size() > 0) {
            po.setSat(vo.getSat().toString().replaceAll(" ", ""));
        } else {
            po.setSat("");
        }
        if (vo.getSun() != null && vo.getSun().size() > 0) {
            po.setSun(vo.getSun().toString().replaceAll(" ", ""));
        } else {
            po.setSun("");
        }
        return po;
    }

    private List timeList(String time) {
        time = time.replace("[", "").replace("]", "").replaceAll(" ", "");
        String[] arr = time.split(",");
        return Arrays.asList(arr);
    }

}