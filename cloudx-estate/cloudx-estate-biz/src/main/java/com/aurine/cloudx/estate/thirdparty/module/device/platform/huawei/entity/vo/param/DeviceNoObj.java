package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.vo.param;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 设备编号对象 json名：deviceNo
 * </p>
 *
 * @ClassName: DeviceNoObj
 * @author: 王良俊 <>
 * @date: 2020年11月25日 下午03:57:18
 * @Copyright:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName("deviceNo")
public class DeviceNoObj {

    /**
     * 设备类型
     */
    @NotNull
    Integer deviceType;

    /**
     * 小区编号
     */
    @NotNull
    String areaNo;

    /**
     * 设备编号
     * 010100001
     */
    @NotNull
    String deviceNo;

    /**
     * 设备编号描述
     */
    String devnoDesc;

    /**
     * 设备编号规则
     * 见设备编号规则对象定义
     */
    JSONObject devNoRule;

}
