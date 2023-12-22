package com.aurine.cloudx.open.origin.service;


import com.aurine.cloudx.open.origin.vo.ProjectDeviceCheckParamVo;
import com.aurine.cloudx.open.origin.vo.ProjectDeviceModifyLogVo;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceInfo;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceModifyLog;
import com.baomidou.mybatisplus.extension.service.IService;

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
    boolean saveDeviceModifyLog(String deviceId, ProjectDeviceInfo projectDeviceInfo);

    /**
     * 校验设备ip4和设备编号以及mac
     *
     * @param deviceId
     * @return
     */
    ProjectDeviceCheckParamVo checkDeviceIp4AndDeviceCode(String deviceId);
}
