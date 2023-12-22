

package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.common.entity.vo.PassPlanPolicyRelVo;
import com.aurine.cloudx.open.origin.entity.ProjectPassPlanPolicyRel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 策略方案关联
 *
 * @author pigx code generator
 * @date 2020-05-20 15:54:21
 */
@Mapper
public interface ProjectPassPlanPolicyRelMapper extends BaseMapper<ProjectPassPlanPolicyRel> {
    List<String> listMacroByPerson(@Param("personId") String personId);

    List<String> listMacroByPlan(@Param("planId") String planId);

    /**
     * 多条件分页查询
     *
     * @param page
     * @param po
     * @return
     */
    Page<PassPlanPolicyRelVo> page(Page page, @Param("query") ProjectPassPlanPolicyRel po);
}
