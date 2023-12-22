package com.aurine.cloudx.estate.thirdparty.module.device.platform.yushi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RespondEnum {

    SUCCEED(0L, "Succeed"),
    INVALID(2L, "Invalid Param");


    public Long ResponseCode;
    public String ResponseString;

    public static RespondEnum getByCode(Long code) {
        for (RespondEnum respondEnum : RespondEnum.values()) {
            if (respondEnum.ResponseCode.equals(code)) {
                return respondEnum;
            }
        }
        return null;
    }
}
