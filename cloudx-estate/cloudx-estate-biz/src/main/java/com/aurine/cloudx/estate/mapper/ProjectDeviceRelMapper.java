

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectDeviceRel;
import com.aurine.cloudx.estate.openapi.ToOpenApi;
import com.aurine.cloudx.estate.openapi.enums.OpenApiServiceNameEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenPushSubscribeCallbackTypeEnum;
import com.aurine.cloudx.estate.vo.ProjectDeviceRelVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备关系表
 *
 * @author 黄健杰
 * @date 2022-01-27
 */
@ToOpenApi(serviceType = OpenPushSubscribeCallbackTypeEnum.OPERATE, serviceName = OpenApiServiceNameEnum.DEVICE_REL)
@Mapper
public interface ProjectDeviceRelMapper extends BaseMapper<ProjectDeviceRel> {

    /**
     * <p>根据设备ID获取设备关系列表 带设备类型</p>
     *
     * @param deviceId 设备ID
     * @return 设备关系vo对象列表
     * @author 王良俊
     */
    List<ProjectDeviceRelVo> listByDeviceId(@Param("deviceId") String deviceId);

    List<ProjectDeviceRelVo> listByDeviceIdAndDeviceType(@Param("deviceId") String deviceId,@Param("deviceType") String deviceType);
}
