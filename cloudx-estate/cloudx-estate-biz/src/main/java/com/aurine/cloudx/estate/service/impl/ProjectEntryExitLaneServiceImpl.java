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

import com.aurine.cloudx.estate.thirdparty.module.parking.factory.ParkingFactoryProducer;

/**
 * 出入口车道信息(ProjectEntryExitLane)表服务实现类
 *
 * @author 王良俊
 * @since 2020-08-17 11:58:43
 */
@Service
public class ProjectEntryExitLaneServiceImpl extends ServiceImpl<ProjectEntryExitLaneMapper, ProjectEntryExitLane> implements ProjectEntryExitLaneService {



    /**
     * 同步车道信息
     *
     * @param parkId
     * @return
     * @author: 王伟
     * @since 2020-08-25
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean syncLane(String parkId) {
        List<ProjectEntryExitLane> laneList = ParkingFactoryProducer.getFactory(parkId).getParkingService().getLaneList(parkId);
        List<ProjectEntryExitLane> oldLaneList = this.list(new QueryWrapper<ProjectEntryExitLane>().lambda().eq(ProjectEntryExitLane::getParkId, parkId));
        if (CollUtil.isNotEmpty(oldLaneList)) {
            boolean remove = true;
            boolean saveBatch = true;
            List<String> oldLaneCodeList = oldLaneList.stream().map(ProjectEntryExitLane::getLaneCode).collect(Collectors.toList());
            List<String> beRemoveLaneCodeList = new ArrayList<>(oldLaneCodeList);
            List<String> newLaneCodeList = laneList.stream().map(ProjectEntryExitLane::getLaneCode).collect(Collectors.toList());
            List<String> beAddLaneCodeList = new ArrayList<>(newLaneCodeList);
            beRemoveLaneCodeList.removeAll(newLaneCodeList);
            beAddLaneCodeList.removeAll(oldLaneCodeList);
            if (CollUtil.isNotEmpty(beRemoveLaneCodeList)) {
                remove = this.remove(new QueryWrapper<ProjectEntryExitLane>().lambda().eq(ProjectEntryExitLane::getParkId, parkId)
                        .in(ProjectEntryExitLane::getLaneCode, beAddLaneCodeList));
            }
            if (CollUtil.isNotEmpty(beAddLaneCodeList)) {
                // 存放要进行添加的车道对象列表
                List<ProjectEntryExitLane> list = new ArrayList<>();
                beAddLaneCodeList.forEach(laneCode -> {
                    List<ProjectEntryExitLane> beAddLaneList = laneList.stream().filter(lane -> {
                        return lane.getLaneCode().equals(laneCode);
                    }).collect(Collectors.toList());
                    list.add(beAddLaneList.get(0));
                });
                saveBatch = this.saveBatch(list);
            }
            return remove && saveBatch;
        }
        return this.saveBatch(laneList);
    }


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
