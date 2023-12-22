package com.aurine.cloudx.push.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description: 模板枚举
 * @author: 邹宇
 * @date: 2021-8-25 09:14:02
 * @Copyright:
 */
@Getter
@AllArgsConstructor
public enum TemplateEnum {

    /**
     * 审核结果通知
     */
    REVIEW("yRxkc2lIDailbslD1upWeROz8pgOPrithK2iT8uInfI","1"),

    /**
     * 投诉建议通知
     */
    COMPLAINT_SUGGESTION("KDRNqGBAUPowuVJ5jUSUeJzJ13BsisJUjwnVYXkOQo0","2"),


    /**
     * 访客预约来访审核提醒
     */
    VISITOR("OLRePkH0Wb04CEmWdXabE_mpIZRg3-g3v7sGlGruGv4","3"),

    /**
     * 投诉处理进展通知
     */
    COMPLAINT_HANDLING("UCSN8SX7bi96PO0zRUkfWXCWse3PUSpUmLKPDW0XylE","4"),

    /**
     * 维修任务提醒
     */
    MAINTAIN("VzlDr2FRCuG5mmwt8gFiR25zFc3b_UPS6naSiZLYlIg","5"),

    /**
     * 报修处理进展通知
     */
    REPAIR("M4J9TU8QALLhE6mhY3ezstwjkplHXGWs0314D85oFZM","6"),

    /**
     * 分机来电通知
     */
    EXTENSION_CALL("MrG5WxPytY5GaZ7imUg-yeo8DN3eDQ7wAP3v4_dXH68","7");


    private final String templateId;

    private final String code;


    /**
     * @param templateId
     * @return
     */
    public static TemplateEnum getEnum(String templateId) {
        for (TemplateEnum value : TemplateEnum.values()) {
            if (value.getTemplateId().equals(templateId)) {
                return value;
            }
        }
        return null;
    }
}
