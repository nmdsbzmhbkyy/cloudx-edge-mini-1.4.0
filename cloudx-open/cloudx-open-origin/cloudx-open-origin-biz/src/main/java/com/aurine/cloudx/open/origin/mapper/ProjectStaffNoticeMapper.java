package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.origin.vo.ProjectStaffNoticeVo;
import com.aurine.cloudx.open.origin.entity.ProjectStaffNotice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 员工通知发布
 *
 * @author guhl@aurine.cn
 * @date 2020-07-06 11:16:25
 */
@Mapper
public interface ProjectStaffNoticeMapper extends BaseMapper<ProjectStaffNotice> {
    /**
     * 检索员工通知
     * @param userId
     * @param status
     * @return
     */
    Page<ProjectStaffNoticeVo> getByVo(Page page, @Param("userId") String userId, @Param("status") String status);

    /**
     * 统计业主未读消息
     * @param personId
     * @return
     */
    Integer countUnReadByPersonId(@Param("userId") String personId);
    /**
     * 统计物业未读消息
     * @param personId
     * @return
     */
    Integer countUnReadByStaffId(@Param("userId") String staffId);
}
