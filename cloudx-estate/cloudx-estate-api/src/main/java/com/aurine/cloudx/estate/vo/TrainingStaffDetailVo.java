package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectTraining;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Title: TrainingStaffDetailVo
 * Description:
 *
 * @author guhl@aurine.cn
 * @version 1.0.0
 * @date 2021/1/20 15:16
 */
@Data
@ApiModel("小程序培训vo")
public class TrainingStaffDetailVo extends ProjectTraining {
    @ApiModelProperty("需要阅读的资料数")
    Integer total;
    @ApiModelProperty("已阅读资料数")
    Integer doneCount;
    @ApiModelProperty("时间排序 0升序 1降序")
    String timeOrderBy;
    @ApiModelProperty("员工ID")
    String staffId;
    @ApiModelProperty("状态 (0 未培训 1 培训中 2 已完成 )")
    String status;
}
