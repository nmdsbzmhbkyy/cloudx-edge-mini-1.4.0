package com.aurine.cloudx.open.biz.service;

import com.aurine.cloudx.open.common.entity.vo.PhysicalPassPolicyVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-物理策略管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface OpenPhysicalPassPolicyService {

    /**
     * 根据ID获取物理策略
     *
     * @param id
     * @return
     */
    R<PhysicalPassPolicyVo> getById(String id);

    /**
     * 根据ID获取物理策略
     *
     * @param page
     * @param vo
     * @return
     */
    R<Page<PhysicalPassPolicyVo>> page(Page page, PhysicalPassPolicyVo vo);

    /**
     * 新增物理策略
     *
     * @param vo
     * @return
     */
    R<PhysicalPassPolicyVo> save(PhysicalPassPolicyVo vo);

    /**
     * 修改物理策略
     *
     * @param vo
     * @return
     */
    R<PhysicalPassPolicyVo> update(PhysicalPassPolicyVo vo);

    /**
     * 删除物理策略
     *
     * @param id
     * @return
     */
    R<Boolean> delete(String id);

}
