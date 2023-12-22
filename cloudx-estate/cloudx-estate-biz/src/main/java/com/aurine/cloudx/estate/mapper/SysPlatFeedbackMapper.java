package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.vo.SysPlatFeedbackFormVo;
import com.aurine.cloudx.estate.vo.SysPlatFeedbackVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aurine.cloudx.estate.entity.SysPlatFeedback;
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

    Page<SysPlatFeedbackVo> pageAll(Page<SysPlatFeedback> page, @Param("param")SysPlatFeedbackFormVo sysPlatFeedback);
}
