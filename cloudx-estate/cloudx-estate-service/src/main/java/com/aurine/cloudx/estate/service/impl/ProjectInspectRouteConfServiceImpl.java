package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.aurine.cloudx.estate.constant.InspectStatusConstants;
import com.aurine.cloudx.estate.entity.ProjectInspectRouteConf;
import com.aurine.cloudx.estate.mapper.ProjectInspectRouteConfMapper;
import com.aurine.cloudx.estate.service.ProjectInspectRouteConfService;
import com.aurine.cloudx.estate.service.ProjectInspectRoutePointConfService;
import com.aurine.cloudx.estate.vo.ProjectInspectRouteConfSearchConditionVo;
import com.aurine.cloudx.estate.vo.ProjectInspectRouteConfVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 设备巡检路线设置(ProjectInspectRouteConf)表服务实现类
 *
 * @author 王良俊
 * @since 2020-07-23 18:33:19
 */
@Service
public class ProjectInspectRouteConfServiceImpl extends ServiceImpl<ProjectInspectRouteConfMapper,
        ProjectInspectRouteConf> implements ProjectInspectRouteConfService {

    @Resource
    ProjectInspectRouteConfMapper projectInspectRouteConfMapper;
    @Resource
    ProjectInspectRoutePointConfService projectInspectRoutePointConfService;

    @Override
    public Page<ProjectInspectRouteConfVo> fetchList(Page<ProjectInspectRouteConfVo> page, ProjectInspectRouteConfSearchConditionVo query) {
        return projectInspectRouteConfMapper.fetchList(page, query);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveInspectRoute(ProjectInspectRouteConfVo vo) {
        int count = this.count(new QueryWrapper<ProjectInspectRouteConf>().lambda().eq(ProjectInspectRouteConf::getInspectRouteName, vo.getInspectRouteName()));
        if (count != 0) {
            throw new RuntimeException("路线名称重复");
        }
        ProjectInspectRouteConf projectInspectRouteConf = new ProjectInspectRouteConf();
        BeanUtil.copyProperties(vo, projectInspectRouteConf);
        projectInspectRouteConf.setStatus(InspectStatusConstants.ACTIVITY);
        boolean save = this.save(projectInspectRouteConf);
        projectInspectRoutePointConfService.saveOrUpdateRoutePointRel(projectInspectRouteConf.getInspectRouteId(), vo.getPointIdArr(), vo.getIsSort());
        return save;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateInspectRoute(ProjectInspectRouteConfVo vo) {
        ProjectInspectRouteConf projectInspectRouteConf = new ProjectInspectRouteConf();
        BeanUtil.copyProperties(vo, projectInspectRouteConf);
        boolean updateById = this.updateById(projectInspectRouteConf);
        projectInspectRoutePointConfService.saveOrUpdateRoutePointRel(vo.getInspectRouteId(), vo.getPointIdArr(), vo.getIsSort());
        return updateById;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeInspectRoute(String inspectorRouteId) {
        projectInspectRoutePointConfService.removeRoutePointRel(inspectorRouteId);
        return this.remove(new QueryWrapper<ProjectInspectRouteConf>().lambda().eq(ProjectInspectRouteConf::getInspectRouteId, inspectorRouteId));
    }

}