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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * (WebProjectVisitorService)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/12/8 16:42
 */
public interface ProjectVisitorService extends IService<ProjectVisitor> {
    void init();

    /**
     * <p>
     * 下发介质
     * </p>
     *
     * @author 王良俊
     */
    void sendCertBatch();

    /**
     * <p>
     * 通行申请、登记
     * 申请状态由前端传输-如果是登记则应该是系统审核通过的状态
     * </p>
     *
     * @param projectVisitorVo 申请数据
     * @return 处理结果
     * @author 王良俊
     */
    boolean register(ProjectVisitorVo projectVisitorVo);

    /**
     * <p>
     * 审核通过
     * </p>
     *
     * @param visitId     申请记录id
     * @param auditStatus 审核状态（有可能只是住户审核通过所以动态输入）
     * @return 处理结果
     * @author 王良俊
     */
    Boolean passAudit(String visitId, AuditStatusEnum auditStatus);


    /**
     * <p>
     * 迁离（会推送消息到小程序）
     * </p>
     *
     * @param visitId 访客申请id
     * @return 处理结果
     * @author 王良俊
     */
    boolean signOff(String visitId);


    /**
     * <p>
     * 审核通过（批量）
     * </p>
     *
     * @param visitorVoList 访客vo对象列表
     * @return 处理结果
     * @author 王良俊
     */
    Boolean passAuditBatch(List<ProjectVisitorVo> visitorVoList);


    /**
     * 用于自动签离
     * 签离所有到时记录
     *
     * @return 处理结果
     * @author 王良俊
     */
    List<String> signOffAll();

    /**
     * <p>
     * 迁离
     * </p>
     *
     * @param visitId 访客申请id
     * @return 处理结果
     * @author 王良俊
     */
    R signOffByTask(String visitId);

    /**
     * <p>
     * 延时操作
     * </p>
     *
     * @param visitId   访客申请id
     * @param timeRange 日期范围
     * @param hourRange 时段范围
     * @return 处理结果
     * @author 王良俊
     */
    boolean delay(String visitId, String[] timeRange, String[] hourRange);

    /**
     * <p>
     * 拒绝请求（未通过审核）
     * </p>
     *
     * @param visitId      申请记录id
     * @param rejectReason 拒绝理由
     * @return 处理结果
     * @author 王良俊
     */
    Boolean rejectAudit(String visitId, String rejectReason);


    /**
     * <p>
     * 下发介质
     * </p>
     *
     * @author 王良俊
     */
    void sendCert(ProjectVisitorHis hisData);

    /**
     * <p>
     * 下发介质
     * </p>
     *
     * @author 王良俊
     */
    void sendCert(ProjectVisitorHis hisData, Integer operator);


    /**
     * <p>
     * 下发介质(当前时间所有可以下发的都会被下发)
     * </p>
     *
     * @author 王良俊
     */
    void sendCert(String time);



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

    Page<ProjectVisitor>  select(Page page);
}
