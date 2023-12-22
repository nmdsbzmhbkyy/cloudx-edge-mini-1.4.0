package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant;

import lombok.AllArgsConstructor;

/**
 * <p>同步请求redis枚举</p>
 * @ClassName: AurineEdgeSyncReqEnum
 * @author: 黄健杰
 * @date: 2022-02-09
 * @Copyright:
 */
@AllArgsConstructor
public enum AurineEdgeSyncReqEnum {
    EDGE_SYNC_REQ("EDGE_SYNC_REQ_", "同步请求"),
    EDGE_SYNC_RESP("EDGE_SYNC_RESP_", "同步响应"),
    ;

    /**
     * 对象名称
     */
    public String code;
    /**
     * 描述
     */
    public String desc;

}
