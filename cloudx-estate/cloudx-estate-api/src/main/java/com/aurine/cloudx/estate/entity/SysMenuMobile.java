package com.aurine.cloudx.estate.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 移动端菜单权限表(SysMenuMobile)表实体类
 *
 * @author makejava
 * @since 2021-02-07 10:23:21
 */
@Data
@TableName("sys_menu_mobile")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "移动端菜单权限表(SysMenuMobile)")
public class SysMenuMobile extends Model<SysMenuMobile> {

    private static final long serialVersionUID = -78231550333793499L;

    /**
     * 菜单ID
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "菜单ID")
    private Integer menuId;


    @ApiModelProperty(value = "")
    private String name;


    @ApiModelProperty(value = "")
    private String permission;


    /**
     * 父菜单ID
     */
    @ApiModelProperty(value = "父菜单ID")
    private Integer parentId;


    /**
     * 排序值
     */
    @ApiModelProperty(value = "排序值")
    private Integer sort;


    @ApiModelProperty(value = "是否按钮")
    private String menuType;


    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;


    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;




    /**
     * 区分小程序和APP特有的功能
     */
    @ApiModelProperty(value = "区分小程序和APP特有的功能")
    private String type;


}
