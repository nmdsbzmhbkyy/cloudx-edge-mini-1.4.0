

package com.aurine.cloudx.estate.thirdparty.module.intercom.platform;

import com.aurine.cloudx.estate.entity.ProjectHousePersonRel;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.BaseService;

/**
 * 云对讲 用户 设备 房屋 关系维护接口
 *
 * @ClassName: GateDeviceService
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-11-16 14:43
 * @Copyright:
 */
public interface IntercomService extends BaseService {


    /**
     * 添加项目
     * 如果项目下存在住户设备，根据规则添加住户和设备的云对讲关系
     *
     * 在项目过审、项目启用增值服务后调用
     *
     * @param projectId
     * @return
     */
    boolean addProject(Integer projectId);

    /**
     * 删除项目
     * 如果项目下存在设备、住户，这按照关系、房屋、住户、设备、项目顺序依次清空
     *
     * 在删除项目前、项目停用增值服务后调用
     *
     * @param projectId
     * @return
     */
    boolean delProject(Integer projectId);

    /**
     * 添加设备
     * 已存在住户，需要管理住户、设备关系
     *
     * 在项目添加设备后调用（事务外）
     *
     * @param deviceId
     * @return
     */
    boolean addDevice(String deviceId, Integer projectId);

    /**
     * 删除设备
     * 已存在住户，需要管理住户、设备关系
     *
     * 在项目删除设备前调用
     *
     * @param deviceId
     * @return
     */
    boolean delDevice(String deviceId);

    /**
     * 添加住户
     * 增加住户所有可用设备 极其房屋关系
     *
     * 在项目添加住户后调用
     *
     * @param projectHousePersonRel 房屋住户关系
     * @return
     */
    boolean addPerson(ProjectHousePersonRel projectHousePersonRel, Integer projectId);

    /**
     * 删除住户
     * 末套房，需要注销人员
     * 多套房，删除人员设备配置，删除人员房屋配置等
     *
     * 在删除住户前调用
     *
     * @param personHouseRelId
     * @return
     */
    boolean delPerson(String personHouseRelId);


    /**
     * 删除某个房屋下的所有住户的云对讲(关闭云对讲)
     *
     * @param houseId
     * @return
     */
    boolean delByHouse(String houseId);

    /**
     * 为某个房间下所有住户添加云对讲（打开云对讲）
     * 已存在的业主不再同步
     *
     * @param houseId
     * @param projectId
     * @return
     */
    boolean addByHouse(String houseId, Integer projectId);


}
