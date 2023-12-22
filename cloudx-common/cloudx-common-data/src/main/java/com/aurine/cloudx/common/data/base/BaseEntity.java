package com.aurine.cloudx.common.data.base;

import com.baomidou.mybatisplus.annotation.*;
import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/***
 * @title BaseEntity
 * @description
 * @author cyw
 * @version 1.0.0
 * @create 2023/6/9 10:08
 **/
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableLogic
    @ApiModelProperty(value = "是否删除")
    private Integer isDeleted = 0;

    @ApiModelProperty(value = "操作人")
    @TableField(fill = FieldFill.INSERT)
    private Integer operator;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /** 项目ID */
    @ApiModelProperty(name = "项目ID",notes = "")
    private Integer projectId ;
    /** 租户ID */
    @ApiModelProperty(name = "租户ID",notes = "")
    @TableField("tenant_id")
    private String tenantId ;
}
