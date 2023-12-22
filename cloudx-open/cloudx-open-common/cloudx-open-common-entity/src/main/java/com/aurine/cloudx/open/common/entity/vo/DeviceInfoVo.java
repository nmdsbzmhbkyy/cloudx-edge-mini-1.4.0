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

/**
 * 设备信息Vo
 *
 * @author : Qiu
 * @date : 2021 12 16 17:16
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "设备信息Vo")
public class DeviceInfoVo extends OpenBaseVo {

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
     * uuid，设备id
     */
    @JsonProperty("deviceId")
    @JSONField(name = "deviceId")
    @ApiModelProperty(value = "设备id")
    @Null(message = "设备id（deviceId）新增时需要为空", groups = {InsertGroup.class})
    @NotBlank(message = "设备id（deviceId）不能为空", groups = {UpdateGroup.class})
    @Size(max = 32, message = "设备id（deviceId）长度不能超过32")
    private String deviceId;

    /**
     * 设备编码，可存储第三方编码
     */
    @JsonProperty("deviceCode")
    @JSONField(name = "deviceCode")
    @ApiModelProperty(value = "设备编码，可存储第三方编码")
    @Size(max = 100, message = "设备编码（deviceCode）长度不能超过100")
    private String deviceCode;

    /**
     * 第三方编号
     */
    @JsonProperty("thirdpartyCode")
    @JSONField(name = "thirdpartyCode")
    @ApiModelProperty(value = "第三方编号")
    @Size(max = 128, message = "第三方编号（thirdpartyCode）长度不能超过128")
    private String thirdpartyCode;

    /**
     * 设备描述
     */
    @JsonProperty("deviceDesc")
    @JSONField(name = "deviceDesc")
    @ApiModelProperty("设备描述")
    @Size(max = 128, message = "设备描述（deviceDesc）长度不能超过128")
    private String deviceDesc;

    /**
     * 设备名称
     */
    @JsonProperty("deviceName")
    @JSONField(name = "deviceName")
    @ApiModelProperty(value = "设备名称")
    @NotBlank(message = "设备名称（deviceName）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "设备名称（deviceName）长度不能小于1")
    @Size(max = 128, message = "设备名称（deviceName）长度不能超过128")
    private String deviceName;

    /**
     * 设备类型
     */
    @JsonProperty("deviceType")
    @JSONField(name = "deviceType")
    @ApiModelProperty(value = "设备类型")
    @NotBlank(message = "设备类型（deviceType）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "设备类型（deviceType）长度不能小于1")
    @Size(max = 32, message = "设备类型（deviceType）长度不能超过32")
    private String deviceType;

    /**
     * 设备别名
     */
    @JsonProperty("deviceAlias")
    @JSONField(name = "deviceAlias")
    @ApiModelProperty(value = "设备别名")
    @Size(max = 128, message = "设备别名（deviceAlias）长度不能超过128")
    private String deviceAlias;

    /**
     * 状态，1 在线 2 离线 3 故障 4 未激活 9 未知
     */
    @JsonProperty("status")
    @JSONField(name = "status")
    @ApiModelProperty(value = "状态，1 在线 2 离线 3 故障 4 未激活 9 未知")
    @Size(max = 1, message = "状态（status）长度不能超过1")
    private String status;

    /**
     * 是否启用 1 启用 0 禁用
     */
    @JsonProperty("active")
    @JSONField(name = "active")
    @ApiModelProperty(value = "是否启用 1 启用 0 禁用")
    @Size(max = 1, message = "是否启用（active）长度不能超过1")
    private String active;

    /**
     * 楼栋id
     */
    @JsonProperty("buildingId")
    @JSONField(name = "buildingId")
    @ApiModelProperty(value = "楼栋id")
    @Size(max = 32, message = "楼栋id（buildingId）长度不能超过32")
    private String buildingId;

    /**
     * 单元id
     */
    @JsonProperty("unitId")
    @JSONField(name = "unitId")
    @ApiModelProperty(value = "单元id")
    @Size(max = 32, message = "单元id（unitId）长度不能超过32")
    private String unitId;

    /**
     * 房屋id
     */
    @JsonProperty("houseId")
    @JSONField(name = "houseId")
    @ApiModelProperty(value = "房屋id")
    @Size(max = 32, message = "房屋id（houseId）长度不能超过32")
    private String houseId;

    /**
     * 对接方式
     */
    @JsonProperty("thirdpartNo")
    @JSONField(name = "thirdpartNo")
    @ApiModelProperty(value = "第三方对接接口id")
    @Size(max = 128, message = "第三方对接接口id（thirdpartNo）长度不能超过128")
    private String thirdpartNo;

    /**
     * 设备功能 1 门禁卡 2 指纹 3 人脸识别 4 小区logo 5 媒体广告
     */
    @JsonProperty("deviceFeature")
    @JSONField(name = "deviceFeature")
    @ApiModelProperty(value = "设备功能 1 门禁卡 2 指纹 3 人脸识别 4 小区logo 5 媒体广告")
    @Size(max = 32, message = "设备功能（deviceFeature）长度不能超过32")
    private String deviceFeature;

    /**
     * 出入类型 0 出 1 入
     */
    @JsonProperty("passType")
    @JSONField(name = "passType")
    @ApiModelProperty(value = "出入类型 0 出 1 入")
    @Size(max = 5, message = "出入类型（passType）长度不能超过5")
    private String passType;

    /**
     * 设备区域id
     */
    @JsonProperty("deviceRegionId")
    @JSONField(name = "deviceRegionId")
    @ApiModelProperty(value = "设备区域id")
    @Size(max = 32, message = "设备区域id（deviceRegionId）长度不能超过32")
    private String deviceRegionId;

    /**
     * 组团id
     */
    @JsonProperty("deviceEntityId")
    @JSONField(name = "deviceEntityId")
    @ApiModelProperty("组团id")
    @Size(max = 32, message = "组团id（deviceEntityId）长度不能超过32")
    private String deviceEntityId;

    /**
     * 设备SN
     */
    @JsonProperty("sn")
    @JSONField(name = "sn")
    @ApiModelProperty(value = "设备SN")
    @Size(max = 64, message = "设备SN（sn）长度不能超过64")
    private String sn;

    /**
     * 设备MAC
     */
    @JsonProperty("mac")
    @JSONField(name = "mac")
    @ApiModelProperty(value = "设备MAC")
    @Size(max = 64, message = "设备MAC（mac）长度不能超过64")
    private String mac;

    /**
     * 设备资源类型 字典：device_res_type
     */
    @JsonProperty("resType")
    @JSONField(name = "resType")
    @ApiModelProperty(value = "设备资源类型")
    @Size(max = 64, message = "设备资源类型（resType）长度不能超过64")
    private String resType;

    /**
     * 设备能力集
     */
    @JsonProperty("deviceCapabilities")
    @JSONField(name = "deviceCapabilities")
    @ApiModelProperty(value = "设备能力集")
    @Size(max = 256, message = "设备能力集（deviceCapabilities）长度不能超过256")
    private String deviceCapabilities;

    /**
     * ipv4地址
     */
    @JsonProperty("ipv4")
    @JSONField(name = "ipv4")
    @ApiModelProperty(value = "ipv4地址")
    @Size(max = 32, message = "ipv4地址（ipv4）长度不能超过32")
    private String ipv4;

    /**
     * ipv6地址
     */
    @JsonProperty("ipv6")
    @JSONField(name = "ipv6")
    @ApiModelProperty(value = "ipv6地址")
    @Size(max = 48, message = "ipv6地址（ipv6）长度不能超过48")
    private String ipv6;

    /**
     * 设备端口
     */
    @JsonProperty("port")
    @JSONField(name = "port")
    @ApiModelProperty(value = "设备端口")
    @Max(value = Integer.MAX_VALUE, message = "设备端口（port）数值过大")
    private Integer port;

    /**
     * 设备图片
     */
    @JsonProperty("picUrl")
    @JSONField(name = "picUrl")
    @ApiModelProperty(value = "设备图片")
    @Size(max = 256, message = "设备图片（picUrl）长度不能超过256")
    private String picUrl;

    /**
     * 二维码
     */
    @JsonProperty("qrCode")
    @JSONField(name = "qrCode")
    @ApiModelProperty(value = "二维码")
    @Size(max = 256, message = "二维码（qrCode）长度不能超过256")
    private String qrCode;

    /**
     * 设备详细地址
     */
    @JsonProperty("address")
    @JSONField(name = "address")
    @ApiModelProperty(value = "设备详细地址")
    @Size(max = 256, message = "设备详细地址（address）长度不能超过256")
    private String address;

    /**
     * 设备安装位置代码
     */
    @JsonProperty("locateCode")
    @JSONField(name = "locateCode")
    @ApiModelProperty(value = "设备安装位置代码")
    @Size(max = 32, message = "设备安装位置代码（locateCode）长度不能超过32")
    private String locateCode;

    /**
     * 设备安装位置
     */
    @JsonProperty("location")
    @JSONField(name = "location")
    @ApiModelProperty(value = "设备安装位置")
    @Size(max = 64, message = "设备安装位置（location）长度不能超过64")
    private String location;

    /**
     * 是否启用云台控制 1 是 0 否
     */
    @JsonProperty("cloudCtl")
    @JSONField(name = "cloudCtl")
    @ApiModelProperty(value = "是否启用云台控制 1 是 0 否")
    @NotBlank(message = "是否启用云台控制（cloudCtl）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "是否启用云台控制（cloudCtl）长度不能小于1")
    @Size(max = 1, message = "是否启用云台控制（cloudCtl）长度不能超过1")
    private String cloudCtl;

    /**
     * 通道号
     */
    @JsonProperty("channel")
    @JSONField(name = "channel")
    @ApiModelProperty(value = "通道号")
    @Size(max = 32, message = "通道号（channel）长度不能超过32")
    private String channel;

    /**
     * 通道描述
     */
    @JsonProperty("channelDesc")
    @JSONField(name = "channelDesc")
    @ApiModelProperty(value = "通道描述")
    @Size(max = 128, message = "通道描述（channelDesc）长度不能超过128")
    private String channelDesc;

    /**
     * 设备厂商
     */
    @JsonProperty("company")
    @JSONField(name = "company")
    @ApiModelProperty(value = "设备厂商")
    @Size(max = 128, message = "设备厂商（company）长度不能超过128")
    private String company;

    /**
     * 厂商账号
     */
    @JsonProperty("companyAccount")
    @JSONField(name = "companyAccount")
    @ApiModelProperty(value = "厂商账号")
    @Size(max = 64, message = "厂商账号（companyAccount）长度不能超过64")
    private String companyAccount;

    /**
     * 厂商密码
     */
    @JsonProperty("companyPasswd")
    @JSONField(name = "companyPasswd")
    @ApiModelProperty(value = "厂商密码")
    @Size(max = 64, message = "厂商密码（companyPasswd）长度不能超过64")
    private String companyPasswd;

    /**
     * 品牌名称
     */
    @JsonProperty("brand")
    @JSONField(name = "brand")
    @ApiModelProperty("品牌名称")
    @Size(max = 128, message = "品牌名称（brand）长度不能超过128")
    private String brand;

    /**
     * 设备硬件版本
     */
    @JsonProperty("hardVersion")
    @JSONField(name = "hardVersion")
    @ApiModelProperty(value = "设备硬件版本")
    @Size(max = 64, message = "设备硬件版本（hardVersion）长度不能超过64")
    private String hardVersion;

    /**
     * 设备软件版本
     */
    @JsonProperty("softVersion")
    @JSONField(name = "softVersion")
    @ApiModelProperty(value = "设备软件版本")
    @Size(max = 64, message = "设备软件版本（softVersion）长度不能超过64")
    private String softVersion;

    /**
     * 设备信息来源
     */
    @JsonProperty("originSystem")
    @JSONField(name = "originSystem")
    @ApiModelProperty(value = "设备信息来源")
    @Size(max = 32, message = "设备信息来源（originSystem）长度不能超过32")
    private String originSystem;

    /**
     * 经度
     */
    @JsonProperty("lon")
    @JSONField(name = "lon")
    @ApiModelProperty(value = "经度")
    @Digits(integer = 9, fraction = 6, message = "经度（lon）格式不正确，整数最多9位，小数最多6位")
    private Double lon;

    /**
     * 纬度
     */
    @JsonProperty("lat")
    @JSONField(name = "lat")
    @ApiModelProperty(value = "纬度")
    @Digits(integer = 9, fraction = 6, message = "纬度（lat）格式不正确，整数最多9位，小数最多6位")
    private Double lat;

    /**
     * 高度
     */
    @JsonProperty("alt")
    @JSONField(name = "alt")
    @ApiModelProperty(value = "高度")
    @Digits(integer = 6, fraction = 1, message = "高度（alt）格式不正确，整数最多6位，小数最多1位")
    private Double alt;

    /**
     * 坐标
     */
    @JsonProperty("gisArea")
    @JSONField(name = "gisArea")
    @ApiModelProperty(value = "坐标")
    @Size(max = 255, message = "坐标（gisArea）长度不能超过255")
    private String gisArea;

    /**
     * 坐标系代码
     */
    @JsonProperty("gisType")
    @JSONField(name = "gisType")
    @ApiModelProperty(value = "坐标系代码")
    @Size(max = 32, message = "坐标系代码（gisType）长度不能超过32")
    private String gisType;

    /**
     * 是否支持蓝牙
     */
    @JsonProperty("blueTeeth")
    @JSONField(name = "blueTeeth")
    @ApiModelProperty(value = "是否支持蓝牙")
    @Size(max = 1, message = "是否支持蓝牙（blueTeeth）长度不能超过1")
    private Boolean blueTeeth;

    /**
     * 蓝牙MAC
     */
    @JsonProperty("blueTeethMac")
    @JSONField(name = "blueTeethMac")
    @ApiModelProperty(value = "蓝牙MAC")
    @Size(max = 32, message = "蓝牙MAC（blueTeethMac）长度不能超过32")
    private String blueTeethMac;

    /**
     * 蓝牙密钥
     */
    @JsonProperty("blueTeethSecret")
    @JSONField(name = "blueTeethSecret")
    @ApiModelProperty(value = "蓝牙密钥")
    @Size(max = 32, message = "蓝牙密钥（blueTeethSecret）长度不能超过32")
    private String blueTeethSecret;

    /**
     * 产品id
     */
    @JsonProperty("productId")
    @JSONField(name = "productId")
    @ApiModelProperty(value = "产品id")
    @Size(max = 64, message = "产品id（productId）长度不能超过64")
    private String productId;

    /**
     * 接入方式  1 自动 2 手动 9 未定义
     */
    @JsonProperty("accessMethod")
    @JSONField(name = "accessMethod")
    @ApiModelProperty(value = "接入方式  1 自动 2 手动 9 未定义")
    @NotBlank(message = "接入方式（accessMethod）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "接入方式（accessMethod）长度不能小于1")
    @Size(max = 1, message = "接入方式（accessMethod）长度不能超过1")
    private String accessMethod;

    /**
     * 图片Base64（自定义，非数据库字段）
     */
    @JsonProperty("picBase64")
    @JSONField(name = "picBase64")
    @ApiModelProperty(value = "图片Base64（自定义，非数据库字段）")
    private String picBase64;
}

