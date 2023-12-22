package com.aurine.cloudx.edge.sync.common.constant;

import cn.hutool.core.collection.ListUtil;
import com.aurine.cloudx.open.common.core.constant.enums.ServiceNameEnum;

import java.util.List;

/**
 * @author:zy
 * @data:2023/10/27 9:29 上午
 */
public class TableIdConstant {


    //主键为seq的集合
    public static final List<String> TABLE_ID_LIST = ListUtil.toList(
            ServiceNameEnum.ENTITY_LEVEL_CFG.name,
            ServiceNameEnum.PERSON_PLAN_REL.name,
            ServiceNameEnum.PASS_PLAN_POLICY_REL.name,
            ServiceNameEnum.PERSON_DEVICE_REL.name,
            ServiceNameEnum.ALARM_HANDLE.name,
            ServiceNameEnum.PERSON_ENTRANCE.name,
            ServiceNameEnum.HOUSE_PERSON_CHANGE_HIS.name,
            ServiceNameEnum.PROJECT_CONFIG.name,
            ServiceNameEnum.DEVICE_ATTR.name
    );


}
