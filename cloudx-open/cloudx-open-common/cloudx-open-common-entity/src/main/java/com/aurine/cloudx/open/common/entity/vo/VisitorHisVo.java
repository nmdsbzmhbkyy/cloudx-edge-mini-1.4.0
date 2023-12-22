package com.aurine.cloudx.open.common.entity.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.aurine.cloudx.open.common.entity.base.OpenBaseVo;
import com.aurine.cloudx.open.common.validate.annotation.SizeCustom;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * 来访记录Vo
 *
 * @author : Qiu
 * @date : 2021 12 20 16:35
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "来访记录Vo")
public class VisitorHisVo extends OpenBaseVo {

    /**
     * 序列
     */
    @JsonProperty("seq")
    @JSONField(name = "seq")
    @ApiModelProperty(value = "序列", hidden = true)
    @Null(message = "序列（seq）需要为空")
    private Integer seq;

    /**
     * 项目id
     */
    @JsonProperty("projectId")
    @JSONField(name = "projectId")
    @ApiModelProperty(value = "项目id", required = true, position = -1)
    @NotNull(message = "项目id（projectId）不能为空", groups = {InsertGroup.class})
    @Max(value = Integer.MAX_VALUE, message = "项目id（projectId）数值过大")
    private Integer projectId;

    /**
     * 来访id，uuid，标识一次访客的来访事件
     */
    @JsonProperty("visitId")
    @JSONField(name = "visitId")
    @ApiModelProperty(value = "来访id，uuid，标识一次访客的来访事件")
    @Null(message = "来访id（visitId）新增时需要为空", groups = {InsertGroup.class})
    @NotBlank(message = "来访id（visitId）不能为空", groups = {UpdateGroup.class})
    @Size(max = 32, message = "来访id（visitId）长度不能超过32")
    private String visitId;

    /**
     * 访客id
     */
    @JsonProperty("visitorId")
    @JSONField(name = "visitorId")
    @ApiModelProperty(value = "访客id")
    @NotBlank(message = "访客id（visitorId）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "访客id（visitorId）长度不能小于1")
    @Size(max = 32, message = "访客id（visitorId）长度不能超过32")
    private String visitorId;

    /**
     * 访客第三方编码
     */
    @JsonProperty("visitCode")
    @JSONField(name = "visitCode")
    @ApiModelProperty(value = "访客第三方编码")
    @Size(max = 64, message = "访客第三方编码（visitCode）长度不能超过64")
    private String visitCode;

    /**
     * 访客姓名（冗余存储）
     */
    @JsonProperty("visitorName")
    @JSONField(name = "visitorName")
    @ApiModelProperty(value = "访客姓名（冗余存储）")
    @Size(max = 64, message = "访客姓名（visitorName）长度不能超过64")
    private String visitorName;

    /**
     * 被访房屋
     */
    @JsonProperty("visitHouseId")
    @JSONField(name = "visitHouseId")
    @ApiModelProperty(value = "被访房屋")
    @Size(max = 32, message = "被访房屋（visitHouseId）长度不能超过32")
    private String visitHouseId;

    /**
     * 被访人
     */
    @JsonProperty("visitPersonId")
    @JSONField(name = "visitPersonId")
    @ApiModelProperty(value = "被访人")
    @Size(max = 32, message = "被访人（visitPersonId）长度不能超过32")
    private String visitPersonId;

    /**
     * 随行人数
     */
    @JsonProperty("personCount")
    @JSONField(name = "personCount")
    @ApiModelProperty(value = "随行人数")
    @Max(value = Integer.MAX_VALUE, message = "随行人数（personCount）数值过大")
    private Integer personCount;

    /**
     * 车牌号
     */
    @JsonProperty("plateNumber")
    @JSONField(name = "plateNumber")
    @ApiModelProperty(value = "车牌号")
    @Size(max = 10, message = "车牌号（plateNumber）长度不能超过10")
    private String plateNumber;

    /**
     * 访客类型 1 亲情人员 2 快递人员 3 外卖人员 4 住户服务
     */
    @JsonProperty("visitorType")
    @JSONField(name = "visitorType")
    @ApiModelProperty(value = "访客类型 1 亲情人员 2 快递人员 3 外卖人员 4 住户服务")
    @Size(max = 5, message = "访客类型（visitorType）长度不能超过5")
    private String visitorType;

    /**
     * 通行时间
     */
    @JsonProperty("passBeginTime")
    @JSONField(name = "passBeginTime", format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "通行开始时间")
    private LocalDateTime passBeginTime;

    /**
     * 通行时间
     */
    @JsonProperty("passEndTime")
    @JSONField(name = "passEndTime", format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "通行结束时间")
    private LocalDateTime passEndTime;

    /**
     * 通行频率 参考java crontab格式
     */
    @JsonProperty("frequency")
    @JSONField(name = "frequency")
    @ApiModelProperty(value = "通行频率 参考java crontab格式")
    @Size(max = 128, message = "通行频率（frequency）长度不能超过128")
    private String frequency;

    /**
     * 登记类型 1 物业登记 2 住户申请 3 自住申请
     */
    @JsonProperty("registerType")
    @JSONField(name = "registerType")
    @ApiModelProperty(value = "登记类型 1 物业登记 2 住户申请 3 自住申请")
    @Size(max = 1, message = "登记类型（registerType）长度不能超过1")
    private String registerType;

    /**
     * 访问事由
     */
    @JsonProperty("reason")
    @JSONField(name = "reason")
    @ApiModelProperty(value = "访问事由")
    @Size(max = 128, message = "访问事由（reason）长度不能超过128")
    private String reason;

    /**
     * 拒绝原因
     */
    @JsonProperty("rejectReason")
    @JSONField(name = "rejectReason")
    @ApiModelProperty(value = "拒绝原因")
    @Size(max = 128, message = "拒绝原因（rejectReason）长度不能超过128")
    private String rejectReason;

    /**
     * 到访时间
     */
    @JsonProperty("visitTime")
    @JSONField(name = "visitTime", format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "到访时间")
    private LocalDateTime visitTime;

    /**
     * 离开时间
     */
    @JsonProperty("leaveTime")
    @JSONField(name = "leaveTime", format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "离开时间")
    private LocalDateTime leaveTime;

    /**
     * 是否签离 1 是 0 否
     */
    @JsonProperty("isLeave")
    @JSONField(name = "isLeave")
    @ApiModelProperty(value = "是否签离 1 是 0 否")
    @NotBlank(message = "是否签离（isLeave）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "是否签离（isLeave）长度不能小于1")
    @Size(max = 1, message = "是否签离（isLeave）长度不能超过1")
    private String isLeave;

    /**
     * 是否人脸通行 1 是 0 否
     */
    @JsonProperty("isFace")
    @JSONField(name = "isFace")
    @ApiModelProperty(value = "是否人脸通行 1 是 0 否")
    @NotBlank(message = "是否人脸通行（isFace）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "是否人脸通行（isFace）长度不能小于1")
    @Size(max = 1, message = "是否人脸通行（isFace）长度不能超过1")
    private String isFace;

    /**
     * 审核状态
     */
    @JsonProperty("auditStatus")
    @JSONField(name = "auditStatus")
    @ApiModelProperty(value = "审核状态 1 待审核 2 已通过 9 未通过 字典wechat_audit_status")
    @NotBlank(message = "审核状态（auditStatus）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "审核状态（auditStatus）长度不能小于1")
    @Size(max = 5, message = "审核状态（auditStatus）长度不能超过5")
    private String auditStatus;

    /**
     * 来源
     */
    @JsonProperty("origin")
    @JSONField(name = "origin")
    @ApiModelProperty(value = "来源 1.web 2.小程序  3.app  ")
    @NotBlank(message = "来源（origin）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "来源（origin）长度不能小于1")
    @Size(max = 1, message = "来源（origin）长度不能超过1")
    private String origin;

    /**
     * 拓展来源来源
     */
    @JsonProperty("originEx")
    @JSONField(name = "originEx")
    @ApiModelProperty(value = "来源 0:未知 1：物业 2：业主 3：访客")
    @NotBlank(message = "来源（originEx）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "来源（originEx）长度不能小于1")
    @Size(max = 1, message = "来源（originEx）长度不能超过1")
    private String originEx;
}
