package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.callback.handler;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.constant.DeviceTypeConstants;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.cert.constant.ADownStateEnum;
import com.aurine.cloudx.estate.cert.controller.CertAdownController;
import com.aurine.cloudx.estate.cert.remote.RemoteCertAdownService;
import com.aurine.cloudx.estate.cert.vo.CertAdownRequestVO;
import com.aurine.cloudx.estate.component.chain.AbstractHandler;
import com.aurine.cloudx.estate.component.chain.annotation.ChainHandler;
import com.aurine.cloudx.estate.constant.enums.PassRightCertDownloadStatusEnum;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectRightDevice;
import com.aurine.cloudx.estate.mapper.ProjectRightDeviceMapper;
import com.aurine.cloudx.estate.service.ProjectDeviceInfoService;
import com.aurine.cloudx.estate.service.ProjectFaceResourcesService;
import com.aurine.cloudx.estate.service.ProjectRightDeviceCacheService;
import com.aurine.cloudx.estate.service.ProjectRightDeviceService;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.ResponseOperateConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.TopicConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.ResponseOperateDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.config.AurineEdgeConfigDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineEdgeCacheConstant;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineEdgeFaceResultCodeEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineEdgeServiceEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgeRequestObject;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.AurineEdgeDeviceCertVo;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.respond.CallBackData;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.util.AurineEdgeDeviceDataUtil;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.enums.DeviceCapabilitiesEnum;
import com.aurine.cloudx.estate.util.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 新增面部信息回调处理器
 *
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021/10/14 11:34
 * @Copyright:
 */
@ChainHandler(chainClass = AurineEdgeObjectManageChain.class)
@Component
@Slf4j
public class AurineEdgeObjectManageFaceManageHandler extends AbstractHandler<CallBackData> {

    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;
    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;
    @Resource
    private ProjectRightDeviceMapper projectRightDeviceMapper;
    @Resource
    private ProjectFaceResourcesService projectFaceResourcesService;
    @Resource
    private ProjectRightDeviceService projectRightDeviceService;
//    @Resource
//    private RemoteCertAdownService remoteCertAdownService;
    @Resource
    private ProjectRightDeviceCacheService projectRightDeviceCacheService;
    @Resource
    private CertAdownController certAdownController;


    /**
     * handler校验函数，不满足该函数则跳过处理
     *
     * @param handleEntity
     * @return
     */
    @Override
    public boolean filter(CallBackData handleEntity) {

        if (!StringUtils.equals(AurineEdgeServiceEnum.CERT_FACE.code, handleEntity.getOnNotifyData().getObjManagerData().getServiceId())) {
            return false;
        }

//        if (!StringUtils.equals(AurineEdgeActionConstant.ADD, handleEntity.getOnNotifyData().getObjManagerData().getAction())) {
//            return false;
//        }

        log.info("[冠林边缘网关] {} 开始处理人脸添加返回结果", handleEntity);
        return true;
    }


    /**
     * 执行方法，执行后返回调用next继续下一组handle，调用done则结束处理
     *
     * @param handleEntity
     */
    @Override
    public boolean doHandle(CallBackData handleEntity) {

        boolean b = "ADD".equals(handleEntity.getOnNotifyData().getObjManagerData().getAction());
        if(!b){
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {

            ResponseOperateDTO responseOperateDTO = new ResponseOperateDTO();
            responseOperateDTO.setCompany(PlatformEnum.AURINE_EDGE_MIDDLE.value);

            //执行下发成功的设备
            String deviceThirdCode = handleEntity.getOnNotifyData().getDevId();
            ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getByThirdPartyCode(deviceThirdCode);

            //获取下发的设备
            if (deviceInfo == null) {
                log.error("[冠林边缘网关] 人脸下发结果处理失败，未找到下发的设备：deviceThirdCode = {}", deviceThirdCode);
                return done();
            }
            responseOperateDTO.setDeviceInfo(deviceInfo);

            //获取下发的人脸
            JSONObject objInfo = handleEntity.getOnNotifyData().getObjManagerData().getObjInfo();
            String passNo = objInfo.getJSONObject("doorAccess").getString("passNo");
//            ProjectFaceResources faceResources = projectFaceResourcesService.getByCode(passNo, ProjectContextHolder.getProjectId());

            List<ProjectRightDevice>  rightDeviceList = projectRightDeviceService.list(new QueryWrapper<ProjectRightDevice>().lambda()
                    .eq(ProjectRightDevice::getCertMediaCode,passNo)
                    .eq(ProjectRightDevice::getDeviceId,deviceInfo.getDeviceId())
            );

//            if (faceResources == null) {
//                log.error("[冠林边缘网关] 人脸下发结果处理失败，未找到对应人脸：faceCode = {}", passNo);
//                return done();
//            }

//            String keyid = faceResources.getFaceId();
            String result = objInfo.getJSONObject("doorAccess").getString("result");

//            List<ProjectRightDevice> rightDeviceList = projectRightDeviceMapper.listByDeviceIdAndCertmediaId(deviceInfo.getDeviceId(), keyid);
            AurineEdgeFaceResultCodeEnum certStatusByResult = AurineEdgeFaceResultCodeEnum.getCertStatusByResult(result);

            if (CollUtil.isNotEmpty(rightDeviceList)) {

                log.info("[冠林边缘网关] 人脸介质code：{} 介质下发或删除结果：{} 设备端回调结果code：{}", passNo, certStatusByResult.desc, result);

                CertAdownRequestVO certAdownRequestVO = new CertAdownRequestVO();

                Object obj = RedisUtil.get(AurineEdgeCacheConstant.AURINE_EDGE_REQUEST_PER + handleEntity.getOnNotifyData().getMsgId());
                AurineEdgeRequestObject aurineEdgeRequestObject = JSONObject.parseObject(String.valueOf(obj), AurineEdgeRequestObject.class);
                AurineEdgeConfigDTO config = aurineEdgeRequestObject.getConfig();
                JSONObject requestJson = aurineEdgeRequestObject.getRequestJson();

                if (PassRightCertDownloadStatusEnum.RE_DOWNLOAD.code.equals(certStatusByResult.cetDownloadStatus)) {
                    certAdownRequestVO = retry(deviceInfo, config, requestJson);
                    retryIssued(certAdownRequestVO,false);
                    return done();
                } else if (PassRightCertDownloadStatusEnum.SUCCESS.code.equals(certStatusByResult.cetDownloadStatus)){
                    //成功 通知结果
                    certAdownRequestVO.setRequestId(handleEntity.getOnNotifyData().getMsgId());
                    certAdownRequestVO.setProjectId(ProjectContextHolder.getProjectId().toString());
                    certAdownRequestVO.setState(ADownStateEnum.SUCCESS.getCode());
                    retryIssued(certAdownRequestVO,true);
                    //下发凭证和房屋的关系
                    try {
                        if (deviceInfo.getDeviceType().equals(DeviceTypeConstants.ELEVATOR_RECOGNIZER_DEVICE)
                                || deviceInfo.getDeviceCapabilities().contains(DeviceCapabilitiesEnum.CERT_MULTI_USER.code)) {
                            projectDeviceInfoService.sendCertAndHouseRelation(deviceInfo,rightDeviceList.get(0));
                        }
                    } catch (Exception e) {
                        log.info("下发凭证和房屋的关系失败:{}",e.getMessage());
                        e.printStackTrace();
                    }
                } else{
                    //certAdownRequestVO = retry(deviceInfo, config, requestJson);
                    //失败 通知结果
                    certAdownRequestVO.setRequestId(handleEntity.getOnNotifyData().getMsgId());
                    certAdownRequestVO.setProjectId(ProjectContextHolder.getProjectId().toString());
                    certAdownRequestVO.setState(ADownStateEnum.FAIL.getCode());
                    retryIssued(certAdownRequestVO,true);
//                    if(certAdownRequestVO.getPriotity()== 8){
//                        //失败 通知结果
//                        certAdownRequestVO.setRequestId(handleEntity.getOnNotifyData().getMsgId());
//                        certAdownRequestVO.setState(ADownStateEnum.FAIL.getCode());
//                        retryIssued(certAdownRequestVO,true);
//                    }else{
//                        //失败 重试下发
//                        retryIssued(certAdownRequestVO,false);
//                        //通知结果 当前记录失败的结果
//                        certAdownRequestVO.setRequestId(handleEntity.getOnNotifyData().getMsgId());
//                        certAdownRequestVO.setState(ADownStateEnum.FAIL.getCode());
//                        retryIssued(certAdownRequestVO,true);
//                        return done();
//                    }
                }
                ProjectRightDevice rightDevice = rightDeviceList.get(0);

//                if ("DELETE".equals(handleEntity.getOnNotifyData().getObjManagerData().getAction()) && !PassRightCertDownloadStatusEnum.DELETING.code.equals(rightDevice.getDlStatus())) {
//                    rightDevice.setDlStatus(PassRightCertDownloadStatusEnum.DELETING.code);
//                    projectRightDeviceService.updateById(rightDevice);
//                    log.info("重新更新人脸下载状态为“删除中”：{}", rightDevice);
//                }
                responseOperateDTO.setAction(ResponseOperateConstant.ACTION_UPDATE_CERT_STATUS);
                responseOperateDTO.setRightDevice(rightDevice);
                responseOperateDTO.setRespondStatus(certStatusByResult.cetDownloadStatus);
                this.sendMsg(TopicConstant.SDI_RESPONSE_OPERATE, responseOperateDTO);
            } else {
                projectRightDeviceCacheService.saveRightDeviceCache(deviceInfo,passNo,certStatusByResult.cetDownloadStatus,AurineEdgeServiceEnum.CERT_FACE.cloudCode);
                log.error("[冠林边缘网关] 回调函数 在设备 {} {} 下未获取到通行凭证 {} ", deviceInfo.getDeviceName(), deviceInfo.getDeviceId(), passNo);
            }


        } catch (ClassCastException cce) {
            log.error("[冠林边缘网关] 人脸下发结果处理失败，数据结构错误 {}", handleEntity);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            log.info("[冠林边缘网关] 人脸下发结果处理结束 {}", handleEntity.getOnNotifyData().getObjManagerData().getObjInfo());
            return done();
        }

    }


    private void retryIssued(CertAdownRequestVO certAdownRequestVO,boolean bool){
        if(bool){
            certAdownController.update(certAdownRequestVO);
            //remoteCertAdownService.update(certAdownRequestVO,SecurityConstants.FROM_IN);
        }else{
            certAdownController.request(certAdownRequestVO);
            //remoteCertAdownService.request(certAdownRequestVO,SecurityConstants.FROM_IN);
        }
    }


    /**
     * 转换Vo
     *
     * @param deviceInfo
     * @param config
     * @param requestJson
     */
    private CertAdownRequestVO retry(ProjectDeviceInfo deviceInfo, AurineEdgeConfigDTO config, JSONObject requestJson) {
        JSONObject actionInfo = requestJson.getJSONObject("actionInfo");

        String action = (String) actionInfo.get("action");
        String certType = (String) actionInfo.get("serviceId");
        ProjectDeviceInfo projectDeviceInfo = JSONObject.parseObject(JSON.toJSONString(actionInfo.get("deviceInfo")), ProjectDeviceInfo.class);
        List doorKeyIdList = JSONObject.parseObject(JSON.toJSONString(actionInfo.get("doorKeyIdList")), List.class);
        JSONObject params = actionInfo.getJSONObject("params");

        //优先级
        Integer priotity = (Integer) actionInfo.get("priotity");
        priotity++;

        AurineEdgeDeviceCertVo aurineEdgeDeviceCertVo = new AurineEdgeDeviceCertVo();

        aurineEdgeDeviceCertVo.setConfig(config);
        aurineEdgeDeviceCertVo.setDeviceInfo(deviceInfo);
        aurineEdgeDeviceCertVo.setCertType(certType);
        aurineEdgeDeviceCertVo.setAction(action);
        aurineEdgeDeviceCertVo.setParamsJsonArray(params);
        aurineEdgeDeviceCertVo.setDoorKeyIdList(doorKeyIdList);
        aurineEdgeDeviceCertVo.setPriotity(priotity);
        //数据转换
        CertAdownRequestVO certAdownRequestVO = AurineEdgeDeviceDataUtil.toVO(aurineEdgeDeviceCertVo);
        return certAdownRequestVO;
    }

    /**
     * 发送消息到kafka
     *
     * @param topic   主题
     * @param message 内容体
     */
    private <T> void sendMsg(String topic, T message) {
        kafkaTemplate.send(topic, JSONObject.toJSONString(message));
    }

}
