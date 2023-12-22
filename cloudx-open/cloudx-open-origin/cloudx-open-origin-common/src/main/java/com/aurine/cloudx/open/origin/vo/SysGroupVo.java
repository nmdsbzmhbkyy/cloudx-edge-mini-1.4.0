package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectInfo;
import com.aurine.cloudx.open.origin.entity.SysGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Title: SysGroupVo
 * Description: 项目组视图
 *
 * @author xull@aurine.cn
 * @version 1.0.0
 * @date 2020/5/12 10:46
 */
@Data
@ApiModel("项目组Vo")
public class SysGroupVo extends SysGroup {
    @ApiModelProperty("项目信息列表")
    List<ProjectInfo> projectInfos;
}
