package com.aurine.cloudx.open.origin.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 疫情记录
 *
 * @author 邹宇
 * @date 2021-6-7 11:08:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectEpidemicEventVo {

    private Integer current;
    private Integer size;
    /**
     * 健康码状态 绿码、红码
     */
    private String codeStatus;

    /**
     * 类型 1 住户 2 员工 3 访客
     */
    private String personType;


    /**
     * 姓名
     */
    private String personName;
}
