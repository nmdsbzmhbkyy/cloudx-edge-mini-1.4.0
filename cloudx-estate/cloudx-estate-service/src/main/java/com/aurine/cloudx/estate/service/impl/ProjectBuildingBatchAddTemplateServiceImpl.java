
package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.aurine.cloudx.estate.entity.ProjectBuildingBatchAddTemplate;
import com.aurine.cloudx.estate.entity.ProjectHouseBatchAddTemplate;
import com.aurine.cloudx.estate.entity.ProjectUnitBatchAddTemplate;
import com.aurine.cloudx.estate.mapper.ProjectBuildingBatchAddTemplateMapper;
import com.aurine.cloudx.estate.service.ProjectBuildingBatchAddTemplateService;
import com.aurine.cloudx.estate.service.ProjectHouseBatchAddTemplateService;
import com.aurine.cloudx.estate.service.ProjectUnitBatchAddTemplateService;
import com.aurine.cloudx.estate.vo.ProjectBuildingBatchAddTemplateRecordVo;
import com.aurine.cloudx.estate.vo.ProjectBuildingBatchAddTemplateSearchCondition;
import com.aurine.cloudx.estate.vo.ProjectBuildingBatchAddTemplateVo;
import com.aurine.cloudx.estate.vo.ProjectUnitBatchAddTemplateVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 楼栋模板
 *
 * @author 王伟
 * @date 2020-06-04 15:36:20
 */
@Service
public class ProjectBuildingBatchAddTemplateServiceImpl extends ServiceImpl<ProjectBuildingBatchAddTemplateMapper, ProjectBuildingBatchAddTemplate> implements ProjectBuildingBatchAddTemplateService {

    @Autowired
    private ProjectUnitBatchAddTemplateService projectUnitBatchAddTemplateService;
    @Autowired
    private ProjectHouseBatchAddTemplateService projectHouseBatchAddTemplateService;

    /**
     * 分页查询模板
     *
     * @param page
     * @param searchCondition
     * @return
     */
    @Override
    public IPage<ProjectBuildingBatchAddTemplateRecordVo> findPage(IPage<ProjectBuildingBatchAddTemplateRecordVo> page, ProjectBuildingBatchAddTemplateSearchCondition searchCondition) {
        return this.baseMapper.selectPage(page, searchCondition);
    }

    /**
     * 保存模板
     *
     * @param vo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(ProjectBuildingBatchAddTemplateVo vo) {


        /***
         * @author: 王伟
         * 检查是否存在同名模板，存在禁止添加
         */

        List countList = this.list(new QueryWrapper<ProjectBuildingBatchAddTemplate>().lambda().eq(ProjectBuildingBatchAddTemplate::getTemplateName, vo.getTemplateName()));
        if(CollectionUtil.isNotEmpty(countList)){
            throw new RuntimeException("模板名称重复，请修改模板名称");
        }

        //init building
        List<ProjectHouseBatchAddTemplate> houseList = new ArrayList<>();
        List<ProjectHouseBatchAddTemplate> housePoList = new ArrayList<>();
        List<ProjectUnitBatchAddTemplate> unitPoList = new ArrayList<>();
        ProjectUnitBatchAddTemplate unitTemplatePo;
        ProjectBuildingBatchAddTemplate buildingTemplatePo = new ProjectBuildingBatchAddTemplate();

        String buildingTemplateId = UUID.randomUUID().toString().replaceAll("-", "");
        String unitTemplateId = "";

        List<ProjectUnitBatchAddTemplateVo> unitVoList = vo.getUnitList();

        //解析楼栋模板
        BeanUtils.copyProperties(vo, buildingTemplatePo);
        buildingTemplatePo.setBuildingTemplateId(buildingTemplateId);

        //解析单元模板
        for (ProjectUnitBatchAddTemplateVo unitVo : unitVoList) {
            //init unit
            unitTemplatePo = new ProjectUnitBatchAddTemplate();
            BeanUtils.copyProperties(unitVo, unitTemplatePo);
            unitTemplateId = UUID.randomUUID().toString().replaceAll("-", "");

            unitTemplatePo.setBuildingTemplateId(buildingTemplateId);
            unitTemplatePo.setUnitTemplateId(unitTemplateId);

            unitPoList.add(unitTemplatePo);

            // 解析楼房模板
            houseList = unitVo.getHouseList();
            for (ProjectHouseBatchAddTemplate houseTemplatePo : houseList) {
                houseTemplatePo.setUnitTemplateId(unitTemplateId);
                housePoList.add(houseTemplatePo);
            }
        }

        //批量增加房屋
        this.projectHouseBatchAddTemplateService.saveBatch(housePoList);
        //批量增加单元
        this.projectUnitBatchAddTemplateService.saveBatch(unitPoList);

        //保存楼栋模板
        return this.save(buildingTemplatePo);
    }

    /**
     * 修改模板
     *
     * @param vo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(ProjectBuildingBatchAddTemplateVo vo) {

        //init building
        List<ProjectHouseBatchAddTemplate> housePoList = new ArrayList<>();
        List<ProjectHouseBatchAddTemplate> houseProList = new ArrayList<>();
        List<ProjectUnitBatchAddTemplate> unitPoList = new ArrayList<>();
        ProjectUnitBatchAddTemplate unitTemplatePo;
        ProjectBuildingBatchAddTemplate buildingTemplatePo = new ProjectBuildingBatchAddTemplate();

        String buildingTemplateId = vo.getBuildingTemplateId();
        String unitTemplateId = "";
        List<ProjectUnitBatchAddTemplateVo> unitVoList = vo.getUnitList();

        //删除旧的单元、楼栋模板，并重新添加
        this.projectUnitBatchAddTemplateService.deleteByBuildingTemplateId(vo.getBuildingTemplateId());

        //解析楼栋模板
        BeanUtils.copyProperties(vo, buildingTemplatePo);
        buildingTemplatePo.setBuildingTemplateId(buildingTemplateId);

        //解析单元模板
        for (ProjectUnitBatchAddTemplateVo unitVo : unitVoList) {
            //init unit
            unitTemplatePo = new ProjectUnitBatchAddTemplate();
            BeanUtils.copyProperties(unitVo, unitTemplatePo);
            unitTemplateId = UUID.randomUUID().toString().replaceAll("-", "");

            unitTemplatePo.setBuildingTemplateId(buildingTemplateId);
            unitTemplatePo.setUnitTemplateId(unitTemplateId);

            unitPoList.add(unitTemplatePo);

            // 解析楼房模板
            for (ProjectHouseBatchAddTemplate houseTemplatePo : unitVo.getHouseList()) {
                houseTemplatePo.setUnitTemplateId(unitTemplateId);
                housePoList.add(houseTemplatePo);
            }
        }

        //批量增加房屋
        this.projectHouseBatchAddTemplateService.saveBatch(housePoList);
        //批量增加单元
        this.projectUnitBatchAddTemplateService.saveBatch(unitPoList);

        //修改楼栋模板
        return this.updateById(buildingTemplatePo);
    }

    /**
     * 获取模板
     *
     * @param buildingTemplateId
     * @return
     */
    @Override
    public ProjectBuildingBatchAddTemplateVo getVo(String buildingTemplateId) {

        //1. init
        ProjectBuildingBatchAddTemplateVo vo = new ProjectBuildingBatchAddTemplateVo();

        //2.business
        ProjectBuildingBatchAddTemplate buildingBatchAddTemplate = this.getById(buildingTemplateId);
        BeanUtils.copyProperties(buildingBatchAddTemplate, vo);

        List<ProjectUnitBatchAddTemplateVo> unitTemplateVoList = this.projectUnitBatchAddTemplateService.listVo(buildingTemplateId);
        vo.setUnitList(unitTemplateVoList);

        //3.result
        return vo;
    }

    /**
     * 删除模板
     *
     * @param buildingTemplateId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(String buildingTemplateId) {
        this.projectUnitBatchAddTemplateService.deleteByBuildingTemplateId(buildingTemplateId);
        return this.removeById(buildingTemplateId);
    }
}
