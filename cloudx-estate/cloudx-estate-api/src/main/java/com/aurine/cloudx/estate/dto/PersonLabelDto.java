package com.aurine.cloudx.estate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 人员标签DTO对象
 * </p>
 *
 * @author 王良俊
 * @since 2022/6/17 10:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonLabelDto {

    /*
     * 人员标签ID数组
     **/
    private String[] labelUidArray;

    /*
     * 住户ID
     **/
    private String personId;
}
