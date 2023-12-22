package com.aurine.cloudx.open.biz.service.impl;

import com.aurine.cloudx.open.biz.service.OpenSysRoleService;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.common.entity.vo.SysRoleVo;
import com.aurine.cloudx.open.origin.service.SysRoleService;
import com.pig4cloud.pigx.admin.api.entity.SysRole;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-系统角色实现
 *
 * @author : Qiu
 * @date : 2022 04 18 18:00
 */

@Service
public class OpenSysRoleServiceImpl implements OpenSysRoleService {

    @Resource
    private SysRoleService sysRoleService;


    @Override
    public R<SysRoleVo> getFirstByProjectId(Integer projectId) {
        if (projectId == null || projectId < 0) return Result.fail(null, CloudxOpenErrorEnum.MISSING_REQUIRED_PARAMETERS.getCode(), "项目ID（projectId）为空");

        SysRole po = sysRoleService.getFirstByProjectId(projectId);
        if (po == null) return Result.fail(null, CloudxOpenErrorEnum.EMPTY_RESULT);

        SysRoleVo vo = new SysRoleVo();
        BeanUtils.copyProperties(po, vo);

        return R.ok(vo);
    }
}
