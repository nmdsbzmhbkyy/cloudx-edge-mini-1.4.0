package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.estate.constant.enums.DeviceParamEnum;
import com.aurine.cloudx.estate.entity.ProjectDeviceParamInfo;
import com.aurine.cloudx.estate.mapper.ProjectDeviceParamInfoMapper;
import com.aurine.cloudx.estate.service.ProjectDeviceParamInfoService;
import com.aurine.cloudx.estate.vo.DeviceParamDataVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 设备参数信息表，存储设备的参数信息(ProjectDeviceParamInfo)表服务实现类
 *
 * @author 王良俊
 * @since 2020-12-15 10:24:15
 */
@Service
public class ProjectDeviceParamInfoServiceImpl extends ServiceImpl<ProjectDeviceParamInfoMapper, ProjectDeviceParamInfo>
        implements ProjectDeviceParamInfoService {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public DeviceParamDataVo getParamByDeviceId(String deviceId, String productId) {
        List<ProjectDeviceParamInfo> deviceParamInfoList = this.baseMapper.listValidDeviceParamInfo(deviceId, productId);
//        List<ProjectDeviceParamInfo> deviceParamInfoList = this.list(new QueryWrapper<ProjectDeviceParamInfo>().lambda()
//                .eq(ProjectDeviceParamInfo::getDeviceId, deviceId));
        if (CollUtil.isNotEmpty(deviceParamInfoList)) {
            DeviceParamDataVo deviceParamDataVo = new DeviceParamDataVo();
            List<String> paramList = deviceParamInfoList.stream().map(ProjectDeviceParamInfo::getDeviceParam).collect(Collectors.toList());
            List<String> serviceIdList = deviceParamInfoList.stream().map(ProjectDeviceParamInfo::getServiceId).collect(Collectors.toList());
            List<String> paramNameList = new ArrayList<>();
            // 这里将原本分开存储的参数json数据整合成一个json数据 （和前端传给后端的json数据格式一致）
            ObjectNode rootNode = objectMapper.createObjectNode();
            paramList.forEach(paramJson -> {
                try {
                    ObjectNode param = (ObjectNode) objectMapper.readTree(paramJson);
                    param.fields().forEachRemaining(item -> {
                        if (StrUtil.isNotEmpty(item.getKey())) {
                            // 这里设备编号参数比较特殊里面还带有另一个参数对象
                            rootNode.set(item.getKey(), item.getValue());
                            paramNameList.add(item.getKey());
                        }
                    });
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            });
            deviceParamDataVo.setParamData(rootNode.toString());
            deviceParamDataVo.setParamNameList(paramNameList);
            deviceParamDataVo.setServiceIdList(serviceIdList);
            System.out.println("dataJson:" + rootNode.toString());
            return deviceParamDataVo;
        }
        return null;
    }

}