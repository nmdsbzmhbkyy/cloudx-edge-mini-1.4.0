package com.aurine.cloudx.estate.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CascadeManageQuery {
    /**
     * 边缘网关负责人姓名
     */
    private String slaveContactPerson;
    /**
     * 项目名
     */
    private String projectName;
    /**
     * 设备状态（边缘网关）
     */
    private String edgeStatus;
}
