package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.origin.vo.ProjectDeviceModifyLogVo;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceModifyLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备修改记录表
 *
 * @author 邹宇
 * @date 2021-9-26 16:05:25
 */

@Mapper
public interface ProjectDeviceModifyLogMapper extends BaseMapper<ProjectDeviceModifyLog> {


    /**
     * 根据设备ID查询更新记录
     *
     * @param deviceId
     * @return
     */
    List<ProjectDeviceModifyLogVo> getUpdateRecordByDeviceid(@Param("deviceId") String deviceId, @Param("count") Integer count);


    /**
     * 查询异常的属性
     *
     * @param deviceId
     */
    Integer getAbnormalParam(@Param("deviceId") String deviceId);
}
