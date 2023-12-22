package com.aurine.cloudx.estate.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel("报修详情")
public class AppRepairRecordInfoVo {
    /**
     * 报修单号，uuid
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "报修单号，uuid")
    private String repairId;
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
     * 房间ID
     */
    @ApiModelProperty(value = "房间ID")
    private String houseId;
    /**
     * 报修类型 参考字典类型 repair_type
     */
    @ApiModelProperty(value = "报修类型（0 水电煤气 1 家电家具 2 电梯 3 门禁 4 公共设施 5 物业设备 6 其他报修）")
    private String repairType;
    /**
     * 报修内容
     */
    @ApiModelProperty(value = "报修内容")
    private String content;
    /**
     * 图片1
     */
    @ApiModelProperty(value = "图片1 Base64")
    private String picPath1;
    /**
     * 图片2
     */
    @ApiModelProperty(value = "图片2 Base64")
    private String picPath2;
    /**
     * 图片3
     */
    @ApiModelProperty(value = "图片3 Base64")
    private String picPath3;
    /**
     * 图片4
     */
    @ApiModelProperty(value = "图片4 Base64")
    private String picPath4;
    /**
     * 图片5
     */
    @ApiModelProperty(value = "图片5 Base64")
    private String picPath5;
    /**
     * 图片6
     */
    @ApiModelProperty(value = "图片6 Base64")
    private String picPath6;
    /**
     * 是否尽快 0 否 1 是
     */
    @ApiModelProperty(value = "是否尽快 0 否 1 是")
    private String isASAP;
    /**
     * 预约时间段起始
     */
    @ApiModelProperty(value = "预约时间段起始")
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime reserveTimeBegin;
    /**
     * 预约时间段结束
     */
    @ApiModelProperty(value = "预约时间段结束")
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime reserveTimeEnd;
    /**
     * 处理状态 参考字典类型 maintain_status
     */
    @ApiModelProperty(value = "处理状态（0 待抢单 1 待完成 2 已完成） ")
    private String status;
    /**
     * 接单时间
     */
    @ApiModelProperty(value = "接单时间")
    private LocalDateTime orderTime;
    /**
     * 维修类型 参考字典 maintain_type
     */
    @ApiModelProperty(value = "维修类型（1 公用 2 特约 3 其他）")
    private String maintainType;
    /**
     * 维修时间
     */
    @ApiModelProperty(value = "维修时间")
    private LocalDateTime maintainTime;

    /**
     * 维修费用
     */
    @ApiModelProperty(value = "维修费用")
    private BigDecimal feeAmount;

    /**
     * 完成时间
     */
    @ApiModelProperty(value = "完成时间")
    private LocalDateTime doneTime;
    /**
     * 维修说明
     */
    @ApiModelProperty(value = "维修说明")
    private String remark;
    /**
     * 完成图片
     */
    @ApiModelProperty(value = "完成图片 Base64")
    private String donePicPath;

    @ApiModelProperty(value = "完成图片2 Base64")
    private String donePicPath2;

    @ApiModelProperty(value = "完成图片3 Base64")
    private String donePicPath3;

    @ApiModelProperty(value = "完成图片4 Base64")
    private String donePicPath4;

    @ApiModelProperty(value = "完成图片5 Base64")
    private String donePicPath5;

    @ApiModelProperty(value = "完成图片6 Base64")
    private String donePicPath6;
    /**
     * 关闭原因 参考字典repair_close_reason
     */
    @ApiModelProperty(value = "关闭原因")
    private String repairCloseReason;
    /**
     * 关闭备注
     */
    @ApiModelProperty(value = "关闭备注")
    private String closeRemark;
    /**
     * 处理人
     */
    @ApiModelProperty(value = "处理人")
    private String handler;

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

    @ApiModelProperty("当前房屋名称")
    String currentHouseName;
    @ApiModelProperty("代报人联系方式")
    String phone;
    @ApiModelProperty("代报人姓名")
    String trueName;
    @ApiModelProperty("处理人姓名")
    String handlerName;
}
