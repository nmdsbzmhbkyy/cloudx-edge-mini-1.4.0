package com.aurine.cloudx.open.biz.service;

import com.aurine.cloudx.open.common.entity.vo.AlarmEventVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-报警事件管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface OpenAlarmEventService {

    /**
     * 根据ID获取报警事件
     *
     * @param id
     * @return
     */
    R<AlarmEventVo> getById(String id);

    /**
     * 根据ID获取报警事件
     *
     * @param page
     * @param vo
     * @return
     */
    R<Page<AlarmEventVo>> page(Page page, AlarmEventVo vo);

    /**
     * 新增报警事件
     *
     * @param vo
     * @return
     */
    R<AlarmEventVo> save(AlarmEventVo vo);

    /**
     * 修改报警事件
     *
     * @param vo
     * @return
     */
    R<AlarmEventVo> update(AlarmEventVo vo);

    /**
     * 删除报警事件
     *
     * @param id
     * @return
     */
    R<Boolean> delete(String id);

}
