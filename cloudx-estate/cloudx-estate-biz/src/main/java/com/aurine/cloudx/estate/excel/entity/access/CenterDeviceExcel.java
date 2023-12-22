package com.aurine.cloudx.estate.excel.entity.access;

import com.alibaba.excel.annotation.ExcelProperty;
import com.aurine.cloudx.estate.excel.converter.PassConverter;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Title: IndoorDevice
 * Description:中心机导入模板(已弃用等合并分支再删除该类)
 *
 * @author xull@aurine.cn
 * @version 1.0.0
 * @date 2020/6/9 19:41
 */
@Data
public class CenterDeviceExcel {


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
     * 组团
     */
    @ExcelProperty("组团")
    private String deviceEntityName;
    /**
     * 出入类型
     */
    @ExcelProperty(value = "出入类型", converter = PassConverter.class)
    private String passType;
    /**
     * 设备区域
     */
    @ExcelProperty(value = "所属区域*")
    private String deviceRegionName;
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
