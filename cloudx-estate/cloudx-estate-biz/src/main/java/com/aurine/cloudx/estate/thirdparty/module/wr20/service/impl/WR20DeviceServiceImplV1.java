package com.aurine.cloudx.estate.thirdparty.module.wr20.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.dto.HuaweiRespondDTO;
import com.aurine.cloudx.estate.thirdparty.module.wr20.service.WR20DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-04
 * @Copyright:
 */
@Service
@Slf4j
public class WR20DeviceServiceImplV1 implements WR20DeviceService {
    @Resource
    private WR20RemoteService wr20RemoteService;

    /**
     * 同步设备
     *
     * @param projectId 项目ID
     * @return
     */
    @Override
    public boolean syncDevice(int projectId, String type) {
        JSONObject paramObj = new JSONObject();
        paramObj.put("DeviceType", type);
        HuaweiRespondDTO respondDTO = wr20RemoteService.syncDevice(projectId, paramObj,type);
        log.info("[WR20] 同步设备信息：{}", respondDTO);
        return true;
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


}
