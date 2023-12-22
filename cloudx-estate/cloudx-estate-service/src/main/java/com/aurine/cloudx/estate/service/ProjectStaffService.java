

package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.dto.ProjectStaffDTO;
import com.aurine.cloudx.estate.entity.ProjectAttendance;
import com.aurine.cloudx.estate.entity.ProjectStaff;
import com.aurine.cloudx.estate.vo.ProjectStaffListVo;
import com.aurine.cloudx.estate.vo.ProjectStaffShiftDetailPageVo;
import com.aurine.cloudx.estate.vo.ProjectStaffVo;
import com.aurine.cloudx.estate.vo.StaffRegionVo;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

/**
 * 项目员工信息表
 *
 * @author lingang
 * @date 2020-05-11 13:38:09
 */
public interface ProjectStaffService extends IService<ProjectStaff> {
    
    /**
     * 保存员工
     * @param entity
     * @return
     */
    boolean saveStaff(ProjectStaffDTO entity);
    
    /**
     * 保存员工
     * @param entity
     * @return
     */
    boolean editStaff(ProjectStaffDTO entity);


    /**
     * 通过userId删除员工
     *
     * @param userId
     * @return
     */
    boolean removeByUserId(Integer userId, Integer projectId);

    /**
     * 获取员工拓展属性
     * @param id 员工id
     * @return
     */
    ProjectStaffVo getStaffAttrById(String id);

    /**
     * 获取当前登录用户的员工信息(微信接口相关)
     * @return
     */
    ProjectStaffVo getStaffByOwner();

    /**
     * 获取员工分页信息数据(微信接口相关)
     * @param page
     * @param name
     * @return
     */
    Page<ProjectStaffListVo> pageAll(Page page, String name,String deptId);

    /**
     * 根据userId更新手机号
     * @param phone
     * @param userId
     */
    @SqlParser(filter=true)
    void updatePhoneByUserId(@Param("phone") String phone,@Param("userId") Integer userId);

    /**
     * 根据手机号更新userId
     * @param phone
     * @param userId
     */
    @SqlParser(filter=true)
    void updateUserIdByPhone(@Param("phone") String phone,@Param("userId") Integer userId);

    /**
     * <p>
     *  根据区域ID获取管辖这个区域的员工ID
     * </p>
     *
    */
    List<String> getStaffIdByRegionId(String regionId);

    /**
     * <p>
     *  获取还未管辖区域的员工ID集合
     * </p>
     *
    */
    List<StaffRegionVo> getManagerRegionStaffList();
    /**
     * <p>
     *  根据员工ID获取员工列表
     * </p>
     *
     */
    Page<ProjectStaffListVo> staffPage(Page page, String staffId, String name);

    /**
     * <p>
     *  根据员工ID和date 获取员工上下班时间
     * </p>
     *
     */
    ProjectAttendance getWorkTime(String staffId, LocalDate date);
    /**
     * <p>
     *  根据员工ID和date 获取员工排班计划
     * </p>
     *
     */
    List<ProjectAttendance> getSchedulingPlan (String staffId, LocalDate date);
    /**
     * <p>
     *  根据部门ID和date 分页获取部门下员工的排班
     * </p>
     *
     */
    Page<ProjectStaffShiftDetailPageVo> getDeptSchedulingPage (Page page, Integer deptId, Integer year, Integer month);
    /**
     * <p>
     *  根据部门ID和date 获取部门下所有员工的排班
     * </p>
     *
     */
    List<ProjectStaffShiftDetailPageVo> getDeptScheduling(Integer deptId);

    List<String> selectId(Integer projectId );

    /**
     * <p>
     * 获取当前项目有指定菜单权限的员工ID
     * </p>
     *
     * @param menuId 菜单ID
     * @return 员工ID列表
     * @author: 王良俊
     */
    List<String> getStaffIdListByMenuId(Integer menuId);

}

