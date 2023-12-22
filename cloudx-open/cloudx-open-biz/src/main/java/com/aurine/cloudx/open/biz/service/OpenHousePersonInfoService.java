package com.aurine.cloudx.open.biz.service;

import com.aurine.cloudx.open.common.entity.vo.HousePersonInfoVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-住户信息管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface OpenHousePersonInfoService {

    /**
     * 根据ID获取住户信息
     *
     * @param id
     * @return
     */
    R<HousePersonInfoVo> getById(String id);

    /**
     * 根据ID获取住户信息
     *
     * @param page
     * @param vo
     * @return
     */
    R<Page<HousePersonInfoVo>> page(Page page, HousePersonInfoVo vo);

    /**
     * 新增住户信息
     *
     * @param vo
     * @return
     */
    R<HousePersonInfoVo> save(HousePersonInfoVo vo);

    /**
     * 修改住户信息
     *
     * @param vo
     * @return
     */
    R<HousePersonInfoVo> update(HousePersonInfoVo vo);

    /**
     * 删除住户信息
     *
     * @param id
     * @return
     */
    R<Boolean> delete(String id);

}
