package com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums;

import lombok.AllArgsConstructor;

/**
 * 事件子类型
 */
@AllArgsConstructor
public enum EventSubTypeEnum {

    /**
     * 报警事件子类型
     */
    LINE_DETECTOR_CROSSED("LineDetectorCrossed", "越界检测", EventTypeEnum.A006001),
    FIELD_DETECTOR_OBJECTS_INSIDE("FieldDetectorObjectsInside", "区域入侵", EventTypeEnum.A006001),
    OBJECT_IS_RECOGNIZED("ObjectIsRecognized", "人脸检测", EventTypeEnum.A006001),
    ENTER_AREA("EnterArea", "进入区域", EventTypeEnum.A006001),
    LEAVE_AREA("LeaveArea", "离开区域", EventTypeEnum.A006001),

    /**
     * 人脸抓拍事件子类型
     */
    FACE_CAPTURE("FaceCapture", "人脸抓拍", EventTypeEnum.A006002),

    /**
     * OTHER
     */
    OTHER("other", "其他", EventTypeEnum.OTHER),
    ;


    public String typeString;

    public String typeName;

    public EventTypeEnum eventTypeEnum;

    public static EventSubTypeEnum getByType(String typeString) {
        EventSubTypeEnum[] enums = values();
        for (EventSubTypeEnum eventSubTypeEnum : enums) {
            if (eventSubTypeEnum.typeString.equals(typeString)) {
                return eventSubTypeEnum;
            }
        }
        return null;
    }

}
