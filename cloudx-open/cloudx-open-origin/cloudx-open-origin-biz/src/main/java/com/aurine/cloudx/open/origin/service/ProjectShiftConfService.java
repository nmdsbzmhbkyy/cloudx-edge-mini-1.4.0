package com.aurine.cloudx.open.origin.service;

import com.aurine.cloudx.open.origin.entity.ProjectShiftConf;
import com.aurine.cloudx.open.origin.vo.ProjectShiftConfPageVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.lettuce.core.dynamic.annotation.Param;


/**
 * 班次配置
 *
 * @author guhl@aurine.cn
 * @date 2020-07-23 08:36:54
 */
public interface ProjectShiftConfService extends IService<ProjectShiftConf> {

    /**
     * 班次配置分页查询
     *
     * @param page
     * @param projectShiftConfPageVo
     * @return
     */
    Page<ProjectShiftConfPageVo> pageShiftConf(Page<ProjectShiftConfPageVo> page, @Param("query") ProjectShiftConfPageVo projectShiftConfPageVo);

    /**
     * 根据班次名称获取班次列表
     *
     * @param shiftName
     * @return
     */
    ProjectShiftConf getByShiftName(String shiftName, String shiftId);

    /**
     * 更新班次设置
     *
     * @param projectShiftConf
     * @return
     */
    Boolean updateProjectShiftConf(ProjectShiftConf projectShiftConf);

    /**
     * 删除班次配置
     *
     * @param shiftId
     * @return
     */
    Boolean removeByShiftId(String shiftId);
}
