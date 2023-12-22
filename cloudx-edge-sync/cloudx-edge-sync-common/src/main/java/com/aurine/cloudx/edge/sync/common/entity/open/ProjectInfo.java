package com.aurine.cloudx.edge.sync.common.entity.open;

import lombok.Data;

/**
 * @Author: wrm
 * @Date: 2022/01/12 8:53
 * @Package: com.aurine.cloudx.edge.sync.entity.open
 * @Version: 1.0
 * @Remarks: 入云级联项目信息对象，用于接收数据
 **/
@Data
public class ProjectInfo {
    /**
     *
     */
    private String projectId;
    private String projectUUID;
    private String appId;
    private String syncType;
    /** 第三方项目UUID */
    private String projectCode;
}
