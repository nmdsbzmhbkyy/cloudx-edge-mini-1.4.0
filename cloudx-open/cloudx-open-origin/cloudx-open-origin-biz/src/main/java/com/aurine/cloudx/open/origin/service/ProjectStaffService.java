package com.aurine.cloudx.open.origin.service;

import com.aurine.cloudx.open.common.entity.vo.StaffInfoVo;
import com.aurine.cloudx.open.origin.dto.ProjectStaffDTO;
import com.aurine.cloudx.open.origin.entity.ProjectAttendance;
import com.aurine.cloudx.open.origin.entity.ProjectStaff;
import com.aurine.cloudx.open.origin.vo.*;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 项目员工信息表
 *
 * @author lingang
 * @date 2020-05-11 13:38:09
 */
public interface ProjectStaffService extends IService<ProjectStaff> {
    /**
     * 删除员工
     *
     * @return
     */
    boolean removeStaff(String id);

    /**
     * 根据员工第三方id，保存或更新员工，用于WR20
     *
     * @param staffDTO
     * @return
     */
    String saveOrUpdateStaffByStaffCode(ProjectStaffDTO staffDTO);

    /**
     * 保存员工
     *
     * @param entity
     * @return
     */
    boolean saveStaff(ProjectStaffDTO entity);

    /**
     * 保存员工
     *
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
     *
     * @param id 员工id
     * @return
     */
    ProjectStaffVo getStaffAttrById(String id);

    /**
     * 获取当前登录用户的员工信息(微信接口相关)
     *
     * @return
     */
    ProjectStaffVo getStaffByOwner();

    /**
     * 获取员工分页信息数据(微信接口相关)
     *
     * @param page
     * @param name
     * @return
     */
    Page<ProjectStaffListVo> pageAll(Page page, String name);

    /**
     * 根据userId更新手机号
     *
     * @param phone
     * @param userId
     */
    @SqlParser(filter = true)
    void updatePhoneByUserId(@Param("phone") String phone, @Param("userId") Integer userId);

    /**
     * 根据手机号更新userId
     *
     * @param phone
     * @param userId
     */
    @SqlParser(filter = true)
    void updateUserIdByPhone(@Param("phone") String phone, @Param("userId") Integer userId);

    /**
     * <p>
     * 根据区域ID获取管辖这个区域的员工ID
     * </p>
     */
    List<String> getStaffIdByRegionId(String regionId);

    /**
     * <p>
     * 获取还未管辖区域的员工ID集合
     * </p>
     */
    List<StaffRegionVo> getManagerRegionStaffList();

    /**
     * <p>
     * 根据员工ID获取员工列表
     * </p>
     */
    Page<ProjectStaffListVo> staffPage(Page page, String staffId, String name);

    ProjectStaff getStaffByUserId(Integer userId, Integer deptId);

    List<SysUserVo> getUserVosByDeptId(Integer deptId);

    List<ProjectAttendance> getSchedulingPlan(String staffId, LocalDate date);

    List<ProjectAttendance> getSchedulingPlanVo(String staffId, ProjectAttendance projectAttendance);

    /**
     * <p>
     * 根据项目ID获取员工集合
     * </p>
     */
    List<ProjectStaff> getStaffIdByProjectId(Integer projectId);


    void updateEmployeeAvatar(AppFaceHeadPortraitVo appFaceHeadPortraitVo);

    Boolean updatePhoneById(Integer userId, String phone);

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

    Integer getDepartmentId(Integer projectId);

    Integer getRoleId(Integer projectId);

    /**
     * 分页查询
     *
     * @param page
     * @param vo
     * @return
     */
    Page<StaffInfoVo> page(Page page, StaffInfoVo vo);

}
