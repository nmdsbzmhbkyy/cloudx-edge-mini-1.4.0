package com.aurine.cloudx.estate.thirdparty.module.device.platform.other.service;

import com.aurine.cloudx.estate.dto.DeviceRegDto;
import com.aurine.cloudx.estate.dto.DeviceStatusDto;
import com.aurine.cloudx.estate.service.ProjectDeviceInfoService;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.DeviceService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgeDeviceInfoDTO;
import com.aurine.cloudx.estate.vo.MediaAdInfoVo;
import com.aurine.cloudx.estate.vo.ProjectDeviceGatherAlarmRuleVo;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoProxyVo;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 置空 V1 版本 对接业务实现
 *
 * @ClassName:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-07-31 13:44
 * @Copyright:
 */
@Service
public class DeviceServiceImplByOtherV1 implements DeviceService {

    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;


    /**
     * 新增设备
     *
     * @param device
     * @return
     */
    @Override
    public String addDevice(ProjectDeviceInfoProxyVo device) {
        return "";
    }


    @Override
    public String addDevice(ProjectDeviceInfoProxyVo device, String productKey) {
        return null;
    }

    /**
     * 批量新增设备
     *
     * @param deviceList
     * @return
     */
    @Override
    public String[] addDeviceBatch(List<ProjectDeviceInfoProxyVo> deviceList) {
        return new String[0];
    }

    /**
     * 批量新增设备
     * 新增设备时应根据设备类型进行分组传输，以确保不同的设备类型，调用正确的接口平台和版本(和addDeviceBatch(List<ProjectDeviceInfoProxyVo> deviceList)相比除了多了productKey其他完全一样)
     *
     * @param deviceList
     * @param productKey
     * @return
     */
    @Override
    public String[] addDeviceBatch(List<ProjectDeviceInfoProxyVo> deviceList, String productKey) {
        return new String[0];
    }

    /**
     * 修改设备
     *
     * @param device
     * @return
     */
    @Override
    public boolean updateDevice(ProjectDeviceInfoProxyVo device) {
        return true;
    }

    @Override
    public boolean getDeviceParam(String serviceId, String thirdpartyCode, String deviceId) {
        return true;
    }

    @Override
    public boolean setDeviceParameters(ObjectNode paramNode, String deviceId, String thirdpartyCode) {
        return true;
    }

    @Override
    public boolean getDeviceInfo(String deviceId, String thirdpartyCode) {
        return true;
    }

    /**
     * 删除设备
     *
     * @param deviceId
     * @return
     */
    @Override
    public boolean delDevice(String deviceId) {
        return true;
    }

    /**
     * 重启
     *
     * @param deviceId 设备id
     * @return
     */
    @Override
    public boolean reboot(String deviceId) {
        return true;
    }

    @Override
    public boolean callElevator(String deviceId, String roomCode, String floor, String liftDirect) {
        return false;
    }

    @Override
    public boolean reset(String deviceId) {
        return false;
    }

    @Override
    public boolean setPwd(String deviceId, String password) {
        return false;
    }

    /**
     * 获取版本
     *
     * @return
     */
    @Override
    public String getVersion() {
        return VersionEnum.V1.code;
    }

    /**
     * 获取平台类型
     *
     * @return
     */
    @Override
    public String getPlatform() {
        return PlatformEnum.OTHER.code;
    }


    /**
     * 同步产品
     *
     * @param projectId
     * @param tenantId
     * @return
     */
    @Override
    public boolean syncProduces(int projectId, int tenantId) {
        return true;
    }

    /**
     * 订阅消息
     *
     * @param deviceType
     * @param projectId
     * @param tenantId
     * @return
     */
    @Override
    public boolean subscribe(String deviceType, int projectId, int tenantId) {
        return false;
    }

    /***
     * 同步设备
     * @return
     * @param deviceType
     * @param projectId
     * @param tenantId
     */
    @Override
    public boolean syncDecvice(String deviceType, int projectId, int tenantId) {
        return false;
    }

    @Override
    public boolean dealAlarm(String eventId) {
        return false;
    }

    @Override
    public boolean sendMediaAd(String deviceId, MediaAdInfoVo media) {
        return false;
    }

    @Override
    public boolean cleanMediaAd(Long adSeq, String deviceId) {
        return false;
    }

    @Override
    public boolean setAccount(ProjectDeviceInfoProxyVo device) {
        return false;
    }

    @Override
    public boolean setGatherAlarmRule(ProjectDeviceInfoProxyVo device, ProjectDeviceInfoProxyVo parDevice, ProjectDeviceGatherAlarmRuleVo rule) {
        return false;
    }

    @Override
    public boolean regDevice(DeviceRegDto dto) {
        return false;
    }

    @Override
    public boolean statusChange(DeviceStatusDto dto) {
        return false;
    }

    @Override
    public boolean configDefaultParam(String deviceId) {
        return false;
    }

    @Override
    public boolean openAlways(String deviceId, Integer doorAction) {
        return false;
    }

    @Override
    public AurineEdgeDeviceInfoDTO getChannel(ProjectDeviceInfoProxyVo deviceInfo) {
        return null;
    }

    @Override
    public boolean operateFloor(String deviceId, String[] floors) {
        return false;
    }
}
