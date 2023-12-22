package com.aurine.cloudx.estate.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 集团管理
 *
 * @author xull@aurine.cn
 * @date 2020-04-29 16:23:11
 */
@Data
@TableName("sys_company")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "集团管理")
public class SysCompany extends Model<SysCompany> {
    private static final long serialVersionUID = 1L;


    /**
     * 关联pgixx.sys_dept.dept_id
     */
    @ApiModelProperty(value = "关联pgixx.sys_dept.dept_id")
    @TableId(type = IdType.INPUT)
    private Integer companyId;


    /**
     * 集团/企业名称
     */
    @ApiModelProperty(value = "集团/企业名称")
    private String companyName;
    /**
     * 联系人姓名
     */
    @ApiModelProperty(value = "联系人姓名")
    private String contactPerson;
    /**
     * 联系电话
     */
    @ApiModelProperty(value = "联系电话")
    private String contactPhone;
    /**
     * 企业邮箱
     */
    @ApiModelProperty(value = "企业邮箱")
    private String email;
    /**
     * 省份编码
     */
    @ApiModelProperty(value = "省份编码")
    private String provinceCode;
    /**
     * 城市编码
     */
    @ApiModelProperty(value = "城市编码")
    private String cityCode;
    /**
     * 区域编码
     */
    @ApiModelProperty(value = "区域编码")
    private String districtCode;
    /**
     * 详细地址
     */
    @ApiModelProperty(value = "详细地址")
    private String addr;
    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    @TableField(fill = FieldFill.INSERT)
    private Integer operator;
    /**
     *
     */
    @ApiModelProperty(value = "")
    private LocalDateTime createTime;
    /**
     *
     */
    @ApiModelProperty(value = "")
    private LocalDateTime updateTime;

    /**
     * logo地址
     */
    @ApiModelProperty(value = "logo地址")
    private String logoUrl;

}
