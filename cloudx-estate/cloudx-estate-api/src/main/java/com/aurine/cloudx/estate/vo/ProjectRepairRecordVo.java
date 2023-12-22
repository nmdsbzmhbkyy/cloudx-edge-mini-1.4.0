package com.aurine.cloudx.estate.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * (cloudx-estate-service)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/9/2 11:51
 */
@Data
public class ProjectRepairRecordVo {

    /**
     * 报修单号，uuid
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value="报修单号，uuid")
    private String repairId;
    /**
     * 联系人姓名
     */
    @ApiModelProperty(value="联系人姓名")
    private String personName;
    /**
     * 联系人电话
     */
    @ApiModelProperty(value="联系人电话")
    private String phoneNumber;
    /**
     * 房号
     */
    @ApiModelProperty(value="房号")
    private String houseId;
    /**
     * 房屋名称
     */
    @ApiModelProperty(value="房号")
    private String houseName;
    /**
     * 楼栋名称
     */
    @ApiModelProperty(value="楼栋名称")
    private String buildingName;
    /**
     * 单元名称
     */
    @ApiModelProperty(value="单元名称")
    private String unitName;
    /**
     * 详细地址
     */
    @ApiModelProperty(value="详细地址")
    private String buildingAddress;
    /**
     * 报修类型 参考字典类型 repair_type
     */
    @ApiModelProperty(value="报修类型 0.水电煤气 1.家电家具 2.电梯 3.门禁 4.公共设施 5.物业设备 6.其他报修\n 参考字典类型 repair_type")
    private String repairType;
    /**
     * 报修内容
     */
    @ApiModelProperty(value="报修内容")
    private String content;
    /**
     * 是否尽快 0 否 1 是
     */
    @ApiModelProperty(value="是否尽快 0 否 1 是")
    private String isASAP;
    /**
     * 预约时间段起始
     */
    @ApiModelProperty(value="预约时间段起始")
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime reserveTimeBegin;
    /**
     * 预约时间段结束
     */
    @ApiModelProperty(value="预约时间段结束")
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime reserveTimeEnd;
    /**
     * 处理状态 参考字典类型 maintain_status
     */
    @ApiModelProperty(value="处理状态 0.待抢单 1.待完成 2.待评价 3.已完成 参考字典类型 maintain_status ")
    private String status;
    /**
     * 处理人
     */
    @ApiModelProperty(value="处理人")
    private String handler;
    /**
     * 处理人姓名
     */
    @ApiModelProperty(value="处理人姓名")
    private String staffName;
    /**
     * 评分1-5
     */
    @ApiModelProperty("评分1-5")
    private Integer score;

    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value="更新时间")
    private LocalDateTime updateTime;

    /**
     * 完成时间
     */
    @ApiModelProperty(value = "完成时间")
    private LocalDateTime doneTime;

    /**
     * 维修时间
     */
    @ApiModelProperty(value = "维修时间")
    private LocalDateTime maintainTime;
}
