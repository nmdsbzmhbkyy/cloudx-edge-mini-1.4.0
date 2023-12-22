package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.dto.ProjectPersonDeviceDTO;
import com.aurine.cloudx.estate.dto.ProjectUserHouseDTO;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoProxyVo;
import com.aurine.cloudx.estate.vo.ProjectPassDeviceVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

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
     *
     * @param personDeviceDTO
     * @return
     */
    boolean savePersonDevice(ProjectPersonDeviceDTO personDeviceDTO);


    /**
     * 批量更新并下发人员设备权限
     * 通常用于设备、方案变更。
     *
     * @return
     */
    Boolean updateBatchPersonDevice(List<ProjectPersonPlanRel> personPlanRelList , int projectId);


    /**
     * 添加设备后，更新项目下全部用户权限，并重新下发
     *
     * @return
     */
    boolean refreshAddDevice(ProjectDeviceInfo device);

    /**
     * 关闭通行权限
     *
     * @param personType 人员类型
     * @param personId   人员ID
     * @return
     */
    boolean disablePassRight(String personType, String personId);


    /**
     * 开启通行权限
     *
     * @param personType
     * @param personId
     * @return
     */
    boolean enablePassRight(String personType, String personId);

    /**
     * 删除设备后，更新项目下全部用户权限，并重新下发
     */
    boolean refreshDeleteDevice(String deviceId);

    boolean refresh();

    /**
     * 根据personId,重载该用户的权限，用于用户房屋归属变化等情况
     *
     * @param personId
     * @return
     */
    boolean refreshByPersonId(String personId, PersonTypeEnum personTypeEnum);


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
     * 根据人员ID 和 方案ID 获得该用户可以使用的设备VO列表(根据规则)
     *
     * @param personId
     * @return
     */
    List<ProjectPassDeviceVo> listDeviceByPersonIdAndPlanId(String personId, String planId);


    List<ProjectPassDeviceVo> listDeviceByPersonIdAndPlanIdOnlyPlan(String personId, String planId);

    /**
     * 根据系统默认配置，分配用户的默认通行方案
     *
     * @param personType
     * @param personId
     * @return
     */
    String initDefaultPassRightPlan(PersonTypeEnum personType, String personId);

    /**
     * 获取云对讲设备下的人员信息
     *
     * @param deviceInfo
     * @return
     */
    List<ProjectUserHouseDTO> getDevicePersonInfoList(ProjectDeviceInfoProxyVo deviceInfo);

    /**
     * 获取人员下的云对讲设备信息
     *
     * @param projectHousePersonRel
     * @return
     */
    List<ProjectUserHouseDTO> getPersonDeviceInfoList(ProjectHousePersonRel projectHousePersonRel);

    List<String> findPerson(List<String> planId, String buildingId, String unitId, String groupId);
}
