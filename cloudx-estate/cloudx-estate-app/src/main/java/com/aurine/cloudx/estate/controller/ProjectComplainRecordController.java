package com.aurine.cloudx.estate.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aurine.cloudx.estate.constant.enums.ApproveStatusEnum;
import com.aurine.cloudx.estate.entity.ProjectComplaintRecord;
import com.aurine.cloudx.estate.feign.RemoteComplaintRecordService;
import com.aurine.cloudx.estate.service.ProjectComplaintRecordService;
import com.aurine.cloudx.estate.service.ProjectStaffService;
import com.aurine.cloudx.estate.util.ImgConvertUtil;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
@Api(value = "/complain", tags = "投诉管理")
@RequestMapping("/complain")
public class ProjectComplainRecordController {

    @Resource
    private ProjectComplaintRecordService projectComplaintRecordService;
    @Resource
    private ImgConvertUtil imgConvertUtil;

    @Resource
    private ProjectStaffService projectStaffService;

    @Resource
    private RemoteComplaintRecordService remoteComplaintRecordService;
    /**
     * 分页查询我提交的投诉
     *
     * @param type 类型
     * @return
     */
    @ApiOperation(value = "获取投诉列表(投诉)", notes = "获取投诉列表")
    @ApiImplicitParams({

            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", required = true, value = "小区Id", paramType = "header"),
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "total", value = "总条数", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型（0 环境 1 安全 2 秩序 3 工作人员 4 设备设施 5  供水 6 消防 7 供气 8 供电 9 便民设施 10 其他投诉）", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态（0 待抢单 1 待完成 2 已完成）", paramType = "query"),
            @ApiImplicitParam(name = "date", value = "日期(格式为 yyyy-MM)", paramType = "query")
    })
    @GetMapping("/page")
    public R<Page<ProjectComplaintRecordVo>> pageByComplainRecordList(@RequestParam(value = "size", required = false) Long size,
                                                                      @RequestParam(value = "current", required = false) Long current,
                                                                      @RequestParam(value = "type", required = false) String type,
                                                                      @RequestParam(value = "status", required = false) String status,
                                                                      @RequestParam(value = "date", required = false) String date) {
        AppPage page = new AppPage(current, size);
        return R.ok(projectComplaintRecordService.pageByType(page, type, status, null, SecurityUtils.getUser().getPhone(), date));
    }

    /**
     * 获取投诉单据详细信息
     *
     * @param complaintId 单据id
     * @return 单据详细信息
     */
    @ApiOperation(value = "根据投诉Id获取投诉详情(投诉)", notes = "根据投诉Id获取投诉详情")
    @ApiImplicitParams({

            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", required = true, value = "小区Id", paramType = "header"),
            @ApiImplicitParam(name = "complaintId", value = "投诉单号Id", paramType = "path", required = true)
    })
    @GetMapping("/{complaintId}")
    public R<AppComplaintRecordInfoVo> getComplainRecordInfo(@PathVariable("complaintId") String complaintId) throws IOException {
        AppComplaintRecordInfoVo complaintRecordInfoVo = new AppComplaintRecordInfoVo();
        BeanUtil.copyProperties(projectComplaintRecordService.getByComplainId(complaintId), complaintRecordInfoVo);
        complaintRecordInfoVo.setDonePicPath(imgConvertUtil.urlToBase64(complaintRecordInfoVo.getDonePicPath()));
        complaintRecordInfoVo.setDonePicPath2(imgConvertUtil.urlToBase64(complaintRecordInfoVo.getDonePicPath2()));
        complaintRecordInfoVo.setDonePicPath3(imgConvertUtil.urlToBase64(complaintRecordInfoVo.getDonePicPath3()));
        complaintRecordInfoVo.setDonePicPath4(imgConvertUtil.urlToBase64(complaintRecordInfoVo.getDonePicPath4()));
        complaintRecordInfoVo.setDonePicPath5(imgConvertUtil.urlToBase64(complaintRecordInfoVo.getDonePicPath5()));
        complaintRecordInfoVo.setDonePicPath6(imgConvertUtil.urlToBase64(complaintRecordInfoVo.getDonePicPath6()));
        complaintRecordInfoVo.setPicPath1(imgConvertUtil.urlToBase64(complaintRecordInfoVo.getPicPath1()));
        complaintRecordInfoVo.setPicPath2(imgConvertUtil.urlToBase64(complaintRecordInfoVo.getPicPath2()));
        complaintRecordInfoVo.setPicPath3(imgConvertUtil.urlToBase64(complaintRecordInfoVo.getPicPath3()));
        complaintRecordInfoVo.setPicPath4(imgConvertUtil.urlToBase64(complaintRecordInfoVo.getPicPath4()));
        complaintRecordInfoVo.setPicPath5(imgConvertUtil.urlToBase64(complaintRecordInfoVo.getPicPath5()));
        complaintRecordInfoVo.setPicPath6(imgConvertUtil.urlToBase64(complaintRecordInfoVo.getPicPath6()));
        return R.ok(complaintRecordInfoVo);
    }

    /**
     * 评价
     *
     * @return
     */
    @PutMapping("/appraise")
    @ApiOperation(value = "投诉评价(投诉)", notes = "投诉评价")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", required = true, value = "小区Id", paramType = "header")
    })
    public R<String> ComplainRecordAppraise(@RequestBody AppComplainRecordAppraiseFormVo appComplainRecordAppraiseFormVo) {
        ProjectComplaintRecord projectComplaintRecord = projectComplaintRecordService.getById(appComplainRecordAppraiseFormVo.getComplaintId());

        if (!SecurityUtils.getUser().getPhone().equals(projectComplaintRecord.getPhoneNumber())) {
//            非创建人无法评价
            return R.failed("非创建人无法评价");
        }
        projectComplaintRecord.setScore(appComplainRecordAppraiseFormVo.getScore());
        projectComplaintRecord.setStatus(ApproveStatusEnum.finish.code);
        projectComplaintRecordService.updateById(projectComplaintRecord);
        return R.ok("操作成功");
    }

    /**
     * 登记
     *
     * @param complainRecordRequestFormVo 表单对象
     * @return
     */
    @ApiOperation(value = "投诉登记(投诉)", notes = "投诉登记")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", required = true, value = "小区Id", paramType = "header")
    })
    @PostMapping("/request")
    public R<String> request(@RequestBody AppComplainRecordRequestFormVo complainRecordRequestFormVo) throws IOException {
        ProjectComplainRecordRequestFormVo formVo = new ProjectComplainRecordRequestFormVo();
        BeanUtil.copyProperties(complainRecordRequestFormVo, formVo);
        if (ObjectUtil.isNotEmpty(complainRecordRequestFormVo.getPicBase64List()) && complainRecordRequestFormVo.getPicBase64List().size() > 0) {
            JSONObject obj = JSONUtil.parseObj(formVo);
            for (Integer i = 1; i <= complainRecordRequestFormVo.getPicBase64List().size(); i++) {
                obj.set("picPath" + i, imgConvertUtil.base64ToMinio(complainRecordRequestFormVo.getPicBase64List().get(i - 1)));
            }
            formVo = JSONUtil.toBean(obj, ProjectComplainRecordRequestFormVo.class);
        }
        return projectComplaintRecordService.request(formVo);
    }

    /**
     * 接单
     *
     * @param complaintId 单据id
     * @return 单据详细信息
     */
    @ApiOperation(value = "接单(物业)", notes = "接单")
    @PutMapping("/staff/order/{complaintId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token",required = true,paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id",required = true,paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", required = true, value = "小区Id",  paramType = "header"),
            @ApiImplicitParam(name = "complaintId", required = true, value = "投诉单号Id",  paramType = "path")
    })
    public R<ProjectComplaintRecordInfoVo> order(@PathVariable("complaintId") String complaintId) {
        ProjectComplaintRecord projectComplaintRecord = new ProjectComplaintRecord();
        projectComplaintRecord.setComplaintId(complaintId);
        return remoteComplaintRecordService.robOrder(projectComplaintRecord);
    }

    /**
     * 指派处理人
     * @return
     */
    @ApiOperation(value = "指派处理人(物业)", notes = "指派处理人")
    @PutMapping("/staff/assign")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token",required = true,paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id",required = true,paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id",  paramType = "header")
    })
    public R setHandler(@RequestBody AppComplainRecordAssignVo appComplainRecordAssignVo) {
        ProjectComplaintRecord projectComplaintRecord = new ProjectComplaintRecord();
        BeanUtil.copyProperties(appComplainRecordAssignVo, projectComplaintRecord);
        return remoteComplaintRecordService.sendOrder(projectComplaintRecord);
    }

    /**
     * 处理完成
     *
     * @param complainRecordResultFormVo 表单对象
     * @return
     */
    @ApiOperation(value = "处理完成(物业)", notes = "处理完成")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token",required = true,paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id",required = true,paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", required = true, value = "小区Id",  paramType = "header")
    })
    @PutMapping("/staff/complete")
    public R setResult(@RequestBody AppComplainRecordResultFormVo complainRecordResultFormVo) throws IOException {
        ProjectComplainRecordResultFormVo formVo = new ProjectComplainRecordResultFormVo();
        BeanUtil.copyProperties(complainRecordResultFormVo, formVo);
        if (ObjectUtil.isNotEmpty(complainRecordResultFormVo.getPicBase64List()) && complainRecordResultFormVo.getPicBase64List().size() > 0) {
            JSONObject obj = JSONUtil.parseObj(formVo);
            obj.set("donePicPath", imgConvertUtil.base64ToMinio(complainRecordResultFormVo.getPicBase64List().get(0)));
            for (Integer i = 2; i <= complainRecordResultFormVo.getPicBase64List().size(); i++) {
                obj.set("donePicPath" + i, imgConvertUtil.base64ToMinio(complainRecordResultFormVo.getPicBase64List().get(i - 1)));
            }
            formVo = JSONUtil.toBean(obj, ProjectComplainRecordResultFormVo.class);
        }
        return remoteComplaintRecordService.doneOrder(formVo);
    }

    /**
     * 分页查询所有
     *
     * @param type 类型
     * @return
     */
    @ApiOperation(value = "分页查询所有投诉订单列表(物业)", notes = "分页查询所有投诉订单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型(0 环境 1 安全 2 秩序 3 工作人员 4 设备设施 5  供水 6 消防 7 供气 8 供电 9 便民设施 10 其他投诉)", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态(0 待抢单 1 待完成 2 已完成)", paramType = "query"),
            @ApiImplicitParam(name = "handler", value = "处理人ID", required = false, paramType = "query"),
            @ApiImplicitParam(name = "phone", value = "投诉人手机号", required = false, paramType = "query"),
            @ApiImplicitParam(name = "date", value = "日期(格式为 yyyy-MM)", required = false, paramType = "query")
    })
    @GetMapping("/staff/page")
    public R<Page<ProjectComplaintRecordVo>> page(@RequestParam(value = "size", required = false) Long size,
                                                  @RequestParam(value = "current", required = false) Long current,
                                                  @RequestParam(value = "type", required = false) String type,
                                                  @RequestParam(value = "status", required = false) String status,
                                                  @RequestParam(value = "handler", required = false) String handler,
                                                  @RequestParam(value = "phone", required = false) String phone,
                                                  @RequestParam(value="date", required = false) String date) {
        AppPage page = new AppPage(current, size);
        return R.ok(projectComplaintRecordService.pageByType(page, type, status,handler, phone, date));
    }

    /**
     * 分页查询待我处理的投诉
     *
     * @param type 类型
     * @return
     */
    @ApiOperation(value = "分页查询待我处理的投诉列表(物业)", notes = "分页查询待我处理的投诉列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token",required = true,paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id",required = true,paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true,  paramType = "header"),
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型(0 环境 1 安全 2 秩序 3 工作人员 4 设备设施 5  供水 6 消防 7 供气 8 供电 9 便民设施 10 其他投诉)", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态(0 待抢单 1 待完成 2 已完成)", paramType = "query")
    })
    @GetMapping("/staff/handler/page")
    public R<Page<ProjectComplaintRecordVo>> pageByHandler(@RequestParam(value = "size", required = false) Long size,
                                                           @RequestParam(value = "current", required = false) Long current,
                                                           @RequestParam(value = "type",required = false) String type,
                                                           @RequestParam(value = "status",required = false) String status) {
        AppPage page = new AppPage(current, size);
        ProjectStaffVo projectStaffVo = projectStaffService.getStaffByOwner();
        return R.ok(projectComplaintRecordService.pageByType(page, type,status, projectStaffVo.getStaffId(),null, null));
    }

    /**
     * 获取投诉单据详细信息
     *
     * @param complaintId 单据id
     * @return 单据详细信息
     */
    @ApiOperation(value = "根据投诉Id获取投诉详情(物业)", notes = "根据投诉Id获取投诉详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", required = true, value = "小区Id", paramType = "header"),
            @ApiImplicitParam(name = "complaintId", value = "投诉单号Id", paramType = "path", required = true)
    })
    @GetMapping("/staff/{complaintId}")
    public R<AppComplaintRecordInfoVo> getStaffComplainRecordInfo(@PathVariable("complaintId") String complaintId) throws IOException {
        AppComplaintRecordInfoVo complaintRecordInfoVo = new AppComplaintRecordInfoVo();
        BeanUtil.copyProperties(projectComplaintRecordService.getByComplainId(complaintId), complaintRecordInfoVo);
        complaintRecordInfoVo.setDonePicPath(imgConvertUtil.urlToBase64(complaintRecordInfoVo.getDonePicPath()));
        complaintRecordInfoVo.setDonePicPath2(imgConvertUtil.urlToBase64(complaintRecordInfoVo.getDonePicPath2()));
        complaintRecordInfoVo.setDonePicPath3(imgConvertUtil.urlToBase64(complaintRecordInfoVo.getDonePicPath3()));
        complaintRecordInfoVo.setDonePicPath4(imgConvertUtil.urlToBase64(complaintRecordInfoVo.getDonePicPath4()));
        complaintRecordInfoVo.setDonePicPath5(imgConvertUtil.urlToBase64(complaintRecordInfoVo.getDonePicPath5()));
        complaintRecordInfoVo.setDonePicPath6(imgConvertUtil.urlToBase64(complaintRecordInfoVo.getDonePicPath6()));
        complaintRecordInfoVo.setPicPath1(imgConvertUtil.urlToBase64(complaintRecordInfoVo.getPicPath1()));
        complaintRecordInfoVo.setPicPath2(imgConvertUtil.urlToBase64(complaintRecordInfoVo.getPicPath2()));
        complaintRecordInfoVo.setPicPath3(imgConvertUtil.urlToBase64(complaintRecordInfoVo.getPicPath3()));
        complaintRecordInfoVo.setPicPath4(imgConvertUtil.urlToBase64(complaintRecordInfoVo.getPicPath4()));
        complaintRecordInfoVo.setPicPath5(imgConvertUtil.urlToBase64(complaintRecordInfoVo.getPicPath5()));
        complaintRecordInfoVo.setPicPath6(imgConvertUtil.urlToBase64(complaintRecordInfoVo.getPicPath6()));
        return R.ok(complaintRecordInfoVo);
    }
}
