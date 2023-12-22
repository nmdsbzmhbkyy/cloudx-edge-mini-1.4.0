package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectRightDevice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>设备区域vo对象</p>
 * @author : 王良俊
 * @date : 2021-09-02 11:36:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectRightDeviceVo extends ProjectRightDevice {


    /**
     * 卡状态
     */
    private Integer cardStatus;


}