package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.callback.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.component.chain.AbstractHandler;
import com.aurine.cloudx.estate.component.chain.annotation.ChainHandler;
import com.aurine.cloudx.estate.constant.enums.DeviceMediaAdDlStatusEnum;
import com.aurine.cloudx.estate.entity.ProjectMediaAd;
import com.aurine.cloudx.estate.entity.ProjectMediaAdDevCfg;
import com.aurine.cloudx.estate.entity.SysDeviceProductMap;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.thirdparty.module.device.factory.param.DeviceParamFactoryProducer;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineEdgeCacheConstant;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.respond.CallBackData;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.respond.DevMessageData;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.respond.ObjManagerData;
import com.aurine.cloudx.estate.util.ObjectMapperUtil;
import com.aurine.cloudx.estate.util.RedisUtil;
import com.aurine.cloudx.estate.util.delay.TaskUtil;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoProxyVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * <p>参数下发结果回调</p>
 *
 * @author : 王良俊
 * @date : 2021-10-21 15:28:12
 */
@ChainHandler(chainClass = AurineEdgeObjectManageChain.class)
@Component
@Slf4j
public class AurineEdgeObjectMediaAdCallbackHandler extends AbstractHandler<CallBackData> {

    @Resource
    ProjectDeviceInfoProxyService projectDeviceInfoProxyService;
    @Resource
    ProjectMediaAdDevCfgService projectMediaAdDevCfgService;
    @Resource
    ProjectMediaAdService projectMediaAdService;


    @Override
    public boolean filter(CallBackData handleEntity) {
        return "ADMediaManager".equals(handleEntity.getOnNotifyData().getObjManagerData().getServiceId());
    }

    /**
     * {
     * "onNotifyData":{
     * "devId":"e7742b68-023f-4660-86a8-6babee7a51d9",
     * "gatewayId":"e7742b68-023f-4660-86a8-6babee7a51d9",
     * "msgId":"0a605ee633d54ffba4f78cedbbc20703",
     * "objManagerData":{
     * "action":"ADD",
     * "objName":"ADMediaObj",
     * "param":{
     * "msg":"操作成功",
     * "code":0
     * },
     * "serviceId":"ADMediaManager"
     * },
     * "productId":"3T00FFHA"
     * },
     * "resource":"device.objmanager",
     * "event":"report",
     * "subscriptionId":68
     * }
     * <p>
     * code如果是-1说明是软件侧的错误
     */
    public void delayHandle(CallBackData handleEntity) {
        String thirdPartyCode = handleEntity.getOnNotifyData().getDevId();
        ProjectDeviceInfoProxyVo deviceInfo = projectDeviceInfoProxyService.getByThirdPartyCode(thirdPartyCode);
        ProjectContextHolder.setProjectId(deviceInfo.getProjectId());

        String action = handleEntity.getOnNotifyData().getObjManagerData().getAction();
        String data = (String) RedisUtil.get(AurineEdgeCacheConstant.AURINE_EDGE_REQUEST_PER + handleEntity.getOnNotifyData().getMsgId());

        ObjectMapper objectMapper = ObjectMapperUtil.instance();
        try {
            log.info("[媒体广告] redis缓存数据：{}", data);
            ObjectNode objectNode = objectMapper.readValue(data, ObjectNode.class);
            String adSeq = objectNode.findPath("playListId").asText();
            ProjectMediaAd mediaAd = projectMediaAdService.getOne(new LambdaQueryWrapper<ProjectMediaAd>()
                    .eq(ProjectMediaAd::getSeq, adSeq).last("limit 1"));
            if (mediaAd != null) {
                ProjectMediaAdDevCfg mediaAdDevCfg = projectMediaAdDevCfgService.getOne(new LambdaQueryWrapper<ProjectMediaAdDevCfg>().eq(ProjectMediaAdDevCfg::getDeviceId, deviceInfo.getDeviceId()).eq(ProjectMediaAdDevCfg::getAdId, mediaAd.getAdId()));
                String code = handleEntity.getOnNotifyData().getObjManagerData().getParam().getString("code");
                if ("ADD".equals(action)) {
                    mediaAdDevCfg.setDlStatus("0".equals(code) ? DeviceMediaAdDlStatusEnum.SUCCESS.systemCode : DeviceMediaAdDlStatusEnum.FAIL.systemCode);
                    projectMediaAdDevCfgService.updateById(mediaAdDevCfg);
                    log.info("[媒体广告] 下发{} 广告ID：{} 设备ID：{}", "0".equals(code) ? "成功" : "失败", mediaAd.getAdId(), deviceInfo.getDeviceId());
                } else if ("DELETE".equals(action)) {
                    if ("0".equals(code)) {
                        mediaAdDevCfg.setDlStatus(DeviceMediaAdDlStatusEnum.CLEARED.systemCode);
                        projectMediaAdDevCfgService.updateById(mediaAdDevCfg);
                    }
                    log.warn("[媒体广告] 删除{} 广告ID：{} 设备ID：{}", "0".equals(code) ? "成功" : "失败", mediaAd.getAdId(), deviceInfo.getDeviceId());
                }
            } else {
                log.error("[媒体广告] 未找到指定序列广告 adSeq：{} 回调内容：{}", adSeq, handleEntity);
            }
        } catch (JsonProcessingException e) {
            log.error("[媒体广告回调处理异常]");
            e.printStackTrace();
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_UNCOMMITTED, rollbackFor = Exception.class)
    public boolean doHandle(CallBackData handleEntity) {
        // 主要是为了防止过早查询数据库中没有数据的问题
        TaskUtil.hashedWheelTimer.newTimeout(timeout -> {
            this.delayHandle(handleEntity);
        }, 2, TimeUnit.SECONDS);
        return done();
    }


}
