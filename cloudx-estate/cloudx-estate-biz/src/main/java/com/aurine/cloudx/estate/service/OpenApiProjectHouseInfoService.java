package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.dto.OpenApiProjectHouseInfoDto;
import com.aurine.cloudx.estate.entity.ProjectHouseInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * 开放平台内部项目房屋信息Service
 *
 * @author : Qiu
 * @date : 2022/7/13 15:31
 */
public interface OpenApiProjectHouseInfoService extends IService<ProjectHouseInfo> {

    /**
     * 开放平台内部计算已入住的房屋数
     *
     * @param buildingId
     * @param unitId
     * @return
     */
    Integer countHouseUsed(String buildingId, String unitId);

    /**
     * 开放平台内部新增项目房屋
     *
     * @param dto 新增的项目房屋
     * @return 返回新增后的项目房屋
     */
    R<OpenApiProjectHouseInfoDto> save(OpenApiProjectHouseInfoDto dto);

    /**
     * 开放平台内部修改项目房屋
     *
     * @param dto 修改的项目房屋
     * @return 返回修改后的项目房屋
     */
    R<OpenApiProjectHouseInfoDto> update(OpenApiProjectHouseInfoDto dto);

    /**
     * 开放平台内部删除项目房屋
     *
     * @param query 要删除的条件
     * @return 返回删除结果
     */
    R<Boolean> delete(OpenApiProjectHouseInfoDto query);

    /**
     * 开放平台内部通过房屋ID删除项目房屋
     *
     * @param houseId 要删除的房屋ID
     * @return 返回删除结果
     */
    R<Boolean> deleteByHouseId(String houseId);

    /**
     * 开放平台内部通过单元ID删除项目房屋
     *
     * @param unitId 单元ID
     * @return 返回删除结果
     */
    R<Boolean> deleteByUnitId(String unitId);
}
