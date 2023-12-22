package com.aurine.cloudx.open.origin.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 手动开闸信息
 * </p>
 *
 * @author 王良俊
 * @since 2022/6/27 17:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OpenLaneInfoVo {

    /*
     * 车道ID
     **/
    private String laneId;

    /*
     * 车牌号
     **/
    private String plateNumber;

    /*
     * 放行原因字典code
     **/
    private Character reasonCode;

    private String parkId;

}
