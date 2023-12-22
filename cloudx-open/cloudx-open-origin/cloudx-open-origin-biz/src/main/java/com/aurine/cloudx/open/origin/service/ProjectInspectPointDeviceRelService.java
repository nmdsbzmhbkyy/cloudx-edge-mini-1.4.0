package com.aurine.cloudx.open.origin.service;

import com.aurine.cloudx.open.origin.entity.ProjectInspectPointDeviceRel;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 设备巡检点与设备关联表(ProjectInspectPointDeviceRel)表服务接口
 *
 * @author 王良俊
 * @since 2020-07-23 18:26:38
 */
public interface ProjectInspectPointDeviceRelService extends IService<ProjectInspectPointDeviceRel> {

    /**
     * <p>
     * 保存巡检点和设备的关系
     * </p>
     *
     * @param deviceIdArr 设备id数组
     * @param pointId     巡检点id
     * @return 处理结果
     */
    boolean saveOrUpdatePointDeviceRel(String pointId, String[] deviceIdArr);

}