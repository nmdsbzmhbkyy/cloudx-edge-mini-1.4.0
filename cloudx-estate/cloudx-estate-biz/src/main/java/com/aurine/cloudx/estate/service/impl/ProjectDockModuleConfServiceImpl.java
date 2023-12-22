package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.constant.enums.DeviceTypeEnum;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.DockModuleEnum;
import com.aurine.cloudx.estate.entity.ProjectDockModuleConf;
import com.aurine.cloudx.estate.entity.SysThirdPartyInterfaceConfig;
import com.aurine.cloudx.estate.mapper.ProjectDockModuleConfMapper;
import com.aurine.cloudx.estate.service.ProjectBuildingInfoService;
import com.aurine.cloudx.estate.service.ProjectDeviceInfoService;
import com.aurine.cloudx.estate.service.ProjectDockModuleConfService;
import com.aurine.cloudx.estate.service.SysDeviceTypeThirdPartyConfigService;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.factory.DeviceFactoryProducer;
import com.aurine.cloudx.estate.thirdparty.module.wr20.factory.WR20Factory;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoProxyVo;
import com.aurine.cloudx.estate.vo.ProjectDockModuleConfBaseVo;
import com.aurine.cloudx.estate.vo.ProjectDockModuleConfWR20Vo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p> 第三方配置</p>
 *
 * @ClassName: WebProjectDockModuleConfServiceImpl
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-12-15 13:51
 * @Copyright:
 */
@Service
@Slf4j
public class ProjectDockModuleConfServiceImpl extends ServiceImpl<ProjectDockModuleConfMapper, ProjectDockModuleConf> implements ProjectDockModuleConfService {

    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;
    @Resource
    private ProjectBuildingInfoService projectBuildingInfoService;
    @Resource
    private SysDeviceTypeThirdPartyConfigService sysDeviceTypeThirdPartyConfigService;


    /**
     * 保存WR20配置信息
     *
     * @param dockModuleConfWR20Vo
     * @return
     */
    @Override
    public boolean saveWR20(ProjectDockModuleConfWR20Vo dockModuleConfWR20Vo) {
        //检查当前项目下是否已存在配置

        ProjectDockModuleConfWR20Vo wr20Conf = this.getConfigByProjectId(dockModuleConfWR20Vo.getProjectId(), DockModuleEnum.WR20.code, ProjectDockModuleConfWR20Vo.class);

        if (wr20Conf != null) {
            throw new RuntimeException("当前项目已存在配置，请删除后再添加");
        }


        //调用第三方业务
        ProjectDeviceInfoProxyVo deviceInfoProxyVo = new ProjectDeviceInfoProxyVo();


        //创建虚拟设备，模拟WR20网关
        deviceInfoProxyVo.setSn(dockModuleConfWR20Vo.getSn());
        deviceInfoProxyVo.setDeviceType(DeviceTypeEnum.WR20_DEVICE.getCode());
        deviceInfoProxyVo.setProjectId(dockModuleConfWR20Vo.getProjectId());
        deviceInfoProxyVo.setDeviceName("WR20网关");
        deviceInfoProxyVo.setTenantId(1);


        SysThirdPartyInterfaceConfig configGate = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceType(DeviceTypeEnum.GATE_DEVICE.getCode(), dockModuleConfWR20Vo.getProjectId(), 1);
        SysThirdPartyInterfaceConfig configLadder = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceType(DeviceTypeEnum.LADDER_WAY_DEVICE.getCode(), dockModuleConfWR20Vo.getProjectId(), 1);

        //梯口机和区口机都需要配置为华为中台
        if (configGate == null || configLadder == null) {
            throw new RuntimeException("请检查 [设备对接] 配置中 [梯口机]、[区口机] 是否配置为 [华为中台]");
        } else {
            if (!configGate.getName().equals(PlatformEnum.HUAWEI_MIDDLE.value) || !configLadder.getName().equals(PlatformEnum.HUAWEI_MIDDLE.value)) {
                throw new RuntimeException("请检查 [设备对接] 配置中 [梯口机]、[区口机] 是否配置为 [华为中台]");
            }
        }

        String thirdCode = DeviceFactoryProducer.getFactory(dockModuleConfWR20Vo.getProjectId()).getDeviceService().addDevice(deviceInfoProxyVo);
        if (StringUtils.isEmpty(thirdCode)) {
            throw new RuntimeException("请检查 [设备对接] 配置中 [梯口机]、[区口机] 是否配置为 [华为中台]");
        }

        dockModuleConfWR20Vo.setThirdCode(thirdCode);
        dockModuleConfWR20Vo.setModuleId(DockModuleEnum.WR20.code);
        dockModuleConfWR20Vo.setModuleName(DockModuleEnum.WR20.name);


        ProjectDockModuleConf conf = new ProjectDockModuleConf();
        conf.setModuleId(DockModuleEnum.WR20.code);
        conf.setModuleName(DockModuleEnum.WR20.name);
        conf.setJsonConfig(JSONObject.toJSONString(dockModuleConfWR20Vo));
        conf.setIsActive(dockModuleConfWR20Vo.getIsActive());
        conf.setProjectId(dockModuleConfWR20Vo.getProjectId());

        if (wr20Conf == null) {//add
            return this.save(conf);
        } else {//update
            conf.setSeq(wr20Conf.getSeq());
            return this.updateById(conf);
        }


    }

    /**
     * 删除WR20配置
     *
     * @return
     */
    @Override
    public boolean delWR20(Integer projectId) {
        List<ProjectDockModuleConf> list = this.list(new QueryWrapper<ProjectDockModuleConf>().lambda().eq(ProjectDockModuleConf::getProjectId, projectId));
        if (CollUtil.isNotEmpty(list)) {
            //确认是否存在设备，楼栋，房屋，如果有设备禁止删除
            Integer currProjectId = ProjectContextHolder.getProjectId();

            ProjectContextHolder.setProjectId(projectId);
            int deviceCount = projectDeviceInfoService.count();
            int buildingCount = projectBuildingInfoService.countBuilding();
            ProjectContextHolder.setProjectId(currProjectId);

            if (deviceCount >= 1) {
                throw new RuntimeException("当前项目已存在设备，请全部清空后再删除WR20配置");
            }

            if (buildingCount >= 1) {
                throw new RuntimeException("当前项目已存在楼栋，请全部清空后再删除WR20配置");
            }

            return this.removeById(list.get(0));
        }
        return false;
    }

    /**
     * 同步WR20数据
     *
     * @param projectId
     * @param type      1：框架 2：设备 3：住户员工权限
     * @return
     */
    @Override
    public boolean syncWr20(Integer projectId, String type) {
        if ("1".equals(type)) {
            return WR20Factory.getFactoryInstance().getFrameService(projectId).syncFrame(projectId);
        } else if ("2".equals(type)) {
            WR20Factory.getFactoryInstance().getDeviceService(projectId).syncDevice(projectId, "1");
            return  WR20Factory.getFactoryInstance().getDeviceService(projectId).syncDevice(projectId, "2");
//            WR20Factory.getFactoryInstance().getDeviceService(projectId).syncDevice(projectId, "4");
//            return
        } else if ("3".equals(type)) {
            return WR20Factory.getFactoryInstance().getPersonService(projectId).syncPerson(projectId);
        }
        return false;
    }

    /**
     * 通过projectId和配置对象，获取组装好的配置参数
     *
     * @param projectId
     * @param t
     * @return
     */
    @Override
    public <T extends ProjectDockModuleConfBaseVo> T getConfigByProjectId(int projectId, String moduleId, Class<T> t) {
        List<ProjectDockModuleConf> dockModuleConfList = this.list(new QueryWrapper<ProjectDockModuleConf>().lambda()
                .eq(ProjectDockModuleConf::getProjectId, projectId)
                .eq(ProjectDockModuleConf::getModuleId, moduleId));

        if (CollUtil.isNotEmpty(dockModuleConfList)) {
            return getConfig(dockModuleConfList.get(0), t);
        } else {
            return null;
        }
    }

    /**
     * 通过第三方编号，获取对应的配置信息
     *
     * @param moduleId
     * @param t
     * @return
     */
    @Override
    public <T extends ProjectDockModuleConfBaseVo> T getConfigByThirdCode(String moduleId, String thirdCode, Class<T> t) {
        List<ProjectDockModuleConf> confList = this.list(new QueryWrapper<ProjectDockModuleConf>().lambda().eq(ProjectDockModuleConf::getModuleId, moduleId).like(ProjectDockModuleConf::getJsonConfig, thirdCode));
        if (CollUtil.isNotEmpty(confList)) {
            return this.getConfig(confList.get(0), t);
        } else {
            return null;
        }
    }


    private <T extends ProjectDockModuleConfBaseVo> T getConfig(ProjectDockModuleConf dockModuleConf, Class<T> t) {
        String configJsonStr = dockModuleConf.getJsonConfig();


        if (StringUtils.isEmpty(configJsonStr)) {
            log.error("项目 {} 下的第三方配置信息为空", dockModuleConf.getProjectId());
            throw new RuntimeException("未找到配置");
        }

        if (!JSONUtil.isJsonObj(configJsonStr)) {
            log.error("项目 {} 下的第三方配置信息格式错误", dockModuleConf.getProjectId());
            throw new RuntimeException("配置格式错误");
        }

        try {
            T resultDTO = JSONObject.parseObject(dockModuleConf.getJsonConfig(), t);
            resultDTO.setProjectId(dockModuleConf.getProjectId());
            resultDTO.setModuleId(dockModuleConf.getModuleId());
            resultDTO.setModuleName(dockModuleConf.getModuleName());
            return resultDTO;

        } catch (Exception e) {
            log.error("项目 {} 下的第三方配置格式错误:{}", dockModuleConf.getProjectId(), dockModuleConf);
            e.printStackTrace();
            throw new RuntimeException("配置格式错误");
        }
    }
}
