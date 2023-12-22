

package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.entity.ProjectAlarmHandle;
import com.aurine.cloudx.estate.service.ProjectAlarmHandleService;
import com.aurine.cloudx.estate.vo.ProjectEntranceAlarmEventVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * 报警事件处理
 *
 * @author 黄阳光
 * @date 2020-06-04 08:31:21
 */
@RestController
@AllArgsConstructor
@RequestMapping("/projectalarmhandle" )
@Api(value = "projectalarmhandle", tags = "报警事件处理管理")
public class ProjectAlarmHandleController {

    private final  ProjectAlarmHandleService projectAlarmHandleService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param projectAlarmHandle 报警事件处理
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getProjectAlarmHandlePage(Page page, ProjectAlarmHandle projectAlarmHandle) {

        return R.ok(projectAlarmHandleService.page(page, Wrappers.query(projectAlarmHandle)));
    }


    /**
     * 通过id查询报警事件处理
     * @param seq id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{seq}" )
    public R getById(@PathVariable("seq" ) Integer seq) {
        return R.ok(projectAlarmHandleService.getById(seq));
    }

    /**
     * 新增报警事件处理
     * @param projectAlarmHandleVo 报警事件处理
     * @return R
     */
    @ApiOperation(value = "新增报警事件处理", notes = "新增报警事件处理")
    @SysLog("新增报警事件处理" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('estate_projectalarmhandle_add')" )
    public R save(@RequestBody ProjectEntranceAlarmEventVo projectAlarmHandleVo) {
        return R.ok(projectAlarmHandleService.save(projectAlarmHandleVo));
    }

    /**
     * 修改报警事件处理
     * @param projectAlarmHandle 报警事件处理
     * @return R
     */
    @ApiOperation(value = "修改报警事件处理", notes = "修改报警事件处理")
    @SysLog("修改报警事件处理" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('estate_projectalarmhandle_edit')" )
    public R updateById(@RequestBody ProjectAlarmHandle projectAlarmHandle) {
        return R.ok(projectAlarmHandleService.updateById(projectAlarmHandle));
    }

    /**
     * 通过id删除报警事件处理
     * @param seq id
     * @return R
     */
    @ApiOperation(value = "通过id删除报警事件处理", notes = "通过id删除报警事件处理")
    @SysLog("通过id删除报警事件处理" )
    @DeleteMapping("/{seq}" )
    @PreAuthorize("@pms.hasPermission('estate_projectalarmhandle_del')" )
    public R removeById(@PathVariable Integer seq) {
        return R.ok(projectAlarmHandleService.removeById(seq));
    }

}
