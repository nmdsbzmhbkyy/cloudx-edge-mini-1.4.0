

package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectBuildingBatchAddTemplate;
import com.aurine.cloudx.estate.vo.ProjectBuildingBatchAddTemplateRecordVo;
import com.aurine.cloudx.estate.vo.ProjectBuildingBatchAddTemplateSearchCondition;
import com.aurine.cloudx.estate.vo.ProjectBuildingBatchAddTemplateVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 楼栋模板
 *
 * @author 王伟
 * @date 2020-06-04 15:36:20
 */
public interface ProjectBuildingBatchAddTemplateService extends IService<ProjectBuildingBatchAddTemplate> {

    /**
     * 分页查询模板
     * @param page
     * @param searchCondition
     * @return
     */
    IPage<ProjectBuildingBatchAddTemplateRecordVo> findPage(IPage<ProjectBuildingBatchAddTemplateRecordVo> page, ProjectBuildingBatchAddTemplateSearchCondition searchCondition) ;

    /**
     * 保存模板
     * @param vo
     * @return
     */
    boolean save(ProjectBuildingBatchAddTemplateVo vo);

    /**
     * 修改模板
     * @param vo
     * @return
     */
    boolean update(ProjectBuildingBatchAddTemplateVo vo);

    /**
     * 获取模板
     * @param templateId
     * @return
     */
    ProjectBuildingBatchAddTemplateVo getVo(String templateId);

    /**
     * 删除模板
     * @param buildingTemplateId
     * @return
     */
    boolean deleteById(String buildingTemplateId);
}
