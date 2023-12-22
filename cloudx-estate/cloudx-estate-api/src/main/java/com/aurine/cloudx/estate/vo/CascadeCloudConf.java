package com.aurine.cloudx.estate.vo;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>存储级联入云时MQTT可能需要的信息</p>
 * @author : 王良俊
 * @date : 2021-12-29 10:21:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CascadeCloudConf{

    /**
     * 当前设备上项目的UUID
     */
    private String projectUUID;

    /**
     * 第三方项目ID
     */
    private String projectCode;

    /**
     * 级联码或是入云码
     */
    private String connectCode;

}
