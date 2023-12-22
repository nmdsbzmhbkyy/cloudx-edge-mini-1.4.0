package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.origin.entity.ProjectPatrolDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 项目巡更明细(ProjectPatrolInfo)表数据库访问层
 *
 * @author 黄阳光 <huangyg@aurine.cn>
 * @since 2020-09-09 09:01:23
 */
@Mapper
public interface ProjectPatrolDetailMapper extends BaseMapper<ProjectPatrolDetail> {
}