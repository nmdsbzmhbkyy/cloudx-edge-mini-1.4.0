package com.aurine.cloudx.edge.sync.common.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @Author: wrm
 * @Date: 2021/12/15 16:38
 * @Package: com.aurine.cloudx.open.entity
 * @Version: 1.0
 * @Remarks:
 **/
@Data
public class OpenApiEntity {
    /** 服务类型 */
    private String serviceType;

    /** 服务名称 */
    private String serviceName;

    /** 操作类型 */
    private String operateType;

    /** 命令类型 */
    private String commandType;

    /** 级联类型 */
    private String cascadeType;

    /** 项目ID */
    private String projectUUID;

    /** 租户ID */
    private Integer tenantId;

    /** uuid 业务主键ID*/
    private String entityId;

    /** 对象 */
    private JSONObject data;

    // --------------额外参数----------------------
    /** 来源，不传默认设置当前项目source */
    private String source;
}
