package com.aurine.cloudx.estate.thirdparty.main.service;


import com.aurine.cloudx.estate.thirdparty.main.entity.PersonInfo;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.EventDeviceGatePassDTO;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.EventWarningErrorDTO;

/**
 * <p>统一事件回调业务接口</p>
 *
 * @ClassName: EventService
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-10 15:10
 * @Copyright:
 */
public interface EventService {

    /**
     *  门禁开门事件
     *
     * @param eventDeviceGatePassDTO
     */
    void passGate(EventDeviceGatePassDTO eventDeviceGatePassDTO);

    /**
     *  异常通行事件
     *
     * @param eventDeviceGatePassDTO
     */
    void passGateError(EventDeviceGatePassDTO eventDeviceGatePassDTO);

    /**
     *  门禁开门事件(不含用户信息，如系统开门，远程开门等)
     *
     * @param eventDeviceGatePassDTO
     */
    void passGateWithOutPerson(EventDeviceGatePassDTO eventDeviceGatePassDTO);

    /**
     * 异常警告事件
     * @param eventWarningErrorDTO
     */
    void warning(EventWarningErrorDTO eventWarningErrorDTO);

    /**
     * 周界告警事件
     * @param eventWarningErrorDTO
     */
    void channelAlarm(EventWarningErrorDTO eventWarningErrorDTO);

     PersonInfo getPersonTypeAndPersonId(EventDeviceGatePassDTO eventDeviceGatePassDTO, Integer projectId);
}
