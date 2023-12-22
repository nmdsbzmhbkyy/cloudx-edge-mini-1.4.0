package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.aurine.cloudx.common.core.exception.OpenApiServiceException;
import com.aurine.cloudx.estate.constant.VisitorIsLeaveConstant;
import com.aurine.cloudx.estate.constant.enums.AuditStatusEnum;
import com.aurine.cloudx.estate.constant.enums.OriginTypeEnum;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.dto.OpenApiProjectVisitorDto;
import com.aurine.cloudx.estate.entity.ProjectFaceResources;
import com.aurine.cloudx.estate.entity.ProjectVisitor;
import com.aurine.cloudx.estate.entity.ProjectVisitorHis;
import com.aurine.cloudx.estate.feign.OpenApiRemoteNewUserService;
import com.aurine.cloudx.estate.mapper.ProjectVisitorMapper;
import com.aurine.cloudx.estate.service.OpenApiProjectHousePersonRelService;
import com.aurine.cloudx.estate.service.OpenApiProjectVisitorService;
import com.aurine.cloudx.estate.service.ProjectFaceResourcesService;
import com.aurine.cloudx.estate.service.ProjectPersonDeviceService;
import com.aurine.cloudx.estate.service.ProjectVisitorHisService;
import com.aurine.cloudx.estate.service.adapter.AbstractProjectVisitorService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Author: wrm
 * @Date: 2022/05/23 14:10
 * @Package: com.aurine.openv2.service
 * @Version: 1.0
 * @Remarks:
 **/
@Service
public class OpenApiProjectVisitorServiceImpl extends ServiceImpl<ProjectVisitorMapper, ProjectVisitor> implements OpenApiProjectVisitorService {

    @Resource
    private OpenApiRemoteNewUserService openApiRemoteNewUserService;

    @Resource
    private ProjectPersonDeviceService projectPersonDeviceService;

    @Resource
    private ProjectFaceResourcesService projectFaceResourcesService;

    @Resource
    private ProjectVisitorHisService projectVisitorHisService;

    @Resource
    private OpenApiProjectHousePersonRelService openApiProjectHousePersonRelService;

    @Resource
    private AbstractProjectVisitorService adapterProjectVisitorServiceImpl;

    private static final String TIME_FORMAT = "yyyy-MM-dd HH:mm";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<OpenApiProjectVisitorDto> visitSave(OpenApiProjectVisitorDto openApiProjectVisitorDto) {

        // 判断被访人是否拥有对应房屋
        Boolean checkResult = openApiProjectHousePersonRelService.checkPersonOwnHouse(openApiProjectVisitorDto.getBeVisitorPersonId(), openApiProjectVisitorDto.getBeVisitorHouseId());

        if (!checkResult) {
            throw new OpenApiServiceException(String.format("申请的房屋Id[%s]不属于此被访人[%s]", openApiProjectVisitorDto.getBeVisitorHouseId(), openApiProjectVisitorDto.getBeVisitorPersonId()));
        }

        // 这里先生成访客记录的ID
        String visitId = UUID.randomUUID().toString().replaceAll("-", "");

        ProjectVisitor projectVisitor = new ProjectVisitor();
        BeanUtil.copyProperties(openApiProjectVisitorDto, projectVisitor);

        projectVisitor.setPersonName(openApiProjectVisitorDto.getVisitorName());

        // 根据手机号查询账号设置userId 如果没有则不做处理
        R<SysUser> requestUser = openApiRemoteNewUserService.innerGetUserByPhone(openApiProjectVisitorDto.getMobileNo(), "Y");

        if (requestUser.getCode() == 0 && ObjectUtil.isNotEmpty(requestUser.getData())) {
            projectVisitor.setUserId(requestUser.getData().getUserId());
        }

        // 获取访问的住户权限对应的通行设备列表
        String[] deviceIdArray = projectPersonDeviceService.listDeviceByPersonId(openApiProjectVisitorDto.getBeVisitorPersonId()).stream().map(r -> r.getDeviceId()).toArray(String[]::new);

        // 判断这个手机号是否已经有访客了，如果有的话不添加访客而是更新访客信息
        List<ProjectVisitor> visitorList = this.list(new QueryWrapper<ProjectVisitor>().lambda()
                .eq(ProjectVisitor::getMobileNo, openApiProjectVisitorDto.getMobileNo()));

        if (CollUtil.isEmpty(visitorList)) {
            boolean visitorSave = save(projectVisitor);
            if (!visitorSave) {
                throw new OpenApiServiceException("访客信息保存失败");
            }
        } else {
            projectVisitor.setVisitorId(visitorList.get(0).getVisitorId());
            updateById(projectVisitor);
        }

        String visitorId = projectVisitor.getVisitorId();

        DateTimeFormatter df = DateTimeFormatter.ofPattern(TIME_FORMAT);
        LocalDateTime effTime = LocalDateTime.parse(openApiProjectVisitorDto.getPassBeginTime(), df);
        LocalDateTime expTime = LocalDateTime.parse(openApiProjectVisitorDto.getPassEndTime(), df);

        if (openApiProjectVisitorDto.getPicUrl() != null) {
            openApiProjectVisitorDto.setIsFace("1");
        } else {
            openApiProjectVisitorDto.setIsFace("0");
        }

        // 保存人和设备关系 （这里访客的权限适合记录绑定的不在和访客绑定了）
        if (deviceIdArray.length > 0) {
            boolean b = projectPersonDeviceService.savePersonDevice(visitId, PersonTypeEnum.VISITOR, deviceIdArray, effTime, expTime);
            if (!b) {
                throw new OpenApiServiceException("保存设备关系失败");
            }
        }

        // 初始化要保存的访客记录数据
        ProjectVisitorHis projectVisitorHis = new ProjectVisitorHis();
        BeanUtil.copyProperties(openApiProjectVisitorDto, projectVisitorHis);

        projectVisitorHis.setVisitorId(visitorId);
        projectVisitorHis.setVisitPersonId(openApiProjectVisitorDto.getBeVisitorPersonId());
        projectVisitorHis.setVisitHouseId(openApiProjectVisitorDto.getBeVisitorHouseId());
        projectVisitorHis.setVisitorName(openApiProjectVisitorDto.getVisitorName());
        projectVisitorHis.setPassBeginTime(openApiProjectVisitorDto.getPassBeginTime());
        projectVisitorHis.setPassEndTime(openApiProjectVisitorDto.getPassEndTime());
        projectVisitorHis.setCreateTime(null);
        projectVisitorHis.setUpdateTime(null);
        projectVisitorHis.setAuditStatus(AuditStatusEnum.inAudit.code);
        projectVisitorHis.setIsLeave(VisitorIsLeaveConstant.UNLEAVE);
        projectVisitorHis.setVisitId(visitId);


        // 保存访客记录
        boolean save = projectVisitorHisService.save(projectVisitorHis);

        if (!save) {
            throw new OpenApiServiceException("访客记录保存失败");
        }

        // 对人脸进行增加或删除操作 （这里介质适合访客记录绑定的）
        if (openApiProjectVisitorDto.getPicUrl() != null) {
            List<ProjectFaceResources> faceResourcesList = new ArrayList<>();

            ProjectFaceResources faceResources = new ProjectFaceResources();
            faceResources.setPersonType(PersonTypeEnum.VISITOR.code);
            faceResources.setOrigin(OriginTypeEnum.OPEN_API.code);
            faceResources.setPicUrl(openApiProjectVisitorDto.getPicUrl());

            faceResourcesList.add(faceResources);

            projectFaceResourcesService.operateFace(faceResourcesList, visitId);
        }

        // 通过审核,保存关系下发设备操作等
        boolean passAudit = adapterProjectVisitorServiceImpl.passAudit(projectVisitorHis.getVisitId(), AuditStatusEnum.pass);

        if (!passAudit) {
            throw new OpenApiServiceException("访客记录保存失败");
        }

        openApiProjectVisitorDto.setVisitId(visitId);
        openApiProjectVisitorDto.setStartTime(openApiProjectVisitorDto.getPassBeginTime());
        openApiProjectVisitorDto.setEndTime(openApiProjectVisitorDto.getPassEndTime());

        return R.ok(openApiProjectVisitorDto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<OpenApiProjectVisitorDto> reRegister(OpenApiProjectVisitorDto openApiProjectVisitorDto) {
        // 根据访客id查询访客信息
        ProjectVisitor visitor = this.getById(openApiProjectVisitorDto.getVisitorId());

        // 合并结果
        OpenApiProjectVisitorDto visitorDto = new OpenApiProjectVisitorDto();

        BeanUtil.copyProperties(visitor, visitorDto);
        BeanUtil.copyProperties(openApiProjectVisitorDto, visitorDto, CopyOptions.create().setIgnoreNullValue(true));

        visitorDto.setVisitorName(visitor.getPersonName());
        visitorDto.setVisitId(null);

        // 进行访客请求申请
        return this.visitSave(visitorDto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<List<String>> signOff(List<String> visitIdList) {
        for (String visitId : visitIdList) {
            // 根据访客记录id获取访客信息
            ProjectVisitorHis byId = projectVisitorHisService.getById(visitId);

            if (ObjectUtil.isEmpty(byId)) {
                throw new OpenApiServiceException("未找到访客记录");
            }

            boolean signOff = adapterProjectVisitorServiceImpl.signOff(visitId);

            if (!signOff) {
                throw new OpenApiServiceException("访客签离失败");
            }
        }
        return R.ok(visitIdList);
    }

}