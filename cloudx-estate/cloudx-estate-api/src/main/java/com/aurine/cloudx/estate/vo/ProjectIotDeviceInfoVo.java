package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 带Iot报警参数的设备对象
 * </p>
 * @author : 王良俊
 * @date : 2021-07-22 10:40:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectIotDeviceInfoVo extends ProjectDeviceInfo {

    /**
    * IOT在设备维护页面还需要获取到这些“参数”，带有报警事件信息（路灯存储的不是报警事件而是路灯开关状态以及亮度数据）
    * */
    private String paramJson;

    /**
     * 事件名（使用','分隔），这里因为路灯比较特殊所以要从报警事件表中获取设备报警，且为未处理的
     * */
    private String eventNames;

    /**
     * 报警事件数（如果为0则事件名一定是空字符串）
     * */
    private int alarmNum;

}
