package com.aurine.cloudx.open.biz.service.impl;

import com.aurine.cloudx.open.biz.service.OpenPhysicalPassPolicyService;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.common.entity.vo.PhysicalPassPolicyVo;
import com.aurine.cloudx.open.origin.entity.ProjectPhysicalPassPolicy;
import com.aurine.cloudx.open.origin.service.ProjectPhysicalPassPolicyService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-物理策略管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

@Service
public class OpenPhysicalPassPolicyServiceImpl implements OpenPhysicalPassPolicyService {

    @Resource
    private ProjectPhysicalPassPolicyService projectPhysicalPassPolicyService;


    @Override
    public R<PhysicalPassPolicyVo> getById(String id) {
        ProjectPhysicalPassPolicy po = projectPhysicalPassPolicyService.getById(id);
        if (po == null) return Result.fail(null, CloudxOpenErrorEnum.EMPTY_RESULT);

        PhysicalPassPolicyVo vo = new PhysicalPassPolicyVo();
        BeanUtils.copyProperties(po, vo);

        return R.ok(vo);
    }

    @Override
    public R<Page<PhysicalPassPolicyVo>> page(Page page, PhysicalPassPolicyVo vo) {
        return R.ok(projectPhysicalPassPolicyService.page(page, vo));
    }

    @Override
    public R<PhysicalPassPolicyVo> save(PhysicalPassPolicyVo vo) {
        return R.ok();
    }

    @Override
    public R<PhysicalPassPolicyVo> update(PhysicalPassPolicyVo vo) {
        return R.ok();
    }

    @Override
    public R<Boolean> delete(String id) {
        return R.ok();
    }

}
