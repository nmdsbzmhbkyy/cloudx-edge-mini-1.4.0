package com.aurine.cloudx.estate.dto.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 显示屏显示设置对象信息
 * </p>
 *
 * @author 王良俊
 * @since 2022/5/13 18:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkShowDetailDto extends ParkingDisplaySettingDto {

    /*
     * 颜色
     * */
    private String color;

    /*
    * 屏幕内容
    * */
    private String content;

    /*
     * 行号
     * */
    private Integer lineNum;

}