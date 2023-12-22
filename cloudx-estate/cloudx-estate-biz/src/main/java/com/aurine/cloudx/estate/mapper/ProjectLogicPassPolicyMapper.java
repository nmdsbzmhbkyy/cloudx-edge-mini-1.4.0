

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectLogicPassPolicy;
import com.aurine.cloudx.estate.openapi.ToOpenApi;
import com.aurine.cloudx.estate.openapi.enums.OpenApiServiceNameEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenPushSubscribeCallbackTypeEnum;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 逻辑策略
 *
 * @author pigx code generator
 * @date 2020-05-20 15:24:32
 */

@ToOpenApi(serviceType = OpenPushSubscribeCallbackTypeEnum.OPERATE, serviceName = OpenApiServiceNameEnum.LOGIC_PASS_POLICY)
@Mapper
public interface ProjectLogicPassPolicyMapper extends BaseMapper<ProjectLogicPassPolicy> {

}
