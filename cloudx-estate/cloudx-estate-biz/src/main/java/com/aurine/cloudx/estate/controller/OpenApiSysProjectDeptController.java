package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.common.core.exception.OpenApiServiceException;
import com.aurine.cloudx.estate.dto.OpenApiSysProjectDeptDto;
import com.aurine.cloudx.estate.service.OpenApiSysProjectDeptService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: wrm
 * @Date: 2022/06/29 10:35
 * @Package: com.aurine.cloudx.estate.controller
 * @Version: 1.0
 * @Remarks:
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/open/dept/inner")
@Api(value = "dept", tags = "部门信息管理", hidden = true)
public class OpenApiSysProjectDeptController {

    @Resource
    private OpenApiSysProjectDeptService openApiSysProjectDeptService;

    /**
     * 部门新增
     * 根据部门名称新增部门，返回新增对象
     *
     * @param sysProjectDeptDto 部门新增对象信息
     * @return
     */
    @ApiOperation(value = "新增部门信息", notes = "通过部门名称新增部门，并返回新增后部门信息")
    @SysLog("部门信息管理 - 部门新增")
    @PostMapping
    @Inner
    public R<OpenApiSysProjectDeptDto> openApiInnerSave(@RequestBody OpenApiSysProjectDeptDto sysProjectDeptDto) {
        try {
            return openApiSysProjectDeptService.deptSave(sysProjectDeptDto);
        } catch (OpenApiServiceException e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
    }

    /**
     * 部门修改
     * 修改部门名称
     *
     * @param sysProjectDeptDto 部门修改对象信息
     * @return R
     */
    @ApiOperation(value = "修改部门名称", notes = "修改部门名称，并返回修改后部门信息")
    @SysLog("部门信息管理 - 部门修改")
    @PutMapping
    @Inner
    public R<OpenApiSysProjectDeptDto> openApiInnerUpdate(@RequestBody OpenApiSysProjectDeptDto sysProjectDeptDto) {
        try {
            return openApiSysProjectDeptService.deptUpdate(sysProjectDeptDto);
        } catch (OpenApiServiceException e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
    }

    /**
     * 部门删除
     * 根据部门名称/Id删除部门信息，关联人员的部门不允许删除
     *
     * @param sysProjectDeptDto 部门删除对象信息
     * @return R
     */
    @ApiOperation(value = "删除部门信息", notes = "根据部门名称/Id删除部门信息，关联人员的部门不允许删除，返回删除的部门id")
    @SysLog("部门信息管理 - 部门删除")
    @DeleteMapping
    @Inner
    public R<Integer> openApiInnerRemove(@RequestBody OpenApiSysProjectDeptDto sysProjectDeptDto) {
        try {
            return openApiSysProjectDeptService.deptRemove(sysProjectDeptDto);
        } catch (OpenApiServiceException e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
    }
}
