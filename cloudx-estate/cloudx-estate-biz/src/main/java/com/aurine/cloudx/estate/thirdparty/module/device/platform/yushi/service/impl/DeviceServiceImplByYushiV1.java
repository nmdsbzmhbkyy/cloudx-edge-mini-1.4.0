package com.aurine.cloudx.estate.thirdparty.module.device.platform.yushi.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.constant.enums.DeviceStatusEnum;
import com.aurine.cloudx.estate.dto.DeviceRegDto;
import com.aurine.cloudx.estate.dto.DeviceStatusDto;
import com.aurine.cloudx.estate.entity.ProjectDeviceAttr;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.service.ProjectDeviceAttrService;
import com.aurine.cloudx.estate.service.ProjectDeviceInfoService;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.DeviceService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgeDeviceInfoDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.yushi.entity.request.YushiConnectDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.yushi.entity.respond.YushiResponse;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.yushi.enums.RespondEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.yushi.remote.factory.YushiRemoteDeviceServiceFactory;
import com.aurine.cloudx.estate.util.RedisUtil;
import com.aurine.cloudx.estate.vo.MediaAdInfoVo;
import com.aurine.cloudx.estate.vo.ProjectDeviceAttrVo;
import com.aurine.cloudx.estate.vo.ProjectDeviceGatherAlarmRuleVo;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoProxyVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.models.auth.In;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
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
public class DeviceServiceImplByYushiV1 implements DeviceService {

    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;
    @Resource
    private ProjectDeviceAttrService projectDeviceAttrService;


    /**
     * 新增设备
     *
     * @param device
     * @return
     */
    @Override
    public String addDevice(ProjectDeviceInfoProxyVo device) {
        List<ProjectDeviceInfo> deviceInfoList = projectDeviceInfoService.list(new LambdaQueryWrapper<ProjectDeviceInfo>().eq(ProjectDeviceInfo::getIpv4, device.getIpv4()));
        if (CollectionUtil.isNotEmpty(deviceInfoList)) {
            throw new RuntimeException("设备IP已存在！");
        }
        YushiConnectDTO connectDTO = YushiConnectDTO.builder().ip(device.getIpv4()).port(device.getPort())
                .userName(device.getCompanyAccount()).password(device.getCompanyPasswd()).build();

        try {
            // 获取设备信息
            JSONObject data = YushiRemoteDeviceServiceFactory.getInstance(VersionEnum.V1).systemDeviceInfo(connectDTO);
            device.setStatus(DeviceStatusEnum.ONLINE.code);

            // 订阅设备事件
            JSONObject data1 = YushiRemoteDeviceServiceFactory.getInstance(VersionEnum.V1).eventSubscribe(connectDTO);
            Integer subscribeId = data1.getInteger("ID");
            RedisUtil.set("yushi:subscribe:" + device.getDeviceId(), subscribeId, 3600);

            // 获取视频流rtsp
            JSONObject data2 = YushiRemoteDeviceServiceFactory.getInstance(VersionEnum.V1).videoLiveStream(connectDTO);
            ProjectDeviceAttrVo attrVo = new ProjectDeviceAttrVo();
            attrVo.setAttrName("视频流地址");
            attrVo.setAttrCode("videoUrl");
            attrVo.setAttrValue(data2.getString("URL"));
            device.setDeviceAttrList(new ArrayList<>());
            device.getDeviceAttrList().add(attrVo);

            // 返回设备序列号
            return data.getString("SerialNumber");
        } catch (Exception e) {
            e.printStackTrace();
            device.setStatus(DeviceStatusEnum.OFFLINE.code);
        }
        return null;
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
        YushiConnectDTO connectDTO = YushiConnectDTO.builder().ip(device.getIpv4()).port(device.getPort())
                .userName(device.getCompanyAccount()).password(device.getCompanyPasswd()).build();
        // 获取设备信息
        JSONObject data = null;
        try {
            data = YushiRemoteDeviceServiceFactory.getInstance(VersionEnum.V1).systemDeviceInfo(connectDTO);
            device.setStatus(DeviceStatusEnum.ONLINE.code);
            device.setThirdpartyCode(data.getString("SerialNumber"));
        } catch (Exception e) {
//            throw new RuntimeException(e);
            device.setStatus(DeviceStatusEnum.OFFLINE.code);
        }
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
        return PlatformEnum.YUSHI.code;
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
        return true;
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
