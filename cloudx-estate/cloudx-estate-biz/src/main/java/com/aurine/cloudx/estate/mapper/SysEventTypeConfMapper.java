package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.SysEventTypeConf;
import com.aurine.cloudx.estate.entity.SysExpertPlanEventTypeRel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
/**
 * @author Administrator
 */
@Mapper
public interface SysEventTypeConfMapper extends BaseMapper<SysEventTypeConf> {
    /**
     * 平台管理员分页查询报警类型
     * @param page
     * @param sysEventTypeConf
     * @return
     */
    Page<SysEventTypeConf> pageAlarmType(Page page, @Param("query") SysEventTypeConf sysEventTypeConf);

    /**
     * 通过预案获取关联报警
     * @param page
     * @param sysExpertPlanEventTypeRel
     * @return
     */
    Page<SysEventTypeConf> pageAlarmTypeByPlanId(Page page, @Param("query") SysExpertPlanEventTypeRel sysExpertPlanEventTypeRel);
}
