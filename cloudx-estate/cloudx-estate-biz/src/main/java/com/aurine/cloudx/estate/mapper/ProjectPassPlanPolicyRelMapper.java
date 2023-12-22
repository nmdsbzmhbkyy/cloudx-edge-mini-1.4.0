

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectPassPlanPolicyRel;
import com.aurine.cloudx.estate.openapi.ToOpenApi;
import com.aurine.cloudx.estate.openapi.enums.OpenApiServiceNameEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenPushSubscribeCallbackTypeEnum;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 策略方案关联
 *
 * @author pigx code generator
 * @date 2020-05-20 15:54:21
 */

@ToOpenApi(serviceType = OpenPushSubscribeCallbackTypeEnum.OPERATE, serviceName = OpenApiServiceNameEnum.PASS_PLAN_POLICY_REL)
@Mapper
public interface ProjectPassPlanPolicyRelMapper extends BaseMapper<ProjectPassPlanPolicyRel> {
    List<String> listMacroByPerson(@Param("personId") String personId);

    List<String> listMacroByPlan(@Param("planId") String planId);
}
