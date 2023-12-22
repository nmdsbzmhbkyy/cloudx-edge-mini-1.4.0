package com.aurine.cloudx.open.biz.service;

import com.aurine.cloudx.open.common.entity.vo.PassPlanVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-通行方案管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface OpenPassPlanService {

    /**
     * 根据ID获取通行方案
     *
     * @param id
     * @return
     */
    R<PassPlanVo> getById(String id);

    /**
     * 根据ID获取通行方案
     *
     * @param page
     * @param vo
     * @return
     */
    R<Page<PassPlanVo>> page(Page page, PassPlanVo vo);

    /**
     * 新增通行方案
     *
     * @param vo
     * @return
     */
    R<PassPlanVo> save(PassPlanVo vo);

    /**
     * 修改通行方案
     *
     * @param vo
     * @return
     */
    R<PassPlanVo> update(PassPlanVo vo);

    /**
     * 删除通行方案
     *
     * @param id
     * @return
     */
    R<Boolean> delete(String id);

}
