package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.callback.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.constant.DeviceTypeConstants;
import com.aurine.cloudx.common.core.constant.enums.DeviceTypeEnum;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.cert.constant.ADownStateEnum;
import com.aurine.cloudx.estate.cert.controller.CertAdownController;
import com.aurine.cloudx.estate.cert.vo.CertAdownRequestVO;
import com.aurine.cloudx.estate.component.chain.AbstractHandler;
import com.aurine.cloudx.estate.component.chain.annotation.ChainHandler;
import com.aurine.cloudx.estate.constant.enums.PassRightCertDownloadStatusEnum;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectRightDevice;
import com.aurine.cloudx.estate.entity.ProjectVisitorHis;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.ResponseOperateConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.TopicConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.ResponseOperateDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.config.AurineEdgeConfigDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineEdgeFaceResultCodeEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineEdgeServiceEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.door.DoorAccessObj;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.door.DoorKeyObj;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.respond.CallBackData;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.respond.ObjManagerData;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.impl.AurineEdgePassWayDeviceServiceImplV1;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.enums.DeviceCapabilitiesEnum;
import com.aurine.cloudx.estate.util.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 卡片下发回调事件处理
 *
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021/09/17
 * @Copyright:
 */
@ChainHandler(chainClass = AurineEdgeObjectManageChain.class)
@Component
@Slf4j
public class AurineEdgeObjectManageCardManagelHandler extends AbstractHandler<CallBackData> {
    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;
    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;
    @Resource
    private ProjectRightDeviceService projectRightDeviceService;
    //    @Resource
//    private RemoteCertAdownService remoteCertAdownService;
    @Resource
    private ProjectRightDeviceCacheService projectRightDeviceCacheService;
    @Resource
    private CertAdownController certAdownController;
    @Resource
    private SysDeviceTypeThirdPartyConfigService sysDeviceTypeThirdPartyConfigService;
    @Resource
    private ProjectVisitorHisService projectVisitorHisService;
    @Resource
    private AurineEdgePassWayDeviceServiceImplV1 aurineEdgePassWayDeviceServiceImplV1;


    /**
     * handler校验函数，不满足该函数则跳过处理
     *
     * @param handleEntity
     * @return
     */
    @Override
    public boolean filter(CallBackData handleEntity) {

        if (!StringUtils.equals(AurineEdgeServiceEnum.CERT_CARD.code, handleEntity.getOnNotifyData().getObjManagerData().getServiceId())) {
            return false;
        }

        /*if (!StringUtils.equals(AurineEdgeActionConstant.ADD, handleEntity.getOnNotifyData().getObjManagerData().getAction())) {
            return false;
        }*/

        log.info("[冠林边缘网关] {} 开始处理卡片添加返回结果", handleEntity);
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
        if (!b) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {

            //执行下发成功的设备
            String deviceThirdCode = handleEntity.getOnNotifyData().getDevId();
            ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getByThirdPartyCode(deviceThirdCode);

            //获取下发的设备
            if (deviceInfo == null) {
                log.error("[冠林边缘网关] 卡片下发结果处理失败，未找到下发的设备：deviceThirdCode = {}", deviceThirdCode);
                return done();
            }

            //获取下发的卡片结果
            ObjManagerData objManagerData = handleEntity.getOnNotifyData().getObjManagerData();
            JSONObject objInfo = handleEntity.getOnNotifyData().getObjManagerData().getObjInfo();
            JSONArray doorAccess = objInfo.getJSONArray("doorAccess");

            List<ProjectRightDevice> successList = new ArrayList<>();
            List<ProjectRightDevice> failList = new ArrayList<>();
            List<ProjectRightDevice> otherList = new ArrayList<>();
            List<ProjectRightDevice> lessList = new ArrayList<>();
            ResponseOperateDTO responseOperateDTO = new ResponseOperateDTO();
            responseOperateDTO.setCompany(PlatformEnum.AURINE_EDGE_MIDDLE.value);
            responseOperateDTO.setDeviceInfo(deviceInfo);
            responseOperateDTO.setAction(ResponseOperateConstant.ACTION_UPDATE_CERT_STATUS_LIST);
            for (int i = 0; i < doorAccess.size(); i++) {
                JSONObject jsonObject = doorAccess.getJSONObject(i);
                String passNo = jsonObject.getString("passNo");
                String result = jsonObject.getString("result");
                String type = jsonObject.getString("type");

                List<ProjectRightDevice> rightDeviceList = projectRightDeviceService.list(new QueryWrapper<ProjectRightDevice>().lambda()
                        .eq(ProjectRightDevice::getCertMediaInfo, passNo)
                        .eq(ProjectRightDevice::getDeviceId, deviceInfo.getDeviceId())
                );

                AurineEdgeFaceResultCodeEnum certStatusByResult = AurineEdgeFaceResultCodeEnum.getCertStatusByResult(result);
                if (CollUtil.isNotEmpty(rightDeviceList)) {

                    log.info("[冠林边缘网关] 卡片介质code：{} 介质下发或删除结果：{} 设备端回调结果code：{}", passNo, certStatusByResult.desc, result);

                    //通知下发服务结果
                    CertAdownRequestVO certAdownRequestVO = new CertAdownRequestVO();
                    certAdownRequestVO.setRequestId(handleEntity.getOnNotifyData().getMsgId());
                    certAdownRequestVO.setProjectId(ProjectContextHolder.getProjectId().toString());
                    certAdownRequestVO.setState(PassRightCertDownloadStatusEnum.SUCCESS.code.equals(certStatusByResult.cetDownloadStatus) ? ADownStateEnum.SUCCESS.getCode() : ADownStateEnum.FAIL.getCode());
                    certAdownController.update(certAdownRequestVO);
                    //remoteCertAdownService.update(certAdownRequestVO, SecurityConstants.FROM_IN);

                    ProjectRightDevice projectRightDevice = rightDeviceList.get(0);
                    if (PassRightCertDownloadStatusEnum.SUCCESS.code.equals(certStatusByResult.cetDownloadStatus)) {
                        //下发凭证和房屋的关系
                        try {
                            if (deviceInfo.getDeviceType().equals(DeviceTypeConstants.ELEVATOR_RECOGNIZER_DEVICE)
                                    || deviceInfo.getDeviceCapabilities().contains(DeviceCapabilitiesEnum.CERT_MULTI_USER.code)) {
                                projectDeviceInfoService.sendCertAndHouseRelation(deviceInfo, projectRightDevice);
                            }
                        } catch (Exception e) {
                            log.info("下发凭证和房屋的关系失败:{}", e.getMessage());
                            e.printStackTrace();
                        }
                        successList.add(projectRightDevice);
                    } else if (PassRightCertDownloadStatusEnum.FAIL.code.equals(certStatusByResult.cetDownloadStatus)) {
                        //第一次失败重试一下,之后还是失败的话凭证状态改为下载失败  应对广州庆胜项目多门控制器下卡的问题
                        if (Objects.isNull(RedisUtil.get(projectRightDevice.getCertMediaId()))) {
                            RedisUtil.set(projectRightDevice.getCertMediaId(), 1, 30);
                            retry(deviceInfo, objManagerData, projectRightDevice);
                        } else {
                            failList.add(projectRightDevice);
                        }
                    } else if (PassRightCertDownloadStatusEnum.RE_DOWNLOAD.code.equals(certStatusByResult.cetDownloadStatus)) {
                        otherList.add(projectRightDevice);
                    } else {
                        lessList.add(projectRightDevice);
                    }
                } else {
                    projectRightDeviceCacheService.saveRightDeviceCache(deviceInfo, passNo, certStatusByResult.cetDownloadStatus, AurineEdgeServiceEnum.CERT_CARD.cloudCode);
                    log.error("[冠林边缘网关] 回调函数 在设备 {} {} 下未获取到通行凭证 {} ", deviceInfo.getDeviceName(), deviceInfo.getDeviceId(), passNo);
                }
            }
            if (CollUtil.isNotEmpty(successList)) {
                responseOperateDTO.setRightDeviceList(successList);
                responseOperateDTO.setRespondStatus(PassRightCertDownloadStatusEnum.SUCCESS.code);
                this.sendMsg(TopicConstant.SDI_RESPONSE_OPERATE, responseOperateDTO);
            }
            if (CollUtil.isNotEmpty(failList)) {
                responseOperateDTO.setRightDeviceList(failList);
                responseOperateDTO.setRespondStatus(PassRightCertDownloadStatusEnum.FAIL.code);
                this.sendMsg(TopicConstant.SDI_RESPONSE_OPERATE, responseOperateDTO);
            }
            if (CollUtil.isNotEmpty(otherList)) {
                responseOperateDTO.setRightDeviceList(otherList);
                responseOperateDTO.setRespondStatus(PassRightCertDownloadStatusEnum.RE_DOWNLOAD.code);
                this.sendMsg(TopicConstant.SDI_RESPONSE_OPERATE, responseOperateDTO);
            }
            if (CollUtil.isNotEmpty(lessList)) {
                responseOperateDTO.setRightDeviceList(lessList);
                responseOperateDTO.setRespondStatus(PassRightCertDownloadStatusEnum.OUT_OF_MEMORY.code);
                this.sendMsg(TopicConstant.SDI_RESPONSE_OPERATE, responseOperateDTO);
            }
        } catch (ClassCastException cce) {
            log.error("[冠林边缘网关] 卡片下发结果处理失败，数据结构错误 {}", handleEntity);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            log.info("[冠林边缘网关] 卡片下发结果处理结束 {}", handleEntity.getOnNotifyData().getObjManagerData().getObjInfo());
            return done();
        }

    }

    private void retry(ProjectDeviceInfo deviceInfo, ObjManagerData objManagerData, ProjectRightDevice projectRightDevice) {
        AurineEdgeConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceType(deviceInfo.getDeviceType(), deviceInfo.getProjectId(), 1, AurineEdgeConfigDTO.class);

        String houseCode = "";
        List<String> houseCodeList = new ArrayList<>();
        if (deviceInfo.getDeviceType().equals(DeviceTypeEnum.LADDER_WAY_DEVICE.getCode())) {
            houseCodeList = projectRightDeviceService.getHouseCodeByUnit(projectRightDevice.getCertMediaId(), deviceInfo.getDeviceId());
        } else {
            houseCodeList = projectRightDeviceService.getHouseCode(projectRightDevice.getCertMediaId());
        }
        if (CollUtil.isNotEmpty(houseCodeList)) {
            houseCode = houseCodeList.get(0);
        } else {
            houseCode = "0";
        }
        DoorKeyObj doorKeyVo = new DoorKeyObj();
        doorKeyVo.setPassNo(projectRightDevice.getCertMediaInfo());
        doorKeyVo.setKeyId(projectRightDevice.getCertMediaId());
        doorKeyVo.setRoomNo(houseCode);
        doorKeyVo.setPeriod(getPeriod(projectRightDevice.getPersonId(), projectRightDevice.getPersonType()));
        if (StrUtil.isNotBlank(doorKeyVo.getPeriod())) {
            doorKeyVo.setLifecycle("-4");
        }
        List<DoorKeyObj> doorKeyVoList = new ArrayList<>();
        doorKeyVoList.add(doorKeyVo);
        DoorAccessObj paramJsonObj = new DoorAccessObj();
        paramJsonObj.setDoorAccess(doorKeyVoList);
        List<String> doorKeyIdList = doorKeyVoList.stream().map(DoorKeyObj::getKeyId).collect(Collectors.toList());

        aurineEdgePassWayDeviceServiceImplV1.sendCert(config, deviceInfo, AurineEdgeServiceEnum.CERT_CARD.code, objManagerData.getAction(),
                JSONObject.parseObject(JSONObject.toJSONString(paramJsonObj)), doorKeyIdList);
    }

    /**
     * 根据人员获取凭证访问周期
     *
     * @return
     */
    private String getPeriod(String personId, String personType) {
        String period = "";
        if (StrUtil.isBlank(personId) || StrUtil.isBlank(personType)) {
            return period;
        }
        //当前支持访客周期
        if (StrUtil.equals(personType, PersonTypeEnum.VISITOR.code)) {
            ProjectVisitorHis projectVisitorHis = this.projectVisitorHisService.getById(personId);
            if (projectVisitorHis != null) {
                return projectVisitorHis.getPeriod();
            }
        }
        return period;
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
