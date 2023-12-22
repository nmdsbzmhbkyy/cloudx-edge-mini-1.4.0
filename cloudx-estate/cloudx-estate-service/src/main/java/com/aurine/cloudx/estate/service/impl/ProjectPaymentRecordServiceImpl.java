package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.common.core.constant.DataConstants;
import com.aurine.cloudx.estate.constant.FeeConstant;
import com.aurine.cloudx.estate.constant.enums.PayStatusEnum;
import com.aurine.cloudx.estate.entity.ProjectBillingInfo;
import com.aurine.cloudx.estate.entity.ProjectPaymentRecord;
import com.aurine.cloudx.estate.mapper.ProjectPaymentRecordMapper;
import com.aurine.cloudx.estate.service.ProjectBillingInfoService;
import com.aurine.cloudx.estate.service.ProjectPaymentRecordService;
import com.aurine.cloudx.estate.service.ProjectPromotionConfService;
import com.aurine.cloudx.estate.service.ProjectPromotionFeeRelService;
import com.aurine.cloudx.estate.vo.ProjectBillPromotionVo;
import com.aurine.cloudx.estate.vo.ProjectPaymentRecordFormVo;
import com.aurine.cloudx.estate.vo.ProjectPaymentRecordVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
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
        for (ProjectBillPromotionVo e : promotionVos) {
            ProjectBillingInfo projectBillingInfo = new ProjectBillingInfo();
            BeanUtils.copyProperties(e, projectBillingInfo);
            projectBillingInfo.setPayStatus(PayStatusEnum.PAID.code);
            projectBillingInfo.setPaidBy(projectPaymentRecordVo.getPaidBy());
            projectBillingInfo.setPayTime(LocalDateTime.now());
            projectBillingInfo.setPayOrderNO(uuid);
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
        }
        projectBillingInfoService.updateBatchById(projectBillingInfos);
        projectPaymentRecord.setActAmount(actAmount);
        projectPaymentRecord.setPayableAmount(payAbleAmount);
        projectPaymentRecord.setPromotionAmount(promotionAmount);
        projectPaymentRecord.setPaidBy(projectPaymentRecordVo.getPaidBy());
        projectPaymentRecord.setOrderStatus(FeeConstant.RECEIVED);
        projectPaymentRecord.setPayTime(LocalDateTime.now());
        save(projectPaymentRecord);
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
}