package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.dto.OpenApiProjectBlacklistFaceStatusDto;
import com.aurine.cloudx.estate.entity.ProjectRightDevice;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;

import java.util.List;

/**
 * @Author: 顾文豪
 * @Date: 2023/11/88 20:11
 * @Package: com.aurine.cloudx.estate.service.impl
 * @Version: 1.0
 * @Remarks:
 **/
public interface OpenApiProjectRightDeviceService extends IService<ProjectRightDevice> {
    /**
     * 人脸黑名单下发状态查询
     *
     * @param thirdFaceId  第三方人脸id
     * @return
     */
    R<List<OpenApiProjectBlacklistFaceStatusDto>> selectFaceBlacklist(String thirdFaceId);

}
