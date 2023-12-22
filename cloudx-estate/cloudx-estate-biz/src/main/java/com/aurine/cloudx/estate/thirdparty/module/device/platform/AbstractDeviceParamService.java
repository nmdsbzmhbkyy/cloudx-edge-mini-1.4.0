package com.aurine.cloudx.estate.thirdparty.module.device.platform;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.estate.constant.enums.DeviceParamEnum;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectDeviceParamInfo;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.factory.DeviceFactoryProducer;
import com.aurine.cloudx.estate.thirdparty.module.device.handler.DeviceParamDataHandleChain;
import com.aurine.cloudx.estate.util.ObjectMapperUtil;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>实现了获取参数表单数据方法的抽象类</p>
 *
 * @author : 王良俊
 * @date : 2021-10-21 14:14:37
 */
@Service
public abstract class AbstractDeviceParamService implements DeviceParamConfigService, DeviceParamDataService, DeviceParamFormService {

    @Resource
    protected SysServiceParamConfService sysServiceParamConfService;
    @Resource
    protected ProjectDeviceParamInfoService projectDeviceParamInfoService;
    @Resource
    protected SysDeviceTypeThirdPartyConfigService sysDeviceTypeThirdPartyConfigService;
    @Resource
    protected ProjectDeviceInfoService projectDeviceInfoService;
    @Resource
    protected SysProductServiceService sysProductServiceService;
    @Resource
    protected ProjectEntityLevelCfgService projectEntityLevelCfgService;
    @Resource
    protected KafkaTemplate<String, String> kafkaTemplate;
    @Resource
    protected DeviceParamDataHandleChain deviceParamDataHandleChain;

    protected ObjectMapper objectMapper = ObjectMapperUtil.instance();

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * <p>用于区分当前实现和其他实现的日志</p>
     *
     * @return 设备参数设置-{} {}的内容
     * @author: 王良俊
     */
    protected abstract String getLogMark();

    /**
     * <p>设备参数发送给设备之前的处理</p>
     *
     * @param serviceId 参数的serviceId
     * @param paramObj  参数的json对象节点
     */
    protected abstract void deviceParamPreHandle(String serviceId, ObjectNode paramObj);

    @Override
    public String getParamFormJson(List<String> serviceIdList, String deviceId) throws JsonProcessingException {
        List<DeviceParamForm> formParamDataByServiceId = getDeviceParamFormOriginData(getParamConfVoList(serviceIdList, deviceId));
        return objectMapper.writeValueAsString(adjustFormData(formParamDataByServiceId, deviceId));
    }

    /**
     * <p>决定设备表单参数的数据源（这里是默认实现）</p>
     *
     * @param deviceId      设备ID
     * @param serviceIdList 参数服务ID列表
     * @return 参数服务配置Vo对象
     */
    protected List<SysServiceParamConfVo> getParamConfVoList(List<String> serviceIdList, String deviceId) {
        return sysServiceParamConfService.listParamConf(serviceIdList);
    }

    protected ProjectDeviceParamInfo initProjectDeviceParamInfo(String deviceId, String serviceId, ObjectNode paramNode) {
        ProjectDeviceParamInfo deviceParamInfo = projectDeviceParamInfoService.getOne(new QueryWrapper<ProjectDeviceParamInfo>().lambda()
                .eq(ProjectDeviceParamInfo::getDeviceId, deviceId).eq(ProjectDeviceParamInfo::getServiceId, serviceId).last("limit 1"));
        if (deviceParamInfo == null) {
            deviceParamInfo = new ProjectDeviceParamInfo();
            deviceParamInfo.setDeviceId(deviceId);
            deviceParamInfo.setServiceId(serviceId);
        }
        deviceParamInfo.setDeviceParam(paramNode.toString());
        return deviceParamInfo;
    }

    /**
     * <p>对参数设置结果进行处理</p>
     *
     * @param resultMap 存放参数设置结果的map key是设备ID
     * @param resultVo  参数设置结果VO
     */
    protected void saveParamSetResult(Map<String, List<String>> resultMap, ProjectDeviceParamSetResultVo resultVo) {
        List<String> idList = resultMap.get(resultVo.getDeviceId());
        if (idList == null) {
            idList = new ArrayList<>();
            idList.add(resultVo.getServiceId());
            resultMap.put(resultVo.getDeviceId(), idList);
        } else {
            idList.add(resultVo.getServiceId());
        }
    }

    // 设备表单生成相关

    /**
     * <p>生成没有带任何第三方限制的原始表单json数据</p>
     *
     * @return 表单json数据
     */
    protected final List<DeviceParamForm> getDeviceParamFormOriginData(List<SysServiceParamConfVo> paramConfVoList) {
        Map<String, List<DeviceParamForm.FormItem>> paramMap = new HashMap<>();
        Map<String, String> serviceNameDict = new HashMap<>();
        // 这里查询的结果所有参数都已经根据servLeve进行排序了，所以子参数对象肯定排在父参数对象后面 避免了父参数的其他参数显示在子参数分类后面
        if (CollUtil.isNotEmpty(paramConfVoList)) {
            List<SysServiceParamConfVo> titleParamList = paramConfVoList.stream()
                    .filter(sysServiceParamConfVo -> "title".equals(sysServiceParamConfVo.getInputType()))
                    .collect(Collectors.toList());
            Map<String, DeviceParamForm.FormItem> titleMap = new HashMap<>();
            if (CollUtil.isNotEmpty(titleParamList)) {
                paramConfVoList.removeAll(titleParamList);
                titleParamList.forEach(sysServiceParamConfVo -> {
                    DeviceParamForm.FormItem formItem = DeviceParamForm.FormItem.createFormItem(sysServiceParamConfVo);
                    titleMap.put(sysServiceParamConfVo.getParamId(), formItem);
                });
            }

            paramConfVoList.forEach(serviceParam -> {
                if (serviceParam.getServLevel() == 1) {
                    serviceNameDict.put(serviceParam.getServiceId(), serviceParam.getServiceName());
                    paramMap.computeIfAbsent(serviceParam.getRootServiceId(), key -> new ArrayList<>()).add(DeviceParamForm.FormItem.createFormItem(serviceParam));
                } else {
                    // 这里把子参数放入对应的title参数对象中用于前端分类展示 （这里数据库已经进行排序了所以一定已经存在父参数对象-父参数对象比子参数对象先添加）
                    List<DeviceParamForm.FormItem> formItemList = paramMap.get(serviceParam.getRootServiceId());
                    Optional<DeviceParamForm.FormItem> title = formItemList.stream()
                            .filter(paramConfVo -> paramConfVo.getParamId().equals(serviceParam.getParParamId())).findFirst();
                    if (title.isPresent()) {
                        DeviceParamForm.FormItem formItem = title.get();
                        formItem.getFormItemList().add(DeviceParamForm.FormItem.createFormItem(serviceParam));
                    } else {
                        DeviceParamForm.FormItem titleParam = titleMap.get(serviceParam.getParParamId());
                        titleParam.getFormItemList().add(DeviceParamForm.FormItem.createFormItem(serviceParam));
                        formItemList.add(titleParam);
                    }
                }
            });
            List<DeviceParamForm> deviceParamFormList = new ArrayList<>();
            paramMap.forEach((key, formItemList) -> {
                deviceParamFormList.add(new DeviceParamForm(serviceNameDict.get(key), key, formItemList));
            });
            return deviceParamFormList;
        }
        return new ArrayList<>();
    }

    /**
     * <p>如果需要完整的参数才能设置则调用此方法（可同步设置的参数不能直接取出来单独设置的情况）</p>
     *
     * @param originJsonNode 设备的完整参数
     * @param newJsonNode    本次要同步的参数json（将这个json中的数据更新到设备原参数json中）
     */
    protected static void recursivelyJsonTree(ObjectNode originJsonNode, ObjectNode newJsonNode) {
        newJsonNode.fields().forEachRemaining(nodeEntry -> {
            // 如果是对象节点则继续进行递归操作
            if (nodeEntry.getValue().isObject()) {
                recursivelyJsonTree(originJsonNode, (ObjectNode) nodeEntry.getValue());
            } else {
                ObjectNode parent = originJsonNode.findParent(nodeEntry.getKey());
                if (parent != null) {
                    parent.set(nodeEntry.getKey(), nodeEntry.getValue());
                }
            }
        });
    }

    /**
     * <p>有需要就重写这个方法，对表单数据进行调整</p>
     */
    protected List<DeviceParamForm> adjustFormData(List<DeviceParamForm> paramFormList, String deviceId) {
        return paramFormList;
    }

    /**
     * 发送消息到kafka
     *
     * @param topic   主题
     * @param message 内容体
     */
    protected <T> void sendMsg(String topic, T message) {
        try {
            kafkaTemplate.send(topic, objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>下发设备参数到设备上，一次性设置一种参数</p>
     *
     * @param thirdPartyCode 设备第三方编码
     * @param deviceId       设备ID（非第三方）
     * @param paramNode      设备参数节点
     * @param serviceId      参数服务ID
     * @return 该参数设置结果对象
     */
    protected ProjectDeviceParamSetResultVo sendDeviceParam(String deviceId, String thirdPartyCode, ObjectNode paramNode, String serviceId, List<ProjectDeviceParamInfo> deviceParamInfoList) {
        deviceParamPreHandle(serviceId, paramNode);

        ProjectDeviceParamSetResultVo resultVo;
        try {
            boolean isSuccess = DeviceFactoryProducer.getFactory(deviceId).getDeviceService().setDeviceParameters(paramNode, deviceId, thirdPartyCode);

            resultVo = new ProjectDeviceParamSetResultVo(deviceId, thirdPartyCode, serviceId, isSuccess);
            // 这里如果设备参数设置成功则更新本地数据库里面的参数数据
            if (deviceParamInfoList == null) {
                deviceParamInfoList = new ArrayList<>();
            }
            if (isSuccess) {
                deviceParamInfoList.add(initProjectDeviceParamInfo(deviceId, serviceId, paramNode));
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultVo = new ProjectDeviceParamSetResultVo(deviceId, thirdPartyCode, serviceId, false);
        }
        return resultVo;
    }

    /**
     * <p>用于需要多个参数同时一起设置的情况或是一次性设置多种参数（单独设置都会导致别的参数设置失败，比如导致设备重启的参数）</p>
     *
     * @param thirdPartyCode 设备第三方编码
     * @param deviceId       设备ID（非第三方）
     * @param paramNode      设备参数节点
     * @return 这些参数的设置结果
     */
    protected List<ProjectDeviceParamSetResultVo> sendDeviceParam(String deviceId, String thirdPartyCode, ObjectNode paramNode,
                                                                  List<ProjectDeviceParamInfo> deviceParamInfoList) {

        Iterator<Map.Entry<String, JsonNode>> fields = paramNode.fields();
        fields.forEachRemaining(nodeEntry -> {
            if (!nodeEntry.getValue().isMissingNode() && nodeEntry.getValue().isObject()) {
                deviceParamPreHandle(nodeEntry.getKey(), (ObjectNode) nodeEntry.getValue());
            }
        });

        List<ProjectDeviceParamSetResultVo> resultVoList = new ArrayList<>();

        try {
            boolean isSuccess = DeviceFactoryProducer.getFactory(deviceId).getDeviceService().setDeviceParameters(paramNode, deviceId, thirdPartyCode);
            // 这里如果设备参数设置成功则更新本地数据库里面的参数数据
            if (isSuccess) {
                paramNode.fields().forEachRemaining(nodeEntry -> {
                    resultVoList.add(new ProjectDeviceParamSetResultVo(deviceId, thirdPartyCode, nodeEntry.getKey(), isSuccess));

                    ObjectNode objectNode = objectMapper.createObjectNode();
                    objectNode.set(nodeEntry.getKey(), nodeEntry.getValue());
                    deviceParamInfoList.add(this.initProjectDeviceParamInfo(deviceId, nodeEntry.getKey(), objectNode));
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            paramNode.fields().forEachRemaining(nodeEntry -> {
                resultVoList.add(new ProjectDeviceParamSetResultVo(deviceId, thirdPartyCode, nodeEntry.getKey(), false));
            });
        }
        return resultVoList;
    }

    @Override
    public DeviceParamDataVo getParamByDeviceId(String deviceId, String productId) {
        List<ProjectDeviceParamInfo> deviceParamInfoList = this.projectDeviceParamInfoService.listValidDeviceParamInfo(deviceId, productId);
        if (CollUtil.isNotEmpty(deviceParamInfoList)) {
            DeviceParamDataVo deviceParamDataVo = new DeviceParamDataVo();
            List<String> paramList = deviceParamInfoList.stream().map(ProjectDeviceParamInfo::getDeviceParam).collect(Collectors.toList());
            List<String> serviceIdList = deviceParamInfoList.stream().map(ProjectDeviceParamInfo::getServiceId).collect(Collectors.toList());
            List<String> paramNameList = new ArrayList<>();
            // 这里将原本分开存储的参数json数据整合成一个json数据 （和前端传给后端的json数据格式一致）
            ObjectNode rootNode = objectMapper.createObjectNode();
            paramList.forEach(paramJson -> {
                if (StrUtil.isNotEmpty(paramJson)) {
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

    /**
     * <p>可能需要对设备参数的数据进行调整比如添加一个临时参数（最后设置的时候不带有这个参数）</p>
     *
     * @param paramData 返回的参数数据
     * @return 调整过的参数数据
     * @author 王良俊
     */
    /*private DeviceParamDataVo convertDeviceParamData(DeviceParamDataVo paramData) {
        try {
            JsonNode jsonNode = deviceParamDataHandleChain.handle(paramData.getServiceIdList(), paramData.getParamData(), getPlateForm(), true);
            paramData.setParamData(jsonNode.toString());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return paramData;
    }*/

    /**
     * <p>可能需要对设备参数的数据进行调整比如添加一个临时参数（最后设置的时候不带有这个参数）</p>
     *
     * @param paramsNode 要进行还原的参数数据
     * @return 调整过的参数数据
     * @author 王良俊
     */
/*    private ObjectNode revertDeviceParamData(ObjectNode paramsNode, List<String> serviceIdList) {
        try {
            JsonNode jsonNode = deviceParamDataHandleChain.handle(serviceIdList, paramsNode.toString(), getPlateForm(), false);
            return (ObjectNode) jsonNode;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return paramsNode;
    }*/

    @Override
    public List<ProjectDeviceParamSetResultVo> setDeviceParam(ObjectNode paramsNode, String deviceId) {
        return this.deviceParamSetting(paramsNode, deviceId);
    }

    @Override
    public DevicesResultVo setMultiDeviceParam(ObjectNode paramsNode, List<String> deviceIdList, List<String> serviceIdList) {
        return multiDeviceParamSetting(paramsNode, deviceIdList, serviceIdList);
    }

    protected abstract List<ProjectDeviceParamSetResultVo> deviceParamSetting(ObjectNode paramsNode, String deviceId);

    protected abstract DevicesResultVo multiDeviceParamSetting(ObjectNode paramsNode, List<String> deviceIdList, List<String> serviceIdList);
}
