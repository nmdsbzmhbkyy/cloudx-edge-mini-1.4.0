
package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectDeviceSubsystem;
import com.aurine.cloudx.estate.vo.ProjectDeviceSubsystemTreeVo;
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
    boolean initDeviceSubSysTem(Integer projectId,Integer tenantId);
}
