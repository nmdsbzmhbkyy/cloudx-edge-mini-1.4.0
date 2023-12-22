package com.aurine.cloudx.estate.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.druid.support.json.JSONParser;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.PayTypeEnum;
import com.aurine.cloudx.estate.dto.ProjectBillingInfoFormDTO;
import com.aurine.cloudx.estate.feign.RemoteBillingInfoService;
import com.aurine.cloudx.estate.feign.RemotePayingService;
import com.aurine.cloudx.estate.feign.RemotePaymentRecordService;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.api.client.util.Value;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 项目账单信息
 *
 * @author yz
 * @since 2021-07-8 16:43:48
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
    public R<AppBillingInfoVo> selectAll(ProjectBillingInfoFormDTO billingInfoFormDTO) {
        Page<ProjectBillingInfoVo> pageList = remoteBillingInfoService.selectAll(billingInfoFormDTO).getData();
        List<ProjectBillingInfoVo> records = pageList.getRecords();
        HashMap<String, Map<String, List<ProjectBillingInfoVo>>> hashMap = new HashMap<>();
        AppBillingInfoVo appBillingInfoVo = new AppBillingInfoVo();
        Map<String, List<ProjectBillingInfoVo>> collect = records.stream().collect(Collectors.groupingBy(ProjectBillingInfoVo::getBillMonth));
        collect.forEach((k, v) -> {
            hashMap.put(k, v.stream().collect(Collectors.groupingBy(ProjectBillingInfoVo::getFeeLabel)));
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
    public R<Page<AppProjectPaymentRecordVo>> selectAllProperty(AppProjectPaymentRecordFromVo projectPaymentRecordVo) {
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

        return R.ok(new AppBillPromotionVo(remoteBillingInfoService.listOnPromotionApp(id).getData()));
    }


    /**
     * 查询缴费类型字典
     */
    @GetMapping("/get-dict")
    @ApiOperation(value = "查询当前小区缴费类型字典", notes = "查询当前小区缴费类型字典")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<List<Map<String, String>>> getDict() {

        return remoteBillingInfoService.getDict();
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






    @PostMapping(value = "/checkCode")
    @ApiOperation(value = "生成订单缴费前校验", notes = "生成订单前校验")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R checkCode (@RequestBody ProjectPaymentRecordVo projectPaymentRecord) {


        return remotePaymentRecordService.checkCode(projectPaymentRecord);
    }
    @GetMapping(value = "/checkOrderStatus/{payOrderNo}")
    @ApiOperation(value = "根据订单号校验订单状态", notes = "根据订单号校验订单状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R checkOrderStatus (@PathVariable("payOrderNo") String payOrderNo) {


        return remotePaymentRecordService.checkOrderStatus(payOrderNo);
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
        projectPaymentRecord.setPayType(PayTypeEnum.MINIAPPWY.code);
        R r = remotePayingService.miniAppWyPay(projectPaymentRecord);
        return r;
    }


    /**
     * 支付物业费
     *
     * @param
     * @return 支付物业费业主端
     */
    @PostMapping("/wyPayPropertyFees")
    @ApiOperation(value = "支付物业费业主端", notes = "支付物业费业主端")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R wyPayYzPropertyFees(@RequestBody ProjectPaymentRecordVo projectPaymentRecord) {
        List<BigDecimal> list = new ArrayList<>();
        projectPaymentRecord.getPayBills().forEach(data->{
            list.add(new BigDecimal(remoteBillingInfoService.getAppMoneyByBillingNo(data.getId()).getData().toString()).setScale(2, BigDecimal.ROUND_HALF_UP));
        });
        BigDecimal reduce = list.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        projectPaymentRecord.setMoney(String.valueOf(Integer.parseInt(reduce.movePointRight(2).toPlainString())));
        projectPaymentRecord.setProjectId(ProjectContextHolder.getProjectId());
        projectPaymentRecord.setActAmount(reduce);
        projectPaymentRecord.setPayType(PayTypeEnum.MINIAPPYZ.code);
        R r = remotePayingService.miniAppYzPay(projectPaymentRecord);
        return r;
    }

    /**
     * 获取openid
     *
     * @param
     * @return
     */
    @GetMapping("/getOpenId/{code}")
    @ApiOperation(value = "获取openid", notes = "获取openid")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R getOpenId(@PathVariable (value = "code")String code) {

        R r = remotePayingService.getOpenId(code);
        return r;
    }


}