package com.aurine.cloudx.estate.dto.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <p>
 * 其他系统参数设置
 * </p>
 *
 * @author 王良俊
 * @since 2022/5/13 17:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingOtherSettingDto extends BaseParam {

    /*
    * LED亮度
    * */
    @JsonProperty("LEDBrightness")
    private Integer LEDBrightness;

    /*
    * 音量设置对象
    * */
    private List<ParkVolumeSetupDto> volumeSetup;

    /*
    * 补光灯开始时间
    * */
    private String fillStartTime;

    /*
     * 补光灯结束时间
     * */
    private String fillEndTime;

    @Override
    public String getObjName() {
        return "parkingOtherSetting";
    }
}