package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.origin.vo.SysPlatFeedbackFormVo;
import com.aurine.cloudx.open.origin.vo.SysPlatFeedbackVo;
import com.aurine.cloudx.open.origin.entity.SysPlatFeedback;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 平台意见反馈(SysPlatFeedback)表数据库访问层
 *
 * @author xull
 * @since 2021-03-05 09:58:57
 */
@Mapper
public interface SysPlatFeedbackMapper extends BaseMapper<SysPlatFeedback> {

    Page<SysPlatFeedbackVo> selectAllByOwner(Page<SysPlatFeedback> page, @Param("param") SysPlatFeedback sysPlatFeedback);

    Page<SysPlatFeedbackVo> pageAll(Page<SysPlatFeedback> page, @Param("param") SysPlatFeedbackFormVo sysPlatFeedback);
}
