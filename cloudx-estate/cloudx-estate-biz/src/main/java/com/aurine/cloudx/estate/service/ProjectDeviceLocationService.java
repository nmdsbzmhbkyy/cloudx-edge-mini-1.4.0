

package com.aurine.cloudx.estate.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.aurine.cloudx.estate.entity.ProjectDeviceLocation;
import com.aurine.cloudx.estate.vo.ProjectDeviceLocationVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 记录设备平面图位置打点信息
 *
 * @author lingang
 * @date 2020-06-15 16:07:41
 */
public interface ProjectDeviceLocationService extends IService<ProjectDeviceLocation> {
    List<ProjectDeviceLocationVo> getPoints(String picId);
}
