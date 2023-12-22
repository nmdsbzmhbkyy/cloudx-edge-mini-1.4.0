

package com.aurine.cloudx.open.origin.service;

import com.aurine.cloudx.open.origin.vo.ProjectNoticeVo;
import com.aurine.cloudx.open.origin.vo.ProjectPersonNoticeVo;
import com.aurine.cloudx.open.origin.entity.ProjectNotice;
import com.aurine.cloudx.open.origin.entity.ProjectNoticeDevice;
import com.aurine.cloudx.open.origin.vo.ProjectNoticeAddVo;
import com.aurine.cloudx.open.origin.vo.ProjectNoticeFormVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

/**
 * 信息发布
 *
 * @author guhl@aurine.cn
 * @date 2020-05-20 11:52:46
 */
public interface ProjectNoticeService extends IService<ProjectNotice> {


    /**
     * 分页查询信息
     *
     * @param projectNoticeFormVo
     * @return
     */
    Page<ProjectNoticeVo> pageNotice(Page<ProjectNoticeVo> page, @Param("query") ProjectNoticeFormVo projectNoticeFormVo);




    /**
     * 获取失败的Project集合
     *
     * @param projectNoticeId
     * @return
     */
    List<ProjectNoticeDevice> projectNoticeList(String projectNoticeId);

    /**
     * 公告清除
     *
     * @param deviceIds
     * @return
     */
    boolean removeNotice(List<String> deviceIds);

    /**
     * 获取需要删除公告的设备信息集合
     *
     * @param deviceIds
     * @return
     */
    List<ProjectNoticeDevice> projectNoticeDeviceList(@Param("deviceIds") List<String> deviceIds);

    /**
     * 删除信息（预留功能）
     *
     * @param id
     * @return
     */

    boolean removeNoticeById(String id);

    /**
     * 一键重发 xull@aurine.cn 2020年5月22日 09点55分
     *
     * @param id
     * @return
     */
    boolean resendAll(String id);

    /**
     * 重新发送失败的一条信息  xull@aurine.cn 2020/5/22 13:42
     *
     * @param noticeId 消息id
     * @param deviceId 设备id
     * @return
     */
    boolean resend(String noticeId, String deviceId);

    /**
     * 根据用户查询消息(微信相关接口)
     *
     * @param page
     * @param personId
     * @return
     */
    Page<ProjectPersonNoticeVo> pageByPerson(Page page, String personId);

    /**
     * 批量发送信息
     *
     * @param projectNoticeAddVo
     */
    String  sendBatch(ProjectNoticeAddVo projectNoticeAddVo);

    void sendVo(ProjectNoticeAddVo projectNoticeAddVo);
}
