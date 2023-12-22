package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.estate.entity.SysRoleMenuMobile;
import com.aurine.cloudx.estate.mapper.SysRoleMenuMobileMapper;
import com.aurine.cloudx.estate.service.SysRoleMenuMobileService;
import com.aurine.cloudx.estate.vo.SysMenuMobileTreeVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.api.vo.TreeUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色菜单表(SysRoleMenuMobile)表服务实现类
 *
 * @author makejava
 * @since 2021-02-07 10:25:50
 */
@Service
public class SysRoleMenuMobileServiceImpl extends ServiceImpl<SysRoleMenuMobileMapper, SysRoleMenuMobile> implements SysRoleMenuMobileService {

    @Override
    public List<Integer> listIdByRoleId(Integer roleId) {
        return baseMapper.listIdByRoleId(roleId);
    }

    @Override
    public List<String> listPermissionByRoleId(Integer roleId) {
        return baseMapper.listPermissionByRoleId(roleId);
    }

    @Override
    public List<SysMenuMobileTreeVo> treeByRoleId(Integer roleId) {
        List<SysMenuMobileTreeVo> list = baseMapper.treeByRoleId(roleId);
        return TreeUtil.build(list, -1);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateByRoleId(Integer roleId, List<Integer> menuIds) {
        //先删除原有菜单列表
        remove(Wrappers.lambdaQuery(SysRoleMenuMobile.class).eq(SysRoleMenuMobile::getRoleId, roleId));

        List<SysRoleMenuMobile> roleMenuMobiles = new ArrayList<>();
        //再添加菜单
        if (menuIds != null && menuIds.size() > 0) {
            for (Integer menuId : menuIds) {
                SysRoleMenuMobile sysRoleMenuMobile = new SysRoleMenuMobile();
                sysRoleMenuMobile.setMenuId(menuId);
                sysRoleMenuMobile.setRoleId(roleId);
                roleMenuMobiles.add(sysRoleMenuMobile);
            }
            saveBatch(roleMenuMobiles);
        }

    }

    @Override
    public List<SysMenuMobileTreeVo> treeTypeByRoleId(Integer roleId, Integer type) {
        List<SysMenuMobileTreeVo> list = baseMapper.treeTypeByRoleId(roleId, type);
        return TreeUtil.build(list, -1);
    }
}
