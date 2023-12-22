

package com.aurine.cloudx.open.origin.service;


import com.aurine.cloudx.open.origin.entity.ProjectParkingInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 停车场
 *
 * @author 王伟
 * @date 2020-05-07 09:13:25
 */
public interface ProjectParkingInfoService extends IService<ProjectParkingInfo> {

    /**
     * <p>
     * 初始化项目车场
     * </p>
     *
     * @param projectId 项目ID
     * @author: 王良俊
     */
    void init(String projectName, Integer projectId, Integer tenantId);

}
