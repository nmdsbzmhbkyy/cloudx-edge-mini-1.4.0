package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.ParkRegionIsPublicConstant;
import com.aurine.cloudx.estate.constant.enums.PlaceRelTypeEnum;
import com.aurine.cloudx.estate.entity.ProjectParkingPlace;
import com.aurine.cloudx.estate.service.ProjectParkingPlaceService;
import com.aurine.cloudx.estate.vo.ProjectParkRegionVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aurine.cloudx.estate.entity.ProjectParkRegion;
import com.aurine.cloudx.estate.mapper.ProjectParkRegionMapper;
import com.aurine.cloudx.estate.service.ProjectParkRegionService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 车位区域表
 *
 * @author 王良俊
 * @date 2020-07-07 11:00:29
 */
@Service
public class ProjectParkRegionServiceImpl extends ServiceImpl<ProjectParkRegionMapper, ProjectParkRegion> implements ProjectParkRegionService {

    @Resource
    ProjectParkRegionMapper projectParkRegionMapper;

    @Resource
    ProjectParkingPlaceService projectParkingPlaceServiceImpl;


    @Override
    public IPage<ProjectParkRegionVo> fetchList(IPage<ProjectParkRegionVo> page, ProjectParkRegionVo query) {
        return projectParkRegionMapper.selectPage(page, query);
    }

    @Override
    public boolean checkHasPublic(String parkId, String parkRegionId) {
        List<ProjectParkRegion> list = this.list(new LambdaQueryWrapper<ProjectParkRegion>().eq(ProjectParkRegion::getParkId, parkId)
                .notIn(StrUtil.isNotBlank(parkRegionId), ProjectParkRegion::getParkRegionId, parkRegionId)
                .eq(ProjectParkRegion::getIsPublic, ParkRegionIsPublicConstant.PUBLIC));
        return CollUtil.isNotEmpty(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveParkRegion(ProjectParkRegion projectParkRegion) {
        List<ProjectParkingPlace> parkingPlaceList = new ArrayList<>();
        boolean save = this.save(projectParkRegion);
        if (ParkRegionIsPublicConstant.PUBLIC.equals(projectParkRegion.getIsPublic())) {
            // 每个公共区域默认给1000个公共车位
            int parkNum = projectParkRegion.getParkNum() > 1000 ? projectParkRegion.getParkNum() : 1000;
            if (projectParkRegion.getParkNum() > 0) {
                for (int i = 1; i <= parkNum; i++) {
                    ProjectParkingPlace projectParkingPlace = new ProjectParkingPlace();
                    BeanUtil.copyProperties(projectParkRegion, projectParkingPlace);
                    projectParkingPlace.setPlaceName("");
                    projectParkingPlace.setRelType(PlaceRelTypeEnum.PUBLIC.code);
                    parkingPlaceList.add(projectParkingPlace);
                }
                projectParkingPlaceServiceImpl.saveBatch(parkingPlaceList);
            } else {
                return false;
            }
        }
        return save;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R updateParkRegion(ProjectParkRegion po) {
        String message = "修改成功";
        Integer updateParkNum = po.getParkNum();
        po.setParkNum(updateParkNum);
        if (this.updateById(po)) {
            return R.ok(message);
        } else {
            return R.failed("修改失败");
        }
    }

    @Override
    @Async
    public void initPublicParkingPlace(String parkId, Integer projectId) {
        List<ProjectParkRegion> regionList = this.list(new QueryWrapper<ProjectParkRegion>().lambda()
                .eq(ProjectParkRegion::getParkId, parkId)
                .eq(ProjectParkRegion::getIsPublic, "1"));
        if (CollUtil.isNotEmpty(regionList)) {
            ProjectParkRegion region = regionList.get(0);
            int count = projectParkingPlaceServiceImpl.count(new QueryWrapper<ProjectParkingPlace>().lambda()
                    .eq(ProjectParkingPlace::getParkRegionId, region.getParkRegionId()));
            if (count < 1000) {
                List<ProjectParkingPlace> addPlaceList = new ArrayList<>();
                int initParkNum = 1000 - count;
                for (int i = 0; i < initParkNum; i++) {
                    ProjectParkingPlace projectParkingPlace = new ProjectParkingPlace();
                    BeanUtil.copyProperties(region, projectParkingPlace);
                    projectParkingPlace.setPlaceId(UUID.randomUUID().toString().replaceAll("-", ""));
                    projectParkingPlace.setRelType(PlaceRelTypeEnum.PUBLIC.code);
                    projectParkingPlace.setPlaceName("");
                    addPlaceList.add(projectParkingPlace);
                }
                projectParkingPlaceServiceImpl.saveBatch(addPlaceList);
            }
        }
    }


    @Override
    public List<ProjectParkRegion> listPersonAttrParkingRegionByRelType(String parkId, String personId, String relType, String placeId) {
        List<ProjectParkRegion> regionList = new ArrayList<>();
        if (StrUtil.isBlank(personId) || StrUtil.isBlank(relType) || "0".equals(relType)) {
            if (StrUtil.isNotBlank(parkId)) {
                regionList = this.list(new QueryWrapper<ProjectParkRegion>().lambda()
                        .eq(ProjectParkRegion::getIsPublic, ParkRegionIsPublicConstant.PUBLIC).eq(ProjectParkRegion::getParkId, parkId));
            }
            return regionList;
        }
        List<String> regionIdList = projectParkingPlaceServiceImpl.listParkRegionIdByPersonId(parkId, personId, relType, placeId);
        if (CollUtil.isNotEmpty(regionIdList)) {
            regionList = this.list(new QueryWrapper<ProjectParkRegion>().lambda().in(ProjectParkRegion::getParkRegionId, regionIdList));
        }
        return regionList;
    }

    @Override
    public List<ProjectParkRegion> listByParkId(String parkId) {
        return projectParkRegionMapper.listByParkId(parkId);
    }

}
