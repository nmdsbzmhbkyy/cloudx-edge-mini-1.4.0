package com.aurine.cloudx.estate.mapper;
import com.aurine.cloudx.estate.vo.ProjectMediaAdDevCfgVo;
import com.aurine.cloudx.estate.vo.ProjectPersonNoticePlanPageVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aurine.cloudx.estate.entity.ProjectPersonNoticePlan;
import org.apache.ibatis.annotations.Param;

/**
 * 住户通知计划(ProjectPersonNoticePlan)表数据库访问层
 *
 * @author makejava
 * @since 2020-12-14 15:58:04
 */
@Mapper
public interface ProjectPersonNoticePlanMapper extends BaseMapper<ProjectPersonNoticePlan> {
    /**
     * 分页查询发送设备列表信息
     *
     * @param page
     * @param projectPersonNoticePlanPageVo
     *
     * @return
     */
    Page<ProjectPersonNoticePlanPageVo> pageNoticePlan(Page page, @Param("query") ProjectPersonNoticePlanPageVo projectPersonNoticePlanPageVo);
}