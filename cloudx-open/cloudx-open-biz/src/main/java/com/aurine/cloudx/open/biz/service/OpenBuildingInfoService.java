package com.aurine.cloudx.open.biz.service;

import com.aurine.cloudx.open.common.entity.vo.BuildingInfoVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-楼栋信息管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface OpenBuildingInfoService {

    /**
     * 根据ID获取楼栋信息
     *
     * @param id
     * @return
     */
    R<BuildingInfoVo> getById(String id);

    /**
     * 根据ID获取楼栋信息
     *
     * @param page
     * @param vo
     * @return
     */
    R<Page<BuildingInfoVo>> page(Page page, BuildingInfoVo vo);

    /**
     * 新增楼栋信息
     *
     * @param vo
     * @return
     */
    R<BuildingInfoVo> save(BuildingInfoVo vo);

    /**
     * 修改楼栋信息
     *
     * @param vo
     * @return
     */
    R<BuildingInfoVo> update(BuildingInfoVo vo);

    /**
     * 删除楼栋信息
     *
     * @param id
     * @return
     */
    R<Boolean> delete(String id);

}
