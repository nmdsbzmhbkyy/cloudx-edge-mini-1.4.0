package com.aurine.cloudx.open.biz.service;

import com.aurine.cloudx.open.common.entity.vo.PassPlanPolicyRelVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-通行方案策略关系管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface OpenPassPlanPolicyRelService {

    /**
     * 根据ID获取通行方案策略关系
     *
     * @param id
     * @return
     */
    R<PassPlanPolicyRelVo> getById(String id);

    /**
     * 根据ID获取通行方案策略关系
     *
     * @param page
     * @param vo
     * @return
     */
    R<Page<PassPlanPolicyRelVo>> page(Page page, PassPlanPolicyRelVo vo);

    /**
     * 新增通行方案策略关系
     *
     * @param vo
     * @return
     */
    R<PassPlanPolicyRelVo> save(PassPlanPolicyRelVo vo);

    /**
     * 修改通行方案策略关系
     *
     * @param vo
     * @return
     */
    R<PassPlanPolicyRelVo> update(PassPlanPolicyRelVo vo);

    /**
     * 删除通行方案策略关系
     *
     * @param id
     * @return
     */
    R<Boolean> delete(String id);

}
