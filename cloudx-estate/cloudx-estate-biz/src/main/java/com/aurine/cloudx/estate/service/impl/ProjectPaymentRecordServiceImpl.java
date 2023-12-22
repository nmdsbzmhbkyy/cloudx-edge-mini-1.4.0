package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.aurine.cloudx.common.core.constant.DataConstants;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.FeeConstant;
import com.aurine.cloudx.estate.constant.enums.PayStatusEnum;
import com.aurine.cloudx.estate.entity.ProjectBillingInfo;
import com.aurine.cloudx.estate.entity.ProjectPayAccountConf;
import com.aurine.cloudx.estate.entity.ProjectPaymentRecord;
import com.aurine.cloudx.estate.entity.ProjectStaff;
import com.aurine.cloudx.estate.excel.payment.PaymentRecordExcel;
import com.aurine.cloudx.estate.feign.RemotePayingService;
import com.aurine.cloudx.estate.mapper.ProjectPaymentRecordMapper;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.util.ExcelUtil;
import com.aurine.cloudx.estate.util.RedisUtilBill;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 交易记录(ProjectPaymentRecord)表服务实现类
 *
 * @author makejava
 * @since 2020-07-23 18:54:07
 */
@Service
public class ProjectPaymentRecordServiceImpl extends ServiceImpl<ProjectPaymentRecordMapper, ProjectPaymentRecord> implements ProjectPaymentRecordService {


    @Autowired
    ProjectBillingInfoService projectBillingInfoService;

    @Autowired
    ProjectPromotionFeeRelService projectPromotionFeeRelService;

    @Autowired
    ProjectPromotionConfService projectPromotionConfService;
    @Autowired
    ProjectPayAccountConfService projectPayAccountConfService;
    @Autowired
    ProjectStaffService staffService;
    @Autowired
    RemotePayingService  remotePayingService;
    @Resource
    private RedisUtilBill redisUtilBill;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean savePayment(ProjectPaymentRecordVo projectPaymentRecordVo) {
        List<String> billIds = projectPaymentRecordVo.getPayBills().stream().map(e -> e.getId()).collect(Collectors.toList());

        List<ProjectBillPromotionVo> promotionVos = projectBillingInfoService.listOnPromotion(projectPaymentRecordVo.getHouseId(), billIds, Arrays.asList(PayStatusEnum.UNPAID.code));
        BigDecimal payAbleAmount = new  BigDecimal(0);
        BigDecimal promotionAmount = new BigDecimal(0);
        BigDecimal actAmount =new BigDecimal(0);
        ProjectPaymentRecord projectPaymentRecord = new ProjectPaymentRecord();
        BeanUtils.copyProperties(projectPaymentRecordVo, projectPaymentRecord);
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        projectPaymentRecord.setPayOrderNo(uuid);
        List<ProjectBillingInfo> projectBillingInfos = new ArrayList<>();
        BigDecimal multiply2= new BigDecimal(0);
        for (int i = 0; i <promotionVos.size() ; i++) {
            ProjectBillingInfo projectBillingInfo = new ProjectBillingInfo();
            BeanUtils.copyProperties(promotionVos.get(i), projectBillingInfo);
            projectBillingInfo.setPayStatus(PayStatusEnum.PAID.code);
            projectBillingInfo.setPaidBy(projectPaymentRecordVo.getPaidBy());
            projectBillingInfo.setPayTime(LocalDateTime.now());
            projectBillingInfo.setPayOrderNO(uuid);
            if(ObjectUtil.isNotNull(projectPaymentRecordVo.getPromotionAmount())) {
                // 算出每一项的优惠金额
                BigDecimal multiply = (projectPaymentRecordVo.getPromotionAmount().divide(projectPaymentRecordVo.getPayableAmount(),20,BigDecimal.ROUND_HALF_UP)).multiply(promotionVos.get(i).getPayAbleAmount()).setScale(2, BigDecimal.ROUND_HALF_UP);
                projectBillingInfo.setPromotionAmount(multiply);
                // 设置应付金额等于
                projectBillingInfo.setActAmount(promotionVos.get(i).getPayAbleAmount().subtract(multiply));
                //防止值为空时添加报错
                if(i == promotionVos.size()-1) {
                    projectBillingInfo.setPromotionAmount(projectPaymentRecordVo.getPromotionAmount().subtract(multiply2));
                    projectBillingInfo.setActAmount(promotionVos.get(i).getPayAbleAmount().subtract(projectBillingInfo.getPromotionAmount()));
                }
                multiply2=multiply2.add(multiply);
            }else {
                projectBillingInfo.setActAmount(promotionVos.get(i).getActAmount());
                projectBillingInfo.setPromotionAmount(BigDecimal.ZERO);
            }


            projectBillingInfos.add(projectBillingInfo);
        }

        // 更新订单
        projectBillingInfoService.updateBatchById(projectBillingInfos);
        projectPaymentRecord.setActAmount(projectPaymentRecordVo.getActAmount());
        projectPaymentRecord.setPayableAmount(projectPaymentRecordVo.getPayableAmount());
        projectPaymentRecord.setPromotionAmount(projectPaymentRecordVo.getPromotionAmount());
        projectPaymentRecord.setPaidBy(projectPaymentRecordVo.getPaidBy());
        projectPaymentRecord.setOrderStatus(FeeConstant.RECEIVED);
        projectPaymentRecord.setPayTime(LocalDateTime.now());
        // 生成交易记录
        save(projectPaymentRecord);
        // 删除redis锁
        List<PayBillVo> payBills = projectPaymentRecordVo.getPayBills();
        List<String> collect = payBills.stream().sorted(Comparator.comparing(PayBillVo::getId)).map(item -> item.getId()).collect(Collectors.toList());
        String uuidLock = JSON.toJSONString(collect);
//        redisUtilBill.hdel(projectPaymentRecordVo.getHouseId()+"_lock", JSONObject.toJSONString(uuidLock));
//        redisUtilBill.hdel(projectPaymentRecordVo.getHouseId()+"_order_lock",JSONObject.toJSONString(uuidLock));
        redisUtilBill.del(projectPaymentRecord.getHouseId()+"_order"+"_"+uuidLock);

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean savePaymentNotPaid(ProjectPaymentRecordVo projectPaymentRecordVo) {
        List<String> billIds = projectPaymentRecordVo.getPayBills().stream().map(e -> e.getId()).collect(Collectors.toList());
        List<ProjectBillingInfo> list = projectBillingInfoService.list(new LambdaQueryWrapper<ProjectBillingInfo>().in(ProjectBillingInfo::getBillingNo, billIds));
//        list.stream().forEach(item -> {
//            this.remove(new QueryWrapper<ProjectPaymentRecord>().lambda().eq(ProjectPaymentRecord::getPayOrderNo, item.getPayOrderNO()));
//        });
        List<ProjectBillPromotionVo> promotionVos = projectBillingInfoService.listOnPromotion(projectPaymentRecordVo.getHouseId(), billIds, Arrays.asList(PayStatusEnum.UNPAID.code));
        BigDecimal payAbleAmount = new  BigDecimal(0);
        BigDecimal promotionAmount = new BigDecimal(0);
        BigDecimal actAmount =new BigDecimal(0);
        ProjectPaymentRecord projectPaymentRecord = new ProjectPaymentRecord();
        BeanUtils.copyProperties(projectPaymentRecordVo, projectPaymentRecord);
        projectPaymentRecord.setPayOrderNo(projectPaymentRecordVo.getPayOrderNo());
        List<ProjectBillingInfo> projectBillingInfos = new ArrayList<>();
        BigDecimal multiply2= new BigDecimal(0);
        for (int i = 0; i <promotionVos.size() ; i++) {
            ProjectBillingInfo projectBillingInfo = new ProjectBillingInfo();
            BeanUtils.copyProperties(promotionVos.get(i), projectBillingInfo);
            remove(new QueryWrapper<ProjectPaymentRecord>().lambda().eq(ProjectPaymentRecord::getPayOrderNo, projectBillingInfo.getPayOrderNO()));
            projectBillingInfo.setPayStatus(PayStatusEnum.UNPAID.code);
            projectBillingInfo.setPaidBy(projectPaymentRecordVo.getPaidBy());
            projectBillingInfo.setPayTime(LocalDateTime.now());
            projectBillingInfo.setCreateTime(LocalDateTime.now());
            projectBillingInfo.setPayOrderNO(projectPaymentRecordVo.getPayOrderNo());
            // 算出每一项的优惠金额
            if(ObjectUtil.isNotNull(projectPaymentRecordVo.getPromotionAmount())){
                //
                //BigDecimal multiply = projectPaymentRecordVo.getPromotionAmount().divide(projectPaymentRecordVo.getPayableAmount(), 2,BigDecimal.ROUND_HALF_UP).multiply(e.getPayAbleAmount()).setScale(2,BigDecimal.ROUND_HALF_UP);
                BigDecimal multiply = (projectPaymentRecordVo.getPromotionAmount().divide(projectPaymentRecordVo.getPayableAmount(),20,BigDecimal.ROUND_HALF_UP)).multiply(promotionVos.get(i).getPayAbleAmount()).setScale(2, BigDecimal.ROUND_HALF_UP);

                projectBillingInfo.setPromotionAmount(multiply);
                projectBillingInfo.setActAmount(promotionVos.get(i).getPayAbleAmount().subtract(multiply));

                if(i == promotionVos.size()-1) {
                    projectBillingInfo.setPromotionAmount(projectPaymentRecordVo.getPromotionAmount().subtract(multiply2));
                    projectBillingInfo.setActAmount(promotionVos.get(i).getPayAbleAmount().subtract(projectBillingInfo.getPromotionAmount()));
                }
                multiply2=multiply2.add(multiply);
            }else {
                projectBillingInfo.setActAmount(promotionVos.get(i).getActAmount());
                projectBillingInfo.setPromotionAmount(BigDecimal.ZERO);

            }

            // 设置应付金额等于

            //防止值为空时添加报错
//            if (e.getPayAbleAmount() != null) {
//                payAbleAmount = payAbleAmount .add (e.getPayAbleAmount());
//            }
//            if (e.getPromotionAmount() != null) {
//                promotionAmount = promotionAmount .add (e.getPromotionAmount());
//            }
//            if (e.getActAmount() != null) {
//                actAmount = actAmount .add (e.getActAmount());
//            }
//            projectBillingInfoService.updateById(projectBillingInfo);
            projectBillingInfos.add(projectBillingInfo);
        }
//        for (ProjectBillPromotionVo e : promotionVos) {
//            //防止值为空时添加报错
//            ProjectBillingInfo projectBillingInfo = new ProjectBillingInfo();
//            BeanUtils.copyProperties(e, projectBillingInfo);
//            remove(new QueryWrapper<ProjectPaymentRecord>().lambda().eq(ProjectPaymentRecord::getPayOrderNo, projectBillingInfo.getPayOrderNO()));
//            projectBillingInfo.setPayStatus(PayStatusEnum.UNPAID.code);
//            projectBillingInfo.setPaidBy(projectPaymentRecordVo.getPaidBy());
//            projectBillingInfo.setPayTime(LocalDateTime.now());
//            projectBillingInfo.setCreateTime(LocalDateTime.now());
//            projectBillingInfo.setPayOrderNO(projectPaymentRecordVo.getPayOrderNo());
//            // 算出每一项的优惠金额
//            if(ObjectUtil.isNotNull(projectPaymentRecordVo.getPromotionAmount())){
//                //
//
//                System.out.println( projectPaymentRecordVo.getPromotionAmount());
//                System.out.println(projectPaymentRecordVo.getPayableAmount());
//                System.out.println(e.getPayAbleAmount());
//                //BigDecimal multiply = projectPaymentRecordVo.getPromotionAmount().divide(projectPaymentRecordVo.getPayableAmount(), 2,BigDecimal.ROUND_HALF_UP).multiply(e.getPayAbleAmount()).setScale(2,BigDecimal.ROUND_HALF_UP);
//                BigDecimal multiply = (projectPaymentRecordVo.getPromotionAmount().divide(projectPaymentRecordVo.getPayableAmount(),20,BigDecimal.ROUND_HALF_UP)).multiply(e.getPayAbleAmount()).setScale(2, BigDecimal.ROUND_HALF_UP);
//
//                projectBillingInfo.setPromotionAmount(multiply);
//                projectBillingInfo.setActAmount(e.getPayAbleAmount().subtract(multiply));
//            }else {
//                projectBillingInfo.setActAmount(e.getActAmount());
//                projectBillingInfo.setPromotionAmount(BigDecimal.ZERO);
//
//            }
//
//            // 设置应付金额等于
//
//            //防止值为空时添加报错
////            if (e.getPayAbleAmount() != null) {
////                payAbleAmount = payAbleAmount .add (e.getPayAbleAmount());
////            }
////            if (e.getPromotionAmount() != null) {
////                promotionAmount = promotionAmount .add (e.getPromotionAmount());
////            }
////            if (e.getActAmount() != null) {
////                actAmount = actAmount .add (e.getActAmount());
////            }
////            projectBillingInfoService.updateById(projectBillingInfo);
//            projectBillingInfos.add(projectBillingInfo);
//        }
        // 更新订单
        System.out.println(JSON.toJSONString(projectBillingInfos));
        projectBillingInfoService.updateBatchById(projectBillingInfos);
        projectPaymentRecord.setActAmount(projectPaymentRecordVo.getActAmount());
        projectPaymentRecord.setPayableAmount(projectPaymentRecordVo.getPayableAmount());
        projectPaymentRecord.setPromotionAmount(projectPaymentRecordVo.getPromotionAmount());
        projectPaymentRecord.setPaidBy(projectPaymentRecordVo.getPaidBy());
        projectPaymentRecord.setOrderStatus(FeeConstant.UNPAID);
        projectPaymentRecord.setPayTime(LocalDateTime.now());
        // 生成交易记录
        save(projectPaymentRecord);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOrderStatus(Map map) {
        // 更新record表
        String  payOderNo = (String) map.get("payOrderNo");
        String  transactionId = (String) map.get("transactionId");
        String  subMchId = (String) map.get("subMchId");
        Integer projectId = Integer.valueOf((String)map.get("projectId"));
        ProjectPaymentRecord projectPaymentRecord = baseMapper.selectByPayOrderNo(payOderNo);
        if(ObjectUtil.isNotNull(projectPaymentRecord) && projectPaymentRecord.getOrderStatus().equals(FeeConstant.RECEIVED)){
            // 如果是已经支付过的订单
            return false;
        }
        String accountId = null;
        // 找微信支付账号
        ProjectPayAccountConf projectPayAccountConf = projectPayAccountConfService.getOne(new LambdaQueryWrapper<ProjectPayAccountConf>().eq(ProjectPayAccountConf::getWechatOfficialAccountMerchantNo, subMchId)
        .eq(ProjectPayAccountConf::getPayType,"2").eq(ProjectPayAccountConf::getProjectId,projectId));

        if(ObjectUtil.isNotNull(projectPayAccountConf)) {
            accountId = projectPayAccountConf.getAccountId();
        }
        boolean update = update(new LambdaUpdateWrapper<ProjectPaymentRecord>().eq(ProjectPaymentRecord::getPayOrderNo, payOderNo).set(ProjectPaymentRecord::getOrderStatus, FeeConstant.RECEIVED)
        .set(ProjectPaymentRecord::getBillingNo,transactionId).set(ProjectPaymentRecord::getAccountId,accountId).set(ProjectPaymentRecord::getPayTime,LocalDateTime.now()));

        // 更新billinfo表
        projectBillingInfoService.update(new LambdaUpdateWrapper<ProjectBillingInfo>().eq(ProjectBillingInfo::getPayOrderNO,payOderNo).set(ProjectBillingInfo::getPayStatus,PayStatusEnum.PAID.code)
        .set(ProjectBillingInfo::getPayTime,LocalDateTime.now()));
        List<ProjectBillingInfo> collect = projectBillingInfoService.list(new LambdaQueryWrapper<ProjectBillingInfo>().eq(ProjectBillingInfo::getPayOrderNO, payOderNo));
        String uuid = JSON.toJSONString(collect.stream().sorted(Comparator.comparing(ProjectBillingInfo::getBillingNo)).map(item -> item.getBillingNo()).collect(Collectors.toList()));

        redisUtilBill.del(projectPaymentRecord.getHouseId()+"_order"+"_"+uuid);

//        ProjectStaff staffByUserId = staffService.getStaffByUserId(SecurityUtils.getUser().getId(), ProjectContextHolder.getProjectId());
//        if (ObjectUtil.isNotNull(staffByUserId)){
//            //
//            staffByUserId.getStaffId()
//        }
//        redisUtilBill.hdel(projectPaymentRecord.getHouseId()+"_lock", JSONObject.toJSONString(uuid));
//        redisUtilBill.hdel(projectPaymentRecord.getHouseId()+"_order_lock",JSONObject.toJSONString(uuid));
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R savePrePayment(ProjectPaymentRecordVo projectPaymentRecordVo) {
        List<ProjectBillPromotionVo> promotionVos = projectBillingInfoService.listOnPrePromotion(projectPaymentRecordVo.getHouseId(), projectPaymentRecordVo.getMonths(), projectPaymentRecordVo.getPayBills());
        BigDecimal payAbleAmount = new BigDecimal(0);
        BigDecimal promotionAmount = new BigDecimal(0);
        BigDecimal actAmount = new BigDecimal(0);
        ProjectPaymentRecord projectPaymentRecord = new ProjectPaymentRecord();
        BeanUtils.copyProperties(projectPaymentRecordVo, projectPaymentRecord);
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        projectPaymentRecord.setPayOrderNo(uuid);
        List<ProjectBillingInfo> projectBillingInfos = new ArrayList<>();
        //更新未缴但允许预存的账单列表
        List<ProjectBillingInfo> updateBillingInfos = new ArrayList<>();
        for (ProjectBillPromotionVo e : promotionVos) {
            ProjectBillingInfo projectBillingInfo = new ProjectBillingInfo();
            BeanUtils.copyProperties(e, projectBillingInfo);
            projectBillingInfo.setPayStatus(PayStatusEnum.PAID.code);
            projectBillingInfo.setFeeName("[预存] " + e.getFeeName());
            projectBillingInfo.setPaidBy(projectPaymentRecordVo.getPaidBy());
            projectBillingInfo.setPayTime(LocalDateTime.now());
            projectBillingInfo.setPayOrderNO(uuid);
            // 设置为预存状态
            projectBillingInfo.setPrestore(DataConstants.TRUE);
            //防止值为空时添加报错
            if (e.getPayAbleAmount() != null) {
                payAbleAmount = payAbleAmount .add (e.getPayAbleAmount());
            }
            if (e.getPromotionAmount() != null) {
                promotionAmount = promotionAmount .add (e.getPromotionAmount());
            }
            if (e.getActAmount() != null) {
                actAmount = actAmount .add (e.getActAmount());
            }
            projectBillingInfos.add(projectBillingInfo);

            ProjectBillingInfo billingInfo = new ProjectBillingInfo();
            billingInfo.setPayStatus(PayStatusEnum.PREPAID.code);
            billingInfo.setPayOrderNO(uuid);
            billingInfo.setPaidBy(projectPaymentRecordVo.getPaidBy());
            billingInfo.setPayTime(LocalDateTime.now());
            billingInfo.setHouseId(e.getHouseId());
            billingInfo.setFeeId(e.getFeeId());
            billingInfo.setFeeCycleStart(e.getFeeCycleStart());
            billingInfo.setFeeCycleEnd(e.getFeeCycleEnd());
            updateBillingInfos.add(billingInfo);
        }
        //保存预存账单
        projectBillingInfoService.saveBatch(projectBillingInfos);
        // 更新已有的账单使其状态变为预存缴费并关联付费账单
        projectBillingInfoService.updateListByBillMonth(updateBillingInfos);
        //保存付款单据
        projectPaymentRecord.setActAmount(actAmount);
        projectPaymentRecord.setPayableAmount(payAbleAmount);
        projectPaymentRecord.setPromotionAmount(promotionAmount);
        projectPaymentRecord.setPaidBy(projectPaymentRecordVo.getPaidBy());
        projectPaymentRecord.setOrderStatus(FeeConstant.RECEIVED);
        projectPaymentRecord.setPayTime(LocalDateTime.now());
        save(projectPaymentRecord);
        return R.ok();
    }

    @Override
    public IPage pageAll(Page<ProjectPaymentRecord> page, ProjectPaymentRecordFormVo projectPaymentRecord) {
        return baseMapper.pageAll(page, projectPaymentRecord);
    }




    @Override
    public Double getSumFeeByDate(LocalDate beginDate, LocalDate endDate) {
        return baseMapper.getSumFeeByDate(beginDate, endDate);
    }

    @Override
    public void exportExcel(HttpServletResponse response,ProjectPaymentRecordFormVo projectPaymentRecord) {


        List<ProjectPaymentRecord> records = baseMapper.pageAll(projectPaymentRecord);
        List<PaymentRecordExcel> records2 = new ArrayList<>();
        // 如果有选择某些行就导出某些行的数据
        if(CollectionUtil.isNotEmpty(projectPaymentRecord.getSelectRow())){
            records = projectPaymentRecord.getSelectRow();
        }

        records.stream().forEach(item -> {
            PaymentRecordExcel paymentRecordExcel=new PaymentRecordExcel();
            BeanUtils.copyProperties(item, paymentRecordExcel);
            // 大于等于0 位支出
            paymentRecordExcel.setPayType(item.getActAmount().compareTo(BigDecimal.ZERO) >=0 ?"收入":"支出");
            records2.add(paymentRecordExcel);

        });
        String sheetName = "收款账号管理-收支记录";
        ExcelUtil excelUtil = new ExcelUtil();
        ExcelUtil.DefaultExportStrategy<PaymentRecordExcel> billInfoExcelDefaultExportStrategy = excelUtil.new DefaultExportStrategy<PaymentRecordExcel>(records2);
        excelUtil.exportExcel("收款账号管理-收支记录", sheetName, records2, response, billInfoExcelDefaultExportStrategy);
    }


    @Override
    public IPage<AppProjectPaymentRecordVo> selectAllApp(Page<AppProjectPaymentRecordVo> page, AppProjectPaymentRecordFromVo appProjectPaymentRecordVo) {
        return baseMapper.selectAllApp(page,appProjectPaymentRecordVo);
    }

    @Override
    public Boolean selectByPayOrderNo(String payOrderNo) {
        ProjectPaymentRecord projectPaymentRecord = baseMapper.selectByPayOrderNo(payOrderNo);
        if(ObjectUtil.isNotNull(projectPaymentRecord)){
            List<ProjectBillingInfo> list = projectBillingInfoService.list(new LambdaQueryWrapper<ProjectBillingInfo>().eq(ProjectBillingInfo::getPayOrderNO, projectPaymentRecord.getPayOrderNo()));
            List<ProjectBillingInfo> collect = list.stream().filter(item -> item.getPayStatus().equals(PayStatusEnum.PAID.code)).collect(Collectors.toList());
            if(list.size() == collect.size()){
                return true;
            }
        }
        return false;
    }

    @Override
    public R checkCode(ProjectPaymentRecordVo projectPaymentRecord) {
        // 存订单list
        String key = projectPaymentRecord.getHouseId()+"_order";
        // 存支付信息
        String qrCodeKey = projectPaymentRecord.getHouseId()+"_qrCodeMsg";
        List<PayBillVo> payBills = projectPaymentRecord.getPayBills();
        List<String> collect = payBills.stream().sorted(Comparator.comparing(PayBillVo::getId)).map(item -> item.getId()).collect(Collectors.toList());
        String uuid = JSON.toJSONString(collect);
        // 需要判断缴费项的状态
        List<ProjectBillingInfo> list = projectBillingInfoService.list(new LambdaQueryWrapper<ProjectBillingInfo>().in(ProjectBillingInfo::getBillingNo, collect));

        list=list.stream().filter(item -> item.getPayStatus().equals(PayStatusEnum.PAID.code)).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(list)){
            return R.failed("存在已支付的缴费项");
        }
        Integer qrCodeTime = 60;
        Map map = new HashMap();
        projectPaymentRecord.setProjectId(ProjectContextHolder.getProjectId());
        if(ObjectUtil.isNotNull(redisUtilBill.get(key+"_"+uuid))){
            map = (Map) redisUtilBill.get(key+"_"+uuid);
            List <String> collect1=(List<String>)map.get("collect");
            String payType= (String)map.get("payType");
            String outTradeNo= (String)map.get("outTradeNo");
            if(!payType.equals(projectPaymentRecord.getPayType())){
                return R.failed("已存在其他支付方式的订单");
            }
            // 判断是否为相同的缴费项目
            if(collect.containsAll(collect1) && collect1.containsAll(collect) ){
                // 如果存在相同缴费项
                redisUtilBill.del(key+"_"+uuid);

                if(ObjectUtil.isNotNull(outTradeNo)) {
                    // 去关闭之前的订单
                    projectPaymentRecord.setPayOrderNo(outTradeNo);
                    R data = remotePayingService.closeOrder(projectPaymentRecord);
                }


            }else {

            }
        }
        Set pattenKey = redisUtilBill.getPattenKey(key+"_*");
        List<Map> values = redisUtilBill.mutiGet(pattenKey);
        List newArray=new ArrayList();
        if (CollectionUtil.isNotEmpty(values)) {
            for (int i = 0; i <values.size() ; i++) {
                newArray.addAll( (List)values.get(i).get("collect"));
            }

            if (CollectionUtil.containsAny(newArray,collect)) {
                return R.failed("订单包含正在支付的缴费项,请重新选择");
            }
        }

        return R.ok(uuid);
    }
}