package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectParkBillingRule;
import com.aurine.cloudx.estate.vo.ProjectParkBillingRuleRecordVo;
import com.aurine.cloudx.estate.vo.ProjectParkBillingRuleSearchConditionVo;
import com.aurine.cloudx.estate.vo.ProjectParkRuleVo;
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

    Page<ProjectParkBillingRuleRecordVo> pageBillRule(Page page, @Param("query") ProjectParkBillingRuleSearchConditionVo query, @Param("projectId") Integer projectId);

    /**
    * <p>
    * 获取当前项目所有车场和其对应的收费规则 车场ID+收费规则名，收费规则ID
    * </p>
    *
    * @param projectId 项目ID
    * @author: 王良俊
    */
    List<ProjectParkRuleVo> getRuleParkList(@Param("projectId") Integer projectId);
}
