

package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectUnitInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p> 单元配置VO </p>
 * @ClassName: ProjectUnitListVo
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/6/10 14:08
 * @Copyright:
 */
@Data
@ApiModel(value = "单元")
public class ProjectUnitListVo  {
private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="楼栋ID")
    private String buildingId;

    @ApiModelProperty(value="单元列表")
    private List<ProjectUnitInfoVo> unitList;

    }
