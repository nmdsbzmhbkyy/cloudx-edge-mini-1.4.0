package com.aurine.cloudx.open.biz.service;

import com.aurine.cloudx.open.common.entity.vo.VisitorHisVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-来访记录管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface OpenVisitorHisService {

    /**
     * 根据ID获取来访记录
     *
     * @param id
     * @return
     */
    R<VisitorHisVo> getById(String id);

    /**
     * 根据ID获取来访记录
     *
     * @param page
     * @param vo
     * @return
     */
    R<Page<VisitorHisVo>> page(Page page, VisitorHisVo vo);

    /**
     * 新增来访记录
     *
     * @param vo
     * @return
     */
    R<VisitorHisVo> save(VisitorHisVo vo);

    /**
     * 修改来访记录
     *
     * @param vo
     * @return
     */
    R<VisitorHisVo> update(VisitorHisVo vo);

    /**
     * 删除来访记录
     *
     * @param id
     * @return
     */
    R<Boolean> delete(String id);

}
