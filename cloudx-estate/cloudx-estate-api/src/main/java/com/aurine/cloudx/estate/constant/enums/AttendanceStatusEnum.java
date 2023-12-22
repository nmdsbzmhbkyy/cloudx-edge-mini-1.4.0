package com.aurine.cloudx.estate.constant.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum AttendanceStatusEnum {
    NONE("-1", "没有排班计划"),
    REST("0", "休息"),
    NORMAL("1", "正常打卡"),
    LATE("2", "迟到"),
    LEAVEEARLY("3", "早退"),
    LATE_AND_LEAVEEARLY("4", "迟到、早退"),
    ABSENTEEISM("5", "旷工"),
    MISSED_CLOCKING("6", "下班漏打卡"),
    MORNING_MISSED_CLOCKING("7", "上班漏打卡"),
    LATE_AND_MISSED_CLOCKING("8", "迟到、漏打卡"),
    LEAVEEARLY_AND_MISSED_CLOCKING("9", "早退、漏打卡");



    public String code;
    public String name;
}
