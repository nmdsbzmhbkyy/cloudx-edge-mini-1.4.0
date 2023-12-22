package com.aurine.cloudx.estate.config;

import com.aurine.cloudx.estate.vo.PermissionProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * (PermissionConfigure)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/9/15 9:55
 */
@Component
@ConfigurationProperties(prefix = "cloudx.project")
@Data
public class PermissionConfigure {
    List<PermissionProperty> defaultPermissions;
    List<PermissionProperty> excludePermissions;
}
