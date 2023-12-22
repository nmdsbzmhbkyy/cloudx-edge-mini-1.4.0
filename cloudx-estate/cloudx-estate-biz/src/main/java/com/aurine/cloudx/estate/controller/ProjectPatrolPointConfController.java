package com.aurine.cloudx.estate.controller;

import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.qrcode.QRCodeGenerateUtil;
import com.aurine.cloudx.estate.entity.ProjectPatrolPointConf;
import com.aurine.cloudx.estate.entity.ProjectPatrolRoutePointConf;
import com.aurine.cloudx.estate.service.ProjectPatrolPointConfService;
import com.aurine.cloudx.estate.service.ProjectPatrolRoutePointConfService;
import com.aurine.cloudx.estate.vo.ProjectPatrolPointConfVo;
import com.aurine.cloudx.estate.vo.QRCode;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 项目巡更点配置(ProjectPatrolPointConf)表控制层
 *
 * @author makejava
 * @since 2020-07-28 08:52:45
 */
@RestController
@RequestMapping("/projectPatrolPointConf")
@Api(value = "projectPatrolPointConf", tags = "项目巡更点配置")
public class ProjectPatrolPointConfController {
    /**
     * 服务对象
     */
    @Resource
    private ProjectPatrolPointConfService projectPatrolPointConfService;
    @Resource
    ProjectPatrolRoutePointConfService projectPatrolRoutePointConfService;

    /**
     * 分页查询
     *
     * @param page          分页对象
     * @param projectPatrolPointConfVo 项目巡更点
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R fetchList(Page page, ProjectPatrolPointConfVo projectPatrolPointConfVo) {
        projectPatrolPointConfVo.setProjectId(ProjectContextHolder.getProjectId());
        return R.ok(projectPatrolPointConfService.page(page, projectPatrolPointConfVo));
    }

    /**
     * 新增项目巡更点
     *
     * @param vo 项目巡更点
     * @return R
     */
    @ApiOperation(value = "新增项目巡更点", notes = "新增项目巡更点")
    @SysLog("新增项目巡更点")
    @PostMapping
    public R save(@RequestBody ProjectPatrolPointConfVo vo) {
        vo.setProjectId(ProjectContextHolder.getProjectId());
        return R.ok(projectPatrolPointConfService.save(vo));
    }

    /**
     * 通过id查询
     * @param pointId id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{pointId}" )
    public R getById(@PathVariable("pointId" ) String pointId) {
        return R.ok(projectPatrolPointConfService.getById(pointId));
    }

    /**
     * 修改项目巡更点
     *
     * @param projectPatrolPointConf 项目巡更点
     * @return R
     */
    @ApiOperation(value = "修改项目巡更点", notes = "修改项目巡更点")
    @SysLog("修改项目巡更点")
    @PutMapping
    public R update(@RequestBody ProjectPatrolPointConf projectPatrolPointConf) {
        return R.ok(projectPatrolPointConfService.updateById(projectPatrolPointConf));
    }

    /**
     * 删除项目巡更点
     */
    @ApiOperation(value = "删除项目巡更点", notes = "删除项目巡更点")
    @SysLog("修改项目巡更点")
    @DeleteMapping("/{pointId}" )
    public R delete(@PathVariable String pointId) {
        projectPatrolRoutePointConfService.remove(new QueryWrapper<ProjectPatrolRoutePointConf>().lambda().eq(ProjectPatrolRoutePointConf::getPatrolPointId, pointId));
        return R.ok(projectPatrolPointConfService.removeById(pointId));
    }

    /**
     * 批量删除项目巡更点
     */
    @ApiOperation(value = "批量删除项目巡更点", notes = "批量删除项目巡更点")
    @SysLog("批量删除项目巡更点")
    @DeleteMapping("/deleteBatchById" )
    public R deleteBatch(@RequestBody List<String> idList) {
        projectPatrolRoutePointConfService.remove(new QueryWrapper<ProjectPatrolRoutePointConf>().lambda().in(ProjectPatrolRoutePointConf::getPatrolPointId, idList));
        return R.ok(projectPatrolPointConfService.removeByIds(idList));
    }

    /**
     * 切换巡更点状态
     */
    @ApiOperation(value = "切换巡更点状态", notes = "切换巡更点状态")
    @SysLog("切换巡更点状态")
    @PutMapping("/updateStatusById/{pointId}" )
    public R updateByStatus(@PathVariable("pointId") String pointId) {
        return R.ok(projectPatrolPointConfService.updateStatusById(pointId));
    }


    /**
     * <p>
     * 生成巡检点二维码并返回给前端下载
     * </p>
     *
     * @param qrCodeVo 巡检点二维码vo对象存放二维码内容、二维码宽高
     * @author: wlj
     */
    @ApiOperation(value = "生成巡检点二维码并返回给前端下载", notes = "生成巡检点二维码并返回给前端下载")
    @SysLog("生成巡检点二维码并返回给前端下载")
    @PostMapping("/downloadQrCode")
    @Inner(false)
    public void test(QRCode qrCodeVo, HttpServletResponse response) {
        if (CollUtil.isNotEmpty(qrCodeVo.getPointIdList())) {
            String rgbStr = qrCodeVo.getRgbStr();
            String[] rgbArr = rgbStr.substring(4, rgbStr.length() - 1).split(", ");
            int qrCodeColor = new Color(Integer.parseInt(rgbArr[0]), Integer.parseInt(rgbArr[1]), Integer.parseInt(rgbArr[2])).getRGB();
            List<ProjectPatrolPointConf> pointConfList = projectPatrolPointConfService.list(new QueryWrapper<ProjectPatrolPointConf>()
                    .lambda().in(ProjectPatrolPointConf::getPointId, qrCodeVo.getPointIdList()));
            Map<String, String> pointMap = pointConfList.stream().collect(Collectors.toMap(ProjectPatrolPointConf::getPointId,
                    ProjectPatrolPointConf::getPointName));
            QRCodeGenerateUtil.getInstance().genQRCodeZip(response, pointMap, qrCodeVo.getWidth(), qrCodeVo.getHeight(),
                    qrCodeVo.getLogoFile(), qrCodeColor);
        }
    }


}