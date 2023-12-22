package com.aurine.cloudx.estate.controller;
import com.aurine.cloudx.estate.entity.ProjectDeviceLoadLog;
import com.aurine.cloudx.estate.service.ProjectDeviceLoadLogService;
import com.aurine.cloudx.estate.vo.ProjectDeviceLoadLogVo;
import com.pig4cloud.pigx.common.core.util.R;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper; 
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Api;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 设备导入日志(ProjectDeviceLoadLog)表控制层
 *
 * @author 王良俊
 * @since 2021-06-03 10:49:35
 */
@RestController
@RequestMapping("projectDeviceLoadLog")
@Api(value="projectDeviceLoadLog",tags="设备导入日志")
public class ProjectDeviceLoadLogController  {
    /**
     * 服务对象
     */
    @Resource
    private ProjectDeviceLoadLogService projectDeviceLoadLogService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @return 所有数据
     */
    @GetMapping("/page/{deviceType}")
    @ApiOperation(value = "分页查询", notes = "分页查询projectDeviceLoadLog所有数据")
    public R selectAll(Page<ProjectDeviceLoadLogVo> page, @PathVariable("deviceType") String deviceType) {
        return R.ok(this.projectDeviceLoadLogService.fetchList(page, deviceType));
    }


    /**
     * 获取导入失败的Excel文件的临时下载链接
     *
     * @param batchId 根据批次ID获取这个批次的失败Excel下载链接
     * @return 所有数据
     */
    @GetMapping("/getFailedExcelLink/{batchId}")
    @ApiOperation(value = "下载失败文件", notes = "获取导入失败的Excel文件的临时下载链接")
    @Inner(false)
    public void selectAll(@PathVariable("batchId") String batchId, HttpServletResponse response) {
        this.projectDeviceLoadLogService.getFailedExcelLink(batchId, response);
    }

}
