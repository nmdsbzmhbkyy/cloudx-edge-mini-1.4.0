package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.constant.DeviceTypeConstants;
import com.aurine.cloudx.estate.constant.enums.DeviceParamEnum;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectPerimeterAlarmArea;
import com.aurine.cloudx.estate.entity.ProjectPerimeterAlarmEvent;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.PerimeterAlarmDeviceService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.AsyncNotifyHelper;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.callback.impl.ProjectPerimeterAlarmAreaServiceHelper;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.config.AurineEdgeConfigDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.core.AurineEdgeDataConnector;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.core.util.AurineEdgeUtil;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineEdgeActionConstant;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineEdgeErrorEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineEdgeObjNameEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineEdgeServiceEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgePerimeterDeviceParamsDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgeRequestObject;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgeRespondDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.config.HuaweiConfigDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant.HuaweiCommandConstant;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant.HuaweiServiceEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.dto.HuaweiRespondDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.remote.factory.HuaweiRemoteDeviceOperateServiceFactory;
import com.aurine.cloudx.estate.thirdparty.remote.edge.ali.remote.factory.AliRemotePerimeterAlarmServiceFactory;
import com.aurine.cloudx.estate.thirdparty.transport.mq.MQDataTransporter;
import com.aurine.project.entity.Message;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.remote.factory.AurineEdgeRemoteDeviceOperateServiceFactory;

/**
 * 冠林边缘网关 V1 版本 对接业务实现
 *
 * @ClassName:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-07-31 13:44
 * @Copyright:
 */
@Service
@Slf4j
public class AurineEdgePerimeterAlarmServiceImplV1 implements PerimeterAlarmDeviceService {

    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;
    @Resource
    private SysDeviceTypeThirdPartyConfigService sysDeviceTypeThirdPartyConfigService;
    @Resource
    private SysDeviceProductMapService sysDeviceProductMapService;
    @Resource
    private ProjectBuildingInfoService projectBuildingInfoService;
    @Resource
    private ProjectDeviceInfoProxyService projectDeviceInfoProxyService;
    @Resource
    private ProjectInfoService projectInfoService;
    @Resource
    private AsyncNotifyHelper asyncNotifyHelper;
    @Resource
    private MQDataTransporter mqDataTransporter;
    @Resource
    private AurineEdgeDataConnector aurineEdgeDataConnector;
    @Resource
    private ProjectPerimeterAlarmAreaServiceHelper projectPerimeterAlarmAreaServiceHelper;
    @Resource
    private ProjectPerimeterAlarmAreaService projectPerimeterAlarmAreaService;

    private boolean waitMoment(String key){
        int count=0;
        while(asyncNotifyHelper.getAsyncNotifyMap().containsKey(key)==false && count<1000){
            try {
                Thread.sleep(10);
                count++;
            }catch (InterruptedException e){

            }
        }
        return false;
    }

    /**
     * 同步通道
     *
     * @param deviceInfo 设备
     * @return
     */
    @Override
    public List<ProjectPerimeterAlarmArea> queryChannel(ProjectDeviceInfo deviceInfo)  {
        List<ProjectPerimeterAlarmArea> resultList = new ArrayList<>();
        AurineEdgeConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceId(deviceInfo.getDeviceId(), AurineEdgeConfigDTO.class);
        JSONObject objInfo=new JSONObject();
        objInfo.put("channelList",new ArrayList<String>());
        //获取通道信息

//        AurineEdgeRespondDTO respDTO= AurineEdgeRemoteDeviceOperateServiceFactory.getInstance(this.getVer())
//                       .objectManageSync(config,"",deviceInfo.getThirdpartyCode(), AurineEdgeServiceEnum.CHANNEl_LIST_QUERY.code, AurineEdgeActionConstant.GET,"",objInfo, null);
        AurineEdgeRespondDTO respDTO= AurineEdgeRemoteDeviceOperateServiceFactory.getInstance(this.getVer())
                .objectManage(config,deviceInfo.getThirdpartyCode(), AurineEdgeServiceEnum.CHANNEl_LIST_QUERY.code, AurineEdgeActionConstant.GET,"",objInfo, null);


        //========================同步操作=============================
//        String key=MessageFormat.format("{0}_{1}_{2}"
//                ,deviceInfo.getDeviceId()
//                ,AurineEdgeServiceEnum.CHANNEl_LIST_QUERY.code
//                ,AurineEdgeActionConstant.GET);
//        waitMoment(key);
//        List<String> channelList=(List<String>) asyncNotifyHelper.getAsyncNotifyMap().get(key);
//        asyncNotifyHelper.getAsyncNotifyMap().remove(key);
//        waitMoment(key);
        if (respDTO.getErrorEnum() == AurineEdgeErrorEnum.SUCCESS) {
            JSONObject bodyObj = respDTO.getBodyObj();

            ProjectPerimeterAlarmArea[] array = JSONUtil.toBean(bodyObj.toString(), new TypeReference<ProjectPerimeterAlarmArea[]>() {
            }, false);

            resultList = ListUtil.toList(array);
        }else{
            log.error("同步通道错误！");
        }

        //==========================================

        return resultList;
    }

    /**
     * 防区布防
     *
     * @param perimeterAlarmArea 防区（通道）
     * @return
     */
    @Override
    public boolean channelProtection(ProjectPerimeterAlarmArea perimeterAlarmArea) {
        //获取主机信息
        ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getProjectDeviceInfoById(perimeterAlarmArea.getDeviceId());
        AurineEdgeConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceId(deviceInfo.getDeviceId(), AurineEdgeConfigDTO.class);

        JSONObject objInfo=new JSONObject();
        String testStr= String.format("Module%03dZone%03d",Integer.parseInt(perimeterAlarmArea.getModuleNo()),Integer.parseInt(perimeterAlarmArea.getChannelNo()));
        objInfo.put("channelID",testStr);
        objInfo.put("operation","1");
        AurineEdgeRespondDTO respDTO=
                AurineEdgeRemoteDeviceOperateServiceFactory
                        .getInstance(this.getVer())
                        .objectManage(config,deviceInfo.getThirdpartyCode(),AurineEdgeServiceEnum.CHANNEl_OPERATION.code,AurineEdgeActionConstant.SET,"DevParamObj",objInfo,null);
        if (respDTO.getErrorEnum() == AurineEdgeErrorEnum.SUCCESS) {
            return true;
        }
        return false;
    }

    /**
     * 防区撤防
     *
     * @param perimeterAlarmArea 防区（通道）
     * @return
     */
    @Override
    public boolean channelRemoval(ProjectPerimeterAlarmArea perimeterAlarmArea) {
        //获取主机信息
        ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getProjectDeviceInfoById(perimeterAlarmArea.getDeviceId());
        AurineEdgeConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceId(deviceInfo.getDeviceId(), AurineEdgeConfigDTO.class);
        JSONObject objInfo=new JSONObject();
        String testStr= String.format("Module%03dZone%03d",Integer.parseInt(perimeterAlarmArea.getModuleNo()),Integer.parseInt(perimeterAlarmArea.getChannelNo()));
        objInfo.put("channelID",testStr);
        objInfo.put("operation","0");
        AurineEdgeRespondDTO respDTO=
                AurineEdgeRemoteDeviceOperateServiceFactory
                        .getInstance(this.getVer())
                        .objectManage(config,deviceInfo.getThirdpartyCode(),AurineEdgeServiceEnum.CHANNEl_OPERATION.code,AurineEdgeActionConstant.SET,"DevParamObj",objInfo,null);
        if (respDTO.getErrorEnum() == AurineEdgeErrorEnum.SUCCESS) {
            return true;
        }
        return false;
    }

    /**
     * 消除防区告警
     *
     * @param perimeterAlarmArea 防区（通道）
     */
    @Override
    public boolean clearAlarm(ProjectPerimeterAlarmArea perimeterAlarmArea) {
        //获取主机信息
        ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getProjectDeviceInfoById(perimeterAlarmArea.getDeviceId());
        JSONObject respJson = AliRemotePerimeterAlarmServiceFactory.getInstance(this.getVer())
                .clearAlarm(deviceInfo.getSn(), perimeterAlarmArea.getChannelNo(), deviceInfo.getProjectId());

        return handleResp(respJson);

    }
    /**
     * 消除防区告警-指定报警类型
     *
     * @param projectPerimeterAlarmEvent 告警事件
     */
    @Override
    public boolean clearAlarm(ProjectPerimeterAlarmEvent projectPerimeterAlarmEvent) {
        //获取主机信息
        ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getProjectDeviceInfoById(projectPerimeterAlarmEvent.getDeviceId());
        AurineEdgeRespondDTO respondDTO=new AurineEdgeRespondDTO();
        if(deviceInfo.getDeviceType().equals(DeviceTypeConstants.ALARM_EQUIPMENT)){
            ProjectPerimeterAlarmArea perimeterAlarmArea=
                    projectPerimeterAlarmAreaService
                            .getOne(Wrappers.lambdaQuery(ProjectPerimeterAlarmArea.class).eq(ProjectPerimeterAlarmArea::getInfoUid,deviceInfo.getDeviceId()) );
            ProjectDeviceInfo deviceAlarmHostInfo =projectDeviceInfoService
                    .getOne(Wrappers.lambdaQuery(ProjectDeviceInfo.class).eq(ProjectDeviceInfo::getDeviceId,perimeterAlarmArea.getDeviceId()));

            AurineEdgeConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceId(deviceAlarmHostInfo.getDeviceId(), AurineEdgeConfigDTO.class);
            JSONObject objInfo = new JSONObject();
            String channelID=String.format("Module%03dZone%03d",Integer.parseInt(perimeterAlarmArea.getModuleNo()),Integer.parseInt(perimeterAlarmArea.getChannelNo()));
            objInfo.put("channelID", channelID);
            objInfo.put("alarmType", projectPerimeterAlarmEvent.getAlaramType());

//            respondDTO = AurineEdgeRemoteDeviceOperateServiceFactory.
//                    getInstance(getVer()).commandsDown(config, deviceAlarmHostInfo.getThirdpartyCode(), AurineEdgeServiceEnum.CLEAR_ALARM.code, HuaweiCommandConstant.CLEAR_ALARM, objInfo, null);

            respondDTO=AurineEdgeRemoteDeviceOperateServiceFactory.
                    getInstance(getVer()).objectManage(config, deviceAlarmHostInfo.getThirdpartyCode(), AurineEdgeServiceEnum.CLEAR_ALARM.code, HuaweiCommandConstant.CLEAR_ALARM,"channelAlarmObj",objInfo, null);

        }else {
            AurineEdgeConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceId(deviceInfo.getDeviceId(), AurineEdgeConfigDTO.class);
            JSONObject objInfo = new JSONObject();
            objInfo.put("channelID", projectPerimeterAlarmEvent.getChannelId());
            objInfo.put("alarmType", projectPerimeterAlarmEvent.getAlaramType());

            respondDTO = AurineEdgeRemoteDeviceOperateServiceFactory.
                    getInstance(getVer()).commandsDown(config, deviceInfo.getThirdpartyCode(), AurineEdgeServiceEnum.CLEAR_ALARM.code, HuaweiCommandConstant.CLEAR_ALARM, null, null);

        }
//        String key=MessageFormat.format("{0}_{1}_{2}"
//                ,deviceInfo.getDeviceId()
//                ,HuaweiServiceEnum.CLEAR_ALARM.code
//                ,HuaweiCommandConstant.CLEAR_ALARM);
//        waitMoment(key);
//        Boolean operateRes=(Boolean) asyncNotifyHelper.getAsyncNotifyMap().get(key);
//        asyncNotifyHelper.getAsyncNotifyMap().remove(key);
//
//        return operateRes;
        if (respondDTO.getErrorEnum()== AurineEdgeErrorEnum.SUCCESS) {
            //生成返回消息
            return true;

        } else {

            return false;
        }
    }

    @Override
    public boolean queryDevParams(String devId,AurineEdgePerimeterDeviceParamsDTO aurineEdgePerimeterDeviceParamsDTO) {
        AurineEdgeConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceId(devId, AurineEdgeConfigDTO.class);
        JSONObject objInfo=JSONObject.parseObject(JSON.toJSONString(aurineEdgePerimeterDeviceParamsDTO));

        AurineEdgeRespondDTO respondDTO  = AurineEdgeRemoteDeviceOperateServiceFactory.getInstance(this.getVer())
                .objectManage(config,devId, AurineEdgeServiceEnum.DEVICE_PARAM.code, AurineEdgeActionConstant.GET,"",objInfo,null);

        if (respondDTO.getErrorEnum()== AurineEdgeErrorEnum.SUCCESS) {
            //生成返回消息
            return true;

        } else {

            return false;
        }
    }

    @Override
    public boolean setDevParams(ProjectDeviceInfo deviceInfo,AurineEdgePerimeterDeviceParamsDTO aurineEdgePerimeterDeviceParamsDTO) {
        String devId=deviceInfo.getDeviceId();
        AurineEdgeConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceId(devId, AurineEdgeConfigDTO.class);

//        AurineEdgePerimeterDeviceParamsDTO aurineEdgePerimeterDeviceParamsDTO= projectPerimeterAlarmAreaServiceHelper.queryChannel(devId);
        JSONObject objInfo=JSONObject.parseObject(JSON.toJSONString(aurineEdgePerimeterDeviceParamsDTO));

        AurineEdgeRespondDTO respondDTO  = AurineEdgeRemoteDeviceOperateServiceFactory
                .getInstance(getVer())
                .commandsDown(config,deviceInfo.getThirdpartyCode(), AurineEdgeServiceEnum.DEVICE_PARAMS.code, AurineEdgeActionConstant.SET,objInfo,null);

        if (respondDTO.getErrorEnum()== AurineEdgeErrorEnum.SUCCESS) {
            //生成返回消息
            return true;

        } else {

            return false;
        }
    }

    /**
     * 通道状态查询（暂不支持）
     * @param deviceInfo 主机设备
     * @return
     */
    //@Override
    public List<ProjectPerimeterAlarmArea> queryChannelStatus(ProjectDeviceInfo deviceInfo) {
        List<ProjectPerimeterAlarmArea> resultList = new ArrayList<>();
        AurineEdgeConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceId(deviceInfo.getDeviceId(), AurineEdgeConfigDTO.class);
        JSONObject objInfo=new JSONObject();
        objInfo.put("channelList",new ArrayList<String>());
        //获取通道信息
        //AliRemotePerimeterAlarmServiceFactory.getInstance(this.getVer()).channelListQuery(deviceInfo.getSn(), deviceInfo.getProjectId());

        AurineEdgeRespondDTO respDTO= AurineEdgeRemoteDeviceOperateServiceFactory.getInstance(this.getVer())
                .objectManage(config,deviceInfo.getThirdpartyCode(), AurineEdgeServiceEnum.CHANNEl_STATUS_QUERY.code, AurineEdgeActionConstant.GET, AurineEdgeObjNameEnum.DevParamObj.code,objInfo, null);

        String key=MessageFormat.format("{0}_{1}_{2}"
                ,deviceInfo.getDeviceId()
                ,AurineEdgeServiceEnum.CHANNEl_LIST_QUERY.code
                ,AurineEdgeActionConstant.GET);
        waitMoment(key);
        List<String> channelList=(List<String>) asyncNotifyHelper.getAsyncNotifyMap().get(key);
        asyncNotifyHelper.getAsyncNotifyMap().remove(key);


        for (String channelNo : channelList) {
            ProjectPerimeterAlarmArea alarmArea = new ProjectPerimeterAlarmArea();
            alarmArea.setChannelNo(channelNo);
            alarmArea.setDeviceId(deviceInfo.getDeviceId());
            alarmArea.setArmedStatus("0");//默认给与撤防状态

            resultList.add(alarmArea);
        }


        return resultList;
    }


    /**
     * 获取版本
     *
     * @return
     */
    @Override
    public String getVersion() {
        return VersionEnum.V1.code;
    }

    /**
     * 获取版本
     *
     * @return
     */
    public VersionEnum getVer() {
        return VersionEnum.V1;
    }

    /**
     * 获取平台类型
     *
     * @return
     */
    @Override
    public String getPlatform() {
        return PlatformEnum.AURINE_EDGE_MIDDLE.code;
    }


    private boolean handleResp(JSONObject json) {
//        {"errorCode":1,"errorMsg":"操作失败"}
        String errorCode = json.getString("errorCode");
        if (StringUtils.equals("0", errorCode)) {
            return true;
        } else {
            String errorMsg = json.getString("errorMsg");
            log.error("[阿里边缘][周界报警] 操作失败：{}", json);
            return false;
        }

    }

}
