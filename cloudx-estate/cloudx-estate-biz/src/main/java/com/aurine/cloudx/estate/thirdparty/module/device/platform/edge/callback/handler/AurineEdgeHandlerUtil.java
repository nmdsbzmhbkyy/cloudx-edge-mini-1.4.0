package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.callback.handler;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.constant.enums.DeviceTypeEnum;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.DeviceAccessMethodEnum;
import com.aurine.cloudx.estate.constant.enums.device.DeviceRegAbnormalEnum;
import com.aurine.cloudx.estate.constant.enums.device.DeviceRegParamEnum;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineEdgeDevResTypeEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineEdgeOnlineStatusEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgeDeviceInfoDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.exception.entity.DeviceAbnormalHandleInfo;
import com.aurine.cloudx.estate.vo.ProjectBuildingInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021/09/29
 * @Copyright:
 */
@Component
@Slf4j
public class AurineEdgeHandlerUtil {

    @Resource
    private ProjectDeviceRegionService projectDeviceRegionService;
    @Resource
    private SysDeviceProductMapService sysDeviceProductMapService;
    @Resource
    private ProjectBuildingInfoService projectBuildingInfoService;
    @Resource
    private ProjectFrameInfoService projectFrameInfoService;
    @Resource
    private ProjectEntityLevelCfgService projectEntityLevelCfgService;
    @Resource
    private ProjectDeviceAbnormalService projectDeviceAbnormalService;
    @Resource
    private SysDeviceTypeModelService sysDeviceTypeModelService;
    @Resource
    private SysDeviceTypeModelTypeConfigService sysDeviceTypeModelTypeConfigService;

    /**
     * 校验设备数据是否存在异常
     *
     * @param deviceInfo
     */
    Boolean checkDeviceError(ProjectDeviceInfo deviceInfo, String failCode) {
        DeviceAbnormalHandleInfo deviceAbnormalHandleInfo = new DeviceAbnormalHandleInfo();
        BeanUtil.copyProperties(deviceInfo, deviceAbnormalHandleInfo);
        deviceAbnormalHandleInfo.setDStatus(deviceInfo.getStatus());
        deviceAbnormalHandleInfo.setDeviceDesc(deviceInfo.getDeviceName());
        deviceAbnormalHandleInfo.setThirdpartyCode(deviceInfo.getThirdpartyCode());
        deviceAbnormalHandleInfo.setSn(deviceInfo.getSn());
        deviceAbnormalHandleInfo.setFailCode(failCode);
        deviceAbnormalHandleInfo.addParam(DeviceRegParamEnum.IPV4, deviceInfo.getIpv4())
                .addParam(DeviceRegParamEnum.MAC, deviceInfo.getMac())
                .addParam(DeviceRegParamEnum.DEVICE_NO, deviceInfo.getDeviceCode());
        return projectDeviceAbnormalService.checkAbnormal(deviceAbnormalHandleInfo);
    }

    /**
     * 发送设备异常记录
     *
     * @param deviceInfo
     */
    void sendDeviceError(String deviceOldNo, ProjectDeviceInfo deviceInfo, DeviceRegAbnormalEnum abnormalEnum) {
        DeviceAbnormalHandleInfo deviceAbnormalHandleInfo = new DeviceAbnormalHandleInfo();
        BeanUtil.copyProperties(deviceInfo, deviceAbnormalHandleInfo);
        deviceInfo.setDeviceId(null);
        deviceAbnormalHandleInfo.setThirdpartyCode(deviceInfo.getThirdpartyCode());
        deviceAbnormalHandleInfo.setDStatus(deviceInfo.getStatus());
        deviceAbnormalHandleInfo.setDeviceDesc(deviceInfo.getDeviceName());
        deviceAbnormalHandleInfo.setFailCode(abnormalEnum.thirdCode);
        deviceAbnormalHandleInfo.setDeviceCodeReg(deviceOldNo);
        deviceAbnormalHandleInfo.setSn(deviceInfo.getSn());
        deviceAbnormalHandleInfo.addParam(DeviceRegParamEnum.IPV4, deviceInfo.getIpv4())
                .addParam(DeviceRegParamEnum.MAC, deviceInfo.getMac())
                .addParam(DeviceRegParamEnum.DEVICE_NO, deviceInfo.getDeviceCode());
        projectDeviceAbnormalService.checkAbnormal(deviceAbnormalHandleInfo);
    }


    /**
     * 将数据转换成Cloudx所使用的设备对象
     *
     * @param deviceEdgeInfo
     * @return
     */
    ProjectDeviceInfo toCloudxPo(AurineEdgeDeviceInfoDTO deviceEdgeInfo) {
        ProjectDeviceInfo deviceInfo = new ProjectDeviceInfo();

        deviceInfo.setDeviceId(UUID.randomUUID().toString().replaceAll("-", ""));
        deviceInfo.setDeviceCode(deviceEdgeInfo.getDeviceNo());
        deviceInfo.setThirdpartyCode(deviceEdgeInfo.getDevId());
        deviceInfo.setDeviceName(deviceEdgeInfo.getName());
        deviceInfo.setStatus(AurineEdgeOnlineStatusEnum.getByCode(String.valueOf(deviceEdgeInfo.getDevStatus())).cloudCode);
        deviceInfo.setActive("1");
        deviceInfo.setSn(deviceEdgeInfo.getDevSN());
        deviceInfo.setAccessMethod(DeviceAccessMethodEnum.AUTO.code);
        deviceInfo.setResType(deviceEdgeInfo.getDevResType());
        deviceInfo.setDeviceCapabilities(deviceEdgeInfo.getCapabilitiesString());

        ProjectDeviceRegion deviceRegion = projectDeviceRegionService.getPublicRegion();

        if (deviceRegion != null) {
//            deviceInfo.setDeviceRegionName(deviceRegion.getRegionName());//设置默认的区域名称
            deviceInfo.setDeviceRegionId(deviceRegion.getRegionId());//获取默认的区域
        }

        deviceInfo.setMac(deviceEdgeInfo.getMac());
        deviceInfo.setIpv4(deviceEdgeInfo.getExtParam().getDeviceIp());
        deviceInfo.setPort(deviceEdgeInfo.getExtParam().getDevicePort());

        //区分设备类型
        String code = getDeviceType(deviceEdgeInfo).getCode();
        deviceInfo.setDeviceType(code);

        SysDeviceProductMap sysDeviceProductMap = sysDeviceProductMapService.getByProductCode(deviceEdgeInfo.getProductId());
        if (sysDeviceProductMap != null) {
            deviceInfo.setProductId(sysDeviceProductMap.getProductId());
            // 分层控制器、乘梯识别终端、状态监测器设备设置品牌型号信息
            if (DeviceTypeEnum.ELEVATOR_LAYER_CONTROL_DEVICE.getCode().equals(code) || DeviceTypeEnum.ELEVATOR_RECOGNIZER_DEVICE.getCode().equals(code) || DeviceTypeEnum.ELEVATOR_STATE_DETECTOR_DEVICE.getCode().equals(code)) {
                deviceInfo.setBrand(sysDeviceProductMap.getProductModel());
            }
        } else {
            log.error("[冠林边缘网关] deviceObjInfo转换为系统对象时出现错误数据，在系统中未找到产品信息：{}", deviceEdgeInfo.getProductId());
        }

        log.info("设备编号解析前:{}", JSONObject.toJSONString(deviceInfo));
        //设置设备的框架关联
        //如果是梯口机，绑定楼栋、单元数据
        //如果是室内机，绑定楼栋、单元和房屋数据
        this.setDeviceFrame(deviceInfo);
        log.info("设备编号解析后:{}", JSONObject.toJSONString(deviceInfo));
        //针对报警主机设置设备名
        this.setDeviceName(deviceEdgeInfo, deviceInfo);


        return deviceInfo;
    }

    private void setDeviceName(AurineEdgeDeviceInfoDTO edgeInfo, ProjectDeviceInfo deviceInfo) {

//        if ("3E99ZJ01".equals(deviceInfo.getProductId())==false || "3Z99JHL01".equals(deviceInfo.getProductId())==false ) {
//            log.debug("");
//            return;
//        } else{
//            return;
//        }

        if ("3E99ZJ01".equals(edgeInfo.getProductId())) {
            String name = "报警主机驱动" + DateUtil.format(new Date(), "yyMMddhhmmss");
            deviceInfo.setDeviceName(name);
        }
        if (deviceInfo.getDeviceType().equals(DeviceTypeEnum.ALARM_DEVICE.getCode())) {
            String name = "报警主机" + DateUtil.format(new Date(), "yyMMddhhmmss");

            deviceInfo.setDeviceName(name);
            deviceInfo.setSn(edgeInfo.getParamDevId());
        }
    }

    /**
     * 将数据转换成Cloudx所使用的设备对象
     *
     * @param deviceEdgeInfo
     * @return
     */
    ProjectDeviceInfo toCloudxPoNotSetName(AurineEdgeDeviceInfoDTO deviceEdgeInfo,ProjectDeviceInfo currDeviceInfo) {
        String accessMethod = DeviceAccessMethodEnum.AUTO.code;
        if(StringUtils.isEmpty(deviceEdgeInfo.getAutoReg())){
            if(deviceEdgeInfo.getExtParam() != null && StrUtil.isNotEmpty(deviceEdgeInfo.getExtParam().getAutoReg())){
                DeviceAccessMethodEnum accessMethodEnum = DeviceAccessMethodEnum.getCode(deviceEdgeInfo.getExtParam().getAutoReg());
                accessMethod = accessMethodEnum.code;
            }
        }else{
            DeviceAccessMethodEnum accessMethodEnum = DeviceAccessMethodEnum.getCode(deviceEdgeInfo.getAutoReg());
            accessMethod = accessMethodEnum.code;
        }

        ProjectDeviceInfo deviceInfo = new ProjectDeviceInfo();

        deviceInfo.setDeviceId(UUID.randomUUID().toString().replaceAll("-", ""));
        deviceInfo.setDeviceCode(deviceEdgeInfo.getDeviceNo());
        deviceInfo.setThirdpartyCode(deviceEdgeInfo.getDevId());
        deviceInfo.setStatus(AurineEdgeOnlineStatusEnum.getByCode(String.valueOf(deviceEdgeInfo.getDevStatus())).cloudCode);
        deviceInfo.setActive("1");
        deviceInfo.setSn(deviceEdgeInfo.getDevSN());
        deviceInfo.setAccessMethod(accessMethod);
        deviceInfo.setResType(deviceEdgeInfo.getDevResType());
        deviceInfo.setDeviceCapabilities(deviceEdgeInfo.getCapabilitiesString());

        ProjectDeviceRegion deviceRegion = projectDeviceRegionService.getPublicRegion();

        if (deviceRegion != null) {
//            deviceInfo.setDeviceRegionName(deviceRegion.getRegionName());//设置默认的区域名称
            deviceInfo.setDeviceRegionId(deviceRegion.getRegionId());//获取默认的区域
        }

        deviceInfo.setMac(deviceEdgeInfo.getMac());
        deviceInfo.setIpv4(deviceEdgeInfo.getExtParam().getDeviceIp());
        deviceInfo.setPort(deviceEdgeInfo.getExtParam().getDevicePort());


        SysDeviceProductMap sysDeviceProductMap = sysDeviceProductMapService.getByProductCode(deviceEdgeInfo.getProductId());
        if (sysDeviceProductMap != null) {
            deviceInfo.setProductId(sysDeviceProductMap.getProductId());
        } else {
            log.error("[冠林边缘网关] deviceObjInfo转换为系统对象时出现错误数据，在系统中未找到产品信息：{}", deviceEdgeInfo.getProductId());
        }

        //区分设备类型
        String code = null;
        if (StrUtil.isEmpty(deviceEdgeInfo.getDevResType()) || "none".equals(deviceEdgeInfo.getDevResType())) {
            code = currDeviceInfo.getDeviceType();
        }else{
            code = getDeviceType(deviceEdgeInfo).getCode();
        }
        deviceInfo.setDeviceType(code);

        //设置设备的框架关联
        //如果是梯口机，绑定楼栋、单元数据
        //如果是室内机，绑定楼栋、单元和房屋数据
        this.setDeviceFrame(deviceInfo);


        return deviceInfo;
    }

    /**
     * 解析设备类型
     *
     * @param deviceInfo
     * @return
     */
    private DeviceTypeEnum getDeviceType(AurineEdgeDeviceInfoDTO deviceInfo) {
        if (StrUtil.isEmpty(deviceInfo.getDevResType()) || "none".equals(deviceInfo.getDevResType())) {
            SysDeviceProductMap sysDeviceProductMap = sysDeviceProductMapService.getByProductCode(deviceInfo.getProductId());
            //SysDeviceTypeModel deviceTypeModel = sysDeviceTypeModelService.getOne(new LambdaQueryWrapper<SysDeviceTypeModel>().eq(SysDeviceTypeModel::getProductId, sysDeviceProductMap.getProductId()).last("limit 1"));
            //从 配置第三方产品和平台设备类型关联关系表里取设备类型
            SysDeviceTypeModelTypeConfig sysDeviceTypeModelTypeConfig = sysDeviceTypeModelTypeConfigService.getOne(Wrappers.lambdaQuery(SysDeviceTypeModelTypeConfig.class)
                    .eq(SysDeviceTypeModelTypeConfig::getProductModelType, sysDeviceProductMap.getModelType())
                    .eq(SysDeviceTypeModelTypeConfig::getPlatformName, PlatformEnum.AURINE_EDGE_MIDDLE.value)
                    .last("limit 1"));
            return DeviceTypeEnum.getByCode(sysDeviceTypeModelTypeConfig.getDeviceTypeId());
        }
        return AurineEdgeDevResTypeEnum.getByCode(deviceInfo.getDevResType()).deviceType;
    }

    /**
     * 设置设备房屋框架
     *
     * @param deviceInfo
     */
    private void setDeviceFrame(ProjectDeviceInfo deviceInfo) {
        DeviceTypeEnum deviceTypeEnum = DeviceTypeEnum.getByCode(deviceInfo.getDeviceType());

        if (deviceTypeEnum == DeviceTypeEnum.LADDER_WAY_DEVICE || deviceTypeEnum == DeviceTypeEnum.INDOOR_DEVICE || deviceTypeEnum == DeviceTypeEnum.ELEVATOR_LAYER_CONTROL_DEVICE || deviceTypeEnum == DeviceTypeEnum.ELEVATOR_RECOGNIZER_DEVICE) {
            log.debug("");
        } else {
            return;
        }

        String deviceNo = deviceInfo.getDeviceCode();
        if (StringUtils.isEmpty(deviceNo)) {
            log.error("[冠林边缘网关] 设备自动注册失败，设备编号为空 ： {}", deviceInfo);
            throw new RuntimeException("设备自动注册失败，设备编号为空");
        }


        //获取框架层级配置信息，进行配对
        //初始化框架号规则信息
        List<ProjectEntityLevelCfg> entityLevelCfgList = projectEntityLevelCfgService.list(new QueryWrapper<ProjectEntityLevelCfg>().lambda().eq(ProjectEntityLevelCfg::getProjectId, ProjectContextHolder.getProjectId()).orderByAsc(ProjectEntityLevelCfg::getLevel));

        if (CollUtil.isEmpty(entityLevelCfgList)) {
            log.error("[冠林边缘网关] 设备自动注册失败, 项目{} 无框架数据，无法进行设备注册", ProjectContextHolder.getProjectId());
            throw new RuntimeException("当前项目无框架数据，请进行系统初始化");
        }

        String[] frameNoArray = new String[entityLevelCfgList.size()];//框架值数组
        int[] frameRuleArray = new int[entityLevelCfgList.size()];//框架规则数组
        int frameTotalLength = 0;//框架号总字符长度

        //解析框架号规则
        for (int i = 0; i < entityLevelCfgList.size(); i++) {
            if (StringUtils.equals(entityLevelCfgList.get(i).getIsDisable(), "0")) {//启用的框架
                frameRuleArray[i] = entityLevelCfgList.get(i).getCodeRule();
                frameTotalLength += entityLevelCfgList.get(i).getCodeRule();
            } else {
                frameRuleArray[i] = 0;//未启用的框架层级，规则设置为0
            }
        }

        if (deviceNo.length() < frameTotalLength) {
            log.error("[冠林边缘网关] 设备自动注册失败，设备号长度小于分段参数: {} 设备信息： {}", frameRuleArray, deviceInfo);

            DeviceAbnormalHandleInfo deviceAbnormalHandleInfo = new DeviceAbnormalHandleInfo();
            BeanUtil.copyProperties(deviceInfo, deviceAbnormalHandleInfo);
            deviceAbnormalHandleInfo.setDStatus(deviceInfo.getStatus());
            deviceAbnormalHandleInfo.setDeviceDesc(deviceInfo.getDeviceName());
            deviceAbnormalHandleInfo.setThirdpartyCode(deviceInfo.getThirdpartyCode());
            deviceAbnormalHandleInfo.setSn(deviceInfo.getSn());
            deviceAbnormalHandleInfo.addParam(DeviceRegParamEnum.DEVICE_NO, deviceInfo.getDeviceCode());
            deviceAbnormalHandleInfo.setProjectId(deviceInfo.getProjectId());
            projectDeviceAbnormalService.checkAbnormal(deviceAbnormalHandleInfo);
            throw new RuntimeException("设备自动注册失败");
        }

        int beginIndex = 0;
        int endIndex = 0;
        //根据框架号规则拆分设备编号
        for (int i = 0; i < frameRuleArray.length; i++) {

            endIndex = beginIndex + frameRuleArray[frameRuleArray.length - i - 1];
            frameNoArray[i] = deviceNo.substring(beginIndex, endIndex);
            beginIndex += frameRuleArray[frameRuleArray.length - i - 1];

        }


        //解析框架号
        log.info("[冠林边缘网关] 自动注册设备 解析框架号：{}", frameNoArray);

        String buildingFrameNo = getFrameNo(frameNoArray, 0, 4);
        String unitFrameNo = getFrameNo(frameNoArray, 0, 5);
        String houseFrameNo = getFrameNo(frameNoArray, 0, 6);

        ProjectFrameInfo unit = null;
        ProjectBuildingInfoVo building = null;
        ProjectFrameInfo house = null;


        switch (deviceTypeEnum) {
            case LADDER_WAY_DEVICE:
            case ELEVATOR_LAYER_CONTROL_DEVICE:
            case ELEVATOR_RECOGNIZER_DEVICE:
                unit = projectFrameInfoService.getByFrameNo(unitFrameNo);

                if (unit != null) {
                    building = projectBuildingInfoService.getById(unit.getPuid());
                    deviceInfo.setBuildingId(building.getBuildingId());
                    deviceInfo.setUnitId(unit.getEntityId());
                    deviceInfo.setDeviceRegionId(building.getRegionId());
                } else {
                    log.error("[冠林边缘网关] 自动注册梯口机失败 未找到单元：{}", unitFrameNo);
                }
                break;
            case INDOOR_DEVICE:

                house = projectFrameInfoService.getByFrameNo(houseFrameNo);
                if (house != null) {
                    unit = projectFrameInfoService.getParent(house.getEntityId());
                    building = projectBuildingInfoService.getById(unit.getPuid());

                    deviceInfo.setBuildingId(unit.getPuid());
                    deviceInfo.setUnitId(unit.getEntityId());
                    deviceInfo.setHouseId(house.getEntityId());
                    deviceInfo.setDeviceRegionId(building.getRegionId());
                } else {
                    log.error("[冠林边缘网关] 自动注册室内机失败 未找到房屋：{}", houseFrameNo);
                }
                break;
            default:
                return;
        }
    }


    /**
     * 从frame数组中拼接框架号
     *
     * @param frameNoArray
     * @param begin
     * @param end
     * @return
     */
    private String getFrameNo(String[] frameNoArray, int begin, int end) {
        String result = "";
        for (int i = 0; i < frameNoArray.length; i++) {
            if (i >= begin && i <= end) {
                result += frameNoArray[i];
            }
        }
        return result;
    }

}
