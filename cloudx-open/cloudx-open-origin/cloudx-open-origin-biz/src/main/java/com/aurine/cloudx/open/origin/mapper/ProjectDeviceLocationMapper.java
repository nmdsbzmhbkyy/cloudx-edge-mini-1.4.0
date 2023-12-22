

package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.origin.vo.ProjectDeviceLocationVo;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceLocation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
