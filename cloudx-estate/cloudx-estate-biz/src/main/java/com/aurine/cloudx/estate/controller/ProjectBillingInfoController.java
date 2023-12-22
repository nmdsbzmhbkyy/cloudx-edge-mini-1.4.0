package com.aurine.cloudx.estate.controller;

import cn.hutool.core.bean.BeanUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.FeeConstant;
import com.aurine.cloudx.estate.constant.enums.PayStatusEnum;
import com.aurine.cloudx.estate.constant.enums.PayTypeEnum;
import com.aurine.cloudx.estate.entity.ProjectBillDayConf;
import com.aurine.cloudx.estate.entity.ProjectBillingInfo;
import com.aurine.cloudx.estate.entity.ProjectPaymentRecord;
import com.aurine.cloudx.estate.feign.RemotePayingService;
import com.aurine.cloudx.estate.service.ProjectBillDayConfService;
import com.aurine.cloudx.estate.service.ProjectBillingInfoService;
import com.aurine.cloudx.estate.service.ProjectPaymentRecordService;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
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
@RequestMapping("projectBillingInfo")
@Api(value = "projectBillingInfo", tags = "项目已出账的账单信息")
public class ProjectBillingInfoController {
    /**
     * 服务对象
     */
    @Resource
    @Lazy
    private ProjectBillingInfoService projectBillingInfoService;


    @Resource
    private ProjectBillDayConfService projectBillDayConfService;

    @Resource
    private RemotePayingService remotePayingService;

    @Resource
    private ProjectPaymentRecordService projectPaymentRecordService;
    /**
     * 分页查询所有数据
     *
     * @param page               分页对象
     * @param projectBillingInfo 查询实体
     * @return 所有数据
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询projectBillingInfo所有数据", notes = "分页查询projectBillingInfo所有数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<IPage<ProjectBillingInfoVo>> selectAll(Page<ProjectBillingInfoVo> page, ProjectBillingInfoFormVo projectBillingInfo) {

        return R.ok(this.projectBillingInfoService.pageBill(page, projectBillingInfo));
    }

    @PostMapping("/exportExcel")
    @ApiOperation(value = "导出excel", notes = "导出excel")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public void exportExcel(Page<ProjectBillingInfoVo> page, ProjectBillingInfoFormVo projectBillingInfo, HttpServletResponse httpServletResponse) {
        projectBillingInfoService.exportExcel(page, projectBillingInfo, httpServletResponse);
    }


    /**
     * 查询缴费分类汇总分页
     *
     * @param page
     * @param query
     * @return
     */
    @GetMapping("/getFeeReportPage")
    @ApiOperation(value = "查询缴费分类汇总", notes = "查询缴费分类汇总")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R getFeeReportPage(Page<ProjectHouseFeeTotalVo> page, ProjectBillingInfoVo query) {
        return R.ok(projectBillingInfoService.getFeeReportPage(page, query));
    }


    /**
     * 查询缴费分类汇总
     *
     * @param query
     * @return
     */
    @GetMapping("/getFeeRate")
    @ApiOperation(value = "查询缴费分类汇总", notes = "查询缴费分类汇总")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R getFeeRate(ProjectBillingInfoVo query) {
        return R.ok(projectBillingInfoService.getFeeRate(query));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @ApiOperation(value = "通过id查询", notes = "通过主键查询projectBillingInfo单条数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "账单id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<ProjectBillingInfo> selectOne(@PathVariable String id) {
        return R.ok(this.projectBillingInfoService.getById(id));
    }


    /**
     * 分页查询房屋费用统计数据
     *
     * @param page                       分页对象
     * @param projectHouseFeeTotalFormVo 查询实体
     * @return 所有数据
     */
    @GetMapping("/pageByHouse")
    @ApiOperation(value = "分页查询projectHouseFeeItem所有数据", notes = "分页查询projectHouseFeeItem所有数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<IPage<ProjectHouseFeeTotalVo>> selectAll(Page<ProjectHouseFeeTotalVo> page, ProjectHouseFeeTotalFormVo projectHouseFeeTotalFormVo) {
        return R.ok(this.projectBillingInfoService.pageBillTotal(page, projectHouseFeeTotalFormVo));
    }


    /**
     * 按当前时段查询优惠后的账单列表
     *
     * @param id 房屋id
     * @return 优惠后账单列表
     */
    @GetMapping("/listOnPromotion/{id}")
    @ApiOperation(value = "按当前时段查询优惠后的账单列表", notes = "按当前时段查询优惠后的账单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "房屋id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<List<ProjectBillPromotionVo>> listOnPromotion(@PathVariable String id) {
        return R.ok(projectBillingInfoService.listOnPromotion(id));
    }

    /**
     * 获取押金列表
     *
     * @param id 房屋id
     * @return 获取押金列表
     */
    @GetMapping("/listOnDeposit/{id}")
    @ApiOperation(value = "获取押金列表", notes = "获取押金列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "房屋id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<List<ProjectBillingInfoVo>> listOnDeposit(@PathVariable String id) {
        return R.ok(projectBillingInfoService.listOnDeposit(id));
    }

    /**
     * 退押金
     *
     * @return
     */
    @PutMapping("/updateDeposit")
    @ApiOperation(value = "退押金", notes = "退押金")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R updateDeposit(@RequestBody List<String> ids) {
        projectBillingInfoService.updateDeposit(ids);
        return R.ok();
    }

    /**
     * 按当前时段查询优惠后的预存费用
     *
     * @param id   房屋id
     * @param type 时长类型
     * @return 优惠后账单列表
     */
    @ApiOperation(value = "按当前时段查询优惠后的预存费用", notes = "按当前时段查询优惠后的预存费用")
    @GetMapping("/listOnPrePromotion/{type}/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "房屋id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "type", value = "时长类型", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<List<ProjectBillPromotionVo>> listOnPrePromotion(@PathVariable("id") String id, @PathVariable("type") Integer type) {
        return R.ok(projectBillingInfoService.listOnPrePromotion(id, type));
    }

    /**
     * 生成账单
     *
     * @return
     */
    @PostMapping("/resent")
    @ApiOperation(value = "生成账单", notes = "生成账单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R resentBillingInfo(@RequestBody ProjectHouseFeeTotalVo projectHouseFeeTotalVo) {
        List<ProjectBillDayConf> dayConfs = this.projectBillDayConfService.list();
        String date = "01";
        if (dayConfs != null && dayConfs.size() > 0) {
            date = dayConfs.get(0).getBillDay();
        }
        Integer dateInt = Integer.valueOf(date);
        Integer thisDateInt = LocalDate.now().getDayOfMonth();
//        if (thisDateInt - dateInt < 0 || thisDateInt - dateInt >= 10) {
//            return R.failed("只能在账单日后10天内重新生成账单");
//        }
        return projectBillingInfoService.resentBillingInfo(projectHouseFeeTotalVo);
    }

    @PostMapping("/resentBatch")
    @ApiOperation(value = "生成账单", notes = "生成账单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R resentBillingInfoBatch() {

        return projectBillingInfoService.resentBillingInfoBatch();
    }

    /**
     * 按当前时段查询优惠后的账单列表(App端)
     *
     * @param id 房屋id
     * @return 优惠后账单列表
     */
    @GetMapping("/listOnPromotionApp/{id}")
    @ApiOperation(value = "按当前时段查询优惠后的账单列表", notes = "按当前时段查询优惠后的账单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "房屋id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<HashMap<String, Map<String, List<ProjectBillPromotionVo>>>> listOnPromotionApp(@PathVariable String id) {
        List<ProjectBillPromotionVo> billList = projectBillingInfoService.listOnPromotion(id);
        HashMap<String, Map<String, List<ProjectBillPromotionVo>>> hashMap = new HashMap<>();

        Map<String, List<ProjectBillPromotionVo>> collect = billList.stream().collect(Collectors.groupingBy(ProjectBillPromotionVo::getBillMonth));

        collect.forEach((k, v) -> {
            hashMap.put(k, v.stream().collect(Collectors.groupingBy(ProjectBillPromotionVo::getFeeLabel)));
        });
        return R.ok(hashMap);
    }


    /**
     * 分页查询所有数据
     *
     * @param page               分页对象
     * @param projectBillingInfo 查询实体
     * @return 所有数据
     */
    @GetMapping("/pageApp")
    @ApiOperation(value = "分页查询账单已缴费所有数据", notes = "分页查询账单已缴费所有数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<Page<ProjectBillingInfoVo>> selectAllApp(Page<ProjectBillingInfoVo> page, ProjectBillingInfoFormVo projectBillingInfo) {
        projectBillingInfo.setPayStatus(PayStatusEnum.PAID.code);
        IPage<ProjectBillingInfoVo> pageList = this.projectBillingInfoService.pageBill(page, projectBillingInfo);
        Page<ProjectBillingInfoVo> pageRen = new Page<>();
        BeanUtil.copyProperties(pageList, pageRen);
        return R.ok(pageRen);
    }


    /**
     * 查询项目缴费字典
     */
    @GetMapping("/getDict")
    @ApiOperation(value = "查询项目缴费字典", notes = "查询项目缴费字典")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<List<Map<String, String>>> getDict() {

        return R.ok(projectBillingInfoService.getDict());
    }


    /**
     * 获取明细
     *
     * @return
     */
    @GetMapping("/getSumFeeByToday/{orderNo}")
    @ApiOperation(value = "通过缴费单号查询明细", notes = "通过缴费单号查询明细")
    public R<List<AppProjectBillingInfoVo>> getDetailByOrder(@PathVariable("orderNo") String orderNo) {

        return R.ok(projectBillingInfoService.getDetailByOrder(orderNo));
    }

    /**
     * 发送催缴通知
     *
     * @param houseId
     * @return
     */
    @GetMapping("/call/{houseId}")
    public R callByHouseId(@PathVariable("houseId") String houseId) {

        return projectBillingInfoService.callByHouseId(houseId);
    }

    /**
     * 根据订单号查到金额
     *
     * @param billingNo
     * @return
     */
    @GetMapping("/getMoneyByBillingNo/{billingNo}")
    @Inner(false)
    public R getMoneyByBillingNo(@PathVariable("billingNo") String billingNo) {
        return projectBillingInfoService.getMoneyByBillingNo(billingNo);
    }

    /**
     * 根据订单号查到金额
     *
     * @param billingNo
     * @return
     */
    @GetMapping("/getAppMoneyByBillingNo/{billingNo}")
    public R getAppMoneyByBillingNo(@PathVariable("billingNo") String billingNo) {
        return projectBillingInfoService.getMoneyByBillingNo(billingNo);
    }

    /**
     * 平台端支付接口
     * @param projectPaymentRecordVo
     * @return
     */
    @PostMapping("/platformPay")
    public R platformPay(@RequestBody ProjectPaymentRecordVo projectPaymentRecordVo) {
        projectPaymentRecordVo.setPayType(PayTypeEnum.WECHAT.code);
        return remotePayingService.platformPay(projectPaymentRecordVo);
    }

    /**
     * 调用微信api查询订单状态
     * @param transactionId
     * @param outTradeNo
     * @return
     */
    @GetMapping("/queryOrder")
    public R queryOrder(@RequestParam(value = "transactionId", required = false) String transactionId, @RequestParam(value = "outTradeNo", required = false) String outTradeNo) {
        Integer projectId = ProjectContextHolder.getProjectId();
        return remotePayingService.queryOrder(transactionId, outTradeNo, projectId);
    }
    @PutMapping("/updateOrderStatus")
    public R updateOrderStatus (@RequestBody QueryWxOrderVo queryWxOrderVo){
        // 如果订单状态为未更新的则去更新订单状态，此处为手动更新，防止rabbitmq出问题时订单没有及时更新的情况
        int count = projectPaymentRecordService.count(new LambdaQueryWrapper<ProjectPaymentRecord>()
                .eq(ProjectPaymentRecord::getPayOrderNo, queryWxOrderVo.getPayOrderNo()).eq(ProjectPaymentRecord::getOrderStatus, FeeConstant.UNPAID));
        if (count > 0) {
            Map params = new HashMap();
            params.put("payOrderNo",queryWxOrderVo.getPayOrderNo());
            params.put("transactionId",queryWxOrderVo.getTransactionId());
            params.put("subMchId",queryWxOrderVo.getSubMchId());
            projectPaymentRecordService.updateOrderStatus(params);
            return R.ok("更新成功");
        }
        return R.failed("更新失败");
    }
}