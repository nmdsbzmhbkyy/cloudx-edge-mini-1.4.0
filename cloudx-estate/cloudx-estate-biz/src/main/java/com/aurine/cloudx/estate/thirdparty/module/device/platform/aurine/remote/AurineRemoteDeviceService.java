

package com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.remote;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.config.AurineConfigDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.dto.AurineMessageDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.vo.AurineCertVo;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.vo.AurineDeviceVo;
import com.aurine.cloudx.estate.thirdparty.remote.BaseRemote;

import java.util.List;

/**
 * Aurine 中台 设备指令下发接口 异步
 * 用于设备操作指令下发，如开门、重启等命令
 *
 * @ClassName: HuaweiRemoteDeviceService
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-12 14:02
 * @Copyright:
 */
public interface AurineRemoteDeviceService extends BaseRemote {
    /**
     * 产品型号id， 020302为门禁产品梯区口机的型号id
     */
    String MODEL_ID = "020302";

    /**
     * 开门
     *
     * @param configDTO 配置对象
     * @param deviceSn  设备SN
     * @return
     */
    boolean openDoor(AurineConfigDTO configDTO, String deviceSn, String personId, String personType);

    /**
     * 重启
     *
     * @param configDTO 配置对象
     * @param deviceSn  设备SN
     * @return
     */
    boolean reboot(AurineConfigDTO configDTO, String deviceSn);

    /**
     * 添加设备
     *
     * @param configDTO 配置对象
     * @param deviceVo  设备对象
     * @return
     */
    String addDevice(AurineConfigDTO configDTO, AurineDeviceVo deviceVo);

    /**
     * 删除设备
     *
     * @param configDTO
     * @param deviceVo
     * @return
     */
    boolean delDevice(AurineConfigDTO configDTO, AurineDeviceVo deviceVo);

    /**
     * 添加通行凭证
     *
     * @param certList
     * @return
     */
    JSONObject addCert(AurineConfigDTO configDTO, AurineDeviceVo deviceVo, List<AurineCertVo> certList);

    /**
     * 删除通行凭证
     *
     * @param certList
     * @return
     */
    JSONObject delCert(AurineConfigDTO configDTO, AurineDeviceVo deviceVo, List<AurineCertVo> certList);

    /**
     * 订阅
     *
     * @param configDTO
     * @return
     */
    boolean subscribe(AurineConfigDTO configDTO);

    /**
     * 同步设备
     *
     * @param configDTO
     * @return
     */
    JSONObject syncDevice(AurineConfigDTO configDTO, int pageNo, int pageSize);

    JSONObject sendMessages(AurineConfigDTO configDTO, String deviceSn, List<AurineMessageDTO> messageList);


}
