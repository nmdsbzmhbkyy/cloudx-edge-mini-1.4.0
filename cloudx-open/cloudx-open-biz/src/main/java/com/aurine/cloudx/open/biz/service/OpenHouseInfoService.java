package com.aurine.cloudx.open.biz.service;

import com.aurine.cloudx.open.common.entity.vo.HouseInfoVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-房屋信息管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface OpenHouseInfoService {

    /**
     * 根据ID获取房屋信息
     *
     * @param id
     * @return
     */
    R<HouseInfoVo> getById(String id);

    /**
     * 根据ID获取房屋信息
     *
     * @param page
     * @param vo
     * @return
     */
    R<Page<HouseInfoVo>> page(Page page, HouseInfoVo vo);

    /**
     * 新增房屋信息
     *
     * @param vo
     * @return
     */
    R<HouseInfoVo> save(HouseInfoVo vo);

    /**
     * 修改房屋信息
     *
     * @param vo
     * @return
     */
    R<HouseInfoVo> update(HouseInfoVo vo);

    /**
     * 删除房屋信息
     *
     * @param id
     * @return
     */
    R<Boolean> delete(String id);

}
