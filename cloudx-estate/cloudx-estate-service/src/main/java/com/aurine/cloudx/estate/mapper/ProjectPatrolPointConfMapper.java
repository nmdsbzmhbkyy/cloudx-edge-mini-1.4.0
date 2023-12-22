package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectPatrolPointConf;
import com.aurine.cloudx.estate.vo.ProjectPatrolPointConfVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 项目巡更点配置(ProjectPatrolPointConf)表数据库访问层
 *
 * @author makejava
 * @since 2020-07-28 08:52:41
 */
@Mapper
public interface ProjectPatrolPointConfMapper extends BaseMapper<ProjectPatrolPointConf> {

    IPage<ProjectPatrolPointConfVo> selectAll(Page page, @Param("param") ProjectPatrolPointConfVo vo);

}