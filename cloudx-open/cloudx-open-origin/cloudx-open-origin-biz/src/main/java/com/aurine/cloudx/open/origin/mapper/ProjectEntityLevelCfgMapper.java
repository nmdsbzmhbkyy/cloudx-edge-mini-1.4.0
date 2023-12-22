package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.origin.vo.ProjectDeviceNoRule;
import com.aurine.cloudx.open.origin.entity.ProjectEntityLevelCfg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 配置项目区域层级
 *
 * @author pigx code generator
 * @date 2020-05-06 13:49:41
 */
@Mapper
//@CacheNamespace
public interface ProjectEntityLevelCfgMapper extends BaseMapper<ProjectEntityLevelCfg> {

    ProjectDeviceNoRule getProjectSubSection(@Param("projectId") Integer projectId);

    void putMacroId(Integer projectId);
}
