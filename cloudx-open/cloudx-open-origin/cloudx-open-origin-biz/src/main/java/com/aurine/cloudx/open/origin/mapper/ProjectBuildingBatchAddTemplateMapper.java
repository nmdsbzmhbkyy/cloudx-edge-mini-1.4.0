

package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.origin.entity.ProjectBuildingBatchAddTemplate;
import com.aurine.cloudx.open.origin.vo.ProjectBuildingBatchAddTemplateRecordVo;
import com.aurine.cloudx.open.origin.vo.ProjectBuildingBatchAddTemplateSearchCondition;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 楼栋模板
 *
 * @author 王伟
 * @date 2020-06-04 15:36:20
 */
@Mapper
public interface ProjectBuildingBatchAddTemplateMapper extends BaseMapper<ProjectBuildingBatchAddTemplate> {

    IPage<ProjectBuildingBatchAddTemplateRecordVo> selectPage(IPage<ProjectBuildingBatchAddTemplateRecordVo> page, @Param("searchCondition") ProjectBuildingBatchAddTemplateSearchCondition searchCondition);
}
