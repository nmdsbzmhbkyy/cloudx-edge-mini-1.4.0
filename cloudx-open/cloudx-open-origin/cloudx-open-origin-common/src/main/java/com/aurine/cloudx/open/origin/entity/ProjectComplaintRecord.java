package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 项目投诉服务记录
 *
 * @author guhl@aurine.cn
 * @date 2020-07-20 13:34:42
 */
@Data
@TableName("project_complaint_record")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "项目投诉服务记录")
public class ProjectComplaintRecord extends OpenBasePo<ProjectComplaintRecord> {
    private static final long serialVersionUID = 1L;

    /**
     * 投诉单号，uuid
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "投诉单号，uuid")
    private String complaintId;
    /**
     * 联系人姓名
     */
    @ApiModelProperty(value = "联系人姓名")
    private String personName;
    /**
     * 联系人电话
     */
    @ApiModelProperty(value = "联系人电话")
    private String phoneNumber;
    /**
     * 房号
     */
    @ApiModelProperty(value = "房号")
    private String houseId;
    /**
     * 投诉类型 参考字典类型 complaint_type
     */
    @ApiModelProperty(value = "投诉类型 0.环境 1.安全 2.秩序 3.工作人员 4.设备设施 5.供水 6.消防 7.供气8.供电 9.便民设施 10.其他投诉 参考字典类型 complaint_type")
    private String complaintType;
    /**
     * 投诉内容
     */
    @ApiModelProperty(value = "投诉内容")
    private String content;
    /**
     * 图片1
     */
    @ApiModelProperty(value = "图片1")
    private String picPath1;
    /**
     * 图片2
     */
    @ApiModelProperty(value = "图片2")
    private String picPath2;
    /**
     * 图片3
     */
    @ApiModelProperty(value = "图片3")
    private String picPath3;
    /**
     * 图片4
     */
    @ApiModelProperty(value = "图片4")
    private String picPath4;
    /**
     * 图片5
     */
    @ApiModelProperty(value = "图片5")
    private String picPath5;
    /**
     * 图片6
     */
    @ApiModelProperty(value = "图片6")
    private String picPath6;
    /**
     * 处理状态 参考字典类型 complaint_status
     */
    @ApiModelProperty(value = "处理状态 0: 待抢单 1: 待完成 2: 已完成 参考字典类型 complaint_status ")
    private String status;
    /**
     * 接单时间
     */
    @ApiModelProperty(value = "接单时间")
    private LocalDateTime orderTime;
    /**
     * 完成时间
     */
    @ApiModelProperty(value = "完成时间")
    private LocalDateTime doneTime;
    /**
     * 回复内容
     */
    @ApiModelProperty(value = "回复内容")
    private String replyContent;

    /**
     * 完成图片
     */
    @ApiModelProperty(value = "完成图片")
    private String donePicPath;

    @ApiModelProperty(value = "完成图片2")
    private String donePicPath2;

    @ApiModelProperty(value = "完成图片3")
    private String donePicPath3;

    @ApiModelProperty(value = "完成图片4")
    private String donePicPath4;

    @ApiModelProperty(value = "完成图片5")
    private String donePicPath5;

    @ApiModelProperty(value = "完成图片6")
    private String donePicPath6;
    /**
     * 关闭原因 参考字典complaint_close_reason
     */
    @ApiModelProperty(value = "关闭原因 参考字典complaint_close_reason")
    private String complaintCloseReason;
    /**
     * 关闭备注
     */
    @ApiModelProperty(value = "关闭备注")
    private String closereMark;
    /**
     * 评分1-5
     */
    @ApiModelProperty("评分1-5")
    private Integer score;
    /**
     * 处理人
     */
    @ApiModelProperty(value = "处理人")
    private String handler;
}
