package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 *  员工工作情况-用于员工查看-工作情况数据展示
 * </p>
 *
 * @ClassName: ProjectWorkConditionVo
 * @author: 王良俊 <>
 * @date: 2020年12月11日 下午04:44:24
 * @Copyright:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectStaffWorkVo {
    
    /**
     * 任务名称
     * */
    private String taskName;
    
    /**
     * 已接收（总共要完成的工作量）
     * */
    @ApiModelProperty(value = "已接收（总共要完成的工作量）")
    private Integer allTaskNum;
    
    /**
     * 未完成（剩余的工作量）
     * */
    @ApiModelProperty(value = "未完成（剩余的工作量）")
    private Integer unCompletedTaskNum;

    /**
     * 已完成（已经完成的工作量）
     * */
    @ApiModelProperty(value = "已完成（已经完成的工作量）")
    private Integer completedTaskNum;

}
