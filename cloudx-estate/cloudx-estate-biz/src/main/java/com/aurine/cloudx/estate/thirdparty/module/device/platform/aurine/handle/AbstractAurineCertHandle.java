package com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.handle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <p>门禁凭证下发消费者，用于将异步门禁转为同步下发，匹配设备功能</p>
 *
 * @ClassName: EventDeviceParkingPayConsumer
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-06 14:02
 * @Copyright:
 */
@Component
@Slf4j
public abstract class AbstractAurineCertHandle {

    public abstract void readActiveQueue(String message);

}
