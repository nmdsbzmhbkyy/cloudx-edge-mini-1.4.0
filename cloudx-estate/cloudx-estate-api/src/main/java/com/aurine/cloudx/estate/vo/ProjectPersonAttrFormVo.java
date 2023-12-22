package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectPersonAttr;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * (ProjectPersonAttrFormVo)
 *
 * @author xull
 * @since 2020/7/6 9:01
 */
@Data
@ApiModel("人员拓展属性表单")
public class ProjectPersonAttrFormVo {
    /**
     * 人员参数key-value视图列表
     */
    @ApiModelProperty(value = "人员参数key-value视图列表")
    List<ProjectPersonAttrListVo> projectPersonAttrList = new ArrayList<>();
    /**
     * 人员类型
     */
    @ApiModelProperty(value = "人员类型")
    private String type;
    /**
     * 人员id
     */
    @ApiModelProperty(value = "人员id")
    private String personId;
    /**
     * 项目Id
     */
    @ApiModelProperty(value = "项目Id")
    private Integer projectId;
}
