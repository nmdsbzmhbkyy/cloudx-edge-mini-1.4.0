package com.aurine.cloudx.open.biz.service;

import com.aurine.cloudx.open.common.entity.vo.PersonPlanRelVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-人员通行方案关系管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface OpenPersonPlanRelService {

    /**
     * 根据ID获取人员通行方案关系
     *
     * @param id
     * @return
     */
    R<PersonPlanRelVo> getById(String id);

    /**
     * 根据ID获取人员通行方案关系
     *
     * @param page
     * @param vo
     * @return
     */
    R<Page<PersonPlanRelVo>> page(Page page, PersonPlanRelVo vo);

    /**
     * 新增人员通行方案关系
     *
     * @param vo
     * @return
     */
    R<PersonPlanRelVo> save(PersonPlanRelVo vo);

    /**
     * 修改人员通行方案关系
     *
     * @param vo
     * @return
     */
    R<PersonPlanRelVo> update(PersonPlanRelVo vo);

    /**
     * 删除人员通行方案关系
     *
     * @param id
     * @return
     */
    R<Boolean> delete(String id);

}
