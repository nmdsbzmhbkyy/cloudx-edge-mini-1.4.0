package com.aurine.cloudx.edge.sync.common.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author: wrm
 * @Date: 2021/12/23 16:23
 * @Package: com.aurine.cloudx.open.intocloud.api.entity
 * @Version: 1.0
 * @Remarks:
 **/
@Data
@AllArgsConstructor
public class AddUpdateResponseVo<T> {
    /**
     * 返回结果 true/false
     */
    private Boolean result;

    /**
     * 流水表对象
     */
    private T taskInfo;
}
