package com.aurine.cloudx.common.data.project;

import com.aurine.cloudx.common.core.constant.WebConstants;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lengleng
 * @date 2018/9/14
 */
@Slf4j
public class ProjectFeignInterceptor implements RequestInterceptor {
	@Override
	public void apply(RequestTemplate requestTemplate) {
		if (ProjectContextHolder.getProjectId() == null) {
			log.error("TTL 中的 项目ID为空，feign拦截器 >> 增强失败");
			return;
		}
		requestTemplate.header(WebConstants.PROJECT_ID, ProjectContextHolder.getProjectId().toString());
	}
}
