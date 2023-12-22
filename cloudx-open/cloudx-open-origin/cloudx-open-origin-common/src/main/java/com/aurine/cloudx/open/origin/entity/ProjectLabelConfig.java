

package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;

/**
 * 标签管理
 *
 * @author 王伟
 * @date 2020-05-07 08:09:35
 */
@Data
@TableName("project_label_config")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "标签管理")
public class ProjectLabelConfig extends OpenBasePo<ProjectLabelConfig> {
    private static final long serialVersionUID = 1L;

    /**
     * uid,标签id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "uid,标签id")
    private String labelId;
    /**
     * 标签编码，可用于第三方编码
     */
    @ApiModelProperty(value = "标签编码，可用于第三方编码")
    private String labelCode;
    /**
     * 人员特征标签
     */
//    @NotEmpty
    @ApiModelProperty(value = "人员特征标签")
    private String labelName;
    /**
     * 人员特征标签
     */
    @ApiModelProperty(value = "标签颜色代码")
    private String labelColor;
    /**
     * 标签描述
     */
    @ApiModelProperty(value = "标签描述")
    private String remark;
//    /**
//     * 项目ID
//     */
//    @ApiModelProperty(value = "项目ID")
//    private Integer projectId;
    /**
     * 标签使用次数
     */
    @ApiModelProperty(value = "标签使用次数")
    private Integer useCount;
}
