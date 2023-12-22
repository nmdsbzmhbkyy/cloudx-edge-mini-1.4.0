package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.origin.entity.ProjectShiftConf;
import com.aurine.cloudx.open.origin.vo.ProjectShiftConfPageVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 班次配置
 *
 * @author guhl@aurine.cn
 * @date 2020-07-23 08:36:54
 */
@Mapper
public interface ProjectShiftConfMapper extends BaseMapper<ProjectShiftConf> {

    /**
     * 班次配置分页查询
     *
     * @param page
     * @param projectShiftConfPageVo
     * @return
     */
    Page<ProjectShiftConfPageVo> pageShiftConf(Page<ProjectShiftConfPageVo> page, @Param("query") ProjectShiftConfPageVo projectShiftConfPageVo);
}
