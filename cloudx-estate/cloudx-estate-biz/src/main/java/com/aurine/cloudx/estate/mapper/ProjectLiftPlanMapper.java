

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectLiftPlan;
import com.aurine.cloudx.estate.entity.ProjectPassPlan;
import com.aurine.cloudx.estate.vo.ProjectLiftPlanVo;
import com.aurine.cloudx.estate.vo.ProjectPassPlanVo;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 通行方案
 *
 * @author pigx code generator
 * @date 2022-02-22 14:01:07
 */
@Mapper
public interface ProjectLiftPlanMapper extends BaseMapper<ProjectLiftPlan> {

    @SqlParser(filter=true)
    boolean saveLiftPlan(@Param("liftPlan") ProjectLiftPlanVo liftPlanVo);

}
