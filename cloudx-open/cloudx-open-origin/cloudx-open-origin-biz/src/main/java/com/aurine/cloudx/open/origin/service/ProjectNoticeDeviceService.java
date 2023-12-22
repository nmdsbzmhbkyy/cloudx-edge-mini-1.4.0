

package com.aurine.cloudx.open.origin.service;

import com.aurine.cloudx.open.origin.vo.ProjectNoticeDeviceVo;
import com.aurine.cloudx.open.origin.entity.ProjectNoticeDevice;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.lettuce.core.dynamic.annotation.Param;

/**
 * 设备配置信息发布
 *
 * @author guhl@aurine.cn
 * @date 2020-05-20 11:52:18
 */
public interface ProjectNoticeDeviceService extends IService<ProjectNoticeDevice> {

    /**
     * 分页查询设备信息
     *
     * @param page
     * @param projectNoticeDevice
     *
     * @return
     */
    Page<ProjectNoticeDeviceVo> pageNoticeDevice(Page page, @Param("query") ProjectNoticeDeviceVo projectNoticeDevice);


}
