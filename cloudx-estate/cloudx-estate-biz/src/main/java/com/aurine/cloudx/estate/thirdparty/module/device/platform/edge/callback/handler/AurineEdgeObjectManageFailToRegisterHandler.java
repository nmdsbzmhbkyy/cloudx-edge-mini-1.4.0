package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.callback.handler;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONException;
import com.aurine.cloudx.common.core.constant.enums.DeviceTypeEnum;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.component.chain.AbstractHandler;
import com.aurine.cloudx.estate.component.chain.annotation.ChainHandler;
import com.aurine.cloudx.estate.constant.enums.device.DeviceRegParamEnum;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.service.ProjectDeviceAbnormalService;
import com.aurine.cloudx.estate.service.ProjectDeviceRegionService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineEdgeActionConstant;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineEdgeDevResTypeEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineEdgeObjNameEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineEdgeOnlineStatusEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgeDeviceInfoDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.respond.CallBackData;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.exception.entity.DeviceAbnormalHandleInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 设备注册失败处理器
 *
 * @author: 邹宇
 * @date: 2021/09/29
 */
@ChainHandler(chainClass = AurineEdgeObjectManageChain.class)
@Component
@Slf4j
class AurineEdgeObjectManageFailToRegisterHandler extends AbstractHandler<CallBackData> {

    @Resource
    private ProjectDeviceAbnormalService projectDeviceAbnormalService;


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

        if (!StringUtils.equals(AurineEdgeActionConstant.REG_FAIL, handleEntity.getOnNotifyData().getObjManagerData().getAction())) {
            return false;
        }

        log.info("[冠林边缘网关]开始处理记录设备注册失败功能:{}", handleEntity);
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

            AurineEdgeDeviceInfoDTO param = handleEntity.getOnNotifyData().getObjManagerData().getParam().toJavaObject(AurineEdgeDeviceInfoDTO.class);
            //数据结构转换
            ProjectDeviceInfo deviceInfo = this.toCloudxPo(deviceEdgeInfo);

            this.sendTocheckAbnormal( deviceInfo, param);

        } catch (JSONException cce) {
            log.error("[冠林边缘网关] 记录设备注册失败，数据结构错误 {}", handleEntity);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            log.info("[冠林边缘网关] 记录设备注册结束 {}", handleEntity.getOnNotifyData().getObjManagerData().getObjInfo());
            return done();
        }

    }

    /**
     * 调用异常添加接口
     *
     * @param deviceInfo
     * @param param
     */
    private void sendTocheckAbnormal( ProjectDeviceInfo deviceInfo, AurineEdgeDeviceInfoDTO param) {
        DeviceAbnormalHandleInfo deviceAbnormalHandleInfo = new DeviceAbnormalHandleInfo();
        BeanUtil.copyProperties(deviceInfo, deviceAbnormalHandleInfo);

        deviceAbnormalHandleInfo.setThirdpartyCode(deviceInfo.getThirdpartyCode());
        deviceAbnormalHandleInfo.setDeviceDesc(deviceInfo.getDeviceName());
        deviceAbnormalHandleInfo.setFailCode(String.valueOf(param.getFailCode()));
        deviceAbnormalHandleInfo.setSn(deviceInfo.getSn());
        deviceAbnormalHandleInfo.addParam(DeviceRegParamEnum.IPV4, deviceInfo.getIpv4())
                .addParam(DeviceRegParamEnum.MAC, deviceInfo.getMac())
                .addParam(DeviceRegParamEnum.DEVICE_NO, deviceInfo.getDeviceCode());
        //保存注册失败记录
        projectDeviceAbnormalService.checkAbnormal(deviceAbnormalHandleInfo);
    }

    /**
     * 将数据转换成Cloudx所使用的设备对象
     *
     * @param deviceEdgeInfo
     * @return
     */
    private ProjectDeviceInfo toCloudxPo(AurineEdgeDeviceInfoDTO deviceEdgeInfo) {
        ProjectDeviceInfo deviceInfo = new ProjectDeviceInfo();

        //注册失败，系统里不存在这台设备 设备为null
        deviceInfo.setDeviceId(null);

        deviceInfo.setProjectId(ProjectContextHolder.getProjectId());
        deviceInfo.setDeviceCode(deviceEdgeInfo.getDeviceNo());
        deviceInfo.setIpv4(deviceEdgeInfo.getDeviceIp());
        deviceInfo.setSn(deviceEdgeInfo.getDevSN());
        deviceInfo.setThirdpartyCode(deviceEdgeInfo.getDevId());
        //区分设备类型
        deviceInfo.setDeviceType(getDeviceType(deviceEdgeInfo).getCode());

        deviceInfo.setMac(deviceEdgeInfo.getMac());
        deviceInfo.setPort(deviceEdgeInfo.getDevicePort());
        deviceInfo.setHardVersion(deviceEdgeInfo.getHardwareVersion());
        deviceInfo.setSoftVersion(deviceEdgeInfo.getSoftwareVersion());
        deviceInfo.setDeviceCapabilities(deviceEdgeInfo.getCapabilitiesString());

        deviceInfo.setStatus(AurineEdgeOnlineStatusEnum.getByCode(deviceEdgeInfo.getOnline()).code);
        deviceInfo.setActive("0");

        return deviceInfo;
    }

    /**
     * 解析设备类型
     *
     * @param deviceInfo
     * @return
     */
    private DeviceTypeEnum getDeviceType(AurineEdgeDeviceInfoDTO deviceInfo) {
        return AurineEdgeDevResTypeEnum.getByCode(deviceInfo.getDevResType()).deviceType;
    }

}

