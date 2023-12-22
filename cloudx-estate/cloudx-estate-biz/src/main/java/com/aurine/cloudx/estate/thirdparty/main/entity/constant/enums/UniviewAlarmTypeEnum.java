package com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums;

import lombok.AllArgsConstructor;

/**
 * 宇视告警类型
 */
@AllArgsConstructor
public enum UniviewAlarmTypeEnum {

//    MOTION_ALARM(1000, "MotionAlarm", "运动检测"),
//    VIDEO_LOSS_ALARM(1002, "VideoLossAlarm", "视频丢失"),
//    MASK_IMAGE_ALARM(1004, "MaskImageAlarm", "遮挡检测"),
    LINE_DETECTOR_CROSSED(2000, "LineDetectorCrossed", "越界检测", EventSubTypeEnum.LINE_DETECTOR_CROSSED),
    FIELD_DETECTOR_OBJECTS_INSIDE(2001, "FieldDetectorObjectsInside", "区域入侵", EventSubTypeEnum.FIELD_DETECTOR_OBJECTS_INSIDE),
    OBJECT_IS_RECOGNIZED(2002, "ObjectIsRecognized", "人脸检测", EventSubTypeEnum.OBJECT_IS_RECOGNIZED),
//    GLOBAL_SCENE_CHANGE(2003, "GlobalSceneChange", "场景变更"),
//    IMAGE_TOO_BLURRY(2004, "ImageTooBlurry", "虚焦检测"),
//    ABNORMAL_AUDIO(2006, "AbnormalAudio", "音频检测"),
//    OBJECT_LEFT_BEHIND(2009, "ObjectLeftBehind", "物品遗留"),
//    OBJECT_REMOVED(2010, "ObjectRemoved", "物品搬移"),
//    LOITERING(2011, "Loitering", "区域徘徊"),
//    AUTO_TRACKING(2012, "AutoTracking", "智能跟踪"),
    ENTER_AREA(2014, "EnterArea", "进入区域", EventSubTypeEnum.ENTER_AREA),
    LEAVE_AREA(2015, "LeaveArea", "离开区域", EventSubTypeEnum.LEAVE_AREA),
//    HUMAN_SHAPE_DETECT(2016, "HumanShapeDetect", "人形检测"),
//    CONFLAGRATION(2018, "Conflagration", "火点检测"),
//    PEOPLE_GATHERING(2022, "PeopleGathering", "人员聚集"),
//    NOT_WEARING_MASK(2024, "NotWearingMask", "口罩未佩戴"),
//    ABNORMAL_BODY_TEMPERATURE(2025, "AbnormalBodyTemperature", "体温异常"),
//    FAST_MOVING(2026, "FastMoving", "快速移动"),
//    CROWD_DENSITY_MINOR_ALARM(2029, "CrowdDensityMinorAlarm", "人员密度普通告警"),
//    CROWD_DENSITY_MAJOR_ALARM(2031, "CrowdDensityMajorAlarm", "人员密度中度告警"),
//    CROWD_DENSITY_CRITICAL_ALARM(2033, "CrowdDensityCriticalAlarm", "人员密度严重告警"),
//    NOT_WEARING_SAFETY_HELMET(2036, "NotWearingSafetyHelmet", "未佩戴安全帽"),
//    NOT_WEARING_CHEF_HAT(2037, "NotWearingChefHat", "未佩戴厨师帽"),
//    NOT_WEARING_WORK_CLOTHES(2038, "NotWearingWorkClothes", "未穿戴工作服"),
//    PHONING(2039, "Phoning", "打电话"),
//    SMOKING(2040, "Smoking", "吸烟"),
//    FACE_RECOGNITION_MATCHLIST(2500, "FaceRecognitionMatchlist", "人脸识别匹配"),
//    FACE_RECOGNITION_MISMATCHLIST(2502, "FaceRecognitionMismatchlist", "人脸识别不匹配"),
    ;

    public Integer type;

    public String typeString;

    public String desc;

    public EventSubTypeEnum subEventTypeEnum;

    public static EventSubTypeEnum getEventSubTypeEnum(Integer type) {
        UniviewAlarmTypeEnum[] enums = values();
        for (UniviewAlarmTypeEnum univiewAlarmTypeEnum : enums) {
            if (univiewAlarmTypeEnum.type.equals(type)) {
                return univiewAlarmTypeEnum.subEventTypeEnum;
            }
        }
        return EventSubTypeEnum.OTHER;
    }

}
