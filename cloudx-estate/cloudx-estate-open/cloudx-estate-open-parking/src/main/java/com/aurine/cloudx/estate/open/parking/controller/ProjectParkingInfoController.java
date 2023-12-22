package com.aurine.cloudx.estate.open.parking.controller;

import com.aurine.cloudx.estate.entity.ProjectParkingInfo;
import com.aurine.cloudx.estate.open.parking.bean.ParkingPage;
import com.aurine.cloudx.estate.open.parking.fegin.RemoteProjectParkingInfoService;
import com.aurine.cloudx.estate.vo.ProjectParkingInfoVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/parking")
@Api(value = "parking", tags = "停车场管理")
public class ProjectParkingInfoController {

    private RemoteProjectParkingInfoService remoteProjectParkingInfoService;

    /**
     * 分页查询
     *
     * @param page 分页对象
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('parking:get:page')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<Page<ProjectParkingInfoVo>> getParkingInfoPage(ParkingPage page) {
        return null;
    }

    /**
     * 获取当前项目下所有数据
     *
     * @return
     */
    @ApiOperation(value = "获取所有停车场信息的列表", notes = "获取所有停车场信息的列表")
    @GetMapping("/list")
    @PreAuthorize("@pms.hasPermission('parking:get:list')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<List<ProjectParkingInfo>> list() {
        return null;
    }


    /**
     * 通过id查询停车场
     *
     * @param parkId id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{parkId}")
    @PreAuthorize("@pms.hasPermission('parking:get:info')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<ProjectParkingInfo> getById(@PathVariable("parkId") String parkId) {
        return null;
    }

    /**
     * 新增停车场
     *
     * @param parkingInfo 停车场
     * @return R
     * @author: 王良俊
     */
    @ApiOperation(value = "新增停车场", notes = "新增停车场")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('parking:post:save')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<Boolean> save(@RequestBody ProjectParkingInfo parkingInfo) {
        return null;
    }

    /**
     * 修改停车场
     *
     * @param parkingInfo 停车场
     * @return R
     */
    @ApiOperation(value = "修改停车场", notes = "修改停车场")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('parking:put:info')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<Boolean> updateById(@RequestBody ProjectParkingInfo parkingInfo) {
        return null;
    }

    /**
     * 通过id删除停车场
     *
     * @param parkId uid
     * @return R
     */
    @ApiOperation(value = "通过id删除停车场", notes = "通过id删除停车场")
    @DeleteMapping("/{parkId}/{projectId}")
    @PreAuthorize("@pms.hasPermission('parking:delete:info')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "parkId", value = "停车场ID", required = true, paramType = "path"),
            @ApiImplicitParam(name = "projectId", value = "小区Id", required = true, paramType = "path"),
    })
    public R<Boolean> removeById(@PathVariable String parkId, @PathVariable Integer projectId) {
        return null;
    }


    /**
     * 通过id查询停车场支付二维码
     *
     * @param parkId id
     * @return R
     * @author: 王伟
     * @since 2020-09-10
     */
    @ApiOperation(value = "通过id查询停车场支付二维码", notes = "通过id查询停车场支付二维码")
    @GetMapping("/payImgUrl/{parkId}")
    @PreAuthorize("@pms.hasPermission('parking:get:pay-img-url')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "parkId", value = "停车场ID", required = true, paramType = "path"),
    })
    public R<String> getPayUrlById(@PathVariable("parkId") String parkId) {
        return null;
    }

}
