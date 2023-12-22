package com.aurine.cloudx.open.origin.service;

import com.aurine.cloudx.open.origin.entity.ProjectDeviceRel;
import com.aurine.cloudx.open.origin.vo.ProjectDeviceRelVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 设备关系性表
 *
 * @author 黄健杰
 * @date 2022-02-07
 */
public interface ProjectDeviceRelService extends IService<ProjectDeviceRel> {

    void removeByParDeviceId(String id);

    void removeByDeviceId(String id);

    /**
     * <p>根据设备ID获取设备关系列表 带设备类型</p>
     *
     * @param deviceId 设备ID
     * @return 设备关系vo对象列表
     * @author 王良俊
     */
    List<ProjectDeviceRelVo> ListByDeviceId(String deviceId);

    List<ProjectDeviceRelVo> ListByDeviceIdAndDeviceType(String deviceId, String deviceType);
}
