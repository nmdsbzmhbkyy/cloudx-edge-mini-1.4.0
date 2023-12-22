package com.aurine.cloudx.open.origin.constant.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Title: RoleProperties
 * Description:
 *
 * @author xull@aurine.cn
 * @version 1.0.0
 * @date 2020/6/17 9:19
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "cloudx.base.role")
public class BaseRoleProperties {
    /**
     * 默认平台管理员角色id
     */
    private Integer platformManagerId;
    /**
     * 默认平台运营管理员角色id
     */
    private Integer operationalManagerId;
    /**
     * 默认集团管理员角色id
     */
    private Integer companyManagerId;
    /**
     * 默认项目组管理员角色id
     */
    private Integer groupManagerId;
    /**
     * 默认项目管理员角色id
     */
    private Integer projectManagerId;
}
