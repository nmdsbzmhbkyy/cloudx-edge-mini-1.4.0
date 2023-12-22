

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectHousePersonChangeHis;
import com.aurine.cloudx.estate.openapi.ToOpenApi;
import com.aurine.cloudx.estate.openapi.enums.OpenApiServiceNameEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenPushSubscribeCallbackTypeEnum;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 住户变动记录
 *
 * @author 王伟
 * @date 2020-05-11 13:54:18
 */
@ToOpenApi(serviceType = OpenPushSubscribeCallbackTypeEnum.OPERATE, serviceName = OpenApiServiceNameEnum.HOUSE_PERSON_CHANGE_HIS)
@Mapper
public interface ProjectHousePersonChangeHisMapper extends BaseMapper<ProjectHousePersonChangeHis> {

}
