package com.aurine.cloudx.open.biz.service;

import com.aurine.cloudx.open.common.entity.vo.ProjectRegionVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-项目区域管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface OpenProjectRegionService {

    /**
     * 通过id查询项目区域
     *
     * @param id
     * @return
     */
    R<ProjectRegionVo> getById(String id);

    /**
     * 分页查询项目区域
     *
     * @param page
     * @param vo
     * @return
     */
    R<Page<ProjectRegionVo>> page(Page page, ProjectRegionVo vo);

    /**
     * 新增
     *
     * @author:
     * @param vo
     * @return
     */
    R<ProjectRegionVo> save(ProjectRegionVo vo);

    /**
     * 修改
     *
     * @author:
     * @param vo
     * @return
     */
    R<ProjectRegionVo> update(ProjectRegionVo vo);

    /**
     * 删除
     *
     * @author:
     * @param id
     * @return
     */
    R<Boolean> delete(String id);

}
