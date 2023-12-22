package com.aurine.cloudx.open.origin.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * <p>
 * pigx系统用户对象
 * </p>
 *
 * @author 王良俊
 * @since 2022/6/28 9:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PigxxUserInfo {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    private Integer user_id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @JsonIgnore
    @ApiModelProperty("随机盐")
    private String salt;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;

    @TableLogic
    @ApiModelProperty("删除标记,1:已删除,0:正常")
    private String del_flag;

    @ApiModelProperty("锁定标记")
    private String lock_flag;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("头像地址")
    private String avatar;

    @ApiModelProperty("用户所属部门id")
    private Integer dept_id;

    @ApiModelProperty("用户所属租户id")
    private Integer tenant_id;

    @ApiModelProperty("微信openid")
    private String wx_openid;

    @ApiModelProperty("微信小程序openid")
    private String mini_openid;

    @ApiModelProperty("物业微信小程序openid")
    private String mini_openid_wy;

    @ApiModelProperty("业主微信小程序openid")
    private String mini_openid_yz;

    @ApiModelProperty("访客微信小程序openid")
    private String mini_openid_fk;

    @ApiModelProperty("QQ openid")
    private String qq_openid;

    @ApiModelProperty("码云唯一标识")
    private String gitee_login;

    @ApiModelProperty("开源中国唯一标识")
    private String osc_id;

    @ApiModelProperty("证件类型")
    private String credential_type;

    @ApiModelProperty("证件号码")
    private String credential_no;

    @ApiModelProperty("性别")
    private String sex;

    @ApiModelProperty("生效时间")
    private LocalDateTime effTime;

    @ApiModelProperty("失效时间")
    private LocalDateTime expTime;

    @ApiModelProperty("真实姓名")
    private String true_name;
}
