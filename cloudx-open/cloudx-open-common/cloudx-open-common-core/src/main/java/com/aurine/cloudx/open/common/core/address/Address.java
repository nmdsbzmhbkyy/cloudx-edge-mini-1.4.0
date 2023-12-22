package com.aurine.cloudx.open.common.core.address;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.lang.reflect.Field;

@Data
public class Address {

    /**
     * 省编码
     */
    @ApiModelProperty(value = "省编码")
    private String provinceCode;

    /**
     * 市编码
     */
    @ApiModelProperty(value = "市编码")
    private String cityCode;

    /**
     * 县(区)编码
     */
    @ApiModelProperty(value = "县(区)编码")
    private String countyCode;

    /**
     * 街道编码
     */
    @ApiModelProperty(value = "街道编码")
    private String streetCode;

    /**
     * 省名
     */
    @ApiModelProperty(value = "省名")
    private String provinceName;

    /**
     * 市名
     */
    @ApiModelProperty(value = "市名")
    private String cityName;

    /**
     * 县(区)名
     */
    @ApiModelProperty(value = "县(区)名")
    private String countyName;

    /**
     * 街道名
     */
    @ApiModelProperty(value = "街道名")
    private String streetName;

    /**
     * 是否完全转换成功
     */
    @ApiModelProperty(value = "是否完全转换成功")
    private boolean success = false;

    /**
     * 失败原因
     */
    @ApiModelProperty(value = "失败原因")
    private String failReason;

    /**
     * <p>
     * 从省-街逐条添加地址信息
     * </p>
     *
     * @param addressName 地址名：福建/福州/马尾区/快安路
     */
    public void addAddress(String addressName) {
        if (StrUtil.isNotEmpty(addressName)) {
            if (StrUtil.isEmpty(getProvinceName())) {
                setProvinceName(addressName);
            } else if (StrUtil.isEmpty(getCityName())) {
                setCityName(addressName);
            } else if (StrUtil.isEmpty(getCountyName())) {
                setCountyName(addressName);
            } else if (StrUtil.isEmpty(getStreetName())) {
                setStreetName(addressName);
            }
        }
    }

    /**
     * <p>
     * 计算当前地址对象存有的地址名有效数量
     * </p>
     *
     * @return 有效数量
     */
    public int countAddressNum() {
        int num = 0;
        Class<? extends Address> aClass = this.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.getName().contains("Name")) {
                try {
                    // 省 市 县 街 必须连续有值否则后面作废
                    if (field.get(this) != null && StrUtil.isNotEmpty(field.get(this).toString())) {
                        num++;
                    } else {
                        break;
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }
        return num;
    }

    /**
     * <p>
     * 清空当前所有地址code
     * </p>
     *
     */
    public void clearAddressCode() {

        Class<? extends Address> aClass = this.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.getName().contains("Code")) {
                try {
                    field.set(this, null);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}
