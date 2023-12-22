package com.aurine.cloudx.estate.thirdparty.module.device.platform.yushi.entity.respond;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class YushiResponse {

    private String ResponseURL;

    private Long CreatedID;

    private Long ResponseCode;

    private Long SubResponseCode;

    private String ResponseString;

    private Long StatusCode;

    private String StatusString;

    private JSONObject Data;
}
