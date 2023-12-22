package com.aurine.cloudx.open.push.service.impl;

import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.open.common.core.constant.enums.OpenPushSubscribeCallbackModeEnum;
import com.aurine.cloudx.open.common.core.constant.enums.OpenPushSubscribeCallbackTypeEnum;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.common.entity.entity.OpenPushSubscribeCallback;
import com.aurine.cloudx.open.common.entity.vo.OpenPushSubscribeCallbackVo;
import com.aurine.cloudx.open.push.handler.PushSendCommonHandler;
import com.aurine.cloudx.open.push.mapper.OpenPushSubscribeCallbackMapper;
import com.aurine.cloudx.open.push.service.PushSubscribeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 推送订阅回调信息
 *
 * @author : Qiu
 * @date : 2021 12 09 16:13
 */

@Service
public class PushSubscribeServiceImpl extends ServiceImpl<OpenPushSubscribeCallbackMapper, OpenPushSubscribeCallback> implements PushSubscribeService {

    @Resource
    private PushSendCommonHandler pushSendCommonHandler;


    @Override
    public R<OpenPushSubscribeCallbackVo> getById(String id) {
        OpenPushSubscribeCallback po = super.getById(id);
        if (po == null) return Result.fail(null, CloudxOpenErrorEnum.EMPTY_RESULT);

        OpenPushSubscribeCallbackVo vo = new OpenPushSubscribeCallbackVo();
        BeanUtils.copyProperties(po, vo);

        return R.ok(vo);
    }

    @Override
    public R<List<OpenPushSubscribeCallbackVo>> getList(OpenPushSubscribeCallbackVo vo) {
        OpenPushSubscribeCallback po = new OpenPushSubscribeCallback();
        BeanUtils.copyProperties(vo, po);

        return R.ok(baseMapper.getList(po));
    }

    @Override
    public R<List<OpenPushSubscribeCallbackVo>> getListByServiceTypeAndProjectUUID(String callbackType, String projectUUID) {
        OpenPushSubscribeCallbackVo vo = new OpenPushSubscribeCallbackVo();
        vo.setCallbackType(callbackType);
        vo.setProjectUUID(projectUUID);
        return this.getList(vo);
    }

    @Override
    public R<Page<OpenPushSubscribeCallbackVo>> page(Page page, OpenPushSubscribeCallbackVo vo) {
        OpenPushSubscribeCallback po = new OpenPushSubscribeCallback();
        BeanUtils.copyProperties(vo, po);

        return R.ok(baseMapper.page(page, po));
    }

    @Override
    public R<OpenPushSubscribeCallbackVo> save(OpenPushSubscribeCallbackVo vo) {
        R<Boolean> filterResult = this.saveFilter(vo);
        if (filterResult.getData() == null || !filterResult.getData())
            return Result.fail(vo, filterResult.getCode(), filterResult.getMsg());

        String appId = vo.getAppId();
        String callbackType = vo.getCallbackType();
        String callbackMode = vo.getCallbackMode();
        String projectUUID = vo.getProjectUUID();
        String callbackUrl = vo.getCallbackUrl();
        String callbackTopic = vo.getCallbackTopic();

        // 如果callbackMode是空，则设置默认值0（url回调方式）
        if (StringUtil.isBlank(callbackMode)) {
            callbackMode = OpenPushSubscribeCallbackModeEnum.URL.code;
            vo.setCallbackMode(callbackMode);
        }

        // 参数校验，根据callbackMode判断对应的参数
        if (OpenPushSubscribeCallbackModeEnum.TOPIC.code.equals(callbackMode)) {
            if (StringUtil.isBlank(callbackTopic)) {
                return Result.fail(vo, CloudxOpenErrorEnum.MISSING_REQUIRED_PARAMETERS.getCode(), "缺少必要参数callbackTopic（回调主题）");
            }
        } else if (StringUtil.isBlank(callbackUrl)) {
            return Result.fail(vo, CloudxOpenErrorEnum.MISSING_REQUIRED_PARAMETERS.getCode(), "缺少必要参数callbackTopic（回调地址）");
        }

        // 注：一个应用，只能订阅同一个项目的一种类型的回调
        OpenPushSubscribeCallback one = super.getOne(new LambdaQueryWrapper<OpenPushSubscribeCallback>()
                .eq(OpenPushSubscribeCallback::getAppId, appId)
                .eq(OpenPushSubscribeCallback::getCallbackType, callbackType)
                .eq(StringUtil.isNotBlank(projectUUID), OpenPushSubscribeCallback::getProjectUUID, projectUUID)
                .last("LIMIT 1")
        );
        if (one != null) {
//            // 如果已存在（应用ID + 回调类型 + 项目ID）回调地址，则替换数据，而不是新增。
//            vo.setCallbackId(one.getCallbackId());
//            return this.update(vo);

            // 如果已存在（应用ID + 回调类型 + 项目ID）回调地址，则通知调用方已存在，新增失败。
            if (StringUtils.isNotBlank(projectUUID)) {
                return Result.fail(vo, CloudxOpenErrorEnum.SERVICE_ERROR.getCode(), String.format("已经订阅过该项目的回调类型了, 回调类型=%s, 项目UUID=%s", callbackType, projectUUID));
            }
            return Result.fail(vo, CloudxOpenErrorEnum.SERVICE_ERROR.getCode(), String.format("已经订阅过该回调类型了, 回调类型=%s", callbackType));
        }

        OpenPushSubscribeCallback po = new OpenPushSubscribeCallback();
        BeanUtils.copyProperties(vo, po);

        boolean result = super.save(po);
        if (!result) return Result.fail(vo, CloudxOpenErrorEnum.SYSTEM_ERROR);

        OpenPushSubscribeCallbackVo resultVo = new OpenPushSubscribeCallbackVo();
        BeanUtils.copyProperties(po, resultVo);

        // 新增了回调地址之后，对应的回调地址map缓存可能需要刷新，所以在这里清空一下
        pushSendCommonHandler.removeCallbackListMapIfExistKey(resultVo.getCallbackType(), resultVo.getProjectUUID());

        return R.ok(resultVo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<List<OpenPushSubscribeCallbackVo>> saveBatch(List<OpenPushSubscribeCallbackVo> voList) {
        Map<String, OpenPushSubscribeCallbackVo> resultVoMap = new HashMap<>();

        // 因为新增逻辑重复，所以循环调用新增单个的方法
        for (OpenPushSubscribeCallbackVo vo : voList) {
            R<OpenPushSubscribeCallbackVo> result = this.save(vo);
            OpenPushSubscribeCallbackVo resultVo = result.getData();

            // 如果为空，则新增失败，回滚并返回错误
            if (resultVo == null) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return Result.fail(voList, result.getCode(), result.getMsg());
            }

            resultVoMap.put(resultVo.getCallbackId(), resultVo);
        }

        return R.ok(new ArrayList<>(resultVoMap.values()));
    }

    @Override
    public R<OpenPushSubscribeCallbackVo> update(OpenPushSubscribeCallbackVo vo) {
        OpenPushSubscribeCallback po = new OpenPushSubscribeCallback();
        BeanUtils.copyProperties(vo, po);

        boolean result = super.updateById(po);
        if (!result) return Result.fail(vo, CloudxOpenErrorEnum.EMPTY_RESULT);

        OpenPushSubscribeCallbackVo resultVo = new OpenPushSubscribeCallbackVo();
        BeanUtils.copyProperties(po, resultVo);

        // 修改了回调地址之后，对应的回调地址map缓存可能需要刷新，所以在这里清空一下
        pushSendCommonHandler.removeCallbackListMapIfExistKey(resultVo.getCallbackType(), resultVo.getProjectUUID());

        return R.ok(resultVo);
    }

    @Override
    public R<Boolean> delete(String id) {
        OpenPushSubscribeCallback po = super.getById(id);

        boolean result = super.removeById(id);
        if (!result) return Result.fail(false, CloudxOpenErrorEnum.EMPTY_RESULT);

        if (po != null) {
            // 删除了回调地址之后，对应的回调地址map缓存可能需要刷新，所以在这里清空一下
            pushSendCommonHandler.removeCallbackListMapIfExistKey(po.getCallbackType(), po.getProjectUUID());
        }

        return R.ok(true);
    }

    @Override
    public R<Boolean> deleteByAppId(String appId) {
        return this.delete(appId, null, null);
    }

    @Override
    public R<Boolean> delete(String appId, String callbackType, String projectUUID) {
        if (StringUtils.isEmpty(appId)) {
            return Result.fail(false, CloudxOpenErrorEnum.MISSING_REQUIRED_PARAMETERS.getCode(), "应用ID（appId）为空");
        }

        Boolean result = baseMapper.delete(appId, callbackType, projectUUID);
        if (!result) return Result.fail(false, CloudxOpenErrorEnum.EMPTY_RESULT);

        // 删除了回调地址之后，对应的回调地址map缓存可能需要刷新，所以在这里清空一下
        pushSendCommonHandler.removeCallbackListMapIfExistKey(callbackType, projectUUID);

        return R.ok(true);
    }

    @Override
    public R<Boolean> clearCache() {
        boolean result = pushSendCommonHandler.clearCallbackListCache();
        if (!result) return Result.failed();

        return R.ok(true);
    }

    /**
     * 添加前校验
     *
     * @param vo
     * @return
     */
    private R<Boolean> saveFilter(OpenPushSubscribeCallbackVo vo) {
        if (vo == null) return Result.fail(false, CloudxOpenErrorEnum.ARGUMENT_INVALID);

        // 这三个都是必传参数，需要校验（如果callbackType是配置类，则projectUUID可以为空）
        // 虽然在vo层部分参数已经做了valid校验，这里校验只是做了一层保险
        String appId = vo.getAppId();
        String callbackType = vo.getCallbackType();
        String projectUUID = vo.getProjectUUID();

        if (StringUtils.isEmpty(appId)) {
            return Result.fail(false, CloudxOpenErrorEnum.MISSING_REQUIRED_PARAMETERS.getCode(), String.format("%s: %s", CloudxOpenErrorEnum.MISSING_REQUIRED_PARAMETERS.getMsg(), "appId"));
        }
        if (StringUtils.isEmpty(callbackType)) {
            return Result.fail(false, CloudxOpenErrorEnum.MISSING_REQUIRED_PARAMETERS.getCode(), String.format("%s: %s", CloudxOpenErrorEnum.MISSING_REQUIRED_PARAMETERS.getMsg(), "callbackType"));
        }
        // 如果回调类型（callbackType）不是配置类，需要非空检验，否则把projectUUID（项目UUID）设置为空
        if (!OpenPushSubscribeCallbackTypeEnum.CONFIG.code.equals(callbackType)) {
            if (StringUtils.isEmpty(projectUUID)) {
                return Result.fail(false, CloudxOpenErrorEnum.MISSING_REQUIRED_PARAMETERS.getCode(), String.format("%s: %s", CloudxOpenErrorEnum.MISSING_REQUIRED_PARAMETERS.getMsg(), "projectUUID"));
            }
        } else {
            vo.setProjectUUID(null);
        }

        return R.ok(true);
    }
}
