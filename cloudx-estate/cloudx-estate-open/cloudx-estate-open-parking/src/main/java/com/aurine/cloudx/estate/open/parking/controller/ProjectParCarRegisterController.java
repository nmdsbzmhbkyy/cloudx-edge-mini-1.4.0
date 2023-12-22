package com.aurine.cloudx.estate.open.parking.controller;

import com.aurine.cloudx.estate.open.parking.bean.ProjectParCarRegisterSeachConditionVoPage;
import com.aurine.cloudx.estate.open.parking.fegin.RemoteProjectParCarRegisterService;
import com.aurine.cloudx.estate.vo.ProjectParCarRegisterRecordVo;
import com.aurine.cloudx.estate.vo.ProjectParCarRegisterVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 车辆登记
 *
 * @author 王伟
 * @date 2020-07-08 14:10:32
 */
@RestController
@AllArgsConstructor
@RequestMapping("/par-car-register")
@Api(value = "par-car-register", tags = "车辆登记管理")
public class ProjectParCarRegisterController {

    private RemoteProjectParCarRegisterService projectParCarRegisterService;

    /**
     * 分页查询
     *
     * @param page 分页对象
     * @param
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @PreAuthorize("@pms.hasPermission('par-car-register:get:page')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @GetMapping("/page")
    public R<Page<ProjectParCarRegisterRecordVo>> getProjectParCarRegisterPage(ProjectParCarRegisterSeachConditionVoPage page) {
        return projectParCarRegisterService.getProjectParCarRegisterPage(page);
    }


    /**
     * 通过id查询车辆登记
     *
     * @param registerId id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{registerId}")
    @PreAuthorize("@pms.hasPermission('par-car-register:get:info')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "registerId", value = "车辆登记管理id", required = true, paramType = "path"),
    })
    public R<ProjectParCarRegisterVo> getById(@PathVariable("registerId") String registerId) {
        return projectParCarRegisterService.getById(registerId);
    }
//
//    /**
//     * 通过车牌号查询车辆以及所有人的信息
//     *
//     * @param plateNumber 通过车牌号查询
//     * @return R
//     */
//    @ApiOperation(value = "通过id查询", notes = "通过车牌号查询")
//    @GetMapping("/getCarVoByPlateNumber/{plateNumber}")
//    @PreAuthorize("@pms.hasPermission('par-car-register:get:info')")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
//            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
//            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
//            @ApiImplicitParam(name = "plateNumber", value = "车牌号", required = true, paramType = "path"),
//    })
//    public R getCarVoByPlateNumber(@PathVariable("plateNumber") String plateNumber) {
//        return projectParCarRegisterService.getCarVoByPlateNumber(plateNumber);
//    }

    /**
     * 新增车辆登记
     *
     * @param projectParCarRegister 车辆登记
     * @return R
     */
    @ApiOperation(value = "新增车辆登记", notes = "新增车辆登记")
    @SysLog("新增车辆登记")
    @PreAuthorize("@pms.hasPermission('par-car-register:post:save')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @PostMapping
    public R<Boolean> register(@RequestBody ProjectParCarRegisterVo projectParCarRegister) {

        return projectParCarRegisterService.register(projectParCarRegister);
    }


    /**
     * 修改车辆登记
     *
     * @param projectParCarRegister 车辆登记
     * @return R
     */
    @ApiOperation(value = "修改车辆登记", notes = "修改车辆登记")
    @SysLog("修改车辆登记")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('par-car-register:put:info')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<Boolean> updateById(@RequestBody ProjectParCarRegisterVo projectParCarRegister) {
        return projectParCarRegisterService.updateById(projectParCarRegister);
    }

//    /**
//     * 通过id删除车辆登记
//     *
//     * @param id id
//     * @return R
//     */
//    @ApiOperation(value = "通过id删除车辆登记", notes = "通过id删除车辆登记")
//    @SysLog("通过id删除车辆登记")
//    @DeleteMapping("/{id}")
//    @PreAuthorize("@pms.hasPermission('par-car-register:delete:info')")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
//            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
//            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
//            @ApiImplicitParam(name = "plateNumber", value = "车牌号", required = true, paramType = "path"),
//    })
//    public R<Boolean> removeById(@PathVariable String id) {
//        return projectParCarRegisterService.removeById(id);
//    }

    /**
     * 注销车辆登记
     *
     * @param registerId id
     * @return R
     */
    @ApiOperation(value = "通过id删除车辆登记", notes = "通过id删除车辆登记")
    @SysLog("通过id删除车辆登记")
    @GetMapping("/cancelCarRegister/{registerId}")
    @PreAuthorize("@pms.hasPermission('par-car-register:delete:info')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "plateNumber", value = "车牌号", required = true, paramType = "path"),
    })
    public R cancelCarRegister(@PathVariable String registerId) {
        return projectParCarRegisterService.cancelCarRegister(registerId);
    }


//    /**
//     * 注销车辆登记
//     *
//     * @param cancelRegisterIdList id
//     * @return R
//     */
//    @ApiOperation(value = "通过id删除车辆登记", notes = "通过id删除车辆登记")
//    @SysLog("通过id删除车辆登记")
//    @PostMapping("/cancelCarRegisterList")
//    public R cancelCarRegisterList(@RequestBody List<String> cancelRegisterIdList) {
//        return this.projectParCarRegisterService.cancelCarRegisterList(cancelRegisterIdList);
//    }

    /**
     * 延期租赁时间
     *
     * @param vo 登记vo对象
     * @return R
     */
    @ApiOperation(value = "延期租赁时间", notes = "延期租赁时间")
    @SysLog("更新租赁时间")
    @PostMapping("/delay")
    @PreAuthorize("@pms.hasPermission('par-car-register:post:delay')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
    })
    public R<Boolean> delay(@RequestBody ProjectParCarRegisterVo vo) {
        return projectParCarRegisterService.delay(vo);
    }

//    /**
//     * 判断车位是否已被登记
//     *
//     * @param placeId 车位ID
//     * @return R
//     */
//    @ApiOperation(value = "判断车位是否已被登记", notes = "判断车位是否已被登记")
//    @SysLog("判断车位是否已被登记")
//    @GetMapping("/checkHasRegister/{placeId}")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "placeId", value = "车位id", required = true, paramType = "path"),
//    })
//    public R checkHasRegister(@PathVariable("placeId") String placeId) {
//        return projectParCarRegisterService.checkHasRegister(placeId);
//    }

//
//    /**
//     * 判断车牌号是否被登记过了
//     *
//     * @param plateNumber 车牌号
//     * @return R
//     */
//    @ApiOperation(value = "判断车牌号是否被登记过了", notes = "判断车牌号是否被登记过了")
//    @SysLog("判断车牌号是否被登记过了")
//    @GetMapping("/checkIsRegister/{plateNumber}")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "plateNumber", value = "车牌号", required = true, paramType = "path"),
//    })
//    public R checkIsRegister(@PathVariable("plateNumber") String plateNumber) {
//
//        return projectParCarRegisterService.checkIsRegister(plateNumber);
//    }

//    /**
//     * 判断当前项目是否已开启一位多车
//     *
//     * @return R
//     */
//    @ApiOperation(value = "判断当前项目是否已开启一位多车", notes = "判断当前项目是否已开启一位多车")
//    @SysLog("判断当前项目是否已开启一位多车")
//    @GetMapping("/isAlreadyAMultiCar")
//    public R isAlreadyAMultiCar() {
//        return projectParCarRegisterService.isAlreadyAMultiCar();
//    }


}
