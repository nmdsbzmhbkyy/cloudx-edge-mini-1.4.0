

package com.aurine.cloudx.estate.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.aurine.cloudx.common.core.util.CronExpParserUtil;
import com.aurine.cloudx.estate.entity.ProjectMediaAd;
import com.aurine.cloudx.estate.entity.ProjectMediaAdDevCfg;
import com.aurine.cloudx.estate.entity.ProjectMediaAdPlaylist;
import com.aurine.cloudx.estate.service.ProjectMediaAdDevCfgService;
import com.aurine.cloudx.estate.service.ProjectMediaAdPlaylistService;
import com.aurine.cloudx.estate.service.ProjectMediaAdService;
import com.aurine.cloudx.estate.vo.ProjectMediaAdFormVo;
import com.aurine.cloudx.estate.vo.ProjectMediaAdVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 媒体广告表
 *
 * @author xull@aurine.cn
 * @date 2020-06-04 11:37:46
 */
@RestController
@AllArgsConstructor
@RequestMapping("/projectMediaAd")
@Api(value = "projectMediaAd", tags = "媒体广告表管理")
public class ProjectMediaAdController {

    private final ProjectMediaAdService projectMediaAdService;
    private final ProjectMediaAdPlaylistService projectMediaAdPlaylistService;
    private final ProjectMediaAdDevCfgService projectMediaAdDevCfgService;

    /**
     * 分页查询
     *
     * @param page                 分页对象
     * @param projectMediaAdFormVo 媒体广告表
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<Page<ProjectMediaAdVo>> getProjectMediaAdPage(Page page, ProjectMediaAdFormVo projectMediaAdFormVo) {
        //前端无法传递时间类型 故做转换 xull@aurine.cn 2020/5/22 18:40
        if (projectMediaAdFormVo.getFinishTimeString() != null && !"".equals(projectMediaAdFormVo.getFinishTimeString())) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            projectMediaAdFormVo.setFinishTime(LocalDate.parse(projectMediaAdFormVo.getFinishTimeString(), fmt));
        }
        if (projectMediaAdFormVo.getBeginTimeString() != null && !"".equals(projectMediaAdFormVo.getBeginTimeString())) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            projectMediaAdFormVo.setBeginTime(LocalDate.parse(projectMediaAdFormVo.getBeginTimeString(), fmt));
        }
        Page<ProjectMediaAdVo> mediaAdPage = projectMediaAdService.pageMediaAd(page, projectMediaAdFormVo);
        //转换播放频率为中文显示
        mediaAdPage.getRecords().forEach(e -> {
            e.setFrequencyString(CronExpParserUtil.translateToChinese(e.getFrequency()));
        });
        return R.ok(mediaAdPage);
    }


    /**
     * 通过id查询媒体广告表
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "媒体广告id", paramType = "path", required = true),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<ProjectMediaAdVo> getById(@PathVariable("id") String id) {
        ProjectMediaAd projectMediaAd = projectMediaAdService.getById(id);
        ProjectMediaAdVo projectMediaAdVo = new ProjectMediaAdVo();
        if (BeanUtil.isNotEmpty(projectMediaAd)) {
            BeanUtils.copyProperties(projectMediaAd, projectMediaAdVo);
            List<ProjectMediaAdPlaylist> projectMediaAdPlaylists = projectMediaAdPlaylistService.list(Wrappers.lambdaQuery(ProjectMediaAdPlaylist.class).eq(ProjectMediaAdPlaylist::getAdId, id));
            projectMediaAdVo.setProjectMediaAdPlaylistList(projectMediaAdPlaylists);
            return R.ok(projectMediaAdVo);
        } else {
            return R.ok(projectMediaAdVo);
        }

    }

    /**
     * 新增媒体广告表
     *
     * @param projectMediaAdFormVo 媒体广告表
     * @return R
     */
    @ApiOperation(value = "新增媒体广告表", notes = "新增媒体广告表")
    @SysLog("新增媒体广告表")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('estate_mediaad_add')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R save(@RequestBody ProjectMediaAdFormVo projectMediaAdFormVo) {

        if (CollectionUtil.isEmpty(projectMediaAdFormVo.getDeviceIds())) {
            return R.failed("设备不能为空");
        }
        if (CollectionUtil.isEmpty(projectMediaAdFormVo.getRepoIds())) {
            return R.failed("资源不能为空");
        }
        return R.ok(projectMediaAdService.saveMedia(projectMediaAdFormVo));
    }


    /**
     * 通过id删除媒体广告表
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除媒体广告表", notes = "通过id删除媒体广告表")
    @SysLog("通过id删除媒体广告表")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('estate_mediaad_del')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "媒体广告id", paramType = "path", required = true),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R removeById(@PathVariable String id) {
        return R.ok(projectMediaAdService.removeById(id));
    }


    @ApiOperation(value = "批量清除广告", notes = "批量清除广告")
    @SysLog("批量清除广告")
    @PostMapping("/clean")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R clean(@RequestBody ProjectMediaAdFormVo projectMediaAdFormVo) {
        List<String> deviceIds = projectMediaAdFormVo.getDeviceIds();
        if (ObjectUtil.isEmpty(deviceIds) || deviceIds.size() <= 0) {
            return R.failed("请选择需要清除的设备");
        }
        List<ProjectMediaAdDevCfg> projectMediaAdDevCfgs = projectMediaAdDevCfgService.getMediaAdDevCfgByDeviceIds(deviceIds);
        projectMediaAdService.cleanMedia(projectMediaAdDevCfgs);
        return R.ok();
    }

    @ApiOperation(value = "根据广告id批量清除广告", notes = "根据广告id批量清除广告")
    @SysLog("根据广告id批量清除广告")
    @PostMapping("/clean/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "媒体广告id", paramType = "path", required = true),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R clean(@PathVariable("id") String id) {
        List<ProjectMediaAdDevCfg> projectMediaAdDevCfgs = projectMediaAdDevCfgService.getMediaAdDevCfgByAdId(id);
        projectMediaAdService.cleanMedia(projectMediaAdDevCfgs);
        return R.ok();
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
            @ApiImplicitParam(name = "id", value = "媒体广告id", paramType = "path", required = true),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R resendAll(@PathVariable String id) {

        return R.ok(projectMediaAdService.resendAll(id));
    }

    /**
     * 重发
     *
     * @param deviceId 设备id
     * @param adId     广告发布id
     * @return R
     */
    @ApiOperation(value = "重发", notes = "重发")
    @SysLog("重发")
    @PutMapping("/resend/{adId}/{deviceId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "adId", value = "媒体广告id", paramType = "path", required = true),
            @ApiImplicitParam(name = "deviceId", value = "设备id", paramType = "path", required = true),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R resendAll(@PathVariable("adId") String adId, @PathVariable("deviceId") String deviceId) {

        return R.ok(projectMediaAdService.resend(adId, deviceId));
    }
    @ApiOperation(value = "异步查询相同媒体名称", notes = "异步查询相同媒体名称")
    @SysLog("异步查询相同媒体名称")
    @GetMapping("/getMediaAdByAdName")
    public R getMediaAdByAdName(ProjectMediaAd projectMediaAd){
        List<ProjectMediaAd> mediaAdByAdName = projectMediaAdService.getMediaAdByAdName(projectMediaAd.getAdName());
        return R.ok(mediaAdByAdName);
    }


}
