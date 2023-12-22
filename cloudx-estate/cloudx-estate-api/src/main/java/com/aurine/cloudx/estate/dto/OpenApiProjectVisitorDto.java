package com.aurine.cloudx.estate.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: wrm
 * @Date: 2022/05/18 10:55
 * @Package: com.aurine.openv2.controller
 * @Version: 1.0
 * @Remarks: po拓展对象返回了更多参数，dto和vo按需添加返回参数
 **/
@Data
@ApiModel(value = "访客信息dto")
public class OpenApiProjectVisitorDto {
	/**
	 * 访客记录id
	 */
	@ApiModelProperty(value = "访客记录id")
	private String visitId;
	/**
	 * 访客id
	 */
	@ApiModelProperty(value = "访客id")
	private String visitorId;

	/**
	 * 被访人Id
	 */
	@ApiModelProperty(value = "被访人Id")
	private String beVisitorPersonId;

	/**
	 * 性别
	 */
	@ApiModelProperty(value = "性别")
	private String gender;
	/**
	 * 证件类型
	 */
	@ApiModelProperty(value = "证件类型")
	private String credentialType;
	/**
	 * 证件号码
	 */
	@ApiModelProperty(value = "证件号码")
	private String credentialNo;
	/**
	 * 手机号
	 */
	@ApiModelProperty(value = "手机号")
	private String mobileNo;
	/**
	 * 照片
	 */
	@ApiModelProperty(value = "照片")
	private String picUrl;
	/**
	 * 证件照正面
	 */
	@ApiModelProperty(value = "证件照正面")
	private String credentialPicFront;
	/**
	 * 证件照背面
	 */
	@ApiModelProperty(value = "证件照背面")
	private String credentialPicBack;
//	/**
//	 * 用户id
//	 */
//	@ApiModelProperty("用户Id")
//	private Integer userId;

	/**
	 * 操作人
	 */
	@ApiModelProperty(value = "操作人")
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

	//--------------额外参数----------------
	/**
	 * 访客类型 1 亲情人员 2 快递人员 3 外卖人员 4 住户服务
	 */
	@ApiModelProperty(value = "访客类型 1 亲情人员 2 快递人员 3 外卖人员 4 住户服务")
	private String visitorType;

	/**
	 * 访客车牌号
	 */
	@ApiModelProperty(value = "访客车牌号")
	private String plateNumber;

	/**
	 * 访客姓名
	 */
	@ApiModelProperty(value = "访客姓名")
	private String visitorName;

	/**
	 * 被访姓人
	 */
	@ApiModelProperty(value = "被访人姓名")
	private String beVisitorName;

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
	 * 项目id
	 */
	@ApiModelProperty(value = "项目id")
	private Integer projectId;
	/**
	 * 项目名称
	 */
	@ApiModelProperty(value = "项目名称")
	private String projectName;
	/**
	 * 设备名
	 */
	@ApiModelProperty(value = "设备名")
	private String deviceName;
	@ApiModelProperty(value = "单位名称")
	private String employerName;
	/**
	 * 车场名称
	 */
	@ApiModelProperty(value = "车场名称")
	private String parkName;

	/**
	 * 允许通行开始时间
	 */
	@ApiModelProperty(value = "允许通行开始时间, 格式yyyy-MM-dd HH:mm")
	private String passBeginTime;
	/**
	 * 允许通行结束时间
	 */
	@ApiModelProperty(value = "允许通行结束时间, 格式yyyy-MM-dd HH:mm")
	private String passEndTime;

	/**
	 * 是否人脸通行 1 是 0 否
	 */
	@ApiModelProperty(value = "是否人脸通行 1 是 0 否")
	private String isFace;

	/**
	 * 来访状态 1 是 0 否 (也就是页面上的状态)
	 */
	@ApiModelProperty(value = "来访状态 0 未签离 1 已签离 ")
	private String isLeave;

	/**
	 * 人脸图片地址
	 */
	@ApiModelProperty("人脸图片地址")
	private String faceUrl;

	/**
	 * 筛选时间范围
	 */
	@ApiModelProperty(value = "查询范围开始时间，格式:yyyy-MM-dd HH:mm。如：2022-01-01 18:00，不传则不进行时间范围筛选")
	private String startTime;

	/**
	 * 筛选时间范围
	 */
	@ApiModelProperty(value = "查询范围结束时间，格式:yyyy-MM-dd HH:mm。如：2022-01-01 18:00，不传则结束时间默认当前时间")
	private String endTime;

	/**
	 * 被访房屋
	 */
	@ApiModelProperty(value = "被访房屋")
	private String beVisitorHouseId;

	/**
	 * 有效周期
	 */
	@ApiModelProperty(value = "有效周期,cron表达式")
	private String period;

}
