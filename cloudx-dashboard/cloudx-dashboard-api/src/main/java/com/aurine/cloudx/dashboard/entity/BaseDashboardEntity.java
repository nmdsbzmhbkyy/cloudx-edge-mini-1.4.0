package com.aurine.cloudx.dashboard.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description: Dashboard 基础对象
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-25
 * @Copyright:
 */
@Data
public class BaseDashboardEntity {

    @ApiModelProperty("项目ID")
    private Integer projectId;

    @ApiModelProperty("项目名称")
    private String projectName;

//    @ApiModelProperty("更新时间")
//    @TableField("UPDATE_TIME")
//    private String updateTime;
}
