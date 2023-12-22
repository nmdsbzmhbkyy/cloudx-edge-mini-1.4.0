package com.aurine.cloudx.open.biz.service;

import com.aurine.cloudx.open.common.entity.vo.RightDeviceRelVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-权限设备关系管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface OpenRightDeviceRelService {

    /**
     * 根据ID获取权限设备关系
     *
     * @param id
     * @return
     */
    R<RightDeviceRelVo> getById(String id);

    /**
     * 根据ID获取权限设备关系
     *
     * @param page
     * @param vo
     * @return
     */
    R<Page<RightDeviceRelVo>> page(Page page, RightDeviceRelVo vo);

    /**
     * 新增权限设备关系
     *
     * @param vo
     * @return
     */
    R<RightDeviceRelVo> save(RightDeviceRelVo vo);

    /**
     * 修改权限设备关系
     *
     * @param vo
     * @return
     */
    R<RightDeviceRelVo> update(RightDeviceRelVo vo);

    /**
     * 删除权限设备关系
     *
     * @param id
     * @return
     */
    R<Boolean> delete(String id);

}
