package com.aurine.cloudx.dashboard.mapper;

import com.aurine.cloudx.dashboard.entity.ProjectEntranceEvent;
import com.aurine.cloudx.dashboard.entity.ProjectParkEntranceHis;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>  </p>
 * @ClassName: ProjectEntranceEventMapper
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-04-12 9:37
 * @Copyright:
 */
@Mapper
public interface ProjectEntranceEventMapper extends BaseMapper<ProjectEntranceEvent> {

    List<ProjectEntranceEvent> selectEntranceEvent(@Param("projectIdArray")String[] projectIdArray);
}
