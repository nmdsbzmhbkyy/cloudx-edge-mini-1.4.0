
package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.origin.entity.ProjectPlateNumberDevice;
import com.aurine.cloudx.open.origin.vo.ParkingDeviceCertDlstatusCountVo;
import com.aurine.cloudx.open.origin.vo.ProjectPlateNumberDeviceVo;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  设备车牌号下发情况
 * </p>
 *
 * @author 王良俊
 * @since 2022/5/26 15:17
 */
@Mapper
public interface ProjectPlateNumberDeviceMapper extends BaseMapper<ProjectPlateNumberDevice> {

    /**
     * <p>
     * 根据项目ID统计设备车牌号下发情况
     * </p>
     *
     * @param projectId 要统计的项目ID
     * @author: 王良俊
     * @return 统计信息列表
     */
    @SqlParser(filter=true)
    List<ParkingDeviceCertDlstatusCountVo> countByProjectId(@Param("projectId") Integer projectId);


    /**
     * <p>
     * 获取车牌下发记录Vo列表（两个字典属性都转换成对应的中文描述）
     * </p>
     *
     * @param deviceId 设备ID
     * @author: 王良俊
     * @return 字典值转换成中文描述的vo对象列表
     */
    List<ProjectPlateNumberDeviceVo> listVoByDeviceId(@Param("deviceId") String deviceId);

}
