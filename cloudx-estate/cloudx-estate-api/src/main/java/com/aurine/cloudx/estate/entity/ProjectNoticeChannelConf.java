package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 消息推送渠道设置(ProjectNoticeChannelConf)表实体类
 *
 * @author makejava
 * @since 2020-12-14 09:46:48
 */
@Data
@TableName("project_notice_channel_conf")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "消息推送渠道设置(ProjectNoticeChannelConf)")
public class ProjectNoticeChannelConf extends Model<ProjectNoticeChannelConf> {

    private static final long serialVersionUID = 578021053182528122L;

    /**
     * 渠道id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "渠道id")
    private String channelId;

    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Integer projectId;


    /**
     * 渠道名称
     */
    @ApiModelProperty(value = "渠道名称")
    private String channelName;


    /**
     * 是否启用，'1' 是 '0' 否   （需求默认全开，这里做减法）
     */
    @ApiModelProperty(value = "是否启用，'1' 是 '0' 否   （需求默认全开，这里做减法）")
    private String isActive;


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