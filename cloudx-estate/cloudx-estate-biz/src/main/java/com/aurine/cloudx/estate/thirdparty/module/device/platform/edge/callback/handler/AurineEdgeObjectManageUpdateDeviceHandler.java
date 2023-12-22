package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.callback.handler;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.alibaba.druid.sql.visitor.functions.If;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.constant.DeviceTypeConstants;
import com.aurine.cloudx.common.core.constant.enums.DeviceTypeEnum;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.component.chain.AbstractHandler;
import com.aurine.cloudx.estate.component.chain.annotation.ChainHandler;
import com.aurine.cloudx.estate.constant.DeviceConstant;
import com.aurine.cloudx.estate.constant.enums.DeviceAccessMethodEnum;
import com.aurine.cloudx.estate.constant.enums.DeviceStatusEnum;
import com.aurine.cloudx.estate.constant.enums.device.DeviceRegAbnormalEnum;
import com.aurine.cloudx.estate.constant.enums.device.DeviceRegParamEnum;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.EventDeviceNoticeConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.TopicConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.EventDeviceNoticeDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.*;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgeDeviceInfoDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.respond.CallBackData;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.exception.entity.DeviceAbnormalHandleInfo;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.impl.AurineEdgeDeviceServiceImplV1;
import com.aurine.cloudx.estate.thirdparty.transport.mq.kafka.KafkaProducer;
import com.aurine.cloudx.estate.util.RedisUtil;
import com.aurine.cloudx.estate.vo.ProjectBuildingInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 设备更新事件处理器
 *
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021/09/17
 * @Copyright:
 */
@ChainHandler(chainClass = AurineEdgeObjectManageChain.class)
@Component
@Slf4j
public class AurineEdgeObjectManageUpdateDeviceHandler extends AbstractHandler<CallBackData> {
    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;
    @Resource
    private AurineEdgeHandlerUtil aurineEdgeHandlerUtil;

    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;

    @Resource
    private ProjectDeviceModifyLogService projectDeviceModifyLogService;

    @Resource
    private ProjectPersonDeviceService projectPersonDeviceService;


    /**
     * handler校验函数，不满足该函数则跳过处理
     *
     * @param handleEntity
     * @return
     */
    @Override
    public boolean filter(CallBackData handleEntity) {


        if (!StringUtils.equals(AurineEdgeObjNameEnum.DeviceInfoObj.code, handleEntity.getOnNotifyData().getObjManagerData().getObjName())) {
            return false;
        }

        if (!StringUtils.equals(AurineEdgeActionConstant.UPDATE, handleEntity.getOnNotifyData().getObjManagerData().getAction())
        && !StringUtils.equals(AurineEdgeActionConstant.REPLACE, handleEntity.getOnNotifyData().getObjManagerData().getAction())) {
            return false;
        }

        log.info("[冠林边缘网关] {} 开始处理设备更新功能", handleEntity);
        return true;
    }


    /**
     * 执行方法，执行后返回调用next继续下一组handle，调用done则结束处理
     *
     * @param handleEntity
     */
    @Override
    public boolean doHandle(CallBackData handleEntity) {

        try {
            AurineEdgeDeviceInfoDTO deviceEdgeInfo = handleEntity.getOnNotifyData().getObjManagerData().getObjInfo().toJavaObject(AurineEdgeDeviceInfoDTO.class);
            // AI盒子设置聚集报警规则，返回数据为空
            if(deviceEdgeInfo.getDevId() == null){
                ProjectDeviceInfo currDeviceInfo = projectDeviceInfoService.getByThirdPartyCode(handleEntity.getOnNotifyData().getDevId());
                if(currDeviceInfo != null){
                    log.info("[冠林边缘网关] 设备更新返回数据为空，判断为AI盒子设置聚集报警规则：{}",currDeviceInfo);
                    //AI盒子设置聚集报警规则，异步转同步请求
                    if(currDeviceInfo.getDeviceType().equals(DeviceTypeConstants.AI_BOX_DEVICE)){
                        RedisUtil.set(AurineEdgeSyncReqEnum.EDGE_SYNC_RESP.code + handleEntity.getOnNotifyData().getMsgId(), JSONObject.toJSONString(handleEntity), 20);
                    }
                }
                return done();
            }
            //数据结构转换
            ProjectDeviceInfo currDeviceInfo = projectDeviceInfoService.getByThirdPartyCode(deviceEdgeInfo.getDevId());
            ProjectDeviceInfo updateDeviceInfo = aurineEdgeHandlerUtil.toCloudxPoNotSetName(deviceEdgeInfo,currDeviceInfo);

            String currDeviceStatus = currDeviceInfo.getStatus();
            String updateDeviceStatus = updateDeviceInfo.getStatus();

            if(currDeviceInfo == null){
                log.error("[冠林边缘网关] 设备更新失败 要更新的设备不存在{}",deviceEdgeInfo.getDevId());
                throw new RuntimeException("要更新的设备不存在");
            }

            String devId = currDeviceInfo.getDeviceId();
            String accessMethod = currDeviceInfo.getAccessMethod();
            String deviceName = currDeviceInfo.getDeviceName();
            String status = currDeviceInfo.getStatus();

            updateDeviceInfo.setDeviceId(null);
//            updateDeviceInfo.setAccessMethod(null);
            updateDeviceInfo.setUpdateTime(null);
//            updateDeviceInfo.setCreateTime(null);
            updateDeviceInfo.setProjectId(currDeviceInfo.getProjectId());

            //调用更新日志记录接口
            projectDeviceModifyLogService.saveDeviceModifyLog(currDeviceInfo.getDeviceId(),updateDeviceInfo);

            //更新设备信息
            BeanUtil.copyProperties(updateDeviceInfo, currDeviceInfo);

            currDeviceInfo.setDeviceId(devId);
            currDeviceInfo.setDeviceName(deviceName);
//            currDeviceInfo.setAccessMethod(accessMethod);

            Boolean deviceError = aurineEdgeHandlerUtil.checkDeviceError(updateDeviceInfo, String.valueOf(deviceEdgeInfo.getFailCode()));
            // 存在异常不更新设备数据（原因：设备异常心跳重新上报事件，大量无用数据同步到云端）
            if (!deviceError) {
                projectDeviceInfoService.updateById(currDeviceInfo);
                if (!DeviceStatusEnum.ONLINE.code.equals(currDeviceStatus) && DeviceStatusEnum.ONLINE.code.equals(updateDeviceStatus)) {
                    // 如果是非在线更新为在线状态
                    /**********************开放平台openv2的数据实时推送*********************/
                    ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getById(devId);
                    KafkaProducer.sendMessage(TopicConstant.DEVICE_STATUS,deviceInfo);
                    /*********************************************************************/
                }
                //第一次由手动添加改为自动添加  进行授权变更
                if(accessMethod.equals(DeviceAccessMethodEnum.MANUAL.code)){
                   projectPersonDeviceService.refreshAddDevice(currDeviceInfo);
                }
            }
            log.info("[冠林边缘网关] 设备更新成功 更新设备信息：{}", currDeviceInfo);

        } catch (ClassCastException cce) {
            log.error("[冠林边缘网关] 设备更新失败，数据结构错误 {}", handleEntity);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            log.info("[冠林边缘网关] 设备更新流程结束 {}", handleEntity.getOnNotifyData().getObjManagerData().getObjInfo());
            return done();
        }

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
