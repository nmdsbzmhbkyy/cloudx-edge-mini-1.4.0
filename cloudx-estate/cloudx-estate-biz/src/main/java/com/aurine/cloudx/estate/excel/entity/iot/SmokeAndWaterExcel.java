package com.aurine.cloudx.estate.excel.entity.iot;

import com.aurine.cloudx.estate.excel.invoke.DevicePortCustomizeValid;
import com.aurine.cloudx.estate.excel.invoke.valid.annotation.ExcelValid;
import com.aurine.cloudx.estate.excel.invoke.valid.constants.ExcelRegexConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>烟感和水表的Excel实体对象</p>
 *
 * @author : 王良俊
 * @date : 2021-09-02 11:03:37
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmokeAndWaterExcel {

    /**
     * 所属区域 必填
     */
    @ExcelValid(fieldName = "设备区域", required = true)
    private String deviceRegionName;

    /**
     * 楼栋
     */
    @ExcelValid(fieldName = "楼栋")
    private String building;

    /**
     * 单元
     */
    @ExcelValid(fieldName = "单元")
    private String unit;

    /**
     * 房屋
     */
    @ExcelValid(fieldName = "房屋")
    private String house;

    /**
     * 设备SN 必填
     */
    @ExcelValid(fieldName = "设备SN", required = true, maxLength = 64)
    private String sn;

    /**
     * 设备名称 必填
     */
    @ExcelValid(fieldName = "设备名称", required = true, maxLength = 128)
    private String deviceName;

    /**
     * 设备别名
     */
    @ExcelValid(fieldName = "设备描述", maxLength = 128)
    private String deviceAlias;

    /**
     * 设备别名
     */
    @ExcelValid(fieldName = "设备编号", maxLength = 100)
    private String deviceCode;

    /**
     * ipv4地址
     */
    @ExcelValid(fieldName = "IP地址", maxLength = 32)
    private String ipv4;

    /**
     * ipv6地址
     */
    @ExcelValid(fieldName = "IPv6地址", maxLength = 48)
    private String ipv6;

    /**
     * 网络端口号 （公安）必填
     */
    @ExcelValid(fieldName = "网络端口号", requiredPolice = true, using = DevicePortCustomizeValid.class)
    private String port;

    /**
     * 品牌型号 必填
     */
    @ExcelValid(fieldName = "品牌型号", required = true, maxLength = 128)
    private String brand;

    /**
     * 设备MAC
     */
    @ExcelValid(fieldName = "MAC地址", maxLength = 64)
    private String mac;

    /**
     * 设备硬件版本
     */
    @ExcelValid(fieldName = "设备固件版本", maxLength = 64)
    private String hardVersion;

    /**
     * 设备软件版本
     */
    @ExcelValid(fieldName = "设备版本", maxLength = 64)
    private String softVersion;

    /**
     * 安装位置代码
     */
    @ExcelValid(fieldName = "安装位置代码", maxLength = 32)
    private String locateCode;

    /**
     * 安装位置
     */
    @ExcelValid(fieldName = "安装位置", maxLength = 64)
    private String location;

    /**
     * 设备厂商
     */
    @ExcelValid(fieldName = "厂商名称", maxLength = 128)
    private String company;

    /**
     * 厂商账号
     */
    @ExcelValid(fieldName = "厂商账号", maxLength = 64)
    private String companyAccount;

    /**
     * 厂商密码
     */
    @ExcelValid(fieldName = "厂商密码", maxLength = 64)
    private String companyPasswd;

    /**
     * 设备编码，可存储第三方编码
     */
    @ExcelValid(fieldName = "第三方编号", maxLength = 128)
    private String thirdpartyCode;

}
