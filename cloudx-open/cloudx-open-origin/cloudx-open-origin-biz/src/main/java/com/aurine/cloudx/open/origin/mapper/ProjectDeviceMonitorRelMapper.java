package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.origin.entity.ProjectDeviceMonitorRel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 设备管理-监控设备关联
 *
 * @author 邹宇
 * @date 2021-07-12 13:47:21
 */

@Mapper
public interface ProjectDeviceMonitorRelMapper extends BaseMapper<ProjectDeviceMonitorRel> {

    /**
     * 删除报警主机下防区和监控的关联
     * @param deviceId
     * @return
     */
    boolean deleteDefenseArea(@Param("deviceId") String deviceId);
}
