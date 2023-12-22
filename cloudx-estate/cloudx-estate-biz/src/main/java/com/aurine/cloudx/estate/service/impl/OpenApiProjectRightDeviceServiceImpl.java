package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.estate.dto.OpenApiProjectBlacklistFaceStatusDto;
import com.aurine.cloudx.estate.entity.ProjectRightDevice;
import com.aurine.cloudx.estate.mapper.ProjectRightDeviceMapper;
import com.aurine.cloudx.estate.service.OpenApiProjectRightDeviceService;
import com.aurine.cloudx.estate.service.ProjectRightDeviceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: 顾文豪
 * @Date: 2023/11/8 20:44
 * @Package: com.aurine.openv2.service.impl
 * @Version: 1.0
 * @Remarks:
 **/
@Service
public class OpenApiProjectRightDeviceServiceImpl extends ServiceImpl<ProjectRightDeviceMapper, ProjectRightDevice> implements OpenApiProjectRightDeviceService {

    @Resource
    private ProjectRightDeviceService projectRightDeviceService;

    @Override
    public R<List<OpenApiProjectBlacklistFaceStatusDto>> selectFaceBlacklist(String thirdFaceId) {
        List<OpenApiProjectBlacklistFaceStatusDto> list = baseMapper.selectFaceBlacklistStatusDto(thirdFaceId);
        return R.ok(list);
    }
}
