package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.estate.entity.ProjectEntryExitLane;
import com.aurine.cloudx.estate.mapper.ProjectEntryExitLaneMapper;
import com.aurine.cloudx.estate.service.ProjectEntryExitLaneService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 出入口车道信息(ProjectEntryExitLane)表服务实现类
 *
 * @author 王良俊
 * @since 2020-08-17 11:58:43
 */
@Service
public class ProjectEntryExitLaneServiceImpl extends ServiceImpl<ProjectEntryExitLaneMapper, ProjectEntryExitLane> implements ProjectEntryExitLaneService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateEntryExitLaneRel(String[] laneIdArr, String entryId) {
        // 这里先取消原有的出入口和车道的关联
        this.removeEntryExitLaneRel(entryId);
        List<ProjectEntryExitLane> laneList = new ArrayList<>();
        Arrays.stream(laneIdArr).forEach(laneId -> {
            ProjectEntryExitLane entryExitLane = new ProjectEntryExitLane();
            entryExitLane.setLaneId(laneId);
            entryExitLane.setEntryId(entryId);
            laneList.add(entryExitLane);
        });
        return this.updateBatchById(laneList);
    }

    @Override
    public boolean removeEntryExitLaneRel(String entryId) {
        List<ProjectEntryExitLane> entryExitLaneList = this.list(new QueryWrapper<ProjectEntryExitLane>().lambda()
                .eq(ProjectEntryExitLane::getEntryId, entryId));
        if (CollUtil.isNotEmpty(entryExitLaneList)) {
            entryExitLaneList.forEach(entryExitLane -> {
                entryExitLane.setEntryId("");
            });
        }
        return this.updateBatchById(entryExitLaneList);
    }

    @Override
    public List<ProjectEntryExitLane> listByParkId(String parkId) {
        return this.list(new QueryWrapper<ProjectEntryExitLane>().lambda()
                .eq(ProjectEntryExitLane::getParkId, parkId));
    }

    @Override
    public List<ProjectEntryExitLane> listByParkId(String parkId, String entryId) {
        return this.list(new QueryWrapper<ProjectEntryExitLane>().lambda()
                .eq(ProjectEntryExitLane::getParkId, parkId).eq(ProjectEntryExitLane::getEntryId, entryId));
    }


}