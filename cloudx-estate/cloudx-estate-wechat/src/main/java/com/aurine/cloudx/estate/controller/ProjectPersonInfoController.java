package com.aurine.cloudx.estate.controller;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import com.aurine.cloudx.estate.entity.ProjectNotice;
import com.aurine.cloudx.estate.entity.ProjectPersonInfo;
import com.aurine.cloudx.estate.entity.ProjectStaff;
import com.aurine.cloudx.estate.entity.ProjectStaffNotice;
import com.aurine.cloudx.estate.feign.RemoteStaffService;
import com.aurine.cloudx.estate.service.ProjectNoticeService;
import com.aurine.cloudx.estate.service.ProjectPersonInfoService;
import com.aurine.cloudx.estate.service.ProjectStaffNoticeObjectService;
import com.aurine.cloudx.estate.service.ProjectStaffNoticeService;
import com.aurine.cloudx.estate.vo.ProjectPersonNoticeVo;
import com.aurine.cloudx.estate.vo.ProjectStaffNoticeVo;
import com.aurine.cloudx.estate.vo.ProjectStaffVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * (ProjectPersonInfoController)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/9/2 15:08
 */
@RestController
@AllArgsConstructor
@RequestMapping("/projectPersonInfo")
@Api(value = "projectPersonInfo", tags = "业主管理")
@Slf4j
public class ProjectPersonInfoController {

    @Resource
    private final ProjectPersonInfoService projectPersonInfoService;

    @Resource
    private final ProjectStaffNoticeService projectStaffNoticeService;
    @Resource
    private final ProjectNoticeService projectNoticeService;
    @Resource
    private final ProjectStaffNoticeObjectService projectStaffNoticeObjectService;





    /**
     * 分页查询业主消息通知
     *
     * @param page 分页对象
     * @return
     */
    @ApiOperation(value = "分页查询人员消息通知(业主)", notes = "分页查询人员消息通知")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })

    @GetMapping("/notice/page")
    public R<Page<ProjectPersonNoticeVo>> getProjectStaffNoticePage(Page page) {
        ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getPersonByOwner();
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectPersonInfo)) {
            return R.failed("您在该小区下还未登记");
        }
        return R.ok(projectNoticeService.pageByPerson(page, projectPersonInfo.getPersonId(), "2"));
    }

    /**
     * 查询人员消息通知未读汇总
     *
     * @return
     */
    @ApiOperation(value = "查询人员消息通知未读汇总(业主)", notes = "查询人员消息通知未读汇总")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })

    @GetMapping("/notice/count")
    public R<Integer> getCount() {

        ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getPersonByOwner();
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectPersonInfo)) {
            return R.failed("您在该小区下还未登记");
        }
        return R.ok(projectStaffNoticeService.countByPersonId(projectPersonInfo.getPersonId(), "2"));
    }

    /**
     * 获取消息信息
     *
     * @param noticeId 消息id
     * @return
     */
    @ApiOperation(value = "获取消息信息(业主)", notes = "获取消息信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = " noticeId", value = "消息id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })

    @GetMapping("/notice/{noticeId}")
    public R<ProjectNotice> getProjectStaffNoticePage(@PathVariable("noticeId") String noticeId) {
        ProjectNotice projectNotice = projectNoticeService.getById(noticeId);
        ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getPersonByOwner();

        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectPersonInfo)) {
            return R.failed("您在该小区下还未登记");
        }
        projectStaffNoticeObjectService.updateNoticeStatus(projectPersonInfo.getPersonId(), ListUtil.toList(noticeId));
        return R.ok(projectNotice);
    }


    /**
     * 获取住户信息
     *
     * @return
     */
    @ApiOperation(value = "获取住户信息(业主)", notes = "获取员工信息")

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })

    @GetMapping
    public R<ProjectPersonInfo> getInfo() {
        ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getPersonByOwner();
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectPersonInfo)) {
            return R.failed("您在该小区下还未登记");
        }
        return R.ok(projectPersonInfo);
    }

    /**
     * 获取业主信息
     *
     * @return
     */
    @ApiOperation(value = "获取业主信息(物业，业主)", notes = "获取业主信息")

    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, paramType = "header"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @GetMapping("/getInfoByPhone/{phone}")
    public R<ProjectPersonInfo> getInfoByPhone(@PathVariable String phone) {
        ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getByTelephone(phone);
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectPersonInfo)) {
            return R.ok();
        }
        return R.ok(projectPersonInfo);
    }




    /**
     * 更改业主手机号
     *
     * @param phone
     * @return
     */
    @ApiOperation(value = "更改人员手机号", notes = "更改人员手机号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, paramType = "path"),
    })
    @PutMapping("/updatePhone/{phone}/{newPhone}")
    public R<Boolean> getNewPhoneQRCode(@PathVariable("phone") String phone,@PathVariable("newPhone")String newPhone) {

        return  projectPersonInfoService.updatePhoneById(phone,newPhone);

    }



}
