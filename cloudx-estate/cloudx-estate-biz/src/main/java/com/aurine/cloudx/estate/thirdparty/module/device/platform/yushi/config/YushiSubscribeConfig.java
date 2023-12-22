package com.aurine.cloudx.estate.thirdparty.module.device.platform.yushi.config;

import com.aurine.cloudx.estate.thirdparty.main.entity.dto.BaseConfigDTO;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Data
@Component
@RefreshScope
public class YushiSubscribeConfig extends BaseConfigDTO {

    @Value("${yushi.subscribe.ip:''}")
    private String ip;

    @Value("${yushi.subscribe.port:''}")
    private String port;

}
