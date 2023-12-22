package com.aurine.cloudx.estate.dto.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <p>
 * 显示屏内容设置
 * </p>
 *
 * @author 王良俊
 * @since 2022/5/13 17:44
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingDeviceScreenDto extends BaseParam {

    /*
    * 非过车期间显示内容有效时间
    * */
    private String defaultValidTime;

    /*
     * 非过车期间显示内容
     * */
    private List<ParkShowDetailDto> defaultContent;

    /*
     * 过车期间 入场显示内容
     * */
    private List<ParkShowDetailDto> unexpiredInSetup;

    /*
     * 过车期间 出场显示内容
     * */
    private List<ParkShowDetailDto> unexpiredOutSetup;

    /*
     * 过期车辆 入场显示内容
     * */
    private List<ParkShowDetailDto> expiredInSetup;

    /*
     * 过期车辆 出场显示内容
     * */
    private List<ParkShowDetailDto> expiredOutSetup;

    /*
     * 非小区车辆 入场显示内容
     * */
    private List<ParkShowDetailDto> unrecordedInSetup;

    /*
     * 非小区车辆 出场显示内容
     * */
    private List<ParkShowDetailDto> unrecordedOutSetup;

    @Override
    public String getObjName() {
        return "parkingDeviceScreen";
    }
}