package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.SysDeviceTypeThirdPartyConfig;
import com.aurine.cloudx.estate.entity.SysThirdPartyInterfaceConfig;
import com.aurine.cloudx.estate.mapper.SysThirdpartyInterfaceConfigMapper;
import com.aurine.cloudx.estate.service.SysDeviceTypeThirdPartyConfigService;
import com.aurine.cloudx.estate.service.SysThirdPartyInterfaceConfigService;
import com.aurine.cloudx.estate.vo.SysThirdPartyInterfaceConfigVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

/**
 * 第三方接口配置
 *
 * @author 王伟
 * @date 2020-08-27 14:20:04
 */
@Service
public class SysThirdPartyInterfaceConfigServiceImpl extends ServiceImpl<SysThirdpartyInterfaceConfigMapper, SysThirdPartyInterfaceConfig> implements SysThirdPartyInterfaceConfigService {


    @Resource
    private SysDeviceTypeThirdPartyConfigService sysDeviceTypeThirdPartyConfigService;

    /**
     * 获取平台级接口配置信息
     *
     * @param platformName 对接平台名称
     * @param tenantId     租户ID
     * @return
     */
    @Override
    public List<SysThirdPartyInterfaceConfig> listInterfaceConfig(String platformName, int tenantId) {
        return this.list(new QueryWrapper<SysThirdPartyInterfaceConfig>().lambda()
                .eq(StringUtils.isNotEmpty(platformName), SysThirdPartyInterfaceConfig::getName, platformName)
                .isNull(SysThirdPartyInterfaceConfig::getProjectId)
                .eq(SysThirdPartyInterfaceConfig::getTenantId, tenantId)
                .orderByAsc(SysThirdPartyInterfaceConfig::getName)
                .orderByAsc(SysThirdPartyInterfaceConfig::getVersion));
    }

    /**
     * 通过平台级接口 id获取配置详情
     * 如果当前项目是项目对接，且未创建，则自动复制一份项目配置模板，并返回
     * 如当前项目是项目级对接，且已创建当前项目配置，则自动返回当前项目的配置信息
     *
     * @param id
     * @return
     */
    @Override
    public SysThirdPartyInterfaceConfig getConfig(Integer projectId, String id) {
        SysThirdPartyInterfaceConfig config = this.getById(id);

        if ("2".equals(config.getType())) {//如果是项目级配置，则获取到的为模板数据
            List<SysThirdPartyInterfaceConfig> configlist = this.list(new QueryWrapper<SysThirdPartyInterfaceConfig>().lambda()
                    .eq(SysThirdPartyInterfaceConfig::getProjectId, projectId)
                    .eq(SysThirdPartyInterfaceConfig::getName, config.getName())
                    .eq(SysThirdPartyInterfaceConfig::getVersion, config.getVersion())
            );

            //当前项目无配置信息，则创建一个,否则返回项目级配置
            if (CollectionUtil.isEmpty(configlist)) {
                String uuid = UUID.randomUUID().toString().replace("-", "");
                SysThirdPartyInterfaceConfig sysThirdPartyInterfaceConfig = new SysThirdPartyInterfaceConfig();

                //根据模板创建一份项目配置信息
                BeanUtils.copyProperties(config, sysThirdPartyInterfaceConfig);
                sysThirdPartyInterfaceConfig.setThirdPartyNo(uuid);
                sysThirdPartyInterfaceConfig.setSeq(null);
                sysThirdPartyInterfaceConfig.setProjectId(projectId);

                this.save(sysThirdPartyInterfaceConfig);

                return sysThirdPartyInterfaceConfig;
            } else {
                //已存在项目配置信息，直接返回
                return configlist.get(0);
            }
        } else {
            return config;
        }
    }

    /**
     * 获取某个项目下，某个设备使用的平台级配置，如果这个设备使用的是项目级配置，则返回模板对象
     *
     * @param projectId
     * @param deviceTypeCode
     * @return
     */
    @Override
    public SysThirdPartyInterfaceConfig getConfigByDeviceType(Integer projectId, String deviceTypeCode) {

        //获取当前设备类型的项目默认配置
        List<SysDeviceTypeThirdPartyConfig> deviceTypeConfigList = sysDeviceTypeThirdPartyConfigService.list(new QueryWrapper<SysDeviceTypeThirdPartyConfig>().lambda()
                .eq(SysDeviceTypeThirdPartyConfig::getDeviceTypeId, deviceTypeCode)
                .eq(SysDeviceTypeThirdPartyConfig::getProjectId, projectId)
                .eq(SysDeviceTypeThirdPartyConfig::getTenantId, TenantContextHolder.getTenantId()));

        if (CollectionUtil.isNotEmpty(deviceTypeConfigList)) {
            SysDeviceTypeThirdPartyConfig deviceTypeThirdPartyConfig = deviceTypeConfigList.get(0);
            SysThirdPartyInterfaceConfig config = this.getById(deviceTypeThirdPartyConfig.getThirdPartyNo());

            //如果是项目级配置，获取模板对象，否则直接返回平台配置对象
            if ("2".equals(config.getType())) {

                List<SysThirdPartyInterfaceConfig> configlist = this.list(new QueryWrapper<SysThirdPartyInterfaceConfig>().lambda()
                        .isNull(SysThirdPartyInterfaceConfig::getProjectId)
                        .eq(SysThirdPartyInterfaceConfig::getName, config.getName())
                        .eq(SysThirdPartyInterfaceConfig::getVersion, config.getVersion())
                );

                return configlist.get(0);
            } else {
                return config;
            }

        } else {
            return null;
        }
    }

    /**
     * 保存第三方对接配置信息
     *
     * @param sysThirdPartyInterfaceConfigVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(SysThirdPartyInterfaceConfigVo sysThirdPartyInterfaceConfigVo) {

        //更新配置内容
        if ("1".equals(sysThirdPartyInterfaceConfigVo.getUpdateState())) {
            SysThirdPartyInterfaceConfig po = new SysThirdPartyInterfaceConfig();
            BeanUtils.copyProperties(sysThirdPartyInterfaceConfigVo, po);
            //如果配置项是平台级，不能存储projectId
            if(!"2".equals(po.getType())){
                po.setProjectId(null);
            }
            this.updateById(po);
        }


        //保存设备类型使用的配置信息
        SysDeviceTypeThirdPartyConfig sysDeviceTypeThirdPartyConfig = new SysDeviceTypeThirdPartyConfig();

        sysDeviceTypeThirdPartyConfig.setDeviceTypeId(sysThirdPartyInterfaceConfigVo.getDeviceType());
        sysDeviceTypeThirdPartyConfig.setIsDefault("1");
        sysDeviceTypeThirdPartyConfig.setProjectId(sysThirdPartyInterfaceConfigVo.getProjectId());
        sysDeviceTypeThirdPartyConfig.setTenantId(TenantContextHolder.getTenantId());
        sysDeviceTypeThirdPartyConfig.setThirdPartyNo(sysThirdPartyInterfaceConfigVo.getThirdPartyNo());

        return sysDeviceTypeThirdPartyConfigService.saveConfig(sysDeviceTypeThirdPartyConfig);
    }
}
