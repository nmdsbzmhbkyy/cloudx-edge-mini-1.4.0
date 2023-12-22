

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectPersonPlanRel;
import com.aurine.cloudx.estate.openapi.ToOpenApi;
import com.aurine.cloudx.estate.openapi.enums.OpenApiServiceNameEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenPushSubscribeCallbackTypeEnum;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 人员方案关系
 *
 * @author pigx code generator
 * @date 2020-05-22 09:53:09
 */
@ToOpenApi(serviceType = OpenPushSubscribeCallbackTypeEnum.OPERATE, serviceName = OpenApiServiceNameEnum.PERSON_PLAN_REL)
@Mapper
public interface ProjectPersonPlanRelMapper extends BaseMapper<ProjectPersonPlanRel> {

    /**
     * <p>
     *  获取到今天过期的住户权限记录
     * </p>
     *
    */
    List<ProjectPersonPlanRel> getTodayExpList();

    /**
     * <p>
     *  获取到指定时间之前所有的超期权限（住户）
     * </p>
     *
     * @param timeStr 如2020-08-10 12:11
    */
    List<String> getAllTimeOutRightPerson(String timeStr);

    /**
     * <p>
     *  获取到指定时间之前所有的超期权限（员工）
     * </p>
     *
     * @param timeStr 如2020-08-10 12:11
    */
    List<String> getAllTimeOutRightStaff(String timeStr);



    /**
     * <p>
     *  获取到今天过期的住户权限记录
     * </p>
     *
     */
    List<ProjectPersonPlanRel> getExpList();

}
