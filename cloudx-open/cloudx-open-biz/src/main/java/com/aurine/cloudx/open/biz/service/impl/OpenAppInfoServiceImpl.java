package com.aurine.cloudx.open.biz.service.impl;

import com.aurine.cloudx.open.biz.mapper.OpenAppInfoMapper;
import com.aurine.cloudx.open.biz.service.OpenAppInfoService;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.common.entity.entity.OpenAppInfo;
import com.aurine.cloudx.open.common.entity.vo.OpenAppInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 开放平台应用信息
 *
 * @author : Qiu
 * @date : 2022 01 20 11:13
 */

@Service
public class OpenAppInfoServiceImpl extends ServiceImpl<OpenAppInfoMapper, OpenAppInfo> implements OpenAppInfoService {

    @Override
    public R<OpenAppInfoVo> getById(String id) {
        OpenAppInfo po = super.getById(id);
        if (po == null) return Result.fail(null, CloudxOpenErrorEnum.EMPTY_RESULT);

        OpenAppInfoVo vo = new OpenAppInfoVo();
        BeanUtils.copyProperties(po, vo);

        return R.ok(vo);
    }

    @Override
    public R<OpenAppInfoVo> getByAppId(String appId) {
        OpenAppInfo po = super.getOne(new LambdaQueryWrapper<OpenAppInfo>()
                .eq(OpenAppInfo::getAppId, appId));
        if (po == null) return Result.fail(null, CloudxOpenErrorEnum.EMPTY_RESULT);

        OpenAppInfoVo vo = new OpenAppInfoVo();
        BeanUtils.copyProperties(po, vo);

        return R.ok(vo);
    }
}
