

package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.ProjectParkingInfo;
import com.aurine.cloudx.estate.service.ProjectParkingInfoService;
import com.aurine.cloudx.estate.thirdparty.module.parking.factory.ParkingFactoryProducer;
import com.aurine.cloudx.estate.vo.ProjectParkingInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * 停车场
 *
 * @author 王良俊
 * @date 2020-05-07 09:13:25
 */
@RestController
@AllArgsConstructor
@RequestMapping("/baseParkingArea")
@Api(value = "baseParkingArea", tags = "停车场管理")
public class ProjectParkingInfoController {

    private final ProjectParkingInfoService projectParkingInfoService;

    /**
     * 分页查询
     *
     * @param page 分页对象
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getParkingInfoPage(Page page, ProjectParkingInfoVo projectParkingInfo) {
        ProjectContextHolder.setProjectId(projectParkingInfo.getProjectId());
        return R.ok(projectParkingInfoService.findPage(page, projectParkingInfo));
    }

    /**
     * 获取当前项目下所有数据
     *
     * @return
     */
    @ApiOperation(value = "获取所有停车场信息的列表", notes = "获取所有停车场信息的列表")
    @GetMapping("/list")
    public R list() {
        return R.ok(projectParkingInfoService.list(new QueryWrapper<ProjectParkingInfo>()
                .lambda().eq(ProjectParkingInfo::getProjectId, ProjectContextHolder.getProjectId())));
    }

    /**
     * 通过id查询停车场
     *
     * @param parkId id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{parkId}")
    public R getById(@PathVariable("parkId") String parkId) {
        return R.ok(projectParkingInfoService.getById(parkId));
    }

    /**
     * 新增停车场
     *
     * @param parkingInfo 停车场
     * @return R
     * @author: 王良俊
     */
    @ApiOperation(value = "新增停车场", notes = "新增停车场")
    @SysLog("新增停车场")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('estate_parkinginfo_add')")
    public R save(@RequestBody ProjectParkingInfo parkingInfo) {
        boolean b = projectParkingInfoService.saveParkInfo(parkingInfo);
        if (b){
            try {
                projectParkingInfoService.syncPark(parkingInfo.getParkName(),parkingInfo.getProjectId());
            }catch (Exception e){
            }
        }
        return R.ok(b);

    }

    /**
     * 修改停车场
     *
     * @param parkingInfo 停车场
     * @return R
     */
    @ApiOperation(value = "修改停车场", notes = "修改停车场")
    @SysLog("修改停车场")
    @PutMapping
//    @PreAuthorize("@pms.hasPermission('estate_parkinginfo_edit')")
    public R updateById(@RequestBody ProjectParkingInfo parkingInfo) {
        //修改 添加编辑名称时重名校验 xull@aurine.cn 2020年7月7日 10点57分
        return R.ok(projectParkingInfoService.updateParkInfo(parkingInfo));
    }

    /**
     * 通过id删除停车场
     *
     * @param parkId uid
     * @return R
     */
    @ApiOperation(value = "通过id删除停车场", notes = "通过id删除停车场")
    @SysLog("通过id删除停车场")
    @DeleteMapping("/{parkId}/{projectId}")
    @PreAuthorize("@pms.hasPermission('estate_parkinginfo_del')")
    public R removeById(@PathVariable String parkId, @PathVariable Integer projectId) {
        ProjectContextHolder.setProjectId(projectId);
        return R.ok(projectParkingInfoService.removeParkById(parkId));
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
    public R getPayUrlById(@PathVariable("parkId") String parkId) {
        return R.ok(ParkingFactoryProducer.getFactory(parkId).getParkingService().getParkingPayUrl(parkId));
    }


}
