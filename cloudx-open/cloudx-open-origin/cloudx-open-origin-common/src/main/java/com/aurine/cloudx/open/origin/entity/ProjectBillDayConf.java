package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 账单日设置(ProjectBillDayConf)表实体类
 *
 * @author makejava
 * @since 2020-07-20 16:43:48
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "账单日设置(ProjectBillDayConf)")
public class ProjectBillDayConf extends OpenBasePo<ProjectBillDayConf> {

    private static final long serialVersionUID = 299235534518274417L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "主键")
    private String recordId;


    /**
     * 账单日，每月%d日
     */
    @ApiModelProperty(value = "账单日，每月%d日")
//    @NotBlank
//    @Pattern(regexp = "([1-9]|[1-2]\\d|3[0-1])",message = "账单日格式错误")
    private String billDay;

    @ApiModelProperty(value = "账单生成月数")
//    @NotBlank
//    @Pattern(regexp = "([1-9]|[1-2]\\d|3[0-1])",message = "账单生成月数必须为整数")

    private String billMonths;

    /**
     * 状态 1 启用 0 停用
     */
    @ApiModelProperty(value = "状态 1 启用 0 停用")
    @Pattern(regexp = "([0-1])",message = "状态值有误")
    private String status;

    /**
     * 项目id
     */
    @ApiModelProperty("项目id")
    private Integer projectId ;


}