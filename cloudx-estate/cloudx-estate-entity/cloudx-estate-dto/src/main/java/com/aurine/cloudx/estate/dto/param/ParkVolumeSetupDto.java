package com.aurine.cloudx.estate.dto.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 车道一体机音量参数设置
 * </p>
 *
 * @author 王良俊
 * @since 2022/5/13 18:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkVolumeSetupDto {

    /*
    * 音量
    * */
    private int volume;

    /*
     * 开始时间 HH
     * */
    private String startTime;

    /*
     * 结束时间 HH
     * */
    private String endTime;

}