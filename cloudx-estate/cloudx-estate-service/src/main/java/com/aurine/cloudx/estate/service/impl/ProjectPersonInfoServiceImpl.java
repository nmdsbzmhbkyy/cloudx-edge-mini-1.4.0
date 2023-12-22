
package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.*;
import com.aurine.cloudx.estate.entity.ProjectCarPreRegister;
import com.aurine.cloudx.estate.entity.ProjectParCarRegister;
import com.aurine.cloudx.estate.entity.ProjectParkingPlace;
import com.aurine.cloudx.estate.entity.ProjectPersonInfo;
import com.aurine.cloudx.estate.mapper.ProjectPersonInfoMapper;
import com.aurine.cloudx.estate.service.ProjectHousePersonRelService;
import com.aurine.cloudx.estate.service.ProjectParCarRegisterService;
import com.aurine.cloudx.estate.service.ProjectParkingPlaceService;
import com.aurine.cloudx.estate.service.ProjectPersonInfoService;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.api.dto.CxUserDTO;
import com.pig4cloud.pigx.admin.api.entity.SysRole;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.admin.api.feign.RemoteRoleService;
import com.pig4cloud.pigx.admin.api.feign.RemoteUserService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 人员
 *
 * @author 王伟
 * @date 2020-05-11 09:12:50
 */
@Service
public class ProjectPersonInfoServiceImpl extends ServiceImpl<ProjectPersonInfoMapper, ProjectPersonInfo> implements ProjectPersonInfoService {

    @Resource
    private ProjectHousePersonRelService projectHousePersonRelService;
    @Resource
    private RemoteUserService remoteUserService;
    @Resource
    private RemoteRoleService remoteRoleService;
    @Resource
    private ProjectParkingPlaceService projectParkingPlaceService;
    @Resource
    private ProjectParCarRegisterService projectParCarRegisterService;

    /**
     * 通过门禁系统保存
     *
     * @param personInfo
     * @return
     */
    @Override
    public boolean saveFromSystem(ProjectPersonInfo personInfo) {

        personInfo.setSource("2");//来自于门禁系统
//        if (StringUtils.isEmpty(personInfo.getPicUrl())) {
//            personInfo.setPicUrl("-");//设置默认头像
//        }
        personInfo.setPStatus("1");// 1 正常
        return this.save(personInfo);
    }

    /**
     * 通过门禁系统批量保存
     *
     * @param personInfoList
     * @return
     */
    @Override
    public boolean saveFromSystemBatch(List<ProjectPersonInfo> personInfoList) {
        List<ProjectPersonInfo> newPersonInfoList = new ArrayList<>();
        List<String> telephoneList = new ArrayList<>();
        for (ProjectPersonInfo personInfo : personInfoList) {
            if (!telephoneList.contains(personInfo.getTelephone())) {
                personInfo.setSource("2");//来自于门禁系统
//                if (StringUtils.isEmpty(personInfo.getPicUrl())) {
//                    personInfo.setPicUrl("-");//设置默认头像
//                }
                personInfo.setPStatus("1");// 1 正常
                newPersonInfoList.add(personInfo);
                telephoneList.add(personInfo.getTelephone());
            }
        }

        return this.saveBatch(newPersonInfoList);
    }

    /**
     * 通过电话获取人员信息
     *
     * @param telephone
     * @return
     */
    @Override
    public ProjectPersonInfo getByTelephone(String telephone) {

//        return getBaseMapper().selectOne(Wrappers.lambdaQuery(ProjectPersonInfo.class)
//                .eq(ProjectPersonInfo::getTelephone, telephone)
//                .eq(ProjectPersonInfo::getPStatus, "1"));
        if (StringUtils.isEmpty(telephone)) return null;

        List<ProjectPersonInfo> personList = getBaseMapper().selectList(Wrappers.lambdaQuery(ProjectPersonInfo.class)
                .eq(ProjectPersonInfo::getTelephone, telephone)
                .eq(ProjectPersonInfo::getPStatus, "1").orderByDesc(ProjectPersonInfo::getCreateTime));

        return CollUtil.isNotEmpty(personList) ? personList.get(0) : null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ProjectHousePersonRelVo> initHousePersonRelPersonId(List<ProjectHousePersonRelVo> projectHousePersonRelVoList) {
        List<String> telephoneList = projectHousePersonRelVoList.stream().map(ProjectPersonInfoVo::getTelephone).collect(Collectors.toList());
        List<ProjectPersonInfo> hasExistPersonInfoList = new ArrayList<>();
        if (CollUtil.isNotEmpty(telephoneList)) {
            // 这是在进行新住户添加前就已经存在的住户列表
            hasExistPersonInfoList = this.list(new QueryWrapper<ProjectPersonInfo>().lambda().in(ProjectPersonInfo::getTelephone, telephoneList));
            // 这里获取到已经存在住户的手机号
            List<String> existTelephoneList = hasExistPersonInfoList.stream().map(ProjectPersonInfo::getTelephone).collect(Collectors.toList());
            // 这里获取到新住户迁入的房屋住户关系列表
            List<ProjectHousePersonRelVo> notExistPersonVoList = projectHousePersonRelVoList.stream().filter(housePersonRelVo -> {
                return !existTelephoneList.contains(housePersonRelVo.getTelephone());
            }).collect(Collectors.toList());

            List<ProjectPersonInfo> beAddPersonInfoList = new ArrayList<>();
            for (ProjectHousePersonRelVo housePersonRelVo : notExistPersonVoList) {
                ProjectPersonInfo personInfo = new ProjectPersonInfo();
                BeanUtil.copyProperties(housePersonRelVo, personInfo);
                beAddPersonInfoList.add(personInfo);
            }
            // 对手机号去重避免重复新增同个住户
            beAddPersonInfoList = beAddPersonInfoList.stream().filter(distinctByKey(ProjectPersonInfo::getTelephone)).collect(Collectors.toList());
            this.saveFromSystemBatch(beAddPersonInfoList);

            hasExistPersonInfoList.addAll(beAddPersonInfoList);
            for (ProjectPersonInfo personInfo : hasExistPersonInfoList) {
                // 这里获取到的是已经存在的住户（要将住户id存入住户房屋关系列表）
                projectHousePersonRelVoList.forEach(housePersonRelVo -> {
                    if (StrUtil.isBlank(housePersonRelVo.getPersonId()) && personInfo.getTelephone().equals(housePersonRelVo.getTelephone())) {
                        BeanUtil.copyProperties(personInfo, housePersonRelVo);
                    }
                });
            }
            return projectHousePersonRelVoList;
        }
        return null;
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    @Override
    public List<ProjectPersonInfo> findByName(String name) {
        List<ProjectPersonInfo> projectPersonInfos = list(Wrappers.lambdaQuery(ProjectPersonInfo.class)
                .eq(ProjectPersonInfo::getPeopleTypeCode, PersonTypeEnum.PROPRIETOR.code)
                .eq(ProjectPersonInfo::getPStatus, "1").eq(ProjectPersonInfo::getPersonName, name));
        return projectPersonInfos;
    }

    @Override
    @SqlParser(filter = true)
    public void updatePhoneByUserId(String phone, Integer userId) {
        baseMapper.updatePhoneByUserId(phone, userId);
    }

    @Override
    @SqlParser(filter = true)
    public void updateUserIdByPhone(String phone, Integer userId) {
        baseMapper.updateUserIdByPhone(phone, userId);
    }

    @Override
    public ProjectPersonInfo getPersonByOwner() {
        //获取当前登录用户账号
        Integer userId = SecurityUtils.getUser().getId();
        ProjectPersonInfo projectStaff = baseMapper.selectOne(Wrappers.lambdaQuery(ProjectPersonInfo.class)
                .eq(ProjectPersonInfo::getPStatus, "1")
                .eq(ProjectPersonInfo::getUserId, userId));
        if (ObjectUtil.isEmpty(projectStaff)) {
            String phone = SecurityUtils.getUser().getPhone();
            projectStaff = baseMapper.selectOne(Wrappers.lambdaQuery(ProjectPersonInfo.class)
                    .eq(ProjectPersonInfo::getPStatus, "1")
                    .eq(ProjectPersonInfo::getTelephone, phone));
        }
        return projectStaff;
    }

    @Override
    public ProjectPersonInfoVo getPersoninfo() {
        // 获取当前登录用户账号
        Integer roleId = null;
        Integer userId = SecurityUtils.getUser().getId();
        ProjectPersonInfo projectStaff = baseMapper.selectOne(Wrappers.lambdaQuery(ProjectPersonInfo.class)
                .eq(ProjectPersonInfo::getPStatus, "1")
                .eq(ProjectPersonInfo::getUserId, userId));
        // 判断当前用户在当前小区下是否有业主角色，没有则添加
        R<List<Object>> userDeptRole = remoteUserService.getUserDeptRole();
        List<String> roleIdList = new ArrayList<>();
        // 获取当前账号、当前项目所有的角色
        for (Object datum : userDeptRole.getData()) {
            JSONObject jsonObject = JSONUtil.parseObj(datum);
            if (jsonObject.get("deptId",Integer.class).equals(ProjectContextHolder.getProjectId())) {
                roleIdList.add(jsonObject.get("roleId",String.class));
            }
        }
        if (roleIdList.size() > 0) {
            // 获取当前账号、当前项目业主角色
            List<SysRole> roleList = remoteRoleService.getRoleList(roleIdList).getData().stream()
                    .filter(e -> e.getRoleName().indexOf("业主") != -1 && e.getRoleCode().indexOf("wechat") != -1)
                    .collect(Collectors.toList());
            if (roleList.size() == 0) {
                // 授权当前账号业主角色
                List<SysRole> sysRoleList = remoteRoleService.getByDeptId(ProjectContextHolder.getProjectId())
                        .getData().stream().filter(e -> e.getRoleName().indexOf("业主") != -1 && e.getRoleCode().indexOf("wechat") != -1)
                        .collect(Collectors.toList());
                if (sysRoleList.size() > 0) {
                    SysRole role = sysRoleList.get(0);
                    roleId = role.getRoleId();
                    CxUserDTO userDTO = new CxUserDTO();
                    userDTO.setUsername(projectStaff.getTelephone());
                    userDTO.setNewRoleId(role.getRoleId());
                    userDTO.setUserId(userId);
                    remoteUserService.editUserRole(userDTO);
                }
            } else {
                roleId = roleList.get(0).getRoleId();
            }
        }
        if (ObjectUtil.isEmpty(projectStaff)) {
            String phone = SecurityUtils.getUser().getPhone();
            projectStaff = baseMapper.selectOne(Wrappers.lambdaQuery(ProjectPersonInfo.class)
                    .eq(ProjectPersonInfo::getPStatus, "1")
                    .eq(ProjectPersonInfo::getTelephone, phone));
        }
        ProjectPersonInfoVo projectPersonInfoVo = new ProjectPersonInfoVo();
        BeanUtil.copyProperties(projectStaff, projectPersonInfoVo);
        projectPersonInfoVo.setRoleId(roleId);
        return projectPersonInfoVo;
    }

    @Override
    public List<ProjectHouseStatusVo> getListPerson() {
        //获取当前登录用户账号
        Integer userId = SecurityUtils.getUser().getId();
        List<ProjectHouseStatusVo> projectIdList = baseMapper.getListPersonById(userId);
        return projectIdList;
    }

    @Override
    public List<ProjectUserHouseInfoVo> getUserHouseInfo(Integer userId) {
        return baseMapper.getListUserHouseInfoByUserId(userId);
    }

    @Override
    public String getPersonId(ProjectHousePersonRelVo projectHousePersonRel) {
        //检查人员是否存在，如果不存在则添加人员
        ProjectPersonInfo personInfo = this.getByTelephone(projectHousePersonRel.getTelephone());
        String personId = "";
        Integer userId = null;
        //查询是否存在手机账号对应的userId 更新对应用户信息
        R<SysUser> requestUser = remoteUserService.user(projectHousePersonRel.getTelephone());
        if (requestUser.getCode() == 0 && ObjectUtil.isNotEmpty(requestUser.getData())) {
            userId = requestUser.getData().getUserId();
        }
        if (ObjectUtil.isEmpty(personInfo)) {
            // 人员不存在,则新增人员
            personInfo = new ProjectPersonInfo();
            personId = UUID.randomUUID().toString().replaceAll("-", "");
            personInfo.setPersonId(personId);
            personInfo.setTelephone(projectHousePersonRel.getTelephone());
            personInfo.setGender(projectHousePersonRel.getGender());
            personInfo.setPersonName(projectHousePersonRel.getPersonName());
            personInfo.setUserId(userId);
            this.saveFromSystem(personInfo);
        } else {
            personId = personInfo.getPersonId();
            personInfo.setUserId(userId);
            this.updateById(personInfo);
        }
        return personId;
    }

    @Override
    public ProjectPersonCarInfoVo getCarInfo(String personId) {
        //获取人员信息
        ProjectPersonInfo projectPersonInfo = this.getById(personId);
        //设置人员信息
        ProjectPersonCarInfoVo parkPlaceInfoVo = new ProjectPersonCarInfoVo();
        parkPlaceInfoVo.setPersonId(projectPersonInfo.getPersonId());
        parkPlaceInfoVo.setGender(projectPersonInfo.getGender());
        parkPlaceInfoVo.setPersonName(projectPersonInfo.getPersonName());
        parkPlaceInfoVo.setTelephone(projectPersonInfo.getTelephone());

        //获取关联的停车场信息
        List<ProjectParkingPlace> parkingPlaces = projectParkingPlaceService.getByPersonId(personId);
        //存在可能没有关联停车场的情况 故这里做一层非空判断
        if (ObjectUtil.isNotEmpty(parkingPlaces) && parkingPlaces.size() > 0) {
            //获取停车场id列表
            List<String> placeIds = parkingPlaces.stream().map(e -> e.getPlaceId()).collect(Collectors.toList());
            //获取与停车场关联的车辆信息
            List<ProjectParCarRegister> parCarRegisters = projectParCarRegisterService.list(
                    Wrappers.lambdaQuery(ProjectParCarRegister.class).in(ProjectParCarRegister::getParkPlaceId, placeIds));
            parkPlaceInfoVo.setParkingPlaces(parkingPlaces);
            parkPlaceInfoVo.setParCarRegisters(parCarRegisters);
        }

        return parkPlaceInfoVo;
    }

    @Override
    public Map<String, Object> groupByPersonType() {
        List<Map<String, Object>> list = baseMapper.groupByPersonType();
        Map<String, Object> map = new HashMap<String, Object>();

        list.forEach(item -> {
            map.put((String) item.get("peopleTypeCode"), item.get("c"));
        });

        return map;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<ProjectParCarRegisterVo> initParCarRegisterPersonId(List<ProjectParCarRegisterVo> parCarRegisterVoList) {
        List<String> telephoneList = parCarRegisterVoList.stream().map(ProjectParCarRegisterVo::getTelephone).collect(Collectors.toList());
        List<ProjectPersonInfo> hasExistPersonInfoList = new ArrayList<>();
        if (CollUtil.isNotEmpty(telephoneList)) {
            // 这是在进行新住户添加前就已经存在的住户列表
            hasExistPersonInfoList = this.list(new QueryWrapper<ProjectPersonInfo>().lambda().in(ProjectPersonInfo::getTelephone, telephoneList));
            // 这里获取到已经存在住户的手机号
            List<String> existTelephoneList = hasExistPersonInfoList.stream().map(ProjectPersonInfo::getTelephone).collect(Collectors.toList());
            // 这里获取到新住户迁入的房屋住户关系列表
            List<ProjectParCarRegisterVo> notExistPersonVoList = parCarRegisterVoList.stream().filter(parCarRegisterVo -> {
                return !existTelephoneList.contains(parCarRegisterVo.getTelephone());
            }).collect(Collectors.toList());

            List<ProjectPersonInfo> beAddPersonInfoList = new ArrayList<>();
            for (ProjectParCarRegisterVo parCarRegisterVo : notExistPersonVoList) {
                ProjectPersonInfo personInfo = new ProjectPersonInfo();
                BeanUtil.copyProperties(parCarRegisterVo, personInfo);
                beAddPersonInfoList.add(personInfo);
            }
            // 对手机号去重避免重复新增通个住户
            beAddPersonInfoList = beAddPersonInfoList.stream().filter(distinctByKey(ProjectPersonInfo::getTelephone)).collect(Collectors.toList());
            this.saveFromSystemBatch(beAddPersonInfoList);

            hasExistPersonInfoList.addAll(beAddPersonInfoList);
            for (ProjectPersonInfo personInfo : hasExistPersonInfoList) {
                // 这里获取到的是已经存在的住户（要将住户id存入住户房屋关系列表）
                parCarRegisterVoList.forEach(parCarRegisterVo -> {
                    if (StrUtil.isBlank(parCarRegisterVo.getPersonId()) && personInfo.getTelephone().equals(parCarRegisterVo.getTelephone())) {
                        BeanUtil.copyProperties(personInfo, parCarRegisterVo);
                    }
                });
            }
            return parCarRegisterVoList;
        }
        return null;
    }


    @Override
    public R<Boolean> updatePhoneById(String phone,String newPhone) {
        if (!CollUtil.isNotEmpty(baseMapper.getPersonByTelephone(phone))){
            return R.ok(Boolean.FALSE,"没有该用户");
        }
/*        if (CollUtil.isNotEmpty(baseMapper.getPersonByTelephone(newPhone))){
            return R.ok(Boolean.FALSE,"该手机号已被占用");
        }*/
        try {
            this.update(null,new UpdateWrapper<ProjectPersonInfo>().lambda().set(ProjectPersonInfo::getTelephone,newPhone).eq(ProjectPersonInfo::getTelephone,phone));
            return R.ok(Boolean.TRUE);
        }catch (Exception ex){
            ex.printStackTrace();
            return R.ok(Boolean.FALSE,"修改失败联系管理员");
        }
    }
}
