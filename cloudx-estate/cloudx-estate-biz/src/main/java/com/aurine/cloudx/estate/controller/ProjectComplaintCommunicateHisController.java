package com.aurine.cloudx.estate.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.aurine.cloudx.estate.entity.ProjectComplaintCommunicateHis;
import com.aurine.cloudx.estate.service.ProjectComplaintCommunicateHisService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 投诉沟通历史记录
 *
 * @author guhl@aurine.cn
 * @date 2020-07-20 13:39:06
 */
@RestController
@AllArgsConstructor
@RequestMapping("/projectComplaintCommunicateHis")
@Api(value = "projectComplaintCommunicateHis", tags = "投诉沟通历史记录管理")
public class ProjectComplaintCommunicateHisController {

    private final ProjectComplaintCommunicateHisService projectComplaintCommunicateHisService;

    /**
     * 根据投诉单号查询所有沟通记录
     *
     * @param complaintId 投诉单号
     * @return
     */
    @ApiOperation(value = "根据投诉单号查询所有沟通记录", notes = "根据投诉单号查询所有沟通记录")
    @GetMapping("/listCommunicateHisBycomplaintId/{complaintId}")
    public R getProjectComplaintCommunicateHisPage(@PathVariable("complaintId") String complaintId) {
        return R.ok(projectComplaintCommunicateHisService.list(Wrappers.lambdaQuery(ProjectComplaintCommunicateHis.class)
                .eq(ProjectComplaintCommunicateHis::getComplaintId, complaintId)));
    }

    /**
     * 新增投诉沟通历史记录
     *
     * @param projectComplaintCommunicateHis 投诉沟通历史记录
     * @return R
     */
    @ApiOperation(value = "新增投诉沟通历史记录", notes = "新增投诉沟通历史记录")
    @SysLog("新增投诉沟通历史记录")
    @PostMapping
    //@PreAuthorize("@pms.hasPermission('estate_projectcomplaintcommunicatehis_add')")
    public R save(@RequestBody ProjectComplaintCommunicateHis projectComplaintCommunicateHis) {
        return R.ok(projectComplaintCommunicateHisService.save(projectComplaintCommunicateHis));
    }


}
