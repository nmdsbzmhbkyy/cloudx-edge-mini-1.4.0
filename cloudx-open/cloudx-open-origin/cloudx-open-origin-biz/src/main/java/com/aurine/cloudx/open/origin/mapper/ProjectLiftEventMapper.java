package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.origin.entity.ProjectLiftEvent;
import com.aurine.cloudx.open.origin.vo.ProjectEventSearchCondition;
import com.aurine.cloudx.open.origin.vo.ProjectLiftEventVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 乘梯事件记录
 *
 *  @author zy
 *  @date 2022-07-15 10:49:26
 */
@Mapper
public interface ProjectLiftEventMapper extends BaseMapper<ProjectLiftEvent> {

    IPage<ProjectLiftEventVo> select(Page page, @Param("param") ProjectEventSearchCondition param);
}
