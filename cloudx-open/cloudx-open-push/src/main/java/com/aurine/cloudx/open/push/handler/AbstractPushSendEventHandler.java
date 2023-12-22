package com.aurine.cloudx.open.push.handler;

import com.aurine.cloudx.open.common.core.exception.CloudxOpenException;
import com.aurine.cloudx.open.common.entity.util.HandleResult;
import com.aurine.cloudx.open.common.entity.model.OpenPushModel;
import com.aurine.cloudx.open.common.core.constant.enums.OpenPushSubscribeCallbackTypeEnum;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.push.factory.PushSendEventMachineFactory;
import com.aurine.cloudx.open.push.constant.PushSendKeyConstant;
import com.aurine.cloudx.open.push.machine.PushSendEventMachine;
import org.apache.commons.lang3.StringUtils;

/**
 * 事件类处理抽象类
 *
 * @author : Qiu
 * @date : 2021 12 13 15:37
 */

public abstract class AbstractPushSendEventHandler extends PushSendCommonHandler implements PushSendHandler {

    @Override
    protected HandleResult<OpenPushModel> filter(OpenPushModel model) {
        HandleResult<OpenPushModel> result = super.filter(model);
        if (!result.getResult()) return result;

        // 事件类model校验
        if (!getServiceType().equals(model.getServiceType())) {
            String message = super.buildHandleMessage(CloudxOpenErrorEnum.ARGUMENT_INVALID.getMsg(), PushSendKeyConstant.SERVICE_TYPE);
            result.failed(message);
        }

        return result;
    }

    /**
     * 预处理
     *
     * @param model 推送模型
     * @return
     */
    protected HandleResult<OpenPushModel> preHandle(OpenPushModel model) {
        return preHandle(model, null);
    }

    /**
     * 预处理
     *
     * @param model   推送模型
     * @param version 版本
     * @return
     */
    protected HandleResult<OpenPushModel> preHandle(OpenPushModel model, String version) {
        HandleResult<OpenPushModel> result = filter(model);
        if (!result.getResult()) return result;

        String serviceName = model.getServiceName();

        // 业务名称（serviceName）不为空的情况下才能继续处理
        if (StringUtils.isNotEmpty(serviceName)) {
            try {
                // 根据业务名称（serviceName）和版本（version）从工厂中找到对应service对象
                PushSendEventMachine machine = PushSendEventMachineFactory.getInstance().getMachine(serviceName, version);
                if (machine != null) {
                    // 目前事件类的方法只有一个，所以不需要利用反射，直接调用即可
                    result.setData(machine.event(model));
                }
            } catch (CloudxOpenException e) {
                result.failed(e.getMessage());
            } catch (Exception e) {
                result.failed(CloudxOpenErrorEnum.NOT_FOUND_METHOD.getMsg());
            }
        }

        return result;
    }

    @Override
    public String getServiceType() {
        return OpenPushSubscribeCallbackTypeEnum.EVENT.name;
    }

}
