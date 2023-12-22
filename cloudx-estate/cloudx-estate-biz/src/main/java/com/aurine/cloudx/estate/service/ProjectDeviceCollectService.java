

package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectDeviceCollect;
import com.aurine.cloudx.estate.vo.ProjectDeviceCollectFormVo;
import com.aurine.cloudx.estate.vo.ProjectDeviceCollectListVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 项目设备采集参数
 *
 * @author xull@aurine.cn
 * @date 2020-06-12 11:43:43
 */
public interface ProjectDeviceCollectService extends IService<ProjectDeviceCollect> {

    /**
     * 编辑项目设备采集参数列表
     *
     * @param projectDeviceCollectFormVo
     *
     * @return
     */
    boolean updateDeviceCollectList(ProjectDeviceCollectFormVo projectDeviceCollectFormVo);

    /**
     * 获取项目设备采集参数列表
     *
     * @param type
     * @param projectId
     *
     * @return
     */
    List<ProjectDeviceCollectListVo> getDeviceCollectListVo(Integer projectId, String type);
    /**
     * 获取项目设备采集参数列表
     *
     * @param type
     * @param projectId
     * @param param
     *
     * @return
     */
    List<ProjectDeviceCollectListVo> getDeviceCollectListVo(Integer projectId, String type,String param);

    String getPoliceEnable(Integer projectId);
}
