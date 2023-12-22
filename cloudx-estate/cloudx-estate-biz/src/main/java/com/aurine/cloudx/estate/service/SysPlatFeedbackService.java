package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.vo.SysPlatFeedbackFormVo;
import com.aurine.cloudx.estate.vo.SysPlatFeedbackVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aurine.cloudx.estate.entity.SysPlatFeedback;

/**
 * 平台意见反馈(SysPlatFeedback)表服务接口
 *
 * @author xull
 * @since 2021-03-05 09:58:56
 */
public interface SysPlatFeedbackService extends IService<SysPlatFeedback> {

    Page<SysPlatFeedbackVo> selectAllByOwner(Page<SysPlatFeedback> page, SysPlatFeedback sysPlatFeedback);

    Page<SysPlatFeedbackVo> pageAll(Page<SysPlatFeedback> page, SysPlatFeedbackFormVo sysPlatFeedback);
}
