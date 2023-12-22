package com.aurine.cloudx.open.biz.service;

import com.aurine.cloudx.open.common.entity.vo.FrameInfoVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-框架信息管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface OpenFrameInfoService {

    /**
     * 根据ID获取框架信息
     *
     * @param id
     * @return
     */
    R<FrameInfoVo> getById(String id);

    /**
     * 根据ID获取框架信息
     *
     * @param page
     * @param vo
     * @return
     */
    R<Page<FrameInfoVo>> page(Page page, FrameInfoVo vo);

    /**
     * 新增框架信息
     *
     * @param vo
     * @return
     */
    R<FrameInfoVo> save(FrameInfoVo vo);

    /**
     * 修改框架信息
     *
     * @param vo
     * @return
     */
    R<FrameInfoVo> update(FrameInfoVo vo);

    /**
     * 删除框架信息
     *
     * @param id
     * @return
     */
    R<Boolean> delete(String id);

}
