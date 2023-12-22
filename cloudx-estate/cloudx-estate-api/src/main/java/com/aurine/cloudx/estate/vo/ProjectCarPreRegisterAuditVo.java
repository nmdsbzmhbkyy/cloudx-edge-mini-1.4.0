package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectCarPreRegister;
import com.aurine.cloudx.estate.entity.ProjectParCarRegister;
import lombok.Data;

/**
 * 车辆预登记审核通过vo对象
 */
@Data
public class ProjectCarPreRegisterAuditVo extends ProjectParCarRegisterVo {

    /**
     * 车辆预登记ID（用于审核通过的时候修改审核状态）
     */
    private String preRegId;

    /**
     * 停车区域ID
     * */
    private String parkRegionId;

    /**
     * 车牌号是否已被登记
     * */
    private String plateNumberHasRegister;

    /**
     * 日期数组（后端返回的对象没有这个属性会导致前端组件无法选择时间）
     * */
    private String[] dateArray;

}
