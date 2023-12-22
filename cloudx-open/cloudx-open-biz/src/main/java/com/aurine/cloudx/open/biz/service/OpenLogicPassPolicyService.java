package com.aurine.cloudx.open.biz.service;

import com.aurine.cloudx.open.common.entity.vo.LogicPassPolicyVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-逻辑策略管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface OpenLogicPassPolicyService {

    /**
     * 根据ID获取逻辑策略
     *
     * @param id
     * @return
     */
    R<LogicPassPolicyVo> getById(String id);

    /**
     * 根据ID获取逻辑策略
     *
     * @param page
     * @param vo
     * @return
     */
    R<Page<LogicPassPolicyVo>> page(Page page, LogicPassPolicyVo vo);

    /**
     * 新增逻辑策略
     *
     * @param vo
     * @return
     */
    R<LogicPassPolicyVo> save(LogicPassPolicyVo vo);

    /**
     * 修改逻辑策略
     *
     * @param vo
     * @return
     */
    R<LogicPassPolicyVo> update(LogicPassPolicyVo vo);

    /**
     * 删除逻辑策略
     *
     * @param id
     * @return
     */
    R<Boolean> delete(String id);

}
