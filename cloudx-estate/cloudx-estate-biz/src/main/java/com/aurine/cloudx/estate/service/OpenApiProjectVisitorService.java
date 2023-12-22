package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.dto.OpenApiProjectVisitorDto;
import com.aurine.cloudx.estate.entity.ProjectVisitor;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;

import java.util.List;

/**
 * @Author: wrm
 * @Date: 2022/05/23 14:09
 * @Package: com.aurine.openv2.service
 * @Version: 1.0
 * @Remarks: 访客信息
 **/
public interface OpenApiProjectVisitorService extends IService<ProjectVisitor> {

	/**
	 * 新增访客
	 *
	 * @param OpenApiProjectVisitorDto
	 * @return
	 */
	R<OpenApiProjectVisitorDto> visitSave(OpenApiProjectVisitorDto OpenApiProjectVisitorDto);

	/**
	 * 重新申请
	 *
	 * @param OpenApiProjectVisitorDto
	 * @return
	 */
	R<OpenApiProjectVisitorDto> reRegister(OpenApiProjectVisitorDto OpenApiProjectVisitorDto);

	/**
	 * 签离
	 *
	 * @param visitIdList
	 * @return
	 */
	R<List<String>> signOff(List<String> visitIdList);
}
