

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectAlarmHandle;
import com.aurine.cloudx.estate.openapi.ToOpenApi;
import com.aurine.cloudx.estate.openapi.enums.OpenApiServiceNameEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenPushSubscribeCallbackTypeEnum;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 报警事件处理
 *
 * @author 黄阳光
 * @date 2020-06-04 08:31:21
 */
@ToOpenApi(serviceType = OpenPushSubscribeCallbackTypeEnum.OPERATE, serviceName = OpenApiServiceNameEnum.ALARM_HANDLE)
@Mapper
public interface ProjectAlarmHandleMapper extends BaseMapper<ProjectAlarmHandle> {

}
