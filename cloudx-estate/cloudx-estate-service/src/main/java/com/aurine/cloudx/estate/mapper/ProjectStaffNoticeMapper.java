package com.aurine.cloudx.estate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aurine.cloudx.estate.entity.ProjectStaffNotice;
import com.aurine.cloudx.estate.vo.ProjectStaffNoticeVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

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
    Page<ProjectStaffNoticeVo> getByVo(Page page, @Param("userId")String userId,@Param("status") String status);
    
    /**
     * 统计业主未读消息
     * @param personId
     * @return
     */
    Integer countUnReadByPersonId(@Param("userId")String personId);
    /**
     * 统计物业未读消息
     * @param personId
     * @return
     */
    Integer countUnReadByStaffId(@Param("userId")String staffId);

    /**
     * 统计业主未读消息
     * @param personId
     * @return
     */
    Integer countNoticeByPersonId(@Param("userId")String personId,@Param("type") String type);
}
