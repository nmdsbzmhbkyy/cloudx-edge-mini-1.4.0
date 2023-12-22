package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.common.entity.vo.StaffInfoVo;
import com.aurine.cloudx.open.origin.entity.ProjectStaff;
import com.aurine.cloudx.open.origin.vo.ProjectStaffListVo;
import com.aurine.cloudx.open.origin.vo.StaffRegionVo;
import com.aurine.cloudx.open.origin.vo.SysUserVo;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 项目员工信息表
 *
 * @author lingang
 * @date 2020-05-11 13:38:09
 */
@Mapper
public interface ProjectStaffMapper extends BaseMapper<ProjectStaff> {
    Page<ProjectStaffListVo> pageAll(Page page, @Param("name") String name, @Param("projectId") Integer projectId);

    /**
     * 根据用户id更新手机号
     *
     * @param phone
     * @param userId
     */
    @SqlParser(filter = true)
    void updatePhoneByUserId(@Param("phone") String phone, @Param("userId") Integer userId);

    /**
     * 根据手机号更新用户id
     *
     * @param phone
     * @param userId
     */
    @SqlParser(filter = true)
    void updateUserIdByPhone(@Param("phone") String phone, @Param("userId") Integer userId);

    /**
     * <p>
     * 获取未管辖区域的员工ID集合
     * </p>
     */
    List<StaffRegionVo> getManagerRegionStaffIdList(@Param("projectId") Integer projectId);

    /**
     * <p>
     * 根据设备区域ID获取到管辖这个区域的员工ID
     * </p>
     *
     * @param regionId 设备区域ID
     */
    List<String> getStaffIdByRegionId(@Param("regionId") String regionId);

    Page<ProjectStaffListVo> getStaffPage(Page page, @Param("grade") String grade, @Param("departmentId") Integer departmentId, @Param("staffId") String staffId, @Param("name") String name);

    @SqlParser(filter = true)
    boolean removeByUserId(@Param("userId") Integer userId, @Param("projectId") Integer projectId);

    @SqlParser(filter = true)
    ProjectStaff getStaffByUserId(@Param("userId") Integer userId, @Param("projectId") Integer projectId);

    @SqlParser(filter = true)
    List<SysUserVo> getUserVosByDeptId(@Param("projectId") Integer projectId, @Param("roleName") String roleName);

    /**
     * <p>
     * 获取当前项目有指定菜单权限的员工ID
     * </p>
     *
     * @param
     * @return
     * @author: 王良俊
     */
    List<String> getStaffIdListByMenuId(@Param("menuId") Integer menuId, @Param("projectId") Integer projectId);

    @SqlParser(filter = true)
    List<ProjectStaff> getStaffListByUserId(Integer userId);

    @SqlParser(filter = true)
    Integer getDepartmentId(@Param("projectId") Integer projectId);

    @SqlParser(filter = true)
    Integer getRoleId(@Param("projectId") Integer projectId);

    /**
     * 多条件分页查询
     *
     * @param page
     * @param po
     * @return
     */
    Page<StaffInfoVo> page(Page page, @Param("query") ProjectStaff po);
}
