package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectDeviceLoadLog;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 设备导入日志Vo对象
 *
 * @author 王良俊
 * @since 2021-06-03 10:49:35
 */
@Data
@NoArgsConstructor
public class ProjectDeviceLoadLogVo extends ProjectDeviceLoadLog {

    /*
    * 失败数
    * */
    private String failedNum;
}
