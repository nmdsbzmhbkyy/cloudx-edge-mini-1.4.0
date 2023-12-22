

package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;
import org.apache.ibatis.annotations.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 设备信息表
 *
 * @author xull@aurine.cn
 * @date 2020-05-20 10:38:59
 */
public interface ProjectDeviceInfoService extends IService<ProjectDeviceInfo> {

    /**
     * 分页查询设备
     *
     * @param page
     * @param projectDeviceInfoPageFormVo
     *
     * @return
     */
    Page<ProjectDeviceInfoPageVo> pageVo(Page page, ProjectDeviceInfoPageFormVo projectDeviceInfoPageFormVo);

    /**
     * 根据设备类型列表及楼栋单元id查询获取设备
     *
     * @param projectDeviceInfoFormVo
     *
     * @return
     */
    List<ProjectDeviceInfo> findByType(ProjectDeviceInfoFormVo projectDeviceInfoFormVo);

    /**
     * 根据设备id获取设备信息视图
     *
     * @param id
     *
     * @return
     */
    ProjectDeviceInfoVo getProjectDeviceInfoById(String id);





    /**
     * 根据组团id获取区口机
     * @param deviceEntityId
     * @return
     */
    List<ProjectDeviceInfo> getListByDeviceEntityId(String deviceEntityId);

    /**
     * 根据id获取到该巡检点绑定的设备列表
     * @author 王良俊
     * @param pointId 巡检点id
     * @return
     */
    List<ProjectPointDeviceVo> listDeviceByPointId(String pointId);

    /**
     * 根据设备的第三方编号更新设备状态
     * @param deviceCode
     * @param status
     * @author: 王伟
     * @since 2020-08-07
     * @return
     */
    boolean updateStatusByDeviceCode(String deviceCode,String status);


    /**
     * 根据设备的SN编号更新设备状态
     * @param deviceSn
     * @param status
     * @author: 王伟
     * @since 2020-08-07
     * @return
     */
    boolean updateStatusByDeviceSn(String deviceSn,String status);

    /**
     * 通过第三方ID,获取设备
     * @param deviceCode
     * @author: 王伟
     * @since 2020-08-07
     * @return
     */
    ProjectDeviceInfo getByDeviceCode(String deviceCode);
    
    /**
     * 获取直播流地址
     * @param deviceInfo
     * @return
     */
    String getLiveUrl(String deviceInfo);

    /**
     * 获取直播流地址
     * @param
     * @return
     */
    String getVideoUrl(String deviceId, Long startTime, Long endTime);



    /**
     * 获取离线设备数量
     * @return
     * @author: 王伟
     * @since: 2020-09-03
     */
    int countOfflineDevice();

    /**
     * 根据设备类型查询该项目下的设备
     *
     * @param projectId
     * @param type
     * @return
     */
    List<ProjectDeviceInfo> listDeviceByType(Integer projectId, String type);

    Boolean uniqueDeviceNameByProject(String deviceName, String deviceId);

    int countByProductId(String productId);

    /**
     * 设备参数信息分页查询
     *
     * @param page
     * @param projectDeviceInfoPageFormVo
     *
     * @return
     */
    Page<ProjectDeviceInfoPageVo> pageDeviceParam(Page page, ProjectDeviceInfoPageFormVo projectDeviceInfoPageFormVo);

    /**
     * 查询不支持富文本的门禁设备列表
     * @param projectDeviceInfoFormVo
     * @return
     */
    List<ProjectDeviceInfo> findRichByType(ProjectDeviceInfoFormVo projectDeviceInfoFormVo);

    /**
     * 根据设备sn查询统计设备
     * @param sn
     * @return
     */
    Integer countBySn(String sn,String deviceId);
}
