
package com.aurine.cloudx.open.origin.service.impl;

import com.aurine.cloudx.open.origin.mapper.SysRoleMapper;
import com.aurine.cloudx.open.origin.service.SysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.api.entity.SysRole;
import org.springframework.stereotype.Service;


/**
 * 系统角色实现
 *
 * @author : Qiu
 * @date : 2022 04 18 18:03
 */

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Override
    public SysRole getFirstByProjectId(Integer projectId) {
        return baseMapper.getFirstByProjectId(projectId);
    }
}
