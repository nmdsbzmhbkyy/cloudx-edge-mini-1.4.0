package com.aurine.cloudx.open.biz.service.impl;

import com.aurine.cloudx.open.biz.service.OpenAlarmHandleService;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.common.entity.vo.AlarmHandleVo;
import com.aurine.cloudx.open.origin.entity.ProjectAlarmHandle;
import com.aurine.cloudx.open.origin.service.ProjectAlarmHandleService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-报警处理管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

@Service
public class OpenAlarmHandleServiceImpl implements OpenAlarmHandleService {

    @Resource
    private ProjectAlarmHandleService projectAlarmHandleService;


    @Override
    public R<AlarmHandleVo> getById(String id) {
        ProjectAlarmHandle po = projectAlarmHandleService.getById(id);
        if (po == null) return Result.fail(null, CloudxOpenErrorEnum.EMPTY_RESULT);

        AlarmHandleVo vo = new AlarmHandleVo();
        BeanUtils.copyProperties(po, vo);

        return R.ok(vo);
    }

    @Override
    public R<Page<AlarmHandleVo>> page(Page page, AlarmHandleVo vo) {
        return R.ok(projectAlarmHandleService.page(page, vo));
    }

    @Override
    public R<AlarmHandleVo> save(AlarmHandleVo vo) {
        return R.ok();
    }

    @Override
    public R<AlarmHandleVo> update(AlarmHandleVo vo) {
        return R.ok();
    }

    @Override
    public R<Boolean> delete(String id) {
        return R.ok();
    }

}
