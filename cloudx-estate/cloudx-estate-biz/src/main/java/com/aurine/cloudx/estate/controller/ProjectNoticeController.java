
package com.aurine.cloudx.estate.controller;

import cn.hutool.core.util.ObjectUtil;
import com.aurine.cloudx.estate.entity.ProjectNotice;
import com.aurine.cloudx.estate.service.ProjectNoticeService;
import com.aurine.cloudx.estate.vo.ProjectNoticeAddVo;
import com.aurine.cloudx.estate.vo.ProjectNoticeCleanVo;
import com.aurine.cloudx.estate.vo.ProjectNoticeFormVo;
import com.aurine.cloudx.estate.vo.ProjectNoticeVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


/**
 * 信息发布
 *
 * @author guhl@aurine.cn
 * @date 2020-05-20 11:52:46
 */
@RestController
@RequestMapping("/projectNotice")
@Api(value = "projectNotice", tags = "信息发布管理")
public class ProjectNoticeController {
    @Resource
    private ProjectNoticeService projectNoticeService;

    /**
     * 分页查询
     *
     * @param page                分页对象
     * @param projectNoticeFormVo 信息发布
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<Page<ProjectNoticeVo>> getProjectNoticePage(Page page, ProjectNoticeFormVo projectNoticeFormVo) {
        //前端无法传递时间类型 故做转换 xull@aurine.cn 2020/5/22 18:40
        if (projectNoticeFormVo.getEndTimeString() != null && !"".equals(projectNoticeFormVo.getEndTimeString())) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            projectNoticeFormVo.setEndTime(LocalDate.parse(projectNoticeFormVo.getEndTimeString(), fmt));
        }
        if (projectNoticeFormVo.getStartTimeString() != null && !"".equals(projectNoticeFormVo.getStartTimeString())) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            projectNoticeFormVo.setStartTime(LocalDate.parse(projectNoticeFormVo.getStartTimeString(), fmt));
        }
        return R.ok(projectNoticeService.pageNotice(page, projectNoticeFormVo));
    }

    /**
     * 一键重发
     *
     * @param id 信息发布id
     * @return R
     */
    @ApiOperation(value = "一键重发", notes = "一键重发")
    @SysLog("一键重发")
    @PutMapping("/resendAll/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "信息发布id", paramType = "path", required = true),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R resendAll(@PathVariable String id) {

        return R.ok(projectNoticeService.resendAll(id));
    }
    /**
     * 一键重发
     *
     * @param id 信息发布id
     * @return R
     */
    @ApiOperation(value = "一键重发", notes = "一键重发")
    @SysLog("一键重发")
    @PutMapping("/inner/resendAll/{id}")
    @Inner
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "信息发布id", paramType = "path", required = true),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R innerResendAll(@PathVariable String id) {

        return R.ok(projectNoticeService.resendAll(id));
    }

    /**
     * 新增信息发布
     *
     * @param projectNoticeAddVo 信息发布
     * @return R
     */
    @ApiOperation(value = "新增信息发布", notes = "新增信息发布")
    @SysLog("新增信息发布")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('estate_notice_add')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R saveByProjectIds(@RequestBody ProjectNoticeAddVo projectNoticeAddVo) {
        //发布时设置发布时间为当前时间 xull@aurine.cn 2020年5月22日 10点19分
        projectNoticeAddVo.setPubTime(LocalDateTime.now());
        projectNoticeService.sendVo(projectNoticeAddVo);
        return R.ok();
    }

    /**
     * 新增信息发布
     *
     * @param projectNoticeAddVo 信息发布
     * @return R
     */
    @ApiOperation(value = "新增信息发布", notes = "新增信息发布")
    @SysLog("新增信息发布")
    @PostMapping("/inner")
    @Inner
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R innerSaveByProjectIds(@RequestBody ProjectNoticeAddVo projectNoticeAddVo) {
        //发布时设置发布时间为当前时间 xull@aurine.cn 2020年5月22日 10点19分
        projectNoticeAddVo.setPubTime(LocalDateTime.now());
        projectNoticeService.sendVo(projectNoticeAddVo);
        return R.ok();
    }

    /**
     * 批量新增信息发布
     *
     * @param projectNoticeAddVo 信息发布
     * @return R
     */
    @ApiOperation(value = "批量新增信息发布", notes = "批量新增信息发布")
    @SysLog("批量新增信息发布")
    @PostMapping("/sendBatch")
    @PreAuthorize("@pms.hasPermission('estate_notice_add')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R sendBatch(@RequestBody ProjectNoticeAddVo projectNoticeAddVo) {
        projectNoticeAddVo.setPubTime(LocalDateTime.now());
        return R.ok( projectNoticeService.sendBatch(projectNoticeAddVo));
    }

    /**
     * 通过id查询信息发布
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "信息发布id", paramType = "path", required = true),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<ProjectNotice> getById(@PathVariable("id") String id) {
        return R.ok(projectNoticeService.getById(id));
    }


    @ApiOperation(value = "通过id删除信息发布", notes = "通过id删除信息发布")
    @SysLog("通过id删除信息发布")
    @DeleteMapping("/{id}")
//    @PreAuthorize("@pms.hasPermission('estate_notice_del')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "信息发布id", paramType = "path", required = true),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R removeById(@PathVariable String id) {
        return R.ok(projectNoticeService.removeNoticeById(id));
    }

    @ApiOperation(value = "批量清除公告", notes = "批量清除公告")
    @SysLog("批量清除公告")
    @PostMapping("/clean")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R clean(@RequestBody ProjectNoticeCleanVo projectNoticeCleanVo) {
        List<String> deviceIds = projectNoticeCleanVo.getDeviceIds();
        if (ObjectUtil.isEmpty(deviceIds) || deviceIds.size() <= 0) {
            return R.failed("请选择需要清除的设备");
        }
        projectNoticeService.removeNotice(deviceIds);
        return R.ok();
    }
    @ApiOperation(value = "批量清除公告", notes = "批量清除公告")
    @SysLog("批量清除公告")
    @PostMapping("/inner/clean")
    @Inner
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R innerClean(@RequestBody ProjectNoticeCleanVo projectNoticeCleanVo) {
        List<String> deviceIds = projectNoticeCleanVo.getDeviceIds();
        if (ObjectUtil.isEmpty(deviceIds) || deviceIds.size() <= 0) {
            return R.failed("请选择需要清除的设备");
        }
        projectNoticeService.removeNotice(deviceIds);
        return R.ok();
    }


    /**
     * 重发
     *
     * @param deviceId 设备id
     * @param noticeId 信息发布id
     * @return R
     */
    @ApiOperation(value = "重发", notes = "重发")
    @SysLog("重发")
    @PutMapping("/resend/{noticeId}/{deviceId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "noticeId", value = "信息发布id", paramType = "path", required = true),
            @ApiImplicitParam(name = "deviceId", value = "设备id", paramType = "path", required = true),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R resendAll(@PathVariable("noticeId") String noticeId, @PathVariable("deviceId") String deviceId) {
        return R.ok(projectNoticeService.resend(noticeId, deviceId));
    }

    /**
     * 重发
     *
     * @param deviceId 设备id
     * @param noticeId 信息发布id
     * @return R
     */
    @ApiOperation(value = "重发", notes = "重发")
    @SysLog("重发")
    @PutMapping("/inner/resend/{noticeId}/{deviceId}")
    @Inner
    @ApiImplicitParams({
            @ApiImplicitParam(name = "noticeId", value = "信息发布id", paramType = "path", required = true),
            @ApiImplicitParam(name = "deviceId", value = "设备id", paramType = "path", required = true),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R innerResendAll(@PathVariable("noticeId") String noticeId, @PathVariable("deviceId") String deviceId) {
        return R.ok(projectNoticeService.resend(noticeId, deviceId));
    }


}
