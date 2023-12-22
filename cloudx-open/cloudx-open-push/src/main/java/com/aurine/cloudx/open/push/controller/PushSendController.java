package com.aurine.cloudx.open.push.controller;

import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.common.entity.model.OpenPushModel;
import com.aurine.cloudx.open.common.core.annotation.SkipCheck;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenException;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.push.handler.impl.*;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 推送发送服务
 * 注：推送相关接口都是对内接口，在接口处添加SkipCheck注解来跳过检测
 *
 * @author : Qiu
 * @date : 2021 12 10 9:30
 */

@RestController
@RequestMapping("/v1/push/send")
@Api(value = "openPushSend", tags = {"v1", "推送相关", "推送发送服务"})
@Inner
@Slf4j
public class PushSendController {

    @Resource
    private PushSendConfigHandler pushSendConfigHandler;

    @Resource
    private PushSendCascadeHandler pushSendCascadeHandler;

    @Resource
    private PushSendOperateHandler pushSendOperateHandler;

    @Resource
    private PushSendCommandHandler pushSendCommandHandler;

    @Resource
    private PushSendEventHandler pushSendEventHandler;

    @Resource
    private PushSendOtherHandler pushSendOtherHandler;


    /**
     * 发送配置类消息
     *
     * @param model 推送模型对象
     * @return
     */
    @SkipCheck
    @ApiOperation(value = "发送配置类消息", notes = "发送配置类消息")
    @SysLog("发送配置类消息")
    @PostMapping("/config")
    public R config(@Validated @RequestBody OpenPushModel model) {
        log.info("[PushSendController - config]: 发送配置类消息, model={}", JSONConvertUtils.objectToString(model));
        try {
            return pushSendConfigHandler.handle(model);
//            return PushSendHandlerFactory.getHandler(model).handle(model);
        } catch (CloudxOpenException coe) {
            log.error("[PushSendController - config]: CloudxOpenException, coe={}", coe.getMessage());
            coe.printStackTrace();
            return Result.fail(model, CloudxOpenErrorEnum.SYSTEM_ERROR.getCode(), coe.getMessage());
        } catch (Exception e) {
            log.error("[PushSendController - config]: Exception, e={}", e.getMessage());
            e.printStackTrace();
            return Result.fail(model, CloudxOpenErrorEnum.SYSTEM_ERROR);
        }
    }

    /**
     * 发送级联入云类消息
     * （申请apply、撤销revoke、同意accept、拒绝reject）
     *
     * @param model 推送模型对象
     * @return
     */
    @SkipCheck
    @ApiOperation(value = "发送级联入云类消息", notes = "发送级联入云类消息（申请apply、撤销revoke、同意accept、拒绝reject）")
    @SysLog("发送级联入云类消息")
    @PostMapping("/cascade")
    public R cascade(@Validated @RequestBody OpenPushModel model) {
        log.info("[PushSendController - cascade]: 发送级联入云类消息, model={}", JSONConvertUtils.objectToString(model));
        try {
            return pushSendCascadeHandler.handle(model);
//            return PushSendHandlerFactory.getHandler(model).handle(model);
        } catch (CloudxOpenException coe) {
            log.error("[PushSendController - cascade]: CloudxOpenException, coe={}", coe.getMessage());
            coe.printStackTrace();
            return Result.fail(model, CloudxOpenErrorEnum.SYSTEM_ERROR.getCode(), coe.getMessage());
        } catch (Exception e) {
            log.error("[PushSendController - cascade]: Exception, e={}", e.getMessage());
            e.printStackTrace();
            return Result.fail(model, CloudxOpenErrorEnum.SYSTEM_ERROR);
        }
    }

    /**
     * 发送操作类消息
     * （添加add、修改update、删除delete、同步sync）
     *
     * @param model 推送模型对象
     * @return
     */
    @SkipCheck
    @ApiOperation(value = "发送操作类消息", notes = "发送操作类消息（添加add、修改update、删除delete 同步sync）")
    @SysLog("发送操作类消息")
    @PostMapping("/operate")
    public R operate(@Validated @RequestBody OpenPushModel model) {
        log.info("[PushSendController - operate]: 发送操作类消息, model={}", JSONConvertUtils.objectToString(model));
        try {
            return pushSendOperateHandler.handle(model);
//            return PushSendHandlerFactory.getHandler(model).handle(model);
        } catch (CloudxOpenException coe) {
            log.error("[PushSendController - operate]: CloudxOpenException, coe={}", coe.getMessage());
            coe.printStackTrace();
            return Result.fail(model, CloudxOpenErrorEnum.SYSTEM_ERROR.getCode(), coe.getMessage());
        } catch (Exception e) {
            log.error("[PushSendController - operate]: Exception, e={}", e.getMessage());
            e.printStackTrace();
            return Result.fail(model, CloudxOpenErrorEnum.SYSTEM_ERROR);
        }
    }

    /**
     * 发送指令类消息
     * （关闭close、打开open、改变change、清空empty）
     *
     * @param model 推送模型对象
     * @return
     */
    @SkipCheck
    @ApiOperation(value = "发送指令类消息", notes = "发送指令类消息（关闭close、打开open、改变change 清空empty）")
    @SysLog("发送指令类消息")
    @PostMapping("/command")
    public R command(@Validated @RequestBody OpenPushModel model) {
        log.info("[PushSendController - command]: 发送指令类消息, model={}", JSONConvertUtils.objectToString(model));
        try {
            return pushSendCommandHandler.handle(model);
//            return PushSendHandlerFactory.getHandler(model).handle(model);
        } catch (CloudxOpenException coe) {
            log.error("[PushSendController - command]: CloudxOpenException, coe={}", coe.getMessage());
            coe.printStackTrace();
            return Result.fail(model, CloudxOpenErrorEnum.SYSTEM_ERROR.getCode(), coe.getMessage());
        } catch (Exception e) {
            log.error("[PushSendController - command]: Exception, e={}", e.getMessage());
            e.printStackTrace();
            return Result.fail(model, CloudxOpenErrorEnum.SYSTEM_ERROR);
        }
    }

    /**
     * 发送事件类消息
     * （人行、车行、告警）
     *
     * @param model 推送模型对象
     * @return
     */
    @SkipCheck
    @ApiOperation(value = "发送事件类消息", notes = "发送事件类消息（人行、车行、告警）")
    @SysLog("发送事件类消息")
    @PostMapping("/event")
    public R event(@Validated @RequestBody OpenPushModel model) {
        log.info("[PushSendController - event]: 发送事件类消息, model={}", JSONConvertUtils.objectToString(model));
        try {
            return pushSendEventHandler.handle(model);
//            return PushSendHandlerFactory.getHandler(model).handle(model);
        } catch (CloudxOpenException coe) {
            log.error("[PushSendController - event]: CloudxOpenException, coe={}", coe.getMessage());
            coe.printStackTrace();
            return Result.fail(model, CloudxOpenErrorEnum.SYSTEM_ERROR.getCode(), coe.getMessage());
        } catch (Exception e) {
            log.error("[PushSendController - event]: Exception, e={}", e.getMessage());
            e.printStackTrace();
            return Result.fail(model, CloudxOpenErrorEnum.SYSTEM_ERROR);
        }
    }

    /**
     * 发送其他消息
     *
     * @param model 推送模型对象
     * @return
     */
    @SkipCheck
    @ApiOperation(value = "发送其他消息", notes = "发送其他消息")
    @SysLog("发送其他消息")
    @PostMapping("/other")
    public R other(@Validated @RequestBody OpenPushModel model) {
        log.info("[PushSendController - other]: 发送其他消息, model={}", JSONConvertUtils.objectToString(model));
        try {
            return pushSendOtherHandler.handle(model);
//            return PushSendHandlerFactory.getHandler(model).handle(model);
        } catch (CloudxOpenException coe) {
            log.error("[PushSendController - other]: CloudxOpenException, coe={}", coe.getMessage());
            coe.printStackTrace();
            return Result.fail(model, CloudxOpenErrorEnum.SYSTEM_ERROR.getCode(), coe.getMessage());
        } catch (Exception e) {
            log.error("[PushSendController - other]: Exception, e={}", e.getMessage());
            e.printStackTrace();
            return Result.fail(model, CloudxOpenErrorEnum.SYSTEM_ERROR);
        }
    }
}
