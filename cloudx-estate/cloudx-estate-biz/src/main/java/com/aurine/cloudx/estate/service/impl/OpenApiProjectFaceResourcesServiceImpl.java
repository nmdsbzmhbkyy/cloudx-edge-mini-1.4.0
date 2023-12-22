package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.aurine.cloudx.common.core.constant.enums.DeviceTypeEnum;
import com.aurine.cloudx.common.core.exception.OpenApiServiceException;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.DataOriginEnum;
import com.aurine.cloudx.estate.constant.enums.OriginTypeEnum;
import com.aurine.cloudx.estate.constant.enums.PassRightTokenStateEnum;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.dto.OpenApiProjectAddBlacklistFaceDto;
import com.aurine.cloudx.estate.dto.OpenApiProjectFaceResourcesDto;
import com.aurine.cloudx.estate.entity.ProjectFaceResources;
import com.aurine.cloudx.estate.entity.SysThirdPartyInterfaceConfig;
import com.aurine.cloudx.estate.mapper.ProjectFaceResourcesMapper;
import com.aurine.cloudx.estate.service.OpenApiProjectFaceResourcesService;
import com.aurine.cloudx.estate.service.OpenApiProjectPersonDeviceService;
import com.aurine.cloudx.estate.service.SysDeviceTypeThirdPartyConfigService;
import com.aurine.cloudx.estate.service.adapter.AbstractProjectFaceResourcesService;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.config.AurineEdgeConfigDTO;
import com.aurine.cloudx.estate.util.ShortUUIDUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import com.pig4cloud.pigx.common.core.util.R;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: wrm
 * @Date: 2022/05/24 11:19
 * @Package: com.aurine.openv2.service.impl
 * @Version: 1.0
 * @Remarks:
 **/
@Service
public class OpenApiProjectFaceResourcesServiceImpl extends ServiceImpl<ProjectFaceResourcesMapper, ProjectFaceResources> implements OpenApiProjectFaceResourcesService {

    @Resource
    private AbstractProjectFaceResourcesService adapterWebProjectFaceResourcesService;
    @Resource
    private OpenApiProjectPersonDeviceService openApiProjectPersonDeviceService;
    @Resource
    private SysDeviceTypeThirdPartyConfigService sysDeviceTypeThirdPartyConfigService;

    /**
     * 保存人脸信息到数据库
     *
     * @param projectFaceResourcesDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<OpenApiProjectFaceResourcesDto> saveFaceInfo(OpenApiProjectFaceResourcesDto projectFaceResourcesDto) {
        String personTypeName = PersonTypeEnum.getType(projectFaceResourcesDto.getPersonType());

        ProjectFaceResources projectFaceResources = new ProjectFaceResources();
        BeanUtil.copyProperties(projectFaceResourcesDto, projectFaceResources);

        projectFaceResources.setStatus(PassRightTokenStateEnum.USED.code);
        projectFaceResources.setOrigin(OriginTypeEnum.OPEN_API.code);

        // 保存人脸信息以及关系, 人脸新增涉及生成faceCode调用4.0原有接口实现
        boolean saveFaceResourceResult = adapterWebProjectFaceResourcesService.saveFace(projectFaceResources);

        if (!saveFaceResourceResult) {
            throw new OpenApiServiceException(String.format("%s[%s]人脸信息保存失败", personTypeName, projectFaceResourcesDto.getPersonId()));
        }

        // 保存人脸通行权限
        Boolean savePassPermissionResult = openApiProjectPersonDeviceService.addPersonPassRightDevice(projectFaceResources.getPersonId(), projectFaceResources.getPersonType(), projectFaceResourcesDto.getPlanId());

        if (!savePassPermissionResult) {
            throw new OpenApiServiceException(String.format("%s[%s]人脸通行权限保存失败", personTypeName, projectFaceResourcesDto.getPersonId()));
        }

        // 返回结果
        OpenApiProjectFaceResourcesDto respFaceResourceDto = new OpenApiProjectFaceResourcesDto();
        BeanUtil.copyProperties(projectFaceResources, respFaceResourceDto);

        return R.ok(respFaceResourceDto);
    }

    /**
     * 批量删除
     * 根据人员id人脸信息,人脸关系以及人脸设备关系
     *
     * @param projectFaceResourcesDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<String> removeFaceInfo(OpenApiProjectFaceResourcesDto projectFaceResourcesDto) {
        String personTypeName = PersonTypeEnum.getType(projectFaceResourcesDto.getPersonType());

        LambdaQueryWrapper<ProjectFaceResources> wrapper = new QueryWrapper<ProjectFaceResources>().lambda()
                .eq(ProjectFaceResources::getPersonId, projectFaceResourcesDto.getPersonId());

        if (StringUtils.isNotEmpty(projectFaceResourcesDto.getFaceId())) {
            wrapper.eq(ProjectFaceResources::getFaceId, projectFaceResourcesDto.getFaceId());
        }

        // 获取人员人脸
        List<ProjectFaceResources> list = this.list(wrapper);

        // 批量删除人脸及人脸关系
        for (ProjectFaceResources projectFaceResources : list) {
            boolean removeFaceResult = adapterWebProjectFaceResourcesService.removeFace(projectFaceResources.getFaceId());

            if (!removeFaceResult) {
                throw new OpenApiServiceException(String.format("%s[%s]人脸删除失败", personTypeName, projectFaceResourcesDto.getPersonId()));
            }
        }

        return R.ok(projectFaceResourcesDto.getPersonId());
    }
}
