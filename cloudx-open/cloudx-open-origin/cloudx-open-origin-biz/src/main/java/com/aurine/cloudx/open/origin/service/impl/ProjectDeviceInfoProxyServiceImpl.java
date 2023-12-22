
package com.aurine.cloudx.open.origin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.common.core.constant.DeviceTypeConstants;
import com.aurine.cloudx.open.origin.mapper.ProjectDeviceInfoMapper;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceInfo;
import com.aurine.cloudx.open.origin.entity.ProjectFrameInfo;
import com.aurine.cloudx.open.origin.vo.ProjectDeviceInfoFormVo;
import com.aurine.cloudx.open.origin.vo.ProjectDeviceInfoProxyVo;
import com.aurine.cloudx.open.origin.service.ProjectDeviceInfoProxyService;
import com.aurine.cloudx.open.origin.service.ProjectDeviceInfoService;
import com.aurine.cloudx.open.origin.service.ProjectFrameInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p> 设备信息代理服务实现类 </p>
 *
 * @ClassName: ProjectDeviceInfoProxyServiceImpl
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/21 12:44
 * @Copyright:
 */
@Service
public class ProjectDeviceInfoProxyServiceImpl extends ServiceImpl<ProjectDeviceInfoMapper, ProjectDeviceInfo> implements ProjectDeviceInfoProxyService {
    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;
    @Resource
    private ProjectFrameInfoService projectFrameInfoService;


    /**
     * 依照SN新增或删除设备
     *
     * @param deviceInfoProxyVo
     * @return
     */
    @Override
    public boolean saveOrUpdateDeviceByThirdPartyCode(ProjectDeviceInfoProxyVo deviceInfoProxyVo) {
        List<ProjectDeviceInfo> deviceList = this.baseMapper.selectList(new QueryWrapper<ProjectDeviceInfo>()
                .lambda().eq(ProjectDeviceInfo::getThirdpartyCode, deviceInfoProxyVo.getThirdpartyCode()));
        if (CollUtil.isNotEmpty(deviceList)) {
            //update
            ProjectDeviceInfo deviceInfo = deviceList.get(0);
            deviceInfoProxyVo.setDeviceId(deviceInfo.getDeviceId());
            return this.updateById(deviceInfoProxyVo);
        } else {
            //save
            return this.baseMapper.saveDeviceProxy(deviceInfoProxyVo) >= 1;
        }
    }

    /**
     * 获取全部
     *
     * @return
     */
    @Override
    public List<ProjectDeviceInfoProxyVo> listAllDevice(Integer projectId) {
        return this.baseMapper.listByProjectId(projectId);
    }

    /**
     * 获取全部
     *
     * @return
     */
    @Override
    public List<String> listByBuildingIdList(List<String> buildingIdList) {
        return baseMapper.listByBuildingIdList(buildingIdList);
    }

    /**
     * 获取全部区口机
     *
     * @return
     */
    @Override
    public List<ProjectDeviceInfo> listAllGateDevice() {

        ProjectDeviceInfoFormVo projectDeviceInfoFormVo = new ProjectDeviceInfoFormVo();

        List<String> typeList = new ArrayList<>();
        typeList.add(DeviceTypeConstants.GATE_DEVICE);

        projectDeviceInfoFormVo.setTypes(typeList);
        return projectDeviceInfoService.findByType(projectDeviceInfoFormVo);
    }

    /**
     * 获取全部公共，以及楼栋所在的组团的区口机
     *
     * @param buildingId
     * @return
     */
    @Override
    public List<ProjectDeviceInfo> listPublicGateAndGateByFrameDevice(String buildingId) {
        //根据楼栋ID,获取上一层框架ID，如果不存在上一层框架，则仅获取到公共区口机
        ProjectFrameInfo group4Frame = projectFrameInfoService.getParent(buildingId);

        List<ProjectDeviceInfo> resultList = new ArrayList<>();
        if (group4Frame != null) {
            resultList = projectDeviceInfoService.getListByDeviceEntityId(group4Frame.getEntityId());
        }

        return resultList;
    }

    /**
     * 根据楼栋ID或单元ID获取梯口机
     *
     * @param buildingId
     * @param unitId
     * @return
     */
    @Override
    public List<ProjectDeviceInfo> listLadderDevice(String buildingId, String unitId) {

        ProjectDeviceInfoFormVo projectDeviceInfoFormVo = new ProjectDeviceInfoFormVo();

        List<String> typeList = new ArrayList<>();
        typeList.add(DeviceTypeConstants.LADDER_WAY_DEVICE);

        projectDeviceInfoFormVo.setTypes(typeList);

        if (StringUtils.isNotEmpty(unitId)) {
            projectDeviceInfoFormVo.setUnitId(unitId);
        }

        if (StringUtils.isNotEmpty(buildingId)) {
            projectDeviceInfoFormVo.setBuildingId(buildingId);
        }
        return projectDeviceInfoService.findByType(projectDeviceInfoFormVo);
    }

    /**
     * 根据第三方ID,获取设备信息
     *
     * @param thirdPartyCode
     * @return
     */
    @Override
    public ProjectDeviceInfoProxyVo getByThirdPartyCode(String thirdPartyCode) {
        return this.baseMapper.getByThirdPartyCode(thirdPartyCode);
    }

    /**
     * 根据第三方ID,获取设备信息
     *
     * @param thirdpartyNo
     * @return
     */
    @Override
    public ProjectDeviceInfoProxyVo getByThirdPartyNo(String thirdpartyNo) {
        return this.baseMapper.getByThirdPartyNo(thirdpartyNo);
    }

    /**
     * 根据第三方ID,获取设备信息
     *
     * @param thirdPartyCode
     * @return
     */
    @Override
    public Integer countThirdPartyCode(String thirdPartyCode) {
        return this.baseMapper.countThirdPartyCode(thirdPartyCode);
    }

    /**
     * 根据第三方ID,获取设备信息
     *
     * @param deviceSn
     * @return
     */
    @Override
    public ProjectDeviceInfoProxyVo getByDeviceSn(String deviceSn) {
        return this.baseMapper.getByDeviceSn(deviceSn);
    }

    /**
     * 根据第三方ID,获取设备信息
     *
     * @param deviceId
     * @return
     */
    @Override
    public ProjectDeviceInfoProxyVo getVoById(String deviceId) {
        ProjectDeviceInfoProxyVo deviceInfo = this.baseMapper.getByDeviceId(deviceId);
        return deviceInfo;
    }
}
