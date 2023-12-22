
package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.estate.entity.ProjectHouseBatchAddTemplate;
import com.aurine.cloudx.estate.entity.ProjectUnitBatchAddTemplate;
import com.aurine.cloudx.estate.mapper.ProjectUnitBatchAddTemplateMapper;
import com.aurine.cloudx.estate.service.ProjectHouseBatchAddTemplateService;
import com.aurine.cloudx.estate.service.ProjectUnitBatchAddTemplateService;
import com.aurine.cloudx.estate.vo.ProjectUnitBatchAddTemplateVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 房屋模板
 *
 * @author 王伟
 * @date 2020-06-04 15:37:03
 */
@Service
public class ProjectUnitBatchAddTemplateServiceImpl extends ServiceImpl<ProjectUnitBatchAddTemplateMapper, ProjectUnitBatchAddTemplate> implements ProjectUnitBatchAddTemplateService {

    @Autowired
    private ProjectHouseBatchAddTemplateService projectHouseBatchAddTemplateService;

    /**
     * 根据building Id 获取 VO
     *
     * @param buildingTemplateId
     * @return
     */
    @Override
    public List<ProjectUnitBatchAddTemplateVo> listVo(String buildingTemplateId) {

        //1. init
        List<ProjectUnitBatchAddTemplateVo> unitTemplateVoList = new ArrayList<>();
        ProjectUnitBatchAddTemplateVo unitTemplateVo;
        List<ProjectHouseBatchAddTemplate> houseTemplatePoList;

        //2.business
        List<ProjectUnitBatchAddTemplate> unitTemplatePoList = this.list(new QueryWrapper<ProjectUnitBatchAddTemplate>().lambda().eq(ProjectUnitBatchAddTemplate::getBuildingTemplateId, buildingTemplateId));
        //po -> vo
        for (ProjectUnitBatchAddTemplate unitTemplatePo : unitTemplatePoList) {
            unitTemplateVo = new ProjectUnitBatchAddTemplateVo();
            BeanUtils.copyProperties(unitTemplatePo, unitTemplateVo);

            //获取单元下的房屋信息
            houseTemplatePoList = this.projectHouseBatchAddTemplateService.list(new QueryWrapper<ProjectHouseBatchAddTemplate>().lambda().eq(ProjectHouseBatchAddTemplate::getUnitTemplateId, unitTemplateVo.getUnitTemplateId()));
            unitTemplateVo.setHouseList(houseTemplatePoList);
            unitTemplateVoList.add(unitTemplateVo);
        }

        //3.result
        return unitTemplateVoList;
    }

    /**
     * 根据buildingTemplateId删除单元极其所属的房屋模板
     *
     * @param buildingTemplateId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByBuildingTemplateId(String buildingTemplateId) {
        List<ProjectUnitBatchAddTemplate> unitList = list(new QueryWrapper<ProjectUnitBatchAddTemplate>().lambda().eq(ProjectUnitBatchAddTemplate::getBuildingTemplateId, buildingTemplateId));
        //删除房屋模板
        for (ProjectUnitBatchAddTemplate unitTemplate : unitList) {
            this.projectHouseBatchAddTemplateService.deleteByUnitId(unitTemplate.getUnitTemplateId());
        }

        //删除单元模板
        return this.remove(new QueryWrapper<ProjectUnitBatchAddTemplate>().lambda().eq(ProjectUnitBatchAddTemplate::getBuildingTemplateId, buildingTemplateId));
    }
}
