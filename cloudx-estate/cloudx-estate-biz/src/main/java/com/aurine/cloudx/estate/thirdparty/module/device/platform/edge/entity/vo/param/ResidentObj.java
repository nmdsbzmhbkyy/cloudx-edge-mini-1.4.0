package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.param;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 住户参数对象 json名：residentParam
 * </p>
 *
 * @ClassName: ResidentObj
 * @author: 王良俊 <>
 * @date: 2020年11月25日 下午04:03:31
 * @Copyright:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName("residentParam")
public class ResidentObj {

    /**
     * 起始房号
     * 默认01
     */
    @NotNull
    String roomNoStart;

    /**
     * 楼层数
     * 默认10
     */
    @NotNull
    Integer floorCount;

    /**
     * 每层户数
     * 默认2
     */
    @NotNull
    Integer floorHouseNum;

}
