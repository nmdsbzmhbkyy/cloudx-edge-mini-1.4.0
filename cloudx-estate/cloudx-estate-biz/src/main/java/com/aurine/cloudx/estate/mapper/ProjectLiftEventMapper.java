

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectEntranceEvent;
import com.aurine.cloudx.estate.entity.ProjectLiftEvent;
import com.aurine.cloudx.estate.openapi.ToOpenApi;
import com.aurine.cloudx.estate.openapi.enums.OpenApiServiceNameEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenPushSubscribeCallbackTypeEnum;
import com.aurine.cloudx.estate.vo.ProjectEventSearchCondition;
import com.aurine.cloudx.estate.vo.ProjectLiftEventVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 乘梯事件记录
 *
 * @author pigx code generator
 * @date 2020-05-20 13:27:59
 */
@Mapper
@ToOpenApi(serviceType = OpenPushSubscribeCallbackTypeEnum.EVENT, serviceName = OpenApiServiceNameEnum.LIFT_EVENT)
public interface ProjectLiftEventMapper extends BaseMapper<ProjectLiftEvent> {

    IPage<ProjectLiftEventVo> select(Page page, @Param("param") ProjectEventSearchCondition param);
}
