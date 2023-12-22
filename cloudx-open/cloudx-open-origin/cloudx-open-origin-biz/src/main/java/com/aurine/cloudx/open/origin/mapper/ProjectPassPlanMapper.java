

package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.common.entity.vo.PassPlanVo;
import com.aurine.cloudx.open.origin.entity.ProjectPassPlan;
import com.aurine.cloudx.open.origin.vo.ProjectPassPlanVo;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    @SqlParser(filter = true)
    boolean savePassPlan(@Param("passPlan") ProjectPassPlanVo passPlanVo);

    /**
     * 多条件分页查询
     *
     * @param page
     * @param po
     * @return
     */
    Page<PassPlanVo> page(Page page, @Param("query") ProjectPassPlan po);
}
