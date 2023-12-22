package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.callback.handler;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.common.core.constant.DeviceTypeConstants;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.component.chain.AbstractHandler;
import com.aurine.cloudx.estate.component.chain.annotation.ChainHandler;
import com.aurine.cloudx.estate.constant.enums.DeviceAccessMethodEnum;
import com.aurine.cloudx.estate.constant.enums.DeviceStatusEnum;
import com.aurine.cloudx.estate.constant.enums.device.DeviceRegAbnormalEnum;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectDeviceReplaceInfo;
import com.aurine.cloudx.estate.service.ProjectDeviceInfoService;
import com.aurine.cloudx.estate.service.ProjectDeviceReplaceInfoService;
import com.aurine.cloudx.estate.service.ProjectPersonDeviceService;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.TopicConstant;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineEdgeActionConstant;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineEdgeObjNameEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgeDeviceInfoDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.respond.CallBackData;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.impl.AurineEdgeDeviceServiceImplV1;
import com.aurine.cloudx.estate.thirdparty.transport.mq.kafka.KafkaProducer;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 设备自动注册处理器
 *
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021/09/17
 * @Copyright:
 */
@ChainHandler(chainClass = AurineEdgeObjectManageChain.class)
@Component
@Slf4j
public class AurineEdgeObjectManageDeviceAddCallbackHandler extends AbstractHandler<CallBackData> {

    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;
    @Resource
    private ProjectDeviceReplaceInfoService projectDeviceReplaceInfoService;

    @Resource
    private AurineEdgeHandlerUtil aurineEdgeHandlerUtil;

    @Resource
    private AurineEdgeDeviceServiceImplV1 aurineEdgeDeviceServiceImplV1;

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

        if (!StringUtils.equals(AurineEdgeActionConstant.ADD, handleEntity.getOnNotifyData().getObjManagerData().getAction())) {
            return false;
        }

        log.info("[冠林边缘网关] {} 开始处理设备手动添加回调功能", handleEntity);
        return true;
    }


    /**
     * 执行方法，执行后返回调用next继续下一组handle，调用done则结束处理
     *
     * @param handleEntity
     */
    @SneakyThrows
    @Override
    public boolean doHandle(CallBackData handleEntity) {
        Thread.sleep(1000);
        try {
            AurineEdgeDeviceInfoDTO deviceEdgeInfo = handleEntity.getOnNotifyData().getObjManagerData().getObjInfo().toJavaObject(AurineEdgeDeviceInfoDTO.class);

            String deviceThirdCode = handleEntity.getOnNotifyData().getDevId();
            String resultCode = handleEntity.getOnNotifyData().getObjManagerData().getParam().getString("code");

            if (StringUtils.equals(resultCode, "0")) {
                ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getByThirdPartyCode(deviceThirdCode);
                String beforeStatus = deviceInfo.getStatus();
                if (deviceInfo != null) {
                    Integer devStatus = deviceEdgeInfo.getDevStatus();
                    if (devStatus == null) {
                        deviceInfo.setStatus(DeviceStatusEnum.ONLINE.code);
                    } else {
                        deviceInfo.setStatus(DeviceStatusEnum.getByCode(devStatus).getCode());
                    }
                    //deviceInfo.setDeviceCapabilities(deviceEdgeInfo.getCapabilitiesString());

                    projectDeviceInfoService.updateById(deviceInfo);
                    // 设备上线通知
                    if (!DeviceStatusEnum.ONLINE.code.equals(beforeStatus) && DeviceStatusEnum.ONLINE.code.equals(deviceInfo.getStatus())) {
                        // 如果是非在线更新为在线状态
                        /**********************开放平台openv2的数据实时推送*********************/
                        ProjectDeviceInfo nowDeviceInfo = projectDeviceInfoService.getById(deviceInfo.getDeviceId());
                        KafkaProducer.sendMessage(TopicConstant.DEVICE_STATUS,nowDeviceInfo);
                        /*********************************************************************/
                    }

                    //AI盒子设备添加成功后，设置账号数据
                    if (deviceInfo.getDeviceType().equals(DeviceTypeConstants.AI_BOX_DEVICE)) {
                        ProjectDeviceInfoVo projectDeviceInfo = new ProjectDeviceInfoVo();
                        BeanUtils.copyProperties(deviceInfo, projectDeviceInfo);
                        projectDeviceInfoService.setAccount(projectDeviceInfo);
                    }
                    //进行授权变更
//                    if(deviceInfo.getStatus().equals(DeviceStatusEnum.ONLINE.code)){
//                        projectPersonDeviceService.refreshAddDevice(deviceInfo);
//                    }

                } else {
                    log.error("[冠林边缘网关] 处理设备手动添加回调 未找到对应设备：thirdPartyCode={}", deviceThirdCode);
                }
            } else {
                log.info("设备手动注册失败 {}", handleEntity);
            }

        } catch (ClassCastException cce) {
            log.error("[冠林边缘网关] 设备手动添加回调失败，数据结构错误 {}", handleEntity);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            log.info("[冠林边缘网关] 设备手动添加回调结束 {}", handleEntity.getOnNotifyData().getObjManagerData().getObjInfo());
            return done();
        }

    }

}
