package com.aurine.cloudx.open.biz.service;

import com.aurine.cloudx.open.common.entity.vo.StaffInfoVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-员工信息管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface OpenStaffInfoService {

    /**
     * 根据ID获取员工信息
     *
     * @param id
     * @return
     */
    R<StaffInfoVo> getById(String id);

    /**
     * 根据ID获取员工信息
     *
     * @param page
     * @param vo
     * @return
     */
    R<Page<StaffInfoVo>> page(Page page, StaffInfoVo vo);

    /**
     * 新增员工信息
     *
     * @param vo
     * @return
     */
    R<StaffInfoVo> save(StaffInfoVo vo);

    /**
     * 修改员工信息
     *
     * @param vo
     * @return
     */
    R<StaffInfoVo> update(StaffInfoVo vo);

    /**
     * 删除员工信息
     *
     * @param id
     * @return
     */
    R<Boolean> delete(String id);

}
