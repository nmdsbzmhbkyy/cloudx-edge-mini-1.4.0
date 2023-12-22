package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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
            int parkNum = projectParkRegion.getParkNum();
            if (parkNum > 0) {
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
        List<ProjectParkRegion> parkRegionList = this.list(new QueryWrapper<ProjectParkRegion>()
                .lambda().eq(ProjectParkRegion::getParkRegionId, po.getParkRegionId()));
        // 这里获取到这个区域的车位列表（根据创建时间降序排列）
        List<ProjectParkingPlace> placeList = projectParkingPlaceServiceImpl.list(new QueryWrapper<ProjectParkingPlace>()
                .lambda().eq(ProjectParkingPlace::getParkRegionId, po.getParkRegionId()));
        if (CollUtil.isNotEmpty(parkRegionList)) {
            ProjectParkRegion projectParkRegion = parkRegionList.get(0);
            Integer currentParkNum = projectParkRegion.getParkNum();
            int addNum = updateParkNum - currentParkNum;
            List<String> placeIdList = new ArrayList<>();
            // 判断是新增车位还是减少车位
            if (addNum < 0) {
                // 用于记录实际可以删除的车位数
                for (ProjectParkingPlace projectParkingPlace : placeList) {
                    if (StrUtil.isBlank(projectParkingPlace.getPersonId())) {
                        placeIdList.add(projectParkingPlace.getPlaceId());
                    }
                    // 如果可操作的车位数已经和要被删除的一样多了就跳出循环
                    if (placeIdList.size() == -addNum) {
                        break;
                    }
                }
                if (CollUtil.isNotEmpty(placeIdList)) {
                    projectParkingPlaceServiceImpl.remove(new QueryWrapper<ProjectParkingPlace>()
                            .lambda().in(ProjectParkingPlace::getPlaceId, placeIdList));
                }
                if (placeIdList.size() != -addNum) {
                    message = "已更新车位数为" + (currentParkNum - placeIdList.size()) + "个,由于车位已被使用所以无法删除部分车位";
                }
            } else {
                List<ProjectParkingPlace> addPlaceList = new ArrayList<>();
                for (int i = 0; i < addNum; i++) {
                    ProjectParkingPlace projectParkingPlace = new ProjectParkingPlace();
                    BeanUtil.copyProperties(po, projectParkingPlace);
                    projectParkingPlace.setRelType(PlaceRelTypeEnum.PUBLIC.code);
                    projectParkingPlace.setPlaceName("");
                    addPlaceList.add(projectParkingPlace);
                }
                projectParkingPlaceServiceImpl.saveBatch(addPlaceList);
            }
        }

        Integer currentParkNum = projectParkingPlaceServiceImpl.list(new QueryWrapper<ProjectParkingPlace>()
                .lambda().eq(ProjectParkingPlace::getParkRegionId, po.getParkRegionId()).orderByDesc(ProjectParkingPlace::getSeq)).size();
        po.setParkNum(currentParkNum);
        if (this.updateById(po)) {
            return R.ok(message);
        } else {
            return R.failed("修改失败");
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
