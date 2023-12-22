package com.aurine.cloudx.open.biz.service;

import com.aurine.cloudx.open.common.entity.vo.VisitorInfoVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-访客信息管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface OpenVisitorInfoService {

    /**
     * 根据ID获取访客信息
     *
     * @param id
     * @return
     */
    R<VisitorInfoVo> getById(String id);

    /**
     * 根据ID获取访客信息
     *
     * @param page
     * @param vo
     * @return
     */
    R<Page<VisitorInfoVo>> page(Page page, VisitorInfoVo vo);

    /**
     * 新增访客信息
     *
     * @param vo
     * @return
     */
    R<VisitorInfoVo> save(VisitorInfoVo vo);

    /**
     * 修改访客信息
     *
     * @param vo
     * @return
     */
    R<VisitorInfoVo> update(VisitorInfoVo vo);

    /**
     * 删除访客信息
     *
     * @param id
     * @return
     */
    R<Boolean> delete(String id);

}
