package com.aurine.cloudx.estate.dto.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 出场播报信息
 * </p>
 *
 * @author 王良俊
 * @since 2022/5/13 17:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkInOutSetupDto {

    /**
     * <p>
     * 是否播报车牌号
     * </p>
     *
     * @author 王良俊
     * @since 2022/5/13 17:53
     */
    private String plateNumber;

    /*
    * 是否播报有效期信息
    * */
    private String validTime;

    /*
    * 是否播报收费信息
    * */
    private String parkingFee;

    /*
     * 是否播报常用语
     * */
    private String commonTerms;

    /*
     * 播报常用语内容（中台常用语模板）
     * */
    private String commonTermsVal;

}
