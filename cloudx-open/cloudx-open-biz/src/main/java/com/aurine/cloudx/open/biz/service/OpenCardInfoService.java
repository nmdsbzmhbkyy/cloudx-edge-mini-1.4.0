package com.aurine.cloudx.open.biz.service;

import com.aurine.cloudx.open.common.entity.vo.CardInfoVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-卡信息管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface OpenCardInfoService {

    /**
     * 根据ID获取卡信息
     *
     * @param id
     * @return
     */
    R<CardInfoVo> getById(String id);

    /**
     * 根据ID获取卡信息
     *
     * @param page
     * @param vo
     * @return
     */
    R<Page<CardInfoVo>> page(Page page, CardInfoVo vo);

    /**
     * 新增卡信息
     *
     * @param vo
     * @return
     */
    R<CardInfoVo> save(CardInfoVo vo);

    /**
     * 修改卡信息
     *
     * @param vo
     * @return
     */
    R<CardInfoVo> update(CardInfoVo vo);

    /**
     * 删除卡信息
     *
     * @param id
     * @return
     */
    R<Boolean> delete(String id);

}
