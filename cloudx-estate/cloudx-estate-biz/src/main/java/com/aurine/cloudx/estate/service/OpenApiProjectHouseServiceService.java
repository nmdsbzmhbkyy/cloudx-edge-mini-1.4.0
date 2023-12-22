package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.dto.OpenApiProjectHouseServiceDto;
import com.aurine.cloudx.estate.entity.ProjectHouseService;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * 开放平台内部项目房屋增值服务Service
 *
 * @author : Qiu
 * @date : 2022/7/14 10:58
 */
public interface OpenApiProjectHouseServiceService extends IService<ProjectHouseService> {

    /**
     * 开放平台内部为房屋新增增值服务
     *
     * @param dto 新增的条件
     * @return 返回新增结果
     */
    R<Boolean> save(OpenApiProjectHouseServiceDto dto);

    /**
     * 开放平台内部为房屋新增增值服务
     *
     * @param dto 新增的条件
     * @return 返回新增结果
     */
    R<Boolean> saveOne(OpenApiProjectHouseServiceDto dto);

    /**
     * 开放平台内部批量为房屋新增增值服务
     *
     * @param dto 新增的条件
     * @return 返回新增结果
     */
    R<Boolean> saveBatch(OpenApiProjectHouseServiceDto dto);

    /**
     * 开放平台内部为房屋删除增值服务
     *
     * @param dto 要删除的条件
     * @return 返回删除结果
     */
    R<Boolean> delete(OpenApiProjectHouseServiceDto dto);

    /**
     * 开放平台内部为房屋删除增值服务
     *
     * @param dto 要删除的条件
     * @return 返回删除结果
     */
    R<Boolean> deleteOne(OpenApiProjectHouseServiceDto dto);

    /**
     * 开放平台内部批量为房屋删除增值服务
     *
     * @param dto 要删除的条件
     * @return 返回删除结果
     */
    R<Boolean> deleteBatch(OpenApiProjectHouseServiceDto dto);
}
