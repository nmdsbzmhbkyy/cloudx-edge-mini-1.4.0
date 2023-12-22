

package com.aurine.cloudx.estate.mapper;
import com.aurine.cloudx.estate.entity.ProjectPersonLiftRel;
import com.aurine.cloudx.estate.openapi.ToOpenApi;
import com.aurine.cloudx.estate.openapi.enums.OpenApiServiceNameEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenPushSubscribeCallbackTypeEnum;
import com.aurine.cloudx.estate.vo.ProjectPassDeviceVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
@ToOpenApi(serviceType = OpenPushSubscribeCallbackTypeEnum.OPERATE, serviceName = OpenApiServiceNameEnum.PERSON_LIFT_REL)
public interface ProjectPersonLiftRelMapper extends BaseMapper<ProjectPersonLiftRel> {
    /**
     * 根据人员id获取该人员可通行的电梯设备
     *
     * @param personId         人物id
     * @param isUnitLift     是否为本单元电梯
     * @param onlyPlan         是否只获取方案中的设备
     * @return
     */
    List<ProjectPassDeviceVo> liftListByPerson(
            @Param("personId") String personId,
            @Param("planId") String planId,
            @Param("isUnitLift") boolean isUnitLift,
            @Param("onlyPlan") boolean onlyPlan
    );
}
