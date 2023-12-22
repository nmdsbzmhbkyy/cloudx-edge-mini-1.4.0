

package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 访客记录
 *
 * @author 王伟
 * @date 2020-06-03 19:43:06
 */
@Data
@TableName("project_visitor_his")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "访客记录")
public class ProjectVisitorHis extends OpenBasePo<ProjectVisitorHis> {
    private static final long serialVersionUID = 1L;


    /**
     * 来访id，uuid，标识一次访客的来访事件
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "来访id，uuid，标识一次访客的来访事件")
    private String visitId;
    /**
     * 访客id
     */
    @ApiModelProperty(value = "访客id")
    private String visitorId;    /**
     * 访客id
     */
    @ApiModelProperty(value = "访客第三方编码")
    private String visitCode;
    /**
     * 访客姓名（冗余存储）
     */
    @ApiModelProperty(value = "访客姓名（冗余存储）")
    private String visitorName;
    /**
     * 被访房屋
     */
    @ApiModelProperty(value = "被访房屋")
    private String visitHouseId;
    /**
     * 被访人
     */
    @ApiModelProperty(value = "被访人")
    private String visitPersonId;
    /**
     * 随行人数
     */
    @ApiModelProperty(value = "随行人数")
    private Integer personCount;
    /**
     * 车牌号
     */
    @ApiModelProperty(value = "车牌号")
    private String plateNumber;
    /**
     * 访客类型 1 亲情人员 2 快递人员 3 外卖人员 4 住户服务
     */
    @ApiModelProperty(value = "访客类型 1 亲情人员 2 快递人员 3 外卖人员 4 住户服务")
    private String visitorType;
    /**
     * 通行时间
     */
    @ApiModelProperty(value = "通行开始时间")
    private LocalDateTime passBeginTime;
    /**
     * 通行时间
     */
    @ApiModelProperty(value = "通行结束时间")
    private LocalDateTime passEndTime;
    /**
     * 通行时间
     */
    @ApiModelProperty(value = "通行时间段")
    private String frequency;
    /**
     * 登记类型 1 物业登记 2 住户申请 3 自住申请
     */
    @ApiModelProperty(value = "登记类型 1 物业登记 2 住户申请 3 自住申请")
    private String registerType;
    /**
     * 访问事由
     */
    @ApiModelProperty(value = "访问事由")
    private String reason;
    /**
     * 拒绝原因
     */
    @ApiModelProperty(value = "拒绝原因")
    private String rejectReason;
    /**
     * 到访时间
     */
    @ApiModelProperty(value = "到访时间")
    private LocalDateTime visitTime;
    /**
     * 离开时间
     */
    @ApiModelProperty(value = "离开时间")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private LocalDateTime leaveTime;
    /**
     * 是否签离 1 是 0 否
     */
    @ApiModelProperty(value = "是否签离 1 是 0 否")
    private String isLeave;
    /**
     * 是否人脸通行 1 是 0 否
     */
    @ApiModelProperty(value = "是否人脸通行 1 是 0 否")
    private String isFace;
    /**
     * 审核状态
     */
    @ApiModelProperty(value = "审核状态 1 待审核 2 已通过 9 未通过 字典wechat_audit_status")
    private String auditStatus;
    /**
     * 来源
     */
    @ApiModelProperty(value = "来源 1.web 2.小程序  3.app  ")
    private String origin;

    /**
     * 拓展来源来源
     */
    @ApiModelProperty(value = "来源 0:未知 1：物业 2：业主 3：访客")
    private String originEx;
}
