

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectPhysicalPassPolicy;
import com.aurine.cloudx.estate.openapi.ToOpenApi;
import com.aurine.cloudx.estate.openapi.enums.OpenApiServiceNameEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenPushSubscribeCallbackTypeEnum;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 物理策略
 *
 * @author pigx code generator
 * @date 2020-05-20 15:44:19
 */

@ToOpenApi(serviceType = OpenPushSubscribeCallbackTypeEnum.OPERATE, serviceName = OpenApiServiceNameEnum.PHYSICAL_PASS_POLICY)
@Mapper
public interface ProjectPhysicalPassPolicyMapper extends BaseMapper<ProjectPhysicalPassPolicy> {

}
