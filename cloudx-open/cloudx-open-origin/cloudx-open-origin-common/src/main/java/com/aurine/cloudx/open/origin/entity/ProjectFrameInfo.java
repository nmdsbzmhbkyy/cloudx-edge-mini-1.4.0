
package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 框架
 *
 * @author 王伟
 * @date 2020-05-07 14:00:08
 */
@Data
@TableName("project_frame_info")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "框架")
public class ProjectFrameInfo extends OpenBasePo<ProjectFrameInfo> {
    private static final long serialVersionUID = 1L;


    /**
     * uid，实体ID
     */

    @TableId(type = IdType.ASSIGN_UUID)
//    @TableId(type = IdType.INPUT)
    @ApiModelProperty(value = "uid，实体ID")
    private String entityId;

    /**
     * 实体编码
     */
    @ApiModelProperty(value = "实体编码")
    private String entityCode;

    /**
     * 框架编码
     */
    @ApiModelProperty(value = "框架编码")
    private String frameNo;
    
    /**
     * 实体名称
     */
    @ApiModelProperty(value = "实体名称")
    private String entityName;
    /**
     * 上级id
     */
    @ApiModelProperty(value = "上级id")
    private String puid;
    /**
     * 是否楼栋
     */
    @ApiModelProperty(value = "是否楼栋")
    private String isBuilding;
    /**
     * 是否单元
     */
    @ApiModelProperty(value = "是否单元")
    private String isUnit;
    /**
     * 是否房屋
     */
    @ApiModelProperty(value = "是否房屋")
    private String isHouse;
    /**
     * 层级
     */
    @ApiModelProperty(value = "层级")
    private Integer level;
}
