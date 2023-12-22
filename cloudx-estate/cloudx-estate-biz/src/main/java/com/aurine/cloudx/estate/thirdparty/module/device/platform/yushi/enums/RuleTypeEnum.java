package com.aurine.cloudx.estate.thirdparty.module.device.platform.yushi.enums;

import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.EventSubTypeEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.EventTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RuleTypeEnum {

    FIELD_DETECTOR_OBJECTS_INSIDE(0L, "区域入侵", EventSubTypeEnum.FIELD_DETECTOR_OBJECTS_INSIDE),
    LINE_DETECTOR_CROSSED(1L, "越界检测", EventSubTypeEnum.LINE_DETECTOR_CROSSED),
    LEAVE_AREA(2L, "离开区域", EventSubTypeEnum.LEAVE_AREA),
    ENTER_AREA(3L, "进入区域", EventSubTypeEnum.ENTER_AREA),
    ;


    public Long type;

    public String typeName;

    public EventSubTypeEnum eventSubTypeEnum;

    public static EventSubTypeEnum getEventSubTypeByType(Long type) {
        RuleTypeEnum[] enums = values();
        for (RuleTypeEnum ruleTypeEnum : enums) {
            if (ruleTypeEnum.type.equals(type)) {
                return ruleTypeEnum.eventSubTypeEnum;
            }
        }
        return EventSubTypeEnum.OTHER;
    }

}
