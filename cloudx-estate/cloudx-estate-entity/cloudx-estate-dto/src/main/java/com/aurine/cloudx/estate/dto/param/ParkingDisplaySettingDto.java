package com.aurine.cloudx.estate.dto.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 车道一体机显示屏设置类
 * </p>
 *
 * @author 王良俊
 * @since 2022/5/13 17:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingDisplaySettingDto {


    /*
     * 文字满屏停留时间
     * */
    private String stopTime;

    /*
     * 即时显示的时间
     * */
    private String showTime;

    /*
     * 移动模式
     * 0：立即显示
     * 1：向上移动
     * 2：向下移动
     * 3：向左移动
     * 4：向右移动动
     * */
    private String moveMode;
}