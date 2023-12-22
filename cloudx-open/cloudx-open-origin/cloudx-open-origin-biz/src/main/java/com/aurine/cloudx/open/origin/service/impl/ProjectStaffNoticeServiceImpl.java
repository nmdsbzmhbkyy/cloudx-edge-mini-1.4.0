package com.aurine.cloudx.open.origin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.open.origin.constant.ReadStatusConstant;
import com.aurine.cloudx.open.origin.entity.*;
import com.aurine.cloudx.open.origin.mapper.ProjectStaffNoticeMapper;
import com.aurine.cloudx.open.origin.service.*;
import com.aurine.cloudx.open.origin.vo.ProjectPersonNoticeAddVo;
import com.aurine.cloudx.open.origin.vo.ProjectStaffNoticeAddVo;
import com.aurine.cloudx.open.origin.vo.ProjectStaffNoticeVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 员工通知发布
 *
 * @author guhl@aurine.cn
 * @date 2020-07-06 11:16:25
 */
@Service
public class ProjectStaffNoticeServiceImpl extends ServiceImpl<ProjectStaffNoticeMapper, ProjectStaffNotice> implements ProjectStaffNoticeService {
    @Resource
    private ProjectStaffService projectStaffService;
    @Resource
    private ProjectStaffNoticeObjectService projectStaffNoticeObjectService;
    @Resource
    private ProjectNoticeService projectNoticeService;
    @Resource
    private ProjectPersonInfoService projectPersonInfoService;

    /**
     * 住户类型
     */
    private static final String PROJECT_TYPE = "2";
    /**
     * 员工类型
     */
    private static final String STAFF_TYPE = "1";


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Integer saveByUserIds(ProjectStaffNoticeAddVo projectStaffNoticeAddVo) {


        List<String> userIds = projectStaffNoticeAddVo.getUserIds();
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        projectStaffNoticeAddVo.setNoticeId(uuid);
        //批量获取员工信息

        List<ProjectStaff> projectStaffs = CollUtil.isNotEmpty(userIds) ? projectStaffService.listByIds(userIds) : new ArrayList<>();
        //消息列表
        List<ProjectStaffNoticeObject> projectStaffNoticeObjects = new ArrayList<>();
        //员工姓名列表
//        List<String> userNames = new ArrayList<>();
        StringBuilder userNames = new StringBuilder();
        AtomicBoolean skipAppend = new AtomicBoolean(false);
        projectStaffs.forEach(e -> {
            ProjectStaffNoticeObject projectStaffNoticeObject = new ProjectStaffNoticeObject();
            projectStaffNoticeObject.setNoticeId(projectStaffNoticeAddVo.getNoticeId());
            projectStaffNoticeObject.setUserId(e.getStaffId());
            projectStaffNoticeObject.setStatus(ReadStatusConstant.UnRead);
            projectStaffNoticeObject.setUserType(STAFF_TYPE);
            projectStaffNoticeObjects.add(projectStaffNoticeObject);
            if (userNames.length() + e.getStaffName().length() + 1 <= 200) {
                userNames.append("、").append(e.getStaffName());
            } else if (!skipAppend.get()){
                userNames.append("...");
                skipAppend.set(true);
            }
        });
        ProjectStaffNotice projectStaffNotice = new ProjectStaffNotice();

        BeanUtils.copyProperties(projectStaffNoticeAddVo, projectStaffNotice);
        if ("0".equals(projectStaffNoticeAddVo.getNoticeType())){
            projectStaffNotice.setSource("1");
        }
        if (userNames.length()>50){
            projectStaffNotice.setTarget(userNames.substring(0,50)+"...");
        }else {
            projectStaffNotice.setTarget(userNames.toString());
        }

        projectStaffNotice.setPubTime(LocalDateTime.now());
        boolean b = projectStaffNoticeObjectService.saveBatch(projectStaffNoticeObjects);
        // 获取当员工信息
        ProjectStaff staff = projectStaffService.getStaffByOwner();

        if (staff == null) {
            //消息队列发过来的信息没法获取用户信息
            if (SecurityUtils.getAuthentication() == null){
                projectStaffNotice.setSender("admin");
            }else {
                projectStaffNotice.setSender(SecurityUtils.getUser().getUsername());
            }
        } else {
            projectStaffNotice.setSender(staff.getStaffName());
        }

        return baseMapper.insert(projectStaffNotice);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void saveByPersonIds(ProjectPersonNoticeAddVo projectPersonNoticeAddVo) {
        List<String> userIds = projectPersonNoticeAddVo.getUserIds();
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        projectPersonNoticeAddVo.setNoticeId(uuid);
        //批量获取业主信息
        List<ProjectPersonInfo> projectPersonInfos = projectPersonInfoService.listByIds(userIds);
        //消息列表
        List<ProjectStaffNoticeObject> projectStaffNoticeObjects = new ArrayList<>();
        //员工姓名列表
        List<String> userNames = new ArrayList<>();
        projectPersonInfos.forEach(e -> {
            ProjectStaffNoticeObject projectStaffNoticeObject = new ProjectStaffNoticeObject();
            projectStaffNoticeObject.setNoticeId(projectPersonNoticeAddVo.getNoticeId());
            projectStaffNoticeObject.setUserId(e.getPersonId());
            projectStaffNoticeObject.setStatus(ReadStatusConstant.UnRead);
            projectStaffNoticeObject.setUserType(STAFF_TYPE);
            projectStaffNoticeObjects.add(projectStaffNoticeObject);
            userNames.add(e.getPersonName());
        });
        String userName = StringUtil.join(",", userNames);
        ProjectNotice projectNotice = new ProjectNotice();
        BeanUtils.copyProperties(projectPersonNoticeAddVo, projectNotice);
        if (userName.length()>50){
            projectNotice.setTarget(userName.substring(0,50)+"...");
        }else {
            projectNotice.setTarget(userName);
        }

        projectNotice.setPubTime(LocalDateTime.now());
        projectNotice.setContentType("1");
        projectNotice.setTargetType("[2,3]");
        projectStaffNoticeObjectService.saveBatch(projectStaffNoticeObjects);
        // 获取当员工信息
//        LambdaQueryWrapper<ProjectStaff> queryWrapper = Wrappers.lambdaQuery(ProjectStaff.class);
//
//        queryWrapper.eq(ProjectStaff::getUserId, SecurityUtils.getUser().getId());
//        queryWrapper.eq(ProjectStaff::getProjectId, ProjectContextHolder.getProjectId());
        projectNoticeService.save(projectNotice);
    }

    @Override
    public Page<ProjectStaffNoticeVo> getByStaff(Page page, ProjectStaffNoticeVo vo) {
        LambdaQueryWrapper<ProjectStaff> queryWrapper = Wrappers.lambdaQuery(ProjectStaff.class);

        queryWrapper.eq(ProjectStaff::getUserId, SecurityUtils.getUser().getId());
        queryWrapper.eq(ProjectStaff::getProjectId, ProjectContextHolder.getProjectId());

        ProjectStaff staff = projectStaffService.getOne(queryWrapper);

        if (staff == null) {
            return new Page<>();
        }

        return baseMapper.getByVo(page, staff.getStaffId(), vo.getStatus());
    }

    @Override
    public Integer countUnRead() {
        LambdaQueryWrapper<ProjectStaff> queryWrapper = Wrappers.lambdaQuery(ProjectStaff.class);

        queryWrapper.eq(ProjectStaff::getUserId, SecurityUtils.getUser().getId());
        queryWrapper.eq(ProjectStaff::getProjectId, ProjectContextHolder.getProjectId());

        ProjectStaff staff = projectStaffService.getOne(queryWrapper);

        if (staff == null) {
            return 0;
        }

        return baseMapper.countUnReadByStaffId(staff.getStaffId());
    }

    @Override
    public Page<ProjectStaffNoticeVo> pageByPerson(Page page, String userId) {
        return baseMapper.getByVo(page, userId, null);
    }

    @Override
    public Integer countByPersonId(String personId) {
        return baseMapper.countUnReadByPersonId(personId);
    }

    @Override
    public Integer countByStaffId(String staffId) {
        return baseMapper.countUnReadByStaffId(staffId);
    }

    @Override
    public void sendMessage(boolean isStaff, String noticeTitle, String content, List<String> userIds) {
        if (isStaff) {
            ProjectStaffNoticeAddVo projectStaffNoticeAddVo = new ProjectStaffNoticeAddVo();
            projectStaffNoticeAddVo.setUserIds(userIds);
            projectStaffNoticeAddVo.setPubTime(LocalDateTime.now());
            projectStaffNoticeAddVo.setNoticeTitle(noticeTitle);
            projectStaffNoticeAddVo.setContent(content);
            saveByUserIds(projectStaffNoticeAddVo);
        } else {
            ProjectPersonNoticeAddVo projectPersonNoticeAddVo = new ProjectPersonNoticeAddVo();
            projectPersonNoticeAddVo.setUserIds(userIds);
            projectPersonNoticeAddVo.setPubTime(LocalDateTime.now());
            projectPersonNoticeAddVo.setNoticeTitle(noticeTitle);
            //设置默认消息类型为3 1室内机 2为梯口区口
            projectPersonNoticeAddVo.setNoticeType("2");
            projectPersonNoticeAddVo.setContent(content);
            saveByPersonIds(projectPersonNoticeAddVo);
        }

    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendMessageOnId(String noticeId, boolean isStaff, String noticeTitle, String content, String userId) {
        if (isStaff) {

            ProjectStaffNotice projectStaffNotice = new ProjectStaffNotice();
            projectStaffNotice.setNoticeId(noticeId);
            projectStaffNotice.setPubTime(LocalDateTime.now());
            projectStaffNotice.setNoticeTitle(noticeTitle);
            projectStaffNotice.setContent(content);

            // 获取当员工信息
            ProjectStaff staff = projectStaffService.getStaffByOwner();
            ProjectStaffNoticeObject projectStaffNoticeObject = new ProjectStaffNoticeObject();
            projectStaffNoticeObject.setNoticeId(noticeId);
            projectStaffNoticeObject.setUserId(userId);
            projectStaffNoticeObject.setStatus(ReadStatusConstant.UnRead);
            projectStaffNoticeObject.setUserType(STAFF_TYPE);
            projectStaffNoticeObjectService.save(projectStaffNoticeObject);
            if (staff == null) {
                projectStaffNotice.setSender(SecurityUtils.getUser().getUsername());
            } else {
                projectStaffNotice.setSender(staff.getStaffName());
            }
            baseMapper.insert(projectStaffNotice);
        } else {
            ProjectNotice projectNotice = new ProjectNotice();
            projectNotice.setNoticeId(noticeId);
            projectNotice.setNoticeType("2");
            projectNotice.setNoticeTitle(noticeTitle);
            projectNotice.setContent(content);
            projectNotice.setPubTime(LocalDateTime.now());
            ProjectStaffNoticeObject projectStaffNoticeObject = new ProjectStaffNoticeObject();
            projectStaffNoticeObject.setNoticeId(noticeId);
            projectStaffNoticeObject.setUserId(userId);
            projectStaffNoticeObject.setTenantId(1);
            projectStaffNoticeObject.setStatus(ReadStatusConstant.UnRead);
            projectStaffNoticeObjectService.saveOrUpdate(projectStaffNoticeObject);
            projectNotice.setContentType("1");
            projectNotice.setTargetType("[2,3]");
            projectNotice.setTenantId(1);
            projectNoticeService.saveOrUpdate(projectNotice);
        }

    }
}
