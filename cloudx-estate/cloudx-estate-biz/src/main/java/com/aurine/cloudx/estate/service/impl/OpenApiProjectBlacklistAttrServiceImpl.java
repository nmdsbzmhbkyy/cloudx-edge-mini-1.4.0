package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.common.core.constant.enums.DeviceTypeEnum;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.DataOriginEnum;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.dto.OpenApiProjectAddBlacklistFaceDto;
import com.aurine.cloudx.estate.entity.ProjectBlacklistAttr;
import com.aurine.cloudx.estate.entity.ProjectFaceResources;
import com.aurine.cloudx.estate.entity.SysThirdPartyInterfaceConfig;
import com.aurine.cloudx.estate.mapper.ProjectBlacklistAttrMapper;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.config.AurineEdgeConfigDTO;
import com.aurine.cloudx.estate.util.ShortUUIDUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: 顾文豪
 * @Date: 2023/11/9 11:19
 * @Package: com.aurine.openv2.service.impl
 * @Version: 1.0
 * @Remarks:
 **/
@Service
public class OpenApiProjectBlacklistAttrServiceImpl extends ServiceImpl<ProjectBlacklistAttrMapper, ProjectBlacklistAttr> implements OpenApiProjectBlacklistAttrService {
    @Resource
    private SysDeviceTypeThirdPartyConfigService sysDeviceTypeThirdPartyConfigService;
    @Resource
    private ProjectRightDeviceService projectRightDeviceService;
    @Resource
    private ProjectFaceResourcesService projectFaceResourcesService;
    @Resource
    private ProjectBlacklistAttrService projectBlacklistAttrService;

    /**
     * 保存黑名单人臉
     *
     * @param dto
     * @return
     * @auther:顾文豪
     * @since;2023-11-08 16:54
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R saveFaceBlacklist(OpenApiProjectAddBlacklistFaceDto dto) {
        // 校验是否已添加过该黑名单人脸
        LambdaQueryWrapper<ProjectBlacklistAttr> queryWrapper = new QueryWrapper<ProjectBlacklistAttr>().lambda()
                .eq(ProjectBlacklistAttr::getThirdFaceId, dto.getThirdFaceId())
                .eq(ProjectBlacklistAttr::getIsDeleted,0);
        List<ProjectBlacklistAttr> list =  this.list(queryWrapper);
        if (CollUtil.isNotEmpty(list)) {
            return R.failed("人脸黑名单已存在");
        }
        String faceCode = "";
        SysThirdPartyInterfaceConfig thirdPartyConfig = sysDeviceTypeThirdPartyConfigService.getConfigByProjectId(ProjectContextHolder.getProjectId());

        if (thirdPartyConfig != null && StringUtils.equals(thirdPartyConfig.getName(), PlatformEnum.AURINE_EDGE_MIDDLE.value)) {
            //获取默认的第三方前缀
            AurineEdgeConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceType(DeviceTypeEnum.GATE_DEVICE.getCode(), ProjectContextHolder.getProjectId(), 1, AurineEdgeConfigDTO.class);
            String pre = config.getFaceUuidPre();
            //拼接第三方ID
            String uuid = ShortUUIDUtil.shortUUID();
            // 传10代表是黑名单类型
            faceCode = String.format(pre, PersonTypeEnum.BLACKLIST.code, uuid);

        }
        ProjectFaceResources po = new ProjectFaceResources();
        po.setFaceCode(faceCode);
        po.setPicUrl(dto.getPicUrl());
        po.setOrigin(DataOriginEnum.THIRD_PARTY.code);
        po.setStatus("1");
        po.setPersonType(PersonTypeEnum.BLACKLIST.code);
        // 保存人脸资源库
        projectFaceResourcesService.save(po);

        // 保存黑名单属性
        ProjectBlacklistAttr blacklistAttr = new ProjectBlacklistAttr();
        blacklistAttr.setFaceId(po.getFaceId());
        blacklistAttr.setCredentialNo(dto.getCredentialNo());
        blacklistAttr.setMobile(dto.getMobile());
        blacklistAttr.setName(dto.getName());
        blacklistAttr.setThirdFaceId(dto.getThirdFaceId());
        //blacklistAttr.setProjectId(ProjectContextHolder.getProjectId());
        //blacklistAttr.setTenant_id(TenantContextHolder.getTenantId());
        projectBlacklistAttrService.save(blacklistAttr);

        // 保存凭证数据
        projectRightDeviceService.saveFaceBlacklistByFaceId(po.getFaceId());
        return R.ok("添加成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R delFaceBlacklist(String thirdFaceId) {
        // 从黑名单额外属性表找到对应在我们平台的人脸id
        LambdaQueryWrapper queryWrapper = new QueryWrapper<ProjectBlacklistAttr>().lambda()
                .eq(ProjectBlacklistAttr::getThirdFaceId, thirdFaceId)
                .eq(ProjectBlacklistAttr::getIsDeleted,0)
                .last("limit 1");
        ProjectBlacklistAttr blacklistAttr = getOne(queryWrapper);
        if (blacklistAttr == null) {
            return R.ok("删除成功");
        }

        // 删除黑名单额外属性表
        this.removeById(blacklistAttr.getId());
        //删除人脸资源表及凭证下发表数据
        projectFaceResourcesService.removeFace(blacklistAttr.getFaceId());
        return R.ok("删除成功");
    }
}
