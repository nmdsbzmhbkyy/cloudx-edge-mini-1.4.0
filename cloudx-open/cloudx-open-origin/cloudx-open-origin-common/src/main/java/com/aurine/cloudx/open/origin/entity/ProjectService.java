

package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 项目增值服务
 *
 * @author guhl@aurine.cn
 * @date 2020-06-04 11:18:39
 */
@Data
@TableName("project_service")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "项目增值服务")
public class ProjectService extends OpenBasePo<ProjectService> {
    private static final long serialVersionUID = 1L;


    /**
     * 服务id
     */
    @ApiModelProperty(value = "服务id")
    private String serviceId;
    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Integer projectId;
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
}
