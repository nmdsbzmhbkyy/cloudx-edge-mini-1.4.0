package com.aurine.cloudx.estate.service;


import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectDeviceModifyLog;
import com.aurine.cloudx.estate.vo.ProjectDeviceCheckParamVo;
import com.aurine.cloudx.estate.vo.ProjectDeviceModifyLogVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;

import java.util.List;

/**
 * 设备修改记录表
 *
 * @author 邹宇
 * @date 2021-9-26 16:05:25
 */
public interface ProjectDeviceModifyLogService extends IService<ProjectDeviceModifyLog> {

    /**
     * 根据设备ID查询更新记录
     *
     * @param deviceId
     * @return
     */
    List<ProjectDeviceModifyLogVo> getUpdateRecordByDeviceId(String deviceId, Integer count);


    /**
     * 添加设备修改记录
     *
     * @param projectDeviceInfo
     * @return
     */
    boolean saveDeviceModifyLog(String deviceId,ProjectDeviceInfo projectDeviceInfo);

    /**
     * 校验设备ip4和设备编号以及mac
     *
     * @param deviceId
     * @return
     */
    ProjectDeviceCheckParamVo checkDeviceIp4AndDeviceCode(String deviceId);

    /**
     * 添加设备时校验参数
     *
     * @param ipv4
     * @param deviceCode
     * @param mac
     * @param deviceId
     * @return
     */
    R<Boolean> checkDeviceParam(String ipv4, String deviceCode, String mac,String deviceId);
    R<Boolean> checkDeviceParam(String ipv4, String deviceCode, String mac,String deviceId,String sn);
}
