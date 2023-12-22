package com.aurine.cloudx.estate.service;
import com.aurine.cloudx.estate.vo.AppProjectPaymentRecordFromVo;
import com.aurine.cloudx.estate.vo.AppProjectPaymentRecordVo;
import com.aurine.cloudx.estate.vo.ProjectPaymentRecordFormVo;
import com.aurine.cloudx.estate.vo.ProjectPaymentRecordVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aurine.cloudx.estate.entity.ProjectPaymentRecord;
import com.pig4cloud.pigx.common.core.util.R;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
     * 生成订单
     * @param projectPaymentRecord
     * @return
     */
    boolean savePaymentNotPaid(ProjectPaymentRecordVo projectPaymentRecord);

    boolean updateOrderStatus(Map map);

    /**
     * 保存预存费用账单
     * @param projectPaymentRecord
     * @return
     */
    R savePrePayment(ProjectPaymentRecordVo projectPaymentRecord);

    IPage pageAll(Page<ProjectPaymentRecord> page, ProjectPaymentRecordFormVo projectPaymentRecord);
    
    /** 
     * @description: 导出execel
     * @param:  projectPaymentRecord
     * @return:  
     * @author cjw
     * @date: 2021/7/6 15:44
     */
    void exportExcel (HttpServletResponse response,ProjectPaymentRecordFormVo projectPaymentRecord);

    /**
     * 统计某个日期时间段内费用数据
     * @param beginDate  开始时间
     * @param endDate 结束时间
     * @return
     */
    Double getSumFeeByDate(LocalDate beginDate, LocalDate endDate);

    IPage<AppProjectPaymentRecordVo> selectAllApp(Page<AppProjectPaymentRecordVo> page, AppProjectPaymentRecordFromVo appProjectPaymentRecordVo);

    Boolean selectByPayOrderNo(String payOrderNo);

    /**
     * 查询缴费项是否被锁
     * @return
     */
    R checkCode(ProjectPaymentRecordVo projectPaymentRecord);

}