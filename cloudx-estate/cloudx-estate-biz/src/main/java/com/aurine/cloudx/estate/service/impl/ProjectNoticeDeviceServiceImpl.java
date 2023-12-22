
package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.estate.entity.ProjectNoticeDevice;
import com.aurine.cloudx.estate.mapper.ProjectNoticeDeviceMapper;
import com.aurine.cloudx.estate.service.ProjectNoticeDeviceService;
import com.aurine.cloudx.estate.vo.ProjectNoticeDeviceVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 设备配置信息发布
 *
 * @author guhl@aurine.cn
 * @date 2020-05-20 11:52:18
 */
@Service
public class ProjectNoticeDeviceServiceImpl extends ServiceImpl<ProjectNoticeDeviceMapper, ProjectNoticeDevice> implements ProjectNoticeDeviceService {

    @Override
    public Page<ProjectNoticeDeviceVo> pageNoticeDevice(Page page, ProjectNoticeDeviceVo projectNoticeDevice) {
        return baseMapper.pageNoticeDevice(page, projectNoticeDevice);
    }

}
