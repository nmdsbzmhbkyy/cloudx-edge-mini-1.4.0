

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectEntityLevelCfg;
import com.aurine.cloudx.estate.openapi.ToOpenApi;
import com.aurine.cloudx.estate.openapi.enums.OpenApiCommandTypeEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenApiServiceNameEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenPushSubscribeCallbackTypeEnum;
import com.aurine.cloudx.estate.vo.ProjectDeviceNoRule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 配置项目区域层级
 *
 * @author pigx code generator
 * @date 2020-05-06 13:49:41
 */

@Mapper
@ToOpenApi(serviceType = OpenPushSubscribeCallbackTypeEnum.OPERATE, serviceName = OpenApiServiceNameEnum.ENTITY_LEVEL_CFG)
//@CacheNamespace
public interface ProjectEntityLevelCfgMapper extends BaseMapper<ProjectEntityLevelCfg> {

    ProjectDeviceNoRule getProjectSubSection(@Param("projectId") Integer projectId);

    List<String> getPolicyIdList(@Param("projectId") Integer projectId);
}
