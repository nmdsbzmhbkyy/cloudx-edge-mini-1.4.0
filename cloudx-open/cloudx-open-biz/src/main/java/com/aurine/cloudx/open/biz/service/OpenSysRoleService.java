package com.aurine.cloudx.open.biz.service;

import com.aurine.cloudx.open.common.entity.vo.SysRoleVo;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-系统角色
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface OpenSysRoleService {

    /**
     * 通过projectId（项目id）查询第一个系统部门
     *
     * @param projectId
     * @return
     */
    R<SysRoleVo> getFirstByProjectId(Integer projectId);

}
