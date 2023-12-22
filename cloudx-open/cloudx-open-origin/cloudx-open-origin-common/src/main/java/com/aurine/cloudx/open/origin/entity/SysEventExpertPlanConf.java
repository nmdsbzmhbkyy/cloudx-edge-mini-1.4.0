package com.aurine.cloudx.open.origin.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author 谢泽毅
 */
@Data
@TableName("sys_event_expert_plan_conf")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "事件专家预案定义")
public class SysEventExpertPlanConf extends Model<SysEventExpertPlanConf> {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty("预案id")
    private String planId;
    @ApiModelProperty("预案名称")
    private String planName;
    @ApiModelProperty("预案内容")
    private String planContent;
    @ApiModelProperty("排序")
    private Integer planSort;
    @ApiModelProperty("tenant_id")
    private Integer tenant_Id;
    @ApiModelProperty("创建人")
    private Integer operator;
    @ApiModelProperty("操作时间")
    private LocalDateTime createTime;
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
