
package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 房屋增值服务
 *
 * @author guhl@aurine.cn
 * @date 2020-06-04 15:23:14
 */
@Data
@TableName("project_house_service")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "房屋增值服务")
public class ProjectHouseService extends Model<ProjectHouseService> {
private static final long serialVersionUID = 1L;
    /**
     * 房屋id
     */
    @ApiModelProperty(value="房屋id")
    private String houseId;
    /**
     * 服务id
     */
    @ApiModelProperty(value="服务id")
    private String serviceId;
    /**
     * 生效时间
     */
    @ApiModelProperty(value = "生效时间")
    private LocalDateTime effTime;
    /**
     * 失效时间
     */
    @ApiModelProperty(value = "失效时间")
    private LocalDateTime expTime;
    /**
     * 创建人
     */
    @ApiModelProperty(value="创建人")
    @TableField(fill = FieldFill.INSERT)
    private Integer operator;
    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value="更新时间")
    private LocalDateTime updateTime;
    }
