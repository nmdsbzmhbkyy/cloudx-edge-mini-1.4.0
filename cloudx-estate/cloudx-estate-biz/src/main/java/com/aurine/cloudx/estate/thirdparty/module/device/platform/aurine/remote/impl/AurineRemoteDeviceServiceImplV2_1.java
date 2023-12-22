package com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.remote.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.ErrorMessageConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.config.AurineConfigDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.config.AurineSubscribeDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.core.AurineDataConnector;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.core.util.AurineUtil;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.constant.AurineConstant;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.constant.AurineErrorEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.dto.AurineCertDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.dto.AurineMessageDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.dto.AurineRequestObject;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.vo.AurineCertVo;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.vo.AurineDeviceVo;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.remote.AurineRemoteDeviceService;
import com.aurine.cloudx.estate.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
public class AurineRemoteDeviceServiceImplV2_1 implements AurineRemoteDeviceService {
    @Resource
    AurineDataConnector aurineDataConnector;

    /**
     * 开门
     *
     * @param configDTO 配置对象
     * @param deviceSn  设备第三方编码
     * @return
     */
    @Override
    public boolean openDoor(AurineConfigDTO configDTO, String deviceSn, String personId, String personType) {
        //生成请求对象
        String uri = "device/opendoor";
        JSONObject requestJson = new JSONObject();

        requestJson.put("devsn", deviceSn);
        requestJson.put("modelid", MODEL_ID);

        //生成原生数据对象
        JSONObject requestObjectJson = new JSONObject();
        requestObjectJson.put("personId", personId);
        requestObjectJson.put("personType", personType);


        AurineRequestObject requestObject = AurineUtil.createPostRequestObject(configDTO, uri, requestJson, VersionEnum.V2_1.number, MediaType.MULTIPART_FORM_DATA_VALUE);
        requestObject.setRequestObjectJson(requestObjectJson);

        JSONObject resultJson = aurineDataConnector.send(requestObject);
        log.info("获取返回值：{}", resultJson);
        throwError(resultJson);
        /**
         * {"errorCode":0,"body":{"msgid":"1613786557439"},"errorMsg":"操作成功"}
         */
        String msgId = AurineConstant.MESSAGE_OPEN_DOOR_ID_PRE + resultJson.getJSONObject("body").getString("msgid");

        RedisUtil.set(msgId, requestObjectJson, 15);

        return true;
    }

    /**
     * 重启
     *
     * @param configDTO 配置对象
     * @param deviceSn  设备SN
     * @return
     */
    @Override
    public boolean reboot(AurineConfigDTO configDTO, String deviceSn) {
        //生成请求对象
        String uri = "device/restart";
        JSONObject requestJson = new JSONObject();

        requestJson.put("devsn", deviceSn);
        requestJson.put("modelid", MODEL_ID);

        AurineRequestObject requestObject = AurineUtil.createPostRequestObject(configDTO, uri, requestJson, VersionEnum.V2_1.number, MediaType.MULTIPART_FORM_DATA_VALUE);

        JSONObject resultJson = aurineDataConnector.send(requestObject);
        log.info("获取返回值：{}", resultJson);
        throwError(resultJson);
        return true;
    }

    /**
     * 添加设备
     *
     * @param configDTO 配置对象
     * @param deviceVo  设备对象
     * @return
     */
    @Override
    public String addDevice(AurineConfigDTO configDTO, AurineDeviceVo deviceVo) {
        //生成请求对象
        String uri = "device/register";
        JSONObject requestJson = new JSONObject();
        JSONArray deviceArray = new JSONArray();

        deviceVo.setModelid(MODEL_ID);
        deviceArray.add(deviceVo);

        requestJson.put("devices", deviceArray);


        AurineRequestObject requestObject = AurineUtil.createPostRequestObject(configDTO, uri, requestJson, VersionEnum.V2_1.number, MediaType.APPLICATION_JSON_VALUE);

        JSONObject resultJson = aurineDataConnector.send(requestObject);
        log.info("获取返回值：{}", resultJson);
        throwError(resultJson);
        JSONArray resultArray = resultJson.getJSONArray("body");

        //TODO: 等待二维码生成规则，如果二维码需要使用框架号，则需要在激活失败时返回异常，禁止添加设备，否则会导致设备先添加， 再上线激活，无法获取到框架信息 王伟 since 2020-09-16
        if (resultArray != null) {//对象不存在等情况
            List<JSONObject> resultList = resultArray.toJavaList(JSONObject.class);
            return resultList.get(0).getString("deviceNo");//返回设备框架号
        } else {
            return "";
        }
    }

    /**
     * 删除设备
     *
     * @param configDTO
     * @param deviceVo
     * @return
     */
    @Override
    public boolean delDevice(AurineConfigDTO configDTO, AurineDeviceVo deviceVo) {
        //生成请求对象
        String uri = "device/delete";
        JSONObject requestJson = new JSONObject();

        deviceVo.setModelid(MODEL_ID);

        requestJson.put("communityId", deviceVo.getCommunityId());
        requestJson.put("devsn", deviceVo.getDevsn());
        requestJson.put("modelid", MODEL_ID);


        AurineRequestObject requestObject = AurineUtil.createPostRequestObject(configDTO, uri, requestJson, VersionEnum.V2_1.number, MediaType.MULTIPART_FORM_DATA_VALUE);

        JSONObject resultJson = aurineDataConnector.send(requestObject);
        log.info("获取返回值：{}", resultJson);

        //处理返回值
        if (resultJson == null) {
            log.error("冠林中台 参数返回为空");
            throw new RuntimeException("接口未返回数据");
        }

        String errorCode = resultJson.getString("errorCode");
        String errorMsg = resultJson.getString("errorMsg");
        if (StringUtil.isNotEmpty(errorCode) && !"0".equals(errorCode)) {

            if (AurineErrorEnum.WRONG_OBJECT.code.equals(errorCode) || AurineErrorEnum.WRONG_OBJECT_AUTH.code.equals(errorCode)) {
                //凭证不存在，直接删除设备即可
            } else {
                throw new RuntimeException(errorMsg);
            }

        }

        throwError(resultJson);
        return true;
    }

    /**
     * 添加通行凭证
     *
     * @param certList
     * @return
     */
    @Override
    public JSONObject addCert(AurineConfigDTO configDTO, AurineDeviceVo deviceVo, List<AurineCertVo> certList) {

        //生成请求对象
        String uri = "device/key/add";
        JSONObject requestJson = new JSONObject();
        JSONArray certArray = new JSONArray();

        requestJson.put("devsn", deviceVo.getDevsn());
        requestJson.put("modelid", MODEL_ID);

        AurineCertDTO certDTO;
        List<AurineCertDTO> certDTOList = new ArrayList<>();

        for (AurineCertVo vo : certList) {
            certDTO = new AurineCertDTO();
            BeanUtils.copyProperties(vo, certDTO);
            certDTOList.add(certDTO);
        }

        requestJson.put("keys", certDTOList);


        AurineRequestObject requestObject = AurineUtil.createPostRequestObject(configDTO, uri, requestJson, VersionEnum.V2_1.number, MediaType.APPLICATION_JSON_VALUE);

        JSONObject resultJson = aurineDataConnector.send(requestObject);
        log.info("获取返回值：{}", resultJson);
        throwError(resultJson);

        return resultJson.getJSONObject("body");
    }

    /**
     * 删除通行凭证
     *
     * @param certList
     * @return
     */
    @Override
    public JSONObject delCert(AurineConfigDTO configDTO, AurineDeviceVo deviceVo, List<AurineCertVo> certList) {

        //生成请求对象
        String uri = "device/key/delete";
        JSONObject requestJson = new JSONObject();
        JSONArray certArray = new JSONArray();
//
//        certList.forEach(e -> {
//            e.setKeyvalue(null);
//            e.setUid(null);
//        });

        AurineCertDTO certDTO;
        List<AurineCertDTO> certDTOList = new ArrayList<>();

        for (AurineCertVo vo : certList) {
            certDTO = new AurineCertDTO();
            BeanUtils.copyProperties(vo, certDTO);
            certDTOList.add(certDTO);
        }

        requestJson.put("devsn", deviceVo.getDevsn());
        requestJson.put("modelid", MODEL_ID);
        requestJson.put("keys", certDTOList);

        AurineRequestObject requestObject = AurineUtil.createPostRequestObject(configDTO, uri, requestJson, VersionEnum.V2_1.number, MediaType.APPLICATION_JSON_VALUE);

        JSONObject resultJson = aurineDataConnector.send(requestObject);
        log.info("获取返回值：{}", resultJson);
        throwError(resultJson);


        //如果删除返回对象不存在，则添加异常代码到body，用于上一层直接删除
        String errorCode = resultJson.getString("errorCode");
        if (errorCode.equals(AurineErrorEnum.WRONG_OBJECT.code)) {
            JSONObject resultObj = new JSONObject();
            String uid = UUID.randomUUID().toString().replaceAll("-", "");
            resultObj.put("msgid", uid);
            resultObj.put("errorCode", resultJson.getString("errorCode"));
            resultObj.put("devsn", deviceVo.getDevsn());
            resultObj.put("modelid", MODEL_ID);

            resultJson.put("body", resultObj);//{"errorCode":10020,"errorMsg":"请求对象不存在","body":"{"errorCode":10020,"errorMsg":"请求对象不存在"}"}
        }

        return resultJson.getJSONObject("body");

    }

    /**
     * 订阅
     *
     * @param configDTO
     * @return
     */
    @Override
    public boolean subscribe(AurineConfigDTO configDTO) {
        //生成请求对象
        String uri = "device/event/subscribe";
        JSONObject requestJson = new JSONObject();
        JSONArray requestArray = new JSONArray();
        JSONObject requestArrayJson = new JSONObject();

        List<AurineSubscribeDTO> subscribeDTOList = configDTO.getSubscribe();

        for (AurineSubscribeDTO aurineSubscribe : subscribeDTOList) {
            if (aurineSubscribe.getEnable()) {
                requestArrayJson = new JSONObject();
                requestArrayJson.put("msgtype", aurineSubscribe.getType());
                requestArrayJson.put("resturl", aurineSubscribe.getUrl());
                requestArray.add(requestArrayJson);
            }
        }

        requestJson.put("subscribes", requestArray);
        AurineRequestObject requestObject = AurineUtil.createPostRequestObject(configDTO, uri, requestJson, VersionEnum.V2_1.number, MediaType.APPLICATION_JSON_VALUE);

        JSONObject resultJson = aurineDataConnector.send(requestObject);
        log.info("获取返回值：{}", resultJson);
        throwError(resultJson);

        if ("0".equals(resultJson.getString("errorCode"))) {
            return true;
        } else {
            return true;
        }
    }

    /**
     * 同步设备
     *
     * @param configDTO
     * @return
     */
    @Override
    public JSONObject syncDevice(AurineConfigDTO configDTO, int pageNo, int pageSize) {
        //生成请求对象
        String uri = "device/query";
        JSONObject requestJson = new JSONObject();


        requestJson.put("communityId", "S" + configDTO.getProjectId());
        if (pageNo != 0) {
            requestJson.put("pageno", pageNo);
        }
        if (pageSize != 0){
            requestJson.put("pagesize", pageSize);
        }

        AurineRequestObject requestObject = AurineUtil.createPostRequestObject(configDTO, uri, requestJson, VersionEnum.V2_1.number, MediaType.APPLICATION_JSON_VALUE);

        JSONObject resultJson = aurineDataConnector.send(requestObject);
        log.info("获取返回值：{}", resultJson);
        throwError(resultJson);
        return resultJson.getJSONObject("body");
    }

    @Override
    public JSONObject sendMessages(AurineConfigDTO configDTO, String deviceSn, List<AurineMessageDTO> messageList) {
        //生成请求对象
        String uri = "device/msg/add";
        JSONObject requestJson = new JSONObject();

        requestJson.put("communityId", "S" + configDTO.getProjectId());
        requestJson.put("devsn", deviceSn);
        requestJson.put("modelid", MODEL_ID);

        requestJson.put("msgs", JSONArray.parseArray(JSONArray.toJSONString(messageList)));

        AurineRequestObject requestObject = AurineUtil.createPostRequestObject(configDTO, uri, requestJson, VersionEnum.V2_1.number, MediaType.APPLICATION_JSON_VALUE);

        JSONObject resultJson = aurineDataConnector.send(requestObject);
        log.info("获取返回值：{}", resultJson);
        throwError(resultJson);
        return resultJson;
    }

    /**
     * 获取版本
     *
     * @return
     */
    @Override
    public String getVersion() {
        return VersionEnum.V2_1.code;
    }

    /**
     * 结果值抛出异常
     *
     * @param json
     */
    private boolean throwError(JSONObject json) {
        if (json == null) {
            log.error("[冠林中台] 参数返回为空");
            throw new RuntimeException("接口未返回数据");
        }
        String errorCode = json.getString("errorCode");
        String errorMsg = json.getString("errorMsg");
        if (StringUtil.isNotEmpty(errorCode) && !"0".equals(errorCode)) {

            if (AurineErrorEnum.WRONG_OBJECT.code.equals(errorCode) || AurineErrorEnum.WRONG_OBJECT_AUTH.code.equals(errorCode)) {
                errorMsg = ErrorMessageConstant.ERROR_DEVICE_NONE;
                return true;
            } else {
                throw new RuntimeException(errorMsg);

            }

        } else {
            return true;
        }
    }
}
