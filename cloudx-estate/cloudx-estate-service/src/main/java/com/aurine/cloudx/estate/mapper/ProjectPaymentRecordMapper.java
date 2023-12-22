package com.aurine.cloudx.estate.mapper;
import com.aurine.cloudx.estate.vo.ProjectPaymentRecordFormVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aurine.cloudx.estate.entity.ProjectPaymentRecord;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

/**
 * 交易记录(ProjectPaymentRecord)表数据库访问层
 *
 * @author makejava
 * @since 2020-07-23 18:54:07
 */
@Mapper
public interface ProjectPaymentRecordMapper extends BaseMapper<ProjectPaymentRecord> {

    IPage<ProjectPaymentRecord> pageAll(Page<ProjectPaymentRecord> page, @Param("query") ProjectPaymentRecordFormVo projectPaymentRecord);
    /**
     * 统计某个日期时间端内的费用收入信息
     * @param beginDate
     * @param endDate
     * @return
     */
    Double getSumFeeByDate(@Param("beginDate") LocalDate beginDate,@Param("endDate") LocalDate endDate);
}