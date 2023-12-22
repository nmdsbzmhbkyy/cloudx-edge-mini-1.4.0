package com.aurine.cloudx.open.origin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.api.entity.SysRole;

/**
 * 系统角色
 *
 * @author : Qiu
 * @date : 2022 04 18 18:03
 */

public interface SysRoleService extends IService<SysRole> {

    /**
     * 通过projectId（项目id）查询第一个系统角色
     *
     * @param projectId
     * @return
     */
    SysRole getFirstByProjectId(Integer projectId);

}
