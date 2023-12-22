package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectInspectCheckinDetail;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 巡检点签到明细(ProjectInspectCheckinDetail)表实体类
 *
 * @author 王良俊
 * @since 2020-08-04 10:08:51
 */
@Data
@ApiModel(value = "巡检点签到明细Vo对象(ProjectInspectCheckinDetail)")
public class ProjectInspectCheckinDetailVo extends ProjectInspectCheckinDetail {

    @ApiModelProperty(value = "巡检任务ID")
    private String taskId;


}