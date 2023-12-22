package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.dto.OpenApiProjectAddBlacklistFaceDto;
import com.aurine.cloudx.estate.entity.ProjectBlacklistAttr;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * @Author: 顾文豪
 * @Date: 2023/11/9 10:35
 * @Package: com.aurine.openv2.service
 * @Version: 1.0
 * @Remarks: 黑名单属性管理，openApi专用接口
 **/
public interface OpenApiProjectBlacklistAttrService extends IService<ProjectBlacklistAttr> {
    /**
     * 保存黑名单人臉
     *
     * @param dto
     * @return
     * @auther:顾文豪
     * @since;2023-11-08 16:54
     */
    R saveFaceBlacklist(OpenApiProjectAddBlacklistFaceDto dto);

    /**
     * 删除黑名单人臉
     *
     * @param thirdFaceId 第三方人脸id
     * @return
     * @auther:顾文豪
     * @since;2023-11-08 16:54
     */
    R delFaceBlacklist(String thirdFaceId);
}
