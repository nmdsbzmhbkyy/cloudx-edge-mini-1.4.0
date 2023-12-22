package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.estate.vo.SysPlatFeedbackFormVo;
import com.aurine.cloudx.estate.vo.SysPlatFeedbackVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aurine.cloudx.estate.mapper.SysPlatFeedbackMapper;
import com.aurine.cloudx.estate.entity.SysPlatFeedback;
import com.aurine.cloudx.estate.service.SysPlatFeedbackService;
import org.springframework.stereotype.Service;

/**
 * 平台意见反馈(SysPlatFeedback)表服务实现类
 *
 * @author xull
 * @since 2021-03-05 09:58:56
 */
@Service
public class SysPlatFeedbackServiceImpl extends ServiceImpl<SysPlatFeedbackMapper, SysPlatFeedback> implements SysPlatFeedbackService {

    @Override
    public  Page<SysPlatFeedbackVo> selectAllByOwner(Page<SysPlatFeedback> page, SysPlatFeedback sysPlatFeedback) {

        return baseMapper.selectAllByOwner(page,sysPlatFeedback);
    }

    @Override
    public Page<SysPlatFeedbackVo> pageAll(Page<SysPlatFeedback> page, SysPlatFeedbackFormVo sysPlatFeedback) {
        return baseMapper.pageAll(page,sysPlatFeedback);
    }
}
