package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 设备变更历史表，用于存储被替换掉的设备信息(ProjectDeviceReplaceInfo)表实体类
 *
 * @author 王良俊
 * @since 2021-01-11 11:18:41
 */
@Data
@TableName("project_device_replace_info")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "设备变更历史表，用于存储被替换掉的设备信息(ProjectDeviceReplaceInfo)")
public class ProjectDeviceReplaceInfo extends OpenBasePo<ProjectDeviceReplaceInfo> {

    private static final long serialVersionUID = -31766012738677818L;

    /**
     * 序列
     */
    @TableId
    @ApiModelProperty(value = "序列")
    private Integer seq;


    /**
     * 设备id, 原设备id
     */
    @ApiModelProperty(value = "设备id, 原设备id")
    private String deviceId;


    /**
     * 更换时间
     */
    @ApiModelProperty(value = "更换时间")
    private LocalDateTime replaceTime;


    /**
     * 更换原因
     */
    @ApiModelProperty(value = "更换原因")
    private String replaceReason;


    /**
     * 设备编码，可存储第三方编码
     */
    @ApiModelProperty(value = "设备编码，可存储第三方编码")
    private String deviceCode;

    /**
     * 第三方编号，中台配置
     */
    @ApiModelProperty(value = "第三方编号，中台配置")
    private String thirdpartyCode;

    /**
     * 设备名称
     */
    @ApiModelProperty(value = "设备名称")
    private String deviceName;


    /**
     * 设备类型
     */
    @ApiModelProperty(value = "设备类型")
    private String deviceType;


    /**
     * 设备别名
     */
    @ApiModelProperty(value = "设备别名")
    private String deviceAlias;


    /**
     * 设备描述
     */
    @ApiModelProperty(value = "设备描述")
    private String deviceDesc;


    /**
     * 1 在线 2 离线 3 故障 4 未激活 9 未知
     */
    @ApiModelProperty(value = "1 在线 2 离线 3 故障 4 未激活 9 未知")
    private String dStatus;


    /**
     * 是否启用 1 启用 0 禁用
     */
    @ApiModelProperty(value = "是否启用 1 启用 0 禁用")
    private String isActive;


    /**
     * 楼栋id
     */
    @ApiModelProperty(value = "楼栋id")
    private String buildingId;


    /**
     * 单元id
     */
    @ApiModelProperty(value = "单元id")
    private String unitId;


    /**
     * 房屋id
     */
    @ApiModelProperty(value = "房屋id")
    private String houseId;


    /**
     * 设备功能 1 门禁卡 2 指纹 3 人脸识别 4 小区logo 5 媒体广告
     */
    @ApiModelProperty(value = "设备功能 1 门禁卡 2 指纹 3 人脸识别 4 小区logo 5 媒体广告")
    private String deviceFeature;


    /**
     * 出入类型 0 出 1 入
     */
    @ApiModelProperty(value = "出入类型 0 出 1 入")
    private String passType;


    /**
     * 设备区域
     */
    @ApiModelProperty(value = "设备区域")
    private String deviceRegionId;


    /**
     * 组团id
     */
    @ApiModelProperty(value = "组团id")
    private String deviceEntitiyId;


    /**
     * 设备SN
     */
    @ApiModelProperty(value = "设备SN")
    private String sn;


    /**
     * 设备MAC
     */
    @ApiModelProperty(value = "设备MAC")
    private String mac;


    /**
     * ipv4地址
     */
    @ApiModelProperty(value = "ipv4地址")
    private String ipv4;


    /**
     * ipv6地址
     */
    @ApiModelProperty(value = "ipv6地址")
    private String ipv6;


    /**
     * 设备端口
     */
    @ApiModelProperty(value = "设备端口")
    private Integer dPort;


    /**
     * 安装位置代码
     */
    @ApiModelProperty(value = "安装位置代码")
    private String locateCode;


    /**
     * 安装位置
     */
    @ApiModelProperty(value = "安装位置")
    private String location;


    /**
     * 设备图片
     */
    @ApiModelProperty(value = "设备图片")
    private String picUrl;


    /**
     * 二维码
     */
    @ApiModelProperty(value = "二维码")
    private String qrCode;


    /**
     * 设备详细地址
     */
    @ApiModelProperty(value = "设备详细地址")
    private String dAddress;


    /**
     * 是否启用云台控制 1 是 0 否
     */
    @ApiModelProperty(value = "是否启用云台控制 1 是 0 否")
    private String isCloudCtl;


    /**
     * 通道号
     */
    @ApiModelProperty(value = "通道号")
    private String channel;


    /**
     * 通道描述
     */
    @ApiModelProperty(value = "通道描述")
    private String channelDesc;


    /**
     * 设备厂商
     */
    @ApiModelProperty(value = "设备厂商")
    private String dCompany;


    /**
     * 品牌型号
     */
    @ApiModelProperty(value = "品牌型号")
    private String brand;


    /**
     * 厂商账号
     */
    @ApiModelProperty(value = "厂商账号")
    private String companyAccount;


    /**
     * 厂商密码
     */
    @ApiModelProperty(value = "厂商密码")
    private String companyPasswd;




    /**
     * 设备硬件版本
     */
    @ApiModelProperty(value = "设备硬件版本")
    private String hardVersion;


    /**
     * 设备软件版本
     */
    @ApiModelProperty(value = "设备软件版本")
    private String softVersion;


    /**
     * 对接方式
     */
    @ApiModelProperty(value = "对接方式")
    private String dockerType;


    /**
     * 设备信息来源
     */
    @ApiModelProperty(value = "设备信息来源")
    private String originSystem;


    /**
     * 经度
     */
    @ApiModelProperty(value = "经度")
    private Double lon;


    /**
     * 纬度
     */
    @ApiModelProperty(value = "纬度")
    private Double lat;


    /**
     * 高度
     */
    @ApiModelProperty(value = "高度")
    private Double alt;


    /**
     * 坐标
     */
    @ApiModelProperty(value = "坐标")
    private String gisArea;


    /**
     * 坐标系代码
     */
    @ApiModelProperty(value = "坐标系代码")
    private String gisType;


    /**
     * 是否支持蓝牙开门
     */
    @ApiModelProperty(value = "是否支持蓝牙开门")
    private String isBlueTeeth;


    /**
     * 蓝牙开门器mac
     */
    @ApiModelProperty(value = "蓝牙开门器mac")
    private String blueTeethMac;


    /**
     * 蓝牙开门器秘钥
     */
    @ApiModelProperty(value = "蓝牙开门器秘钥")
    private String blueTeethSecret;


    /**
     * 产品id，对接3.0中台
     */
    @ApiModelProperty(value = "产品id，对接3.0中台")
    private String productId;

}