package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectUnitInfo;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @ClassName: ProjectUnitInfoVo
 * @author: 王良俊 <>
 * @date:  2020年08月12日 下午02:28:50
 * @Copyright:
*/
@Data
@TableName("project_unit_info")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "单元")
public class ProjectUnitInfoVo extends ProjectUnitInfo {

    @ApiModelProperty(value = "单元图片url数组")
    String[] imgArr;

    @ApiModelProperty(value = "单元图片url数组")
    List<ProjectUnitFileVo> fileList;

    /**
     * 项目ID
     */
    @ApiModelProperty(value = "项目ID")
    private Integer projectId;

    /**
     * 租户号
     */
    private Integer tenantId;

}
