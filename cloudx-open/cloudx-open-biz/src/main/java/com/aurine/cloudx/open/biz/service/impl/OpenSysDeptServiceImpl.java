package com.aurine.cloudx.open.biz.service.impl;

import com.aurine.cloudx.open.biz.service.OpenSysDeptService;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.common.entity.vo.SysDeptVo;
import com.aurine.cloudx.open.origin.service.SysDeptService;
import com.pig4cloud.pigx.admin.api.entity.SysDept;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-系统部门实现
 *
 * @author : Qiu
 * @date : 2022 04 18 18:00
 */

@Service
public class OpenSysDeptServiceImpl implements OpenSysDeptService {

    @Resource
    private SysDeptService sysDeptService;


    @Override
    public R<SysDeptVo> getFirstByProjectId(Integer projectId) {
        if (projectId == null || projectId < 0) return Result.fail(null, CloudxOpenErrorEnum.MISSING_REQUIRED_PARAMETERS.getCode(), "项目ID（projectId）为空");

        SysDept po = sysDeptService.getFirstByProjectId(projectId);
        if (po == null) return Result.fail(null, CloudxOpenErrorEnum.EMPTY_RESULT);

        SysDeptVo vo = new SysDeptVo();
        BeanUtils.copyProperties(po, vo);

        return R.ok(vo);
    }
}
