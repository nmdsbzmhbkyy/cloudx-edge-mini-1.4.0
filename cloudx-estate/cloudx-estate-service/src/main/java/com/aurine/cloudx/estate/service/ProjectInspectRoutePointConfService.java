package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectInspectRoutePointConf;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 设备巡检路线与巡更点关系表(ProjectInspectRoutePointConf)表服务接口
 *
 * @author 王良俊
 * @since 2020-07-23 18:32:50
 */
public interface ProjectInspectRoutePointConfService extends IService<ProjectInspectRoutePointConf> {

    /**
     * <p>
     * 更新巡检点和巡检路线的关系
     * </p>
     *
     * @param inspectRouteId 巡检路线id
     * @param pointIdArr     巡检点id数组
     * @return 处理结果
     */
    boolean saveOrUpdateRoutePointRel(String inspectRouteId, String[] pointIdArr, String isSort);

    /**
     * <p>
     * 删除巡检点和巡检路线的关系
     * </p>
     *
     * @param inspectRouteId 巡检路线id
     * @return 处理结果
     */
    boolean removeRoutePointRel(String inspectRouteId);

}