package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Title: ProjectInfoTimeFormVo
 * Description: 设置项目配置设置时间表达
 *
 * @author xull@aurine.cn
 * @version 1.0.0
 * @date 2020/6/9 10:03
 */
@Data
@ApiModel("设置项目配置设置时间表达")
public class ProjectInfoTimeFormVo {
    @ApiModelProperty("项目id")
    private Integer projectId;
    @ApiModelProperty("过期时间")
    private String expTime;
}
