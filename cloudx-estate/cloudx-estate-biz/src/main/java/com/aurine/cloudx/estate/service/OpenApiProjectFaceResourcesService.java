package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.dto.OpenApiProjectAddBlacklistFaceDto;
import com.aurine.cloudx.estate.dto.OpenApiProjectFaceResourcesDto;
import com.aurine.cloudx.estate.entity.ProjectFaceResources;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * @Author: wrm
 * @Date: 2022/06/24 15:02
 * @Package: com.aurine.cloudx.estate.service
 * @Version: 1.0
 * @Remarks:
 **/
public interface OpenApiProjectFaceResourcesService extends IService<ProjectFaceResources> {

    /**
     * 保存人脸，下发凭证
     * @param projectFaceResourcesDto
     * @return
     */
    R<OpenApiProjectFaceResourcesDto> saveFaceInfo(OpenApiProjectFaceResourcesDto projectFaceResourcesDto);

    /**
     * 删除人脸，删除设备人脸凭证
     * @param projectFaceResourcesDto
     * @return
     */
    R<String> removeFaceInfo(OpenApiProjectFaceResourcesDto projectFaceResourcesDto);
}
