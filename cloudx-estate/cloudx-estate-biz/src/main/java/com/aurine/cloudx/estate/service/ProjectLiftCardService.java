package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectCardIssueRecord;
import com.aurine.cloudx.estate.vo.LiftCardVo;
import com.aurine.cloudx.estate.vo.LiftFunctionCardVo;
import com.aurine.cloudx.estate.vo.LiftProprietorCardVo;
import com.aurine.cloudx.estate.vo.LiftStaffCardVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @Auther: hjj
 * @Date: 2022/3/31 15:35
 * @Description: 电梯卡管理
 */
public interface ProjectLiftCardService {
    /**
    * @Author hjj
    * @Description 功能卡数据
    * @Date  2022/4/1
    * @Param cardType LiftCardTypeEnum 电梯卡类型枚举
    * @return
    **/
    String getAbilityCardHex(Integer cardType, Object object);
    /**
    * @Author hjj
    * @Description 业主卡数据
    * @Date  2022/4/1
    * @Param cardType LiftCardTypeEnum 电梯卡类型枚举
    * @return
    **/
    LiftCardVo getProprietorCardHex(Integer cardType, LiftProprietorCardVo liftProprietorCardVo);

    LiftCardVo getInitDeviceData();

    LiftCardVo getInitRFData();

    LiftCardVo getReadRFData(Integer type);

    /**
     * @Author hjj
     * @Description 员工卡数据
     * @Date  2022/4/1
     * @Param cardType LiftCardTypeEnum 电梯卡类型枚举
     * @return
     **/
    LiftCardVo getStaffCardHex(Integer cardType, LiftStaffCardVo staffCardVo);

    /**
     * @Author hjj
     * @Description 员工卡数据
     * @Date  2022/4/1
     * @Param cardType LiftCardTypeEnum 电梯卡类型枚举
     * @return
     **/
    LiftCardVo getFuctionCardHex(LiftFunctionCardVo liftFunctionCardVo);

    /**
     * 保存写卡记录
     * @param liftFunctionCardVo
     * @return
     */
    Boolean saveCardIssueRecord(LiftFunctionCardVo liftFunctionCardVo);

    /**
     * 分页查询写卡记录
     * @param page
     * @param liftFunctionCardVo
     * @return
     */
    Page<ProjectCardIssueRecord> pageCardIssueRecord(Page page, LiftFunctionCardVo liftFunctionCardVo);
}
