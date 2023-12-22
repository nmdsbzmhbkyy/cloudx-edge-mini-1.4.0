package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectTraining;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * (ProjectTrainingPageVo) 培训设置分页vo
 *
 * @author guhl
 * @since 2021/1/13 10:16
 */
@Data
@ApiModel("培训设置分页vo")
public class ProjectTrainingPageVo extends ProjectTraining {
    /**
     * 参与人数
     */
    @ApiModelProperty("参与人数")
    Integer staffCount;

    /**
     * 开始时间字符串
     */
    @ApiModelProperty("开始时间字符串")
    String beginTimeString;

    /**
     * 结束时间字符串
     */
    @ApiModelProperty("结束时间字符串")
    String endTimeString;

    /**
     * 完成人数
     */
    @ApiModelProperty("完成人数")
    Integer doneCount;

    /**
     * 员工id
     */
    @ApiModelProperty("员工id")
    String staffId;

    /**
     * 培训状态
     */
    @ApiModelProperty("培训状态")
    Integer status;

    /**
     * 当前时间
     */
    @ApiModelProperty("当前时间")
    String nowTime;
}
