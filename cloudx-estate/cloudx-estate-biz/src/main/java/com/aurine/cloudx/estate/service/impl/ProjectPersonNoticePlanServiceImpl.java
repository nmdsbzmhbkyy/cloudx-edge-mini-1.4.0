package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.common.core.util.CronExpParserUtil;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.estate.entity.ProjectFrameInfo;
import com.aurine.cloudx.estate.entity.ProjectNoticePlanTarget;
import com.aurine.cloudx.estate.entity.ProjectPersonNoticePlan;
import com.aurine.cloudx.estate.mapper.ProjectPersonNoticePlanMapper;
import com.aurine.cloudx.estate.service.ProjectFrameInfoService;
import com.aurine.cloudx.estate.service.ProjectNoticePlanTargetService;
import com.aurine.cloudx.estate.service.ProjectNoticeService;
import com.aurine.cloudx.estate.service.ProjectPersonNoticePlanService;
import com.aurine.cloudx.estate.vo.ProjectNoticeAddVo;
import com.aurine.cloudx.estate.vo.ProjectPersonNoticePlanFormVo;
import com.aurine.cloudx.estate.vo.ProjectPersonNoticePlanPageVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 住户通知计划(ProjectPersonNoticePlan)表服务实现类
 *
 * @author makejava
 * @since 2020-12-14 15:58:04
 */
@Service
@Slf4j
public class ProjectPersonNoticePlanServiceImpl extends ServiceImpl<ProjectPersonNoticePlanMapper, ProjectPersonNoticePlan> implements ProjectPersonNoticePlanService {
    @Resource
    private ProjectNoticePlanTargetService projectNoticePlanTargetService;
    @Resource
    private ProjectNoticeService projectNoticeService;
    @Resource
    private ProjectFrameInfoService projectFrameInfoService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean saveNoticePlan(ProjectPersonNoticePlanFormVo projectPersonNoticePlanForm) {
        String planName = projectPersonNoticePlanForm.getPlanName();
        int count = count(Wrappers.lambdaQuery(ProjectPersonNoticePlan.class).eq(ProjectPersonNoticePlan::getPlanName, planName));
        if (count != 0) {
            throw new RuntimeException("计划名称已存在");
        }
        String uid = UUID.randomUUID().toString().replace("-", "");
        List<String> houseId = projectPersonNoticePlanForm.getHouseId();
        List<ProjectNoticePlanTarget> projectNoticePlanTargets = new ArrayList<>();
        houseId.forEach(e -> {
            ProjectNoticePlanTarget projectNoticePlanTarget = new ProjectNoticePlanTarget();
            projectNoticePlanTarget.setPlanId(uid);
            projectNoticePlanTarget.setHouseId(e);
            projectNoticePlanTargets.add(projectNoticePlanTarget);
        });

        //批量保存发布对象
        projectNoticePlanTargetService.saveBatch(projectNoticePlanTargets);

        ProjectPersonNoticePlan projectPersonNoticePlan = new ProjectPersonNoticePlan();
        BeanUtils.copyProperties(projectPersonNoticePlanForm, projectPersonNoticePlan);
        projectPersonNoticePlan.setPlanId(uid);
        List<String> checkTargetType = projectPersonNoticePlanForm.getCheckTargetType();
        projectPersonNoticePlan.setTargetType(StringUtil.join(",", checkTargetType));

        LocalDateTime start = LocalDate.now().atTime(8, 0, 0);
        LocalDateTime end = LocalDate.now().atTime(23, 59, 59);
        LocalDateTime now = LocalDateTime.now();
        //判断今日是否在时间范围内
        if (projectPersonNoticePlan.getBeginTime().isBefore(now) && projectPersonNoticePlan.getEndTime().isAfter(now)) {
            //判断当前时间是否已经超过八点
            if ((now.isAfter(start) || now.isEqual(start)) && (now.isBefore(end) || now.isEqual(end))) {
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String currDate = LocalDate.now().format(fmt);

                List result = new ArrayList<>();
                boolean flag = CronExpParserUtil.parser(projectPersonNoticePlan.getFrequency(), currDate, result);
                if (flag && CollUtil.isNotEmpty(result)) {
                    noticePlanParseToNotice(CollUtil.toList(projectPersonNoticePlan));
                }
            }
        }

        return save(projectPersonNoticePlan);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean updateNoticePlan(ProjectPersonNoticePlanFormVo projectPersonNoticePlanFormVo) {
        String planName = projectPersonNoticePlanFormVo.getPlanName();
        int count = count(Wrappers.lambdaQuery(ProjectPersonNoticePlan.class).eq(ProjectPersonNoticePlan::getPlanName, planName)
                .ne(ProjectPersonNoticePlan::getPlanId, projectPersonNoticePlanFormVo.getPlanId()));
        if (count != 0) {
            throw new RuntimeException("计划名称已存在");
        }
        //先删除原先的发布对象
        projectNoticePlanTargetService.remove(Wrappers.lambdaQuery(ProjectNoticePlanTarget.class)
                .eq(ProjectNoticePlanTarget::getPlanId, projectPersonNoticePlanFormVo.getPlanId()));
        List<String> houseId = projectPersonNoticePlanFormVo.getHouseId();
        List<ProjectNoticePlanTarget> projectNoticePlanTargets = new ArrayList<>();
        houseId.forEach(e -> {
            ProjectNoticePlanTarget projectNoticePlanTarget = new ProjectNoticePlanTarget();
            projectNoticePlanTarget.setPlanId(projectPersonNoticePlanFormVo.getPlanId());
            projectNoticePlanTarget.setHouseId(e);
            projectNoticePlanTargets.add(projectNoticePlanTarget);
        });
        //保存当前添加的发布对象
        projectNoticePlanTargetService.saveBatch(projectNoticePlanTargets);

        ProjectPersonNoticePlan projectPersonNoticePlan = new ProjectPersonNoticePlan();
        BeanUtils.copyProperties(projectPersonNoticePlanFormVo, projectPersonNoticePlan);
        List<String> checkTargetType = projectPersonNoticePlanFormVo.getCheckTargetType();
        projectPersonNoticePlan.setTargetType(StringUtil.join(",", checkTargetType));

        return updateById(projectPersonNoticePlan);
    }

    @Override
    public Page<ProjectPersonNoticePlanPageVo> pageNoticePlan(Page page, ProjectPersonNoticePlanPageVo projectPersonNoticePlanPageVo) {
        return baseMapper.pageNoticePlan(page, projectPersonNoticePlanPageVo);
    }

    @Override
    public ProjectPersonNoticePlanFormVo getByPlanId(String planId) {
        ProjectPersonNoticePlan projectPersonNoticePlan = super.getById(planId);
        List<String> houseIds = projectNoticePlanTargetService.list(Wrappers.lambdaQuery(ProjectNoticePlanTarget.class)
                .eq(ProjectNoticePlanTarget::getPlanId, planId))
                .stream().map(ProjectNoticePlanTarget::getHouseId)
                .collect(Collectors.toList());
        ProjectPersonNoticePlanFormVo projectPersonNoticePlanFormVo = new ProjectPersonNoticePlanFormVo();
        BeanUtils.copyProperties(projectPersonNoticePlan, projectPersonNoticePlanFormVo);
        projectPersonNoticePlanFormVo.setHouseId(houseIds);
        List<String> houseNames = new ArrayList<>();
        houseIds.forEach(e -> {
            ProjectFrameInfo projectFrameInfo = projectFrameInfoService.getById(e);
            if (projectFrameInfo != null && "1".equals(projectFrameInfo.getIsHouse())) {
                String houseName = "";
                if (BeanUtil.isNotEmpty(projectFrameInfo)) {
                    //获取单元实体
                    ProjectFrameInfo projectFrameInfoUnit = projectFrameInfoService.getById(projectFrameInfo.getPuid());
                    //获取楼栋实体
                    ProjectFrameInfo projectFrameInfoBuilding = projectFrameInfoService.getById(projectFrameInfoUnit.getPuid());
                    //获取组团信息
                    String groupName = projectFrameInfoService.getFrameNameById(projectFrameInfoBuilding.getEntityId());
                    //如果存在组团 对组团进行拼接
                    if (StrUtil.isNotEmpty(groupName)) {
                        List<String> groupNameList = CollUtil.reverse(StrUtil.split(groupName, ','));
                        String buildingName = groupNameList.toString().replace("[", "")
                                .replace("]", "").replace(",", "-").replace(" ", "");
                        houseName = buildingName + projectFrameInfoUnit.getEntityName() + projectFrameInfo.getEntityName() + "房";
                    } else {
                        houseName = projectFrameInfoBuilding.getEntityName() + projectFrameInfoUnit.getEntityName() + projectFrameInfo.getEntityName() + "房";
                    }
                    houseNames.add(houseName);
                }
            }
        });
        projectPersonNoticePlanFormVo.setHouseName(houseNames);
        return projectPersonNoticePlanFormVo;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean removeByPlanId(String planId) {
        projectNoticePlanTargetService.remove(Wrappers.lambdaQuery(ProjectNoticePlanTarget.class)
                .eq(ProjectNoticePlanTarget::getPlanId, planId));
        return removeById(planId);
    }

    @Override
    public void executeNoticePlan() {
        //当前项目可执行的通知计划
        List<ProjectPersonNoticePlan> list = list(Wrappers.lambdaQuery(ProjectPersonNoticePlan.class)
                .eq(ProjectPersonNoticePlan::getIsActive, "1")
                .le(ProjectPersonNoticePlan::getBeginTime, LocalDateTime.now())
                .ge(ProjectPersonNoticePlan::getEndTime, LocalDateTime.now()));
        if (CollUtil.isNotEmpty(list)) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String currDate = LocalDate.now().format(fmt);
            //根据cron表达式获取真正需要执行的消息计划
            List<ProjectPersonNoticePlan> projectPersonNoticePlans = new ArrayList<>();
            list.forEach(e -> {
                List result = new ArrayList<>();
                boolean flag = CronExpParserUtil.parser(e.getFrequency(), currDate, result);
                if (flag && CollUtil.isNotEmpty(result)) {
                    projectPersonNoticePlans.add(e);
                }
            });
            noticePlanParseToNotice(projectPersonNoticePlans);
        }
    }

    private void noticePlanParseToNotice(List<ProjectPersonNoticePlan> projectPersonNoticePlans) {
        projectPersonNoticePlans.forEach(e -> {
            ProjectNoticeAddVo projectNoticeAddVo = new ProjectNoticeAddVo();
            BeanUtils.copyProperties(e, projectNoticeAddVo);
            List<String> houseIds = projectNoticePlanTargetService.list(Wrappers.lambdaQuery(ProjectNoticePlanTarget.class)
                    .eq(ProjectNoticePlanTarget::getPlanId, e.getPlanId()))
                    .stream().map(ProjectNoticePlanTarget::getHouseId)
                    .collect(Collectors.toList());

            String[] targetTypes = e.getTargetType().substring(2, e.getTargetType().length() - 1).split(",");
            projectNoticeAddVo.setHouseId(houseIds);
            projectNoticeAddVo.setCheckTargetType(CollUtil.toList(targetTypes));
            projectNoticeAddVo.setNoticeType("1");
            projectNoticeAddVo.setNoticeTitle(e.getTitle());
            projectNoticeAddVo.setPubTime(LocalDateTime.now());
            projectNoticeAddVo.setNoticeCategory(e.getNoticeCatetory());
//            projectNoticeService.saveByDeviceIds(projectNoticeAddVo);
            projectNoticeService.sendBatch(projectNoticeAddVo);

        });
    }
}