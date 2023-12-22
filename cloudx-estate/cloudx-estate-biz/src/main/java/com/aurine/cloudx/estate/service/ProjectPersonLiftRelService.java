package com.aurine.cloudx.estate.service;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.dto.ProjectPersonLiftDTO;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.vo.ProjectPassDeviceVo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>人员电梯设备权限 基础 接口</p>
 *
 * @ClassName: ProjectPersonLiftService
 * @author: hjj
 * @date: 2022-03-16 09:10:52
 * @Copyright:
 */
public interface ProjectPersonLiftRelService extends IService<ProjectPersonLiftRel> {

    /**
     * 批量添加人员-设备权限信息
     *
     * @param projectPersonLiftDTO
     * @return
     */
    boolean savePersonDevice(ProjectPersonLiftDTO projectPersonLiftDTO);

    /**
     * 批量添加人员-设备权限信息
     * 无方案
     * 默认未不激活状态
     * 不下发设备
     *
     * @param personId
     * @param liftList
     * @return
     */
    boolean savePersonDevice(String personId, PersonTypeEnum personType, List<String> liftList, LocalDateTime effTime, LocalDateTime expTime);

    /**
     * 批量更新并下发人员设备权限
     * 通常用于设备、方案变更。
     *
     * @return
     */
    Boolean updateBatchPersonDevice(List<ProjectPersonLiftPlanRel> projectPersonLiftPlanRels , int projectId);


    /**
     * 关闭通行权限
     *
     * @param personType 人员类型
     * @param personId   人员ID
     * @return
     */
    boolean disablePassRight(String personType, String personId);

    List<ProjectPassDeviceVo> listLiftByPersonIdAndPlanId(String personId, String planId);

    List<ProjectPassDeviceVo> listLiftByPersonId(String personId, String planId);

    /**
     * 开启通行权限
     *
     * @param personType
     * @param personId
     * @return
     */
    boolean enablePassRight(String personType, String personId);

    /**
     * 删除指定人员的全部权限信息
     *
     * @param personId
     * @return
     */
    boolean deleteByPersonId(String personId);

    /**
     * 根据人员ID 获取该用户可以使用的设备权限
     *
     * @return
     */
    List<ProjectPersonLiftRel> listByPersonId(String personId);

    /**
     * 根据人员id 获取电梯设备的乘梯识别终端
     * @param personId
     * @return
     */
    List<String> childDeviceIdByLift(String personId);

    /**
     * 删除设备后，更新项目下全部用户权限，并重新下发
     */
    boolean refreshDeleteDevice(String deviceId);

    boolean refreshDeleteDeviceAll(List<String> ids);

    @Transactional(rollbackFor = Exception.class)
    boolean refreshAddDevice(ProjectDeviceInfo device);
}
