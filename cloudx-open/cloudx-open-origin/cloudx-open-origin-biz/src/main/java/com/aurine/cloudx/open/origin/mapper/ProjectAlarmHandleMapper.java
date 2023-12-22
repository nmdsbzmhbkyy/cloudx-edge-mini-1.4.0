

package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.common.entity.vo.AlarmHandleVo;
import com.aurine.cloudx.open.origin.entity.ProjectAlarmHandle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 报警事件处理
 *
 * @author 黄阳光
 * @date 2020-06-04 08:31:21
 */
@Mapper
public interface ProjectAlarmHandleMapper extends BaseMapper<ProjectAlarmHandle> {

    /**
     * 多条件分页查询
     *
     * @param page
     * @param po
     * @return
     */
    Page<AlarmHandleVo> page(Page page, @Param("query") ProjectAlarmHandle po);
}
