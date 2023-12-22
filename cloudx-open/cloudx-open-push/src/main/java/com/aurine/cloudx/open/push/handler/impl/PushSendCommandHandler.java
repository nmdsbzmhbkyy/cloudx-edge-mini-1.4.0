package com.aurine.cloudx.open.push.handler.impl;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.common.entity.model.OpenPushModel;
import com.aurine.cloudx.open.common.core.constant.enums.VersionEnum;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.push.handler.AbstractPushSendCommandHandler;
import com.aurine.cloudx.open.common.entity.util.HandleResult;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 指令类处理实现类
 *
 * @author : Qiu
 * @date : 2021 12 13 16:18
 */

@Slf4j
@Component
public class PushSendCommandHandler extends AbstractPushSendCommandHandler {

    @Override
    public R handle(OpenPushModel model) {
        HandleResult<OpenPushModel> handleResult = super.preHandle(model, getVersion());
        log.info("[PushSendCommandHandler - handle]: 处理结束的数据, handleResult={}", JSONConvertUtils.objectToString(handleResult));

        if (!handleResult.getResult()) return Result.fail(model, CloudxOpenErrorEnum.ARGUMENT_INVALID.getCode(), handleResult.getMessage());

        JSONObject callbackResult = super.callback(handleResult.getData());
        if (callbackResult == null) {
            log.info("[PushSendCommandHandler - handle]: 未找到回调地址");
            return Result.fail(model, CloudxOpenErrorEnum.NOT_FOUND_CALLBACK);
        }
        log.info("[PushSendCommandHandler - handle]: 调用回调的结果, callbackResult={}", callbackResult);

        return R.ok(handleResult.getData());
    }

    @Override
    public String getVersion() {
        return VersionEnum.V1.code;
    }
}
