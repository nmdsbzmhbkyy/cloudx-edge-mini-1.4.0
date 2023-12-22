package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.estate.entity.ProjectParCarRegister;
import com.aurine.cloudx.estate.entity.ProjectPersonInfo;
import com.aurine.cloudx.estate.service.ProjectParkEntranceHisService;
import com.aurine.cloudx.estate.service.ProjectPersonInfoService;
import com.aurine.cloudx.estate.vo.ProjectCarInfoVo;
import com.aurine.cloudx.estate.vo.ProjectParCarRegisterRecordVo;
import com.aurine.cloudx.estate.vo.ProjectParCarRegisterVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aurine.cloudx.estate.entity.ProjectCarInfo;
import com.aurine.cloudx.estate.mapper.ProjectCarInfoMapper;
import com.aurine.cloudx.estate.service.ProjectCarInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 车辆信息
 *
 * @author 王伟
 * @date 2020-07-08 14:33:58
 */
@Service
public class ProjectCarInfoServiceImpl extends ServiceImpl<ProjectCarInfoMapper, ProjectCarInfo> implements ProjectCarInfoService {
    @Autowired
    ProjectPersonInfoService projectPersonInfoService;
    @Autowired
    ProjectParkEntranceHisService projectParkEntranceHisService;
    @Override
    public ProjectCarInfo getCar(String carUid) {
        if (StrUtil.isBlank(carUid)) {
            return null;
        }
        List<ProjectCarInfo> carList = list(new QueryWrapper<ProjectCarInfo>().lambda().eq(ProjectCarInfo::getCarUid, carUid));

        if (CollectionUtil.isNotEmpty(carList)) {
            return carList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public ProjectCarInfo getCarByPlateNumber(String plateNumber) {
        if (StrUtil.isBlank(plateNumber)) {
            return null;
        }
        List<ProjectCarInfo> carList = list(new QueryWrapper<ProjectCarInfo>().lambda().eq(ProjectCarInfo::getPlateNumber, plateNumber));
        if (CollectionUtil.isNotEmpty(carList)) {
            return carList.get(0);
        } else {
            return null;
        }
    }

    /**
     * 保存车辆信息
     *
     * @param carInfo
     * @return
     */
    @Override
    public boolean saveCar(ProjectCarInfo carInfo) {
        if (getCar(carInfo.getPlateNumber()) == null) {
            return this.save(carInfo);
        } else {
            throw new RuntimeException("车牌号：" + carInfo.getPlateNumber() + " 已存在，请勿重复添加");
        }

    }

    /**
     * 根据车牌号，获取VO,显示车辆以及所属人员的信息
     *
     * @param plateNumber
     * @return
     */
    @Override
    public ProjectCarInfoVo getVoByPlateNumber(String plateNumber) {
        ProjectCarInfoVo carInfoVo = new ProjectCarInfoVo();
        String copyPlateNumber = plateNumber.replace(plateNumber.substring(0, 2), plateNumber.substring(0, 2) + " ");
        List<ProjectCarInfo> carInfoList = this.list(new QueryWrapper<ProjectCarInfo>().lambda()
                .eq(ProjectCarInfo::getPlateNumber, plateNumber)
                .or().eq(ProjectCarInfo::getPlateNumber, copyPlateNumber));
        ProjectCarInfo carInfoPo = null;
        if (CollUtil.isNotEmpty(carInfoList)) {
            carInfoPo = getCar(carInfoList.get(0).getCarUid());
        }
        if (carInfoPo != null && StrUtil.isNotBlank(carInfoPo.getPersonId())) {
            ProjectPersonInfo person = projectPersonInfoService.getById(carInfoPo.getPersonId());
            BeanUtils.copyProperties(carInfoPo, carInfoVo);
            BeanUtils.copyProperties(person, carInfoVo);
            return carInfoVo;
        }
        return null;
    }

    @Override
    public List<ProjectParCarRegisterVo> initParCarRegisterCarUid(List<ProjectParCarRegisterVo> parCarRegisterVoList) {

        List<String> plateNumberList = parCarRegisterVoList.stream().map(ProjectParCarRegisterVo::getPlateNumber).collect(Collectors.toList());
        List<ProjectCarInfo> hasExisCarInfoList = new ArrayList<>();
        if (CollUtil.isNotEmpty(plateNumberList)) {
            // 这是在进行新住户添加前就已经存在的住户列表
            hasExisCarInfoList = this.list(new QueryWrapper<ProjectCarInfo>().lambda().in(ProjectCarInfo::getPlateNumber, plateNumberList));
            // 这里获取到已经存在住户的手机号
            List<String> existPlateNumberList = hasExisCarInfoList.stream().map(ProjectCarInfo::getPlateNumber).collect(Collectors.toList());
            // 这里获取到新住户迁入的房屋住户关系列表
            List<ProjectParCarRegisterVo> notExistPersonVoList = parCarRegisterVoList.stream().filter(parCarRegisterVo -> {
                return !existPlateNumberList.contains(parCarRegisterVo.getPlateNumber());
            }).collect(Collectors.toList());

            List<ProjectCarInfo> beAddCarInfoList = new ArrayList<>();
            for (ProjectParCarRegisterVo parCarRegisterVo : notExistPersonVoList) {
                ProjectCarInfo carInfo = new ProjectCarInfo();
                BeanUtil.copyProperties(parCarRegisterVo, carInfo);
                beAddCarInfoList.add(carInfo);
            }
            /*this.saveFromSystemBatch(beAddCarInfoList);

            plateNumberList.addAll(beAddCarInfoList);
            for (plateNumberList personInfo : plateNumberList) {
                // 这里获取到的是已经存在的住户（要将住户id存入住户房屋关系列表）
                parCarRegisterVoList.forEach(parCarRegisterVo -> {
                    if (StrUtil.isBlank(parCarRegisterVo.getPersonId()) && personInfo.getTelephone().equals(parCarRegisterVo.getTelephone())) {
                        parCarRegisterVo.setPersonId(personInfo.getPersonId());
                    }
                });
            }
            return parCarRegisterVoList;*/
        }
        return null;
    }

    @Override
    public List<ProjectParCarRegisterVo> saveOrUpdateCarByRegisterVoList(List<ProjectParCarRegisterVo> parCarRegisterVoList) {
        List<String> plateNumberList = parCarRegisterVoList.stream().map(ProjectParCarRegisterVo::getPlateNumber)
                .collect(Collectors.toList());
        List<ProjectCarInfo> carInfoListByPlateNumber = this.list(new QueryWrapper<ProjectCarInfo>().lambda()
                .in(ProjectCarInfo::getPlateNumber, plateNumberList));
        List<String> hasExistPlateNumber = carInfoListByPlateNumber.stream().map(ProjectCarInfo::getPlateNumber)
                .collect(Collectors.toList());
        // 这里分离出未进行登记过的车牌号（数据库中还没有车辆信息的车辆）
        Stream<ProjectParCarRegisterVo> notExistCarInfoVoList = parCarRegisterVoList.stream().filter(parCarRegisterVo -> {
            return !hasExistPlateNumber.contains(parCarRegisterVo.getPlateNumber());
        });

        List<ProjectCarInfo> beSaveCarInfoList = new ArrayList<>();
        notExistCarInfoVoList.forEach(parCarRegisterVo -> {
            ProjectCarInfo carInfo = new ProjectCarInfo();
            BeanUtils.copyProperties(parCarRegisterVo, carInfo);
            beSaveCarInfoList.add(carInfo);
        });
        carInfoListByPlateNumber.addAll(beSaveCarInfoList);
        if (CollUtil.isNotEmpty(beSaveCarInfoList)) {
            // 对车辆进行更新或保存
            this.saveOrUpdateBatch(carInfoListByPlateNumber);
        }
        // 根据车牌号把车辆uid存入车辆登记对象中
        parCarRegisterVoList.forEach(parCarRegisterVo -> {
            for (ProjectCarInfo carInfo : carInfoListByPlateNumber) {
                if (parCarRegisterVo.getPlateNumber().equals(carInfo.getPlateNumber())) {
                    parCarRegisterVo.setCarUid(carInfo.getCarUid());
                    break;
                }
            }
        });
        return parCarRegisterVoList;
    }

}
