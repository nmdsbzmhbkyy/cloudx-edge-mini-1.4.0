package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.common.core.constant.DataConstants;
import com.aurine.cloudx.estate.constant.FeeConstant;
import com.aurine.cloudx.estate.constant.enums.FeeCycleTypeEnum;
import com.aurine.cloudx.estate.constant.enums.FeeTypeEnum;
import com.aurine.cloudx.estate.constant.enums.PayStatusEnum;
import com.aurine.cloudx.estate.entity.ProjectBillingInfo;
import com.aurine.cloudx.estate.entity.ProjectFeeConf;
import com.aurine.cloudx.estate.entity.ProjectHousePersonRel;
import com.aurine.cloudx.estate.entity.ProjectPromotionConf;
import com.aurine.cloudx.estate.mapper.ProjectBillingInfoMapper;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.api.client.util.Lists;
import com.pig4cloud.pigx.common.core.util.R;
import org.joda.time.DateTime;
import org.joda.time.Months;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 项目已出账的账单信息(ProjectBillingInfo)表服务实现类
 *
 * @author makejava
 * @since 2020-07-20 16:43:48
 */
@Service
public class ProjectBillingInfoServiceImpl extends ServiceImpl<ProjectBillingInfoMapper, ProjectBillingInfo> implements ProjectBillingInfoService {


    /**
     * 单次查询长度
     */
    private static final Integer PAGE_SIZE = 500;

    /**
     * 批量插入数据列表
     */
    private List<ProjectBillingInfo> projectBillingInfoList = new ArrayList<>();

    @Resource
    private ProjectPromotionConfService projectPromotionConfService;


    @Resource
    private ProjectHouseFeeItemService projectHouseFeeItemService;


    @Resource
    ProjectFeeConfService projectFeeConfService;

    @Resource
    ProjectHousePersonRelService projectHousePersonRelService;


    @Override
    public IPage<ProjectBillingInfoVo> pageBill(Page<ProjectBillingInfoVo> page, ProjectBillingInfoFormVo projectBillingInfoFormVo) {
        return baseMapper.pageBill(page, projectBillingInfoFormVo);
    }

    @Override
    public IPage<ProjectHouseFeeTotalVo> pageBillTotal(Page<ProjectHouseFeeTotalVo> page, ProjectHouseFeeTotalFormVo projectHouseFeeTotalFormVo) {

        return baseMapper.pageBillTotal(page, projectHouseFeeTotalFormVo);
    }

    @Override
    public List<ProjectBillingInfoVo> listVo(String houseId, List<String> billIds, List<String> payStatusList) {
        return baseMapper.listVo(houseId, billIds, payStatusList);
    }

    @Override
    public List<ProjectBillPromotionVo> listOnPromotion(String id) {
        return listOnPromotion(id, null, Arrays.asList(PayStatusEnum.UNPAID.code));
    }

    @Override
    public List<ProjectBillPromotionVo> listOnPromotion(String id, List<String> billIds, List<String> payStatusList) {
        List<String> types = Lists.newArrayList();
        types.add(FeeConstant.NORMAL_DISCOUNTS);
        List<ProjectPromotionConfOnFeeIdVo> projectPromotionConfOnFeeIdVos = projectPromotionConfService.listConfByType(types);
        List<ProjectBillingInfoVo> projectBillingInfos = listVo(id, billIds, payStatusList);
        List<ProjectBillPromotionVo> projectBillPromotionVos = new ArrayList<>();
        projectBillingInfos.forEach(e -> {
            ProjectBillPromotionVo projectBillPromotionVo = new ProjectBillPromotionVo();
            BeanUtils.copyProperties(e, projectBillPromotionVo);
            //查询是否有普通优惠
            for (ProjectPromotionConfOnFeeIdVo info : projectPromotionConfOnFeeIdVos) {
                if (info.getFeeId().equals(e.getFeeId())) {
                    //判断是否允许上月未缴优惠
                    if (FeeConstant.HAVE_NOT_PAY.equals(info.getClaim())) {
                        //判断是否上月之前存在未缴费
                        if (!DataConstants.TRUE.equals(e.getLastHave())) {
                            //优惠金额=应付金额-应付金额*(折扣)/10
                            BigDecimal promotionAmount = e.getPayAbleAmount().subtract(e.getPayAbleAmount().multiply(info.getDiscount().divide(BigDecimal.TEN)));
                            projectBillPromotionVo.setPromotionAmount(promotionAmount);
                        }
                        //判断是否不限优惠
                    } else if (FeeConstant.UNLIMITED.equals(info.getClaim())) {
                        BigDecimal promotionAmount = e.getPayAbleAmount().subtract(e.getPayAbleAmount().multiply(info.getDiscount()).divide(BigDecimal.TEN));
                        projectBillPromotionVo.setPromotionAmount(promotionAmount);
                    }
                }
            }
            //判断是否存在优惠,存在优惠则实付金额=应付金额-优惠金额
            if (projectBillPromotionVo.getPromotionAmount() != null) {
                BigDecimal actAmount = e.getPayAbleAmount().subtract(projectBillPromotionVo.getPromotionAmount());
                projectBillPromotionVo.setActAmount(actAmount);
            } else {
                projectBillPromotionVo.setActAmount(e.getPayAbleAmount());
            }
            projectBillPromotionVos.add(projectBillPromotionVo);
        });
        return projectBillPromotionVos;
    }

    @Override
    public List<ProjectBillPromotionVo> listOnPrePromotion(String id, Integer type, List<PayBillVo> payBillVos) {
        List<String> types = Lists.newArrayList();
        // 设置查询的优惠方案为普通优惠和预存优惠
        types.add(FeeConstant.NORMAL_DISCOUNTS);
        types.add(FeeConstant.RESELLERS_DISCOUNTS);
        //获取优惠方案
        List<ProjectPromotionConfOnFeeIdVo> projectPromotionConfOnFeeIdVos = projectPromotionConfService.listConfByType(types);
        List<String> feeIds = null;
        if (payBillVos != null) {
            //获取费用id列表
            feeIds = payBillVos.stream().map(PayBillVo::getId).collect(Collectors.toList());
        }
        List<ProjectBillingInfoVo> projectBillingInfos = listPreVo(id, type, feeIds);
        List<ProjectBillPromotionVo> projectBillPromotionVos = new ArrayList<>();
        // 遍历预存账单列表 并设置优惠内容
        projectBillingInfos.forEach(e -> {
            ProjectBillPromotionVo projectBillPromotionVo = new ProjectBillPromotionVo();
            BeanUtils.copyProperties(e, projectBillPromotionVo);
            List<ProjectPromotionConf> prePromotionList = new ArrayList<>();
            //查询是否有优惠
            for (ProjectPromotionConfOnFeeIdVo info : projectPromotionConfOnFeeIdVos) {
                if (info.getFeeId() != null && info.getFeeId().equals(e.getFeeId())) {
                    //当优惠方案为普通优惠时候
                    if (FeeConstant.NORMAL_DISCOUNTS.equals(info.getPromotionType())) {

                        //判断是否允许上月未缴优惠
                        if (FeeConstant.HAVE_NOT_PAY.equals(info.getClaim())) {
                            //判断是否上月之前存在未缴费
                            if (DataConstants.FALSE.equals(e.getLastHave())) {
                                BigDecimal promotionAmount;
                                //判断优惠金额是否已经存在,存在则表示已经被普通优惠或者预存优惠计算过 故应该使用打折后的值计算
                                if (projectBillPromotionVo.getPromotionAmount() != null) {
                                    //优惠金额=已优惠金额-已优惠金额*折扣/10
                                    promotionAmount = (e.getPayAbleAmount().subtract(projectBillPromotionVo.getPromotionAmount()).subtract(e.getPayAbleAmount().subtract(projectBillPromotionVo.getPromotionAmount()).multiply(info.getDiscount()).divide(BigDecimal.TEN)));
                                } else {
                                    //优惠金额=应付金额-应付金额*折扣/10
                                    promotionAmount = (e.getPayAbleAmount().subtract(e.getPayAbleAmount().multiply(info.getDiscount().divide(BigDecimal.TEN))));
                                }
                                projectBillPromotionVo.setPromotionAmount(promotionAmount);
                            }
                            //判断是否不限优惠
                        } else if (FeeConstant.UNLIMITED.equals(info.getClaim())) {
                            BigDecimal promotionAmount;
                            if (projectBillPromotionVo.getPromotionAmount() != null) {
                                promotionAmount = projectBillPromotionVo.getPromotionAmount().multiply(info.getDiscount());
                            } else {
                                promotionAmount = e.getPayAbleAmount().multiply(info.getDiscount());
                            }
                            projectBillPromotionVo.setPromotionAmount(promotionAmount);

                        } else if (FeeConstant.HAVE_PAY.equals(info.getClaim())) {
                            // 判断是否不存在未缴金额
                            if (DataConstants.FALSE.equals(e.getAllHave())) {
                                BigDecimal promotionAmount;
                                //判断优惠金额是否已经存在,存在则表示已经被普通优惠或者预存优惠计算过 故应该使用打折后的值计算
                                if (projectBillPromotionVo.getPromotionAmount() != null) {
                                    //优惠金额=已优惠金额(1-折扣/10)+已优惠
                                    promotionAmount =projectBillPromotionVo.getPromotionAmount().add( e.getPayAbleAmount().subtract(projectBillPromotionVo.getPromotionAmount()).multiply((BigDecimal.ONE.subtract(info.getDiscount().divide(BigDecimal.TEN)))));
                                } else {
                                    //优惠金额=应付金额-应付金额*折扣/10
                                    promotionAmount = (e.getPayAbleAmount().subtract((e.getPayAbleAmount().multiply(info.getDiscount().divide(BigDecimal.TEN)))));
                                }
                                projectBillPromotionVo.setPromotionAmount(promotionAmount);
                            }
                        }
                    }
                    // 当优惠类型为预存优惠时候
                    if (FeeConstant.RESELLERS_DISCOUNTS.equals(info.getPromotionType())) {
                        //判断预存月是否可以被优惠预存月整除，不能整除不加入预存减免

                        if (type % info.getPreStoreMonth() != 0) {
                            continue;
                        }
                        //如果payBillVos 存在值表示已经选中预存类型，生成预存费用
                        if (payBillVos != null) {
                            for (PayBillVo payBillVo : payBillVos) {
                                if (e.getFeeId() != null && !"".equals(e.getFeeId()) && e.getFeeId().equals(payBillVo.getId())) {
                                    if (info.getPromotionId().equals(payBillVo.getTypeId())) {
                                        if (FeeConstant.HAVE_NOT_PAY.equals(info.getClaim())) {
                                            //判断是否上月之前存在未缴费
                                            if (DataConstants.FALSE.equals(e.getLastHave())) {
                                                setPromotionAmount(e, projectBillPromotionVo, info);
                                            }
                                            //判断是否不限优惠
                                        } else if (FeeConstant.UNLIMITED.equals(info.getClaim())) {
                                            setPromotionAmount(e, projectBillPromotionVo, info);
                                            //判断是否没有欠款账单
                                        } else if (FeeConstant.HAVE_PAY.equals(info.getClaim())) {
                                            if (DataConstants.TRUE.equals(e.getAllHave())) {
                                                setPromotionAmount(e, projectBillPromotionVo, info);
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            if (FeeConstant.HAVE_NOT_PAY.equals(info.getClaim())) {
                                //判断是否上月之前存在未缴费
                                if (DataConstants.FALSE.equals(e.getLastHave())) {
                                    prePromotionList.add(info);
                                }
                                //判断是否不限优惠
                            } else if (FeeConstant.UNLIMITED.equals(info.getClaim())) {
                                prePromotionList.add(info);
                            } else if (FeeConstant.HAVE_PAY.equals(info.getClaim())) {
                                //判断是否没有欠款账单
                                if (DataConstants.TRUE.equals(e.getAllHave())) {
                                    prePromotionList.add(info);
                                }
                            }
                        }
                    }
                }
            }

            //判断是否存在优惠,存在优惠则实付金额=应付金额-优惠金额
            if (projectBillPromotionVo.getPromotionAmount() != null) {
                BigDecimal actAmount = e.getPayAbleAmount().subtract(projectBillPromotionVo.getPromotionAmount());
                projectBillPromotionVo.setActAmount(actAmount);
            } else {
                // 如果没有优惠 则实付金额为应付金额
                projectBillPromotionVo.setActAmount(e.getPayAbleAmount());
                // 如果优惠为空 默认设置优惠值为0
                projectBillPromotionVo.setPromotionAmount(BigDecimal.ZERO);
            }
            //添加预存费用方案
            projectBillPromotionVo.setPrePromotionList(prePromotionList);
            projectBillPromotionVos.add(projectBillPromotionVo);
        });
        return projectBillPromotionVos;
    }

    /**
     * 设置预存优惠
     *
     * @param e
     * @param projectBillPromotionVo
     * @param info
     */
    private void setPromotionAmount(ProjectBillingInfoVo e, ProjectBillPromotionVo projectBillPromotionVo, ProjectPromotionConfOnFeeIdVo info) {
        BigDecimal promotionAmount;
        if (projectBillPromotionVo.getPromotionAmount() != null) {
            //判断是否为减免月
            if (FeeConstant.DISCOUNT_ON_MONTH.equals(info.getDiscountType())) {
                // 减免月的优惠金额计算为 优惠金额=已优惠*(减免月/预存月份)+已优惠 保留2位小数
                promotionAmount =projectBillPromotionVo.getPromotionAmount().add (e.getPayAbleAmount().subtract(projectBillPromotionVo.getPromotionAmount()).multiply(new BigDecimal(info.getReduceMonth().doubleValue() / (info.getPreStoreMonth()))));
            } else {
                // 折扣优惠为 优惠金额= 已金额(1- 折扣/10)+已优惠
                promotionAmount =projectBillPromotionVo.getPromotionAmount().add (e.getPayAbleAmount().subtract(projectBillPromotionVo.getPromotionAmount()).multiply(BigDecimal.ONE.subtract(info.getDiscount().divide(BigDecimal.TEN))));
            }
        } else {
            if (FeeConstant.DISCOUNT_ON_MONTH.equals(info.getDiscountType())) {
                // 减免月的优惠金额计算为 优惠金额=应收金额*(减免月/预存月份) 保留2位小数
                promotionAmount = e.getPayAbleAmount().multiply(new BigDecimal((info.getReduceMonth()).doubleValue() / (info.getPreStoreMonth())));
            } else {
                // 折扣优惠为 优惠金额= 应收金额-应收金额*折扣/10
                promotionAmount = (e.getPayAbleAmount().subtract((e.getPayAbleAmount().multiply(info.getDiscount().divide(BigDecimal.TEN)))));
            }

        }
        projectBillPromotionVo.setPromotionAmount(promotionAmount);
    }

    @Override
    public List<ProjectBillPromotionVo> listOnPrePromotion(String id, Integer type) {
        return listOnPrePromotion(id, type, null);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R resentBillingInfo() {

        Page<ProjectHouseFeeItemConfVo> myPage = new Page<>();
        myPage.setSize(PAGE_SIZE);
        myPage.setCurrent(0L);
        do {
            myPage.setCurrent(myPage.getCurrent() + 1);
            myPage = projectHouseFeeItemService.selectHouseFeeItemConf(myPage);
            List<ProjectHouseFeeItemConfVo> projectHouseFeeItems = myPage.getRecords();
            //判断数据是否为空 为空则直接退出循环
            if (projectHouseFeeItems == null || projectHouseFeeItems.size() == 0) {
                break;
            }
            for (ProjectHouseFeeItemConfVo e : projectHouseFeeItems) {
                if (e.getLastMonth() == null || "".equals(e.getLastMonth())) {
                    continue;
                }
                // 判断是否为以住房同日
                String dateString = "01";
                if (FeeConstant.CHECK_IN_DATE.equals(e.getFeeStartDayType())) {
                    if (e.getCheckInTime() != null) {
                        dateString = e.getCheckInTime().format(DateTimeFormatter.ofPattern("dd"));
                    }
                } else {
                    if (e.getFeeStartDay() != null && !"".equals(e.getFeeStartDay())) {
                        //防止传入字符为"1"的字符串造成时间格式转换报错
                        Integer thisDate = Integer.valueOf(e.getFeeStartDay());
                        if (thisDate < 10) {
                            dateString = "0" + thisDate;
                        } else {
                            dateString = "" + thisDate;
                        }

                    }
                }

                Integer feeCycleNum = FeeCycleTypeEnum.getValueByCode(e.getFeeCycleType());
                LocalDate lastDate = LocalDate.parse(e.getLastMonth() + dateString, DateTimeFormatter.ofPattern("yyyyMMdd"));
                //计算当前时间与最后一次账单时间的插值
                int months = Months.monthsBetween(new DateTime().withDate(lastDate.getYear(), lastDate.getMonthValue(), lastDate.getDayOfMonth()), DateTime.now()).getMonths();
                if (months > 0 && months >= feeCycleNum) {
                    // 循环周期 用于填补 漏掉的账单
                    int size = months / feeCycleNum;
                    for (int i = 1; i <= size; i++) {
                        ProjectBillingInfo projectBillingInfo = new ProjectBillingInfo();
                        projectBillingInfo.setFeeId(e.getFeeId());
                        projectBillingInfo.setHouseId(e.getHouseId());

                        projectBillingInfo.setFeeName(e.getFeeName());

                        // 判断是否提前一周期进行收费 如是 周期数再加1
                        if (FeeConstant.CYCLE_AHEAD.equals(e.getBillCycleType())) {
                            projectBillingInfo.setFeeCycleEnd(lastDate.plusMonths((i + 2) * feeCycleNum).minusDays(1));
                            projectBillingInfo.setFeeCycleStart(lastDate.plusMonths((i + 1) * feeCycleNum));
                        } else {
                            projectBillingInfo.setFeeCycleEnd(lastDate.plusMonths((i + 1) * feeCycleNum).minusDays(1));
                            projectBillingInfo.setFeeCycleStart(lastDate.plusMonths(i * feeCycleNum));
                        }
                        // 判断该时间内是否预付
                        if (e.getLastPreDate() != null && e.getLastPreDate().isAfter(projectBillingInfo.getFeeCycleEnd())) {
                            // 预付过则 实付费用为0 状态为 预存 计费标准为预付时候的计费标准
                            projectBillingInfo.setPayOrderNO(e.getPayOrderNo());
                            projectBillingInfo.setPaidBy(e.getPaidBy());
                            projectBillingInfo.setPromotionAmount(BigDecimal.ZERO);
                            projectBillingInfo.setActAmount(BigDecimal.ZERO);
                            projectBillingInfo.setPayTime(e.getPayTime());
                            // 预存账单费用为预存时候的费用
                            projectBillingInfo.setUnitPrice(e.getPreUnitPrice());
                            projectBillingInfo.setFixAmountOrNot(e.getPreFixAmountOrNot());
                            projectBillingInfo.setFixAmount(e.getPreFixAmount());
                            projectBillingInfo.setUnit(e.getPreUnit());
                            projectBillingInfo.setHouseArea(e.getPreHouseArea());
                            //判断是否定额收费 应付金额为预存时候应付单价计算
                            if (DataConstants.TRUE.equals(e.getPreFixAmountOrNot())) {
                                //   定额收费则设置费用为定额的值
                                projectBillingInfo.setPayAbleAmount(e.getPreFixAmount());
                            } else {
                                // 如果不是 应付金额=单价*房屋面积*频次=单价*房屋面积*(周期/计算周期)
                                Integer unitNum = FeeCycleTypeEnum.getValueByCode(e.getPreUnit());
                                projectBillingInfo.setPayAbleAmount(e.getPreUnitPrice().multiply(e.getPreHouseArea()).multiply(new BigDecimal(feeCycleNum.doubleValue() / unitNum)));

                            }
                            projectBillingInfo.setPayStatus(PayStatusEnum.PREPAID.code);
                        } else {
                            // 非预存费用为费用设置的费用
                            projectBillingInfo.setUnitPrice(e.getUnitPrice());
                            projectBillingInfo.setFixAmountOrNot(e.getFixAmountOrNot());
                            projectBillingInfo.setFixAmount(e.getFixAmount());
                            projectBillingInfo.setUnit(e.getUnit());
                            projectBillingInfo.setHouseArea(e.getHouseArea());
                            //判断是否定额收费
                            if (DataConstants.TRUE.equals(e.getFixAmountOrNot())) {
                                //   定额收费则设置费用为定额的值
                                projectBillingInfo.setPayAbleAmount(e.getFixAmount());
                            } else {
                                // 如果不是 应付金额=单价*房屋面积*频次=单价*房屋面积*(周期/计算周期)
                                Integer unitNum = FeeCycleTypeEnum.getValueByCode(e.getUnit());
                                projectBillingInfo.setPayAbleAmount(e.getUnitPrice().multiply(e.getHouseArea()).multiply(BigDecimal.valueOf(feeCycleNum.doubleValue() / unitNum)));

                            }
                            projectBillingInfo.setPromotionAmount(BigDecimal.ZERO);
                            projectBillingInfo.setActAmount(BigDecimal.ZERO);
                            projectBillingInfo.setPayStatus(PayStatusEnum.UNPAID.code);
                        }

                        projectBillingInfo.setBillMonth(lastDate.plusMonths(i * feeCycleNum).format(DateTimeFormatter.ofPattern("yyyyMM")));
                        // 状态不为预付
                        projectBillingInfo.setPrestore(DataConstants.FALSE);
                        addProjectBillingInfoList(projectBillingInfo);
                    }
                }

            }
        } while (myPage.getTotal() / myPage.getSize() <= myPage.getCurrent());
        addProjectBillingInfoListFinish();
        return R.ok();
    }

    /**
     * 分批插入projectBillingInfoList数据到数据库
     *
     * @param projectBillingInfo
     */
    private void addProjectBillingInfoList(ProjectBillingInfo projectBillingInfo) {
        projectBillingInfoList.add(projectBillingInfo);
        if (projectBillingInfoList.size() >= PAGE_SIZE) {
            saveBatch(projectBillingInfoList);
            projectBillingInfoList.clear();
        }
    }

    /**
     * 查询结束后插入projectBillingInfoList剩余数据到数据库
     */
    private void addProjectBillingInfoListFinish() {
        if (projectBillingInfoList.size() >= 0) {
            saveBatch(projectBillingInfoList);
            projectBillingInfoList.clear();
        }
    }

    /**
     * 获取根据房屋id和费用id及时长获取预存费用列表
     *
     * @param houseId 房屋id
     * @param type    时长
     * @param billIds 费用id列表
     * @return 费用账单
     */
    private List<ProjectBillingInfoVo> listPreVo(String houseId, Integer type, List<String> billIds) {
        List<ProjectHouseFeeItemConfVo> projectHouseFeeItemConfVos = projectHouseFeeItemService
                .listHouseFeeItemConf(houseId, billIds, Arrays.asList(FeeConstant.FIXED_CHARGE), Arrays.asList(FeeTypeEnum.ADMINISTRATIVE_FEE.code
                        , FeeTypeEnum.MAINTENANCE_COST.code, FeeTypeEnum.WASTE_DISPOSAL_FEE.code, FeeTypeEnum.OTHER.code));
        List<ProjectBillingInfoVo> projectBillingInfos = new ArrayList<>();
        projectHouseFeeItemConfVos.forEach(e -> {
            // 判断是否为以住房同日
            String dateString = "01";
            if (FeeConstant.CHECK_IN_DATE.equals(e.getFeeStartDayType())) {
                if (e.getCheckInTime() != null) {
                    dateString = e.getCheckInTime().format(DateTimeFormatter.ofPattern("dd"));
                }
            } else {
                if (e.getFeeStartDay() != null && !"".equals(e.getFeeStartDay())) {
                    //防止传入字符为"1"的字符串造成时间格式转换报错
                    Integer thisDate = Integer.valueOf(e.getFeeStartDay());
                    if (thisDate < 10) {
                        dateString = "0" + thisDate;
                    } else {
                        dateString = "" + thisDate;
                    }

                }
            }
            // 周期 月
            Integer feeCycleNum = FeeCycleTypeEnum.getValueByCode(e.getFeeCycleType());
            // 计算非预付账单最后一次周期结束时间
            LocalDate lastDate = LocalDate.parse(e.getLastMonth() + dateString, DateTimeFormatter.ofPattern("yyyyMMdd"));


            // 判断是否提前一周期进行收费 如是 周期数再加1
            if (FeeConstant.CYCLE_AHEAD.equals(e.getBillCycleType())) {
                lastDate = lastDate.plusMonths(2 * feeCycleNum).minusDays(1);
            } else {
                lastDate = lastDate.plusMonths(1 * feeCycleNum).minusDays(1);
            }
            // 计算预存周期的开始时间
            LocalDate beginDate = null;
            if (e.getLastPreDate() != null && e.getLastPreDate().isAfter(lastDate)) {
                beginDate = e.getLastPreDate().plusDays(1);
            } else {
                beginDate = lastDate.plusDays(1);
            }


            ProjectBillingInfoVo projectBillingInfo = new ProjectBillingInfoVo();
            BeanUtils.copyProperties(e, projectBillingInfo);
            projectBillingInfo.setFeeCycleStart(beginDate);
            // 结束时间为开始时间+月份-1天
            projectBillingInfo.setFeeCycleEnd(beginDate.plusMonths(type).minusDays(1));

            //设置预存账单月份为当前月
            projectBillingInfo.setBillMonth(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM")));
            //判断是否定额收费
            if (DataConstants.TRUE.equals(e.getFixAmountOrNot())) {
                //   定额收费则设置费用为定额的值 * 月份
                projectBillingInfo.setPayAbleAmount(e.getFixAmount().multiply(BigDecimal.valueOf(type)));
                // 定额收费 房屋内容不显示
                projectBillingInfo.setHouseArea(null);
                // 计费标准为 "金额:"+估计金额+"元"
                projectBillingInfo.setUnitString("金额: " + e.getFixAmount() + "元");
            } else {
                // 如果不是 应付金额=单价*房屋面积*频次* 月份 =单价*房屋面积*(时长/计算周期) * 月份
                Integer unitNum = FeeCycleTypeEnum.getValueByCode(e.getUnit());
                projectBillingInfo.setPayAbleAmount(e.getUnitPrice().multiply(e.getHouseArea()).multiply(BigDecimal.valueOf(type.doubleValue() / unitNum)));
                // 计费标准为 "金额:"+估计金额+"元"
                projectBillingInfo.setUnitString("单价: " + e.getUnitPrice() + "元");

            }
            //设置最后时间
            projectBillingInfo.setLastHave(e.getLastMonth());
            LocalDate thisDate = LocalDate.parse(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM")) + dateString, DateTimeFormatter.ofPattern("yyyyMMdd"));


            //是否存在上月以前未缴账单
            if (beginDate.equals(thisDate)) {
                projectBillingInfo.setLastHave(DataConstants.FALSE);
                projectBillingInfo.setAllHave(DataConstants.TRUE);
            } else if (thisDate.isBefore(thisDate.minusMonths(1))) {
                projectBillingInfo.setLastHave(DataConstants.TRUE);
                projectBillingInfo.setAllHave(DataConstants.TRUE);
            } else {
                projectBillingInfo.setLastHave(DataConstants.FALSE);
                projectBillingInfo.setAllHave(DataConstants.FALSE);
            }
            projectBillingInfos.add(projectBillingInfo);

        });

//        return baseMapper.listPreVo(houseId, type, billIds);
        return projectBillingInfos;
    }


    @Override
    public boolean updateListByBillMonth(List<ProjectBillingInfo> projectBillingInfos) {
        if (projectBillingInfos != null && projectBillingInfos.size() > 0) {
            return baseMapper.updateListByBillMonth(projectBillingInfos);
        } else {
            return false;
        }
    }


    /**
     * 根据房屋id 账单日期 费用id 获取房屋临时费用账单信息
     *
     * @param houseId
     * @param billMonth
     * @param feeIds
     * @return
     */
    @Override
    public void saveBill(String houseId, String billMonth, List<String> feeIds) {
        List<ProjectHousePersonRel> projectHousePersonRels = projectHousePersonRelService.list(
                Wrappers.lambdaQuery(ProjectHousePersonRel.class).eq(ProjectHousePersonRel::getHouseId, houseId).eq(ProjectHousePersonRel::getIsOwner, DataConstants.TRUE));

        LocalDateTime checkInDate = null;
        if (projectHousePersonRels != null && projectHousePersonRels.size() > 0) {
            checkInDate = projectHousePersonRels.get(0).getCheckInTime();
        }
        List<String> excludeIds = new ArrayList<>();
        // 过滤掉系统已经生成的账单
        List<ProjectBillingInfo> projectBillingInfos = list(Wrappers.lambdaQuery(ProjectBillingInfo.class)
                .eq(ProjectBillingInfo::getHouseId, houseId).eq(ProjectBillingInfo::getBillMonth, billMonth)
                .in(ProjectBillingInfo::getFeeId, feeIds));
        if (projectBillingInfos != null && projectBillingInfos.size() > 0) {
            excludeIds = projectBillingInfos.stream().map(ProjectBillingInfo::getFeeId).collect(Collectors.toList());
        }
        feeIds.removeAll(excludeIds);
        if (feeIds.size() == 0) {
            return;
        }
        //获取临时费用配置
        List<ProjectFeeConf> projectFeeConfList = projectFeeConfService.list(Wrappers.lambdaQuery(ProjectFeeConf.class)
                .eq(ProjectFeeConf::getFeeCycleType, FeeConstant.INCIDENTAL_EXPENSES)
                .eq(ProjectFeeConf::getStatus, DataConstants.TRUE)
                .in(ProjectFeeConf::getFeeId, feeIds));
        List<ProjectBillingInfo> projectBillingInfoList = new ArrayList<>();
        LocalDateTime finalCheckInDate = checkInDate;
        projectFeeConfList.forEach(e -> {
            // 判断是否为以住房同日
            String dateString = "01";
            if (FeeConstant.CHECK_IN_DATE.equals(e.getFeeStartDayType())) {
                if (finalCheckInDate != null) {
                    dateString = finalCheckInDate.format(DateTimeFormatter.ofPattern("dd"));
                }
            } else {
                if (e.getFeeStartDay() != null && !"".equals(e.getFeeStartDay())) {
                    //防止传入字符为"1"的字符串造成时间格式转换报错
                    Integer thisDate = Integer.valueOf(e.getFeeStartDay());
                    if (thisDate < 10) {
                        dateString = "0" + thisDate;
                    } else {
                        dateString = "" + thisDate;
                    }

                }
            }
            // 周期 月
            Integer feeCycleNum = FeeCycleTypeEnum.getValueByCode(e.getFeeCycleType());
            LocalDate beginDate = LocalDate.parse(billMonth + dateString, DateTimeFormatter.ofPattern("yyyyMMdd"));


            // 判断是否提前一周期进行收费 如是 周期数再加1
            if (FeeConstant.CYCLE_AHEAD.equals(e.getBillCycleType())) {
                beginDate = beginDate.plusMonths(feeCycleNum);
            }

            ProjectBillingInfo projectBillingInfo = new ProjectBillingInfo();
            projectBillingInfo.setPrestore(DataConstants.FALSE);
            projectBillingInfo.setFeeId(e.getFeeId());
            projectBillingInfo.setHouseId(houseId);
            projectBillingInfo.setFeeName(e.getFeeName());
            projectBillingInfo.setBillMonth(billMonth);
            projectBillingInfo.setFeeCycleEnd(beginDate.plusMonths(feeCycleNum).minusDays(1));
            projectBillingInfo.setFeeCycleStart(beginDate);
            projectBillingInfo.setFixAmountOrNot(DataConstants.TRUE);
            projectBillingInfo.setFixAmount(e.getFixAmount());
            projectBillingInfo.setPayStatus(PayStatusEnum.UNPAID.code);
            // 临时的应收费用为固定费用
            projectBillingInfo.setPayAbleAmount(e.getFixAmount());
            projectBillingInfoList.add(projectBillingInfo);
        });
        saveBatch(projectBillingInfoList);
    }

    @Override
    public List<ProjectBillingInfoVo> listOnDeposit(String id) {
        return baseMapper.listOnDeposit(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateDeposit(List<String> ids) {
        List<ProjectBillingInfo> projectBillingInfos = listByIds(ids);
        projectBillingInfos.forEach(e -> {
            if (PayStatusEnum.PAID.code.equals(e.getPayStatus()))
                e.setPayStatus(PayStatusEnum.REFUND.code);
        });
        updateBatchById(projectBillingInfos);
    }


}