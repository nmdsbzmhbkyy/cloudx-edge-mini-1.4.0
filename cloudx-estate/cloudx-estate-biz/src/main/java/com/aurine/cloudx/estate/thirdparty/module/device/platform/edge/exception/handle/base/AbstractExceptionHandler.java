package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.exception.handle.base;

import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.estate.constant.enums.device.DeviceRegAbnormalEnum;
import com.aurine.cloudx.estate.constant.enums.device.DeviceRegParamEnum;
import com.aurine.cloudx.estate.entity.ProjectDeviceAbnormal;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.exception.entity.DeviceAbnormalHandleInfo;

import java.util.*;

/**
 * <p>设备异常处理链</p>
 *
 * @author : 王良俊
 * @date : 2021-09-24 16:57:17
 */
public abstract class AbstractExceptionHandler {

    /**
     * <p>具体的处理方法</p>
     *
     * @param info 处理所需要的信息对象
     * @param abnormal 设备异常记录对象
     * @author: 王良俊
     */
    public abstract void handle(DeviceAbnormalHandleInfo info, ProjectDeviceAbnormal abnormal);

    /**
     * <p>获取handle需要判断异常的目标参数</p>
     *
     * @author: 王良俊
     */
    public abstract DeviceRegParamEnum getTargetParam();

    public static void adjustAbnormal(ProjectDeviceAbnormal abnormal, DeviceRegAbnormalEnum abnormalEnum, boolean isAbnormal) {
        String abnormalCode = abnormal.getAbnormalCode();
        Set<String> abnormalCodeSet = new HashSet<>();
        if (StrUtil.isNotEmpty(abnormalCode)) {
            String[] codeArr = abnormalCode.split(",");
            abnormalCodeSet.addAll(Arrays.asList(codeArr));
        }
        if (isAbnormal) {
            abnormalCodeSet.add(abnormalEnum.systemCode);
        } else {
            abnormalCodeSet.remove(abnormalEnum.systemCode);
        }
        abnormal.setAbnormalCode(String.join(",", abnormalCodeSet));
        abnormal.setAbnormalDesc(DeviceRegAbnormalEnum.getDesc(abnormalCodeSet));
        abnormal.setAdvice(DeviceRegAbnormalEnum.getAdvice(abnormalCodeSet));
    }
}
