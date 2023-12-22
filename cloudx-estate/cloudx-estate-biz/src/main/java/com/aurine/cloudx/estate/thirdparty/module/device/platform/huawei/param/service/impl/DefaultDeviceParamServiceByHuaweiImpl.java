package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.param.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.param.service.AbstractParamServiceByHuawei;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.enums.DeviceManufactureEnum;
import com.aurine.cloudx.estate.vo.DevicesResultVo;
import com.aurine.cloudx.estate.vo.ProjectDeviceParamSetResultVo;
import com.aurine.cloudx.estate.vo.SysServiceParamConfVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>默认的设备参数设置</p>
 *
 * @author : 王良俊
 * @date : 2021-07-08 10:16:16
 */
@Service
public class DefaultDeviceParamServiceByHuaweiImpl extends AbstractParamServiceByHuawei {

    @Override
    public DevicesResultVo multiDeviceParamSetting(ObjectNode paramsNode, List<String> deviceIdList, List<String> serviceIdList) {
        if (paramsNode.isMissingNode() || CollUtil.isEmpty(serviceIdList) || CollUtil.isEmpty(deviceIdList)) {
            throw new RuntimeException("缺少必要参数无法进行本次设置");
        }

        log.info("[设备参数设置-{}] 开始批量设置设备参数，项目ID：{}，本次设备数量：{}台", getLogMark(), ProjectContextHolder.getProjectId(), deviceIdList.size());
        List<ProjectDeviceInfo> deviceInfoList = projectDeviceInfoService.list(new LambdaQueryWrapper<ProjectDeviceInfo>().in(ProjectDeviceInfo::getDeviceId, deviceIdList));
        Map<String, List<String>> failedMap = new HashMap<>();
        deviceInfoList.forEach(deviceInfo -> {
            Iterator<Map.Entry<String, JsonNode>> param = paramsNode.fields();
            param.forEachRemaining(nodeEntry -> {
                ProjectDeviceParamSetResultVo resultVo = this.sendDeviceParam(deviceInfo.getDeviceId(),
                        deviceInfo.getThirdpartyCode(), (ObjectNode) nodeEntry.getValue(), nodeEntry.getKey(), null);
                saveParamSetResult(failedMap, resultVo);
            });
        });

        return new DevicesResultVo(deviceInfoList.size() - failedMap.size(),
                failedMap.size(), deviceInfoList.size(), new ArrayList<>(failedMap.keySet()));
    }

    @Override
    protected void deviceParamPreHandle(String serviceId, ObjectNode paramObj) {

    }

    @Override
    public void deviceDataUpdate(String json, ProjectDeviceInfo deviceInfo) {

    }

    @Override
    public Set<DeviceManufactureEnum> getApplicableDeviceProducts() {
        Set<DeviceManufactureEnum> hashSet = new HashSet<>();
        hashSet.add(DeviceManufactureEnum.DEFAULT_DEVICE);
        return hashSet;
    }

    @Override
    protected List<SysServiceParamConfVo> getParamConfVoList(List<String> serviceIdList, String deviceId) {
        return sysProductServiceService.listParamConf(serviceIdList, deviceId);
    }

    @Override
    protected String getLogMark() {
        return "未知设备";
    }
}
