

package com.aurine.cloudx.estate.thirdparty.module.device.platform;

import com.aurine.cloudx.estate.dto.DeviceRegDto;
import com.aurine.cloudx.estate.dto.DeviceStatusDto;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgeDeviceInfoDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgeRespondDTO;
import com.aurine.cloudx.estate.vo.MediaAdInfoVo;
import com.aurine.cloudx.estate.vo.ProjectDeviceGatherAlarmRuleVo;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoProxyVo;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * 设备基础 接口
 * 定义设备抽象出来的共有功能，如添加、修缮，删除，订阅，同步等
 *
 * @ClassName: GateDeviceService
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-07-28 15:29
 * @Copyright:
 */
public interface DeviceService extends BaseService {

    /**
     * 新增设备
     *
     * @param device
     * @return
     */
    public String addDevice(ProjectDeviceInfoProxyVo device);

    /**
     * 新增设备
     *
     * @param device
     * @return
     */
    public String addDevice(ProjectDeviceInfoProxyVo device, String productKey);

    /**
     * 批量新增设备
     * 新增设备时应根据设备类型进行分组传输，以确保不同的设备类型，调用正确的接口平台和版本
     *
     * @param deviceList
     * @return
     */
    public String[] addDeviceBatch(List<ProjectDeviceInfoProxyVo> deviceList);

    /**
     * 批量新增设备
     * 新增设备时应根据设备类型进行分组传输，以确保不同的设备类型，调用正确的接口平台和版本(和addDeviceBatch(List<ProjectDeviceInfoProxyVo> deviceList)相比除了多了productKey其他完全一样)
     *
     * @param deviceList
     * @return
     */
    public String[] addDeviceBatch(List<ProjectDeviceInfoProxyVo> deviceList, String productKey);

    /**
     * 修改设备
     *
     * @param device
     * @return
     */
    public boolean updateDevice(ProjectDeviceInfoProxyVo device);

    /**
     * 根据serviceID获取设备指定参数
     *
     * @param serviceId 设备产品服务ID
     * @param thirdpartyCode 设备第三方编码（第三方ID）
     * @param deviceId 设备ID 本地系统ID
     */
     boolean getDeviceParam(String serviceId, String thirdpartyCode, String deviceId);

    /**
     * <p>
     * 设置设备参数
     * <p>
     * 对应的json结构如下 ↓
     * <br> {
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
     * @param paramNode 参数对象
     * @author: 王良俊
     */
     boolean setDeviceParameters(ObjectNode paramNode, String deviceId, String thirdpartyCode);

    /**
    * <p>
    * 获取设备信息
    * </p>
    *
    * @param deviceId 设备ID(系统)
    * @param thirdpartyCode 第三方设备ID
    * @author: 王良俊
    */
     boolean getDeviceInfo(String deviceId, String thirdpartyCode);

    /**
     * 删除设备
     *
     * @param deviceId
     * @return
     */
    public boolean delDevice(String deviceId);

    /**
     * 重启
     *
     * @param deviceId 设备id
     * @return
     */
    boolean reboot(String deviceId);

    /**
     * 召梯
     * @param deviceId
     * @param roomCode 01011201即01栋01单元1201房
     * @param liftDirect 方向 0向上，1向下
     * @return
     */
    boolean callElevator(String deviceId, String roomCode, String floor, String liftDirect);

    /**
     * 重启
     *
     * @param deviceId 设备id
     * @return
     */
    boolean reset(String deviceId);

    /**
    * <p>
    * 设置设备管理员密码
    * </p>
    *
    * @param deviceId 设备ID（系统，非第三方）
    * @param password 管理员密码
    * @author: 王良俊
    */
    boolean setPwd(String deviceId, String password);


    /**
     * 同步产品
     *
     * @return
     */
    boolean syncProduces(int projectId, int tenantId);


    /**
     * 订阅消息
     *
     * @return
     */
    boolean subscribe(String deviceType, int projectId, int tenantId);


    /***
     * 同步设备
     * @return
     */
    boolean syncDecvice(String deviceType, int projectId, int tenantId);


     boolean dealAlarm(String eventId);

     /**
      * <p>下发媒体广告</p>
      *
      * @param deviceId 设备ID
      * @return 下发是否成功
      * @author: 王良俊
      */
     boolean sendMediaAd(String deviceId, MediaAdInfoVo media);


     /**
      * <p>删除设备上指定媒体ID的播放列表</p>
      *
      * @param adSeq 广告ID
      * @param deviceId 设备ID
      * @return 清空结果
      * @author: 王良俊
      */
    boolean cleanMediaAd(Long adSeq, String deviceId);

    /**
    * @Author 黄健杰
    * @Description 设置账号密码
    * @Date  2022/1/27
    * @Param
    * @return
    **/
    boolean setAccount(ProjectDeviceInfoProxyVo device);
    /**
    * @Author 黄健杰
    * @Description 设置聚集报警规则
    * @Date  2022/1/27
    * @Param
    * @return
    **/
    boolean setGatherAlarmRule(ProjectDeviceInfoProxyVo device, ProjectDeviceInfoProxyVo parDevice, ProjectDeviceGatherAlarmRuleVo rule);
    /**
    * @Author 黄健杰
    * @Description 获取设备通道信息
    * @Date  2022/2/7
    * @Param
    * @return
    **/
    AurineEdgeDeviceInfoDTO getChannel(ProjectDeviceInfoProxyVo deviceInfo);

    /**
     * 梯控操作楼层
     *
     * @param deviceId 设备id
     * @Param floors 楼层
     * @return
     */
    boolean operateFloor(String deviceId,String[] floors);

    /**
     * <p>
     * 设备自动注册处理
     * </p>
     *
     * @param
     * @author: 王良俊
     * @return
     */
    boolean regDevice(DeviceRegDto dto);

    /**
     * <p>
     * 设备自动注册处理
     * </p>
     *
     * @param
     * @author: 王良俊
     * @return
     */
    boolean statusChange(DeviceStatusDto dto);

    /**
     * <p>
     * 配置默认参数
     * </p>
     *
     * @param
     * @author: 王良俊
     * @return
     */
    boolean configDefaultParam(String deviceId);

    /**
     * 门常开设置
     * @param deviceId
     * @param doorAction 1 = 常开，2 = 常闭
     * @return
     */
    boolean openAlways(String deviceId, Integer doorAction);
}
