package com.aurine.cloudx.estate.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 房屋某个住户数量（用于判断是否已存在该住户）
 * </p>
 * @author : 王良俊
 * @date : 2021-06-11 10:01:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectHousePersonNumVo {

    /**
     * 房屋住户ID（房屋ID+住户ID拼接）
     * */
    private String housePersonId;


    /**
     * 用于判断住户是否已存在（如果已存在则设置为1）
     * */
    private String num;
}
