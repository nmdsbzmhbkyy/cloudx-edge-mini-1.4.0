package com.aurine.cloudx.estate.feign;

import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * (RemoteDeviceInfo)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/12/9 8:55
 */
@FeignClient(contextId = "remoteDeviceInfoService", value = "cloudx-estate-biz")
public interface RemoteDeviceInfoService {

    @GetMapping("/projectDeviceInfo/open/{id}")
    R open(@PathVariable("id") String id);

    /**
     * 开门，带开门者信息
     * @param id
     * @param personType
     * @param personId
     * @return
     */
    @GetMapping("/projectDeviceInfo/open-by-person/{id}/{personType}/{personId}")
    R open(@PathVariable("id") String id, @PathVariable("personType") String personType, @PathVariable("personId") String personId);

    /**
     * <p>
     *  保存车道一体机信息
     * </p>
     *
     * @param deviceInfo 设备信息对象
     * @author: 王良俊
     */
    @PostMapping("/projectDeviceInfo/saveVehicleBarrierDevice")
    R saveVehicleBarrierDevice(@RequestBody ProjectDeviceInfo deviceInfo);

    /**
     * <p>
     *  获取设备信息
     * </p>
     *
     * @param deviceId 设备ID
     * @author: 王良俊
     */
    @GetMapping("/projectDeviceInfo/inner/getDeviceInfoByDeviceId/{deviceId}")
    R<ProjectDeviceInfo> getDeviceInfoByDeviceId(@PathVariable("deviceId") String deviceId);

    /**
     * <p>
     *  获取设备信息列表
     * </p>
     *
     * @param deviceId 设备ID
     * @author: 王良俊
     */
    @PostMapping("/projectDeviceInfo/inner/listDeviceByIds")
    R<List<ProjectDeviceInfo>> listDeviceByIds(@RequestBody List<String> deviceId,@RequestHeader(SecurityConstants.FROM) String from);

    /**
     * <p>
     *  获取设备信息
     * </p>
     *
     * @param thirdpartyCode 设备ID
     * @author: 王良俊
     */
    @GetMapping("/projectDeviceInfo/inner/getDeviceInfoByThirdpartyCode/{thirdpartyCode}")
    R<ProjectDeviceInfo> getDeviceInfoByThirdpartyCode(@PathVariable("thirdpartyCode") String thirdpartyCode,@RequestHeader(SecurityConstants.FROM) String from);


    @GetMapping("/projectDeviceInfo/network/config}")
    R getNetworkConfig(@RequestParam("mac") String mac , @RequestHeader(SecurityConstants.FROM) String from);

    @PutMapping("/configStatus")
    R updateConfigured(@RequestParam("mac") String mac , @RequestHeader(SecurityConstants.FROM) String from);

}

