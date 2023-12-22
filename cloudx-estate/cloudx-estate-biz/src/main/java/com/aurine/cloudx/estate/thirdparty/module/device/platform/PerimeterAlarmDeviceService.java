

package com.aurine.cloudx.estate.thirdparty.module.device.platform;


import com.aurine.cloudx.estate.constant.enums.CertmediaTypeEnum;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgePerimeterDeviceParamsDTO;

import java.util.List;

/**
 * 统一 周界报警设备 接口
 *
 * @ClassName: PerimeterAlarmDeviceService
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-06-09 14:48
 * @Copyright:
 */
public interface PerimeterAlarmDeviceService extends BaseService {

    /**
     * 查询通道
     *
     * @param deviceInfo 主机设备
     * @return
     */
    List<ProjectPerimeterAlarmArea> queryChannel(ProjectDeviceInfo deviceInfo);


    /**
     * 防区布防
     *
     * @param perimeterAlarmArea 防区（通道）
     * @return
     */
    boolean channelProtection(ProjectPerimeterAlarmArea perimeterAlarmArea);

    /**
     * 防区撤防
     *
     * @param perimeterAlarmArea 防区（通道）
     * @return
     */
    boolean channelRemoval(ProjectPerimeterAlarmArea perimeterAlarmArea);


    /**
     * 消除防区告警
     *
     * @param perimeterAlarmArea 防区（通道）
     */
    boolean clearAlarm(ProjectPerimeterAlarmArea perimeterAlarmArea);

    /**
     * 消除防区告警-指定类型
     *
     * @param projectPerimeterAlarmEvent 周界报警事件
     */
    boolean clearAlarm(ProjectPerimeterAlarmEvent projectPerimeterAlarmEvent);
//    /**
//     * 查询通道状态
//     *
//     * @param deviceInfo 主机设备
//     * @return
//     */
//    List<ProjectPerimeterAlarmArea> queryChannelStatus(ProjectDeviceInfo deviceInfo);

//    boolean queryDevParams(String devId,AurineEdgePerimeterDeviceParamsDTO aurineEdgePerimeterDeviceParamsDTO);
//
//    boolean setDevParams(ProjectDeviceInfo deviceInfo);

    /**
     * 响应中台查询通道事件
     * @param devId
     * @param aurineEdgePerimeterDeviceParamsDTO
     * @return
     */
    boolean queryDevParams(String devId,AurineEdgePerimeterDeviceParamsDTO aurineEdgePerimeterDeviceParamsDTO);

    boolean setDevParams(ProjectDeviceInfo deviceInfo,AurineEdgePerimeterDeviceParamsDTO aurineEdgePerimeterDeviceParamsDTO);


}
