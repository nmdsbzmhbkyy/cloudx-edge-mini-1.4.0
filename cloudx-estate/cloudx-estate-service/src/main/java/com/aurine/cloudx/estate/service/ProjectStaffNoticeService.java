package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.vo.ProjectPersonNoticeAddVo;
import com.aurine.cloudx.estate.vo.ProjectStaffNoticeAddVo;
import com.aurine.cloudx.estate.vo.ProjectStaffNoticeVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import org.apache.ibatis.annotations.Param;

import com.aurine.cloudx.estate.entity.ProjectStaffNotice;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 员工通知发布
 *
 * @author guhl@aurine.cn
 * @date 2020-07-06 11:16:25
 */
public interface ProjectStaffNoticeService extends IService<ProjectStaffNotice> {


    

    /**
     * 保存并发送消息
     *
     * @param projectStaffNoticeAddVo
     * @return
     */
    Integer saveByUserIds(ProjectStaffNoticeAddVo projectStaffNoticeAddVo);

    /**
     * 保存业主消息
     * @param projectStaffNoticeAddVo
     * @return
     */
    void saveByPersonIds(ProjectPersonNoticeAddVo projectStaffNoticeAddVo);

    /**
     * 检索员工通知
     * @param vo
     * @return
     */
    Page<ProjectStaffNoticeVo> getByStaff(Page page, ProjectStaffNoticeVo vo);
    
    /**
     * 统计未读消息
     * @return
     */
    Integer countUnRead();

    /**
     * 根据用户查询消息(微信相关接口)
     * @param page
     * @param userId
     * @return
     */
    Page<ProjectStaffNoticeVo> pageByPerson(Page page,String userId);


    /**
     * 消息未读数量
     * @param personId
     * @return
     */
    Integer countByPersonId(String personId);
    /**
     * 消息未读数量
     * @param personId
     * @return
     */
    Integer countByPersonId(String personId, String type);


    Integer countByStaffId(String personId);

    /**
     * 异步保存消息
     */
    void sendMessage(boolean isStaff,String noticeTitle, String content, List<String> userIds);

    /**
     * 根据主键发送消息内容
     * @param noticeId
     * @param isStaff
     * @param noticeTitle
     * @param content
     * @param userId
     */
    void sendMessageOnId(String noticeId, boolean isStaff, String noticeTitle, String content, String userId);
}
