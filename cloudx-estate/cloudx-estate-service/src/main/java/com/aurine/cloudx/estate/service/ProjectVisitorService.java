

package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.constant.enums.AuditStatusEnum;
import com.aurine.cloudx.estate.entity.ProjectVisitor;
import com.aurine.cloudx.estate.entity.ProjectVisitorHis;
import com.aurine.cloudx.estate.vo.ProjectVisitorRecordVo;
import com.aurine.cloudx.estate.vo.ProjectVisitorSearchConditionVo;
import com.aurine.cloudx.estate.vo.ProjectVisitorVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

/**
 * 访客
 * 王伟
 *
 * @date 2020-06-03 19:42:52
 */
public interface ProjectVisitorService extends IService<ProjectVisitor> {



    /**
     * <p>
     * 用于查询出所有的访客记录
     * </p>
     *
     * @param page                            分页对象
     * @param projectVisitorSearchConditionVo 搜索条件
     * @return 分页数据
     * @author 王良俊
     */
    Page<ProjectVisitorRecordVo> getPage(Page page, ProjectVisitorSearchConditionVo projectVisitorSearchConditionVo);




    /**
     * <p>
     * 获取到所有应该签离但是未签离的访客(只限于今天)
     * </p>
     *
     * @return 今天未签离访客记录列表
     */
    List<ProjectVisitorRecordVo> getAllUnLeaveListToday();




    /**
     * <p>
     * 重新申请时查询数据
     * </p>
     *
     * @param visitId 访客申请id
     * @return 访客vo对象
     * @author 王良俊
     */
    ProjectVisitorVo getDataById(String visitId);

    /**
     * 分页查询(业主查询访客申请记录)
     *
     * @param page
     * @param personId
     * @param status
     * @return
     */
    Page<ProjectVisitorRecordVo> getPageByPerson(Page page, String personId, String status);

    /**
     * 分页查询(访客查询申请记录)
     *
     * @param page   分页对象
     * @param status 审核状态
     * @param userId 账号ID
     * @return
     */
    Page<ProjectVisitorRecordVo> getPageByVisitor(Page page, Integer userId, String status, String date);

    /**
     * 分页查询(查询创建人申请记录)
     *
     * @param page   分页对象
     * @param status 审核状态
     * @param status 创建人
     * @return
     */
    Page<ProjectVisitorRecordVo> getPageByCreate(Page page, String personId, String status);


    /**
     * 获取当前登录用户的员工信息(微信接口相关)
     *
     * @return
     */
    ProjectVisitor getVisitorByOwner();

    /**
     * 根据userId更新手机号
     *
     * @param phone
     * @param userId
     */
    void updatePhoneByUserId(String phone, Integer userId);

    /**
     * 根据手机号更新userId
     *
     * @param phone
     * @param userId
     */
    void updateUserIdByPhone(String phone, Integer userId);

}
