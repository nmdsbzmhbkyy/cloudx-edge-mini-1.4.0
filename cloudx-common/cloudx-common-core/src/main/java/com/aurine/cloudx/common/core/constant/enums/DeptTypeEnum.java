package com.aurine.cloudx.common.core.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName:  部门类型   
 * @author: 林港 <ling@aurine.cn>
 * @date:   2020年5月8日 下午4:08:18      
 * @Copyright:
 */
@Getter
@AllArgsConstructor
public enum DeptTypeEnum {
    TOP("0", "平台管理"),
    COMPANY("1", "集团/企业"),
    GROUP("2", "项目组"),
    PROJECT("3", "项目"),
    DEPT("4", "一级部门");

    /**
     * 类型
     */
    private String id;
    
    /**
     * 名称
     */
    private String name;
}
