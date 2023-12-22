package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色菜单表(SysRoleMenuMobile)表实体类
 *
 * @author makejava
 * @since 2021-02-07 10:25:49
 */
@Data
@TableName("sys_role_menu_mobile")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "角色菜单表(SysRoleMenuMobile)")
public class SysRoleMenuMobile extends Model<SysRoleMenuMobile> {

    private static final long serialVersionUID = -48451251811223388L;

    @TableId
    @ApiModelProperty(value = "")
    private Integer id;


    /**
     * 角色ID
     */
    @ApiModelProperty(value = "角色ID")
    private Integer roleId;


    /**
     * 菜单ID
     */
    @ApiModelProperty(value = "菜单ID")
    private Integer menuId;


}
