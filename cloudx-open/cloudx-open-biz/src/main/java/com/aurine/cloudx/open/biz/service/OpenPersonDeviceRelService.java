package com.aurine.cloudx.open.biz.service;

import com.aurine.cloudx.open.common.entity.vo.PersonDeviceRelVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-人员设备权限关系管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface OpenPersonDeviceRelService {

    /**
     * 根据ID获取人员设备权限关系
     *
     * @param id
     * @return
     */
    R<PersonDeviceRelVo> getById(String id);

    /**
     * 根据ID获取人员设备权限关系
     *
     * @param page
     * @param vo
     * @return
     */
    R<Page<PersonDeviceRelVo>> page(Page page, PersonDeviceRelVo vo);

    /**
     * 新增人员设备权限关系
     *
     * @param vo
     * @return
     */
    R<PersonDeviceRelVo> save(PersonDeviceRelVo vo);

    /**
     * 修改人员设备权限关系
     *
     * @param vo
     * @return
     */
    R<PersonDeviceRelVo> update(PersonDeviceRelVo vo);

    /**
     * 删除人员设备权限关系
     *
     * @param id
     * @return
     */
    R<Boolean> delete(String id);

}
