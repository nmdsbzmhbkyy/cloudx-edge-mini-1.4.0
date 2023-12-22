package com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.constant.DeviceTypeConstants;
import com.aurine.cloudx.estate.address.Address;
import com.aurine.cloudx.estate.address.AddressParseUtil;
import com.aurine.cloudx.estate.entity.ProjectHousePersonRel;
import com.aurine.cloudx.estate.entity.ProjectInfo;
import com.aurine.cloudx.estate.service.ProjectDeviceInfoProxyService;
import com.aurine.cloudx.estate.service.ProjectHousePersonRelService;
import com.aurine.cloudx.estate.service.ProjectInfoService;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.IntercomService;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.business.entity.DdCommunityMap;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.business.entity.DdDeviceMap;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.business.entity.DdPersonMap;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.business.service.DdCommunityMapService;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.business.service.DdDeviceMapService;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.business.service.DdPersonMapService;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.entity.constant.DongDongErrorEnum;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.entity.dto.DongDongRespondDTO;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.remote.factory.DongDongRemoteServiceFactory;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoProxyVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
public class IntercomServiceImplByDongDongV2 implements IntercomService {
    @Resource
    private ProjectDeviceInfoProxyService projectDeviceInfoProxyService;

    @Resource
    private ProjectHousePersonRelService projectHousePersonRelService;

    @Resource
    private ProjectInfoService projectInfoService;

    @Resource
    private DdPersonMapService ddPersonMapService;

    @Resource
    private DdCommunityMapService ddCommunityMapService;

    @Resource
    private DdDeviceMapService ddDeviceMapService;

    /**
     * 获取设备可使用人员
     * <p>
     * 获取项目下可使用的设备
     * <p>
     * 获取房间下存在的人员
     * <p>
     * 获取房间下存在的设备
     */


    /**
     * 添加项目
     * 如果项目下存在住户设备，根据规则添加住户和设备的云对讲关系
     * <p>
     * ***** 咚咚的项目没有查重机制，每次添加都是新的项目，需要在本地对项目数据进行管控。清空数据的时候务必不能清空项目映射信息
     *
     * @param projectId
     * @return
     */
    @Override
    public boolean addProject(Integer projectId) {
        //检查咚咚是否已存在当前社区
        ProjectInfo projectInfo = projectInfoService.getProjectInfoVoById(projectId);
        if (ddCommunityMapService.checkCommunityExist(projectId)) {
            log.error("[咚咚云对讲] 项目{}  {} 已对接，请勿重复添加", projectInfo.getProjectId(), projectInfo.getProjectName());
            return false;
        }


        Address address = AddressParseUtil.parseAddressCodeToName(projectInfo.getProvinceCode(), projectInfo.getCityCode(), projectInfo.getCountyCode(), projectInfo.getStreetCode());

        StringBuilder detailAddress = new StringBuilder();

        if (StringUtils.isNotEmpty(address.getProvinceName())) {
            detailAddress.append(address.getProvinceName());
        }

        if (StringUtils.isNotEmpty(address.getCityName())) {
            detailAddress.append(address.getCityName());
        }

        if (StringUtils.isNotEmpty(address.getCountyName())) {
            detailAddress.append(address.getCountyName());
        }

        if (StringUtils.isNotEmpty(address.getStreetName())) {
            detailAddress.append(address.getStreetName());
        }

        //添加社区，并保存关联
        DongDongRespondDTO respondDTO = DongDongRemoteServiceFactory.getInstance(getVer()).addVillage(detailAddress.toString(), projectInfo.getAddress(), projectInfo.getProjectName());
        if (checkResult(respondDTO)) {
            Integer villageId = respondDTO.getRespondObj().getInteger("villageid");
            ddCommunityMapService.addCommunity(projectInfo, villageId);
        }

        //如果项目下存在设备，则操作为启用操作。
        List<ProjectDeviceInfoProxyVo> deviceInfoList = projectDeviceInfoProxyService.listAllDevice(projectId);
        if (CollUtil.isNotEmpty(deviceInfoList)) {
            for (ProjectDeviceInfoProxyVo deviceInfo : deviceInfoList) {
                this.addDevice(deviceInfo, projectId);
            }
        }

        return true;
    }

    /**
     * 删除项目
     * <p>
     * 对方接口删除项目，会连带删除所有的设备数据
     *
     * @param projectId
     * @return
     */
    @Override
    public boolean delProject(Integer projectId) {

        DdCommunityMap communityMap = ddCommunityMapService.getByProjectId(projectId);

        if (communityMap != null) {

            DongDongRespondDTO respondDTO = DongDongRemoteServiceFactory.getInstance(getVer()).delVillage(communityMap.getCommunityid());

            //删除成功与失败逻辑
            if (checkResult(respondDTO)) {
                return ddCommunityMapService.delCommunity(projectId);
            } else {
                if (respondDTO.getErrorEnum() == DongDongErrorEnum.WRONG_VILLAGE_ID) {
                    log.info("[咚咚云对讲] 删除项目 项目{} 在咚咚平台不存在，直接执行删除", projectId);
                    return ddCommunityMapService.delCommunity(projectId);
                } else {
                    log.error("[咚咚云对讲] 删除项目失败 项目{} 出现错误：{}", projectId, respondDTO.getErrorEnum().value);
                }
            }

        } else {
            log.error("[咚咚云对讲] 删除项目失败 项目{} 不存在", projectId);
        }


        return false;
    }


    /**
     * 添加设备
     * 已存在住户，需要管理住户、设备关系
     *
     * @param deviceId
     * @return
     */
    @Override
    public boolean addDevice(String deviceId, Integer projectId) {

        //获取设备对象，并校验设备
        ProjectDeviceInfoProxyVo deviceInfo = projectDeviceInfoProxyService.getVoById(deviceId);
        if (deviceInfo == null) {
            log.error("[咚咚云对讲] 根据设备查询可用住户出错 设备{}不存在", deviceId);
            throw new RuntimeException("设备不存在，请联系管理员");
        }
        return this.addDevice(deviceInfo, projectId);
    }

    /**
     * 添加设备
     * 已存在住户，需要管理住户、设备关系
     *
     * @param deviceInfo
     * @return
     */
    private boolean addDevice(ProjectDeviceInfoProxyVo deviceInfo, Integer projectId) {
        if (!DeviceTypeConstants.GATE_DEVICE.equals(deviceInfo.getDeviceType())
                && !DeviceTypeConstants.LADDER_WAY_DEVICE.equals(deviceInfo.getDeviceType())
                && !DeviceTypeConstants.INDOOR_DEVICE.equals(deviceInfo.getDeviceType())) {
            return true;
        }

        //天鸿 忽略非咚咚设备 临时方案
        if (StringUtils.isEmpty(deviceInfo.getSn()) || deviceInfo.getSn().length() != 20) {
            return true;
        }

        DdCommunityMap communityMap = ddCommunityMapService.getByProjectId(projectId);

        //添加咚咚设备映射
        DongDongRespondDTO respondDTO = DongDongRemoteServiceFactory.getInstance(getVer()).addDevice(communityMap.getCommunityid(), deviceInfo.getSn(), deviceInfo.getDeviceName());
        if (respondDTO.getErrorEnum() == DongDongErrorEnum.SUCCESS) {

            //存储设备到咚咚和本地
            Integer deviceThirdId = respondDTO.getRespondObj().getInteger("deviceid");
            ddDeviceMapService.addDevice(deviceInfo, communityMap, deviceThirdId);

            //查询并存储这台设备可用的的人员、房屋关系
            List<DdPersonMap> personMapList = ddPersonMapService.listOriginPersonMapByDeviceId(deviceInfo);
            personMapList.forEach(e -> e.setDeviceId(deviceThirdId));
            this.savePersonBatch(personMapList, projectId);


        } else {
            //设备在已存在
            log.error("[咚咚云对讲] 设备添加失败 设备 {} {} {}", deviceInfo.getDeviceId(), deviceInfo.getDeviceName(), respondDTO.getErrorEnum().value);
            if (respondDTO.getErrorEnum() == DongDongErrorEnum.ALREADY_ADD) {
                throw new RuntimeException("设备" + deviceInfo.getDeviceName() + "已被添加");
            } else if (respondDTO.getErrorEnum() == DongDongErrorEnum.WRONG_PARAMS) {
                throw new RuntimeException("设备" + deviceInfo.getDeviceName() + "的SN:" + deviceInfo.getSn() + "无效");
            }
        }

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
        //调用删除接口
        DdDeviceMap deviceMap = ddDeviceMapService.getByDeviceId(deviceId);

        if (deviceMap != null) {
            DongDongRespondDTO respondDTO = DongDongRemoteServiceFactory.getInstance(this.getVer()).delDevice(deviceMap.getCommunityid(), deviceMap.getDeviceid());

            //删除本地数据
            if (checkResult(respondDTO)) {
                return ddDeviceMapService.delDevice(deviceMap);
            }
        } else {
            log.info("[咚咚云对讲] 设备 {} 在咚咚映射中不存在，跳过对接业务", deviceId);
        }
        return true;
    }

    /**
     * 添加住户
     * 增加住户所有可用设备 极其房屋关系
     *
     * @param projectHousePersonRel 房屋住户关系
     * @return
     */
    @Override
    public boolean addPerson(ProjectHousePersonRel projectHousePersonRel, Integer projectId) {
        //获取住户可以使用的设备(按照要存储person关系的数据结构)
        List<DdPersonMap> personMapList = ddPersonMapService.listOriginPersonMapByHousePersonRel(projectHousePersonRel);
        return savePersonBatch(personMapList, projectId);
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

        ProjectHousePersonRel housePersonRel = projectHousePersonRelService.getById(personHouseRelId);
        //获取用户可以删除的数据结构

        // 只有用户当前要删除的是最后一套房屋的时候才可以删除公共区口机关联

        //只有用户当前要删除的是本组团最后一套房屋的时候，才可以删除组团区口机关联

        //只有用户当前要删除的是本单元最后一套房屋的时候，才可以删除单元口机关联

        //由于原数据结构为最大冗余结构，直接删除即可。

        List<DdPersonMap> ddPersonMapList = ddPersonMapService.listByHouseAndPerson(housePersonRel.getHouseId(), housePersonRel.getPersonId());

        JSONArray requestJson = createDelMemberBatchJson(ddPersonMapList);

        DongDongRemoteServiceFactory.getInstance(getVer()).delMemberBatch(requestJson);

        //删除数据
        return ddPersonMapService.removeByIds(ddPersonMapList.stream().map(e -> e.getSeq()).collect(Collectors.toList()));
    }

    /**
     * 删除某个房屋下的所有住户的云对讲
     *
     * @param houseId
     * @return
     */
    @Override
    public boolean delByHouse(String houseId) {
        List<DdPersonMap> ddPersonMapList = ddPersonMapService.listByHouse(houseId);

        JSONArray requestJson = createDelMemberBatchJson(ddPersonMapList);

        DongDongRemoteServiceFactory.getInstance(getVer()).delMemberBatch(requestJson);

        return ddPersonMapService.removeByIds(ddPersonMapList.stream().map(e -> e.getSeq()).collect(Collectors.toList()));
    }

    /**
     * 为某个房间下所有住户添加云对讲
     * 已存在的业主不再同步
     *
     * @param houseId
     * @return
     */
    @Override
    public boolean addByHouse(String houseId, Integer projectId) {
        List<DdPersonMap> personMapList = ddPersonMapService.listOriginPersonMapByHouse(houseId);
        return savePersonBatch(personMapList, projectId);
    }


    /**
     * 校验结果方法
     * 可扩产异常处理
     *
     * @param respondDTO
     */
    private boolean checkResult(DongDongRespondDTO respondDTO) {
        if (respondDTO.getErrorEnum() == DongDongErrorEnum.SUCCESS) {
            return true;
        } else {

            //异常处理
            return false;
        }
    }

    /**
     * 将扁平结构的关系数据转换为传输用的树形结构
     *
     * @param ddPersonMapList
     * @return
     */
    private JSONArray createDelMemberBatchJson(List<DdPersonMap> ddPersonMapList) {

        JSONArray deviceRoomMemberArray = new JSONArray();
        JSONObject deviceRoomMemberObj = new JSONObject();
        JSONArray roomMemberArray = new JSONArray();
        JSONObject roomMemberObj = new JSONObject();

        //按照设备分组
        List<Integer> deviceThirdIdList = ddPersonMapList.stream().map(e -> e.getDeviceId()).collect(Collectors.toList());

        for (Integer deviceThirdId : deviceThirdIdList) {
            deviceRoomMemberObj = new JSONObject();
            roomMemberArray = new JSONArray();

            for (DdPersonMap personMap : ddPersonMapList) {
                if (personMap.getDeviceId().intValue() == deviceThirdId.intValue()) {
                    roomMemberObj = new JSONObject();
                    roomMemberObj.put("roomid", personMap.getRoomId());
                    roomMemberObj.put("memberid", personMap.getMemberId());

                    roomMemberArray.add(roomMemberObj);
                }
            }

            deviceRoomMemberObj.put("rmids", roomMemberArray);
            deviceRoomMemberObj.put("deviceid", deviceThirdId);
            deviceRoomMemberArray.add(deviceRoomMemberObj);
        }


        return deviceRoomMemberArray;
    }

    /**
     * 批量存储 人员映射 到 咚咚 和 本地映射表
     * 到咚咚为同步逐个存储
     * 咚咚 人员-房屋 如果已存在，将覆盖原有数据
     *
     * @param personMapList
     * @return
     */
    private boolean savePersonBatch(List<DdPersonMap> personMapList, Integer projectId) {

        //init
        Integer roomId = 0;
        Integer memberId = 0;
        DongDongRespondDTO respondDTO = null;
        List<DdPersonMap> savePersonMapList = new ArrayList<>();
        JSONObject memberIdObj = null;

        if (CollUtil.isNotEmpty(personMapList)) {
            for (DdPersonMap personMap : personMapList) {

                //逐个保存到咚咚
                respondDTO = DongDongRemoteServiceFactory.getInstance(getVer()).addMember(personMap.getDeviceId(), personMap.getRomNumber(), personMap.getPersonName(), personMap.getIdNumber(), personMap.getMobilePhone());

                if (checkResult(respondDTO)) {
                    //存储住户关联
                    memberIdObj = respondDTO.getRespondObj().getJSONObject("memberid");
                    roomId = memberIdObj.getInteger("roomid");
                    memberId = memberIdObj.getInteger("memberid");

                    personMap.setProjectId(projectId);
                    personMap.setRoomId(roomId);
                    personMap.setMemberId(memberId);
                    personMap.setTenantId(1);
                    savePersonMapList.add(personMap);
                } else {
                    //失败处理
                }

            }
            return ddPersonMapService.saveBatch(savePersonMapList);
        } else {
            return true;
        }
    }


    /**
     * 获取版本
     *
     * @return
     */
    @Override
    public String getVersion() {
        return VersionEnum.V2.code;
    }

    public VersionEnum getVer() {
        return VersionEnum.V2;
    }

    /**
     * 获取平台类型
     *
     * @return
     */
    @Override
    public String getPlatform() {
        return PlatformEnum.INTERCOM_DONGDONG.code;
    }


}
