package com.aurine.cloudx.estate.controller;
import com.aurine.cloudx.estate.constant.LoadStatusConstants;
import com.aurine.cloudx.estate.entity.ProjectDeviceLoadLogDetail;
import com.aurine.cloudx.estate.service.ProjectDeviceLoadLogDetailService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pig4cloud.pigx.common.core.util.R;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Api;
import javax.annotation.Resource;

/**
 * 设备导入日志明细(ProjectDeviceLoadLogDetail)表控制层
 *
 * @author 王良俊
 * @since 2021-06-03 10:49:52
 */
@RestController
@RequestMapping("projectDeviceLoadLogDetail")
@Api(value="projectDeviceLoadLogDetail",tags="设备导入日志明细")
public class ProjectDeviceLoadLogDetailController  {
    /**
     * 服务对象
     */
    @Resource
    private ProjectDeviceLoadLogDetailService projectDeviceLoadLogDetailService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param deviceLoadLogDetail 查询实体(这里只是获取记录明细所属的批次ID)
     * @return 所有数据
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询", notes = "分页查询projectDeviceLoadLogDetail所有数据")
    public R selectAll(Page<ProjectDeviceLoadLogDetail> page, ProjectDeviceLoadLogDetail deviceLoadLogDetail) {
        return R.ok(this.projectDeviceLoadLogDetailService.page(page, new LambdaQueryWrapper<ProjectDeviceLoadLogDetail>()
                .eq(ProjectDeviceLoadLogDetail::getLoadStatus, LoadStatusConstants.IMPORT_FAILED)
                .eq(ProjectDeviceLoadLogDetail::getBatchId, deviceLoadLogDetail.getBatchId())));
    }

}
