package com.aurine.cloudx.open.push.handler;

import com.aurine.cloudx.open.common.core.constant.enums.OpenPushSubscribeCallbackTypeEnum;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenException;
import com.aurine.cloudx.open.common.entity.model.OpenPushModel;
import com.aurine.cloudx.open.common.entity.util.HandleResult;
import com.aurine.cloudx.open.push.constant.PushSendKeyConstant;
import com.aurine.cloudx.open.push.factory.PushSendOperateMachineFactory;
import com.aurine.cloudx.open.push.machine.PushSendOperateMachine;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;

/**
 * 操作类处理抽象类
 *
 * @author : Qiu
 * @date : 2021 12 13 15:37
 */

public abstract class AbstractPushSendOperateHandler extends PushSendCommonHandler implements PushSendHandler {

    @Override
    protected HandleResult<OpenPushModel> filter(OpenPushModel model) {
        HandleResult<OpenPushModel> result = super.filter(model);
        if (!result.getResult()) return result;

        // 操作类model校验
        if (!getServiceType().equals(model.getServiceType())) {
            String message = super.buildHandleMessage(CloudxOpenErrorEnum.ARGUMENT_INVALID.getMsg(), PushSendKeyConstant.SERVICE_TYPE);
            result.failed(message);
        } else {
            // 操作类情况下operateType是必要的
            if (StringUtils.isEmpty(model.getOperateType())) {
                String message = super.buildHandleMessage(CloudxOpenErrorEnum.MISSING_REQUIRED_PARAMETERS.getMsg(), PushSendKeyConstant.OPERATE_TYPE);
                result.failed(message);
            }
            // 操作类情况下entityId是必要的
            if (StringUtils.isEmpty(model.getEntityId())) {
                String message = super.buildHandleMessage(CloudxOpenErrorEnum.MISSING_REQUIRED_PARAMETERS.getMsg(), PushSendKeyConstant.ENTITY_ID);
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
        String operateType = model.getOperateType();

        // 业务名称（serviceName）和操作类型（operateType）不为空的情况下才能继续处理
        if (StringUtils.isNotEmpty(serviceName) && StringUtils.isNotEmpty(operateType)) {
            try {
                // 根据业务名称（serviceName）和版本（version）从工厂中找到对应service对象
                PushSendOperateMachine machine = PushSendOperateMachineFactory.getInstance().getMachine(serviceName, version);
                if (machine != null) {
                    // 根据操作类型（operateType）使用反射获取到对应方法
                    Class<? extends PushSendOperateMachine> machineClass = machine.getClass();
                    Method declaredMethod = machineClass.getDeclaredMethod(operateType, OpenPushModel.class);

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
        return OpenPushSubscribeCallbackTypeEnum.OPERATE.name;
    }
}
