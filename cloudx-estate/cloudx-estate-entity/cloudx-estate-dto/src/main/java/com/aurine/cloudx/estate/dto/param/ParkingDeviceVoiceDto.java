package com.aurine.cloudx.estate.dto.param;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 语音播报设置
 * </p>
 *
 * @author 王良俊
 * @since 2022/5/13 17:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingDeviceVoiceDto extends BaseParam {

    /*
     * 是否启用语音播报
     **/
    private String enable;

    /*
     * 入场播报信息
     **/
    private ParkInOutSetupDto inSetup;

    /*
     * 出场播报信息
     **/
    private ParkInOutSetupDto outSetup;

    @Override
    public String getObjName() {
        return "parkingDeviceVoice";
    }
}