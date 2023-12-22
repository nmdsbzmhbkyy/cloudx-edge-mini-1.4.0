package com.aurine.cloudx.estate.open.project.controller;

import com.aurine.cloudx.estate.entity.SysDecided;
import com.aurine.cloudx.estate.open.project.fegin.RemoteSysDecideService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.api.client.util.ArrayMap;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/decided")
@Api(value = "decided", tags = "数据订阅")
public class SysDecidedController {

    @Resource
    private RemoteSysDecideService remoteSysDecideService;
    /**
     * 订阅的数据分页查询
     * @param page
     * @param sysDecided
     * @return
     */
    @ApiOperation(value = "已订阅的数据分页查询", notes = "已订阅的数据分页查询")
    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('decided:get:page')")
    public R getSysDecidedPage(Page page, SysDecided sysDecided) {
        Map<String, Object> pageMap = new ArrayMap<>();
        pageMap.put("size",page.getSize());
        pageMap.put("current",page.getCurrent());
        return remoteSysDecideService.getSysDecidedPage(pageMap);
    }

    /**
     * 订阅
     * @param sysDecided
     * @return R
     */
    @ApiOperation(value = "订阅", notes = "订阅")
    @SysLog("订阅" )
    @PostMapping("/subscription")
    @PreAuthorize("@pms.hasPermission('decided:post:subscription')")
    public R subscription(@RequestBody SysDecided sysDecided) {
        return remoteSysDecideService.subscription(sysDecided);
    }

    /**
     * 通过地址删除
     * @param id
     * @return
     */
    @ApiOperation(value = "删除订阅", notes = "删除订阅")
    @SysLog("删除订阅")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('decided:delete:remove')")
    public R remove(@PathVariable("id") Integer id) {
        return remoteSysDecideService.removeById(id);
    }
}
