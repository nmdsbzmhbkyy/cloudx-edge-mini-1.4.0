package com.aurine.cloudx.open.biz.service;

import com.aurine.cloudx.open.common.entity.vo.UnitInfoVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-单元信息管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface OpenUnitInfoService {

    /**
     * 根据ID获取单元信息
     *
     * @param id
     * @return
     */
    R<UnitInfoVo> getById(String id);

    /**
     * 根据ID获取单元信息
     *
     * @param page
     * @param vo
     * @return
     */
    R<Page<UnitInfoVo>> page(Page page, UnitInfoVo vo);

    /**
     * 新增单元信息
     *
     * @param vo
     * @return
     */
    R<UnitInfoVo> save(UnitInfoVo vo);

    /**
     * 修改单元信息
     *
     * @param vo
     * @return
     */
    R<UnitInfoVo> update(UnitInfoVo vo);

    /**
     * 删除单元信息
     *
     * @param id
     * @return
     */
    R<Boolean> delete(String id);

}
