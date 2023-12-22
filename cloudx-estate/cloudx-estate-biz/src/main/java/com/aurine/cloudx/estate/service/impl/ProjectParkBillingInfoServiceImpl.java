/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */
package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.BillingOrderTypeEnum;
import com.aurine.cloudx.estate.constant.enums.PayTypeEnum;
import com.aurine.cloudx.estate.entity.ProjectParkBillingInfo;
import com.aurine.cloudx.estate.entity.ProjectParkEntranceHis;
import com.aurine.cloudx.estate.mapper.ProjectParkBillingInfoMapper;
import com.aurine.cloudx.estate.service.ProjectParkBillingInfoService;
import com.aurine.cloudx.estate.service.ProjectParkEntranceHisService;
import com.aurine.cloudx.estate.vo.ProjectParkBillingInfoVo;
import com.aurine.cloudx.estate.vo.ProjectParkBillingSourceSearchTotalConditionVo;
import com.aurine.cloudx.estate.vo.ProjectParkBillingTotalSearchConditionVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 缴费记录
 *
 * @author 黄阳光
 * @date 2020-07-10 09:49:12
 */
@Service
public class ProjectParkBillingInfoServiceImpl extends ServiceImpl<ProjectParkBillingInfoMapper, ProjectParkBillingInfo> implements ProjectParkBillingInfoService {

    private static final String DEFAULT_DATE_TIME_FORMAT = "yyMMdd";

    @Resource
    ProjectParkBillingInfoMapper projectParkBillingInfoMapper;
    @Resource
    ProjectParkEntranceHisService projectParkEntranceHisService;


    @Override
    public IPage<ProjectParkBillingInfoVo> page(Page page, ProjectParkBillingInfoVo vo) {
        vo.setProjectId(ProjectContextHolder.getProjectId());
        vo.setTenantId(TenantContextHolder.getTenantId());
        return projectParkBillingInfoMapper.selectPage(page, vo, ProjectContextHolder.getProjectId());
    }

    /*@Override
    public boolean save(BillingOrderTypeEnum orderType, PayTypeEnum payType, ProjectParkBillingInfo po) {
        po.setOrderType(orderType.code);
        po.setPayType(payType.code);
        return this.save(po);
    }*/

    @Override
    public boolean tempPay(PayTypeEnum payType, ProjectParkBillingInfo po) {
        //临时车缴费记录添加
        po.setOrderType(BillingOrderTypeEnum.PAY.code);
        po.setPayType(payType.code);
        po.setProjectId(ProjectContextHolder.getProjectId());
        po.setTenantId(TenantContextHolder.getTenantId());
        return this.save(po);
    }

    @Override
    public boolean monthlyPay(String parkId, String plateNumber, BigDecimal payment) {
        //月租车缴费记录添加
        ProjectParkBillingInfo po = new ProjectParkBillingInfo();
        po.setParkId(parkId);
        po.setPlateNumber(plateNumber);
        po.setPayment(payment);
        po.setReceivable(payment);
        po.setOrderType(BillingOrderTypeEnum.RECHARGE.code);
        po.setPayType(PayTypeEnum.CASH.code);
        LocalDateTime time = LocalDateTime.now();
        po.setPayTime(time);

        po.setProjectId(ProjectContextHolder.getProjectId());
        po.setTenantId(TenantContextHolder.getTenantId());

        DateTimeFormatter format = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);
        String payTime = time.format(format);
        Timestamp timestamp = Timestamp.valueOf(time);
        String parkCode = projectParkBillingInfoMapper.selectByParkCode(parkId, ProjectContextHolder.getProjectId());
        String payOrderCode = "MC" + payTime + timestamp.getTime() + parkCode;
        po.setPayOrderCode(payOrderCode);
        return this.save(po);
    }

    /**
     * 增加临时车缴费记录
     *
     * @param payType
     * @param orderNo
     * @param receivable
     * @param payment
     * @param payTime
     * @return
     * @author: 王伟
     * @since 2020-07-29
     */
    @Override
    public boolean addTempCarBill(PayTypeEnum payType, String parkingId, String orderNo, BigDecimal receivable, BigDecimal payment, LocalDateTime payTime, String payOrderNno) {
        ProjectParkBillingInfo po = new ProjectParkBillingInfo();

        ProjectParkEntranceHis parkEntranceHis = projectParkEntranceHisService.getByParkOrderCode(parkingId, orderNo);

        BeanUtils.copyProperties(parkEntranceHis, po);

        po.setOrderType(BillingOrderTypeEnum.PAY.code);
        po.setPayType(payType.code);
        po.setPayOrderCode(payOrderNno);
        po.setPayTime(payTime);
        po.setReceivable(receivable);
        po.setPayment(payment);
        boolean result = this.save(po);
        System.out.println(result);
        return result;
    }

    /**
     * 获取当月的项目总缴费金额
     *
     * @return
     */
    @Override
    public BigDecimal currMonthPay() {
        BigDecimal count = this.baseMapper.currMonthPayment(TenantContextHolder.getTenantId(), ProjectContextHolder.getProjectId());
        return count == null ? new BigDecimal(0) : count;
    }

    /**
     * 获取指定月的项目总缴费金额
     *
     * @param date
     * @return
     */
    @Override
    public BigDecimal monthPayment(LocalDateTime date) {
        BigDecimal count = this.baseMapper.monthPayment(date, TenantContextHolder.getTenantId(), ProjectContextHolder.getProjectId());
        return count == null ? new BigDecimal(0) : count;
    }

    /**
     * 获取指定月天的项目总缴费金额
     *
     * @param date
     * @return
     */
    @Override
    public BigDecimal dayPayment(LocalDateTime date) {
        BigDecimal count = this.baseMapper.dayPayment(date, TenantContextHolder.getTenantId(), ProjectContextHolder.getProjectId());
        return count == null ? new BigDecimal(0) : count;
    }

    @Override
    public ProjectParkBillingTotalSearchConditionVo getIncome(Page page, ProjectParkBillingTotalSearchConditionVo vo) {
        //获取临时车缴费与月租车充值金额

        vo.setProjectId(ProjectContextHolder.getProjectId());
        vo.setTenantId(TenantContextHolder.getTenantId());

        ProjectParkBillingTotalSearchConditionVo income = projectParkBillingInfoMapper.selectIncome(vo);
        if (income == null) {
            //返回为空则代表无数据，新new一个对象防止null异常
            income = new ProjectParkBillingTotalSearchConditionVo();
        }
        income.setTotalMonthly(income.getTotalMonthly() != null ? income.getTotalMonthly() : new BigDecimal(0));
        income.setTotalTemp(income.getTotalTemp() != null ? income.getTotalTemp() : new BigDecimal(0));
        income.setTotalRevenue(income.getTotalMonthly().add(income.getTotalTemp()));
        //获取并保存每日的月租车与临时车缴费数据
        income.setDailyPayment(projectParkBillingInfoMapper.selectAllBilling(page, vo));
        return income;
    }

    @Override
    public ProjectParkBillingSourceSearchTotalConditionVo getSourceIncome(Page page, ProjectParkBillingSourceSearchTotalConditionVo vo) {
        vo.setProjectId(ProjectContextHolder.getProjectId());
        vo.setTenantId(TenantContextHolder.getTenantId());
        ProjectParkBillingSourceSearchTotalConditionVo income = projectParkBillingInfoMapper.selectSourceIncome(vo);
        if (income == null) {
            //返回为空则代表无数据，新new一个对象防止null异常
            income = new ProjectParkBillingSourceSearchTotalConditionVo();
        }
        income.setCashTotal(income.getCashTotal() != null ? income.getCashTotal() : new BigDecimal(0));
        income.setOfflineWeChatTotal(income.getOfflineWeChatTotal() != null ? income.getOfflineWeChatTotal() : new BigDecimal(0));
        income.setOfflineAliPayTotal(income.getOfflineAliPayTotal() != null ? income.getOfflineAliPayTotal() : new BigDecimal(0));
        income.setWeChatTotal(income.getWeChatTotal() != null ? income.getWeChatTotal() : new BigDecimal(0));
        income.setAliPayTotal(income.getAliPayTotal() != null ? income.getAliPayTotal() : new BigDecimal(0));
        income.setOtherTotal(income.getOtherTotal() != null ? income.getOtherTotal() : new BigDecimal(0));

        income.setDailyIncome(projectParkBillingInfoMapper.selectBySource(page, vo));
        return income;
    }
}
