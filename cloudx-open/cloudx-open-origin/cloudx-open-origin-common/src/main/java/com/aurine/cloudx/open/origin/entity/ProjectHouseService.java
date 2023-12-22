
package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.TableName;
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
public class ProjectHouseService extends OpenBasePo<ProjectHouseService> {
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
    }
