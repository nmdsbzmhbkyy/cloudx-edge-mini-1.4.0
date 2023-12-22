

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectPassPlanPolicyRel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
}
