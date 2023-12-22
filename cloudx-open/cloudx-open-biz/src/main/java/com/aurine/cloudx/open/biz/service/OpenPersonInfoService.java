package com.aurine.cloudx.open.biz.service;

import com.aurine.cloudx.open.common.entity.vo.PersonInfoVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-人员信息管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface OpenPersonInfoService {

    /**
     * 根据ID获取人员信息
     *
     * @param id
     * @return
     */
    R<PersonInfoVo> getById(String id);

    /**
     * 根据ID获取人员信息
     *
     * @param page
     * @param vo
     * @return
     */
    R<Page<PersonInfoVo>> page(Page page, PersonInfoVo vo);

    /**
     * 新增人员信息
     *
     * @param vo
     * @return
     */
    R<PersonInfoVo> save(PersonInfoVo vo);

    /**
     * 修改人员信息
     *
     * @param vo
     * @return
     */
    R<PersonInfoVo> update(PersonInfoVo vo);

    /**
     * 删除人员信息
     *
     * @param id
     * @return
     */
    R<Boolean> delete(String id);

}
