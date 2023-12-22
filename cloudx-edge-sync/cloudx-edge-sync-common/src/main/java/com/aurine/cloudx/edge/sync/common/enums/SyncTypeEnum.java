package com.aurine.cloudx.edge.sync.common.enums;

import lombok.AllArgsConstructor;


/**
 * @Author: wrm
 * @Date: 2021/12/24 14:35
 * @Package: com.aurine.cloudx.open.intocloud.api.enums
 * @Version: 1.0
 * @Remarks: 数据库关系，数据冲突时主库覆盖从库
 **/
@AllArgsConstructor
public enum SyncTypeEnum {
    /**
     * 主库
     */
    MAIN(0),

    /**
     * 从库
     */
    SECONDARY(1);

    public Integer code;

}
