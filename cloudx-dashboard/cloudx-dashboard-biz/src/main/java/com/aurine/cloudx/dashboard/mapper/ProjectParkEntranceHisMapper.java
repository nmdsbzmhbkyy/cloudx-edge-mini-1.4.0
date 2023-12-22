package com.aurine.cloudx.dashboard.mapper;

import com.aurine.cloudx.dashboard.entity.ProjectParkEntranceHis;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * <p>  </p>
 *
 * @ClassName: ProjectEntranceEventMapper
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-04-12 9:32
 * @Copyright:
 */
@Mapper
public interface ProjectParkEntranceHisMapper extends BaseMapper<ProjectParkEntranceHis> {

    List<ProjectParkEntranceHis> selectParkEntranceHis(@Param("projectIdArray")String[] projectIdArray);
}
