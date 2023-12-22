package com.aurine.cloudx.open.origin.mapper;


import com.aurine.cloudx.open.origin.entity.ProjectEpidemicEvent;
import com.aurine.cloudx.open.origin.vo.ProjectEpidemicEventVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 疫情记录
 *
 * @author 邹宇
 * @date 2021-6-7 11:08:18
 */
@Mapper
public interface ProjectEpidemicEventMapper extends BaseMapper<ProjectEpidemicEvent> {

    List<ProjectEpidemicEvent> selectAll(ProjectEpidemicEventVo projectEpidemicEventVo);

    Integer getCount(ProjectEpidemicEventVo projectEpidemicEventVo);
}
