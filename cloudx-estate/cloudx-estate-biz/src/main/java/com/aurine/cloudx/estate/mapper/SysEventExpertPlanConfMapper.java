package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.SysEventExpertPlanConf;
import com.aurine.cloudx.estate.vo.SysEventExpertPlanConfVo;
import com.aurine.cloudx.estate.vo.SysEventTypeConfVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 谢泽毅
 */
@Mapper
public interface SysEventExpertPlanConfMapper extends BaseMapper<SysEventExpertPlanConf> {
    /**
     * 平台管理员分页查询预案
     * @param sysExpertPlan
     * @param page
     * @return
     */
    Page<SysEventExpertPlanConfVo> pageExpertPlan(@Param("query") SysEventExpertPlanConf sysExpertPlan, Page page);

    /**
     * 预案设备关联展示
     * @param sysEventTypeConfVo
     * @return
     */
    List<SysEventTypeConfVo> getExpertPlanEventTypeRelList(@Param("query") SysEventTypeConfVo sysEventTypeConfVo);

    /**
     * 删除预案
     * @param planId
     */
    void deleteRel(@Param("planId") String planId);

    /**
     * 通过eventTypeId获取相关的预案信息
     * @param eventTypeId
     * @return
     */
    List<SysEventExpertPlanConf> getExpertPlanListByEventTypeId(@Param("eventTypeId")String eventTypeId);
}
