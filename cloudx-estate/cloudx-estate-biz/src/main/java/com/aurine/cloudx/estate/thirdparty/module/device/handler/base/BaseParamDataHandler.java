package com.aurine.cloudx.estate.thirdparty.module.device.handler.base;

import com.aurine.cloudx.estate.constant.enums.DeviceParamEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.util.ObjectMapperUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * <p>参数数据调整基类</p>
 *
 * @author : 王良俊
 * @date : 2022/2/7 17:14
 */
public interface BaseParamDataHandler {

    ObjectMapper objectMapper = ObjectMapperUtil.instance();

    /**
     * <p>参数调整成前端页面需要的格式（调整成返回给前端的数据）</p>
     *
     * @param source 源数据
     * @param target 转换过的
     */
    void convertHandler(ObjectNode source, ObjectNode target);

    /**
     * <p>参数恢复成从设备那边获取到的格式（将参数复原成设备规定的格式）</p>
     *
     * @param source 源数据
     * @param target 转换过的
     */
    void revertHandler(ObjectNode source, ObjectNode target);

    /**
     * <p>获取当前处理类处理的参数服务</p>
     *
     * @return 参数服务枚举
     */
    DeviceParamEnum getServiceId();

    /**
     * <p>获取适用的中台</p>
     *
     * @return 参数服务枚举
     */
    PlatformEnum getPlateForm();


}
