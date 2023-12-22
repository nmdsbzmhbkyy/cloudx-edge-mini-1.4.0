

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectDeviceLocation;
import com.aurine.cloudx.estate.vo.ProjectDeviceLocationVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 记录设备平面图位置打点信息
 *
 * @author lingang
 * @date 2020-06-15 16:07:41
 */
@Mapper
public interface ProjectDeviceLocationMapper extends BaseMapper<ProjectDeviceLocation> {
    /**
     * 获取平面图位置打点信息
     * @param picId
     * @return
     */
    List<ProjectDeviceLocationVo> getPoints(@Param("picId") String picId);
}
