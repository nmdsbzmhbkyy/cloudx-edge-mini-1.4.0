package com.aurine.cloudx.estate.service;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.constant.enums.CertmediaTypeEnum;
import com.aurine.cloudx.estate.constant.enums.DeviceExcelEnum;
import com.aurine.cloudx.estate.constant.enums.DeviceReplaceResultEnum;
import com.aurine.cloudx.estate.dto.OpenApiProjectDeviceInfoManageDto;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectNoticeDevice;
import com.aurine.cloudx.estate.entity.ProjectRightDevice;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.param.entity.StreetLightDeviceStatus;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pig4cloud.pigx.admin.api.entity.SysDictItem;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
     * 根据设备类型列表及楼栋单元id查询获取设备（分页）
     *
     * @param projectDeviceInfoFormVo
     * @return
     */
    Page<ProjectDeviceInfoThirPartyVo> findPageByType(Page page,ProjectDeviceInfoFormVo projectDeviceInfoFormVo);

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

    /**
     * 获取直播流地址
     *
     * @param deviceInfo
     * @return
     */
    R<String> getLiveUrl(String deviceInfo);

    /**
     * rtsp转码流
     *
     * @param deviceInfo
     * @return
     */
    R<JSONObject> rtspToUrl(String rtsp);

    /**
     * 获取直播流地址
     *
     * @param
     * @return
     */
    String getVideoUrl(String deviceId, Long startTime, Long endTime);


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
     * 查询支持人脸的门禁设备列表
     *
     * @param
     * @return
     */
    List<ProjectDeviceInfo> findFaceCapacityDoorDevice();

    /**
     * 根据设备sn查询统计设备
     *
     * @param sn
     * @return
     */
    Integer countBySn(String sn, String deviceId);

    /**
     * 导入excel表格
     *
     * @param file
     * @return
     */
    void importExcel(MultipartFile file, DeviceExcelEnum deviceExcelEnum, Integer projectId) throws IOException;

    /**
     * 导入excel表格
     *
     * @param file
     * @return
     */
    void importExcel(MultipartFile file, DeviceExcelEnum deviceExcelEnum, Integer projectId, boolean isCover) throws IOException;

    /**
     * 导入excel表格，校验code是否存在
     *
     * @param file
     * @return
     */
    List<String> verifyCodeWithExcel(MultipartFile file, DeviceExcelEnum deviceExcelEnum) throws IOException;

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


    /**
     * <p>
     * 设置设备参数（非基本参数）
     * 对应的json结构应该如下 ↓
     *
     * <br> {
     * <br>
     * <br>    "netparam": {
     * <br>        "faceServer": "0.0.0.0",
     * <br>        "elevator": "0.0.0.0",
     * <br>        "netmask": "255.255.255.0",
     * <br>        "managerIp": "0.0.0.0",
     * <br>        "dns": "58.22.96.66",
     * <br>        "rtspServer": "0.0.0.0",
     * <br>        "centerIp": "0.0.0.0",
     * <br>        "ipAddr": "192.168.1.251",
     * <br>        "gateway": "192.168.1.1"
     * <br>    },
     * <br>    "faceParam": {
     * <br>        "securityLevel": 1,
     * <br>        "livenessEnable": 0,
     * <br>        "faceEnable": 1,
     * <br>        "inductionEnable": 0
     * <br>    },
     * <br>    "volumeParam": {
     * <br>        "keyTone": 1,
     * <br>        "talkVolume": 5,
     * <br>        "tipTone": 0,
     * <br>        "mediaMute": 1
     * <br>    }
     * <br>}
     *
     * </p>
     *
     * @param paramsNode jackson的json对象节点
     * @param deviceId   所要设置参数的设备ID（不是第三方的设备ID -> devId）
     * @author: 王良俊
     */
    List<ProjectDeviceParamSetResultVo> setDeviceParam(ObjectNode paramsNode, String deviceId);

    /**
     * <p>
     * 设置多台设备的参数
     * json格式如下
     * <br> {
     * <br>
     * <br>    "netparam": {
     * <br>        "faceServer": "0.0.0.0",
     * <br>        "elevator": "0.0.0.0",
     * <br>        "netmask": "255.255.255.0",
     * <br>        "managerIp": "0.0.0.0",
     * <br>        "dns": "58.22.96.66",
     * <br>        "rtspServer": "0.0.0.0",
     * <br>        "centerIp": "0.0.0.0",
     * <br>        "ipAddr": "192.168.1.251",
     * <br>        "gateway": "192.168.1.1"
     * <br>    },
     * <br>    "faceParam": {
     * <br>        "securityLevel": 1,
     * <br>        "livenessEnable": 0,
     * <br>        "faceEnable": 1,
     * <br>        "inductionEnable": 0
     * <br>    },
     * <br>    "volumeParam": {
     * <br>        "keyTone": 1,
     * <br>        "talkVolume": 5,
     * <br>        "tipTone": 0,
     * <br>        "mediaMute": 1
     * <br>    }
     * <br>}
     *
     * </p>
     *
     * @param deviceIdList 要进行参数设置的设备集合（至少要有设备ID和设备第三方编码thirdPartyCode）
     * @param paramsNode   json对象节点同setDeviceParam接口
     * @author: 王良俊
     */
    DevicesResultVo setDevicesParam(ObjectNode paramsNode, List<String> deviceIdList, List<String> serviceIdList);

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

    /**
     * 获取导入失败文件列表
     *
     * @param name
     */
    void errorExcel(Integer projectId, String name, HttpServletResponse httpServletResponse) throws IOException;

    void modelExcel(String code, HttpServletResponse httpServletResponse, Integer projectId) throws IOException;

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

    /**
     * 获取咚咚设备列表
     */
    List<ProjectDeviceInfo> getDdDeviceList();

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

    /**
     * 查询设备品牌型号
     *
     * @param deviceTypeId
     * @return
     */
    ProjectDeviceProductNameVo getDeviceBrand(String deviceTypeId, String attrVal);


    /**
     * 智能路灯添加控制器IMEL
     *
     * @param projectDeviceLightIMELVo
     * @return
     */
    boolean saveLightingIMEL(ProjectDeviceLightIMELVo projectDeviceLightIMELVo);

    /**
     * 查询控制器IMEL
     *
     * @param deviceType
     * @return
     */
    List<ProjectDeviceProductNameVo> getControllerIMEL(String deviceType);

    /**
     * <p>
     * 路灯开关灯、亮度调节等
     * </p>
     *
     * @param deviceStatus 路灯设备状态对象
     * @return 是否设置成功
     * @author: 王良俊
     */
    boolean streetLightControl(StreetLightDeviceStatus deviceStatus, Integer projectId);

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
    * @Author 黄健杰
    * @Description 设置账号
    * @Date  2022/1/28
    * @Param
    * @return
    **/
    boolean setAccount(ProjectDeviceInfoVo deviceInfo);
    /**
    * @Author 黄健杰
    * @Description 设置聚集报警规则
    * @Date  2022/1/28
    * @Param
    * @return
    **/
    boolean setGatherAlarmRule(String deviceId, ProjectDeviceGatherAlarmRuleVo ruleVo);

    boolean setGatherAlarmRuleSingle(String deviceId, ProjectDeviceGatherAlarmRuleVo ruleVo);

    /**
    * @Author 黄健杰
    * @Description 修改设备别名
    * @Date  2022/1/28
    * @Param
    * @return
    **/
    boolean setDeviceAlias(String deviceId, String alias);
    /**
    * @Author 黄健杰
    * @Description 关联子设备
    * @Date  2022/1/28
    * @Param
    * @return
    **/
    List<ProjectDeviceInfoVo> relateChildDev(String id);
    /**
     * @Author 黄健杰
     * @Description 获取子设备
     * @Date  2022/1/28
     * @Param
     * @return
     **/
    List<ProjectDeviceInfoVo> getChildDev(String id, String deviceAlias, String ipv4);
    /**
    * @Author hjj
    * @Description 根据mac获取设备网络配置信息
    * @Date  2022/6/10
    * @Param
    * @return
    **/
    DeviceNetworkInfoVo getDeviceNetWorkInfo(String mac);
    /**
     * @Author hjj
     * @Description 根据mac更新设备激活状态
     * @Date  2022/6/10
     * @Param
     * @return
     **/
    Boolean updateConfigured(String mac);

    /**
     * @Author chenz
     * @Description 导出excel
     * @Date  2022/4/8
     * @Param
     * @return
     **/
    void exportExcel(String type,HttpServletResponse httpServletResponse );

    /**
     * <p>
     * 获取车道一体机设备列表
     * </p>
     *
     * @param
     * @author: 王良俊
     * @return
     */
    List<ProjectVehicleBarrierDeviceInfoVo> listVehicleBarrierDevice(VehicleBarrierDeviceQuery query);

    /**
     * <p>
     * 保存车道一体机设备信息
     * </p>
     *
     * @param projectDeviceInfo 设备信息对象
     * @author: 王良俊
     * @return 带平台设备ID的设备信息对象
     */
    R saveVehicleBarrierDevice(ProjectDeviceInfo projectDeviceInfo);

    /**
     * <p>
     *  车场设备参数设置（单台）
     * </p>
     *
     * @param
     * @author: 王良俊
     * @return
     */
    R setParkingDeviceParam(ParkingParamInfoVo paramInfoVo);

    R<DevicesResultVo> setParkingDevicesParam(ParkingParamInfoVo paramInfoVo);

    List<ProjectDeviceLiftVo> getLiftsWithFloor(@RequestParam(required = false) String liftPlanId, @RequestParam(required = false) String personType, @RequestParam(required = false) String personId);


    List<SysDictItem> AlarmDeviceTypeList();
    /**
    * @Author hjj
    * @Description 获取乘梯识别终端
    * @Date  2022/7/6
    * @Param
    * @return
    **/
    List<String> childDeviceIdByLift(List<String> LiftIds);

    /**
     * <p>召梯</p>
     *
     * @param houseId 设备ID
     * @return 指令下发是否成功
     * @author: hjj
     */
    R<Boolean> callElevatorByHouseId(String houseId, String callType);


    /**
     * 下发凭证与房屋的关系
     * *
     * @param projectDeviceInfo
     * @param projectRightDevice
     */
    void sendCertAndHouseRelation(ProjectDeviceInfo projectDeviceInfo, ProjectRightDevice projectRightDevice);

    List<String> getLiftLadderList(String personId);
    /**
     * 根据设备id获取设备管理变更通知dto()
     * @param deviceId 设备id
     * @param operateType 操作类型 参考DeviceManageOperateTypeEnum
     * @return  设备管理变更通知dto
     */
    OpenApiProjectDeviceInfoManageDto getDeviceInfoManageDtoByDeviceId(String deviceId,String operateType);
    /**
     * 推送设备管理变更通知到openV2
     * @param deviceInfoManageDt 通知对象
     * @remark 注意在设备操作接口事务提交前调用，否则可能出现设备删除操作后设备信息查询不到的问题)
     * @return
     */
    void sendDeviceManageChangeNotice(OpenApiProjectDeviceInfoManageDto deviceInfoManageDt);

    /**
     * 根据驱动名称获取驱动
     * @param deviceName
     * @return
     */
    ProjectDeviceInfo getDriverByDeviceName(String deviceName);
}
