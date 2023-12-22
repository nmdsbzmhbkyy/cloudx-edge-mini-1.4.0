package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.dto.OpenApiSysProjectDeptDto;
import com.aurine.cloudx.estate.entity.SysProjectDept;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * @Author: wrm
 * @Date: 2022/05/17 14:42
 * @Package: com.aurine.openv2.service
 * @Version: 1.0
 * @Remarks:
 **/
public interface OpenApiSysProjectDeptService extends IService<SysProjectDept> {

	/**
	 * 部门新增
	 *
	 * @param projectDeptDto
	 * @return
	 */
	R<OpenApiSysProjectDeptDto> deptSave(OpenApiSysProjectDeptDto projectDeptDto);

	/**
	 * 部门名称修改
	 *
	 * @param projectDeptDto
	 * @return
	 */
	R<OpenApiSysProjectDeptDto> deptUpdate(OpenApiSysProjectDeptDto projectDeptDto);

	/**
	 * 部门删除
	 *
	 * @param projectDeptDto
	 * @return
	 */
	R<Integer> deptRemove(OpenApiSysProjectDeptDto projectDeptDto);
}
