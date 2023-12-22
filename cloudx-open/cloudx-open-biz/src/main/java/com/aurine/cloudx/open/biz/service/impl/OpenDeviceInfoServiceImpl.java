package com.aurine.cloudx.open.biz.service.impl;

import com.aurine.cloudx.open.biz.service.OpenDeviceInfoService;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.common.entity.vo.DeviceInfoVo;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceInfo;
import com.aurine.cloudx.open.origin.service.ProjectDeviceInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-设备信息管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

@Service
public class OpenDeviceInfoServiceImpl implements OpenDeviceInfoService {

    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;


    @Override
    public R<DeviceInfoVo> getById(String id) {
        ProjectDeviceInfo po = projectDeviceInfoService.getById(id);
        if (po == null) return Result.fail(null, CloudxOpenErrorEnum.EMPTY_RESULT);

        DeviceInfoVo vo = new DeviceInfoVo();
        BeanUtils.copyProperties(po, vo);

        return R.ok(vo);
    }

    @Override
    public R<Page<DeviceInfoVo>> page(Page page, DeviceInfoVo vo) {
        return R.ok(projectDeviceInfoService.page(page, vo));
    }

    @Override
    public R<DeviceInfoVo> save(DeviceInfoVo vo) {
        return R.ok();
    }

    @Override
    public R<DeviceInfoVo> update(DeviceInfoVo vo) {
        return R.ok();
    }

    @Override
    public R<Boolean> delete(String id) {
        return R.ok();
    }

}
