package com.aurine.cloudx.estate.util;

import cn.hutool.core.collection.ListUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.feign.RemoteWebSocketService;
import com.aurine.cloudx.estate.service.ProjectStaffNoticeService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * (NoticeUtil)人员消息工具
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/9/4 15:37
 */
@Component
public class NoticeUtil implements ApplicationContextAware {

    private ProjectStaffNoticeService projectStaffNoticeService;
    @Resource
    private RemoteWebSocketService remoteWebSocketService;

    /**
     * 发送消息
     *
     * @param noticeTitle 标题
     * @param content     内容
     * @param userIds     人员id列表
     */

    public void send(boolean isStaff, String noticeTitle, String content, List<String> userIds) {
        projectStaffNoticeService.sendMessage(isStaff, noticeTitle, content, userIds);
        remoteWebSocketService.transferSocket(ProjectContextHolder.getProjectId().toString());
    }

    /**
     * 发送消息
     *
     * @param noticeTitle 标题
     * @param content     内容
     * @param userIds     人员id
     */

    public void send(boolean isStaff, String noticeTitle, String content, String userIds) {
        send(isStaff, noticeTitle, content, ListUtil.toList(userIds));
      }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        projectStaffNoticeService = applicationContext.getBean(ProjectStaffNoticeService.class);
    }

    public void send(String noticeId,boolean isStaff, String noticeTitle, String content, String userId) {
        projectStaffNoticeService.sendMessageOnId(noticeId,isStaff,noticeTitle,content,userId);
        remoteWebSocketService.transferSocket(ProjectContextHolder.getProjectId().toString());

    }
}
