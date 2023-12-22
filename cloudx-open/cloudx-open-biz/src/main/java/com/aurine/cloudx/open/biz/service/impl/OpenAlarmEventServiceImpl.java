package com.aurine.cloudx.open.biz.service.impl;

import com.aurine.cloudx.open.biz.service.OpenAlarmEventService;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.common.entity.vo.AlarmEventVo;
import com.aurine.cloudx.open.origin.entity.ProjectEntranceAlarmEvent;
import com.aurine.cloudx.open.origin.service.ProjectEntranceAlarmEventService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-报警事件管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

@Service
public class OpenAlarmEventServiceImpl implements OpenAlarmEventService {

    @Resource
    private ProjectEntranceAlarmEventService projectEntranceAlarmEventService;


    @Override
    public R<AlarmEventVo> getById(String id) {
        ProjectEntranceAlarmEvent po = projectEntranceAlarmEventService.getById(id);
        if (po == null) return Result.fail(null, CloudxOpenErrorEnum.EMPTY_RESULT);

        AlarmEventVo vo = new AlarmEventVo();
        BeanUtils.copyProperties(po, vo);

        return R.ok(vo);
    }

    @Override
    public R<Page<AlarmEventVo>> page(Page page, AlarmEventVo vo) {
        return R.ok(projectEntranceAlarmEventService.page(page, vo));
    }

    @Override
    public R<AlarmEventVo> save(AlarmEventVo vo) {
        return R.ok();
    }

    @Override
    public R<AlarmEventVo> update(AlarmEventVo vo) {
        return R.ok();
    }

    @Override
    public R<Boolean> delete(String id) {
        return R.ok();
    }

}
