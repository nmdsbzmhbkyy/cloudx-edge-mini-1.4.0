package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.dto.OpenApiProjectCardDto;
import com.aurine.cloudx.estate.entity.ProjectCard;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * @Author: wrm
 * @Date: 2022/05/23 15:15
 * @Package: com.aurine.openv2.service
 * @Version: 1.0
 * @Remarks: 卡信息管理，openApi专用接口，复杂业务避免使用分布式事务
 **/
public interface OpenApiProjectCardService extends IService<ProjectCard> {

    /**
     * 保存人卡关系，下发到设备
     *
     * @param projectCardDto
     * @return
     */
    R<OpenApiProjectCardDto> saveCardInfo(OpenApiProjectCardDto projectCardDto);

    /**
     * 删除人卡关系，卡信息未删除仅解绑卡和人关系
     *
     * @param projectCardDto
     * @return
     */
    R<String> unbindPersonCardRelation(OpenApiProjectCardDto projectCardDto);
}
