package com.aurine.cloudx.estate.controller;

import cn.hutool.core.bean.BeanUtil;
import com.aurine.cloudx.estate.entity.SysPlatFeedback;
import com.aurine.cloudx.estate.service.SysPlatFeedbackService;
import com.aurine.cloudx.estate.vo.AppFeedbackFormVo;
import com.aurine.cloudx.estate.vo.AppFeedbackPageVo;
import com.aurine.cloudx.estate.vo.SysPlatFeedbackFormVo;
import com.aurine.cloudx.estate.vo.SysPlatFeedbackVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestController
@RequestMapping("feedback")
@Api(value = "/feedback", tags = "意见反馈管理")
public class SysPlatFeedbackController {


    @Resource
    private SysPlatFeedbackService sysPlatFeedbackService;

    @PostMapping("/register")
    @ApiOperation(value = "意见反馈登记", notes = "意见反馈登记")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<Boolean> register (@RequestBody AppFeedbackFormVo feedbackFormVo) {
        SysPlatFeedback sysPlatFeedback = new SysPlatFeedback();
        BeanUtil.copyProperties(feedbackFormVo, sysPlatFeedback);
        //获取当前登录用户账号
        Integer userId = SecurityUtils.getUser().getId();
        sysPlatFeedback.setUserId(userId.toString());
        sysPlatFeedback.setStatus("0");
//        sysPlatFeedback.setOrigin("3");
        sysPlatFeedback.setFeedbackTime(LocalDateTime.now());
        return R.ok(sysPlatFeedbackService.save(sysPlatFeedback));
    }

    /**
     * 分页查询所有数据
     *
     * @param page            分页对象
     * @return 所有数据
     */
    @GetMapping("/page")
    @ApiOperation(value = "获取当前账号意见反馈列表", notes = "获取当前账号意见反馈列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<Page<AppFeedbackPageVo>> selectAll(Page page) {
        //获取当前登录用户账号
        Integer userId = SecurityUtils.getUser().getId();
        Page<SysPlatFeedback> sysPlatFeedbackVoPage = sysPlatFeedbackService.page(page, new QueryWrapper<SysPlatFeedback>().lambda().
                eq(SysPlatFeedback::getUserId, userId)
                .ne(SysPlatFeedback::getOrigin, "1")
                .orderByDesc(SysPlatFeedback::getFeedbackTime));
        Page<AppFeedbackPageVo> appFeedbackPageVoPage = new Page<>();
        BeanUtil.copyProperties(sysPlatFeedbackVoPage, appFeedbackPageVoPage);
        appFeedbackPageVoPage.setRecords(sysPlatFeedbackVoPage.getRecords().stream()
                .map(e -> {
                    AppFeedbackPageVo appFeedbackPageVo = new AppFeedbackPageVo();
                    BeanUtil.copyProperties(e, appFeedbackPageVo);
                    return appFeedbackPageVo;
                }).collect(Collectors.toList()));
        return R.ok(appFeedbackPageVoPage);
    }
}
