package com.aurine.cloudx.estate.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * (ProjectComplainRecordVo)投诉信息视图
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/9/1 16:28
 */
@Data
public class ProjectComplaintRecordVo {
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
     * 房屋名称
     */
    @ApiModelProperty(value = "房屋名称")
    private String houseName;
    /**
     * 单元名称
     */
    @ApiModelProperty(value = "单元名称")
    private String unitName;
    /**
     * 楼栋名称
     */
    @ApiModelProperty(value = "楼栋名称")
    private String buildingName;
    /**
     * 楼栋地址
     */
    @ApiModelProperty(value = "楼栋地址")
    private String buildingAddress;
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
     * 处理状态 参考字典类型 complaint_status
     */
    @ApiModelProperty(value = "处理状态 0: 待抢单 1: 待完成 2: 已完成 参考字典类型 complaint_status ")
    private String status;
    /**
     * 处理人
     */
    @ApiModelProperty(value = "处理人")
    private String handler;
    /**
     * 处理人姓名
     */
    @ApiModelProperty(value = "处理人姓名")
    private String staffName;
    /**
     * 评分1-5
     */
    @ApiModelProperty("评分1-5")
    private Integer score;
    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建人")
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
     *  完成时间
     */
    @ApiModelProperty(value = "完成时间")
    private LocalDateTime doneTime;
}
