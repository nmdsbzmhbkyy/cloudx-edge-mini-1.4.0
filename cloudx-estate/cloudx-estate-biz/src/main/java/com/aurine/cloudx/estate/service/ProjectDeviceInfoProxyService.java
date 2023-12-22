package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoProxyVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>设备信息代理服务，用于扩展设备信息服务接口，主要服务于门禁方案</p>
 *
 * @ClassName: ProjectDeviceInfoProxyService
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/21 12:43
 * @Copyright:
 */
public interface ProjectDeviceInfoProxyService extends IService<ProjectDeviceInfo> {

    /**
     * 依照设备第三方编号新增或删除设备
     *
     * @param deviceInfoProxyVo
     * @return
     */
    boolean saveOrUpdateDeviceByThirdPartyCode(ProjectDeviceInfoProxyVo deviceInfoProxyVo);

    /**
     * 获取全部
     *
     * @return
     */
    List<ProjectDeviceInfoProxyVo> listAllDevice(Integer projectId);

    /**
     * <p>
     * 根据楼栋ID数组获取这些楼栋下的设备
     * </p>
     */
    public List<String> listByBuildingIdList(List<String> buildingIdList);


    /**
     * 获取全部区口机
     *
     * @return
     */
    List<ProjectDeviceInfo> listAllGateDevice();

    /**
     * 获取全部公共，以及楼栋所在的组团的区口机
     *
     * @return
     */
    List<ProjectDeviceInfo> listPublicGateAndGateByFrameDevice(String buildingId);

    /**
     * 根据楼栋ID或单元ID获取梯口机
     *
     * @param buildingId
     * @param unitId
     * @return
     */
    List<ProjectDeviceInfo> listLadderDevice(String buildingId, String unitId);

    /**
     * 根据第三方ID,获取设备信息
     *
     * @param thirdpartyCode
     * @return
     */
    ProjectDeviceInfoProxyVo getByThirdPartyCode(String thirdpartyCode);
    /**
     * 根据第三方ID,获取设备信息
     *
     * @param thirdpartyNo
     * @return
     */
    ProjectDeviceInfoProxyVo getByThirdPartyNo(String thirdpartyNo);

    /**
     * 根据第三方ID,获取设备信息
     *
     * @param thirdPartyCode
     * @return
     */
    Integer countThirdPartyCode(String thirdPartyCode);

    /**
     * 根据第三方ID,获取设备信息
     *
     * @param sn
     * @return
     */
    Integer countSn(String sn,String deviceId);


    /**
     * 根据第三方ID,获取设备信息
     *
     * @param deviceSn
     * @return
     */
    ProjectDeviceInfoProxyVo getByDeviceSn(String deviceSn);

    /**
     * 根据第三方ID,获取设备信息
     *
     * @param deviceId
     * @return
     */
    ProjectDeviceInfoProxyVo getVoById(String deviceId);

    /**
     * 根据设备的第三方编号更新设备状态
     *
     * @param thirdpartyCode
     * @param status
     * @return
     * @author: 王伟
     * @since 2020-08-07
     */
    boolean updateStatusByThirdPartyCode(String thirdpartyCode, String status);


    /**
     * 根据设备的SN编号更新设备状态
     *
     * @param deviceSn
     * @param status
     * @return
     * @author: 王伟
     * @since 2020-08-07
     */
    boolean updateStatusByDeviceSn(String deviceSn, String status);

}
