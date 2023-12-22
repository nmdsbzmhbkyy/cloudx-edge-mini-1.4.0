package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.estate.entity.ProjectEntryExitLane;
import com.aurine.cloudx.estate.entity.ProjectVehiclesEntryExit;
import com.aurine.cloudx.estate.mapper.ProjectVehiclesEntryExitMapper;
import com.aurine.cloudx.estate.service.ProjectEntryExitLaneService;
import com.aurine.cloudx.estate.service.ProjectVehiclesEntryExitService;
import com.aurine.cloudx.estate.vo.ProjectVehiclesEntryExitTreeVo;
import com.aurine.cloudx.estate.vo.ProjectVehiclesEntryExitVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 车辆出入口信息表(ProjectVehiclesEntryExit)表服务实现类
 *
 * @author 王良俊
 * @since 2020-08-17 10:08:53
 */
@Service
public class ProjectVehiclesEntryExitServiceImpl extends ServiceImpl<ProjectVehiclesEntryExitMapper, ProjectVehiclesEntryExit>
        implements ProjectVehiclesEntryExitService {

    @Autowired
    ProjectVehiclesEntryExitMapper projectVehiclesEntryExitMapper;
    @Autowired
    ProjectEntryExitLaneService projectEntryExitLaneService;

    @Override
    public ProjectVehiclesEntryExitVo getByEntryId(String entryId) {
        List<ProjectVehiclesEntryExit> entryExitList = this.list(new QueryWrapper<ProjectVehiclesEntryExit>().lambda()
                .eq(ProjectVehiclesEntryExit::getEntryId, entryId));
        if (CollUtil.isNotEmpty(entryExitList)) {
            ProjectVehiclesEntryExitVo entryExitVo = new ProjectVehiclesEntryExitVo();
            BeanUtil.copyProperties(entryExitList.get(0), entryExitVo);
            List<ProjectEntryExitLane> entryExitLanes = projectEntryExitLaneService.listByParkId(entryExitVo.getParkId(), entryExitVo.getEntryId());
            List<String> laneIdList = entryExitLanes.stream().map(ProjectEntryExitLane::getLaneId).collect(Collectors.toList());
            entryExitVo.setLaneIdArr(laneIdList.toArray(new String[laneIdList.size()]));
            return entryExitVo;
        }
        return null;
    }

    @Override
    public List<ProjectVehiclesEntryExitTreeVo> getEntryExitTreeByParkId(String parkId) {
        ProjectVehiclesEntryExitTreeVo treeVo = new ProjectVehiclesEntryExitTreeVo();
        List<ProjectVehiclesEntryExit> entryExitList = this.list(new QueryWrapper<ProjectVehiclesEntryExit>().lambda()
                .eq(ProjectVehiclesEntryExit::getParkId, parkId));
        treeVo.setName("出入口");
        treeVo.setLevel("1");
        treeVo.setId("1");
        List<ProjectVehiclesEntryExitTreeVo> childrenList = new ArrayList<>();
        entryExitList.forEach(entryExit -> {
            ProjectVehiclesEntryExitTreeVo child = new ProjectVehiclesEntryExitTreeVo();
            child.setName(entryExit.getEntryName());
            child.setLevel("2");
            child.setId(entryExit.getEntryId());
            childrenList.add(child);
        });
        treeVo.setChildren(childrenList);
        List<ProjectVehiclesEntryExitTreeVo> treeVoList = new ArrayList<>();
        treeVoList.add(treeVo);
        return treeVoList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveEntryExit(ProjectVehiclesEntryExitVo entryExitVo) {
        if (this.haveSameName(entryExitVo)) {
            throw new RuntimeException("出入口名称已存在！");
        }
        String[] laneIdArr = entryExitVo.getLaneIdArr();
        ProjectVehiclesEntryExit entryExit = new ProjectVehiclesEntryExit();
        BeanUtil.copyProperties(entryExitVo, entryExit);
        boolean save = this.save(entryExit);
        projectEntryExitLaneService.updateEntryExitLaneRel(laneIdArr, entryExit.getEntryId());
        return save;
    }

    @Override
    public boolean updateEntryExit(ProjectVehiclesEntryExitVo entryExitVo) {
        if (this.haveSameName(entryExitVo)) {
            throw new RuntimeException("出入口名称已存在！");
        }
        String[] laneIdArr = entryExitVo.getLaneIdArr();
        projectEntryExitLaneService.updateEntryExitLaneRel(laneIdArr, entryExitVo.getEntryId());
        ProjectVehiclesEntryExit entryExit = new ProjectVehiclesEntryExit();
        BeanUtil.copyProperties(entryExitVo, entryExit);
        return this.updateById(entryExit);
    }

    /**
     * 判断是否存在同名出入口
     *
     * @param entryExitVo
     * @return
     * @author: 王伟
     * @since：2020-09-27
     */
    private boolean haveSameName(ProjectVehiclesEntryExitVo entryExitVo) {
        int count = this.count(new QueryWrapper<ProjectVehiclesEntryExit>().lambda()
                .eq(ProjectVehiclesEntryExit::getEntryName, entryExitVo.getEntryName())
                .eq(ProjectVehiclesEntryExit::getParkId,entryExitVo.getParkId())
                .notLike(StringUtil.isNotEmpty(entryExitVo.getEntryId()), ProjectVehiclesEntryExit::getEntryId, entryExitVo.getEntryId()));

        return count >= 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeEntryExit(String entryId) {
        projectEntryExitLaneService.removeEntryExitLaneRel(entryId);
        return this.removeById(entryId);
    }

}