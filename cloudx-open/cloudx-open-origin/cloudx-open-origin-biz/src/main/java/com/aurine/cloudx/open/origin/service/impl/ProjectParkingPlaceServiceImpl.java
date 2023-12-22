package com.aurine.cloudx.open.origin.service.impl;

import com.aurine.cloudx.open.origin.entity.ProjectParkingPlace;
import com.aurine.cloudx.open.origin.mapper.ProjectParkingPlaceMapper;
import com.aurine.cloudx.open.origin.service.ProjectParkingPlaceService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 车位
 *
 * @author 王伟
 * @date 2020-05-08 10:24:42
 */
@Service
public class ProjectParkingPlaceServiceImpl extends ServiceImpl<ProjectParkingPlaceMapper, ProjectParkingPlace> implements ProjectParkingPlaceService {

    @Override
    public List<ProjectParkingPlace> getByPersonId(String personId) {
        List<ProjectParkingPlace> parkingPlaceList = this.list(new QueryWrapper<ProjectParkingPlace>().lambda().eq(ProjectParkingPlace::getPersonId, personId));
        return parkingPlaceList;
    }
}
