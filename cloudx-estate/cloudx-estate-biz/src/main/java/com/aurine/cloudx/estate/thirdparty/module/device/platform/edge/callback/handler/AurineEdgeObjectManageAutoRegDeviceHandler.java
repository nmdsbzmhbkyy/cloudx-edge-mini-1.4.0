package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.callback.handler;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.common.core.constant.DeviceTypeConstants;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.component.chain.AbstractHandler;
import com.aurine.cloudx.estate.component.chain.annotation.ChainHandler;
import com.aurine.cloudx.estate.constant.enums.DeviceAccessMethodEnum;
import com.aurine.cloudx.estate.constant.enums.device.DeviceRegAbnormalEnum;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectDeviceReplaceInfo;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.config.AurineEdgeConfigDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineEdgeActionConstant;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineEdgeErrorEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineEdgeObjNameEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgeDeviceInfoDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgeRespondDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.respond.CallBackData;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.impl.AurineEdgeDeviceServiceImplV1;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.remote.factory.AurineEdgeRemoteDeviceServiceFactory;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.omg.CORBA.INTERNAL;
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
public class AurineEdgeObjectManageAutoRegDeviceHandler extends AbstractHandler<CallBackData> {

    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;
    @Resource
    private ProjectDeviceReplaceInfoService projectDeviceReplaceInfoService;

    @Resource
    private AurineEdgeHandlerUtil aurineEdgeHandlerUtil;

    @Resource
    private ProjectDeviceParamInfoService projectDeviceParamInfoService;

    @Resource
    private AurineEdgeDeviceServiceImplV1 aurineEdgeDeviceServiceImplV1;
    @Resource
    private SysDeviceTypeThirdPartyConfigService sysDeviceTypeThirdPartyConfigService;
    @Resource
    private ProjectPersonDeviceService projectPersonDeviceService;

    @Resource
    private SysDeviceProductMapService sysDeviceProductMapService;
    @Resource
    private SysProductServiceService sysProductServiceService;
    @Resource
    private ProjectInfoService projectInfoService;


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

        if (!StringUtils.equals(AurineEdgeActionConstant.REG, handleEntity.getOnNotifyData().getObjManagerData().getAction())) {
            return false;
        }

        log.info("[冠林边缘网关] {} 开始处理设备自动注册功能", handleEntity);
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

            //数据结构转换
            ProjectDeviceInfo deviceInfo = aurineEdgeHandlerUtil.toCloudxPo(deviceEdgeInfo);

            //自动添加逻辑处理
            this.doLogic(deviceEdgeInfo, deviceInfo);


        } catch (ClassCastException cce) {
            log.error("[冠林边缘网关] 设备自动注册失败，数据结构错误 {}", handleEntity);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            log.info("[冠林边缘网关] 设备自动注册结束 {}", handleEntity.getOnNotifyData().getObjManagerData().getObjInfo());
            return done();
        }

    }


    /**
     * 设备自动注册逻辑
     * <p>
     * ### 注册成功说明
     * 1、当设备编号在平台上未登记，且**已注册设备编号**为空，可注册成功`
     * 2、当设备编号在平台上未登记，且**已注册设备编号**非空且在平台上已登记，可注册成功，此时将平台中**已注册设备编号**替换成当前设备编号，即替换设备，并记录该动作（被替换）
     * 3、当设备编号在平台上已登记（已登记的类型为手动添加设备），且**已注册设备编号**为空，已登记mac与当前上报设备mac相等，则可注册成功，此时将当前平台中手动设备替换成自动注册设备，并更新设备信息
     * 4、当设备编号在平台上已登记，且**已注册设备编号**与当前设备编号相同，可注册成功，此时更新设备信息而非注册设备
     * <p>
     * ### 注册失败说明
     * 5、当前设备编号在平台上未登记，且**已注册设备编号**非空且在平台上未登记，返回注册失败：不存在（可能在其他上位机上登记过的，不允许使用）
     * 6、当设备编号在平台上已登记（已登记的类型为自动添加设备），且**已注册设备编号**为空，返回注册失败：已存在（编号重复注册）
     * 7、当设备编号在平台上已登记（已登记的类型为手动添加设备），已登记mac与当前上报设备mac不等，返回注册失败：编号重复注册
     * 8、其他情况，比如编号规则不合法，设备编号取值不合规等，都返回注册失败：失败
     * <p>
     * ### 设备相关规则
     * 设备开机/重启触发：1、断电重启；2、通过平台重启；3、每天定时重启一次
     *
     * @param deviceEdgeInfo
     * @param deviceInfo
     */
    private boolean doLogic(AurineEdgeDeviceInfoDTO deviceEdgeInfo, ProjectDeviceInfo deviceInfo) {

        //初始化必要参数
        String deviceNo = deviceEdgeInfo.getDeviceNo();
        String deviceOldNo = deviceEdgeInfo.getExtParam().getDeviceNoOld();
        String mac = deviceEdgeInfo.getMac();

        //验证设备是否已存在，如果设备已存在，直接发送激活指令，并忽略其他流程
        int countDeviceNum = projectDeviceInfoService.count(new QueryWrapper<ProjectDeviceInfo>().lambda().eq(ProjectDeviceInfo::getThirdpartyCode, deviceInfo.getThirdpartyCode()));

        if(countDeviceNum >= 1){
            log.info("[冠林边缘网关] 设备自动注册  设备第三方ID：{} 已存在，发送激活指令，并忽略自动注册操作",deviceInfo.getThirdpartyCode());

            //获取对接配置
            AurineEdgeConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceType(deviceInfo.getDeviceType(), ProjectContextHolder.getProjectId(), 1, AurineEdgeConfigDTO.class);
            AurineEdgeRespondDTO respondDTO = AurineEdgeRemoteDeviceServiceFactory.getInstance(VersionEnum.V1).activeDevice(config, deviceEdgeInfo);
            if (respondDTO.getErrorEnum() == AurineEdgeErrorEnum.SUCCESS) {
                //成功业务
                log.info("[冠林边缘网关] 设备自动注册 发送激活指令成功 ");
            } else {
                log.error("[冠林边缘网关] 设备自动注册 发送激活指令失败 {},{}", respondDTO.getErrorEnum(), respondDTO.getErrorMsg());
            }

            return true;
        }



        List<ProjectDeviceInfo> deviceInfoList = projectDeviceInfoService.list(new QueryWrapper<ProjectDeviceInfo>().lambda().eq(ProjectDeviceInfo::getDeviceCode, deviceNo));

        List<ProjectDeviceInfo> deviceInfoOldList = null;
        if (StringUtils.isNotEmpty(deviceOldNo)) {
            deviceInfoOldList = projectDeviceInfoService.list(new QueryWrapper<ProjectDeviceInfo>().lambda().eq(ProjectDeviceInfo::getDeviceCode, deviceOldNo));
        }

        ProjectDeviceInfo currDeviceInfo = null;
        ProjectDeviceInfo oldDeviceInfo = null;

        if (CollUtil.isNotEmpty(deviceInfoList)) {
            currDeviceInfo = deviceInfoList.get(0);
        }
        if (CollUtil.isNotEmpty(deviceInfoOldList)) {
            currDeviceInfo = deviceInfoOldList.get(0);
        }


        //执行逻辑操作
        if (logicHandler01(deviceNo, deviceOldNo, mac, currDeviceInfo, oldDeviceInfo, deviceInfo, String.valueOf(deviceEdgeInfo.getFailCode() != null ? deviceEdgeInfo.getFailCode() : ""))) {

            //获取对接配置
            AurineEdgeConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceType(deviceInfo.getDeviceType(), ProjectContextHolder.getProjectId(), 1, AurineEdgeConfigDTO.class);

            //调用对接业务
            AurineEdgeRespondDTO respondDTO = AurineEdgeRemoteDeviceServiceFactory.getInstance(VersionEnum.V1).activeDevice(config, deviceEdgeInfo);
            projectDeviceInfoService.initDeviceParamData(deviceInfo.getDeviceId());
            try {
                projectDeviceInfoService.initDeviceParamData(deviceInfo.getDeviceId());
            } catch (Exception e) {
                log.error("获取参数失败");
                e.printStackTrace();
            }

            //进行授权变更
            projectPersonDeviceService.refreshAddDevice(deviceInfo);

            //更新室内机配置状态
            if(StrUtil.equals(deviceInfo.getDeviceType(), DeviceTypeConstants.INDOOR_DEVICE)){
                projectDeviceInfoService.updateConfigured(deviceInfo.getMac());
            }

            if (respondDTO.getErrorEnum() == AurineEdgeErrorEnum.SUCCESS) {
                //成功业务
                log.info("[冠林边缘网关] 设备自动注册 发送激活指令成功 ");
            } else {
                log.error("[冠林边缘网关] 设备自动注册 发送激活指令失败 {},{}", respondDTO.getErrorEnum(), respondDTO.getErrorMsg());
            }

        } else if (logicHandler02(deviceNo, deviceOldNo, mac, currDeviceInfo, oldDeviceInfo, deviceInfo, String.valueOf(deviceEdgeInfo.getFailCode() != null ? deviceEdgeInfo.getFailCode() : ""))) {
        } else if (logicHandler03(deviceNo, deviceOldNo, mac, currDeviceInfo, oldDeviceInfo, deviceInfo, String.valueOf(deviceEdgeInfo.getFailCode() != null ? deviceEdgeInfo.getFailCode() : ""))) {
        } else if (logicHandler04(deviceNo, deviceOldNo, mac, currDeviceInfo, oldDeviceInfo, deviceInfo, String.valueOf(deviceEdgeInfo.getFailCode() != null ? deviceEdgeInfo.getFailCode() : ""))) {
        } else if (logicHandler05(deviceNo, deviceOldNo, mac, currDeviceInfo, oldDeviceInfo, deviceInfo)) {
        } else if (logicHandler06(deviceNo, deviceOldNo, mac, currDeviceInfo, oldDeviceInfo, deviceInfo)) {
        } else if (logicHandler07(deviceNo, deviceOldNo, mac, currDeviceInfo, oldDeviceInfo, deviceInfo)) {
        } else {
            //  * 8、其他情况，比如编号规则不合法，设备编号取值不合规等，都返回注册失败：失败
            aurineEdgeHandlerUtil.sendDeviceError(deviceOldNo, deviceInfo, DeviceRegAbnormalEnum.NORMAL);
        }
        return true;
    }

    /**
     * * 1、当设备编号在平台上未登记，且**已注册设备编号**为空，可注册成功`
     */
    private boolean logicHandler01(String deviceNo, String deviceOldNo, String mac, ProjectDeviceInfo currDeviceInfo, ProjectDeviceInfo oldDeviceInfo, ProjectDeviceInfo deviceInfo, String failCode) {
        //设备编号00000 允许重复
        boolean flag = false;
        if (StringUtils.isNumeric(deviceNo)) {
            Long deviceNoInt = Long.valueOf(deviceNo);
            flag = deviceNoInt == 0;
        }

        if ((flag || currDeviceInfo == null) && StringUtils.isEmpty(deviceOldNo)) {
            String groupId = aurineEdgeDeviceServiceImplV1.getDeviceGroupId(ProjectContextHolder.getProjectId(), 1);
            List<String> deviceIdList = new ArrayList<>();
            deviceIdList.add(deviceInfo.getThirdpartyCode());
            aurineEdgeDeviceServiceImplV1.addDeviceToDeviceGroup(groupId, deviceIdList, ProjectContextHolder.getProjectId(), 1);

            projectDeviceInfoService.save(deviceInfo);
            log.info("[冠林边缘网关] 自动注册设备成功 设备信息：{}", deviceInfo);
            aurineEdgeHandlerUtil.checkDeviceError(deviceInfo, failCode);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 2、当设备编号在平台上未登记，且**已注册设备编号**非空且在平台上已登记，可注册成功，此时将平台中**已注册设备编号**替换成当前设备编号，即替换设备，并记录该动作（被替换）
     */
    private boolean logicHandler02(String deviceNo, String deviceOldNo, String mac, ProjectDeviceInfo currDeviceInfo, ProjectDeviceInfo oldDeviceInfo, ProjectDeviceInfo deviceInfo, String failCode) {
        if (currDeviceInfo == null && StringUtils.isNotEmpty(deviceOldNo) && oldDeviceInfo != null) {

            // 这里将现有设备的数据备份到设备替换信息表中(既更新旧设备数据， 同时生成替换记录)
            ProjectDeviceReplaceInfo deviceReplaceInfo = new ProjectDeviceReplaceInfo();
            deviceReplaceInfo.setReplaceReason("设备管理-自动上报设备替换");
            deviceReplaceInfo.setReplaceTime(LocalDateTime.now());
            BeanUtil.copyProperties(oldDeviceInfo, deviceReplaceInfo);
            projectDeviceReplaceInfoService.save(deviceReplaceInfo);

            //更新设备信息
            BeanUtil.copyProperties(deviceInfo, oldDeviceInfo);
            projectDeviceInfoService.updateById(oldDeviceInfo);
            log.info("[冠林边缘网关] 自动注册设备成功 更新设备信息：{}", oldDeviceInfo);
            aurineEdgeHandlerUtil.checkDeviceError(deviceInfo, failCode);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 3、当设备编号在平台上已登记（已登记的类型为手动添加设备），且**已注册设备编号**为空，已登记mac与当前上报设备mac相等，则可注册成功，此时将当前平台中手动设备替换成自动注册设备，并更新设备信息
     */
    private boolean logicHandler03(String deviceNo, String deviceOldNo, String mac, ProjectDeviceInfo currDeviceInfo, ProjectDeviceInfo oldDeviceInfo, ProjectDeviceInfo deviceInfo, String failCode) {
        if (currDeviceInfo != null && StringUtils.equals(currDeviceInfo.getAccessMethod(), DeviceAccessMethodEnum.MANUAL.code)
                && StringUtils.isEmpty(deviceOldNo)
                && StringUtils.equals(mac, currDeviceInfo.getMac())
        ) {

            //，可注册成功，此时将平台中**已注册设备编号**替换成当前设备编号，即替换设备，并记录该动作（被替换）
            ProjectDeviceReplaceInfo deviceReplaceInfo = new ProjectDeviceReplaceInfo();
            deviceReplaceInfo.setReplaceReason("设备管理-自动上报设备替换");
            deviceReplaceInfo.setReplaceTime(LocalDateTime.now());
            BeanUtil.copyProperties(currDeviceInfo, deviceReplaceInfo);
            projectDeviceReplaceInfoService.save(deviceReplaceInfo);

            //更新设备信息
            BeanUtil.copyProperties(deviceInfo, currDeviceInfo);
            currDeviceInfo.setAccessMethod(DeviceAccessMethodEnum.AUTO.code);
            projectDeviceInfoService.updateById(currDeviceInfo);
            log.info("[冠林边缘网关] 自动注册设备成功 更新设备信息：{}", currDeviceInfo);
            aurineEdgeHandlerUtil.checkDeviceError(deviceInfo, failCode);

            return true;
        } else {
            return false;
        }
    }

    /**
     * 4、当设备编号在平台上已登记，且**已注册设备编号**与当前设备编号相同，可注册成功，此时更新设备信息而非注册设备
     */
    private boolean logicHandler04(String deviceNo, String deviceOldNo, String mac, ProjectDeviceInfo currDeviceInfo, ProjectDeviceInfo oldDeviceInfo, ProjectDeviceInfo deviceInfo, String failCode) {
        if (currDeviceInfo != null && oldDeviceInfo != null && StringUtils.equals(deviceNo, deviceOldNo)) {

            //可注册成功，此时更新设备信息而非注册设备
//            ProjectDeviceReplaceInfo deviceReplaceInfo = new ProjectDeviceReplaceInfo();
//            deviceReplaceInfo.setReplaceReason("设备管理-自动上报设备替换");
//            deviceReplaceInfo.setReplaceTime(LocalDateTime.now());
//            BeanUtil.copyProperties(currDeviceInfo, deviceReplaceInfo);
//            projectDeviceReplaceInfoService.save(deviceReplaceInfo);

            //更新设备信息
            BeanUtil.copyProperties(deviceInfo, currDeviceInfo);
            currDeviceInfo.setAccessMethod(DeviceAccessMethodEnum.AUTO.code);
            projectDeviceInfoService.updateById(currDeviceInfo);
            log.info("[冠林边缘网关] 自动注册设备成功 更新设备信息：{}", currDeviceInfo);
            aurineEdgeHandlerUtil.checkDeviceError(deviceInfo, failCode);

            return true;
        } else {
            return false;
        }
    }

    /**
     * 5、当前设备编号在平台上未登记，且**已注册设备编号**非空且在平台上未登记，返回注册失败：不存在（可能在其他上位机上登记过的，不允许使用）
     */
    private boolean logicHandler05(String deviceNo, String deviceOldNo, String mac, ProjectDeviceInfo currDeviceInfo, ProjectDeviceInfo oldDeviceInfo, ProjectDeviceInfo deviceInfo) {
        if (currDeviceInfo == null && oldDeviceInfo == null && StringUtils.isNotEmpty(deviceOldNo)) {

            //非空且在平台上未登记，返回注册失败：不存在（可能在其他上位机上登记过的，不允许使用）
            log.info("[冠林边缘网关] 自动注册设备失败 ：旧编号 {} 在系统中不存在，可能在其他上位机上登记过的", deviceOldNo);
            aurineEdgeHandlerUtil.sendDeviceError(deviceOldNo, deviceInfo, DeviceRegAbnormalEnum.DEVICE_FORBIDDEN);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 6、当设备编号在平台上已登记（已登记的类型为自动添加设备），且**已注册设备编号**为空，返回注册失败：已存在（编号重复注册）
     */
    private boolean logicHandler06(String deviceNo, String deviceOldNo, String mac, ProjectDeviceInfo currDeviceInfo, ProjectDeviceInfo oldDeviceInfo, ProjectDeviceInfo deviceInfo) {
        if (currDeviceInfo != null && StringUtils.equals(currDeviceInfo.getAccessMethod(), DeviceAccessMethodEnum.AUTO.code)
                && StringUtils.isEmpty(deviceOldNo)
        ) {

            //失败，设备已存在（编号重复注册，忽略操作）
            log.info("[冠林边缘网关] 自动注册被忽略，设备已存在");
            return true;
        } else {
            return false;
        }
    }

    /**
     * 7、当设备编号在平台上已登记（已登记的类型为手动添加设备），已登记mac与当前上报设备mac不等，返回注册失败：编号重复注册
     */
    private boolean logicHandler07(String deviceNo, String deviceOldNo, String mac, ProjectDeviceInfo currDeviceInfo, ProjectDeviceInfo oldDeviceInfo, ProjectDeviceInfo deviceInfo) {
        if (currDeviceInfo != null && StringUtils.equals(currDeviceInfo.getAccessMethod(), DeviceAccessMethodEnum.MANUAL.code)
                && !StringUtils.equals(mac, currDeviceInfo.getMac())
        ) {

            //失败，编号重复注册
            log.info("[冠林边缘网关] 自动注册被忽略，编号重复注册");
            return true;
        } else {
            return false;
        }
    }


}
