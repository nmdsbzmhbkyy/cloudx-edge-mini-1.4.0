package com.aurine.cloudx.estate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 车道一体机自动注册类
 * </p>
 *
 * @author 王良俊
 * @since 2022/6/6 15:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceStatusDto extends BaseDTO {

    private String thirdpartyCode;

    private Integer status;
}
