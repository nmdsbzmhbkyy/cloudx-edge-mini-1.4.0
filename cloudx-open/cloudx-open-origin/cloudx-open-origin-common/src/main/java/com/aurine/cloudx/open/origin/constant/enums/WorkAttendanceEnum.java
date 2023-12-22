package com.aurine.cloudx.open.origin.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 考勤结果
 *
 *
 */
@Getter
@AllArgsConstructor
public enum WorkAttendanceEnum {
    REST("0","排休"),
    NORMAL("1","正常"),
    LATE("2", "迟到"),
    LEAVE_EARLY("3", "早退"),
    LATE_AND_LEAVE_EARLY("4", "迟到、早退"),
    ABSENT("5", "旷工");

    public String code;
    public String name;
}
