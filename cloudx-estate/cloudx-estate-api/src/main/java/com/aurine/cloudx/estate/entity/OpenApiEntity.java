package com.aurine.cloudx.estate.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: wrm
 * @Date: 2021/12/10 16:26
 * @Package: com.example.aoptest.entity
 * @Version: 1.0
 * @Remarks:
 **/

@Data
@NoArgsConstructor
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
    private Object data;

}
