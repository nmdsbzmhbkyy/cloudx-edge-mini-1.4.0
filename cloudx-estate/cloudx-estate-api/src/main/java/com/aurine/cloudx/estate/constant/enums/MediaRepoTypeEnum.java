package com.aurine.cloudx.estate.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MediaRepoTypeEnum {

    ResourceType_01("01","出入口资源"),
    ResourceType_02("02","小区广播资源");
    private String key;
    private String value;

}
