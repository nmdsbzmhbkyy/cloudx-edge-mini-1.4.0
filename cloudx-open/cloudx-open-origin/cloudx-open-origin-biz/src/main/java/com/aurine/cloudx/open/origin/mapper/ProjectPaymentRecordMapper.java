package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.origin.vo.AppProjectPaymentRecordFromVo;
import com.aurine.cloudx.open.origin.vo.ProjectPaymentRecordFormVo;
import com.aurine.cloudx.open.origin.entity.ProjectPaymentRecord;
import com.aurine.cloudx.open.origin.vo.AppProjectPaymentRecordVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 交易记录(ProjectPaymentRecord)表数据库访问层
 *
 * @author makejava
 * @since 2020-07-23 18:54:07
 */
@Mapper
public interface ProjectPaymentRecordMapper extends BaseMapper<ProjectPaymentRecord> {

    IPage<ProjectPaymentRecord> pageAll(Page<ProjectPaymentRecord> page, @Param("query") ProjectPaymentRecordFormVo projectPaymentRecord);

    List<ProjectPaymentRecord> pageAll(@Param("query") ProjectPaymentRecordFormVo projectPaymentRecord);

    /**
     * 统计某个日期时间端内的费用收入信息
     * @param beginDate
     * @param endDate
     * @return
     */
    Double getSumFeeByDate(@Param("beginDate") LocalDate beginDate, @Param("endDate") LocalDate endDate);



    IPage<AppProjectPaymentRecordVo> selectAllApp(Page<AppProjectPaymentRecordVo> page, @Param("query") AppProjectPaymentRecordFromVo appProjectPaymentRecordVo);

    /**
     * 根据订单ID查找订单
     * @param payOrderNo
     * @return
     */
    ProjectPaymentRecord selectByPayOrderNo(String payOrderNo);

}
