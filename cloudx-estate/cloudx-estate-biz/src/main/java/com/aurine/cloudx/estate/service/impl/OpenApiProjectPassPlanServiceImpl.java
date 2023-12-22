package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.aurine.cloudx.estate.entity.ProjectPassPlan;
import com.aurine.cloudx.estate.mapper.ProjectPassPlanMapper;
import com.aurine.cloudx.estate.service.OpenApiProjectPassPlanService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: wrm
 * @Date: 2022/5/16 11:45
 * @Description:
 */
@Service
@Slf4j
public class OpenApiProjectPassPlanServiceImpl extends ServiceImpl<ProjectPassPlanMapper, ProjectPassPlan> implements OpenApiProjectPassPlanService {

	@Override
	public String getDefaultPlanId(String personType) {
		List<ProjectPassPlan> list = this.list(new QueryWrapper<ProjectPassPlan>().lambda()
				.eq(ProjectPassPlan::getPlanObject, personType)
				.eq(ProjectPassPlan::getIsDefault, "1")
				.orderByAsc(ProjectPassPlan::getCreateTime));

		if (CollectionUtil.isEmpty(list)) {
			return null;
		}

		return list.get(0).getPlanId();
	}

}
