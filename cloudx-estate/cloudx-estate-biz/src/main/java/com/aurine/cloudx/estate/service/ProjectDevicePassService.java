

package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.dto.ProjectDevicePassQRDTO;
import com.aurine.cloudx.estate.entity.ProjectAlarmHandle;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectDeviceParamInfo;
import com.aurine.cloudx.estate.vo.ProjectEntranceAlarmEventVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 通行设备服务
 *
 * @ClassName: ProjectDevicePassService
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-04-29 9:35
 * @Copyright:
 */
public interface ProjectDevicePassService extends IService<ProjectDeviceInfo> {

    /**
     * 获取 QR码
     * @param qrDto
     * @return
     */
    String getQRCode(ProjectDevicePassQRDTO qrDto);


}
