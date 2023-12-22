package com.aurine.cloudx.estate.service;
import com.aurine.cloudx.estate.vo.ProjectPaymentRecordFormVo;
import com.aurine.cloudx.estate.vo.ProjectPaymentRecordVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aurine.cloudx.estate.entity.ProjectPaymentRecord;
import com.pig4cloud.pigx.common.core.util.R;

import java.time.LocalDate;

/**
 * 交易记录(ProjectPaymentRecord)表服务接口
 *
 * @author makejava
 * @since 2020-07-23 18:54:07
 */
public interface ProjectPaymentRecordService extends IService<ProjectPaymentRecord> {

    /**
     * 保存费用账单
     * @param projectPaymentRecord
     * @return
     */
    boolean savePayment(ProjectPaymentRecordVo projectPaymentRecord);

    /**
     * 保存预存费用账单
     * @param projectPaymentRecord
     * @return
     */
    R savePrePayment(ProjectPaymentRecordVo projectPaymentRecord);

    IPage pageAll(Page<ProjectPaymentRecord> page, ProjectPaymentRecordFormVo projectPaymentRecord);


    /**
     * 统计某个日期时间段内费用数据
     * @param beginDate  开始时间
     * @param endDate 结束时间
     * @return
     */
    Double getSumFeeByDate(LocalDate beginDate, LocalDate endDate);
}