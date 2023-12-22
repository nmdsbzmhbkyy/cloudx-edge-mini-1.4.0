package com.aurine.cloudx.estate.openapi.sync.service.impl;

import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.config.SyncConfig;
import com.aurine.cloudx.estate.constant.enums.TableNameEnum;
import com.aurine.cloudx.estate.entity.EdgeCascadeProcessMaster;
import com.aurine.cloudx.estate.entity.OpenApiEntity;
import com.aurine.cloudx.estate.entity.ProjectStaff;
import com.aurine.cloudx.estate.openapi.enums.OpenApiCascadeTypeEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenApiOperateTypeEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenApiServiceNameEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenPushSubscribeCallbackTypeEnum;
import com.aurine.cloudx.estate.openapi.service.OpenApiMessageService;
import com.aurine.cloudx.estate.openapi.sync.service.SyncService;
import com.aurine.cloudx.estate.openapi.sync.util.SyncUtil;
import com.aurine.cloudx.estate.service.EdgeCascadeProcessMasterService;
import com.aurine.cloudx.estate.service.ProjectStaffService;
import com.aurine.cloudx.estate.vo.SysUserVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 员工信息表
 *
 * @author：zouyu
 * @data: 2022/4/8 10:48
 */
@Service
public class SyncStaffServiceImpl implements SyncService {

    @Resource
    private ProjectStaffService projectStaffService;

    @Resource
    private SyncConfig syncConfig;

    @Resource
    private OpenApiMessageService openApiMessageService;

    @Resource
    private EdgeCascadeProcessMasterService edgeCascadeProcessMasterService;

    @Override
    public void perform(Integer projectId, String projectUUID) {
        ProjectContextHolder.setProjectId(projectId);
        LambdaQueryWrapper<ProjectStaff> wrapper = Wrappers.lambdaQuery(ProjectStaff.class).eq(ProjectStaff::getProjectId, projectId);
        int count = projectStaffService.count(wrapper);
        int current = (int) Math.ceil(((double) count / syncConfig.getNumber()));
        //项目管理员的staffId
        List<String> administratorList = projectStaffService.getProjectAdministrator(projectId);
        for (int i = 1; i <= current; i++) {
            Page<ProjectStaff> page = projectStaffService.page(new Page<>(i, syncConfig.getNumber()),wrapper);
            //过滤项目管理员账号 不进行同步
            List<ProjectStaff> staffList = page.getRecords().stream().filter(projectStaff -> !administratorList.contains(projectStaff.getStaffId())).collect(Collectors.toList());

            staffList.forEach(e -> {
                OpenApiEntity openApiEntity = SyncUtil.getOpenApiEntityByOperate(projectUUID, OpenPushSubscribeCallbackTypeEnum.OPERATE.name, OpenApiOperateTypeEnum.ADD.name, OpenApiServiceNameEnum.STAFF_INFO.name, e);
                openApiMessageService.sendOpenApiMessage(openApiEntity);
            });
        }
        //更新状态
        EdgeCascadeProcessMaster edgeCascadeProcessMaster = edgeCascadeProcessMasterService.getOne(Wrappers.lambdaQuery(EdgeCascadeProcessMaster.class)
                .eq(EdgeCascadeProcessMaster::getServiceName, getVersion())
                .orderByDesc(EdgeCascadeProcessMaster::getCreateTime)
                .last("limit 1"));
        edgeCascadeProcessMaster.setStatus("1");
        edgeCascadeProcessMasterService.updateById(edgeCascadeProcessMaster);

        //传输对象
        OpenApiEntity openApiEntity = SyncUtil.getOpenApiEntityByOperate(projectUUID, OpenPushSubscribeCallbackTypeEnum.OPERATE.name, OpenApiCascadeTypeEnum.UPDATE.name, OpenApiServiceNameEnum.CASCADE_PROCESS_MASTER.name, edgeCascadeProcessMaster);
        openApiMessageService.sendOpenApiMessage(openApiEntity);
    }

    @Override
    public String getVersion() {
        return TableNameEnum.STAFF_INFO.code;
    }
}
