package com.aurine.cloudx.open.origin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Title: SysUserVo
 * Description:
 *
 * @author xull@aurine.cn
 * @version 1.0.0
 * @date 2020/5/7 15:16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("人员Vo")
public class SysUserVo {
    @ApiModelProperty("用户id")
    private Integer userId;
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("用户名")
    private String trueName;
    @ApiModelProperty("手机号")
    private String phone;
    @ApiModelProperty("身份证")
    private String credentialNo;
    @ApiModelProperty("性别")
    private String sex;
    @ApiModelProperty("部门id")
    private Integer deptId;
    //    @ApiModelProperty("通行开始时间")
//    private LocalDate effTime;
//    @ApiModelProperty("通行结束时间")
//    private LocalDate expTime;
    @ApiModelProperty("角色id")
    private Integer roleId;
    @ApiModelProperty("更新角色id")
    private Integer oldRoleId;
    @ApiModelProperty("头像地址")
    private String avatar;
    @ApiModelProperty("层级类型id")
    private String deptTypeId;
}
