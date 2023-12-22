

package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectUnitBatchAddTemplate;
import com.aurine.cloudx.estate.vo.ProjectUnitBatchAddTemplateVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 房屋模板
 *
 * @author 王伟
 * @date 2020-06-04 15:37:03
 */
public interface ProjectUnitBatchAddTemplateService extends IService<ProjectUnitBatchAddTemplate> {

    /**
     * 根据building Id 获取 VO
     * @param buildingTemplateId
     * @return
     */
    List<ProjectUnitBatchAddTemplateVo> listVo(String buildingTemplateId);

    /**
     * 根据buildingTemplateId删除单元极其所属的房屋模板
     * @param buildingTemplateId
     * @return
     */
    boolean deleteByBuildingTemplateId(String buildingTemplateId);
}
