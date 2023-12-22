

package com.aurine.cloudx.estate.service;

import com.aurine.parking.entity.po.ProjectPlateNumberDevice;
import com.aurine.parking.entity.vo.ParkingDeviceCertDlstatusCountVo;
import com.aurine.parking.entity.vo.PlateNumberDeviceSearchCondition;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 车牌下发记录服务
 * </p>
 *
 * @author 王良俊
 * @since 2022/5/26 15:08
 */
public interface ProjectPlateNumberDeviceService {

    /**
     * <p>
     * 分页查询车牌号下发情况
     * </p>
     *
     * @param page  分页信息
     * @param query 查询条件
     * @return 分页数据
     * @author: 王良俊
     */
    R<Page<ProjectPlateNumberDevice>> selectPage(Page page, PlateNumberDeviceSearchCondition query);

    /**
     * <p>
     * 根据设备id获取所有车牌号下发记录
     * </p>
     *
     * @param deviceId 设备ID
     * @return 该设备的车牌号下发记录
     * @author: 王良俊
     */
    R<List<ProjectPlateNumberDevice>> listByDeviceId(String deviceId);

    /**
     * <p>
     * 获取车牌号下发情况的统计信息
     * </p>
     *
     * @return 当前项目车牌号下发情况统计信息
     * @author: 王良俊
     */
    R<List<ParkingDeviceCertDlstatusCountVo>> countByProject();

    /**
     * <p>
     * 导出车牌下发记录报表
     * </p>
     *
     * @param deviceId 设备id
     * @param deviceName 设备名
     * @author: 王良俊
     * @return
     */
    void exportExcel(String deviceId, String deviceName, HttpServletResponse httpServletResponse);

    /**
     * <p>
     * 重新下发失败车牌号
     * </p>
     *
     * @param deviceId 设备ID
     * @author: 王良俊
     * @return 是否操作成功
     */
    R<Boolean> redownloadFailedPlateNumber(List<String> deviceIdList);
}
