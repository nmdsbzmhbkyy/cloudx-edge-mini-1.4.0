

package com.aurine.cloudx.open.origin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 访客列表VO
 *
 * @author 王良俊
 * @date 2020-06-04 11:51:11
 */
@Data
@ApiModel(value = "访客列表")
public class ProjectVisitorRecordVo {
    private static final long serialVersionUID = 1L;


    /**
     * 来访id，uuid，标识一次访客的来访事件
     */
    @ApiModelProperty(value = "来访id，uuid，标识一次访客的来访事件")
    private String visitId;
    /**
     * 访客id
     */
    @ApiModelProperty(value = "访客id")
    private String visitorId;
    /**
     * 被访房屋
     */
    @ApiModelProperty(value = "被访房屋")
    private String visitHouseId;
    /**
     * 楼栋名
     */
    @ApiModelProperty(value = "楼栋名")
    private String buildingName;
    /**
     * 楼栋名
     */
    @ApiModelProperty(value = "楼栋id")
    private String buildingId;
    /**
     * 单元名
     */
    @ApiModelProperty(value = "单元名")
    private String unitName;
    /**
     * 房屋名
     */
    @ApiModelProperty(value = "房屋名")
    private String houseName;
    /**
     * 被访人
     */
    @ApiModelProperty(value = "被访人")
    private String visitPersonId;
    /**
     * 访客名
     */
    @ApiModelProperty(value = "访客名")
    private String personName;
    /**
     * 记录表的访客名
     */
    @ApiModelProperty(value = "访客名")
    private String visitorName;
    /**
     * 被访人
     */
    @ApiModelProperty(value = "被访人名")
    private String beVisitorName;
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
    @ApiModelProperty(value = "访客类型 1 住户亲友 2 快递人员 3 外卖人员 4 家政人员 5 装修人员 6 其他人员 字典 visitor_type")
    private String visitorType;
    /**
     * 通行时间
     */
    @ApiModelProperty(value = "通行时间段")
    private String frequency;
    /**
     * 访问时间：始
     */
    @ApiModelProperty(value = "访问时间：始")
    private String startTime;
    /**
     * 访问时间：终
     */
    @ApiModelProperty(value = "访问时间：终")
    private String endTime;
    /**
     * 访问时间：始
     */
    @ApiModelProperty(value = "访问时段：始")
    private String cycleStartTime;
    /**
     * 访问时间：终
     */
    @ApiModelProperty(value = "访问时段：终")
    private String cycleEndTime;
    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String mobileNo;
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
     * 到访时间
     */
    @ApiModelProperty(value = "到访时间")
    private LocalDateTime visitTime;
    /**
     * 离开时间
     */
    @ApiModelProperty(value = "离开时间")
    private LocalDateTime leaveTime;
    /**
     * 是否签离 1 是 0 否 (也就是页面上的状态)
     */
    @ApiModelProperty(value = "是否签离 1 是 0 否")
    private String isLeave;
    /**
     * 审核状态
     */
    @ApiModelProperty(value = "审核状态 0: 住户-审核中 1: 物业-审核中 2: 已通过 9: 未通过 字典wechat_audit_status")
    private String auditStatus;
    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id",hidden = true)
    private String projectId;

    /**
     * 项目名称
     */
    @ApiModelProperty(value = "项目名称",hidden = true)
    private String projectName;

    /**
     * 省
     */
    @ApiModelProperty(value = "省",hidden = true)
    private String provinceCode;

    /**
     * 市
     */
    @ApiModelProperty(value = "市",hidden = true)
    private String cityCode;

    /**
     * 县
     */
    @ApiModelProperty(value = "县",hidden = true)
    private String countyCode;
    /**
     * 街道
     */
    @ApiModelProperty(value = "街道",hidden = true)
    private String streetCode;
    /**
     * 详细地址
     */
    @ApiModelProperty(value = "详细地址",hidden = true)
    private String address;

    /**
     * 拒绝理由
     */
    @ApiModelProperty(value = "拒绝理由")
    private String rejectReason;

    /**
     * 是否上传人脸
     */
    @ApiModelProperty(value = "是否上传人脸 1 是 0 否")
    private String isFace;

    /**
     * 设备名
     */
    @ApiModelProperty(value = "设备名")
    private String deviceName;
}
