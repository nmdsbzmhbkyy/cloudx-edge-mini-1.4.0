package com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.dto;

import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.config.AurineConfigDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.vo.AurineCertVo;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.vo.AurineDeviceVo;
import lombok.Data;

import java.util.List;

/**
 * 数据
 *
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-11-06
 * @Copyright:
 */
@Data
public class AurineCertSendKafkaDTO {
    private AurineConfigDTO configDTO;
    private AurineDeviceVo deviceVo;
    private List<AurineCertVo> certList;
    private String command;//操作指令

    public AurineCertSendKafkaDTO() {

    }

    public AurineCertSendKafkaDTO(AurineConfigDTO configDTO, AurineDeviceVo deviceVo, List<AurineCertVo> certList,String command) {
        this.configDTO = configDTO;
        this.deviceVo = deviceVo;
        this.certList = certList;
        this.command = command;
    }
}
