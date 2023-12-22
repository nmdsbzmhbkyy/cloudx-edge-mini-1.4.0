package com.aurine.cloudx.open.origin.service;

import com.aurine.cloudx.open.common.entity.vo.DeviceInfoVo;
import com.aurine.cloudx.open.origin.constant.enums.CertmediaTypeEnum;
import com.aurine.cloudx.open.origin.constant.enums.DeviceReplaceResultEnum;
import com.aurine.cloudx.open.origin.dto.ProjectParkingDeviceInfoDto;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceInfo;
import com.aurine.cloudx.open.origin.entity.ProjectNoticeDevice;
import com.aurine.cloudx.open.origin.vo.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (WebProjectDeviceInfoService)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/12/8 15:31
 */
public interface ProjectDeviceInfoService extends IService<ProjectDeviceInfo> {

    /**
     * 分页查询设备
     *
     * @param page
     * @param projectDeviceInfoPageFormVo
     * @return
     */
    Page<ProjectDeviceInfoPageVo> pageVo(Page page, ProjectDeviceInfoPageFormVo projectDeviceInfoPageFormVo);


    /**
     * <p>查询所有和指定设备ID存在参数重复异常的设备列表（包括指定设备）</p>
     *
     * @param
     * @return
     * @author: 王良俊
     */
    List<ProjectDeviceInfoAbnormalVo> listAbnormalDevice(String deviceId);

    /**
     * 根据设备类型列表及楼栋单元id查询获取设备
     *
     * @param projectDeviceInfoFormVo
     * @return
     */
    List<ProjectDeviceInfo> findByType(ProjectDeviceInfoFormVo projectDeviceInfoFormVo);

    /**
     * 根据设备类型列表及楼栋单元id查询获取设备
     *
     * @param projectDeviceInfoFormVo
     * @return
     */
    List<ProjectIotDeviceInfoVo> findIotByType(ProjectDeviceInfoFormVo projectDeviceInfoFormVo);

    /**
     * 根据设备id获取设备信息视图
     *
     * @param id
     * @return
     */
    ProjectDeviceInfoVo getProjectDeviceInfoById(String id);

    /**
     * <p>
     * 根据设备ID获取设备编号前缀
     * </p>
     *
     * @param deviceId 设备ID
     * @return 设备编号楼栋单元房屋编号部分
     * @author: 王良俊
     */
    String getDeviceNoPreByDeviceId(String deviceId);


    /**
     * 根据组团id获取区口机
     *
     * @param deviceEntityId
     * @return
     */
    List<ProjectDeviceInfo> getListByDeviceEntityId(String deviceEntityId);

    /**
     * 根据id获取到该巡检点绑定的设备列表
     *
     * @param pointId 巡检点id
     * @return
     * @author 王良俊
     */
    List<ProjectPointDeviceVo> listDeviceByPointId(String pointId);

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

    /**
     * 通过第三方ID,获取设备
     *
     * @param thirdpartyCode
     * @return
     * @author: 王伟
     * @since 2020-08-07
     */
    ProjectDeviceInfo getByThirdPartyCode(String thirdpartyCode);

//    /**
//     * 获取直播流地址
//     *
//     * @param deviceInfo
//     * @return
//     */
//    R<String> getLiveUrl(String deviceInfo);
//
//    /**
//     * 获取直播流地址
//     *
//     * @param
//     * @return
//     */
//    String getVideoUrl(String deviceId, Long startTime, Long endTime);


    /**
     * 获取离线设备数量
     *
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
     * @return
     */
    Page<ProjectDeviceInfoPageVo> pageDeviceParam(Page page, ProjectDeviceInfoPageFormVo projectDeviceInfoPageFormVo);

    /**
     * 查询不支持富文本的门禁设备列表
     *
     * @param projectDeviceInfoFormVo
     * @return
     */
    List<ProjectDeviceInfo> findRichByType(ProjectDeviceInfoFormVo projectDeviceInfoFormVo);

    /**
     * 根据设备sn查询统计设备
     *
     * @param sn
     * @return
     */
    Integer countBySn(String sn, String deviceId);

//    /**
//     * 导入excel表格
//     *
//     * @param file
//     * @return
//     */
//    void importExcel(MultipartFile file, DeviceExcelEnum deviceExcelEnum, Integer projectId) throws IOException;

    /**
     * 保存设备
     *
     * @param deviceInfo
     * @return
     */
    boolean saveVo(ProjectDeviceInfoVo deviceInfo);

    /**
     * <p>
     * 根据旧设备的设备ID和新的设备SN对设备进行替换并继承原设备的服务
     * </p>
     *
     * @param deviceId 旧设备的设备ID
     * @param sn       新设备的设备SN
     * @return 是否替换成功
     * @author: 王良俊
     */
    DeviceReplaceResultEnum replaceDevice(String deviceId, String sn);

    /**
     * 更新设备
     *
     * @param projectDeviceInfo
     * @return
     */
    boolean updateVo(ProjectDeviceInfoVo projectDeviceInfo);


//    /**
//     * <p>
//     * 设置设备参数（非基本参数）
//     * 对应的json结构应该如下 ↓
//     *
//     * <br> {
//     * <br>
//     * <br>    "netparam": {
//     * <br>        "faceServer": "0.0.0.0",
//     * <br>        "elevator": "0.0.0.0",
//     * <br>        "netmask": "255.255.255.0",
//     * <br>        "managerIp": "0.0.0.0",
//     * <br>        "dns": "58.22.96.66",
//     * <br>        "rtspServer": "0.0.0.0",
//     * <br>        "centerIp": "0.0.0.0",
//     * <br>        "ipAddr": "192.168.1.251",
//     * <br>        "gateway": "192.168.1.1"
//     * <br>    },
//     * <br>    "faceParam": {
//     * <br>        "securityLevel": 1,
//     * <br>        "livenessEnable": 0,
//     * <br>        "faceEnable": 1,
//     * <br>        "inductionEnable": 0
//     * <br>    },
//     * <br>    "volumeParam": {
//     * <br>        "keyTone": 1,
//     * <br>        "talkVolume": 5,
//     * <br>        "tipTone": 0,
//     * <br>        "mediaMute": 1
//     * <br>    }
//     * <br>}
//     *
//     * </p>
//     *
//     * @param paramsNode jackson的json对象节点
//     * @param deviceId   所要设置参数的设备ID（不是第三方的设备ID -> devId）
//     * @author: 王良俊
//     */
//    List<ProjectDeviceParamSetResultVo> setDeviceParam(ObjectNode paramsNode, String deviceId);
//
//    /**
//     * <p>
//     * 设置多台设备的参数
//     * json格式如下
//     * <br> {
//     * <br>
//     * <br>    "netparam": {
//     * <br>        "faceServer": "0.0.0.0",
//     * <br>        "elevator": "0.0.0.0",
//     * <br>        "netmask": "255.255.255.0",
//     * <br>        "managerIp": "0.0.0.0",
//     * <br>        "dns": "58.22.96.66",
//     * <br>        "rtspServer": "0.0.0.0",
//     * <br>        "centerIp": "0.0.0.0",
//     * <br>        "ipAddr": "192.168.1.251",
//     * <br>        "gateway": "192.168.1.1"
//     * <br>    },
//     * <br>    "faceParam": {
//     * <br>        "securityLevel": 1,
//     * <br>        "livenessEnable": 0,
//     * <br>        "faceEnable": 1,
//     * <br>        "inductionEnable": 0
//     * <br>    },
//     * <br>    "volumeParam": {
//     * <br>        "keyTone": 1,
//     * <br>        "talkVolume": 5,
//     * <br>        "tipTone": 0,
//     * <br>        "mediaMute": 1
//     * <br>    }
//     * <br>}
//     *
//     * </p>
//     *
//     * @param deviceIdList 要进行参数设置的设备集合（至少要有设备ID和设备第三方编码thirdPartyCode）
//     * @param paramsNode   json对象节点同setDeviceParam接口
//     * @author: 王良俊
//     */
//    DevicesResultVo setDevicesParam(ObjectNode paramsNode, List<String> deviceIdList, List<String> serviceIdList);

    /**
     * <p>
     *
     * </p>
     *
     * @param isReset              是否是重配
     * @param paramSetResultVoList 需要进行处理的参数设置结果集合
     * @return 参数设置是否存在设置失败的参数json对象节点同setDeviceParam接口
     * @author: 王良俊
     */
    boolean handleDeviceParamSetResult(List<ProjectDeviceParamSetResultVo> paramSetResultVoList, boolean isReset);

    /**
     * 删除设备
     *
     * @param id
     * @return
     */
    boolean removeDevice(String id);


    /**
     * 批量删除设备
     *
     * @param ids
     * @return
     */
    boolean removeAll(List<String> ids);

//    /**
//     * 获取导入失败文件列表
//     *
//     * @param name
//     */
//    void errorExcel(Integer projectId, String name, HttpServletResponse httpServletResponse) throws IOException;
//
//    void modelExcel(String code, HttpServletResponse httpServletResponse, Integer projectId) throws IOException;

    /**
     * 保存设备
     *
     * @param deviceInfo
     * @return
     */
    public ProjectDeviceInfoVo saveDeviceVo(ProjectDeviceInfoVo deviceInfo);

    /**
     * 保存设备
     *
     * @param deviceInfo
     * @return
     */
    ProjectDeviceInfoVo saveDeviceVoAndGetParam(ProjectDeviceInfoVo deviceInfo);

    /**
     * 保存设备参数
     *
     * @param deviceId
     */
    public void initDeviceParamData(String deviceId);

//    /**
//     * 获取咚咚设备列表
//     */
//    List<ProjectDeviceInfo> getDdDeviceList();

    /**
     * 修改状态
     *
     * @param deviceId
     * @param name
     * @return
     */
    boolean updateByStatus(String deviceId, String name);

    /**
     * 设备关联
     *
     * @param projectDeviceMonitorRelVo
     * @return
     */
    R putDeviceRelevance(ProjectDeviceMonitorRelVo projectDeviceMonitorRelVo);


    /**
     * 查询关联的设备
     *
     * @param deviceId
     * @return
     */
    List<ProjectDeviceInfoPageVo> getMonitoring(String deviceId);

    /**
     * 报警主机添加设备调用同步接口
     *
     * @param projectDeviceInfo
     */
    boolean reacquireDefenseArea(ProjectDeviceInfo projectDeviceInfo);

    /**
     * 查询设备品牌型号
     *
     * @param deviceTypeId
     * @return
     */
    List<ProjectDeviceProductNameVo> getDeviceBrand(String deviceTypeId);


//    /**
//     * 智能路灯添加控制器IMEL
//     *
//     * @param projectDeviceLightIMELVo
//     * @return
//     */
//    boolean saveLightingIMEL(ProjectDeviceLightIMELVo projectDeviceLightIMELVo);
//
//    /**
//     * 查询控制器IMEL
//     *
//     * @param deviceType
//     * @return
//     */
//    List<ProjectDeviceProductNameVo> getControllerIMEL(String deviceType);
//
//    /**
//     * <p>
//     * 路灯开关灯、亮度调节等
//     * </p>
//     *
//     * @param deviceStatus 路灯设备状态对象
//     * @return 是否设置成功
//     * @author: 王良俊
//     */
//    boolean streetLightControl(StreetLightDeviceStatus deviceStatus, Integer projectId);

    /**
     * 根据设备Sn获取设备产品id
     *
     * @return
     * @author: 谢泽毅
     * @since: 2021-08-30
     */
    String getProductIdByDeviceSn(String deviceSn);

    /**
     * <p>根据介质类型和设备ID判断该设备是否具有使用对应介质的能力</p>
     *
     * @param certType 介质类型枚举
     * @param deviceId 设备ID
     * @return 是否有对应能力
     * @author: 王良俊
     */
    boolean checkCapabilityByCertType(String deviceId, CertmediaTypeEnum certType);

    /**
     * <p>检查设备是否有人脸能力</p>
     *
     * @param deviceId 设备ID
     * @return 是否有能力
     * @author: 王良俊
     */
    boolean checkDeviceHasFaceCapability(String deviceId);

    /**
     * <p>检查设备是否有门禁卡能力</p>
     *
     * @param deviceId 设备ID
     * @return 是否有能力
     * @author: 王良俊
     */
    boolean checkDeviceHasCardCapability(String deviceId);


    /**
     * 根据Id查询设备的楼栋和单元
     *
     * @param deviceId
     * @return
     */
    ProjectDeviceBuildingUnitNameVo getBuildingAndUnitByDeviceId(String deviceId);


    /**
     * 根据设备ID查询所属模块
     *
     * @param deviceId
     * @return
     */
    String getModule(String deviceId);

    /**
     * 查询异常的设备数
     *
     * @param type
     * @param deviceRegionId
     * @return
     */
    Integer getAbnormalCount(String type, String deviceRegionId);

    ProjectNoticeDevice getDeviceNameAndBuildingNameAndUnitName(String deviceId);

    /**
     * 分页查询
     *
     * @param page
     * @param vo
     * @return
     */
    Page<DeviceInfoVo> page(Page page, DeviceInfoVo vo);

    Page<ProjectParkingDeviceInfoDto> pageDeviceInfo(Page<ProjectParkingDeviceInfoDto> page, @Param("laneId")String laneId, @Param("query") ProjectParkingDeviceInfoDto projectParkingDeviceInfoDto);

    ProjectParkingDeviceInfoDto getByDeviceId(@Param("deviceId")String deviceId);

}
