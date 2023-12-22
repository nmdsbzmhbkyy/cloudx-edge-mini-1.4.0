package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.ProjectAttrConstant;
import com.aurine.cloudx.estate.entity.ProjectDeviceAttrConf;
import com.aurine.cloudx.estate.entity.ProjectDeviceCollectCfg;
import com.aurine.cloudx.estate.entity.ProjectPersonAttrConf;
import com.aurine.cloudx.estate.mapper.ProjectAttrMapper;
import com.aurine.cloudx.estate.service.ProjectAttrService;
import com.aurine.cloudx.estate.service.ProjectDeviceAttrConfService;
import com.aurine.cloudx.estate.service.ProjectDeviceCollectCfgService;
import com.aurine.cloudx.estate.service.ProjectPersonAttrConfService;
import com.aurine.cloudx.estate.vo.ProjectAttrVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * (ProjectAttrServiceImpl)
 * 属性拓展配置
 *
 * @author xull
 * @since 2020/7/6 10:14
 */
@Service
@AllArgsConstructor
public class ProjectAttrServiceImpl implements ProjectAttrService {
    ProjectDeviceAttrConfService projectDeviceAttrConfService;
    ProjectDeviceCollectCfgService projectDeviceCollectCfgService;
    ProjectPersonAttrConfService projectPersonAttrConfService;
    ProjectAttrMapper projectAttrMapper;

    @Override
    public IPage<ProjectAttrVo> page(Page page, ProjectAttrVo query) {
        return projectAttrMapper.page(page, query);
    }

    @Override
    public boolean add(ProjectAttrVo projectAttrVo) {
        if (ProjectAttrConstant.DEVICE_ATTR.equals(projectAttrVo.getStyle())) {
            ProjectDeviceAttrConf projectDeviceAttrConf = new ProjectDeviceAttrConf();
            BeanUtils.copyProperties(projectAttrVo, projectDeviceAttrConf);
            projectDeviceAttrConf.setDeviceTypeId(projectAttrVo.getType());
            try {

                projectDeviceAttrConfService.save(projectDeviceAttrConf);
            } catch (Exception e) {
                throw new RuntimeException("属性编码冲突,保存失败");
            }
        }
        if (ProjectAttrConstant.DEVICE_COLLECT.equals(projectAttrVo.getStyle())) {
            ProjectDeviceCollectCfg projectDeviceCollectCfg = new ProjectDeviceCollectCfg();
            BeanUtils.copyProperties(projectAttrVo, projectDeviceCollectCfg);
            projectDeviceCollectCfg.setDeviceType(projectAttrVo.getType());
            try {
                projectDeviceCollectCfgService.save(projectDeviceCollectCfg);
            } catch (Exception e) {
                throw new RuntimeException("属性编码冲突,保存失败");
            }
        }
        if (ProjectAttrConstant.PERSON_ATTR.equals(projectAttrVo.getStyle())) {
            ProjectPersonAttrConf projectPersonAttrConf = new ProjectPersonAttrConf();
            BeanUtils.copyProperties(projectAttrVo, projectPersonAttrConf);
            projectPersonAttrConf.setPersonType(projectAttrVo.getType());
            try {

                projectPersonAttrConfService.save(projectPersonAttrConf);
            } catch (Exception e) {
                throw new RuntimeException("属性编码冲突,保存失败");
            }
        }
        return true;
    }

    @Override
    public boolean update(ProjectAttrVo projectAttrVo) {
        if (ProjectAttrConstant.DEVICE_ATTR.equals(projectAttrVo.getStyle())) {
            ProjectDeviceAttrConf projectDeviceAttrConf = new ProjectDeviceAttrConf();
            BeanUtils.copyProperties(projectAttrVo, projectDeviceAttrConf);
            projectDeviceAttrConf.setDeviceTypeId(projectAttrVo.getType());
            try {
                projectDeviceAttrConfService.updateById(projectDeviceAttrConf);
            } catch (Exception e) {
                throw new RuntimeException("属性编码冲突,保存失败");
            }
        }
        if (ProjectAttrConstant.DEVICE_COLLECT.equals(projectAttrVo.getStyle())) {
            ProjectDeviceCollectCfg projectDeviceCollectCfg = new ProjectDeviceCollectCfg();
            BeanUtils.copyProperties(projectAttrVo, projectDeviceCollectCfg);
            projectDeviceCollectCfg.setDeviceType(projectAttrVo.getType());
            try {

                projectDeviceCollectCfgService.updateById(projectDeviceCollectCfg);
            } catch (Exception e) {
                throw new RuntimeException("属性编码冲突,保存失败");
            }
        }
        if (ProjectAttrConstant.PERSON_ATTR.equals(projectAttrVo.getStyle())) {
            ProjectPersonAttrConf projectPersonAttrConf = new ProjectPersonAttrConf();
            BeanUtils.copyProperties(projectAttrVo, projectPersonAttrConf);
            projectPersonAttrConf.setPersonType(projectAttrVo.getType());
            try {
                projectPersonAttrConfService.updateById(projectPersonAttrConf);
            } catch (Exception e) {
                throw new RuntimeException("属性编码冲突,保存失败");
            }
        }
        return true;
    }

    @Override
    public boolean remove(String style, String attrId) {
        if (ProjectAttrConstant.DEVICE_ATTR.equals(style)) {
            projectDeviceAttrConfService.removeById(attrId);
        }
        if (ProjectAttrConstant.DEVICE_COLLECT.equals(style)) {
            projectDeviceCollectCfgService.removeById(attrId);
        }
        if (ProjectAttrConstant.PERSON_ATTR.equals(style)) {
            projectPersonAttrConfService.removeById(attrId);
        }
        return true;
    }
}
