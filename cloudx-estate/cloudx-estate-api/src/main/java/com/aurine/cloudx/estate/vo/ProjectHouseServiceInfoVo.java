package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectHouseService;
import com.aurine.cloudx.estate.entity.SysServiceCfg;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Title: ProjectServiceInfoVo
 * Description:用于查看该项目配置的增值服务详情
 *
 * @author guhl@aurine.cn
 * @version 1.0.0
 * @date 2020/11/18 11:54
 */
@Data
@ApiModel(value = "项目增值服务详情vo")
public class ProjectHouseServiceInfoVo extends SysServiceCfg {
    @ApiModelProperty("项目增值服务截止时间")
    private LocalDateTime expTime;
}
