package com.aurine.cloudx.estate.vo;

import lombok.Data;

import java.util.List;

/**
 * @author:zy
 * @data:2022/9/14 11:50 上午
 */

@Data
public class SyncVo {


    private List<String> parkingServiceNameList;

    private Integer projectId;

    private String projectUUID;

    private Integer num;

    private String serviceName;

    private String syncId;

}
