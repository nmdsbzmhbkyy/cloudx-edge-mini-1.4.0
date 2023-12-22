

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectVisitor;
import com.aurine.cloudx.estate.entity.ProjectVisitorHis;
import com.aurine.cloudx.estate.openapi.ToOpenApi;
import com.aurine.cloudx.estate.openapi.enums.OpenApiServiceNameEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenPushSubscribeCallbackTypeEnum;
import com.aurine.cloudx.estate.vo.ProjectVisitorRecordVo;
import com.aurine.cloudx.estate.vo.ProjectVisitorSearchConditionVo;
import com.aurine.cloudx.estate.vo.ProjectVisitorVo;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 访客
 *
 * @date 2020-06-03 19:42:52
 */

@ToOpenApi(serviceType = OpenPushSubscribeCallbackTypeEnum.OPERATE, serviceName = OpenApiServiceNameEnum.VISITOR_INFO)
@Mapper
public interface ProjectVisitorMapper extends BaseMapper<ProjectVisitor> {

    /**
     * <p>
     * 分页查询访客信息
     * </p>
     *
     * @param page  分页对象
     * @param query 查询条件
     * @return
     * @throws
     */
    Page<ProjectVisitorRecordVo> selectPage(Page page, @Param("query") ProjectVisitorSearchConditionVo query);

    /**
     * <p>
     * 获取到所有应该签离但是未签离的访客
     * </p>
     *
     * @return 未签离访客记录列表
     */
    List<ProjectVisitorRecordVo> getAllUnLeaveList();

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
     * 获取今天要进行介质下发
     * </p>
     *
     * @return 获取今天要进行介质下发
     */
    List<ProjectVisitorHis> getSendCertHisListToday();


    /**
     * <p>
     * 获取本应下发介质但未下发介质的访客申请
     * </p>
     *
     * @return 当前时间之前的所有符合条件的访客申请
     */
    List<ProjectVisitorHis> getSendCertHisList();

    /**
     * <p>
     * 根据id查询出这条记录访问人员和的基本信息
     * </p>
     *
     * @param
     * @return
     * @throws
     * @author 王良俊
     */
    ProjectVisitorVo getDataById(@Param("visitId") String visitId);

    /**
     * 分页查询(业主查询访客申请记录)
     *
     * @param page
     * @param status
     * @param personId
     * @return
     */
    Page<ProjectVisitorRecordVo> getPageByPerson(Page page, @Param("status") String status, @Param("personId") String personId,  @Param("isOperator") Boolean isOperator);

    /**
     * 分页查询(访客查询访客申请记录)
     *
     * @param page
     * @param status
     * @param userId
     * @return
     */
    @SqlParser(filter = true)
    Page<ProjectVisitorRecordVo> getPageByVisitor(Page page, @Param("status") String status, @Param("userId") Integer userId, @Param("date") LocalDate date);

    /**
     * 根据手机号更新用户id
     *
     * @param phone
     * @param userId
     */
    @SqlParser(filter = true)
    void updatePhoneByUserId(@Param("phone") String phone, @Param("userId") Integer userId);

    /**
     * 根据用户id更新手机号
     *
     * @param phone
     * @param userId
     */
    @SqlParser(filter = true)
    void updateUserIdByPhone(@Param("phone") String phone, @Param("userId") Integer userId);

    Page<ProjectVisitor> select(Page page);
}
