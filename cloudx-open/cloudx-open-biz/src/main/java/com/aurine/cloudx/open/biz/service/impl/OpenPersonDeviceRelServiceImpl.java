package com.aurine.cloudx.open.biz.service.impl;

import com.aurine.cloudx.open.biz.service.OpenPersonDeviceRelService;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.common.entity.vo.PersonDeviceRelVo;
import com.aurine.cloudx.open.origin.entity.ProjectPersonDevice;
import com.aurine.cloudx.open.origin.service.ProjectPersonDeviceService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-人员设备权限关系管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

@Service
public class OpenPersonDeviceRelServiceImpl implements OpenPersonDeviceRelService {

    @Resource
    private ProjectPersonDeviceService projectPersonDeviceService;


    @Override
    public R<PersonDeviceRelVo> getById(String id) {
        ProjectPersonDevice po = projectPersonDeviceService.getById(id);
        if (po == null) return Result.fail(null, CloudxOpenErrorEnum.EMPTY_RESULT);

        PersonDeviceRelVo vo = new PersonDeviceRelVo();
        BeanUtils.copyProperties(po, vo);

        return R.ok(vo);
    }

    @Override
    public R<Page<PersonDeviceRelVo>> page(Page page, PersonDeviceRelVo vo) {
        return R.ok(projectPersonDeviceService.page(page, vo));
    }

    @Override
    public R<PersonDeviceRelVo> save(PersonDeviceRelVo vo) {
        return R.ok();
    }

    @Override
    public R<PersonDeviceRelVo> update(PersonDeviceRelVo vo) {
        return R.ok();
    }

    @Override
    public R<Boolean> delete(String id) {
        return R.ok();
    }

}
