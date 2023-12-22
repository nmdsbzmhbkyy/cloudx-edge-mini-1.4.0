

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectPassPlan;
import com.aurine.cloudx.estate.vo.ProjectPassPlanVo;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 通行方案
 *
 * @author pigx code generator
 * @date 2020-05-20 14:01:07
 */
@Mapper
public interface ProjectPassPlanMapper extends BaseMapper<ProjectPassPlan> {

    @SqlParser(filter=true)
    boolean savePassPlan(@Param("passPlan")ProjectPassPlanVo passPlanVo);

}
