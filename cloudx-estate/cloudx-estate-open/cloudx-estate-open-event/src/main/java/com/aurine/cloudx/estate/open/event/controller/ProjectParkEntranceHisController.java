package com.aurine.cloudx.estate.open.event.controller;

import com.aurine.cloudx.estate.entity.ProjectParkEntranceHis;
import com.aurine.cloudx.estate.open.event.bean.ProjectParkEntranceHisVoPage;
import com.aurine.cloudx.estate.open.event.fegin.RemoteProjectParkEntranceHisService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 车场管理
 *
 * @author 黄阳光
 * @date 2020-07-07 15:10:26
 */
@RestController
@AllArgsConstructor
@RequestMapping("/park-entrance-his")
@Api(value = "parkEntranceHis", tags = "车辆通行历史")
public class ProjectParkEntranceHisController {

    @Resource
    private RemoteProjectParkEntranceHisService parkEntranceHisService;

    /**
     * 分页查询
     *
     * @param page 分页对象
     * @param
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('park-entrance-his:get:getParkEntranceHisPage')")
    public R getParkEntranceHisPage(ProjectParkEntranceHisVoPage page) {
        return parkEntranceHisService.getParkEntranceHisPage(page);
    }

    /**
     * 通过id查询车场管理
     *
     * @param parkOrderNo uid
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{parkOrderNo}")
    @ApiImplicitParam(name = "parkOrderNo", value = "车场管理id", paramType = "path", required = true)
    public R getById(@PathVariable("parkOrderNo") String parkOrderNo) {
        return parkEntranceHisService.getById(parkOrderNo);
    }

    /**
     * 新增车场管理
     *
     * @param po 车场管理
     * @return R
     */
    @ApiOperation(value = "新增车场管理", notes = "新增车场管理")
    @SysLog("新增车场管理")
    @PostMapping
    public R save(@RequestBody ProjectParkEntranceHis po) {
        return parkEntranceHisService.save(po);
    }

    /**
     * 修改车场管理
     *
     * @param projectParkEntranceHis 车场管理
     * @return R
     */
    @ApiOperation(value = "修改车场管理", notes = "修改车场管理")
    @SysLog("修改车场管理")
    @PutMapping
    public R updateById(@RequestBody ProjectParkEntranceHis projectParkEntranceHis) {
        return parkEntranceHisService.updateById(projectParkEntranceHis);
    }
}
