package com.aurine.cloudx.open.origin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 报警类型定义管理
 *
 * @author 谢泽毅
 * @date 2021-07-08 10:12:15
 */

@Data
@TableName("sys_event_type_conf")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "报警类型定义管理")
public class SysEventTypeConf extends Model<SysEventTypeConf>{
    private static final long serialVersionUID = 1L;
//    @ApiModelProperty("序列")
//    private Integer seq;
//    @TableId(type = IdType.INPUT)
    @ApiModelProperty("事件类型id")
    private String eventTypeId;
    @ApiModelProperty("设备类型")
    private String deviceType;
    @ApiModelProperty("事件类型名称")
    private String eventTypeName;
    @ApiModelProperty("报警分类")
    private String eventCategory;
    @ApiModelProperty("报警级别")
    private String eventLevel;
//    @ApiModelProperty("tenant_id")
//    private Integer tenant_Id;
    @ApiModelProperty("创建人")
    private String operator;
    @ApiModelProperty("操作时间")
    private LocalDateTime createTime;
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

}
