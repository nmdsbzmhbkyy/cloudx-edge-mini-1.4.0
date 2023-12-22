package com.aurine.cloudx.open.biz.service.impl;

import com.aurine.cloudx.open.biz.service.OpenPersonEntranceService;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.common.entity.vo.PersonEntranceVo;
import com.aurine.cloudx.open.origin.entity.ProjectEntranceEvent;
import com.aurine.cloudx.open.origin.service.ProjectEntranceEventService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-人行事件管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

@Service
public class OpenPersonEntranceServiceImpl implements OpenPersonEntranceService {

    @Resource
    private ProjectEntranceEventService projectEntranceEventService;


    @Override
    public R<PersonEntranceVo> getById(String id) {
        ProjectEntranceEvent po = projectEntranceEventService.getById(id);
        if (po == null) return Result.fail(null, CloudxOpenErrorEnum.EMPTY_RESULT);

        PersonEntranceVo vo = new PersonEntranceVo();
        BeanUtils.copyProperties(po, vo);

        return R.ok(vo);
    }

    @Override
    public R<Page<PersonEntranceVo>> page(Page page, PersonEntranceVo vo) {
        return R.ok(projectEntranceEventService.page(page, vo));
    }

    @Override
    public R<PersonEntranceVo> save(PersonEntranceVo vo) {
        return R.ok();
    }

    @Override
    public R<PersonEntranceVo> update(PersonEntranceVo vo) {
        return R.ok();
    }

    @Override
    public R<Boolean> delete(String id) {
        return R.ok();
    }

}
