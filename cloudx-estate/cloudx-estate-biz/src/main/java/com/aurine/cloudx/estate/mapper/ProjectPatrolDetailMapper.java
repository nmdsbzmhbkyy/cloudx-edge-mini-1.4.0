package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectPatrolDetail;
import com.aurine.cloudx.estate.entity.ProjectPatrolInfo;
import com.aurine.cloudx.estate.vo.ProjectPatrolDetailVo;
import com.aurine.cloudx.estate.vo.ProjectPatrolInfoSearchCondition;
import com.aurine.cloudx.estate.vo.ProjectPatrolInfoVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 项目巡更明细(ProjectPatrolInfo)表数据库访问层
 *
 * @author 黄阳光 <huangyg@aurine.cn>
 * @since 2020-09-09 09:01:23
 */
@Mapper
public interface ProjectPatrolDetailMapper extends BaseMapper<ProjectPatrolDetail> {
}