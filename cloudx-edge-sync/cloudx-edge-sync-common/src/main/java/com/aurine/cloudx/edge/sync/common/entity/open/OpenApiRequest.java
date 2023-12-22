package com.aurine.cloudx.edge.sync.common.entity.open;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @Author: wrm
 * @Date: 2022/01/10 17:08
 * @Package: com.aurine.cloudx.edge.sync.entity
 * @Version: 1.0
 * @Remarks:
 **/
@Data
public class OpenApiRequest {
    /**
     * data数据内容
     */
    private JSONObject data;

    /**
     * 请求头
     */
    private OpenApiRequestHeader header;
}
