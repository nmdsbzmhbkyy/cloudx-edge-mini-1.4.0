package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectPerimeterAlarmArea;
import com.aurine.cloudx.estate.vo.ProjectDeviceLocationVo;
import com.aurine.cloudx.estate.vo.ProjectPerimeterAlarmAreaVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 防区管理
 *
 * @author 邹宇
 * @date 2021-6-15 14:45:18
 */
public interface ProjectPerimeterAlarmAreaService extends IService<ProjectPerimeterAlarmArea> {



    /**
     * 分页查询
     *
     * @param projectPerimeterAlarmAreaVo
     * @return
     */
    Page<ProjectPerimeterAlarmAreaVo> findPage(Page page, @Param("query") ProjectPerimeterAlarmAreaVo projectPerimeterAlarmAreaVo);


    /**
     * 验证是否重名
     * @param channelName
     * @param deviceId
     * @return
     */
    Boolean verifyDuplicateName(String channelName, String deviceId);

    /**
     * 重新获取防区
     * @param deviceId
     * @return
     */
    Boolean reacquireDefenseArea(String deviceId);

    /**
     * 修改状态
     * @param infoUid
     * @param name
     * @return
     */
    Boolean updateStatusById(String infoUid, String name);



    /**
     * 更改区域定位
     * @param projectDeviceLocationVo
     * @return
     */
    Boolean putPoint(ProjectDeviceLocationVo projectDeviceLocationVo);

    Boolean putMultiDefenseAreaInfo(String deviceId,List<ProjectPerimeterAlarmAreaVo> lstProjectPerimeterAlarmAreaVo);

    Boolean putDefenseAreaInfo(ProjectPerimeterAlarmAreaVo projectPerimeterAlarmArea);
}
