package com.aurine.cloudx.estate.thirdparty.module.device.platform.yushi.entity.request;

import com.aurine.cloudx.estate.thirdparty.main.entity.dto.BaseConfigDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 宇视数据传输基本对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YushiConnectDTO extends BaseConfigDTO {

    /**
     * 设备ip
     */
    private String ip;

    /**
     * 设备端口
     */
    private Integer port;

    private String userName;

    private String password;

}
