package com.aurine.cloudx.open.push.handler;

import com.aurine.cloudx.open.common.core.exception.CloudxOpenException;
import com.aurine.cloudx.open.common.entity.util.HandleResult;
import com.aurine.cloudx.open.common.entity.model.OpenPushModel;
import com.aurine.cloudx.open.common.core.constant.enums.OpenPushSubscribeCallbackTypeEnum;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.push.factory.PushSendCommandMachineFactory;
import com.aurine.cloudx.open.push.constant.PushSendKeyConstant;
import com.aurine.cloudx.open.push.machine.PushSendCommandMachine;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;

/**
 * 指令类处理抽象类
 *
 * @author : Qiu
 * @date : 2021 12 13 15:37
 */

public abstract class AbstractPushSendCommandHandler extends PushSendCommonHandler implements PushSendHandler {

    @Override
    protected HandleResult<OpenPushModel> filter(OpenPushModel model) {
        HandleResult<OpenPushModel> result = super.filter(model);
        if (!result.getResult()) return result;

        // 指令类model校验
        if (!getServiceType().equals(model.getServiceType())) {
            String message = super.buildHandleMessage(CloudxOpenErrorEnum.ARGUMENT_INVALID.getMsg(), PushSendKeyConstant.SERVICE_TYPE);
            result.failed(message);
        } else {
            // 指令类情况下commandType是必要的
            if (StringUtils.isEmpty(model.getCommandType())) {
                String message = super.buildHandleMessage(CloudxOpenErrorEnum.MISSING_REQUIRED_PARAMETERS.getMsg(), PushSendKeyConstant.COMMAND_TYPE);
                result.failed(message);
            }
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
        String commandType = model.getCommandType();

        // 业务名称（serviceName）和指令类型（commandType）不为空的情况下才能继续处理
        if (StringUtils.isNotEmpty(serviceName) && StringUtils.isNotEmpty(commandType)) {
            try {
                // 根据业务名称（serviceName）和版本（version）从工厂中找到对应service对象
                PushSendCommandMachine machine = PushSendCommandMachineFactory.getInstance().getMachine(serviceName, version);
                if (machine != null) {
                    // 根据指令类型（commandType）使用反射获取到对应方法
                    Class<? extends PushSendCommandMachine> machineClass = machine.getClass();
                    Method declaredMethod = machineClass.getDeclaredMethod(commandType, OpenPushModel.class);

                    // 运行方法并将处理结果设置到result的data中，最后返回result
                    result.setData((OpenPushModel) declaredMethod.invoke(machine, model));
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
        return OpenPushSubscribeCallbackTypeEnum.COMMAND.name;
    }

}
