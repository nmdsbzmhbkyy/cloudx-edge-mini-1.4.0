package com.aurine.cloudx.edge.sync.common.entity.open;

import lombok.Data;

/**
 * @Author: wrm
 * @Date: 2022/01/10 17:11
 * @Package: com.aurine.cloudx.edge.sync.entity
 * @Version: 1.0
 * @Remarks:
 **/
@Data
public class OpenApiRequestHeader {

    private String appId;
    private String projectUUID;
    private Integer tenantId;
}
