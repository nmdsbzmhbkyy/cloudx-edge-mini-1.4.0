package com.aurine.cloudx.estate.thirdparty.module.wr20.entity.dto;

import lombok.Data;

/**
 * WR20 住户 面部识别信息 对象
 *
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-12-23 15:04
 * @Copyright:
 */
@Data
public class WR20CardManageObj {
    /**
     * 住户id
     */
    private String teneID;
    /**
     * 卡号
     */
    private String cardNo;
    /**
     * 人员类型 0 住户 10 员工
     */
    private String personType;
}
