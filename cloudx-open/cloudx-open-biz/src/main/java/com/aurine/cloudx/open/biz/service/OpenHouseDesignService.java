package com.aurine.cloudx.open.biz.service;

import com.aurine.cloudx.open.common.entity.vo.HouseDesignVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-户型管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface OpenHouseDesignService {

    /**
     * 根据ID获取户型
     *
     * @param id
     * @return
     */
    R<HouseDesignVo> getById(String id);

    /**
     * 根据ID获取户型
     *
     * @param page
     * @param vo
     * @return
     */
    R<Page<HouseDesignVo>> page(Page page, HouseDesignVo vo);

    /**
     * 新增户型
     *
     * @param vo
     * @return
     */
    R<HouseDesignVo> save(HouseDesignVo vo);

    /**
     * 修改户型
     *
     * @param vo
     * @return
     */
    R<HouseDesignVo> update(HouseDesignVo vo);

    /**
     * 删除户型
     *
     * @param id
     * @return
     */
    R<Boolean> delete(String id);

}
