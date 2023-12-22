package com.aurine.cloudx.open.origin.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("project_floor_pic")
@ApiModel(value = "平面图对象", description = "平面图对象")
public class ProjectFloorPic {

    private static final long serialVersionUID = -2209266605293377427L;
    public static final String DEFAULT_USERNAME = "system";

    @ApiModelProperty(value = "唯一id")
    @TableId(type = IdType.ASSIGN_UUID)
    private String picId;

    @ApiModelProperty(value = "项目编码")
    private Integer projectId;

    @ApiModelProperty(value = "平面图名称")
    private String picName;
    @ApiModelProperty(value = "分辨率")
    private String picResolution;

    @ApiModelProperty(value = "访问路径")
    private String accessPath;

    @ApiModelProperty(value = "操作人id")
    @TableField(fill = FieldFill.INSERT)
    private Integer operator;

    @ApiModelProperty(value = "区域id")
    private String regionId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
