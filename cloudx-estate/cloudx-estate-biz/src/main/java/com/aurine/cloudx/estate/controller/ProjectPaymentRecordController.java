package com.aurine.cloudx.estate.controller;
import cn.hutool.core.bean.BeanUtil;

import com.aurine.cloudx.estate.service.ProjectBillingInfoService;
import com.aurine.cloudx.estate.util.RedisUtilBill;
import com.aurine.cloudx.estate.vo.*;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pig4cloud.pigx.common.core.util.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aurine.cloudx.estate.entity.ProjectPaymentRecord;
import com.aurine.cloudx.estate.service.ProjectPaymentRecordService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Api;
import javax.annotation.Resource;

import javax.servlet.http.HttpServletResponse;

import java.time.LocalDate;

import java.time.format.DateTimeFormatter;


/**
 * 交易记录(ProjectPaymentRecord)表控制层
 *
 * @author 黄阳光 huangyg@aurine.cn
 * @since 2020-07-23 18:54:07
 */
@RestController
@RequestMapping("projectPaymentRecord")
@Api(value="projectPaymentRecord",tags="交易记录")
@Slf4j
public class ProjectPaymentRecordController  {
    /**
     * 服务对象
     */
    @Resource
    private ProjectPaymentRecordService projectPaymentRecordService;
    @Resource
    private ProjectBillingInfoService projectBillingInfoService;
    @Resource
    private RedisUtilBill redisUtilBill;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param projectPaymentRecord 查询实体
     * @return 所有数据
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询", notes = "分页查询projectPaymentRecord所有数据")
    public R selectAll(Page<ProjectPaymentRecord> page, ProjectPaymentRecordFormVo projectPaymentRecord) {
        if (projectPaymentRecord.getStartTimeString() != null && !"".equals(projectPaymentRecord.getStartTimeString())) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            projectPaymentRecord.setStartTime(LocalDate.parse(projectPaymentRecord.getStartTimeString(), fmt));
        }
        if (projectPaymentRecord.getEndTimeString() != null && !"".equals(projectPaymentRecord.getEndTimeString())) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            projectPaymentRecord.setEndTime(LocalDate.parse(projectPaymentRecord.getEndTimeString(), fmt));
        }


        return R.ok(this.projectPaymentRecordService.pageAll(page,projectPaymentRecord));
    }
    
    /** 
     * @description: 导出excel
     * @param:  
     * @return:  
     * @author cjw
     * @date: 2021/7/13 8:44
     */
    @PostMapping("/exportExcel")
    @ApiOperation(value = "导出excel", notes = "导出excel")
    public void exportExcel(HttpServletResponse response,@RequestBody ProjectPaymentRecordFormVo projectPaymentRecord) {
        if (projectPaymentRecord.getStartTimeString() != null && !"".equals(projectPaymentRecord.getStartTimeString())) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            projectPaymentRecord.setStartTime(LocalDate.parse(projectPaymentRecord.getStartTimeString(), fmt));
        }
        if (projectPaymentRecord.getEndTimeString() != null && !"".equals(projectPaymentRecord.getEndTimeString())) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            projectPaymentRecord.setEndTime(LocalDate.parse(projectPaymentRecord.getEndTimeString(), fmt));
        }
        this.projectPaymentRecordService.exportExcel(response,projectPaymentRecord);
    }


    /**
     * 欠费付款
     *
     * @param projectPaymentRecord 实体对象
     * @return 结果
     */
    @PostMapping
    @ApiOperation(value = "欠费付款", notes = "欠费付款")
    public R insert(@RequestBody ProjectPaymentRecordVo projectPaymentRecord) {
        if (projectPaymentRecord.getPayBills() == null||projectPaymentRecord.getPayBills().size()==0) {
            return R.failed("请选择账单");
        }
        boolean savePayment = this.projectPaymentRecordService.savePayment(projectPaymentRecord);
        if(!savePayment) {
            return R.failed("该缴费项已存在");
        }
        return R.ok(savePayment);
    }

    /**
     * 添加预存账单
     *
     * @param projectPaymentRecord 实体对象
     * @return 结果
     */
    @PostMapping("/addPre")
    @ApiOperation(value = "添加预存账单", notes = "添加预存账单")
    public R addPre(@RequestBody ProjectPaymentRecordVo projectPaymentRecord) {
        if (projectPaymentRecord.getPayBills() == null||projectPaymentRecord.getPayBills().size()==0) {
            return R.failed("请选择账单");
        }
        return this.projectPaymentRecordService.savePrePayment(projectPaymentRecord);
    }

    /**
     * 获取今天的收入
     * @return
     */
    @GetMapping("/getSumFeeByToday")
    @ApiOperation(value = "获取今天的收入", notes = "获取今天的收入")
    public R getSumFeeByToday() {
        return R.ok(projectPaymentRecordService.getSumFeeByDate(LocalDate.now(), LocalDate.now().plusDays(1)));
    }


    /**
     * 分页查询所有数据 物业端
     *
     * @param page               分页对象
     */
    @GetMapping("/pageApp")
    @ApiOperation(value = "分页查询账单已缴费所有数据", notes = "分页查询账单已缴费所有数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public  R<Page<AppProjectPaymentRecordVo>> selectAllApp(Page<AppProjectPaymentRecordVo> page, AppProjectPaymentRecordFromVo appProjectPaymentRecordFrom) {
        IPage<AppProjectPaymentRecordVo> pageList = this.projectPaymentRecordService.selectAllApp(page, appProjectPaymentRecordFrom);
        Page<AppProjectPaymentRecordVo> pageRen = new Page<>();
        BeanUtil.copyProperties(pageList, pageRen);
        return R.ok(pageRen);
    }

    /**
     * 校验订单状态
     * @param
     * @return
     */
    @GetMapping("/checkOrderStatus/{payOrderNo}")
    @ApiOperation(value = "根据订单号校验订单状态", notes = "根据订单号校验订单状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public  R checkOrderStatus( @PathVariable("payOrderNo") String payOrderNo) {

        return R.ok(projectPaymentRecordService.selectByPayOrderNo(payOrderNo));
    }

    @PostMapping(value = "/checkCode")
    @ApiOperation(value = "查询缴费项是否被锁", notes = "查询缴费项是否被锁")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R checkCode (@RequestBody ProjectPaymentRecordVo projectPaymentRecord) {
        return projectPaymentRecordService.checkCode(projectPaymentRecord);
    }

}