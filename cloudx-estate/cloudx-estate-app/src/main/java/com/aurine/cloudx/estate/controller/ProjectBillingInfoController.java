package com.aurine.cloudx.estate.controller;

import cn.hutool.core.bean.BeanUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.dto.ProjectBillingInfoFormDTO;
import com.aurine.cloudx.estate.entity.ProjectBillDayConf;
import com.aurine.cloudx.estate.entity.ProjectBillingInfo;
import com.aurine.cloudx.estate.feign.RemoteBillingInfoService;
import com.aurine.cloudx.estate.feign.RemotePayingService;
import com.aurine.cloudx.estate.feign.RemotePaymentRecordService;
import com.aurine.cloudx.estate.service.ProjectBillDayConfService;
import com.aurine.cloudx.estate.service.ProjectBillingInfoService;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.api.feign.RemoteUserService;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 项目已出账的账单信息(ProjectBillingInfo)表控制层
 *
 * @author makejava
 * @since 2020-07-20 16:43:48
 */
@RestController
@RequestMapping("billingInfo")
@Api(value = "projectBillingInfo", tags = "项目账单信息")
public class ProjectBillingInfoController {




    @Resource
    private RemoteBillingInfoService remoteBillingInfoService;


    @Resource
    private RemotePaymentRecordService remotePaymentRecordService;

    @Resource
    private RemotePayingService remotePayingService;



    /**
     * 分页查询所有数据 业主端
     *

     * @return 所有数据
     */
    @GetMapping("/page")
    @ApiOperation(value = "查询已缴费账单信息", notes = "查询已缴费账单信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<AppBillingInfoVo>  selectAll(ProjectBillingInfoFormDTO billingInfoFormDTO) {
        Page<ProjectBillingInfoVo> pageList = remoteBillingInfoService.selectAll(billingInfoFormDTO).getData();
        List<ProjectBillingInfoVo> records = pageList.getRecords();
        HashMap<String, Map<String, List<ProjectBillingInfoVo>>> hashMap = new HashMap<>();
        AppBillingInfoVo appBillingInfoVo = new AppBillingInfoVo();
        Map<String, List<ProjectBillingInfoVo>> collect = records.stream().collect(Collectors.groupingBy(ProjectBillingInfoVo::getBillMonth));
        collect.forEach((k,v)->{
            hashMap.put(k,v.stream().collect(Collectors.groupingBy(ProjectBillingInfoVo::getFeeLabel)));
        });
        appBillingInfoVo.setMapList(hashMap);
        return R.ok(appBillingInfoVo);
    }

    /**
     * 分页查询所有数据 物业端
     *

     * @return 所有数据
     */
    @GetMapping("/page-property")
    @ApiOperation(value = "查询已缴费账单信息(物业端)", notes = "查询已缴费账单信息(物业端)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<Page<AppProjectPaymentRecordVo>>  selectAllProperty(AppProjectPaymentRecordFromVo projectPaymentRecordVo) {
              return remotePaymentRecordService.selectAllApp(projectPaymentRecordVo);
}





    /**
     * 按当前时段查询优惠后的账单列表
     *
     * @param id 房屋id
     * @return 优惠后账单列表
     */
    @GetMapping("/list-on-promotion/{id}")
    @ApiOperation(value = "按当前时段查询优惠后的未缴费账单列表", notes = "按当前时段查询优惠后的未缴费账单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "房屋id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<AppBillPromotionVo> listOnPromotion(@PathVariable String id) {

        return R.ok(new AppBillPromotionVo(remoteBillingInfoService.listOnPromotionApp(id).getData())) ;
    }



    /**
     * 查询缴费类型字典
     *
     */
    @GetMapping("/get-dict")
    @ApiOperation(value = "查询当前小区缴费类型字典", notes = "查询当前小区缴费类型字典")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<List<Map<String,String>>> getDict() {

        return   remoteBillingInfoService.getDict();
    }


    /**
     * 获取明细
     *
     * @return
     */
    @GetMapping("/getSumFeeByToday/{orderNo}")
    @ApiOperation(value = "通过缴费单号查询明细", notes = "通过缴费单号查询明细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderNo", value = "缴费单号", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<List<AppProjectBillingInfoVo>> getDetailByOrder(@PathVariable("orderNo") String orderNo) {

        return remoteBillingInfoService.getDetailByOrder(orderNo);
    }


    /**
     * 催缴
     *
     * @param houseId 房屋id
     * @return 根据房屋id催缴
     */
    @GetMapping("/call/{houseId}")
    @ApiOperation(value = "根据房屋id催缴", notes = "根据房屋id催缴")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "房屋id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R callByHouseId(@PathVariable("houseId") String houseId) {
        return remoteBillingInfoService.callByHouseId(houseId);
    }

    /**
     * 支付物业费
     *
     * @param
     * @return 支付物业费物业端
     */
    @PostMapping("/wyPayWyPropertyFees")
    @ApiOperation(value = "支付物业费物业端", notes = "支付物业费物业端")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R wyPayPropertyFees(@RequestBody ProjectPaymentRecordVo projectPaymentRecord) {
        List<BigDecimal> list = new ArrayList<>();
        projectPaymentRecord.getPayBills().forEach(data->{
            list.add(new BigDecimal(remoteBillingInfoService.getAppMoneyByBillingNo(data.getId()).getData().toString()).setScale(2, BigDecimal.ROUND_HALF_UP));
        });
        BigDecimal reduce = list.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        projectPaymentRecord.setMoney(String.valueOf(Integer.parseInt(reduce.movePointRight(2).toPlainString())));
        projectPaymentRecord.setProjectId(ProjectContextHolder.getProjectId());
        projectPaymentRecord.setActAmount(reduce);
        R r = remotePayingService.AppWyPay(projectPaymentRecord);
        return r;
    }


}