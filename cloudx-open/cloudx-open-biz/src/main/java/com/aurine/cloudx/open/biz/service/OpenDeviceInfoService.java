package com.aurine.cloudx.open.biz.service;

import com.aurine.cloudx.open.common.entity.vo.DeviceInfoVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-设备信息管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface OpenDeviceInfoService {

    /**
     * 根据ID获取设备信息
     *
     * @param id
     * @return
     */
    R<DeviceInfoVo> getById(String id);

    /**
     * 根据ID获取设备信息
     *
     * @param page
     * @param vo
     * @return
     */
    R<Page<DeviceInfoVo>> page(Page page, DeviceInfoVo vo);

    /**
     * 新增设备信息
     *
     * @param vo
     * @return
     */
    R<DeviceInfoVo> save(DeviceInfoVo vo);

    /**
     * 修改设备信息
     *
     * @param vo
     * @return
     */
    R<DeviceInfoVo> update(DeviceInfoVo vo);

    /**
     * 删除设备信息
     *
     * @param id
     * @return
     */
    R<Boolean> delete(String id);

}
