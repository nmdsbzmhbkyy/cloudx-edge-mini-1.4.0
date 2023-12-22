package com.aurine.cloudx.estate.thirdparty.module.device.handler.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LiftPeriodItemDevice {

    /*
     * 时段开关
     **/
    private int pswitch;

    /*
     * 开发刷卡使用
     **/
    private int mode;

    /*
     * 可用楼层数组 前24和后24分别对应双开门的A门和B门
     **/
    private String[] floorArray = new String[48];

    /*
     * 可用楼层数组 前24和后24分别对应双开门的A门和B门
     **/
    private String[] validTime = new String[4];

    /*
     * 可用楼层数组 前24和后24分别对应双开门的A门和B门
     **/
    private String[] validTimeEndTime = new String[7];

}

