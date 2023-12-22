package com.aurine.cloudx.estate.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: wrm
 * @Date: 2022/05/25 8:51
 * @Package: com.aurine.openv2.dto
 * @Version: 1.0
 * @Remarks:
 **/
@Data
@ApiModel(value = "通行权限dto")
public class OpenApiProjectPersonDeviceDto {
	private static final long serialVersionUID = 1L;

	/**
	 * 设备id
	 */
	@ApiModelProperty(value = "设备id")
	private String deviceId;
	/**
	 * uid,主要用于回调识别
	 */
	@ApiModelProperty(value = "uid")
	private String uid;
	/**
	 * 认证介质 1 指纹 2 人脸 3 卡
	 */
	@ApiModelProperty(value = "认证介质 1 指纹 2 人脸 3 卡")
	private String certMedia;
	/**
	 * 认证介质id
	 */
	@ApiModelProperty(value = "认证介质id")
	private String certMediaId;
	/**
	 * 下载状态 0 未下载 1 已下载 2 下载失败
	 */
	@ApiModelProperty(value = "下载状态 0 未下载 1 已下载 2 下载失败")
	private String dlStatus;
	/**
	 * 接口请求状态（适用于异步接口） 1 请求中 2 请求失败 3 请求成功
	 */
	@ApiModelProperty(value = "接口请求状态（适用于异步接口） 1 请求中 2 请求失败 3 请求成功")
	private String reqStatus;
	/**
	 * 接口返回信息
	 */
	@ApiModelProperty(value = "接口返回信息")
	private String errMsg;

	/**
	 * 凭证具体信息，如人脸地址url、卡号等
	 */
	@ApiModelProperty(value = "凭证具体信息，如人脸地址url、卡号等")
	private String certMediaInfo;

	/**
	 * 凭证编码，第三方编码
	 */
	@ApiModelProperty(value = "凭证编码，第三方编码")
	private String certMediaCode;

	/**
	 * 人员类型  1 住户 2 员工 3 访客
	 */
	@ApiModelProperty(value = "人员类型  1 住户 2 员工 3 访客")
	private String personType;

	/**
	 * 人员id
	 */
	@ApiModelProperty(value = "人员id")
	private String personId;

	/**
	 * 姓名
	 */
	@ApiModelProperty(value = "姓名")
	private String personName;

	/**
	 * 电话
	 */
	@ApiModelProperty(value = "电话")
	private String mobile;

	/**
	 * 操作时间
	 */
	@ApiModelProperty(value = "操作时间")
	private LocalDateTime createTime;
	/**
	 * 更新时间
	 */
	@ApiModelProperty(value = "更新时间")
	private LocalDateTime updateTime;

	//-----------额外参数----------------
	/**
	 * 项目ID
	 */
	@ApiModelProperty(value = "项目ID")
	private Integer projectId;

	/**
	 * Id/手机号/姓名列表
	 */
	@ApiModelProperty(value = "Id列表")
	private List<String> personIdList;
	/**
	 * 设备id数组
	 */
	@ApiModelProperty(value = "设备id数组")
	private String[] deviceIdArray;

	/**
	 * 通行方案id，为空则为自选权限
	 */
	@ApiModelProperty(value = "通行方案id")
	private String planId;

	/**
	 *设备名称
	 */
	@ApiModelProperty("设备名称")
	private String deviceName;



}