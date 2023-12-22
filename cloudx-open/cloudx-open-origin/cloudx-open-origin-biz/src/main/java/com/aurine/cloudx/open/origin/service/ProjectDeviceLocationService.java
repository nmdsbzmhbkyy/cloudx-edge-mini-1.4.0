

package com.aurine.cloudx.open.origin.service;

import com.aurine.cloudx.open.origin.vo.ProjectDeviceLocationVo;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceLocation;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 记录设备平面图位置打点信息
 *
 * @author lingang
 * @date 2020-06-15 16:07:41
 */
public interface ProjectDeviceLocationService extends IService<ProjectDeviceLocation> {
    List<ProjectDeviceLocationVo> getPoints(String picId);
}
