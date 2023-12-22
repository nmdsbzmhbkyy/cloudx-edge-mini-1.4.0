

package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.origin.entity.ProjectLabelConfig;
import com.aurine.cloudx.open.origin.vo.ProjectLabelConfigVo;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 标签管理
 *
 * @author 王伟
 * @date 2020-05-07 08:09:35
 */
@Mapper
public interface ProjectLabelConfigMapper extends BaseMapper<ProjectLabelConfig> {
    @SqlParser(filter=true)
    IPage<ProjectLabelConfig> select(IPage<?> page, @Param("query") ProjectLabelConfigVo projectLabelConfigVo);

    @SqlParser(filter=true)
    ProjectLabelConfigVo selectByLabelId(String labelId);

    @SqlParser(filter=true)
    boolean initInsert(@Param("param") ProjectLabelConfig po, @Param("projectId") Integer projectId, @Param("tenantId") Integer tenantId);
//    boolean initInsert(@Param("param") ProjectLabelConfig po);

    @SqlParser(filter=true)
    List<ProjectLabelConfig> selectByTemplate();

    @SqlParser(filter=true)
    List<ProjectLabelConfig> selectByDefault(@Param("projectId") Integer projectId, @Param("oldMan") String oldMan, @Param("arrears") String arrears, @Param("difficulty") String difficulty);

    /***
     * 查询项目层级的模板
     * @return
     */

    @SqlParser(filter=true)
    List<ProjectLabelConfigVo> selectProjectTemplate(@Param("labelName") String labelName, @Param("projectId") Integer projectId);
}
