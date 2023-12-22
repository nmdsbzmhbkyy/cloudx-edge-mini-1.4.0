package com.aurine.cloudx.estate.excel.entity.access;

import com.aurine.cloudx.estate.excel.invoke.DevicePortCustomizeValid;
import com.aurine.cloudx.estate.excel.invoke.valid.annotation.ExcelValid;
import com.aurine.cloudx.estate.excel.invoke.valid.constants.ExcelRegexConstants;
import lombok.Data;

/**
* @Author hjj
* @Description 电梯分层控制器导入模版
* @Date  2022/8/11
* @Param
* @return
**/
@Data
public class ElevatorDeviceExcel {

    /**
     * 所属区域
     */
    @ExcelValid(fieldName = "设备区域", required = true, maxLength = 64)
    private String deviceRegionName;

    /**
     * 楼栋
     */
    @ExcelValid(fieldName = "楼栋", required = true)
    private String building;

    /**
     * 单元
     */
    @ExcelValid(fieldName = "单元", required = true)
    private String unit;

    /**
     * 设备SN
     */
    //@ExcelProperty(value = "设备SN（云端项目必填）")
    @ExcelValid(fieldName = "设备SN", maxLength = 64)
    private String sn;

    /**
     * 设备名称
     */
    //@ExcelProperty(value = "设备名称*")
    @ExcelValid(fieldName = "设备名称", required = true, maxLength = 128)
    private String deviceName;

    /**
     * 设备别名
     */
    //@ExcelProperty(value = "设备描述")
    @ExcelValid(fieldName = "设备描述", maxLength = 128)
    private String deviceAlias;

    /**
     * 设备编码，可存储第三方编码
     */
    //@ExcelProperty(value = "设备编号（边缘网关必填）")
    @ExcelValid(fieldName = "设备编号", required = true,maxLength = 100)
    private String deviceCode;

    /**
     * ipv4地址
     */
    //@ExcelProperty(value = "IP地址（边缘网关必填）")
    @ExcelValid(fieldName = "IP地址",required = true, maxLength = 32, regex = ExcelRegexConstants.IPV4, regexTip = "IP地址格式填写错误，正确格式\"192.168.10.1\"")
    private String ipv4;

    /**
     * ipv6地址
     */
    //@ExcelProperty(value = "IPv6地址")
    @ExcelValid(fieldName = "IPv6地址", maxLength = 48, regex = ExcelRegexConstants.IPV6, regexTip = "IPV6地址格式填写错误")
    private String ipv6;

    /**
     * 网络端口号
     */
    @ExcelValid(fieldName = "网络端口号", using = DevicePortCustomizeValid.class)
    private String port;

    /**
     * 品牌名称
     */
    //@ExcelProperty(value = "品牌型号（边缘网关必填）")
    @ExcelValid(fieldName = "品牌型号", required = true, maxLength = 128)
    private String brand;

    /**
     * 设备MAC
     */
    //@ExcelProperty(value = "MAC地址")
    @ExcelValid(fieldName = "MAC地址", maxLength = 64)
    private String mac;

    /**
     * 设备硬件版本
     */
    //@ExcelProperty(value = "设备固件版本")
    @ExcelValid(fieldName = "设备固件版本", maxLength = 64)
    private String hardVersion;

    /**
     * 设备软件版本
     */
    //@ExcelProperty(value = "设备版本")
    @ExcelValid(fieldName = "设备版本", maxLength = 64)
    private String softVersion;

    /**
     * 安装位置代码
     */
    //@ExcelProperty(value = "安装位置代码")
    @ExcelValid(fieldName = "安装位置代码", maxLength = 32)
    private String locateCode;

    /**
     * 安装位置
     */
    //@ExcelProperty(value = "安装位置")
    @ExcelValid(fieldName = "安装位置", maxLength = 64)
    private String location;

    /**
     * 设备厂商
     */
    //@ExcelProperty(value = "厂商名称")
    @ExcelValid(fieldName = "厂商名称", maxLength = 128)
    private String company;

    /**
     * 厂商账号
     */
    //@ExcelProperty(value = "厂商账号")
    @ExcelValid(fieldName = "厂商账号", maxLength = 64)
    private String companyAccount;

    /**
     * 厂商密码
     */
    //@ExcelProperty(value = "厂商密码")
    @ExcelValid(fieldName = "厂商密码", maxLength = 64)
    private String companyPasswd;

    /**
     * 设备编码，可存储第三方编码
     */
    //@ExcelProperty(value = "第三方编号")
    @ExcelValid(fieldName = "第三方编号", maxLength = 128)
    private String thirdpartyCode;

}
