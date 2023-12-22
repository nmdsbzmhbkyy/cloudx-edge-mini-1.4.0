package com.aurine.cloudx.open.biz.service;

import com.aurine.cloudx.open.common.entity.vo.PersonEntranceVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-人行事件管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface OpenPersonEntranceService {

    /**
     * 根据ID获取人行事件
     *
     * @param id
     * @return
     */
    R<PersonEntranceVo> getById(String id);

    /**
     * 根据ID获取人行事件
     *
     * @param page
     * @param vo
     * @return
     */
    R<Page<PersonEntranceVo>> page(Page page, PersonEntranceVo vo);

    /**
     * 新增人行事件
     *
     * @param vo
     * @return
     */
    R<PersonEntranceVo> save(PersonEntranceVo vo);

    /**
     * 修改人行事件
     *
     * @param vo
     * @return
     */
    R<PersonEntranceVo> update(PersonEntranceVo vo);

    /**
     * 删除人行事件
     *
     * @param id
     * @return
     */
    R<Boolean> delete(String id);

}
