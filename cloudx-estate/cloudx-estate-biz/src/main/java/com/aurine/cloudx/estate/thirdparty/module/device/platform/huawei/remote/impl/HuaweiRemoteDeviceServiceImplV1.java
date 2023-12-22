package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.remote.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.service.SysDeviceTypeThirdPartyConfigService;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.config.HuaweiConfigDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.core.HuaweiDataConnector;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.core.util.HuaweiUtil;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.dto.HuaweiRequestObject;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.dto.HuaweiRespondDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.remote.HuaweiRemoteDeviceService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.util.HuaweiDeviceDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 对接实现，用于拼接对接参数等逻辑
 *
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-03
 * @Copyright:
 */
@Service
@Slf4j
public class HuaweiRemoteDeviceServiceImplV1 implements HuaweiRemoteDeviceService {
    @Resource
    HuaweiDataConnector huaweiDataConnector;

    @Resource
    private SysDeviceTypeThirdPartyConfigService sysDeviceTypeThirdPartyConfigService;

    /**
     * 新增一台设备
     *
     * @param configDTO
     * @param uuid
     * @return
     */
    @Override
    public HuaweiRespondDTO addDevice(HuaweiConfigDTO configDTO, String uuid) {
        return this.addDeviceBatch(configDTO, new String[]{uuid});
    }

    @Override
    public HuaweiRespondDTO addDevice(HuaweiConfigDTO configDTO, String uuid, String productKey) {
        return this.addDeviceBatch(configDTO, new String[]{uuid}, productKey);
    }

    /**
     * 批量新增多态设备
     *
     * @param configDTO
     * @param uuidArray
     * @return
     */
    @Override
    public HuaweiRespondDTO addDeviceBatch(HuaweiConfigDTO configDTO, String[] uuidArray) {

        //生成请求对象
        String uri = "device/add";
        JSONObject requestJson = new JSONObject();
        JSONObject uuidObjJson = new JSONObject();
        JSONArray uuidObjJsonArray = new JSONArray();

        for (String uuid : uuidArray) {
            uuidObjJson = new JSONObject();
            uuidObjJson.put("uuid", uuid);
            uuidObjJsonArray.add(uuidObjJson);
        }

        requestJson.put("devices", uuidObjJsonArray);

        HuaweiRequestObject requestObject = HuaweiUtil.createPostRequestObject(configDTO, uri, requestJson, getVersion());


        return HuaweiDeviceDataUtil.handelRespond(huaweiDataConnector.send(requestObject));
    }

    /**
     * 批量新增多态设备
     *
     * @param configDTO
     * @param uuidArray
     * @return
     */
    @Override
    public HuaweiRespondDTO addDeviceBatch(HuaweiConfigDTO configDTO, String[] uuidArray, String productKey) {

        //生成请求对象
        String uri = "device/add";
        JSONObject requestJson = new JSONObject();
        JSONObject uuidObjJson = new JSONObject();
        JSONArray uuidObjJsonArray = new JSONArray();

        for (String uuid : uuidArray) {
            uuidObjJson = new JSONObject();
            uuidObjJson.put("uuid", uuid);
            uuidObjJson.put("productKey", productKey);
            uuidObjJsonArray.add(uuidObjJson);
        }

        requestJson.put("devices", uuidObjJsonArray);

        HuaweiRequestObject requestObject = HuaweiUtil.createPostRequestObject(configDTO, uri, requestJson, getVersion());


        return HuaweiDeviceDataUtil.handelRespond(huaweiDataConnector.send(requestObject));
    }

    /**
     * 删除设备
     *
     * @param configDTO
     * @param deviceThirdId
     * @return
     */
    @Override
    public HuaweiRespondDTO delDevice(HuaweiConfigDTO configDTO, String deviceThirdId) {
        String uri = "device/" + deviceThirdId + "/del";

        HuaweiRequestObject requestObject = HuaweiUtil.createGetRequestObject(configDTO, uri, new JSONObject(), this.getVersion());
        return HuaweiDeviceDataUtil.handelRespond(huaweiDataConnector.send(requestObject));
    }

    /**
     * 查询产品列表
     *
     * @param configDTO
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public HuaweiRespondDTO queryProducts(HuaweiConfigDTO configDTO, Integer pageNo, Integer pageSize) {
        //init
        StringBuilder queryParams = new StringBuilder();
        if (pageNo != null && pageNo >= 1) {
            queryParams.append(queryParams.length() == 0 ? "?" : "&").append("pageNo=" + pageNo);
        }
        if (pageSize != null && pageSize >= 1) {
            queryParams.append(queryParams.length() == 0 ? "?" : "&").append("pageSize=" + pageSize);
        }

        //生成请求对象
        String uri = "products/query" + queryParams.toString();
        JSONObject requestJson = new JSONObject();

        HuaweiRequestObject requestObject = HuaweiUtil.createGetRequestObject(configDTO, uri, requestJson, getVersion());

        return HuaweiDeviceDataUtil.handelRespond(huaweiDataConnector.send(requestObject));
    }

    @Override
    public HuaweiRespondDTO queryDevices(HuaweiConfigDTO configDTO, Integer pageNo, Integer pageSize, String productId, String fDevId) {
        StringBuilder queryParams = new StringBuilder();
        if (pageNo != null && pageNo >= 1) {
            queryParams.append(queryParams.length() == 0 ? "?" : "&").append("pageNo=" + pageNo);
        }
        if (pageSize != null && pageSize >= 1) {
            queryParams.append(queryParams.length() == 0 ? "?" : "&").append("pageSize=" + pageSize);
        }
        if (StrUtil.isNotEmpty(productId)) {
            queryParams.append(queryParams.length() == 0 ? "?" : "&").append("productId=" + productId);
        }
        if (StrUtil.isNotEmpty(fDevId)) {
            queryParams.append(queryParams.length() == 0 ? "?" : "&").append("fDevId=" + fDevId);
        }
        //生成请求对象
        String uri = "device/query" + queryParams.toString();
        JSONObject requestJson = new JSONObject();

        HuaweiRequestObject requestObject = HuaweiUtil.createGetRequestObject(configDTO, uri, requestJson, getVersion());

        return HuaweiDeviceDataUtil.handelRespond(huaweiDataConnector.send(requestObject));
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
