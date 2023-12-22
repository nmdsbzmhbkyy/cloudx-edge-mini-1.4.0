package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectDeviceRel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDeviceRelVo extends ProjectDeviceRel {

    /**
    * 设备类型
    */
    private String deviceType;

}
