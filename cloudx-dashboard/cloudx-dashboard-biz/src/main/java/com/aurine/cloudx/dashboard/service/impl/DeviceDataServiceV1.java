package com.aurine.cloudx.dashboard.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.uniview.model.GetLiveUrlVo;
import com.aurine.cloudx.common.uniview.model.GetVideoResponse;
import com.aurine.cloudx.common.uniview.vcs.UniviewVcsClient;
import com.aurine.cloudx.dashboard.constant.ProductTypeEnum;
import com.aurine.cloudx.dashboard.constant.ServiceNameEnum;
import com.aurine.cloudx.dashboard.constant.VersionEnum;
import com.aurine.cloudx.dashboard.entity.ProjectDeviceInfo;
import com.aurine.cloudx.dashboard.exception.DashboardErrorEnum;
import com.aurine.cloudx.dashboard.exception.DashboardException;
import com.aurine.cloudx.dashboard.mapper.DeviceDataMapper;
import com.aurine.cloudx.dashboard.mapper.ProjectDeviceInfoMapper;
import com.aurine.cloudx.dashboard.service.AbstractDashboardService;
import com.aurine.cloudx.dashboard.util.JSONUtil;
import com.aurine.cloudx.dashboard.vo.DashboardRequestVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @version 1
 * @description: 获取设备数据
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-04-09
 * @Copyright:
 */
@Service
public class DeviceDataServiceV1 extends AbstractDashboardService {
    @Resource
    private DeviceDataMapper deviceDataMapper;
    @Resource
    private ProjectDeviceInfoMapper projectDeviceInfoMapper;
    @Value("${uniview.appid:25645212521}")
    private Long appid;

    @Value("${uniview.secretKey:65a4f27ee572d23e3e977e19a341708f}")
    private String secretKey;


    /**
     * 获取数据
     *
     * @param request
     * @return
     */
    @Override
    public JSONObject getData(DashboardRequestVo request) throws Exception {
        //参数检查
        if (StringUtils.isEmpty(request.getDeviceTypeCode()) || StringUtils.isEmpty(request.getDeviceSn())) {
            throw new DashboardException(DashboardErrorEnum.ARGUMENT_INVALID);
        }

        //根据设备类型进行分发
        ProductTypeEnum productTypeEnum = ProductTypeEnum.getProductCode(request.getDeviceTypeCode());
        if (productTypeEnum == null) throw new DashboardException(DashboardErrorEnum.ARGUMENT_INVALID);


        if (StringUtils.equals(productTypeEnum.productMiddleType, "0203")) {//门禁可视对讲类，从devieInfo查
            //门禁从设备表获取
            ProjectDeviceInfo deviceInfo = projectDeviceInfoMapper.selectOne(new QueryWrapper<ProjectDeviceInfo>().lambda().eq(ProjectDeviceInfo::getSn, request.getDeviceSn()));
            JSONObject json = JSONUtil.toJSONObject(deviceInfo);
            JSONObject resultJson = new JSONObject();

            json.keySet().forEach(e -> e = e.toUpperCase());
            for (String key : json.keySet()) {
                resultJson.put(key.toUpperCase(),json.get(key));
            }
            return resultJson;

        } else if (StringUtils.equals(productTypeEnum.productMiddleType, "0202")) {//监控摄像头类型， 返回摄像头URL地址

//            //门禁从设备表获取
            ProjectDeviceInfo deviceInfo = projectDeviceInfoMapper.selectOne(new QueryWrapper<ProjectDeviceInfo>().lambda().eq(ProjectDeviceInfo::getSn, request.getDeviceSn()));

            if (deviceInfo == null) throw new DashboardException(DashboardErrorEnum.EMPTY_RESULT);

            /**
             * 解析SN- SN=空间ID + 通道ID
             */

            if (request.getDeviceSn().split("_").length != 2) throw new DashboardException(DashboardErrorEnum.ARGUMENT_INVALID);

            String serial = request.getDeviceSn().split("_")[0];
            Integer channelNo = Integer.valueOf(request.getDeviceSn().split("_")[1]);

            UniviewVcsClient client = UniviewVcsClient.getInstance(appid, secretKey);
            GetLiveUrlVo getLiveUrlVo = new GetLiveUrlVo();
            getLiveUrlVo.setDeviceSerial(serial);
            getLiveUrlVo.setChannelNo(channelNo);
            getLiveUrlVo.setStreamIndex(0);
//            getLiveUrlVo.setType(GetVideoResponse.STREAM_TYPE_RTMP);
            getLiveUrlVo.setType(GetVideoResponse.STREAM_TYPE_HLS);
            System.out.println(client.getLiveUrl(getLiveUrlVo));

            JSONObject dataJson = new JSONObject();

            dataJson.put("URL", client.getLiveUrl(getLiveUrlVo).replaceAll("http://", "https://"));
            dataJson.put("DEVICENAME", deviceInfo.getDeviceName());

            return JSONUtil.toJSONObject(dataJson);
        } else {
            //其他设备，从Oracle创数表获取
            if (StringUtils.isEmpty(productTypeEnum.tableName)) {
                throw new DashboardException(DashboardErrorEnum.ARGUMENT_INVALID);
            }

            List<LinkedHashMap<String, Object>> dataMapList = deviceDataMapper.getDataByCode(productTypeEnum.tableName, request.getDeviceSn(), 1);
            if (CollUtil.isEmpty(dataMapList)) throw new DashboardException(DashboardErrorEnum.EMPTY_RESULT);

            Map<String, Object> dataMap = dataMapList.get(0);
            if (CollUtil.isEmpty(dataMap)) throw new DashboardException(DashboardErrorEnum.EMPTY_RESULT);

            return JSONUtil.toJSONObject(dataMap);
        }


    }

    @Data
    private class VO {
        @ApiModelProperty(value = "每日报修数量")
        private Long[] day;
        @ApiModelProperty(value = "日")
        private String[] dayAxis;

    }

    /**
     * 获取服务名称
     *
     * @return
     */
    @Override
    public String getServiceName() {
        return ServiceNameEnum.DEVICE_DATA.serviceName;
    }

    /**
     * 获取版本号
     *
     * @return
     */
    @Override
    public String getVersion() {
        return VersionEnum.V1.number;
    }


}
