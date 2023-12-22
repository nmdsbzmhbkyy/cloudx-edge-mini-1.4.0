package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.origin.entity.ProjectBillingInfo;
import com.aurine.cloudx.open.origin.vo.*;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 项目已出账的账单信息(ProjectBillingInfo)表数据库访问层
 *
 * @author makejava
 * @since 2020-07-20 16:43:48
 */
@Mapper
public interface ProjectBillingInfoMapper extends BaseMapper<ProjectBillingInfo> {

    /**
     * 分页查询房屋汇总账单信息
     * @param page 分页
     * @param query 查询视图
     * @return 房屋汇总账单信息
     */
    IPage<ProjectHouseFeeTotalVo> pageBillTotal(Page<ProjectHouseFeeTotalVo> page, @Param("query") ProjectHouseFeeTotalFormVo query);

    /**
     * 查询房屋汇总账单信息
     *
     * @param query 查询视图
     * @return 房屋汇总账单信息
     */
    List<ProjectHouseFeeTotalVo> pageBillTotal(@Param("query") ProjectHouseFeeTotalFormVo query);

    /**
     * 分页查询账单信息
     * @param page 分页
     * @param query 查询视图
     * @return 账单信息
     */
    IPage<ProjectBillingInfoVo> pageBill(Page<ProjectBillingInfoVo> page, @Param("query") ProjectBillingInfoFormVo query);



    /**
     * 获取未付款账单
     * @param houseId 房屋id
     * @param billIds 账单id
     * @return 未付款账单信息
     */
    List<ProjectBillingInfoVo> listVo(@Param("houseId") String houseId, @Param("billIds") List<String> billIds, @Param("payStatusList") List<String> payStatusList);


    /**
     * 根据houseId,feeId,billMonth批量更新账单信息
     *
     * @param projectBillingInfos 账单信息列表
     * @return boolean
     */
    boolean updateListByBillMonth(@Param("list") List<ProjectBillingInfo> projectBillingInfos);

    /**
     * 查询押金列表
     * @param id
     * @return
     */
    List<ProjectBillingInfoVo> listOnDeposit(@Param("id") String id);

    /**
     * @description: 缴费分类汇总分页
     * @param:
     * @return:
     * @author cjw
     * @date: 2021/7/8 11:45
     */
    IPage<ProjectBillingInfoVo>  getFeeReportPage(Page<ProjectHouseFeeTotalVo> page, @Param("query") ProjectBillingInfoVo query);

    List<Map<String, String>> getDict();

    /**
     *
     * 查询缴费关键指标
     * @param query
     * @return
     */
    @SqlParser(filter = true)
    FeeRate getFeeRate(@Param("query") ProjectBillingInfoVo query);

    @SqlParser(filter = true)
    List<AppProjectBillingInfoVo> getDetailByOrder(@Param("orderNo") String orderNo);

    List<String> findPersonIdList(@Param("houseId") String houseId);

    double findMoneyByHouseId(@Param("houseId") String houseId);

    List<String> findHouseList();

    List<ProjectBillingInfoVo> findListByHouseId(@Param("houseId") String houseId);

    double findMoneyByBillingNo(@Param("billingNo") String billingNo);

}