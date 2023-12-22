package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.exception.handle;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.aurine.cloudx.estate.constant.enums.device.DeviceRegAbnormalEnum;
import com.aurine.cloudx.estate.constant.enums.device.DeviceRegParamEnum;
import com.aurine.cloudx.estate.entity.ProjectDeviceAbnormal;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.service.ProjectDeviceInfoService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.exception.entity.DeviceAbnormalHandleInfo;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.exception.factory.AbnormalHandleFactory;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.exception.handle.base.AbstractExceptionHandler;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * <p>异常处理责任链</p>
 *
 * @author : 王良俊
 * @date : 2021-09-26 08:40:28
 */
@Slf4j
@Component
public class AbnormalHandleChain {

    /**
     * <p>对参数进行处理</p>
     *
     * @param info 异常判断所需要的信息（设备参数如果不添加则不会进入任何判断）
     * @return AbnormalResult
     * @author: 王良俊
     */
    public void handle(DeviceAbnormalHandleInfo info, ProjectDeviceAbnormal abnormal) {
        log.info("进入异常处理 handleInfo:{} deviceAbnormal:{}", JSON.toJSONString(info), JSON.toJSONString(abnormal));
        abnormal.setAbnormalCode("");
        if (StrUtil.isNotEmpty(info.getFailCode()) && !"null".equals(info.getFailCode())) {
            DeviceRegAbnormalEnum enumByThirdCode = DeviceRegAbnormalEnum.getEnumByThirdCode(info.getFailCode());
            AbstractExceptionHandler.adjustAbnormal(abnormal, enumByThirdCode, StrUtil.isNotEmpty(info.getFailCode()));
            log.info("存在异常code：{}", abnormal);
        }
        Set<DeviceRegParamEnum> paramKeySet = info.getParamKeySet();
        if (CollUtil.isNotEmpty(paramKeySet)) {
            List<AbstractExceptionHandler> chain = AbnormalHandleFactory.getHandlerList(paramKeySet);
            if (CollUtil.isNotEmpty(chain)) {
                chain.forEach(handler -> {
                    handler.handle(info, abnormal);
                });
            }
        }

    }

}
