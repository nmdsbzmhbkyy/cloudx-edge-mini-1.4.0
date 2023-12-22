package com.aurine.cloudx.estate.open.staff.fegin;

import com.aurine.cloudx.estate.dto.ProjectStaffDTO;
import com.aurine.cloudx.estate.open.staff.bean.ProjectStaffPage;
import com.aurine.cloudx.estate.vo.ProjectStaffVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

@FeignClient(contextId = "remoteProjectStaffService", value = "cloudx-estate-biz")
public interface RemoteProjectStaffService {
    
    @GetMapping("/projectStaff/get-owner")
    R<ProjectStaffVo> getOwner();
    
    /**
     * 分页查询
     *
     * @param page 分页对象
     * @return
     */
    @GetMapping("/projectStaff/page")
    R<Page<ProjectStaffVo>> getProjectStaffPage(@SpringQueryMap ProjectStaffPage page);
    
    /**
     * 通过id查询项目员工信息表
     *
     * @param id id
     * @return R
     */
    @GetMapping("/projectStaff/{id}")
    R getById(@PathVariable("id") String id);
    
    /**
     * 获取当前员工的信息
     *
     * @return R
     */
    @GetMapping("/projectStaff/get/staff")
    R getStaff();
    
    /**
     * 新增项目员工信息表
     *
     * @param projectStaff 项目员工信息表
     * @return R
     */
    @PostMapping
    R save(@RequestBody ProjectStaffDTO projectStaff);
    
    /**
     * 修改项目员工信息表
     *
     * @param projectStaff 项目员工信息表
     * @return R
     */
    @PutMapping
    R updateById(@RequestBody ProjectStaffDTO projectStaff);
    
    /**
     * 通过id删除项目员工信息表
     *
     * @param id id
     * @return R
     */
    @DeleteMapping("/projectStaff/{id}")
    R removeById(@PathVariable("id") String id);
    
    /**
     * 通过userId删除员工
     *
     * @param userId
     * @return
     */
    @DeleteMapping("/projectStaff/remove/staff/{userId}/{projectId}")
    R removeByUserId(@PathVariable("userId") Integer userId, @PathVariable("projectId") Integer projectId);
    
    /**
     * 通过手机号获取员工
     *
     * @param mobile
     * @return
     */
    @GetMapping("/projectStaff/check/mobile/{mobile}")
    R getProjectStaffPage(@PathVariable("mobile") String mobile);
    
    @GetMapping("/projectStaff/staff-tree")
    R findStaffTree();
    
    @GetMapping("/projectStaff/staff-tree/PlanId/{planId}")
    R findStaffTreeByPlanId(@PathVariable("planId") String planId);
    
    @GetMapping("/projectStaff/staff-tree/PatrolId/{patrolId}")
    R findStaffTreeByPatrolId(@PathVariable("patrolId") String patrolId);
    
    /**
     * <p>
     * 获取这个区域管辖的员工
     * </p>
     */
    @GetMapping("/projectStaff/get/staff/id/list/regionId")
    R getStaffIdListByRegionId(@RequestParam("regionId") String regionId);
    
    /**
     * <p>
     * 根据员工ID获取到这个员工的工作情况
     * </p>
     *
     * @param dateStr 所要查询的日期 为空默认查询全部
     * @param staffId 所要查询的员工的ID
     * @author: 王良俊
     */
    @GetMapping("/projectStaff/get/staff/on/mobile/{mobile}")
    R getWorkingConditionByStaffId(@RequestParam("staffId") String staffId, @RequestParam("date") String dateStr);
    
    /**
     * <p>
     * 只获取未管辖区域的员工
     * </p>
     */
    @GetMapping("/projectStaff/staff-tree/Region")
    R findStaffTreeUnJurisdiction(@RequestParam("regionId") String regionId);
    
}
