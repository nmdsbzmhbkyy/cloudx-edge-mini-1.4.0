

package com.aurine.cloudx.open.origin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 项目组
 *
 * @author xull@aurine.cn
 * @date 2020-04-30 16:04:44
 */
@Data
@TableName("sys_project_group")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "项目组")
public class SysGroup extends Model<SysGroup> {
    private static final long serialVersionUID = 1L;

    /**
     * 关联pigxx.sys_dept.dept_id
     */
    @ApiModelProperty(value = "关联pigxx.sys_dept.dept_id")
    @TableId(type = IdType.INPUT)
    private Integer projectGroupId;
    /**
     * 项目组名称
     */
    @ApiModelProperty(value = "项目组名称")
    private String projectGroupName;

    /**
     * 父级集团id
     */
    @ApiModelProperty(value = "父级集团id")
    @TableField(exist = false)
    private Integer parentId;

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
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
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
