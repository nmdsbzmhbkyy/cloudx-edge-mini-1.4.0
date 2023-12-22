package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.aurine.cloudx.estate.entity.ProjectPersonPlanRel;
import com.aurine.cloudx.estate.mapper.ProjectPersonPlanRelMapper;
import com.aurine.cloudx.estate.service.OpenApiProjectPersonPlanRelService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @Author: wrm
 * @Date: 2022/06/28 11:14
 * @Package: com.aurine.cloudx.estate.service.impl
 * @Version: 1.0
 * @Remarks:
 **/
@Slf4j
@Service
public class OpenApiProjectPersonPlanRelServiceImpl extends ServiceImpl<ProjectPersonPlanRelMapper, ProjectPersonPlanRel> implements OpenApiProjectPersonPlanRelService {
    @Override
    public boolean saveOrUpdatePersonPlan(ProjectPersonPlanRel personPlanRel) {
        ProjectPersonPlanRel projectPersonPlanRel;

        // 获取当前personId对应的planOpenApiProjectPersonDeviceService
        projectPersonPlanRel = getPlanByPersonId(personPlanRel.getPersonId());

        // 没有plan直接新增
        if (ObjectUtils.isEmpty(projectPersonPlanRel)) {
            projectPersonPlanRel = new ProjectPersonPlanRel();

            projectPersonPlanRel.setPersonId(personPlanRel.getPersonId());
            projectPersonPlanRel.setPlanId(personPlanRel.getPlanId());
            projectPersonPlanRel.setPersonType(personPlanRel.getPersonType());
            projectPersonPlanRel.setIsActive("1");
            if (personPlanRel.getEffTime() == null) {
                projectPersonPlanRel.setEffTime(LocalDateTime.now());
            }
            if (personPlanRel.getExpTime() == null) {
                projectPersonPlanRel.setExpTime(LocalDateTime.parse("2199-01-01 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }

            return this.save(projectPersonPlanRel);
        }

        // 有plan更新plan
        else {
            projectPersonPlanRel.setPlanId(personPlanRel.getPlanId());
            if (personPlanRel.getExpTime() != null) {
                projectPersonPlanRel.setExpTime(personPlanRel.getExpTime());
            }
            return this.updateById(projectPersonPlanRel);
        }

    }

    private ProjectPersonPlanRel getPlanByPersonId(String personId) {
        List<ProjectPersonPlanRel> list = this.list(new QueryWrapper<ProjectPersonPlanRel>().lambda()
                .eq(ProjectPersonPlanRel::getPersonId, personId));
        if (CollectionUtil.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

}
