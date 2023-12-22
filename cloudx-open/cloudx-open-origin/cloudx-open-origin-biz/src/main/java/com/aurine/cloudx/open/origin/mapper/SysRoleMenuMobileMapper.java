package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.origin.entity.SysRoleMenuMobile;
import com.aurine.cloudx.open.origin.vo.SysMenuMobileTreeVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色菜单表(SysRoleMenuMobile)表数据库访问层
 *
 * @author makejava
 * @since 2021-02-07 10:25:50
 */
@Mapper
public interface SysRoleMenuMobileMapper extends BaseMapper<SysRoleMenuMobile> {

    List<Integer> listIdByRoleId(@Param("roleId") Integer roleId);

    List<String> listPermissionByRoleId(@Param("roleId") Integer roleId);

    List<SysMenuMobileTreeVo> treeByRoleId(@Param("roleId") Integer roleId);

    List<SysMenuMobileTreeVo> treeTypeByRoleId(@Param("roleId") Integer roleId, @Param("type") Integer type);
}
