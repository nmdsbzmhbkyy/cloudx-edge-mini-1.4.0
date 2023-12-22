package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant;

import com.aurine.cloudx.common.core.constant.enums.DeviceTypeEnum;
import lombok.AllArgsConstructor;

/**
 * @description: 设备类型资源枚举
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021/09/26 14:51
 * @Copyright:
 */
@AllArgsConstructor
public enum AurineEdgeDevResTypeEnum {

    DjInDoor("djInDoor", "可视对讲-室内机", DeviceTypeEnum.INDOOR_DEVICE),
    DjOutDoor("djOutDoor", "可视对讲-门口机", DeviceTypeEnum.OTHER),
    DjUnitDoor("djUnitDoor", "可视对讲-单元门口机", DeviceTypeEnum.LADDER_WAY_DEVICE),
    DjWallDoor("djWallDoor", "可视对讲-区口机", DeviceTypeEnum.GATE_DEVICE),
    DjManager("djManager", "可视对讲-管理员机", DeviceTypeEnum.CENTER_DEVICE),
    DjUnifyDoor("djUnifyDoor", "可视对讲-门禁一体机", DeviceTypeEnum.GATE_DEVICE),
    DjPerimeterAlarm("djPerimeterAlarm", "安防-周界报警", DeviceTypeEnum.ALARM_DEVICE),
    DjElevatorLayerCtrl("djElevatorLayerCtrl", "门禁系统-电梯分层控制器", DeviceTypeEnum.ELEVATOR_LAYER_CONTROL_DEVICE),
    DjElevatorRecognizer("djElevatorRecognizer", "门禁系统-乘梯识别终端", DeviceTypeEnum.ELEVATOR_RECOGNIZER_DEVICE),
    DjElevatorStateDetector("djElevatorStateDetector", "门禁系统-电梯状态检测器", DeviceTypeEnum.ELEVATOR_STATE_DETECTOR_DEVICE),
    OTHER("OTHER", "其他", DeviceTypeEnum.OTHER);

    public String code;
    public String desc;
    public DeviceTypeEnum deviceType;

    public static AurineEdgeDevResTypeEnum getByCode(String code) {
        AurineEdgeDevResTypeEnum[] huaweiEventTypeEnums = values();
        for (AurineEdgeDevResTypeEnum resEnum : huaweiEventTypeEnums) {
            if (resEnum.code().equals(code)) {
                return resEnum;
            }
        }
        return OTHER;
    }

    private String code() {
        return this.code;
    }
}
