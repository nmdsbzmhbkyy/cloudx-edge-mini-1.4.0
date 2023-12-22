package com.aurine.cloudx.open.origin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 用户注销数据
 * @date 2021-03-17 17:29:48
 */
@Data
@TableName("sys_user_lock")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "用户注销数据")
public class SysUserLock extends Model<SysUserLock> {
private static final long serialVersionUID = 1L;

    @TableId
    @ApiModelProperty(value="主键")
    private Integer id;

    @ApiModelProperty(value="用户id")
    private Integer userId;

    @ApiModelProperty(value="是否注销")
    private String flag;

    @ApiModelProperty(value="注销时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value="电话")
    private String phone;
}
