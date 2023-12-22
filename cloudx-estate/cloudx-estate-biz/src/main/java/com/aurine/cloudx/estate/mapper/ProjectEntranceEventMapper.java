

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectEntranceEvent;
import com.aurine.cloudx.estate.openapi.ToOpenApi;
import com.aurine.cloudx.estate.openapi.enums.OpenApiServiceNameEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenPushSubscribeCallbackTypeEnum;
import com.aurine.cloudx.estate.vo.ProjectAppEventVo;
import com.aurine.cloudx.estate.vo.ProjectEventSearchCondition;
import com.aurine.cloudx.estate.vo.ProjectEventVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 通行事件记录
 *
 * @author pigx code generator
 * @date 2020-05-20 13:27:59
 */
@ToOpenApi(serviceType = OpenPushSubscribeCallbackTypeEnum.EVENT, serviceName = OpenApiServiceNameEnum.PERSON_ENTRANCE)
@Mapper
public interface ProjectEntranceEventMapper extends BaseMapper<ProjectEntranceEvent> {

    IPage<ProjectEventVo> select(Page page, @Param("param") ProjectEventSearchCondition param);

    List<ProjectEventVo> findNum(@Param("projectId") Integer projectId, @Param("tenantId") Integer tenantId);

    Integer countOneDayByPersonType(@Param("projectId") Integer projectId, @Param("tenantId") Integer tenantId, @Param("personType") String personType);

    IPage<ProjectAppEventVo> getPageByPersonId(Page page, @Param("personId") String personId,  @Param("date") String date);
}
