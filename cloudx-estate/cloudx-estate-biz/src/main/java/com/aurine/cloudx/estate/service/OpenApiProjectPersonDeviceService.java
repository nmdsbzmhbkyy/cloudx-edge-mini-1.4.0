package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.dto.OpenApiProjectPersonDeviceDto;
import com.aurine.cloudx.estate.entity.ProjectPersonDevice;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;

import java.util.List;

/**
 * @Author: wrm
 * @Date: 2022/05/25 9:11
 * @Package: com.aurine.openv2.service
 * @Version: 1.0
 * @Remarks:
 **/
public interface OpenApiProjectPersonDeviceService extends IService<ProjectPersonDevice> {

	/**
	 * 新增员工通行权限
	 *
	 * @param openApiProjectPersonDeviceDto
	 * @return
	 */
	R<List<OpenApiProjectPersonDeviceDto>> staffSaveList(OpenApiProjectPersonDeviceDto openApiProjectPersonDeviceDto);

	/**
	 * 删除员工通行权限
	 *
	 * @param openApiProjectPersonDeviceDto
	 * @return
	 */
	R<List<String>> staffRemoveList(OpenApiProjectPersonDeviceDto openApiProjectPersonDeviceDto);

	/**
	 * 新增住户通行权限
	 *
	 * @param openApiProjectPersonDeviceDto
	 * @return
	 */
	R<List<OpenApiProjectPersonDeviceDto>> proprietorSaveList(OpenApiProjectPersonDeviceDto openApiProjectPersonDeviceDto);

	/**
	 * 删除住户通行权限
	 *
	 * @param openApiProjectPersonDeviceDto
	 * @return
	 */
	R<List<String>> proprietorRemoveList(OpenApiProjectPersonDeviceDto openApiProjectPersonDeviceDto);

	// ------------内部调用方法-----------------
    /**
     * 添加通行权限, 分发请求
     *
     * @param personId
     * @param personType
     * @param planId
     * @return
     */
    Boolean addPersonPassRightDevice(String personId, String personType, String planId);

    /**
     * 根据用户Id获取设备id集合
     *
     * @param personId
     * @param personType
     * @return
     */
    List<String> getDeviceIdByPersonId(String personId, String personType);

}
