package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectTrainingFileDb;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * (ProjectTrainingFileDbVo) 主要用于员工资料读取进度查看
 *
 * @author guhl
 * @since 2021/1/18 10:16
 */
@Data
@ApiModel(value = "主要用于员工资料读取进度查看")
public class ProjectTrainingFileDbVo extends ProjectTrainingFileDb {
    /**
     * 进度
     */
    @ApiModelProperty("进度")
    String progress;
}
