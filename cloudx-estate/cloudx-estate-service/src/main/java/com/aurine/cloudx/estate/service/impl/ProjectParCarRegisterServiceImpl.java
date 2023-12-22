package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.ProjectCarInfo;
import com.aurine.cloudx.estate.entity.ProjectParCarRegister;
import com.aurine.cloudx.estate.entity.ProjectParkingPlace;
import com.aurine.cloudx.estate.entity.ProjectPersonInfo;
import com.aurine.cloudx.estate.mapper.ProjectParCarRegisterMapper;
import com.aurine.cloudx.estate.service.ProjectCarInfoService;
import com.aurine.cloudx.estate.service.ProjectParCarRegisterService;
import com.aurine.cloudx.estate.service.ProjectParkingPlaceService;
import com.aurine.cloudx.estate.service.ProjectPersonInfoService;
import com.aurine.cloudx.estate.vo.ProjectParCarRegisterRecordVo;
import com.aurine.cloudx.estate.vo.ProjectParCarRegisterSeachConditionVo;
import com.aurine.cloudx.estate.vo.ProjectParCarRegisterVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 车辆登记
 *
 * @author 王伟
 * @date 2020-07-08 14:10:32
 */
@Service
public class ProjectParCarRegisterServiceImpl extends ServiceImpl<ProjectParCarRegisterMapper, ProjectParCarRegister> implements ProjectParCarRegisterService {

    @Resource
    ProjectPersonInfoService projectPersonInfoService;
    @Resource
    ProjectCarInfoService projectCarInfoService;
    @Resource
    ProjectParkingPlaceService projectParkingPlaceService;

    /**
     * 分页查询信息
     *
     * @param page
     * @param searchConditionVo
     * @return
     */
    @Override
    public Page<ProjectParCarRegisterRecordVo> pageCarRegister(Page<ProjectParCarRegisterRecordVo> page, ProjectParCarRegisterSeachConditionVo searchConditionVo) {
        return this.baseMapper.pageCarRegister(page, searchConditionVo, ProjectContextHolder.getProjectId());
    }


    /**
     * 更新车辆、以及新的人员信息
     *
     * @param parCarRegisterVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateCarRegister(ProjectParCarRegisterVo parCarRegisterVo) {
        // 更新人员信息
        ProjectPersonInfo personInfo = new ProjectPersonInfo();
        personInfo.setPersonId(parCarRegisterVo.getPersonId());
        personInfo.setPersonName(parCarRegisterVo.getPersonName());
        personInfo.setTelephone(parCarRegisterVo.getTelephone());
        projectPersonInfoService.updateById(personInfo);

        // 更新车位信息
        ProjectParkingPlace projectParkingPlace = new ProjectParkingPlace();
        projectParkingPlace.setPlaceId(parCarRegisterVo.getParkPlaceId());
        projectParkingPlace.setPersonName(parCarRegisterVo.getPersonName());
        projectParkingPlaceService.updateById(projectParkingPlace);

        // 更新车辆信息
        ProjectCarInfo car = new ProjectCarInfo();
        BeanUtils.copyProperties(parCarRegisterVo, car);
        projectCarInfoService.updateById(car);

        // 更新登记信息（车牌号）
        ProjectParCarRegister projectParCarRegister = new ProjectParCarRegister();
        projectParCarRegister.setRegisterId(parCarRegisterVo.getRegisterId());
        projectParCarRegister.setPlateNumber(parCarRegisterVo.getPlateNumber().toUpperCase());
        return this.updateById(projectParCarRegister);
    }


    /**
     * 通过id获取注册信息VO,包含车辆信息、人员信息、车位信息、缴费信息等
     *
     * @param id
     * @return
     */
    @Override
    public ProjectParCarRegisterVo getVo(String id) {
        ProjectParCarRegisterVo vo = new ProjectParCarRegisterVo();

        ProjectParCarRegister carRegPo = getById(id);//注册对象、
        ProjectCarInfo carInfo = projectCarInfoService.getCar(carRegPo.getCarUid());// 车辆信息
        ProjectPersonInfo personInfo = projectPersonInfoService.getById(carInfo.getPersonId());//人员信息
        BeanUtils.copyProperties(carRegPo, vo);
        BeanUtils.copyProperties(carInfo, vo);
        ProjectParkingPlace projectParkingPlace = projectParkingPlaceService.getById(vo.getParkPlaceId());
        BeanUtils.copyProperties(projectParkingPlace, vo);
        vo.setPersonName(personInfo.getPersonName());
        vo.setTelephone(personInfo.getTelephone());
        //这里remark会被上面的personInfo中的remark覆盖导致车辆信息的remark丢失
        vo.setRemark(carInfo.getRemark());

        return vo;
    }

    @Override
    public boolean checkHasRegister(String placeId) {
        List<ProjectParCarRegister> parCarRegisterList = this.list(new QueryWrapper<ProjectParCarRegister>().lambda().eq(ProjectParCarRegister::getParkPlaceId, placeId));
        return parCarRegisterList.size() > 0;
    }

}
