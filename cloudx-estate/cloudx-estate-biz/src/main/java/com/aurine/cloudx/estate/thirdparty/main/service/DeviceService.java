package com.aurine.cloudx.estate.thirdparty.main.service;


import com.aurine.cloudx.estate.thirdparty.main.entity.dto.EventDeviceNoticeDTO;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.ResponseOperateDTO;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.ResponseSearchRecordDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * <p>统一设备回调业务接口</p>
 *
 * @ClassName: DeviceService
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-07 11:30
 * @Copyright:
 */
public interface DeviceService {

    /**
     * 设备状态变更
     *
     * @param eventDeviceNoticeDTO
     */
    void deviceStatusUpdate(EventDeviceNoticeDTO eventDeviceNoticeDTO);

    /**
     * 同步设备
     *
     * @param responseSearchRecordDTO
     */
    void syncDevice(ResponseSearchRecordDTO responseSearchRecordDTO);

    /**
     * 更新设备参数
     * @param responseOperateDTO
     */
    void updateDeviceParam(ResponseOperateDTO responseOperateDTO);


    /**
     * 更新设备额外参数
     * @param responseOperateDTO
     * @author: 王良俊
     */
    void updateDeviceOtherParam(ResponseOperateDTO responseOperateDTO) throws JsonProcessingException;

    /**
     * 更新物联网设备的参数（这里需要整合成一个json对象）
     * @param responseOperateDTO
     * @author: 王良俊
     */
    void updateIotDeviceParam(ResponseOperateDTO responseOperateDTO) throws JsonProcessingException;

    /**
     * 更新设备框架
     * @param responseOperateDTO
     */
    void updateDeviceFrame(ResponseOperateDTO responseOperateDTO);

    /**
     * 更新设备上的通行凭证状态
     * @param responseOperateDTO
     */
    void updateCert(ResponseOperateDTO responseOperateDTO);

    /**
     * 更新设备上的通行凭证状态
     * @param responseOperateDTO
     */
    void updateCerts(ResponseOperateDTO responseOperateDTO);


    /**
     * <p>
     * 保存通话记录
     * </p>
     *
     * @author: 王良俊
    */
    void saveCallEvent(EventDeviceNoticeDTO noticeDTO);

    /**
    * <p>
    * 这里更新的状态不是设备在线离线状态而是设备门状态、云电话注册状态
    * </p>
    *
    * @author: 王良俊
    */
    void updateDeviceStatus(ResponseOperateDTO operateDTO);

    /**
     * <p>
     * 报警事件状态修改（一般是由未处理改为已处理）
     * </p>
     *
     * @author: 邱家标
     */
    void updateAlarmStatus(ResponseOperateDTO operateDTO);
}
