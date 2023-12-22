package com.aurine.cloudx.estate.open.device.fegin;

import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.open.device.bean.ProjectDeviceInfoPageFormVoPage;
import com.aurine.cloudx.estate.vo.*;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@FeignClient(contextId = "remoteProjectDeviceInfoService", value = "cloudx-estate-biz")
public interface RemoteProjectDeviceInfoService {

    /**
     * 分页查询
     *
     * @param page                        分页对象
     * @return
     */
    @GetMapping("/projectDeviceInfo/page")
    R getProjectDeviceInfoPage(@SpringQueryMap ProjectDeviceInfoPageFormVoPage page);


    /**
     * 根据产品ID获取到这个产品下所有的设备，根据设备ID排除该设备
     *
     * @param page                 分页对象
     * @return
     */
    @GetMapping("/projectDeviceInfo/getDevicePageByProductId")
    R getDevicePageByProductId(@SpringQueryMap ProjectDeviceInfoPageFormVoPage page);

    /**
     * 分页查询设备参数信息
     *
     * @param page                        分页对象
     * @return
     */
    @GetMapping("/projectDeviceInfo/pageDeviceParam")
    R pageDeviceParam(@SpringQueryMap ProjectDeviceInfoPageFormVoPage page);

    /**
     * 通过id查询设备信息表
     *
     * @param id id
     * @return R
     */
    @GetMapping("/projectDeviceInfo/{id}")
    R<ProjectDeviceInfoVo> getById(@PathVariable("id") String id);

    /**
     * <p>
     * 根据新设备SN和旧设备的ID进行设备替换
     * </p>
     *
     * @param deviceInfo 设备信息对象 -> 设备ID(所要替换的原设备ID)，设备SN(所要替换新设备的SN) 这两个是必填
     * @return R
     */
    @PostMapping("/projectDeviceInfo/replaceDevice")
    R replaceDevice(@RequestBody ProjectDeviceInfo deviceInfo);


    /**
     * 新增设备信息表
     *
     * @param projectDeviceInfo 设备信息表
     * @return R
     */
    @PostMapping("/projectDeviceInfo")
    R save(@RequestBody ProjectDeviceInfoVo projectDeviceInfo);


    /**
     * 修改设备信息表
     *
     * @param projectDeviceInfo 设备信息表
     * @return R
     */
    @PutMapping("/projectDeviceInfo")
    R updateById(@RequestBody @Valid ProjectDeviceInfoVo projectDeviceInfo);

    /**
     * 修改执行状态
     *
     * @param deviceId
     * @param name
     * @return
     */
    @PutMapping("/projectDeviceInfo/putByStatus/{deviceId}/{name}")
    R updateByStatus(@PathVariable("deviceId") String deviceId, @PathVariable("name") String name);

    /**
     * <p>
     * 用于设置多台设备(这些设备的产品ID必须是同一个)
     * </p>
     *
     * @param deviceParamInfoVo deviceInfoList 设备信息对象集合（设备ID和设备第三方编码必须有数据）
     * @param deviceParamInfoVo paramJson      所要设置的参数json数据
     * @return 是否设置成功
     */
    @PutMapping("/projectDeviceInfo/setDevicesParam")
    R setDevicesParam(@RequestBody DeviceParamInfoVo deviceParamInfoVo);

    /**
     * <p>
     * 设置单台设备
     * </p>
     *
     * @param deviceParamInfoVo deviceCode 设备的第三方编码<br>
     *                          deviceParamInfoVo deviceId   设备的设备ID<br>
     *                          deviceParamInfoVo paramJson  所要设置的参数json数据<br>
     * @return 是否设置成功
     */
    @PutMapping("/projectDeviceInfo/setDeviceParam")
    R setDeviceParam(@RequestBody DeviceParamInfoVo deviceParamInfoVo);

    /**
     * <p>
     * 根据设备ID判断设备是否存在
     * </p>
     *
     * @param deviceId 设备ID
     * @author: 王良俊
     */
    @GetMapping("/projectDeviceInfo/checkDeviceIsExist")
    R checkDeviceIsExist(@RequestParam("deviceId") String deviceId);

    /**
     * <p>
     * 根据设备ID获取设备基本信息包括能力集
     * </p>
     *
     * @param deviceId 设备的设备ID
     * @return 是否设置成功
     */
    @GetMapping("/projectDeviceInfo/getDeviceInfo")
    R getDeviceInfo(@RequestParam("deviceId") String deviceId);

    /**
     * 通过id删除设备信息表
     *
     * @param id id
     * @return R
     */
    @DeleteMapping("/projectDeviceInfo/{id}")
    R removeById(@PathVariable("id") String id);


    /**
     * 通过id批量删除设备信息表
     *
     * @param ids ids
     * @return R
     */
    @DeleteMapping("/projectDeviceInfo/removeAll")
    R removeById(@RequestBody List<String> ids);

    /**
     * 根据类型查询设备数量统计
     *
     * @param type 设备类型id
     * @return R
     */
    @GetMapping("/projectDeviceInfo/count/{type}")
    R<DeviceCountInfoVo> selectCount(@PathVariable(value = "type") String type, @RequestParam(value = "deviceRegionId", required = false) String deviceRegionId);

    /**
     * 根据产品id查询设备总数
     *
     * @param productId 产品id
     * @return R
     */
    @GetMapping("/projectDeviceInfo/countByProductId/{productId}")
    R selectCount(@PathVariable(value = "productId") String productId);

    /**
     * 获取门禁设备列表
     * @param projectDeviceInfoFormVo
     * @return
     */
    @PostMapping("/projectDeviceInfo/getDeviceByType")
    R<List<ProjectDeviceInfoThirPartyVo>> getByType(@RequestBody ProjectDeviceInfoFormVo projectDeviceInfoFormVo);

    /**
     * 开门
     * @param getByType
     * @return
     */
    @GetMapping("/projectDeviceInfo/open/{id}")
    R open(@PathVariable("id") String id);

    /**
     * 用户开门
     * @param id
     * @param personType
     * @param personId
     * @return
     */
    @GetMapping("/projectDeviceInfo/open-by-person/{id}/{personType}/{personId}")
    R openByPerson(@PathVariable("id") String id, @PathVariable("personType") String personType, @PathVariable("personId") String personId);

    /**
     * 重启
     * @param id
     * @return
     */
    @GetMapping("/projectDeviceInfo/restart/{id}")
    R restart(@PathVariable("id") String id);

    /**
     * 恢复出厂
     * @param id
     * @return
     */
    @GetMapping("/projectDeviceInfo/reset/{id}")
    R reset(@PathVariable("id") String id);

    /**
     * 设置管理员密码
     * @param adminPassword
     * @return
     */
    @PostMapping("/projectDeviceInfo/setAdminPwd")
    R setAdminPwd(@RequestBody DeviceAdminPassword adminPassword);

    /**
     * 清空人脸
     * @param id
     * @return
     */
    @GetMapping("/projectDeviceInfo/cleanFace/{id}")
    R cleanFace(@PathVariable("id") String id);

    /**
     * 清空门卡
     * @param id
     * @return
     */
    @GetMapping("/projectDeviceInfo/cleanCard/{id}")
    R cleanCard(@PathVariable("id") String id);

    /**
     * 清空指纹
     * @param id
     * @return
     */
    @GetMapping("/projectDeviceInfo/cleanFingerprint/{id}")
    R cleanFingerprint(@PathVariable("id") String id);

    /**
     * 下载人脸
     * @param id
     * @return
     */
    @GetMapping("/projectDeviceInfo/loadFace/{id}")
    R loadFace(@PathVariable("id") String id);

    /**
     * 下载门卡
     * @param id
     * @return
     */
    @GetMapping("/projectDeviceInfo/loadCard/{id}")
    R loadCard(@PathVariable("id") String id);

    /**
     * 下载指纹
     * @param id
     * @return
     */
    @GetMapping("/projectDeviceInfo/loadFingerprint/{id}")
    R loadFingerprint(@PathVariable("id") String id);

    /**
     * 获取室内终端树形图
     *
     * @return
     */
    @GetMapping("/projectDeviceInfo/indoorTree")
    R<List<ProjectDeviceSelectTreeVo>> findIndoorTree();

    /**
     * 获取室内房屋树形图
     * @return
     */
    @GetMapping("/projectDeviceInfo/findIndoorHouseTree")
    R<List<ProjectDeviceSelectTreeVo>> findIndoorHouseTree();

    /**
     * 获取选中的房屋数量
     * @param ids
     * @return
     */
    @GetMapping("/projectDeviceInfo/getCountByIds")
    R<Integer> getCountIndoorByIds(@RequestParam("ids") List<String> ids);

    /**
     * 获取室内终端树形子节点
     * @param pid
     * @return
     */
    @GetMapping("/projectDeviceInfo/indoorTreeByPid")
    R<List<ProjectDeviceSelectTreeVo>> findIndoorTreeByPid(@RequestParam(value = "pid") String pid);


    /**
     * 获取梯口区口终端树形图
     *
     * @return
     */
    @GetMapping("/projectDeviceInfo/doorTree")
    R<List<ProjectDeviceSelectTreeVo>> findDoorTree(@RequestParam(value = "isRich", required = false) Boolean isRich);

    /**
     * 获取到这个巡点拥有的设备列表
     * @param pointId
     * @return
     */
    @GetMapping("/projectDeviceInfo/listDeviceByPointId/{pointId}")
    R listDeviceByPointId(@PathVariable("pointId") String pointId);

    /**
     * 获取视频直播流的地址
     *
     * @param deviceId
     * @return
     */
    @GetMapping("/projectDeviceInfo/getLiveUrl/{deviceId}")
    R<String> getLiveUrl(@PathVariable("deviceId") String deviceId);

    /**
     * 获取视频录播流的地址
     *
     * @param deviceId
     * @return
     */
    @GetMapping("/projectDeviceInfo/getVideoUrl/{deviceId}/{startTime}/{endTime}")
    R<String> getVideoUrl(@PathVariable("deviceId") String deviceId, @PathVariable("startTime") Long startTime, @PathVariable("endTime") Long endTime);

    /**
     * 通过设备类型和项目id获取到设备信息
     * @param projectId
     * @param type
     * @return
     */
    @GetMapping("/projectDeviceInfo/listDeviceByType/{projectId}/{type}")
    R listDeviceByType(@PathVariable("projectId") Integer projectId, @PathVariable("type") String type);

    /**
     * 获取咚咚设备列表
     *
     * @return
     */
    @GetMapping("/projectDeviceInfo/dd/list")
    R<List<ProjectDeviceInfo>> getDdDeviceList();

//
//    @PostMapping("/projectDeviceInfo/floorOperate/{id}")
//    R floorOperate(@PathVariable("id") String id, @RequestBody String[] floor);

    /**
     * 获取咚咚设备列表
     *
     * @return
     */
    @PostMapping("/projectDeviceInfo/exportExcel")
    void exportExcel( String type);
}
