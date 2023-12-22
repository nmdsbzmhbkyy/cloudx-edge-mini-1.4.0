

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectNoticeDevice;
import com.aurine.cloudx.estate.vo.ProjectNoticeDeviceVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 设备配置信息发布
 *
 * @author guhl@aurine.cn
 * @date 2020-05-20 11:52:18
 */
@Mapper
public interface ProjectNoticeDeviceMapper extends BaseMapper<ProjectNoticeDevice> {
    /**
     * 信息设备分页查询
     *
     * @param page
     * @param projectNoticeDevice
     *
     * @return
     */
    Page<ProjectNoticeDeviceVo> pageNoticeDevice(Page page, @Param("query") ProjectNoticeDeviceVo projectNoticeDevice);

}
