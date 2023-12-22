package com.aurine.cloudx.estate.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.AuditStatusEnum;
import com.aurine.cloudx.estate.entity.ProjectCarPreRegister;
import com.aurine.cloudx.estate.entity.ProjectParCarRegister;
import com.aurine.cloudx.estate.entity.ProjectPersonInfo;
import com.aurine.cloudx.estate.feign.RemoteCarRegisterService;
import com.aurine.cloudx.estate.feign.RemoteParCarRegisterService;
import com.aurine.cloudx.estate.feign.RemoteWebSocketService;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("car")
@Api(value = "car", tags = "车辆管理")
public class ProjectCarPreRegisterController {

    @Resource
    private ProjectCarPreRegisterService projectCarPreRegisterService;

    @Resource
    private ProjectPersonInfoService projectPersonInfoService;

    @Resource
    private ProjectParCarRegisterService ProjectParCarRegisterService;

    @Resource
    private RemoteCarRegisterService remoteCarRegisterService;

    @Autowired
    private RemoteParCarRegisterService remoteParCarRegisterService;

    @Resource
    private ProjectCarInfoService projectCarInfoService;

    @Resource
    private ProjectParkEntranceHisService parkEntranceHisService;
    @Resource
    private RemoteWebSocketService remoteWebSocketService;

    @PostMapping("/register")
    @ApiOperation(value = "车辆登记", notes = "车辆登记")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", required = true, value = "小区Id", paramType = "header")
    })
    public R<Boolean> register (@RequestBody AppCarRegisterFormVo carRegisterFormVo) {
        List<ProjectParCarRegister> listByPlateNumber = ProjectParCarRegisterService.list(new QueryWrapper<ProjectParCarRegister>().lambda()
                .eq(ProjectParCarRegister::getPlateNumber, carRegisterFormVo.getPlateNumber()));
        if (ObjectUtil.isNotEmpty(listByPlateNumber)) {
            return R.failed("车牌号已被登记");
        }
        List<ProjectCarPreRegister> projectCarPreRegisterList = projectCarPreRegisterService.list(new QueryWrapper<ProjectCarPreRegister>().lambda()
                .eq(ProjectCarPreRegister::getPlateNumber, carRegisterFormVo.getPlateNumber()));
        // 车牌号还未注册，正在审核的车牌号，不能登记认证
        for (ProjectCarPreRegister carPreRegister : projectCarPreRegisterList) {
            if (AuditStatusEnum.inAudit.code.equals(carPreRegister.getAuditStatus())) {
                return R.failed("车牌号已被登记");
            }
        }
        ProjectCarPreRegister projectCarPreRegister = new ProjectCarPreRegister();
        BeanUtil.copyProperties(carRegisterFormVo, projectCarPreRegister);
        projectCarPreRegister.setCommitTime(LocalDateTime.now());
        projectCarPreRegister.setAuditStatus(AuditStatusEnum.inAudit.code);
        boolean save = projectCarPreRegisterService.save(projectCarPreRegister);
        remoteWebSocketService.transferSocket(ProjectContextHolder.getProjectId().toString());
        return R.ok(save);
    }

    @GetMapping("/page")
    @ApiOperation(value = "车辆登记列表", notes = "车辆登记列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", required = true, value = "小区Id", paramType = "header")
    })
    public R<Page<AppCarPreRegisterPageVo>> page (Page page) {
        ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getPersonByOwner();
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectPersonInfo)) {
            return R.failed("您在该小区下还未登记");
        }
        ProjectCarPreRegister projectCarPreRegister = new ProjectCarPreRegister();
        projectCarPreRegister.setPersonId(projectPersonInfo.getPersonId());
        Page<ProjectCarPreRegister> projectCarPreRegisterPage = projectCarPreRegisterService.page(page,
                new QueryWrapper<>(projectCarPreRegister).lambda().orderByDesc(ProjectCarPreRegister::getCommitTime));
        Page<AppCarPreRegisterPageVo> appCarPreRegisterPageVoPage = new Page<>();
        BeanUtil.copyProperties(projectCarPreRegisterPage, appCarPreRegisterPageVoPage);
        appCarPreRegisterPageVoPage.setRecords(projectCarPreRegisterPage.getRecords().stream()
                .map(e -> {
                    AppCarPreRegisterPageVo appCarPreRegisterPageVo = new AppCarPreRegisterPageVo();
                    BeanUtil.copyProperties(e, appCarPreRegisterPageVo);
                    return appCarPreRegisterPageVo;
                }).collect(Collectors.toList()));
        return R.ok(appCarPreRegisterPageVoPage);
    }

    @DeleteMapping("/remove-register/{preRegId}")
    @ApiOperation(value = "删除认证或撤销车辆认证", notes = "删除认证或撤销车辆认证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", required = true, value = "小区Id", paramType = "header"),
            @ApiImplicitParam(name = "preRegId", required = true, value = "车辆预登记id", paramType = "path")
    })
    public R<Boolean> deleteCarRegister (@PathVariable("preRegId") String preRegId) {
        boolean flag = projectCarPreRegisterService.removeById(preRegId);
        remoteWebSocketService.transferSocket(ProjectContextHolder.getProjectId().toString());
        return R.ok(flag);
    }

    @DeleteMapping("/remove-car/{preRegId}")
    @ApiOperation(value = "注销车辆", notes = "注销车辆")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", required = true, value = "小区Id", paramType = "header"),
            @ApiImplicitParam(name = "preRegId", required = true, value = "车辆预登记id", paramType = "path")
    })
    public R<String> deleteCar (@PathVariable("preRegId") String preRegId) {
        ProjectCarPreRegister projectCarPreRegister = projectCarPreRegisterService.getById(preRegId);
        ProjectParCarRegister projectParCarRegister = ProjectParCarRegisterService.getOne(new QueryWrapper<ProjectParCarRegister>().lambda()
                .eq(ProjectParCarRegister::getPlateNumber, projectCarPreRegister.getPlateNumber()));
        if (BeanUtil.isEmpty(projectParCarRegister)) {
            return R.failed("该车辆还未注册");
        }
        return remoteCarRegisterService.cancelCarRegister(projectParCarRegister.getRegisterId());
    }


    @PostMapping("/staff/register")
    @ApiOperation(value = "车辆登记(物业)", notes = "车辆登记")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", required = true, value = "小区Id", paramType = "header")
    })
    public R register (@RequestBody AppParCarRegisterVo appParCarRegisterVo) {

        ProjectParCarRegisterVo projectParCarRegister = new ProjectParCarRegisterVo();
        BeanUtil.copyProperties(appParCarRegisterVo, projectParCarRegister);
        try {
            return  remoteParCarRegisterService.register(projectParCarRegister);
        }catch (Exception ex){
             ex.printStackTrace();
             return R.failed("车厂异常,请联系管理员");
        }
    }

    /**
     * 根据车牌号，获取VO,显示车辆以及所属人员的信息
     *
     * @param plateNumber
     * @return
     */
    @ApiOperation(value ="根据车牌号获取车辆信息（物业）", notes = "根据车牌号获取车辆信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "plateNumber", value = "车牌号", required = true, paramType = "path")
    })
    @GetMapping("/staff/{plateNumber}")
    public R<ProjectCarInfoVo> getVoByPlateNumber(@PathVariable String plateNumber) {
        ProjectCarInfoVo projectCarInfoVo = projectCarInfoService.getVoByPlateNumber(plateNumber);
        if (ObjectUtil.isEmpty(projectCarInfoVo)) {
            return R.ok();
        }
        return R.ok(projectCarInfoVo);
    }



    /**
     * 分页查询
     * @param page 分页对象
     * @param searchParkEntranceHisVo 车场管理
     * @return
     */
    @ApiOperation(value = "查询车辆的出入记录列表(物业)", notes = "查询车辆的出入记录列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header")
    })
    @GetMapping("/staff/event/page")
    public R<IPage<ProjectParkEntranceHisVo>> getParkEntranceHisPage(Page page, AppSearchParkEntranceHisVo searchParkEntranceHisVo) {
        ProjectParkEntranceHisVo projectParkEntranceHisVo = new ProjectParkEntranceHisVo();
        BeanUtil.copyProperties(searchParkEntranceHisVo, projectParkEntranceHisVo);
        return R.ok(parkEntranceHisService.page(page, projectParkEntranceHisVo));
    }
}
