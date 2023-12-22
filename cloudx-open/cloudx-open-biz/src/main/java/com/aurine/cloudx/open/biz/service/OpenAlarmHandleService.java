package com.aurine.cloudx.open.biz.service;

import com.aurine.cloudx.open.common.entity.vo.AlarmHandleVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-报警处理管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface OpenAlarmHandleService {

    /**
     * 根据ID获取报警处理
     *
     * @param id
     * @return
     */
    R<AlarmHandleVo> getById(String id);

    /**
     * 根据ID获取报警处理
     *
     * @param page
     * @param vo
     * @return
     */
    R<Page<AlarmHandleVo>> page(Page page, AlarmHandleVo vo);

    /**
     * 新增报警处理
     *
     * @param vo
     * @return
     */
    R<AlarmHandleVo> save(AlarmHandleVo vo);

    /**
     * 修改报警处理
     *
     * @param vo
     * @return
     */
    R<AlarmHandleVo> update(AlarmHandleVo vo);

    /**
     * 删除报警处理
     *
     * @param id
     * @return
     */
    R<Boolean> delete(String id);

}
