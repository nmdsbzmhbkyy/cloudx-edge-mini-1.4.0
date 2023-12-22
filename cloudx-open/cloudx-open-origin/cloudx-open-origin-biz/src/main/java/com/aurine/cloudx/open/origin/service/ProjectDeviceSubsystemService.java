
package com.aurine.cloudx.open.origin.service;

import com.aurine.cloudx.open.origin.vo.ProjectDeviceSubsystemTreeVo;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceSubsystem;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 子系统
 *
 * @author xull@aurine.cn
 * @date 2020-05-20 10:39:47
 */
public interface ProjectDeviceSubsystemService extends IService<ProjectDeviceSubsystem> {

    List<ProjectDeviceSubsystemTreeVo> findTree();

    /**
     * 预设子系统
     * @return
     */
    boolean initDeviceSubSysTem(Integer projectId, Integer tenantId);
}
