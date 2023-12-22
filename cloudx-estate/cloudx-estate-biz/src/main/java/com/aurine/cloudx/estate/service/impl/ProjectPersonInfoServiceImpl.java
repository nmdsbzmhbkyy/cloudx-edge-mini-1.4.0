
package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.aurine.cloudx.estate.constant.enums.*;
import com.aurine.cloudx.estate.entity.ProjectHousePersonRel;
import com.aurine.cloudx.estate.entity.ProjectPersonInfo;
import com.aurine.cloudx.estate.mapper.ProjectPersonInfoMapper;
import com.aurine.cloudx.estate.service.ProjectHousePersonRelService;
import com.aurine.cloudx.estate.service.ProjectPersonInfoService;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.admin.api.feign.RemoteUserService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
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

    // 人员信息
    @Resource
    private ProjectPersonInfoService projectPersonInfoService;
    @Resource
    private ProjectHousePersonRelService projectHousePersonRelService;
    @Resource
    private RemoteUserService remoteUserService;
    @Resource
    private RedisTemplate redisTemplateAurine;

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
        if (StringUtils.isEmpty(telephone)){
            return null;
        }

        List<ProjectPersonInfo> personList = getBaseMapper().selectList(Wrappers.lambdaQuery(ProjectPersonInfo.class)
                .eq(ProjectPersonInfo::getTelephone, telephone).orderByDesc(ProjectPersonInfo::getCreateTime));

        return CollUtil.isNotEmpty(personList) ? personList.get(0) : null;
    }

    @Override
    public ProjectPersonInfo getByTelephone(String redisKey, String telephone) {
        if (redisTemplateAurine.hasKey(redisKey)) {
            String personInfoJson = (String) redisTemplateAurine.opsForHash().get(redisKey, telephone);
            return StrUtil.isNotEmpty(personInfoJson) ? JSON.parseObject(personInfoJson, ProjectPersonInfo.class) : null;
        } else {
            this.initPersonInfoTmpCache(redisKey);
            return this.getByTelephone(redisKey, telephone);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_UNCOMMITTED)
    public void initPersonInfoTmpCache(String redisKey) {
        // 一次性获取整个项目所有人员的信息
        List<ProjectPersonInfo> personInfoList = this.list(new QueryWrapper<>());
        Map<String, String> personInfoMap;
        if (CollUtil.isNotEmpty(personInfoList)) {
            personInfoMap = personInfoList.stream().filter(personInfo -> StrUtil.isNotEmpty(personInfo.getTelephone())).collect(Collectors.toMap(ProjectPersonInfo::getTelephone, JSON::toJSONString, (t, t2) -> t2));
        } else {
            personInfoMap = new HashMap<>();
            personInfoMap.put("1", "1");
        }
        redisTemplateAurine.delete(redisKey);
        redisTemplateAurine.opsForHash().putAll(redisKey, personInfoMap);
        redisTemplateAurine.expire(redisKey, 2, TimeUnit.HOURS);
    }

    @Override
    public void addNewPersonInfoToTheCache(String redisKey, String telephone, ProjectPersonInfo personInfo) {
        if (!redisTemplateAurine.opsForHash().hasKey(redisKey, telephone)) {
            redisTemplateAurine.opsForHash().put(redisKey, telephone, JSON.toJSONString(personInfo));
        }
    }

    @Override
    public void deletePersonInfoTmpCache(String redisKey) {
        redisTemplateAurine.delete(redisKey);
    }

    @Override
    public Integer checkPersonAssets(String personId) {
        Integer assetsNum = this.getAssetsNum(personId);
        if (assetsNum == null || assetsNum == 0) {
            // 如果该人员已经没有任何资产了则在人员信息表中删除这个人
            this.remove(new QueryWrapper<ProjectPersonInfo>().lambda().eq(ProjectPersonInfo::getPersonId, personId));

            //状态为0代表以删除
            /*this.update(Wrappers.lambdaUpdate(ProjectPersonInfo.class)
                    .set(ProjectPersonInfo::getPStatus,"0")
                    .eq(ProjectPersonInfo::getPersonId,personId));*/
        }
        return assetsNum;
    }
    @Override
    public Integer checkPersonAssetsParking(String personId) {
        Integer assetsNum = this.getAssetsNumParking(personId);
        if (assetsNum == null || assetsNum == 0) {
            // 如果该人员已经没有任何资产了则在人员信息表中删除这个人
            this.remove(new QueryWrapper<ProjectPersonInfo>().lambda().eq(ProjectPersonInfo::getPersonId, personId));

            //状态为0代表以删除
            /*this.update(Wrappers.lambdaUpdate(ProjectPersonInfo.class)
                    .set(ProjectPersonInfo::getPStatus,"0")
                    .eq(ProjectPersonInfo::getPersonId,personId));*/
        }
        return assetsNum;
    }

    @Override
    public List<PersonAssetsNumVo> checkPersonAssets(List<String> personIdList) {
        List<PersonAssetsNumVo> assetsNumVoList = this.getAssetsNum(personIdList);
        // 这里过滤出我资产人员
        Set<String> noneAssetsIdSet = assetsNumVoList.stream().filter(personAssetsNumVo -> personAssetsNumVo.getAssetsNum() == null || personAssetsNumVo.getAssetsNum() == 0)
                .map(PersonAssetsNumVo::getPersonId).collect(Collectors.toSet());
        if (CollUtil.isNotEmpty(noneAssetsIdSet)) {
            this.remove(new QueryWrapper<ProjectPersonInfo>().lambda().in(ProjectPersonInfo::getPersonId, noneAssetsIdSet));
        }
        return assetsNumVoList;
    }

    @Override
    public Integer getAssetsNum(String personId) {
        return baseMapper.getAssetsNum(personId);
    }
    @Override
    public Integer getAssetsNumParking(String personId) {
        return baseMapper.getAssetsNumParking(personId);
    }

    @Override
    public List<PersonAssetsNumVo> getAssetsNum(List<String> personIdList) {
        return baseMapper.getAssetsNumVoList(personIdList);
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
    public ProjectPersonInfo getPersonById(String relaId) {
        ProjectPersonInfo personInfo = baseMapper.selectPersonById(relaId);
        return personInfo;
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

}
