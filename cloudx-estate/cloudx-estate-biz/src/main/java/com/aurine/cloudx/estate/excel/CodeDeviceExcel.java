package com.aurine.cloudx.estate.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.aurine.cloudx.estate.excel.converter.PassConverter;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * (CodeDeviceExcel)编码设备导入模板
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/8/17 16:00
 */
@Data
public class CodeDeviceExcel {
    /**
     * 所属区域
     */
    @ExcelProperty(value = "所属区域*")
    private String deviceRegionName;

    /**
     * 设备编码，可存储第三方编码
     */
    @ExcelProperty(value = "设备编号")
    private String deviceCode;
    /**
     * 设备名称
     */
    @ExcelProperty(value = "设备名称*")
    @NotBlank(message = "设备名称不能为空")
    private String deviceName;
    /**
     * 设备别名
     */
    @ExcelProperty(value = "设备描述")
    private String deviceAlias;

    /**
     * 设备能力
     */
    @ExcelProperty(value = "设备能力")
    private String deviceFeature;

    /**
     * 设备SN
     */
    @ExcelProperty(value = "设备SN*")
    @NotBlank(message = "设备SN不能为空")
    private String sn;
    /**
     * 设备MAC
     */
    @ExcelProperty(value = "MAC")
    private String mac;
    /**
     * ipv4地址
     */
    @ExcelProperty(value = "IPv4地址")
    private String ipv4;
    /**
     * ipv6地址
     */
    @ExcelProperty(value = "IPv6地址")
    private String ipv6;

    @ExcelProperty(value = "安装位置代码")
    private String locateCode;

    @ExcelProperty(value = "安装位置")
    private String location;
    /**
     * 设备厂商
     */
    @ExcelProperty(value = "厂商名称")
    private String company;
    /**
     * 设备编码，可存储第三方编码
     */
    @ExcelProperty(value = "第三方编号")
    private String thirdpartyCode;
    /**
     * 厂商账号
     */
    @ExcelProperty(value = "厂商账号")
    private String companyAccount;
    /**
     * 厂商密码
     */
    @ExcelProperty(value = "厂商密码")
    private String companyPasswd;
    /**
     * 网络端口号
     */
    @ExcelProperty(value = "网络端口号")
    private Integer port;
    /**
     * 网络端口号*
     */
    @ExcelProperty(value = "网络端口号*")
    private Integer importPort;
    /**
     * 品牌名称
     */
    @ExcelProperty(value = "品牌型号")
    private String brand;

    /**
     * 品牌型号*
     */
    @ExcelProperty(value = "品牌型号*")
    private String importBrand;
    /**
     * 设备硬件版本
     */
    @ExcelProperty(value = "设备固件版本")
    private String hardVersion;
    /**
     * 设备软件版本
     */
    @ExcelProperty(value = "设备版本")
    private String softVersion;
}
