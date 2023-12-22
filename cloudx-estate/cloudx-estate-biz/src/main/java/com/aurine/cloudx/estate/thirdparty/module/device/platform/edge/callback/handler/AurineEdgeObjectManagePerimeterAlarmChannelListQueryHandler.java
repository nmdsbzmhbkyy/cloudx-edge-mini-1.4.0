package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.callback.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.constant.DeviceTypeConstants;
import com.aurine.cloudx.estate.component.chain.AbstractHandler;
import com.aurine.cloudx.estate.component.chain.annotation.ChainHandler;
import com.aurine.cloudx.estate.constant.enums.PerimeterStatusEnum;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectPerimeterAlarmArea;
import com.aurine.cloudx.estate.entity.ProjectRightDevice;
import com.aurine.cloudx.estate.service.ProjectDeviceInfoService;
import com.aurine.cloudx.estate.service.ProjectRightDeviceService;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.ResponseOperateConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.TopicConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.ResponseOperateDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.AsyncNotifyHelper;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.callback.impl.ProjectPerimeterAlarmAreaServiceHelper;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineEdgeActionConstant;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineEdgeFaceResultCodeEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineEdgeServiceEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.respond.CallBackData;
import com.aurine.cloudx.estate.util.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ChainHandler(chainClass = AurineEdgeObjectManageChain.class)
@Component
@Slf4j
public class AurineEdgeObjectManagePerimeterAlarmChannelListQueryHandler extends AbstractHandler<CallBackData> {
    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;
    @Resource
    private ProjectRightDeviceService projectRightDeviceService;
    @Resource
    private AsyncNotifyHelper asyncNotifyHelper;
    @Resource
    private ProjectPerimeterAlarmAreaServiceHelper projectPerimeterAlarmAreaServiceHelper;

    @Override
    public boolean filter(CallBackData handleEntity) {
        if (!StringUtils.equals(AurineEdgeServiceEnum.CHANNEl_LIST_QUERY.code, handleEntity.getOnNotifyData().getObjManagerData().getServiceId())) {
            return false;
        }

        if (!StringUtils.equals(AurineEdgeActionConstant.GET, handleEntity.getOnNotifyData().getObjManagerData().getAction())) {
            return false;
        }

        log.info("[冠林边缘网关] {} 开始周界报警通道列表查询返回结果", handleEntity);
        return true;
    }

    @Override
    public boolean doHandle(CallBackData handleEntity) {
        try {

            //执行下发成功的设备
            String deviceThirdCode = handleEntity.getOnNotifyData().getDevId();
            ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getByThirdPartyCode(deviceThirdCode);

            //获取下发的设备
            if (deviceInfo == null) {
                log.error("[冠林边缘网关] 周界报警通道列表查询失败，未找到下发的设备：deviceThirdCode = {}", deviceThirdCode);
                return done();
            }


            JSONObject objInfo = handleEntity.getOnNotifyData().getObjManagerData().getObjInfo();
            JSONArray channels = objInfo.getJSONArray("channelList");
            List<ProjectPerimeterAlarmArea> resultList = new ArrayList<>();
            if (channels == null || channels.size() == 0){
                return done();
            }

            for (String channelNo : channels.toJavaList(String.class)) {
                ProjectPerimeterAlarmArea alarmArea = new ProjectPerimeterAlarmArea();
                alarmArea.setChannelNo(channelNo);
                alarmArea.setDeviceId(deviceInfo.getDeviceId());
                alarmArea.setArmedStatus("0");//默认给与撤防状态

                resultList.add(alarmArea);
            }
//            String key= MessageFormat.format("{0}_{1}_{2}"
//                    ,deviceInfo.getDeviceId()
//                    ,handleEntity.getOnNotifyData().getObjManagerData().getServiceId()
//                    ,handleEntity.getOnNotifyData().getObjManagerData().getAction()
//            );
//            asyncNotifyHelper.getAsyncNotifyMap().put(key,resultList);
//            projectPerimeterAlarmAreaServiceHelper.queryChannel(deviceInfo.getDeviceId(),resultList);
            String respKey = "HUAWEI_SYNC_RESP_" + handleEntity.getOnNotifyData().getMsgId();
            RedisUtil.set(respKey, JSONUtil.toJsonStr(resultList));

        } catch (ClassCastException cce) {
            log.error("[冠林边缘网关] 周界报警通道列表查询结果处理失败，数据结构错误 {}", handleEntity);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            log.info("[冠林边缘网关] 周界报警通道列表查询结果处理结束 {}", handleEntity.getOnNotifyData().getObjManagerData().getObjInfo());
            return done();
        }
    }
}
