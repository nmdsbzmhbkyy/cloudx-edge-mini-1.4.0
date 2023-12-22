package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.vo.SysMenuMobileTreeVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aurine.cloudx.estate.entity.SysRoleMenuMobile;

import java.util.List;

/**
 * 角色菜单表(SysRoleMenuMobile)表服务接口
 *
 * @author makejava
 * @since 2021-02-07 10:25:50
 */
public interface SysRoleMenuMobileService extends IService<SysRoleMenuMobile> {


    List<Integer> listIdByRoleId(Integer roleId);

    List<String> listPermissionByRoleId(Integer roleId);

    List<SysMenuMobileTreeVo> treeByRoleId(Integer roleId);

    void updateByRoleId(Integer roleId, List<Integer> menuIds);

    List<SysMenuMobileTreeVo> treeTypeByRoleId(Integer roleId, Integer type);
}
