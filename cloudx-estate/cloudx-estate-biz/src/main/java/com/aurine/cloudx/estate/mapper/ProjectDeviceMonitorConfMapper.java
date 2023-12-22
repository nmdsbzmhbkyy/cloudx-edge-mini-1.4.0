package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectDeviceMonitorConf;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 设备监控项配置(ProjectDeviceMonitorConf)表数据库访问层
 *
 * @author 王良俊
 * @since 2020-07-23 18:30:46
 */
@Mapper
public interface ProjectDeviceMonitorConfMapper extends BaseMapper<ProjectDeviceMonitorConf> {

    /**
     * <p>
     *  根据巡检点id获取该巡检点下设备的检查项
     * </p>
     *
     * @param
     * @return
     * @throws
    */
    List<ProjectDeviceMonitorConf> listMoitorByDeviceId(String deviceId);
}