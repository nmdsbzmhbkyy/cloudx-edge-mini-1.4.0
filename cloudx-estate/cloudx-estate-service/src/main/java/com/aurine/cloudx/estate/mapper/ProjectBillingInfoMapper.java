package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectBillingInfo;
import com.aurine.cloudx.estate.vo.ProjectBillingInfoFormVo;
import com.aurine.cloudx.estate.vo.ProjectBillingInfoVo;
import com.aurine.cloudx.estate.vo.ProjectHouseFeeTotalFormVo;
import com.aurine.cloudx.estate.vo.ProjectHouseFeeTotalVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

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
    IPage<ProjectHouseFeeTotalVo> pageBillTotal(Page<ProjectHouseFeeTotalVo> page,@Param("query") ProjectHouseFeeTotalFormVo query);

    /**
     * 分页查询账单信息
     * @param page 分页
     * @param query 查询视图
     * @return 账单信息
     */
    IPage<ProjectBillingInfoVo> pageBill(Page<ProjectBillingInfoVo> page,@Param("query")  ProjectBillingInfoFormVo query);

    /**
     * 获取未付款账单
     * @param houseId 房屋id
     * @param billIds 账单id
     * @return 未付款账单信息
     */
    List<ProjectBillingInfoVo> listVo(@Param("houseId") String houseId,@Param("billIds")List<String> billIds,@Param("payStatusList")List<String> payStatusList);


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


}