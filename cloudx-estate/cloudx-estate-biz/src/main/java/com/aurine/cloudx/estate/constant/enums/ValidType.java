package com.aurine.cloudx.estate.constant.enums;

import com.aurine.cloudx.estate.service.policy.qrpasscode.IValidQrPasscode;
import com.aurine.cloudx.estate.service.policy.qrpasscode.impl.AurineQrPasscodeValidImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@AllArgsConstructor
@Getter
public enum ValidType {
    aurine(0, "aurineQrPasscodeValid"),
    scan_table(1, "scanTableQrPasscodeValid"),
    ;
    private final Integer type;
    private final String validClass;

    public static String findByType(Integer type){
        for (ValidType value : ValidType.values()) {
            if (Objects.equals(value.type,type)){
                return value.validClass;
            }
        }
        return null;
    }

}
