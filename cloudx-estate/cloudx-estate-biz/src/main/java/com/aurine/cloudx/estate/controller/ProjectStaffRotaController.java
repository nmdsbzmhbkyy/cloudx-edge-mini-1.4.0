package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.vo.ProjectStaffRotaFromVo;
import com.pig4cloud.pigx.common.core.util.R;


import com.aurine.cloudx.estate.service.ProjectStaffRotaService;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Api;

import javax.annotation.Resource;


/**
 * 项目员工值班表信息(ProjectStaffRota)表控制层
 *
 * @author guhl@aurine.cn
 * @since 2020-08-03 11:48:49
 */
@RestController
@RequestMapping("/projectStaffRota")
@Api(value = "projectStaffRota", tags = "项目员工值班表信息")
public class ProjectStaffRotaController {
    /**
     * 服务对象
     */
    @Resource
    private ProjectStaffRotaService projectStaffRotaService;

    /**
     * 查询项目员工值班表信息
     *
     * @return 所有数据
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询项目员工值班表信息", notes = "查询项目员工值班表信息所有数据")
    public R selectAll() {
        return R.ok(projectStaffRotaService.listStaffRota());
    }

    /**
     * 新增数据
     *
     * @param projectStaffRotaFromVo 实体对象
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation(value = "新增数据", notes = "新增员工值班表信息数据")
    public R insert(@RequestBody ProjectStaffRotaFromVo projectStaffRotaFromVo) {
        return R.ok(projectStaffRotaService.saveProjectStaffRota(projectStaffRotaFromVo));
    }

    /**
     * 修改数据
     *
     * @param projectStaffRotaFromVo 实体对象
     * @return 修改结果
     */
    @PutMapping
    @ApiOperation(value = "修改数据", notes = "修改员工值班表信息数据")
    public R update(@RequestBody ProjectStaffRotaFromVo projectStaffRotaFromVo) {
        return R.ok(projectStaffRotaService.updateProjectStaffRota(projectStaffRotaFromVo));
    }

    /**
     * 通过id删除员工值班表
     *
     * @param rotaId id
     * @return R
     */
    @ApiOperation(value = "通过id删除员工值班表", notes = "通过id删除员工值班表")
    @SysLog("通过id删除员工值班表")
    @DeleteMapping("/{rotaId}")
    //@PreAuthorize("@pms.hasPermission('')")
    public R removeById(@PathVariable("rotaId") String rotaId) {
        return R.ok(projectStaffRotaService.removeByRotaId(rotaId));
    }
}