package com.aurine.cloudx.estate.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aurine.cloudx.estate.constant.enums.ApproveStatusEnum;
import com.aurine.cloudx.estate.entity.ProjectRepairRecord;
import com.aurine.cloudx.estate.feign.RemoteRepairRecordService;
import com.aurine.cloudx.estate.service.ProjectRepairRecordService;
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
@Api(value = "repair-record", tags = "报修管理")
@RequestMapping("/repair-record")
public class ProjectRepairRecordController {

    @Resource
    private ProjectRepairRecordService projectRepairRecordService;
    @Resource
    private ImgConvertUtil imgConvertUtil;

    @Resource
    private RemoteRepairRecordService remoteRepairRecordService;

    @Resource
    private ProjectStaffService projectStaffService;

    /**
     *
     * @param
     * @param type 报修类型
     * @param status 处理状态
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value="获取报修列表(报修)", notes = "获取报修列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "报修类型（0 水电煤气 1 家电家具 2 电梯 3 门禁 4 公共设施 5 物业设备 6 其他报修）", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "处理状态（0 待抢单 1 待完成 2 已完成） ", paramType = "query"),
            @ApiImplicitParam(name = "date", value = "日期(格式为 yyyy-MM)", required = false, paramType = "query")
    })
    public R<Page<ProjectRepairRecordVo>> pageByRepairRecordList(@RequestParam(value = "size", required = false) Long size,
                                                                 @RequestParam(value = "current", required = false) Long current,
                                                                 @RequestParam(value = "type", required = false) String type,
                                                                 @RequestParam(value = "status", required = false) String status,
                                                                 @RequestParam(value = "date", required = false) String date) {
        AppPage page = new AppPage(current, size);


        return R.ok(projectRepairRecordService.pageByType(page, type, status,null, SecurityUtils.getUser().getPhone(), date));
    }

    /**
     *
     * @param repairId 报修ID
     * @return 单据详细信息
     */
    @GetMapping("/{repairId}")
    @ApiOperation(value = "根据报修ID获取报修详情(报修)", notes = "根据报修ID获取报修详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "repairId", value = "报修ID", required = true, paramType = "path")
    })
    public R<AppRepairRecordInfoVo> getRepairRecordInfo(@PathVariable("repairId") String repairId) throws IOException {
        AppRepairRecordInfoVo repairRecordInfoVo = new AppRepairRecordInfoVo();
        BeanUtil.copyProperties(projectRepairRecordService.getByRepairId(repairId), repairRecordInfoVo);
        repairRecordInfoVo.setDonePicPath(imgConvertUtil.urlToBase64(repairRecordInfoVo.getDonePicPath()));
        repairRecordInfoVo.setDonePicPath2(imgConvertUtil.urlToBase64(repairRecordInfoVo.getDonePicPath2()));
        repairRecordInfoVo.setDonePicPath3(imgConvertUtil.urlToBase64(repairRecordInfoVo.getDonePicPath3()));
        repairRecordInfoVo.setDonePicPath4(imgConvertUtil.urlToBase64(repairRecordInfoVo.getDonePicPath4()));
        repairRecordInfoVo.setDonePicPath5(imgConvertUtil.urlToBase64(repairRecordInfoVo.getDonePicPath5()));
        repairRecordInfoVo.setDonePicPath6(imgConvertUtil.urlToBase64(repairRecordInfoVo.getDonePicPath6()));
        repairRecordInfoVo.setPicPath1(imgConvertUtil.urlToBase64(repairRecordInfoVo.getPicPath1()));
        repairRecordInfoVo.setPicPath2(imgConvertUtil.urlToBase64(repairRecordInfoVo.getPicPath2()));
        repairRecordInfoVo.setPicPath3(imgConvertUtil.urlToBase64(repairRecordInfoVo.getPicPath3()));
        repairRecordInfoVo.setPicPath4(imgConvertUtil.urlToBase64(repairRecordInfoVo.getPicPath4()));
        repairRecordInfoVo.setPicPath5(imgConvertUtil.urlToBase64(repairRecordInfoVo.getPicPath5()));
        repairRecordInfoVo.setPicPath6(imgConvertUtil.urlToBase64(repairRecordInfoVo.getPicPath6()));
        return R.ok(repairRecordInfoVo);
    }

    /**
     * 评价
     *
     * @param appRepairRecordAppraiseFormVo
     * @return 单据详细信息
     */
    @PutMapping("/appraise")
    @ApiOperation(value = "报修评价(报修)", notes = "报修评价")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", required = true, value = "小区Id", paramType = "header")
    })
    public R<String> RepairRecordAppraise(@RequestBody AppRepairRecordAppraiseFormVo appRepairRecordAppraiseFormVo) {
        ProjectRepairRecord projectRepairRecord = projectRepairRecordService.getById(appRepairRecordAppraiseFormVo.getRepairId());
        if (!SecurityUtils.getUser().getPhone().equals(projectRepairRecord.getPhoneNumber())) {
//            非创建人无法评价
            return R.failed("非创建人无法评价");
        }
        projectRepairRecord.setScore(appRepairRecordAppraiseFormVo.getScore());
        projectRepairRecord.setStatus(ApproveStatusEnum.finish.code);
        projectRepairRecordService.updateById(projectRepairRecord);
        return R.ok("操作成功");
    }

    /**
     * 登记
     *
     * @param repairRecordRequestFormVo 表单对象
     * @return
     */
    @PostMapping
    @ApiOperation(value = "报修登记(报修)", notes = "报修登记")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", required = true, value = "小区Id", paramType = "header")
    })
    public R<String> RepairRecordRequest(@RequestBody AppRepairRecordRequestFormVo repairRecordRequestFormVo) throws IOException {
        ProjectRepairRecordRequestFormVo formVo = new ProjectRepairRecordRequestFormVo();
        BeanUtil.copyProperties(repairRecordRequestFormVo, formVo);
        if (ObjectUtil.isNotEmpty(repairRecordRequestFormVo.getPicBase64List()) && repairRecordRequestFormVo.getPicBase64List().size() > 0) {
            JSONObject obj = JSONUtil.parseObj(formVo);
            for (Integer i = 1; i <= repairRecordRequestFormVo.getPicBase64List().size(); i++) {
                obj.set("picPath" + i, imgConvertUtil.base64ToMinio(repairRecordRequestFormVo.getPicBase64List().get(i - 1)));
            }
            formVo = JSONUtil.toBean(obj, ProjectRepairRecordRequestFormVo.class);
        }
        return projectRepairRecordService.request(formVo);
    }

    /**
     * 指派处理人
     *
     * @return
     */
    @ApiOperation(value = "指派处理人(物业)", notes = "指派处理人")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @PutMapping("/staff/assign")
    public R setHandler(@RequestBody AppRepairRecordAssignVo repairRecordAssignVo) {
        ProjectRepairRecord projectRepairRecord = new ProjectRepairRecord();
        BeanUtil.copyProperties(repairRecordAssignVo, projectRepairRecord);
        return remoteRepairRecordService.sendOrder(projectRepairRecord);
    }

    /**
     * 接单
     *
     * @param id 单据id
     * @return 单据详细信息
     */
    @ApiOperation(value = "接单(物业)", notes = "接单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "单据id", paramType = "path", required = true),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", required = true, value = "小区Id", paramType = "header")
    })
    @PutMapping("/staff/order/{id}")
    public R order(@PathVariable("id") String id) {
        ProjectRepairRecord repairRecord = new ProjectRepairRecord();
        repairRecord.setRepairId(id);
        return remoteRepairRecordService.robOrder(repairRecord);
    }

    /**
     * 处理完成
     *
     * @return
     */
    @ApiOperation(value = "处理完成(物业)", notes = "处理完成")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", required = true, value = "小区Id", paramType = "header")
    })
    @PutMapping("/staff/complete")
    public R setResult(@RequestBody AppRepairRecordResultFormVo repairRecordRequestFormVo) throws IOException {
        ProjectRepairRecordResultFormVo formVo = new ProjectRepairRecordResultFormVo();
        BeanUtil.copyProperties(repairRecordRequestFormVo, formVo);
        if (ObjectUtil.isNotEmpty(repairRecordRequestFormVo.getPicBase64List()) && repairRecordRequestFormVo.getPicBase64List().size() > 0) {
            JSONObject obj = JSONUtil.parseObj(formVo);
            obj.set("donePicPath", imgConvertUtil.base64ToMinio(repairRecordRequestFormVo.getPicBase64List().get(0)));
            for (Integer i = 2; i <= repairRecordRequestFormVo.getPicBase64List().size(); i++) {
                obj.set("donePicPath" + i, imgConvertUtil.base64ToMinio(repairRecordRequestFormVo.getPicBase64List().get(i - 1)));
            }
            formVo = JSONUtil.toBean(obj, ProjectRepairRecordResultFormVo.class);
        }
        return remoteRepairRecordService.doneOrder(formVo);
    }

    /**
     * 分页查询所有
     *
     * @param type 类型
     * @return
     */
    @ApiOperation(value = "分页查询所有报修订单列表(物业)", notes = "分页查询所有报修订单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "报修类型 (0 水电煤气 1 家电家具 2 电梯 3 门禁 4 公共设施 5 物业设备 6 其他报修)", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "处理状态 (0 待抢单 1 待完成 2 已完成) ", paramType = "query"),
            @ApiImplicitParam(name = "handler", value = "处理人ID", required = false, paramType = "query"),
            @ApiImplicitParam(name = "phone", value = "报修人手机号", required = false, paramType = "query"),
            @ApiImplicitParam(name = "date", value = "日期(格式为 yyyy-MM)", required = false, paramType = "query")
    })
    @GetMapping("/staff/page")
    public R<Page<ProjectRepairRecordVo>> page(@RequestParam(value = "size", required = false) Long size,
                                               @RequestParam(value = "current", required = false) Long current,
                                               @RequestParam(value = "type", required = false) String type,
                                               @RequestParam(value = "status", required = false) String status,
                                               @RequestParam(value = "handler", required = false) String handler,
                                               @RequestParam(value = "phone", required = false) String phone,
                                               @RequestParam(value = "date", required = false) String date) {
        AppPage page = new AppPage(current, size);
        return R.ok(projectRepairRecordService.pageByType(page, type,status, handler, phone, date));
    }

    /**
     * 分页查询待我处理的报修
     *
     * @param type 类型
     * @return
     */
    @ApiOperation(value = "分页查询待我处理的报修列表(物业)", notes = "分页查询待我处理的报修列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "报修类型 (0 水电煤气 1 家电家具 2 电梯 3 门禁 4 公共设施 5 物业设备 6 其他报修)", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "处理状态 (0 待抢单 1 待完成 2 已完成) ", paramType = "query")
    })
    @GetMapping("/staff/handler/page")
    public R<Page<ProjectRepairRecordVo>> pageByHandler(@RequestParam(value = "size", required = false) Long size,
                                                        @RequestParam(value = "current", required = false) Long current,
                                                        @RequestParam(value = "type", required = false) String type,
                                                        @RequestParam(value = "status", required = false) String status) {
        AppPage page = new AppPage(current, size);
        ProjectStaffVo projectStaffVo = projectStaffService.getStaffByOwner();
        return R.ok(projectRepairRecordService.pageByType(page, type,status, projectStaffVo.getStaffId(), null, null));
    }
    /**
     *
     * @param repairId 报修ID
     * @return 单据详细信息
     */
    @GetMapping("/staff/{repairId}")
    @ApiOperation(value = "根据报修ID获取报修详情(物业)", notes = "根据报修ID获取报修详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "repairId", value = "报修ID", required = true, paramType = "path")
    })
    public R<AppRepairRecordInfoVo> getStaffRepairRecordInfo(@PathVariable("repairId") String repairId) throws IOException {
        AppRepairRecordInfoVo repairRecordInfoVo = new AppRepairRecordInfoVo();
        BeanUtil.copyProperties(projectRepairRecordService.getByRepairId(repairId), repairRecordInfoVo);
        repairRecordInfoVo.setDonePicPath(imgConvertUtil.urlToBase64(repairRecordInfoVo.getDonePicPath()));
        repairRecordInfoVo.setDonePicPath2(imgConvertUtil.urlToBase64(repairRecordInfoVo.getDonePicPath2()));
        repairRecordInfoVo.setDonePicPath3(imgConvertUtil.urlToBase64(repairRecordInfoVo.getDonePicPath3()));
        repairRecordInfoVo.setDonePicPath4(imgConvertUtil.urlToBase64(repairRecordInfoVo.getDonePicPath4()));
        repairRecordInfoVo.setDonePicPath5(imgConvertUtil.urlToBase64(repairRecordInfoVo.getDonePicPath5()));
        repairRecordInfoVo.setDonePicPath6(imgConvertUtil.urlToBase64(repairRecordInfoVo.getDonePicPath6()));
        repairRecordInfoVo.setPicPath1(imgConvertUtil.urlToBase64(repairRecordInfoVo.getPicPath1()));
        repairRecordInfoVo.setPicPath2(imgConvertUtil.urlToBase64(repairRecordInfoVo.getPicPath2()));
        repairRecordInfoVo.setPicPath3(imgConvertUtil.urlToBase64(repairRecordInfoVo.getPicPath3()));
        repairRecordInfoVo.setPicPath4(imgConvertUtil.urlToBase64(repairRecordInfoVo.getPicPath4()));
        repairRecordInfoVo.setPicPath5(imgConvertUtil.urlToBase64(repairRecordInfoVo.getPicPath5()));
        repairRecordInfoVo.setPicPath6(imgConvertUtil.urlToBase64(repairRecordInfoVo.getPicPath6()));
        return R.ok(repairRecordInfoVo);
    }
}
