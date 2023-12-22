

package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.common.entity.vo.PersonPlanRelVo;
import com.aurine.cloudx.open.origin.entity.ProjectPersonPlanRel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 人员方案关系
 *
 * @author pigx code generator
 * @date 2020-05-22 09:53:09
 */
@Mapper
public interface ProjectPersonPlanRelMapper extends BaseMapper<ProjectPersonPlanRel> {

    /**
     * <p>
     * 获取到今天过期的住户权限记录
     * </p>
     */
    List<ProjectPersonPlanRel> getTodayExpList();

    /**
     * <p>
     * 获取到指定时间之前所有的超期权限（住户）
     * </p>
     *
     * @param timeStr 如2020-08-10 12:11
     */
    List<String> getAllTimeOutRightPerson(String timeStr);

    /**
     * <p>
     * 获取到指定时间之前所有的超期权限（员工）
     * </p>
     *
     * @param timeStr 如2020-08-10 12:11
     */
    List<String> getAllTimeOutRightStaff(String timeStr);

    /**
     * <p>
     * 获取到今天过期的住户权限记录
     * </p>
     */
    List<ProjectPersonPlanRel> getExpList();

    /**
     * 多条件分页查询
     *
     * @param page
     * @param po
     * @return
     */
    Page<PersonPlanRelVo> page(Page page, @Param("query") ProjectPersonPlanRel po);
}
