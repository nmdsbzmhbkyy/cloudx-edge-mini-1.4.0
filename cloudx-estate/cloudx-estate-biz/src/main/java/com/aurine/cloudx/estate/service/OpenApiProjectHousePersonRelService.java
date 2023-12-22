package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.dto.OpenApiProjectHousePersonRelDto;
import com.aurine.cloudx.estate.entity.ProjectHousePersonRel;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * @Author: wrm
 * @Date: 2022/05/23 17:13
 * @Package: com.aurine.openv2.mapper
 * @Version: 1.0
 * @Remarks:
 **/
public interface OpenApiProjectHousePersonRelService extends IService<ProjectHousePersonRel> {

	/**
	 * 新增住户
	 *
	 * @param openApiProjectHousePersonRelDto
	 * @return
	 */
	R<OpenApiProjectHousePersonRelDto> saveHousehold(OpenApiProjectHousePersonRelDto openApiProjectHousePersonRelDto);

    /**
     * 修改住户
     *
     * @param openApiProjectHousePersonRelDto
     * @return
     */
    R<OpenApiProjectHousePersonRelDto> updateById(OpenApiProjectHousePersonRelDto openApiProjectHousePersonRelDto);

    /**
     * 删除/迁出住户
     *
     * @param realId
     * @return
     */
    R<String> removeHousePersonRel(String realId);

    /**
     * 校验住户是否拥有房屋
     * @param personId
     * @param houseId
     * @return
     */
    Boolean checkPersonOwnHouse(String personId, String houseId);
}
