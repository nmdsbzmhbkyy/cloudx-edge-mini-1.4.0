package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectService;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Title: ProjectServiceVo
 * Description:用于接受设备名称
 *
 * @author guhl@aurine.cn
 * @version 1.0.0
 * @date 2020/6/4 13:42
 */
@Data
public class ProjectServiceVo extends ProjectService {
    @ApiModelProperty("设备名称")
    private String serviceName;
}
