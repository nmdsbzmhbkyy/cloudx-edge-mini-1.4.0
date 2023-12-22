package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.cert.vo.CertAdownRequestVO;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineEdgeErrorEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineEdgeServiceEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgeRespondDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.AurineEdgeDeviceCertVo;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.respond.CallBackData;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.respond.DevMessageData;
import lombok.extern.slf4j.Slf4j;

/**
 * 华为中台 数据处理工具类
 *
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-11-25
 * @Copyright:
 */
@Slf4j
public class AurineEdgeDeviceDataUtil {

    /**
     * 将 respond json转化为数据对象
     *
     * @param respondJson
     * @return
     */
    public static AurineEdgeRespondDTO handelRespond(JSONObject respondJson) {

        AurineEdgeRespondDTO respondDTO = new AurineEdgeRespondDTO();

        respondDTO.setErrorEnum(AurineEdgeErrorEnum.getByCode(respondJson.getString("errorCode")));
        respondDTO.setErrorMsg(respondJson.getString("errorMsg"));
        try {
            JSONObject respondObj = respondJson.getJSONObject("body");
            respondDTO.setBodyObj(respondObj);
        } catch (Exception e) {
            JSONArray respondArray = respondJson.getJSONArray("body");
            respondDTO.setBodyArray(respondArray);
        }

        return respondDTO;
    }

    /**
     * 设备删除将 respond json转化为数据对象
     *
     * @param respondJson
     * @return
     */
    public static AurineEdgeRespondDTO handelRespondDeleteDevice(JSONObject respondJson) {
        AurineEdgeRespondDTO respondDTO = new AurineEdgeRespondDTO();

        String devId = respondJson.getString("devId");
        if(StrUtil.isNotEmpty(devId)){
            respondDTO.setErrorEnum(AurineEdgeErrorEnum.SUCCESS);
            return respondDTO;
        }

        CallBackData callBackData = JSONObject.parseObject(respondJson.toJSONString(), CallBackData.class);
        if (callBackData == null) {
            log.error("[冠林边缘网关] 接收到事件信息格式错误:{}", respondJson);
            throw new RuntimeException("[冠林边缘网关] 接收到事件信息格式错误");
        }
        DevMessageData messageData = callBackData.getOnNotifyData();

        if (messageData == null) {
            log.error("[冠林边缘网关] 接收到事件信息格式错误:{}", respondJson);
            throw new RuntimeException("[冠林边缘网关] 接收到事件信息格式错误");
        }
        JSONObject param = messageData.getObjManagerData().getParam();
        String msg = param.getString("msg");
        String code = param.getString("code");

        respondDTO.setErrorEnum(AurineEdgeErrorEnum.getByCode(code));
        respondDTO.setErrorMsg(msg);

        return respondDTO;
    }



    /**
    * @Author 黄健杰
    * @Description 异步转同步请求相应对象
    * @Date  2022/2/9
    * @Param
    * @return
    **/
    public static CallBackData handelRespondForCallback(JSONObject respondJson){
        CallBackData callBackData = JSONObject.toJavaObject(respondJson,CallBackData.class);
        return callBackData;
    }

    /**
     * 转换数据
     *
     * @param aurineEdgeDeviceCertVo
     * @return
     */
    public static CertAdownRequestVO toVO(AurineEdgeDeviceCertVo aurineEdgeDeviceCertVo) {

        CertAdownRequestVO certAdownRequestVO = new CertAdownRequestVO();

        //优先级
        certAdownRequestVO.setPriotity(aurineEdgeDeviceCertVo.getPriotity());

        certAdownRequestVO.setAppId("1");
        certAdownRequestVO.setProjectId(ProjectContextHolder.getProjectId().toString());
        certAdownRequestVO.setDeviceName(aurineEdgeDeviceCertVo.getDeviceInfo().getDeviceName());
        certAdownRequestVO.setDeviceId(aurineEdgeDeviceCertVo.getDeviceInfo().getDeviceId());
        certAdownRequestVO.setPlatformId(PlatformEnum.AURINE_EDGE_MIDDLE.code);

        if(aurineEdgeDeviceCertVo.getCertType().equals(AurineEdgeServiceEnum.CERT_FACE.code)){
            certAdownRequestVO.setCertMediaType("1");
        }else if(aurineEdgeDeviceCertVo.getCertType().equals(AurineEdgeServiceEnum.CERT_CARD.code)){
            certAdownRequestVO.setCertMediaType("0");
        }

        JSONObject sendJson = new JSONObject();
        sendJson.put("aurineEdgeDeviceCertVo", aurineEdgeDeviceCertVo);

        certAdownRequestVO.setBody(sendJson);
        return certAdownRequestVO;
    }
}
