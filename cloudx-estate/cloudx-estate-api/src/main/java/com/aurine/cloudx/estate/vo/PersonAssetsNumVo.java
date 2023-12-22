package com.aurine.cloudx.estate.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 存放人员资产数
 * </p>
 * @ClassName: PersonAssetsNumVo
 * @author: 王良俊 <>
 * @date:  2021年02月04日 上午09:17:42
 * @Copyright:
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonAssetsNumVo {

    /**
     * 人员ID
     * */
    private String personId;

    /**
     * 房、车、车位数
     * */
    private Integer assetsNum;
}
