

package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 信息发布
 *
 * @author guhl@aurine.cn
 * @date 2020-05-20 11:52:46
 */
@Data
@TableName("project_notice")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "信息发布")
public class ProjectNotice extends Model<ProjectNotice> {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "序列")
    private Integer seq;
    /**
     * 信息id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "信息id")
    private String noticeId;
    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Integer projectId;
    /**
     * 公告分类 1 室内机 2 梯口/区口机
     */
    @ApiModelProperty(value = "公告分类 1 室内机 2 梯口/区口机")
    private String noticeType;
    /**
     * 类型 '1' 纯文本 '2' 富文本
     */
    @ApiModelProperty(value = "类型 '1' 纯文本 '2' 富文本")
    private String contentType;
    /**
     * 信息分类
     */
    @ApiModelProperty(value = "信息分类 1001.物业缴费 1004.温馨提示 9999.其他 见字典 house_notice_category和entra_notice_category 1001.物业缴费 1002.应急广播 1003.公益服务 1004.温馨提示 9999.其他")
    private String noticeCategory;
    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String noticeTitle;
    /**
     * 文本内容
     */
    @ApiModelProperty(value = "文本内容")
    private String content;
    /**
     * 发送区域，冗余项，用作拼接展示
     */
    @ApiModelProperty(value = "发送区域，冗余项，用作拼接展示")
    private String target;
    /**
     * 发布时间
     */
    @ApiModelProperty(value = "发布时间")
    private LocalDateTime pubTime;
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

    /**
     * 发送目标
     */
    @ApiModelProperty("发送目标：1、室内机，2：移动端，3：app 4：短信  关联字典notice_channel")
    String targetType;

    @TableField(value = "tenant_id")
    private Integer tenantId;
}
