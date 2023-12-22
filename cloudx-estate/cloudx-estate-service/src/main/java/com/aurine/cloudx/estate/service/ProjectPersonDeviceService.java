

package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.dto.ProjectPersonDeviceDTO;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectPersonDevice;
import com.aurine.cloudx.estate.entity.ProjectPersonPlanRel;
import com.aurine.cloudx.estate.vo.ProjectPassDeviceVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.scheduling.annotation.Async;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>人员设备权限 基础 接口</p>
 *
 * @ClassName: ProjectPersonDeviceService
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/22 8:40
 * @Copyright:
 */
public interface ProjectPersonDeviceService extends IService<ProjectPersonDevice> {


    /**
     * 批量添加人员-设备权限信息
     * 无方案
     * 默认未不激活状态
     * 不下发设备
     *
     * @param personId
     * @param deviceIdArray
     * @return
     */
    boolean savePersonDevice(String personId, PersonTypeEnum personType, String[] deviceIdArray, LocalDateTime effTime, LocalDateTime expTime);


    boolean refreshDeleteDevice(String deviceId);

    /**
     * 批量删除设备后,更新项目下全部用户权限，并重新下发
     *
     * @param deviceId
     * @return
     */
    boolean refreshDeleteDeviceAll(List<String> deviceId);






    /**
     * 删除指定人员的全部权限信息
     *
     * @param personId
     * @return
     */
    boolean deleteByPersonId(String personId);

    /**
     * 根据personId 查DTO
     *
     * @param personId
     * @return
     */
    ProjectPersonDeviceDTO getDTOByPersonId(String personId);

    /**
     * 通过personId 获取到第一个权限对象
     *
     * @param personId
     * @return
     */
    ProjectPersonDevice getByPersonId(String personId);

    /**
     * 根据人员ID 获取该用户可以使用的设备权限
     *
     * @return
     */
    List<ProjectPersonDevice> listByPersonId(String personId);

    /**
     * 根据人员ID 分页获取该用户可以使用的设备权限
     *
     * @return
     */
    Page<ProjectPersonDevice> pageByPersonId(Page page, String personId);

    /**
     * 根据人员ID 获得该用户可以使用的设备VO列表(从关系存储表中)
     *
     * @param personId
     * @return
     */
    List<ProjectPassDeviceVo> listDeviceByPersonId(String personId);

    Page<ProjectPassDeviceVo> pageDeviceByPersonId(Page page, String personId);
    /**
     * 根据人员ID 和 方案ID 获得该用户可以使用的设备VO列表(小程序业务)
     *
     * @param personId
     * @param planId
     * @return
     */
    List<ProjectPassDeviceVo> wechatGetlistDevice(String personId, String planId);

    /**
     * 根据人员ID 和 方案ID 获得该用户可以使用的设备VO列表(根据规则)
     *
     * @param personId
     * @return
     */
    List<ProjectPassDeviceVo> listDeviceByPersonIdAndPlanId(String personId, String planId);



    /**
     * 根据系统默认配置，分配用户的默认通行方案
     *
     * @param personType
     * @param personId
     * @return
     */
    String initDefaultPassRightPlan(PersonTypeEnum personType, String personId);


}
