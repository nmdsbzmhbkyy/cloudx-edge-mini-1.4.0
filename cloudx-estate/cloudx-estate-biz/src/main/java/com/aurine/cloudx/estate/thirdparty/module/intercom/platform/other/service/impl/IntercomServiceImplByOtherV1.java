package com.aurine.cloudx.estate.thirdparty.module.intercom.platform.other.service.impl;

import com.aurine.cloudx.estate.entity.ProjectHousePersonRel;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.IntercomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

/**
 * 冠林中台 V1 版本 对接业务实现
 *
 * @ClassName: PassWayDeviceServiceImplByAurineV1
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-12 14:00
 * @Copyright:
 */
@Service
@Slf4j
@RefreshScope
public class IntercomServiceImplByOtherV1 implements IntercomService {

    /**
     * 获取版本
     *
     * @return
     */
    @Override
    public String getVersion() {
        return VersionEnum.V1.code;
    }

    public VersionEnum getVer() {
        return VersionEnum.V1;
    }

    /**
     * 获取平台类型
     *
     * @return
     */
    @Override
    public String getPlatform() {
        return PlatformEnum.OTHER.code;
    }


    /**
     * 添加项目
     * 如果项目下存在住户设备，根据规则添加住户和设备的云对讲关系
     *
     * @param projectId
     * @return
     */
    @Override
    public boolean addProject(Integer projectId) {
        System.out.println("other");
        return false;
    }

    /**
     * 删除项目
     * 如果项目下存在设备、住户，这按照关系、房屋、住户、设备、项目顺序依次清空
     *
     * @param projectId
     * @return
     */
    @Override
    public boolean delProject(Integer projectId) {
        System.out.println("projectId = " + projectId);
        return false;
    }

    /**
     * 添加设备
     * 已存在住户，需要管理住户、设备关系
     *
     * @param deviceId
     * @param projectId
     * @return
     */
    @Override
    public boolean addDevice(String deviceId, Integer projectId) {
        return false;
    }

    /**
     * 删除设备
     * 已存在住户，需要管理住户、设备关系
     *
     * @param deviceId
     * @return
     */
    @Override
    public boolean delDevice(String deviceId) {
        return false;
    }

    /**
     * 添加住户
     * 增加住户所有可用设备 极其房屋关系
     *
     * @param projectHousePersonRel 房屋住户关系
     * @param projectId
     * @return
     */
    @Override
    public boolean addPerson(ProjectHousePersonRel projectHousePersonRel, Integer projectId) {
        return false;
    }

    /**
     * 删除住户
     * 末套房，需要注销人员
     * 多套房，删除人员设备配置，删除人员房屋配置等
     *
     * @param personHouseRelId
     * @return
     */
    @Override
    public boolean delPerson(String personHouseRelId) {
        return false;
    }

    /**
     * 删除某个房屋下的所有住户的云对讲(关闭云对讲)
     *
     * @param houseId
     * @return
     */
    @Override
    public boolean delByHouse(String houseId) {
        return false;
    }

    /**
     * 为某个房间下所有住户添加云对讲（打开云对讲）
     * 已存在的业主不再同步
     *
     * @param houseId
     * @param projectId
     * @return
     */
    @Override
    public boolean addByHouse(String houseId, Integer projectId) {
        return false;
    }
}
