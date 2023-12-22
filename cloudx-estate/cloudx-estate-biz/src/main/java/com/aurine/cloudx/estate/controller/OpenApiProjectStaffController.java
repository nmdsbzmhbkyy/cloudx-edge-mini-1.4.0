package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.common.core.exception.OpenApiServiceException;
import com.aurine.cloudx.estate.dto.OpenApiProjectStaffDto;
import com.aurine.cloudx.estate.entity.ProjectStaff;
import com.aurine.cloudx.estate.service.OpenApiProjectStaffService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: wrm
 * @Date: 2022/06/23 10:26
 * @Package: com.aurine.cloudx.estate.controller
 * @Version: 1.0
 * @Remarks:
 **/
@RestController
@RequestMapping("/open/staff/inner")
@Api(value = "openApiProjectStaff", tags = "openApi项目员工信息表管理", hidden = true)
public class OpenApiProjectStaffController {

    @Resource
    private OpenApiProjectStaffService openApiProjectStaffService;

    /**
     * 开放平台 复合接口
     * 涉及 新增员工信息，有人脸、卡时保存人脸、卡信息 下发用户绑定设备是保存设备权限
     *
     * @param projectStaffDto 项目员工信息表
     * @return R
     */
    @ApiOperation(value = "新增项目员工信息表", notes = "新增项目员工信息表")
    @SysLog("新增项目员工信息表")
    @PostMapping
    @Inner
    public R<OpenApiProjectStaffDto> saveStaff(@RequestBody OpenApiProjectStaffDto projectStaffDto) {
        try {
            return openApiProjectStaffService.saveStaff(projectStaffDto);
        } catch (OpenApiServiceException e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
    }

    /**
     * 修改项目员工信息
     *
     * @param projectStaffDto 项目员工信息表
     * @return R
     */
    @ApiOperation(value = "修改员工", notes = "修改员工姓名、性别、部门、角色、职位等")
    @SysLog("员工信息管理 - 员工修改")
    @PutMapping
    @Inner
    public R<OpenApiProjectStaffDto> updateStaff(@RequestBody OpenApiProjectStaffDto projectStaffDto) {
        try {
            return openApiProjectStaffService.updateStaff(projectStaffDto);
        } catch (OpenApiServiceException e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
    }

    /**
     * 通过id删除项目员工信息表(内部调用)
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除项目员工信息表(内部调用)", notes = "通过id删除项目员工信息表(内部调用)")
    @SysLog("通过id删除项目员工信息表(内部调用)")
    @DeleteMapping("/{id}")
    @Inner
    public R<String> removeStaff(@PathVariable("id") String id) {
        try {
            return openApiProjectStaffService.removeStaff(id);
        } catch (OpenApiServiceException e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
    }
}
