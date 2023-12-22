package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.entity.ProjectShiftConf;
import com.aurine.cloudx.estate.service.ProjectShiftConfService;
import com.aurine.cloudx.estate.vo.ProjectShiftConfPageVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;


/**
 * 班次配置
 *
 * @author guhl@aurine.cn
 * @date 2020-07-23 08:36:54
 */
@RestController
@AllArgsConstructor
@RequestMapping("/projectShiftConf")
@Api(value = "projectShiftConf", tags = "班次配置管理")
public class ProjectShiftConfController {

    private final ProjectShiftConfService projectShiftConfService;

    /**
     * 分页查询
     *
     * @param page                   分页对象
     * @param projectShiftConfPageVo 班次配置
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getProjectShiftConfPage(Page page, ProjectShiftConfPageVo projectShiftConfPageVo) {
        if (projectShiftConfPageVo.getStartTimeString() != null && !"".equals(projectShiftConfPageVo.getStartTimeString())) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            projectShiftConfPageVo.setStartTime(LocalDate.parse(projectShiftConfPageVo.getStartTimeString(), fmt));
        }
        if (projectShiftConfPageVo.getEndTimeString() != null && !"".equals(projectShiftConfPageVo.getEndTimeString())) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            projectShiftConfPageVo.setEndTime(LocalDate.parse(projectShiftConfPageVo.getEndTimeString(), fmt));
        }
        return R.ok(projectShiftConfService.pageShiftConf(page, projectShiftConfPageVo));
    }

    /**
     * 查询当前项目下所有班次信息(无分页)
     *
     * @return
     */
    @ApiOperation(value = "查询当前项目下所有班次信息(无分页)", notes = "查询当前项目下所有班次信息(无分页)")
    @GetMapping("/list")
    public R listProjectShiftConf() {
        return R.ok(projectShiftConfService.list());
    }

    /**
     * 通过id查询班次配置
     *
     * @param shiftId id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{shiftId}")
    public R getById(@PathVariable("shiftId") String shiftId) {
        return R.ok(projectShiftConfService.getById(shiftId));
    }

    /**
     * 新增班次配置
     *
     * @param projectShiftConf 班次配置
     * @return R
     */
    @ApiOperation(value = "新增班次配置", notes = "新增班次配置")
    @SysLog("新增班次配置")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('estate_shiftconf_add')")
    public R save(@RequestBody ProjectShiftConf projectShiftConf) {
        ProjectShiftConf projectShiftConf1 = projectShiftConfService.getByShiftName(projectShiftConf.getShiftName(), projectShiftConf.getShiftId());
        if (projectShiftConf1 != null) {
            throw new RuntimeException("班次名称已存在");
        }
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        projectShiftConf.setShiftId(uuid);
        return R.ok(projectShiftConfService.save(projectShiftConf));
    }

    /**
     * 修改班次配置
     *
     * @param projectShiftConf 班次配置
     * @return R
     */
    @ApiOperation(value = "修改班次配置", notes = "修改班次配置")
    @SysLog("修改班次配置")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('estate_shiftconf_edit')")
    public R updateProjectShiftConf(@RequestBody ProjectShiftConf projectShiftConf) {
        return R.ok(projectShiftConfService.updateProjectShiftConf(projectShiftConf));
    }

    /**
     * 通过id删除班次配置
     *
     * @param shiftId id
     * @return R
     */
    @ApiOperation(value = "通过id删除班次配置", notes = "通过id删除班次配置")
    @SysLog("通过id删除班次配置")
    @DeleteMapping("/{shiftId}")
    @PreAuthorize("@pms.hasPermission('estate_shiftconf_del')")
    public R removeById(@PathVariable("shiftId") String shiftId) {
        return R.ok(projectShiftConfService.removeByShiftId(shiftId));
    }

    /**
     * 根据班次名称查询
     *
     * @param shiftName
     * @return
     */
    @GetMapping("/getByShiftName")
    @ApiOperation(value = "根据班次名称查询", notes = "根据班次名称查询")
    public R getByShiftName(@RequestParam(value = "shiftName", required = false) String shiftName, @RequestParam(value = "shiftId", required = false) String shiftId) {
        return R.ok(projectShiftConfService.getByShiftName(shiftName, shiftId));
    }

}
