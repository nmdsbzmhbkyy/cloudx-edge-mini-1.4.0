package com.aurine.cloudx.estate.mapper;


import com.aurine.cloudx.estate.entity.SysExpertPlanEventTypeRel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 谢泽毅
 */
@Mapper
public interface SysExpertPlanEventTypeRelMapper extends BaseMapper<SysExpertPlanEventTypeRel> {

    void deleteExpertPlanEventTypeRel(@Param("eventTypeIdList")  List eventTypeIdList,
                                      @Param("planId") String planId);

    int insertExpertPlanEventTypeRel(@Param("eventTypeList") String eventTypeList, @Param("planId") String planId,
                                     @Param("tenant_id") Integer tenant_id, @Param("operator") Integer operator);
}
