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

package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.constant.enums.BillingOrderTypeEnum;
import com.aurine.cloudx.estate.constant.enums.PayTypeEnum;
import com.aurine.cloudx.estate.entity.ProjectParkEntranceHis;
import com.aurine.cloudx.estate.vo.ProjectParkBillingInfoVo;
import com.aurine.cloudx.estate.vo.ProjectParkBillingSourceSearchTotalConditionVo;
import com.aurine.cloudx.estate.vo.ProjectParkBillingTotalSearchConditionVo;
import com.aurine.cloudx.estate.vo.ProjectParkEntranceHisVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aurine.cloudx.estate.entity.ProjectParkBillingInfo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 缴费记录
 *
 * @author 黄阳光
 * @date 2020-07-10 09:49:12
 */
public interface ProjectParkBillingInfoService extends IService<ProjectParkBillingInfo> {

//    boolean save(BillingOrderTypeEnum orderType, PayTypeEnum payTypeEnum, ProjectParkBillingInfo po);

    IPage<ProjectParkBillingInfoVo> page(Page page, ProjectParkBillingInfoVo vo);

    ProjectParkBillingTotalSearchConditionVo getIncome(Page page, ProjectParkBillingTotalSearchConditionVo vo);

    boolean tempPay(PayTypeEnum payType, ProjectParkBillingInfo po);

    boolean monthlyPay(String parkId, String plateNumber, BigDecimal payment);

    ProjectParkBillingSourceSearchTotalConditionVo getSourceIncome(Page page, ProjectParkBillingSourceSearchTotalConditionVo vo);

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
    boolean addTempCarBill(PayTypeEnum payType, String parkingId, String orderNo, BigDecimal receivable, BigDecimal payment, LocalDateTime payTime,String payOrderNno);

    /**
     * 获取当月的项目总缴费金额
     * @return
     */
    BigDecimal currMonthPay();    /**
     * 获取指定月的项目总缴费金额
     * @return
     */
    BigDecimal monthPayment(LocalDateTime date);

    /**
     * 获取指定月天的项目总缴费金额
     *
     * @return
     */
    BigDecimal dayPayment(LocalDateTime date);



}
