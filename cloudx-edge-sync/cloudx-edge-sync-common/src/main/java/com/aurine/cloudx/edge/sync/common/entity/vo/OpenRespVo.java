package com.aurine.cloudx.edge.sync.common.entity.vo;

import com.alibaba.fastjson.JSONObject;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: wrm
 * @Date: 2022/01/10 14:13
 * @Package: com.aurine.cloudx.edge.sync.entity.dto
 * @Version: 1.0
 * @Remarks:
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenRespVo {
    /**
     * 业务主键id
     */
    private String id;

    private R<JSONObject> r;
}
