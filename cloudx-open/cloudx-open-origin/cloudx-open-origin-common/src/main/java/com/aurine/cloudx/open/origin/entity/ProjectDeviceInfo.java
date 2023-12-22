

package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 设备信息表
 *
 * @author xull@aurine.cn
 * @date 2020-05-20 10:38:59
 */
@Data
@TableName("project_device_info")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "设备信息表")
public class ProjectDeviceInfo extends OpenBasePo<ProjectDeviceInfo> {
    private static final long serialVersionUID = 1L;


    /**
     * 设备id, 32位uuid
     */
    @ApiModelProperty(value = "设备id, 32位uuid")
    @TableId(type = IdType.ASSIGN_UUID)
    private String deviceId;

    /** 设备编码，可存储第三方编码
     */
    @ApiModelProperty(value = "第三方编码")
    private String thirdpartyCode;

    /**
     * 设备编码，可存储第三方编码
     */
    @ApiModelProperty(value = "设备编码")
    private String deviceCode;

    @ApiModelProperty("设备描述")
    private String deviceDesc;
    /**
     * 设备名称
     */
    @ApiModelProperty(value = "设备名称")
//    @NotBlank(message = "设备名称不能为空")
    private String deviceName;
    /**
     * 设备类型
     */
    @ApiModelProperty(value = "设备类型")
//    @NotBlank(message = "设备类型不能为空")
    private String deviceType;
    /**
     * 设备别名
     */
    @ApiModelProperty(value = "设备别名")
    private String deviceAlias;
    /**
     * 1 在线 2 离线 3 故障 4 未激活 9 未知
     */
    @ApiModelProperty(value = "1 在线 2 离线 3 故障 4 未激活 9 未知")
    @TableField("dStatus")
    private String status;
    /**
     * 是否启用 1 启用 0 禁用
     */
    @ApiModelProperty(value = "是否启用 1 启用 0 禁用")
    @TableField("isActive")
    private String active;
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
     * 对接方式
     */
    @ApiModelProperty(value = "第三方对接接口ID")
    private String thirdpartNo;
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
     * 组团
     */
    @ApiModelProperty("组团")
    private String deviceEntityId;
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
     * 设备资源类型 字典：device_res_type
     */
    @ApiModelProperty(value = "设备资源类型")
    private String resType;
    /**
     * 设备能力集
     */
    @ApiModelProperty(value = "设备能力集")
    private String deviceCapabilities ;
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
    @TableField("dPort")
    private Integer port;
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
    @TableField("dAddress")
    private String address;

    @ApiModelProperty(value = "设备安装位置代码")
    private String locateCode;

    @ApiModelProperty(value = "设备安装位置")
    private String location;
    /**
     * 是否启用云台控制 1 是 0 否
     */
    @ApiModelProperty(value = "是否启用云台控制 1 是 0 否")
    @TableField("isCloudCtl")
    private String cloudCtl;
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
    @TableField("dCompany")
    private String company;
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
     * 品牌名称
     */
    @ApiModelProperty("品牌名称")
    private String brand;
    /**
     * 设备硬件版本
     */@ApiModelProperty(value = "设备硬件版本")
    private String hardVersion;
    /**
     * 设备软件版本
     */
    @ApiModelProperty(value = "设备软件版本")
    private String softVersion;
    /**
     * 设备信息来源
     */
    @ApiModelProperty(value = "设备信息来源")
    private String originSystem;
    /**
     * 经度
     */
    @ApiModelProperty(value = "经度")
    private BigDecimal lon;
    /**
     * 纬度
     */
    @ApiModelProperty(value = "纬度")
    private BigDecimal lat;
    /**
     * 高度
     */
    @ApiModelProperty(value = "高度")
    private BigDecimal alt;
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
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    @TableField(fill = FieldFill.INSERT)
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
     * 是否支持蓝牙
     */
    @ApiModelProperty(value = "是否支持蓝牙")
    @TableField(value = "isBlueTeeth")
    private Boolean blueTeeth;
    /**
     * 蓝牙MAC
     */
    @ApiModelProperty(value = "蓝牙MAC")
    private String blueTeethMac;

    /**
     * 产品ID
     */
    @ApiModelProperty(value = "产品ID")
    private String productId;

    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Integer projectId;
    /**
     * 蓝牙密钥
     */
    @ApiModelProperty(value = "蓝牙密钥")
    private String blueTeethSecret;

    /**
     * 接入方式  1 自动 2 手动 9 未定义
     */
    @ApiModelProperty(value = "接入方式  1 自动 2 手动 9 未定义")
    private String accessMethod;

}
