package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.estate.entity.SysServiceParamConf;
import com.aurine.cloudx.estate.mapper.SysServiceParamConfMapper;
import com.aurine.cloudx.estate.service.SysServiceParamConfService;
import com.aurine.cloudx.estate.vo.SysServiceParamConfVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 平台设备参数定义表(SysServiceParamConf)表服务实现类
 *
 * @author 王良俊
 * @since 2020-12-15 10:24:38
 */
@Service
public class SysServiceParamConfServiceImpl extends ServiceImpl<SysServiceParamConfMapper, SysServiceParamConf>
        implements SysServiceParamConfService {

    private static ObjectMapper objectMapper = new ObjectMapper();

    private static Map<String, String> pServiceIdDict = new HashMap();

    static {
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public String getFormParamDataByServiceId(List<String> serviceIdList) {
        Map<String, List<SysServiceParamConfVo>> paramMap = new HashMap<>();
        Map<String, String> serviceNameDict = new HashMap<>();
        List<SysServiceParamConfVo> paramConfVoList = baseMapper.listParamConfV2(serviceIdList);
        if (CollUtil.isNotEmpty(paramConfVoList)) {
            paramConfVoList.forEach(serviceParam -> {
                if (serviceParam.getServLevel() == 1) {
                    serviceNameDict.put(serviceParam.getServiceId(), serviceParam.getServiceName());
                }
                if (paramMap.get(serviceParam.getRootServiceId()) == null) {
                    List<SysServiceParamConfVo> paramConfList = new ArrayList<>();
                    paramConfList.add(serviceParam);
                    paramMap.put(serviceParam.getRootServiceId(), paramConfList);
                } else {
                    paramMap.get(serviceParam.getRootServiceId()).add(serviceParam);
                }
            });
            ArrayNode rootArrayNode = objectMapper.createArrayNode();
            paramMap.forEach((key, serviceParamConfVoList) -> {
                ObjectNode objectNode = objectMapper.createObjectNode();
                objectNode.put("serviceName", serviceNameDict.get(key));
                objectNode.put("serviceId", key);
                objectNode.putPOJO("formItems", serviceParamConfVoList);
                rootArrayNode.add(objectNode);
            });
            return rootArrayNode.toString();
        }
        return "";
    }

    @Override
    public List<String> getRebootServiceIdList() {
        return baseMapper.getRebootServiceIdList();
    }

    @Override
    public List<String> getValidServiceIdList(List<String> serviceIdList) {
        List<SysServiceParamConf> list = this.list(new QueryWrapper<SysServiceParamConf>().lambda().in(SysServiceParamConf::getServiceId, serviceIdList).select(SysServiceParamConf::getServiceId));
        return list.stream().map(SysServiceParamConf::getServiceId).distinct().collect(Collectors.toList());
    }

}