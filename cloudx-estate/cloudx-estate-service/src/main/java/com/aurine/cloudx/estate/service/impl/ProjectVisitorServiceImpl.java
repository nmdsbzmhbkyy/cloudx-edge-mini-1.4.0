package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.AuditStatusEnum;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.dto.ProjectPersonDeviceDTO;
import com.aurine.cloudx.estate.entity.ProjectVisitor;
import com.aurine.cloudx.estate.entity.ProjectVisitorHis;
import com.aurine.cloudx.estate.mapper.ProjectVisitorMapper;
import com.aurine.cloudx.estate.service.ProjectCardService;
import com.aurine.cloudx.estate.service.ProjectConfigService;
import com.aurine.cloudx.estate.service.ProjectDeviceInfoProxyService;
import com.aurine.cloudx.estate.service.ProjectFaceResourcesService;
import com.aurine.cloudx.estate.service.ProjectPersonDeviceService;
import com.aurine.cloudx.estate.service.ProjectRightDeviceService;
import com.aurine.cloudx.estate.service.ProjectVisitorHisService;
import com.aurine.cloudx.estate.service.ProjectVisitorService;
import com.aurine.cloudx.estate.util.NoticeUtil;
import com.aurine.cloudx.estate.vo.ProjectFaceResourcesAppVo;
import com.aurine.cloudx.estate.vo.ProjectPassDeviceVo;
import com.aurine.cloudx.estate.vo.ProjectVisitorRecordVo;
import com.aurine.cloudx.estate.vo.ProjectVisitorSearchConditionVo;
import com.aurine.cloudx.estate.vo.ProjectVisitorVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.api.feign.RemoteUserService;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 访客业务实现
 * </p>
 *
 * @ClassName: ProjectVisitorServiceImpl
 * @author: 王良俊
 * @date: 2020/6/4 10:11
 * @Copyright:
 */
@Slf4j
@Service
public class ProjectVisitorServiceImpl extends ServiceImpl<ProjectVisitorMapper, ProjectVisitor> implements ProjectVisitorService {

    @Resource
    private ProjectVisitorMapper projectVisitorMapper;
    @Resource
    private ProjectVisitorHisService projectVisitorHisService;
    @Resource
    private ProjectRightDeviceService projectRightDeviceService;
    @Resource
    private ProjectCardService projectCardService;
    @Resource
    private ProjectFaceResourcesService projectFaceResourcesService;
    @Resource
    private ProjectPersonDeviceService projectPersonDeviceService;
    @Resource
    private ProjectConfigService projectConfigService;
    @Resource
    private ProjectDeviceInfoProxyService projectDeviceInfoProxyService;
    @Resource
    private RemoteUserService remoteUserService;

    @Resource
    private NoticeUtil noticeUtil;


    @Override
    public Page<ProjectVisitorRecordVo> getPage(Page page, ProjectVisitorSearchConditionVo projectVisitorSearchConditionVo) {
//        this.signOffAll();
        return projectVisitorMapper.selectPage(page, projectVisitorSearchConditionVo);
    }

    @Override
    public Page<ProjectVisitorRecordVo> getPageByPerson(Page page, String personId, String status) {
        return projectVisitorMapper.getPageByPerson(page, status, personId, false);
    }

    @Override
    public Page<ProjectVisitorRecordVo> getPageByVisitor(Page page, Integer userId, String status, String date) {
        LocalDate localDate = null;
        if (StringUtils.isNotBlank(date)) {
            localDate = LocalDate.parse(date);
        }

        return projectVisitorMapper.getPageByVisitor(page, status, userId, localDate);
    }

    @Override
    public Page<ProjectVisitorRecordVo> getPageByCreate(Page page, String personId, String status) {

        return projectVisitorMapper.getPageByPerson(page, status, personId, true);
    }

    @Override
    public ProjectVisitor getVisitorByOwner() {
        return getOne(Wrappers.lambdaQuery(ProjectVisitor.class).eq(ProjectVisitor::getUserId, SecurityUtils.getUser().getId()));
    }

    @Override
    public void updatePhoneByUserId(String phone, Integer userId) {
        baseMapper.updatePhoneByUserId(phone, userId);
    }

    @Override
    public void updateUserIdByPhone(String phone, Integer userId) {

        baseMapper.updateUserIdByPhone(phone, userId);
    }







    @Override
    public List<ProjectVisitorRecordVo> getAllUnLeaveListToday() {
        return projectVisitorMapper.getAllUnLeaveListToday();
    }

    @Override
    public ProjectVisitorVo getDataById(String visitId) {
        ProjectVisitorVo reAuditDataById = projectVisitorMapper.getDataById(visitId);
        List<ProjectPassDeviceVo> projectPassDeviceVoList = projectPersonDeviceService.listDeviceByPersonId(visitId);
        if (CollUtil.isNotEmpty(projectPassDeviceVoList)) {
            String[] deviceIds = new String[projectPassDeviceVoList.size()];
            for (int i = 0; i < projectPassDeviceVoList.size(); i++) {
                deviceIds[i] = projectPassDeviceVoList.get(i).getDeviceId();
            }
            reAuditDataById.setDeviceIdArray(deviceIds);
        }
        String[] timeRange = {reAuditDataById.getPassBeginTime(), reAuditDataById.getPassEndTime()};
        reAuditDataById.setTimeRange(timeRange);
        ProjectFaceResourcesAppVo projectFaceResourcesAppVo = projectFaceResourcesService.getByPersonIdForApp(visitId, PersonTypeEnum.VISITOR.code);
        reAuditDataById.setPicUrl(projectFaceResourcesAppVo.getPicUrl());
        return reAuditDataById;
    }


}
