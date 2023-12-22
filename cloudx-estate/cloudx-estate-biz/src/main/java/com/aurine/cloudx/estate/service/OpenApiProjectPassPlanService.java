package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectPassPlan;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Auther: wrm
 * @Date: 2022/5/16 11:16
 * @Description:
 */
public interface OpenApiProjectPassPlanService extends IService<ProjectPassPlan> {

	/**
	 * 根据人员类型获取人员默认通行方案id
	 *
	 * @param personType
	 * @return
	 */
	String getDefaultPlanId(String personType);
}
