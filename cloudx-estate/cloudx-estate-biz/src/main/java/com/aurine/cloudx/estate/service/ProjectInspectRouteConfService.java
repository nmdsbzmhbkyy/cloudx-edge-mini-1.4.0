package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectInspectRouteConf;
import com.aurine.cloudx.estate.vo.ProjectInspectRouteConfSearchConditionVo;
import com.aurine.cloudx.estate.vo.ProjectInspectRouteConfVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 设备巡检路线设置(ProjectInspectRouteConf)表服务接口
 *
 * @author 王良俊
 * @since 2020-07-23 18:33:19
 */
public interface ProjectInspectRouteConfService extends IService<ProjectInspectRouteConf> {

    /**
     * <p>
     * 查询巡检路线分页数据
     * </p>
     *
     * @param page  分页对象
     * @param query 查询条件
     * @return 分页数据
     */
    Page<ProjectInspectRouteConfVo> fetchList(Page<ProjectInspectRouteConfVo> page, ProjectInspectRouteConfSearchConditionVo query);

    /**
     * <p>
     * 保存巡检路线
     * </p>
     *
     * @param vo 巡检路线vo对象
     * @return 处理结果
     */
    boolean saveInspectRoute(ProjectInspectRouteConfVo vo);

    /**
     * <p>
     * 更新巡检路线
     * </p>
     *
     * @param vo 巡检路线vo对象
     * @return 处理结果
     */
    boolean updateInspectRoute(ProjectInspectRouteConfVo vo);

    /**
     * <p>
     * 删除巡检路线
     * </p>
     *
     * @param inspectRouteId 巡检路线ID
     * @return 处理结果
     */
    boolean removeInspectRoute(String inspectRouteId);

}