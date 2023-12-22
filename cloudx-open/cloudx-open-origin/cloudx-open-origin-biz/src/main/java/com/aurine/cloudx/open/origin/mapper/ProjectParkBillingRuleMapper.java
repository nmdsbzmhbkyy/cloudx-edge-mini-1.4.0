package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.origin.dto.ProjectParkBillingRuleDto;
import com.aurine.cloudx.open.origin.dto.ProjectParkRuleDto;
import com.aurine.cloudx.open.origin.entity.ProjectParkBillingRule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 车场计费规则
 *
 * @author 王伟
 * @date 2020-07-07 11:34:12
 */
@Mapper
public interface ProjectParkBillingRuleMapper extends BaseMapper<ProjectParkBillingRule> {

    Page<ProjectParkBillingRuleDto> pageBillRule(Page page, @Param("query") ProjectParkBillingRuleDto query, @Param("projectId") Integer projectId);

    /**
    * <p>
    * 获取当前项目所有车场和其对应的收费规则 车场ID+收费规则名，收费规则ID
    * </p>
    *
    * @param projectId 项目ID
    * @author: 王良俊
    */
    List<ProjectParkRuleDto> getRuleParkList(@Param("projectId") Integer projectId);

    /**
     * 查询计费模板是否在使用中
     * @param ruleId
     * @return
     */
    List<String> isInUsed(@Param("ruleId") String ruleId);

    ProjectParkBillingRuleDto getById(@Param("ruleId") String ruleId);
}
