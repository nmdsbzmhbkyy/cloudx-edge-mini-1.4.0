package com.aurine.cloudx.open.origin.constant.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TrueFalseEnum {
    TRUE("1", "是"),
    FALSE("0", "否");
    public String key;
    public String value;
}
