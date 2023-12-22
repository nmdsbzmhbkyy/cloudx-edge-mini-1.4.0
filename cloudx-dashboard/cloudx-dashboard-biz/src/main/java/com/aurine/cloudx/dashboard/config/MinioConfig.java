package com.aurine.cloudx.dashboard.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @ClassName: ProjectBuildingHouseInfoMView
 * @author : Qiu <qiujb@miligc.com>
 * @date : 2021 08 24 19:11
 * @Copyright:
 */

@Data
@Component
public class MinioConfig {

    @Value("${minio.server.url}")
    private String serverUrl;
}
