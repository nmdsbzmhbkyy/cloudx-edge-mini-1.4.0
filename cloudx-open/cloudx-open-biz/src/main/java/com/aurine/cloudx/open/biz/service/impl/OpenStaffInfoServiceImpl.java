package com.aurine.cloudx.open.biz.service.impl;

import com.aurine.cloudx.open.biz.service.OpenStaffInfoService;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.common.entity.vo.StaffInfoVo;
import com.aurine.cloudx.open.origin.entity.ProjectStaff;
import com.aurine.cloudx.open.origin.service.ProjectStaffService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-员工信息管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

@Service
public class OpenStaffInfoServiceImpl implements OpenStaffInfoService {

    @Resource
    private ProjectStaffService projectStaffService;


    @Override
    public R<StaffInfoVo> getById(String id) {
        ProjectStaff po = projectStaffService.getById(id);
        if (po == null) return Result.fail(null, CloudxOpenErrorEnum.EMPTY_RESULT);

        StaffInfoVo vo = new StaffInfoVo();
        BeanUtils.copyProperties(po, vo);

        return R.ok(vo);
    }

    @Override
    public R<Page<StaffInfoVo>> page(Page page, StaffInfoVo vo) {
        return R.ok(projectStaffService.page(page, vo));
    }

    @Override
    public R<StaffInfoVo> save(StaffInfoVo vo) {
        return R.ok();
    }

    @Override
    public R<StaffInfoVo> update(StaffInfoVo vo) {
        return R.ok();
    }

    @Override
    public R<Boolean> delete(String id) {
        return R.ok();
    }

}
