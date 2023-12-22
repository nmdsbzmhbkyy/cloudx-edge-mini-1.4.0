package com.aurine.cloudx.open.biz.service.impl;

import com.aurine.cloudx.open.biz.service.OpenPasswordInfoService;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.common.entity.vo.PasswordInfoVo;
import com.aurine.cloudx.open.origin.entity.ProjectPasswd;
import com.aurine.cloudx.open.origin.service.ProjectPasswdService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-密码信息管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

@Service
public class OpenPasswordInfoServiceImpl implements OpenPasswordInfoService {

    @Resource
    private ProjectPasswdService projectPasswdService;


    @Override
    public R<PasswordInfoVo> getById(String id) {
        ProjectPasswd po = projectPasswdService.getById(id);
        if (po == null) return Result.fail(null, CloudxOpenErrorEnum.EMPTY_RESULT);

        PasswordInfoVo vo = new PasswordInfoVo();
        BeanUtils.copyProperties(po, vo);

        return R.ok(vo);
    }

    @Override
    public R<Page<PasswordInfoVo>> page(Page page, PasswordInfoVo vo) {
        return R.ok(projectPasswdService.page(page, vo));
    }

    @Override
    public R<PasswordInfoVo> save(PasswordInfoVo vo) {
        return R.ok();
    }

    @Override
    public R<PasswordInfoVo> update(PasswordInfoVo vo) {
        return R.ok();
    }

    @Override
    public R<Boolean> delete(String id) {
        return R.ok();
    }

}
