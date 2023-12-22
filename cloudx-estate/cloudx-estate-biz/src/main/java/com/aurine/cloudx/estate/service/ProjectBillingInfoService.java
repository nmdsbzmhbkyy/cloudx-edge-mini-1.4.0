package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectBillDayConf;
import com.aurine.cloudx.estate.entity.ProjectBillingInfo;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;
import org.apache.ibatis.annotations.Param;


import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 项目已出账的账单信息(ProjectBillingInfo)表服务接口
 *
 * @author makejava
 * @since 2020-07-20 16:43:48
 */
public interface ProjectBillingInfoService extends IService<ProjectBillingInfo> {

    /**
     * 分页查询出账明细
     * @param page 分页
     * @param projectBillingInfoFormVo 查询条件
     * @return 明细信息
     */
    IPage<ProjectBillingInfoVo> pageBill(Page<ProjectBillingInfoVo> page, ProjectBillingInfoFormVo projectBillingInfoFormVo);

    /**
     * 分页查询账单汇总信息
     * @param page 分页
     * @param projectHouseFeeTotalFormVo 查询条件
     * @return 汇总信息
     */

     IPage<ProjectHouseFeeTotalVo> pageBillTotal(Page<ProjectHouseFeeTotalVo> page, ProjectHouseFeeTotalFormVo projectHouseFeeTotalFormVo) ;

    /**
     * 根据账单id列表 查询账单信息(含上月以前未缴状态,及所有未缴状态)
     * @param houseId
     * @param billIds
     * @param payStatusList 付款状态列表
     * @return
     */
    List<ProjectBillingInfoVo> listVo(String houseId,List<String> billIds,List<String> payStatusList);
    /**
     * 查询账单信息(含上月以前未缴状态,及所有未缴状态)
     * @param id
     * @return
     */
    List<ProjectBillPromotionVo> listOnPromotion(String id);

    /**
     * 查询优惠后未缴纳账单
     * @param id 房屋id
     * @param billIds 账单id列表
     * @param payStatusList  付款状态列表
     * @return
     */
    List<ProjectBillPromotionVo> listOnPromotion(String id,List<String > billIds,List<String> payStatusList);
    /**
     * 更据选择的费用id和选择的优惠类型 查询优惠后预存费用
     * @param id 房屋
     * @param type 类型
     * @param feeIds  费用id列表
     * @return
     */
    List<ProjectBillPromotionVo> listOnPrePromotion(String id, Integer type, List<PayBillVo> feeIds);

    /**
     * 查询优惠后预存费用
     * @param id 房屋
     * @param type 类型
     * @return
     */
    List<ProjectBillPromotionVo> listOnPrePromotion(String id, Integer type);

    /**
     * 重新生成账单
     * @return
     */
    R resentBillingInfo(ProjectHouseFeeTotalVo rojectHouseFeeTotalVo);

    /**
     * 重新批量生成账单
     * @return
     */
    R resentBillingInfoBatch();

    /**
     * 根据账单列表批量更新账单付款信息
     * @param projectBillingInfos 账单列表
     * @return
     */
    boolean updateListByBillMonth(List<ProjectBillingInfo> projectBillingInfos);

    /**
     * 新增临时费用
     * @param houseId
     * @param billMonth
     * @param feeIds
     */
    void  saveBill(String houseId, String billMonth, List<String> feeIds);

    /**
     * 获取押金列表
     * @param id
     * @return
     */
    List<ProjectBillingInfoVo> listOnDeposit(String id);

    /**
     * 退押金
     * @param ids
     */
    void updateDeposit(List<String> ids);
    
    /** 
     * @description: 导出excel 
     * @param:  
     * @return:  
     * @author cjw
     * @date: 2021/7/8 15:24
     */
    void exportExcel(Page<ProjectBillingInfoVo> page, ProjectBillingInfoFormVo projectBillingInfo, HttpServletResponse httpServletResponse);
    
    /** 
     * @description: 缴费分类汇总分页
     * @param:  
     * @return:  
     * @author cjw
     * @date: 2021/7/8 15:24
     */
    IPage<ProjectBillingInfoVo>  getFeeReportPage(Page<ProjectHouseFeeTotalVo> page, ProjectBillingInfoVo query);

    /**
     * @description:缴费关键指标
     * @param:
     * @return:
     * @author cjw
     * @date: 2021/7/8 15:24
     */
    FeeRate getFeeRate(ProjectBillingInfoVo query);

    List<Map<String,String>> getDict();

    List<AppProjectBillingInfoVo> getDetailByOrder(String orderNo);


    R callByHouseId(String houseId);

    void automaticCall(ProjectBillDayConf projectBillDayConf);

    R getMoneyByBillingNo(String billingNo);

}