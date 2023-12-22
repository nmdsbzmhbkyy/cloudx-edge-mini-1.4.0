package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.origin.entity.ProjectInspectPlan;
import com.aurine.cloudx.open.origin.vo.ProjectInspectPlanSearchConditionVo;
import com.aurine.cloudx.open.origin.vo.ProjectInspectPlanVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备巡检计划设置(ProjectInspectPlan)表数据库访问层
 *
 * @author 王良俊
 * @since 2020-07-23 18:34:05
 */
@Mapper
public interface ProjectInspectPlanMapper extends BaseMapper<ProjectInspectPlan> {

    /**
     * <p>
     * 巡检计划分页查询
     * </p>
     *
     * @param page  分页对象
     * @param query 查询条件
     * @return 分页数据
     */
    Page<ProjectInspectPlanVo> fetchList(Page page, @Param("query") ProjectInspectPlanSearchConditionVo query);

    /**
     * <p>
     * 查询所有已结束的巡检计划id列表
     * </p>
     *
     * @return 应该结束但未结束的巡检计划id的列表
     */
    List<String> listAllOutdatedPlan();

}