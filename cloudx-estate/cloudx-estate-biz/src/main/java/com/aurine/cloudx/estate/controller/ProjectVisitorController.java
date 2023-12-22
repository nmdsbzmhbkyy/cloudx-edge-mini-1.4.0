

package com.aurine.cloudx.estate.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.estate.constant.enums.AuditStatusEnum;
import com.aurine.cloudx.estate.entity.ProjectVisitor;
import com.aurine.cloudx.estate.entity.ProjectVisitorHis;
import com.aurine.cloudx.estate.service.ProjectVisitorHisService;
import com.aurine.cloudx.estate.service.ProjectVisitorService;
import com.aurine.cloudx.estate.service.adapter.AbstractProjectVisitorService;
import com.aurine.cloudx.estate.vo.ProjectVisitorSearchConditionVo;
import com.aurine.cloudx.estate.vo.ProjectVisitorVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


/**
 * 访客
 *
 * @author 王伟
 * @date 2020-06-03 19:42:52
 */
@RestController
@RequestMapping("/serviceVisitor")
@Api(value = "serviceVisitor", tags = "访客管理")
public class ProjectVisitorController {
    @Resource
    private ProjectVisitorService projectVisitorService;
    @Resource
    private AbstractProjectVisitorService adapterProjectVisitorServiceImpl;
    @Resource
    private ProjectVisitorHisService projectVisitorHisService;

    /**
     * 分页查询(查询访客申请记录)
     *
     * @param page            分页对象
     * @param searchCondition 查询条件
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R fetchList(Page page, ProjectVisitorSearchConditionVo searchCondition) {
        return R.ok(projectVisitorService.getPage(page, searchCondition));
    }

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
    public R register(@RequestBody ProjectVisitorVo projectVisitorVo) {
        projectVisitorVo.setAuditStatus(AuditStatusEnum.inAudit.code);
        return R.ok(projectVisitorService.register(projectVisitorVo));
    }

    /**
     * 访客申请微服务调用
     *
     * @param projectVisitorVo 访客vo对象
     * @return R
     */
    @ApiOperation(value = "提交申请", notes = "提交申请")
    @SysLog("提交访客申请")
    @PostMapping("/registerVo")
    public R registerVo(@RequestBody ProjectVisitorVo projectVisitorVo) {
        return R.ok(projectVisitorService.register(projectVisitorVo));
    }

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
    public R delay(@RequestBody ProjectVisitorVo projectVisitorVo) {
        boolean delay = projectVisitorService.delay(projectVisitorVo.getVisitId(), projectVisitorVo.getTimeRange(), projectVisitorVo.getCycleRange());
        if (delay) {
            return R.ok("延时成功");
        }
        return R.failed("延时失败");
    }

    /**
     * <p>
     * 拒绝访客申请
     * </p>
     *
     * @param visitorVo 访客VO记录
     * @return
     * @throws
     */
    @ApiOperation(value = "拒绝访客申请", notes = "拒绝访客申请")
    @SysLog("拒绝访客申请")
    @PutMapping("/rejectAudit")
    public R rejectAudit(@RequestBody ProjectVisitorVo visitorVo) {
        Boolean rejectAudit = projectVisitorService.rejectAudit(visitorVo.getVisitId(), visitorVo.getRejectReason());
        if (rejectAudit) {
            return R.ok("操作成功");
        }
        return R.failed("操作失败");
    }

    /**
     * <p>
     * 获取申请数据
     * </p>
     *
     * @param visitId 访客申请id
     * @return
     * @throws
     */
    @ApiOperation(value = "获取申请数据", notes = "获取申请数据")
    @SysLog("获取申请数据")
    @GetMapping("/getData/{visitId}")
    public R<ProjectVisitorVo> getData(@PathVariable String visitId) {
        return R.ok(projectVisitorService.getDataById(visitId));
    }


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
    @GetMapping("/signOff/{visitId}")
    public R signOff(@PathVariable String visitId) {
        boolean signOff = adapterProjectVisitorServiceImpl.signOff(visitId);
        if (signOff) {
            return R.ok("签离成功");
        } else {
            return R.failed("签离失败");
        }
    }

    /**
     * <p>
     * 自动签离调用接口
     * </p>
     *
     * @return
     * @throws
     */
    @ApiOperation(value = "签离", notes = "签离")
    @SysLog("签离")
    @GetMapping("/signOffAll")
    public R signOffAll() {
        try {
            projectVisitorService.signOffAll();
        } catch (Exception e) {
            return R.failed("自动签离失败");
        }
        return R.failed("自动签离成功");

    }

    /**
     * <p>
     * 业主审核通过
     * </p>
     *
     * @param projectVisitorVo 访客vo对象
     * @return
     * @throws
     */
    @ApiOperation(value = "业主审核通过", notes = "业主审核通过")
    @SysLog("审核通过")
    @PostMapping("/homeowersPassAudit")
    public R homeowersPassAudit(@RequestBody ProjectVisitorVo projectVisitorVo) {
        boolean passAudit = adapterProjectVisitorServiceImpl.passAudit(projectVisitorVo.getVisitId(), AuditStatusEnum.inAudit);
        if (passAudit) {
            return R.ok("审核通过");
        } else {
            return R.failed("操作失败");
        }
    }

    /**
     * <p>
     * 审核通过
     * </p>
     *
     * @param projectVisitorVo 访客vo对象
     * @return
     * @throws
     */
    @ApiOperation(value = "审核通过", notes = "审核通过")
    @SysLog("审核通过")
    @PostMapping("/passAudit")
    public R passAudit(@RequestBody ProjectVisitorVo projectVisitorVo) {
        boolean passAudit = adapterProjectVisitorServiceImpl.passAudit(projectVisitorVo.getVisitId(), AuditStatusEnum.pass);
        if (passAudit) {
            return R.ok("审核通过");
        } else {
            return R.failed("操作失败");
        }
    }

    /**
     * <p>
     * 审核通过
     * </p>
     *
     * @param visitorVoList 访客vo对象列表
     */
    @ApiOperation(value = "审核通过", notes = "审核通过")
    @SysLog("审核通过")
    @PostMapping("/passAuditBatch")
    public R passAuditBatch(@RequestBody List<ProjectVisitorVo> visitorVoList) {
        boolean passAudit = projectVisitorService.passAuditBatch(visitorVoList);
        return R.ok(passAudit);
    }

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
    public R getByMobileNo(@PathVariable String mobileNo) {
        List<ProjectVisitor> list = projectVisitorService.list(new QueryWrapper<ProjectVisitor>().lambda().eq(ProjectVisitor::getMobileNo, mobileNo));
        if (CollUtil.isNotEmpty(list)) {
            /*if (BeanUtil.isNotEmpty(list.get(0)) && projectVisitorHisService.checkVisitorById(list.get(0).getVisitorId())) {
                return R.failed("该访客有未签离申请，无法再次申请");
            }*/
            return R.ok(list.get(0));
        } else {
            return R.ok();
        }
    }

    /**
     * <p>
     * 根据手机号和申请id判断是否可以执行此次操作
     * </p>
     *
     * @param mobileNo 访客手机号
     * @param visitId  访客申请id
     * @return
     * @throws
     */
    @ApiOperation(value = "根据手机号和申请id判断是否可以执行此次操作", notes = "根据手机号和申请id判断是否可以执行此次操作")
    @SysLog("根据手机号和申请id判断是否可以执行此次操作")
    @GetMapping("/isAllLeave/{mobileNo}/{visitId}")
    public R isAllLeave(@PathVariable String mobileNo, @PathVariable String visitId) {
        List<ProjectVisitor> list = projectVisitorService.list(new QueryWrapper<ProjectVisitor>().lambda().eq(ProjectVisitor::getMobileNo, mobileNo));
        if (CollUtil.isNotEmpty(list)) {
            if (BeanUtil.isNotEmpty(list.get(0)) && projectVisitorHisService.checkVisitorById(list.get(0).getVisitorId(), visitId)) {
                return R.failed("该访客有未签离申请，无法执行本次操作");
            }
        }
        return R.ok();
    }

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
    public R updateVisitTime(ProjectVisitorHis visitorHisVo) {
        return R.ok(projectVisitorHisService.saveVisitTime(visitorHisVo.getVisitorId(), visitorHisVo.getVisitTime()));
    }

}
