package com.aurine.cloudx.estate.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 用来获取对应地址的房屋ID
 * </p>
 * @author : 王良俊
 * @date : 2021-06-11 10:00:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectHouseAddressVo {

    /**
     * 房屋ID
     * */
    private String houseId;

    /**
     * 房屋地址（如果开启组团则也会包含组团）
     * */
    private String address;
}
