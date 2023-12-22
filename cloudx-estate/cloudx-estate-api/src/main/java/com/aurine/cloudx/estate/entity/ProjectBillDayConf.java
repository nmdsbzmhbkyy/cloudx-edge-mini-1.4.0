package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * 账单日设置(ProjectBillDayConf)表实体类
 *
 * @author makejava
 * @since 2020-07-20 16:43:48
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "账单日设置(ProjectBillDayConf)")
public class ProjectBillDayConf extends Model<ProjectBillDayConf> {

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
    @NotBlank
    @Pattern(regexp = "([1-9]|[1-2]\\d|3[0-1])",message = "账单日格式错误")
    private String billDay;

    @ApiModelProperty(value = "账单生成月数")
    @NotBlank
    @Pattern(regexp = "([1-9]|[1-2]\\d|3[0-1])",message = "账单生成月数必须为整数")

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
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Integer operator;


    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;


}