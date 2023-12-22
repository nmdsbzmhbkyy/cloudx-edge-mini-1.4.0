package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.estate.entity.ProjectPersonInfo;
import com.aurine.cloudx.estate.mapper.ProjectPersonInfoMapper;
import com.aurine.cloudx.estate.service.OpenApiProjectPersonInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import com.pig4cloud.pigx.common.core.util.R;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: wrm
 * @Date: 2022/05/23 17:20
 * @Package: com.aurine.openv2.service.impl
 * @Version: 1.0
 * @Remarks:
 **/
@Service
public class OpenApiProjectPersonInfoServiceImpl extends ServiceImpl<ProjectPersonInfoMapper, ProjectPersonInfo> implements OpenApiProjectPersonInfoService {

    @Override
    public R<ProjectPersonInfo> getPersonInfoByPhone(String telephone) {
        // 根据手机号获取住户表住户id
        ProjectPersonInfo personInfo = this.getOne(new QueryWrapper<ProjectPersonInfo>().lambda()
                .eq(ProjectPersonInfo::getTelephone, telephone));

        if (BeanUtil.isEmpty(personInfo)) {
            return R.failed(String.format("找不到手机号[%s]对应的住户信息", telephone));
        }

        return R.ok(personInfo);
    }

    @Override
    public R<String> getPersonIdByPhone(String telephone) {
        R<ProjectPersonInfo> personInfo = getPersonInfoByPhone(telephone);

        if (personInfo.getCode() == CommonConstants.SUCCESS) {
            return R.ok(personInfo.getData().getPersonId());
        }

        return R.failed(personInfo.getMsg());
    }

    @Override
    public R<String> getPersonIdByName(String name) {
        // 根据住户名称获取住户表住户id
        ProjectPersonInfo personInfo = this.getOne(new QueryWrapper<ProjectPersonInfo>().lambda()
                .eq(ProjectPersonInfo::getPersonName, name));

        if (BeanUtil.isEmpty(personInfo)) {
            return R.failed(String.format("找不到姓名[%s]对应的住户信息", name));
        }

        return R.ok(personInfo.getPersonId());
    }

    @Override
    public R<List<String>> getPersonIdByNameLike(String name) {
        // 根据住户名称获取住户表住户id
        List<ProjectPersonInfo> list = this.list(new QueryWrapper<ProjectPersonInfo>().lambda()
                .like(ProjectPersonInfo::getPersonName, name));

        if (BeanUtil.isEmpty(list)) {
            return R.failed(String.format("找不到姓名(模糊查询)[%s]对应的住户信息", name));
        }

        return R.ok(list.stream().map(ProjectPersonInfo::getPersonId).collect(Collectors.toList()));
    }

    @Override
    public Boolean checkPersonIdExist(String personId) {
        return this.count(new QueryWrapper<ProjectPersonInfo>().lambda().eq(ProjectPersonInfo::getPersonId, personId)) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> updatePersonPhone(String oldTelephone, String newTelephone) {
        if (StringUtils.isBlank(oldTelephone) || StringUtils.isBlank(oldTelephone)) {
            return R.failed("缺失必传参数 oldTelephone 或 newTelephone");
        }
        // 根据旧手机号判断该用户是否存在
        List<ProjectPersonInfo> useOldPhonepersonList = getBaseMapper().selectList(Wrappers.lambdaQuery(ProjectPersonInfo.class)
                .eq(ProjectPersonInfo::getTelephone, oldTelephone)
                .eq(ProjectPersonInfo::getPStatus,"1")
                .orderByDesc(ProjectPersonInfo::getCreateTime));
        if (CollUtil.isEmpty(useOldPhonepersonList) ) {
            return R.failed(String.format("找不到手机号[%s]对应的人员信息", oldTelephone));
        }
        // 判断新手机号已有用户使用
        List<ProjectPersonInfo> useNewPhonePersonList = getBaseMapper().selectList(Wrappers.lambdaQuery(ProjectPersonInfo.class)
                .eq(ProjectPersonInfo::getTelephone, newTelephone)
                .eq(ProjectPersonInfo::getPStatus,"1")
                .orderByDesc(ProjectPersonInfo::getCreateTime));
        if (CollUtil.isNotEmpty(useNewPhonePersonList) ) {
            return R.failed(String.format("手机号[%s]已被其他人员使用", newTelephone));
        }
        ProjectPersonInfo personInfo = useOldPhonepersonList.get(0);
        // 更新手机号
        LambdaUpdateWrapper<ProjectPersonInfo> updateWrapper = new UpdateWrapper<ProjectPersonInfo>().lambda()
                .eq(ProjectPersonInfo::getPersonId,personInfo.getPersonId())
                .set(ProjectPersonInfo::getTelephone,newTelephone);

        return R.ok(update(updateWrapper));
    }
}
