package com.aurine.cloudx.estate.open.vistor.controller;

import com.aurine.cloudx.estate.entity.ProjectVisitorHis;
import com.aurine.cloudx.estate.open.vistor.bean.ProjectVisitorSearchConditionPage;
import com.aurine.cloudx.estate.open.vistor.fegin.RemoteProjectVisitorService;
import com.aurine.cloudx.estate.vo.ProjectVisitorVo;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 访客
 *
 * @author 王伟
 * @date 2020-06-03 19:42:52
 */
@RestController
@RequestMapping("/service-visitor")
@Api(value = "serviceVisitor", tags = "访客管理")
@AllArgsConstructor
public class ProjectVisitorController {
    @Resource
    private RemoteProjectVisitorService remoteProjectVisitorService;

//    /**
//     * 分页查询(查询访客申请记录)
//     *
//     * @return
//     */
//    @ApiOperation(value = "分页查询", notes = "分页查询")
//    @GetMapping("/page")
//    public R fetchList(ProjectVisitorSearchConditionPage projectVisitorSearchConditionPage) {
//        return remoteProjectVisitorService.fetchList(projectVisitorSearchConditionPage);
//    }

    /**
     * 物业登记或住户申请
     * 物业登记直接通过无需审核
     *
     * @param projectVisitorVo 访客vo对象
     * @return R
     * @author: 王良俊
     */
    @ApiOperation(value = "提交申请", notes = "提交申请")
    @SysLog("提交访客申请")
    @PostMapping("/register")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @PreAuthorize("@pms.hasPermission('service-visitor:post:register')")
    public R register(@RequestBody ProjectVisitorVo projectVisitorVo) {
        return remoteProjectVisitorService.register(projectVisitorVo);
    }

//    /**
//     * 访客申请微服务调用
//     *
//     * @param projectVisitorVo 访客vo对象
//     * @return R
//     */
//    @ApiOperation(value = "提交申请", notes = "提交申请")
//    @SysLog("提交访客申请")
//    @PostMapping("/registerVo")
//    public R registerVo(@RequestBody ProjectVisitorVo projectVisitorVo) {
//        return remoteProjectVisitorService.registerVo(projectVisitorVo);
//    }

    /**
     * <p>
     * 进行延时操作
     * </p>
     *
     * @param projectVisitorVo 访客vo对象
     * @return
     * @throws
     */
    @ApiOperation(value = "延时", notes = "延时")
    @SysLog("访客延时操作")
    @PostMapping("/delay")
    @PreAuthorize("@pms.hasPermission('service-visitor:post:delay')")
    public R delay(@RequestBody ProjectVisitorVo projectVisitorVo) {
        return remoteProjectVisitorService.delay(projectVisitorVo);
    }

//    /**
//     * <p>
//     * 拒绝访客申请
//     * </p>
//     *
//     * @param visitorVo 访客VO记录
//     * @return
//     * @throws
//     */
//    @ApiOperation(value = "拒绝访客申请", notes = "拒绝访客申请")
//    @SysLog("拒绝访客申请")
//    @PutMapping("/rejectAudit")
//    public R rejectAudit(@RequestBody ProjectVisitorVo visitorVo) {
//        return remoteProjectVisitorService.rejectAudit(visitorVo);
//    }

//    /**
//     * <p>
//     * 获取申请数据
//     * </p>
//     *
//     * @param visitId 访客申请id
//     * @return
//     * @throws
//     */
//    @ApiOperation(value = "获取申请数据", notes = "获取申请数据")
//    @SysLog("获取申请数据")
//    @GetMapping("/getData/{visitId}")
//    public R<ProjectVisitorVo> getData(@PathVariable("visitId") String visitId) {
//        return remoteProjectVisitorService.getData(visitId);
//    }

    /**
     * <p>
     * 签离
     * </p>
     *
     * @param visitId 访客申请id
     * @return
     * @throws
     */
    @ApiOperation(value = "签离", notes = "签离")
    @SysLog("签离")
    @DeleteMapping("/sign-off/{visitId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "visitId", value = "访客id", required = true, paramType = "path"),
    })
    @PreAuthorize("@pms.hasPermission('service-visitor:delete:signOff')")
    public R signOff(@PathVariable String visitId) {
        return remoteProjectVisitorService.signOff(visitId);
    }

//    /**
//     * <p>
//     * 自动签离调用接口
//     * </p>
//     *
//     * @return
//     * @throws
//     */
//    @ApiOperation(value = "签离", notes = "签离")
//    @SysLog("签离")
//    @GetMapping("/signOffAll")
//    public R signOffAll() {
//        return remoteProjectVisitorService.signOffAll();
//
//    }

//    /**
//     * <p>
//     * 业主审核通过
//     * </p>
//     *
//     * @param projectVisitorVo 访客vo对象
//     * @return
//     * @throws
//     */
//    @ApiOperation(value = "业主审核通过", notes = "业主审核通过")
//    @SysLog("审核通过")
//    @PostMapping("/homeowersPassAudit")
//    public R homeowersPassAudit(@RequestBody ProjectVisitorVo projectVisitorVo) {
//        return remoteProjectVisitorService.homeowersPassAudit(projectVisitorVo);
//    }

//    /**
//     * <p>
//     * 审核通过
//     * </p>
//     *
//     * @param projectVisitorVo 访客vo对象
//     * @return
//     * @throws
//     */
//    @ApiOperation(value = "审核通过", notes = "审核通过")
//    @SysLog("审核通过")
//    @PostMapping("/passAudit")
//    public R passAudit(@RequestBody ProjectVisitorVo projectVisitorVo) {
//        return remoteProjectVisitorService.passAudit(projectVisitorVo);
//    }

//    /**
//     * <p>
//     * 审核通过
//     * </p>
//     *
//     * @param visitorVoList 访客vo对象列表
//     */
//    @ApiOperation(value = "审核通过", notes = "审核通过")
//    @SysLog("审核通过")
//    @PostMapping("/passAuditBatch")
//    public R passAuditBatch(@RequestBody List<ProjectVisitorVo> visitorVoList) {
//        return remoteProjectVisitorService.passAuditBatch(visitorVoList);
//    }

    /**
     * <p>
     * 使用手机号获取访客数据
     * </p>
     *
     * @param mobileNo 访客手机号
     * @return
     * @throws
     */
    @ApiOperation(value = "使用手机号获取访客数据", notes = "使用手机号获取访客数据")
    @SysLog("使用手机号获取访客数据")
    @GetMapping("/getByMobileNo/{mobileNo}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "mobileNo", value = "手机号", required = true, paramType = "path"),
    })
    @PreAuthorize("@pms.hasPermission('service-visitor:get:getByMobileNo')")
    public R getByMobileNo(@PathVariable("mobileNo") String mobileNo) {
        return remoteProjectVisitorService.getByMobileNo(mobileNo);
    }

//    /**
//     * <p>
//     * 根据手机号和申请id判断是否可以执行此次操作
//     * </p>
//     *
//     * @param mobileNo 访客手机号
//     * @param visitId  访客申请id
//     * @return
//     * @throws
//     */
//    @ApiOperation(value = "根据手机号和申请id判断是否可以执行此次操作", notes = "根据手机号和申请id判断是否可以执行此次操作")
//    @SysLog("根据手机号和申请id判断是否可以执行此次操作")
//    @GetMapping("/isAllLeave/{mobileNo}/{visitId}")
//    public R isAllLeave(@PathVariable String mobileNo, @PathVariable String visitId) {
//        return remoteProjectVisitorService.isAllLeave(mobileNo, visitId);
//    }

    /**
     * <p>
     * 根据传入的访客ID和访问时间对访客的访问时间进行更新
     * </p>
     *
     * @param visitorHisVo 访客记录VO对象
     * @return 访客记录ID visitId
     * @author: 王良俊
     */
    @ApiOperation(value = "根据传入的访客ID和访问时间对访客的访问时间进行更新", notes = "根据传入的访客ID和访问时间对访客的访问时间进行更新")
    @SysLog("根据传入的访客ID和访问时间对访客的访问时间进行更新")
    @PostMapping("/saveVisitTime")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @PreAuthorize("@pms.hasPermission('service-visitor:post:updateVisitTime')")
    public R updateVisitTime(@RequestBody ProjectVisitorHis visitorHisVo) {
        return remoteProjectVisitorService.updateVisitTime(visitorHisVo);
    }
}
