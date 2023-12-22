package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.PlaceRelTypeEnum;
import com.aurine.cloudx.estate.entity.ProjectParCarRegister;
import com.aurine.cloudx.estate.entity.ProjectParkingPlace;
import com.aurine.cloudx.estate.mapper.ProjectParCarRegisterMapper;
import com.aurine.cloudx.estate.mapper.ProjectParkingPlaceMapper;
import com.aurine.cloudx.estate.service.ProjectParCarRegisterService;
import com.aurine.cloudx.estate.service.ProjectParkingPlaceService;
import com.aurine.cloudx.estate.vo.ProjectParkingPlaceAssignmentVo;
import com.aurine.cloudx.estate.vo.ProjectParkingPlaceHisVo;
import com.aurine.cloudx.estate.vo.ProjectParkingPlaceVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 车位
 *
 * @author 王伟
 * @date 2020-05-08 10:24:42
 */
@Service
@AllArgsConstructor
public class ProjectParkingPlaceServiceImpl extends ServiceImpl<ProjectParkingPlaceMapper, ProjectParkingPlace> implements ProjectParkingPlaceService {

    private final ProjectParkingPlaceMapper projectParkingPlaceMapper;
    private final ProjectParCarRegisterMapper projectParCarRegisterMapper;

    @Override
    public IPage<ProjectParkingPlaceVo> findPage(IPage<ProjectParkingPlace> page, ProjectParkingPlaceVo projectParkingPlace) {
        return projectParkingPlaceMapper.select(page, ProjectContextHolder.getProjectId(), projectParkingPlace.getPlaceName(),
                projectParkingPlace.getIsUse(), projectParkingPlace.getParkId());
    }

    @Override
    public boolean add(ProjectParkingPlace projectParkingPlace) {

        boolean isExits = this.checkPlaceNameExist(projectParkingPlace.getPlaceName(), projectParkingPlace.getParkRegionId());

        if (isExits) {
            throw new RuntimeException("车位号 " + projectParkingPlace.getPlaceName() + " 在本区域中已存在，请更换车位号后再试");
        }

        return this.save(projectParkingPlace);

//        //获取车位号进行判断
//        QueryWrapper<ProjectParkingPlace> query = new QueryWrapper<>();
//        query.eq("placeName", projectParkingPlace.getPlaceName());
//        ProjectParkingPlace result = baseMapper.selectOne(query);
//        if (result != null) {
//            throw new RuntimeException("车位号已存在");
//        }
//        boolean complete = baseMapper.insert(projectParkingPlace) == 1;
//        if (!complete) {
//            throw new RuntimeException("未知错误，请联系管理员");
//        }
//        return complete;
    }

    @Override
    public List<ProjectParkingPlaceAssignmentVo> getParkingAttribution(String placeId) {

        ArrayList<ProjectParkingPlaceAssignmentVo> projectParkingPlaceAssignmentVos = new ArrayList<>();
        ProjectParkingPlaceAssignmentVo parkingAttribution = projectParkingPlaceMapper.getParkingOwnershipInformation(ProjectContextHolder.getProjectId(), placeId);
        if (parkingAttribution != null) {
            projectParkingPlaceAssignmentVos.add(parkingAttribution);
        }
        return projectParkingPlaceAssignmentVos;
    }

    @Override
    public List<ProjectParkingPlaceHisVo> getParkUseHis(String placeId) {
        return projectParkingPlaceMapper.getParkingHistory(ProjectContextHolder.getProjectId(), placeId);
    }

    @Override
    public boolean checkPlaceNameExist(String placeName, String parkRegionId) {
        int num = this.count(new QueryWrapper<ProjectParkingPlace>().lambda()
                .eq(ProjectParkingPlace::getPlaceName, placeName)
                .eq(ProjectParkingPlace::getParkRegionId, parkRegionId)
        );
        return num != 0;
    }

    @Override
    public boolean checkPlaceNameExist(String placeName, String parkRegionId, String placeId) {
        int num = this.count(new QueryWrapper<ProjectParkingPlace>().lambda()
                .eq(ProjectParkingPlace::getPlaceName, placeName)
                .eq(ProjectParkingPlace::getParkRegionId, parkRegionId)
                .notLike(StringUtils.isNotEmpty(placeId), ProjectParkingPlace::getPlaceId, placeId)
        );
        return num != 0;
    }

    @Override
    public boolean checkPlaceNameExist(List<String> placeNameList, String parkRegionId) {
        for (String placeName : placeNameList) {
            int num = this.count(new QueryWrapper<ProjectParkingPlace>().lambda()
                    .eq(ProjectParkingPlace::getPlaceName, placeName)
                    .eq(ProjectParkingPlace::getParkRegionId, parkRegionId)
            );
            if (num != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean savePlaceBatch(ProjectParkingPlaceVo parkingPlaceVo) {
        List<String> placeNameList = parkingPlaceVo.getPlaceNameList();
        List<ProjectParkingPlace> placeList = new ArrayList<>();
        for (String placeName : placeNameList) {
            boolean exist = this.checkPlaceNameExist(placeName, parkingPlaceVo.getParkRegionId());
            if (exist) {
                continue;
            }
            ProjectParkingPlace projectParkingPlace = new ProjectParkingPlace();
            BeanUtil.copyProperties(parkingPlaceVo, projectParkingPlace);
            projectParkingPlace.setPlaceName(placeName);
            placeList.add(projectParkingPlace);
        }
        return this.saveBatch(placeList);
    }

    @Override
    public boolean update(ProjectParkingPlace projectParkingPlace) {
        //核对是否存在同名车位

        boolean isExist = checkPlaceNameExist(projectParkingPlace.getPlaceName(), projectParkingPlace.getParkRegionId(), projectParkingPlace.getPlaceId());

        if (isExist) {
            throw new RuntimeException("车位号 " + projectParkingPlace.getPlaceName() + " 在本区域中已存在，请更换车位号后再试");
        }
        return updateById(projectParkingPlace);
    }

    @Override
    public Map<String, Object> getCurrentListAndPuidMap(String placeId) {
        Map<String, Object> map = new HashMap<>();
        List<ProjectParkingPlace> list = this.list(new QueryWrapper<ProjectParkingPlace>().lambda()
                .eq(ProjectParkingPlace::getPlaceId, placeId));
        if (CollUtil.isNotEmpty(list)) {
            String parkRegionId = list.get(0).getParkRegionId();
            List<ProjectParkingPlace> parkingPlaceList = this.list(new QueryWrapper<ProjectParkingPlace>().lambda()
                    .eq(ProjectParkingPlace::getParkRegionId, parkRegionId));
            map.put("parkRegionId", parkRegionId);
            map.put("list", parkingPlaceList);
        }
        return map;
    }

    @Override
    public boolean cleanParkingPlace(String placeId) {
        ProjectParkingPlace place = getById(placeId);
        place.setPersonName("");
        place.setPersonId("");
        place.setRuleId("");
//        place.setRelType(PlaceRelTypeEnum.PUBLIC.code);//public按照规则，同时也是空置的定义

        return this.updateById(place);
    }

    @Override
    public int getFreePublicParkingSpaceNum(String parkId) {
        return projectParkingPlaceMapper.getPublicFreePlaceNum(parkId);
    }

    @Override
    public boolean cancelParkingPlace(String placeId) {
        ProjectParkingPlace place = getById(placeId);
        if (place.getRelType().equals(PlaceRelTypeEnum.PUBLIC.code)) {
            place.setPersonId("");
            place.setPersonName("");
            /**
             * 注销公共车位同时清空第三方ID
             * @since 2020-08-14
             * @author: 王伟
             */
            place.setPlaceCode("");
            place.setPersonCode("");
        }
        place.setRuleId("");
        place.setPersonCode("");
        return this.updateById(place);
    }

    @Override
    public List<String> listParkRegionIdByPersonId(String parkId, String personId, String relType, String placeId) {
        List<String> regionIdList = new ArrayList<>();
        List<ProjectParCarRegister> parCarRegisterList = projectParCarRegisterMapper.selectList(Wrappers.emptyWrapper());
        List<String> placeIdList = new ArrayList<>();
        if (parCarRegisterList != null) {
            if (StrUtil.isNotBlank(placeId) && !"undefined".equals(placeId)) {
                //在修改的时候原本登记的车位也要被显示出来
                placeIdList = parCarRegisterList.stream()
                        .filter(projectParCarRegister -> !placeId.equals(projectParCarRegister.getParkPlaceId()))
                        .map(ProjectParCarRegister::getParkPlaceId)
                        .collect(Collectors.toList());
            }
            List<ProjectParkingPlace> placeList = this.list(new QueryWrapper<ProjectParkingPlace>().lambda()
                    .eq(!"0".equals(relType), ProjectParkingPlace::getPersonId, personId)
                    .eq(ProjectParkingPlace::getRelType, relType)
                    .eq(ProjectParkingPlace::getParkId, parkId)
                    .notIn(CollUtil.isNotEmpty(placeIdList), ProjectParkingPlace::getPlaceId, placeIdList));
            if (CollUtil.isNotEmpty(placeList)) {
                regionIdList = placeList.stream().map(ProjectParkingPlace::getParkRegionId).collect(Collectors.toList());
                //去重
                regionIdList = regionIdList.stream().distinct().collect(Collectors.toList());
            }
        }
        return regionIdList;
    }

    @Override
    public List<String> getPlaceRelTypeByPersonId(String parkId, String personId, List<String> removePlaceIdList) {
        Set<String> relTypeSet = new HashSet<>();
        relTypeSet.add("0");
        if (StrUtil.isNotBlank(personId) && removePlaceIdList != null) {
            List<ProjectParkingPlace> list = this.list(new QueryWrapper<ProjectParkingPlace>().lambda()
                    .eq(ProjectParkingPlace::getParkId, parkId)
                    .eq(ProjectParkingPlace::getPersonId, personId)
                    .notIn(CollUtil.isNotEmpty(removePlaceIdList), ProjectParkingPlace::getPlaceId, removePlaceIdList)
                    .orderByDesc(ProjectParkingPlace::getRelType));
            if (CollUtil.isNotEmpty(list)) {
                list.forEach(projectParkingPlace -> {
                    relTypeSet.add(projectParkingPlace.getRelType());
                });
            }
        }
        return Arrays.asList(relTypeSet.toArray(new String[0]));
    }

    @Override
    public List<ProjectParkingPlace> listParkPlaceByRelType(String parkRegionId, String personId, String relType, String placeId) {
        if (StrUtil.isBlank(parkRegionId) || StrUtil.isBlank(personId) || StrUtil.isBlank(relType)) {
            return new ArrayList<>();
        }
        List<ProjectParCarRegister> parCarRegisterList = projectParCarRegisterMapper.selectList(Wrappers.emptyWrapper());
        List<String> placeIdList = new ArrayList<>();
        if (parCarRegisterList != null) {
            if (StrUtil.isNotBlank(placeId) && !"undefined".equals(placeId)) {
                //在修改的时候原本登记的车位也要被显示出来(前端直接显示文字了这里无所谓了)
                placeIdList = parCarRegisterList.stream()
                        .filter(projectParCarRegister -> !placeId.equals(projectParCarRegister.getParkPlaceId()))
                        .map(ProjectParCarRegister::getParkPlaceId)
                        .collect(Collectors.toList());
            } else {
                placeIdList = parCarRegisterList.stream()
                        .map(ProjectParCarRegister::getParkPlaceId)
                        .collect(Collectors.toList());
            }
            return this.list(new QueryWrapper<ProjectParkingPlace>().lambda()
                    .eq(ProjectParkingPlace::getPersonId, personId)
                    .eq(ProjectParkingPlace::getParkRegionId, parkRegionId)
                    .eq(ProjectParkingPlace::getRelType, relType)
                    .notIn(CollUtil.isNotEmpty(placeIdList), ProjectParkingPlace::getPlaceId, placeIdList));
        }
        return new ArrayList<>();
    }

    @Override
    public boolean checkRuleUseStatus(String ruleId) {
        List<ProjectParkingPlace> parkingPlaceList = this.list(new QueryWrapper<ProjectParkingPlace>().lambda().eq(ProjectParkingPlace::getRuleId, ruleId));
        return parkingPlaceList.size() > 0;
    }
    @Override
    public List<ProjectParkingPlace> getByPersonId(String personId) {
        List<ProjectParkingPlace> parkingPlaceList = this.list(new QueryWrapper<ProjectParkingPlace>().lambda().eq(ProjectParkingPlace::getPersonId, personId));
        return parkingPlaceList;
    }

    @Override
    public int countPlace() {
        return this.count();
    }

    @Override
    public List<ProjectParkingPlace> listByParkRegionId(String parkRegionId) {
        return projectParkingPlaceMapper.listByParkRegionId(parkRegionId);
    }


}
