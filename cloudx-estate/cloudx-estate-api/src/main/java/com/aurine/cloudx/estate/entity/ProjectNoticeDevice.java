

package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 设备配置信息发布
 *
 * @author guhl@aurine.cn
 * @date 2020-05-20 11:52:18
 */
@Data
@TableName("project_notice_device")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "设备配置信息发布")
public class ProjectNoticeDevice extends Model<ProjectNoticeDevice> {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Integer seq;
    /**
     * 信息id，关联project_notice.notice_id
     */
    @ApiModelProperty(value = "信息id，关联project_notice.notice_id")
    private String noticeId;
    /**
     * 设备id
     */
    @ApiModelProperty(value = "设备id")
    private String deviceId;
    /**
     * 下发状态 1 下载成功 2 下载失败 3 正在下载 4 已清除
     */
    @ApiModelProperty(value = "下发状态 1 下载成功 2 下载失败 3 正在下载 4 已清除")
    private String dlStatus;
    @ApiModelProperty("设备名称")
    private String deviceName;
    @ApiModelProperty("楼栋名称")
    private String buildingName;
    @ApiModelProperty("单元名称")
    private String unitName;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Integer operator;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
}
