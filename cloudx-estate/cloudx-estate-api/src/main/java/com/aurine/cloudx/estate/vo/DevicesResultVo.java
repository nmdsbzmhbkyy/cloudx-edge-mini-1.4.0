package com.aurine.cloudx.estate.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DevicesResultVo {

    /**
     * 这里存放的是成功数量
     * */
    private int successNum;

    /**
     * 这里存放的是失败数量
     * */
    private int failedNum;

    /**
     * 这里存放的是总数量
     * */
    private int totalNum;

    /**
     * 这里存放的是失败的设备ID
     * */
    private List<String> failedDeviceIdList;



}
