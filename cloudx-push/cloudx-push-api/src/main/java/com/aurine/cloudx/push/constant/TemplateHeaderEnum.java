package com.aurine.cloudx.push.constant;

import lombok.Getter;

/**
 * @description: 模板枚举
 * @author: 邹宇
 * @date: 2021-8-25 09:14:02
 * @Copyright:
 */
@Getter
public enum TemplateHeaderEnum {
    // 小程序/APP/公众号===========================

    // 审核结果通知模板 OPENTM411227431
    /** 添加房屋成功 参数：房屋地址 */
    HOUSE_ADD_SUCCESS("您申请入住的【%s】,已通过审核"),
    /** 添加房屋失败 参数：房屋地址 */
    HOUSE_ADD_FAILED("您申请入住的【%s】,未通过审核"),
    /** 添加家属成功 参数：家属姓名 房屋地址 */
    KIN_ADD_SUCCESS("您申请的家属: 【%s】,入住【%s】，已通过审核"),
    /** 添加家属失败 参数：家属姓名 房屋地址 */
    KIN_ADD_FAILED("您申请的家属: 【%s】,入住【%s】，未通过审核"),
    /** 添加租客成功 参数：租客姓名 房屋地址 */
    TENANT_ADD_SUCCESS("您申请的租客: 【%s】,入住【%s】，已通过审核"),
    /** 添加租客失败 参数：租客姓名 房屋地址 */
    TENANT_ADD_FAILED("您申请的租客: 【%s】,入住【%s】，未通过审核"),
    /** 访客邀约成功 参数：访客姓名 */
    INVITE_VISITORS_SUCCESS("您邀请的访客【%s】，已通过审核"),
    /** 访客邀约失败 参数：访客姓名 */
    INVITE_VISITORS_FAILED("您邀请的访客【%s】，未通过审核"),
    /** 车辆登记成功 参数：车牌号 */
    VEHICLE_REGISTRATION_SUCCESS("您申请登记的车牌号为【%s】的车辆,已通过审核"),
    /** 车辆登记失败 参数：车牌号 */
    VEHICLE_REGISTRATION_FAILED("您申请登记的车牌号为【%s】车辆,未通过审核"),

    // 访客预约来访审核提醒模板 OPENTM418069702
    /** 来访审核提醒 */
    VISIT_AUDIT_REMARK("您有新的访客，请尽快确认"),

    // 报修处理进展通知 OPENTM409812175
    /** 报修状态变更 */
    REPORT_REPAIR_STATUS_CHANGE("报修状态变更"),
    /** 报修状态完成 */
    REPORT_REPAIR_STATUS_COMPLETE("报修处理完成"),

    // 投诉处理进展通知 OPENTM410578751
    /** 投诉状态变更 */
    COMPLAINT_STATUS_CHANGE("投诉状态变更"),
    /** 投诉状态完成 */
    COMPLAINT_STATUS_COMPLETE("投诉处理完成"),

    // 维修任务提醒模板 OPENTM410578751
    /** 维修任务提醒 */
    REPAIR_TASK_REMINDER("您有被指派的报修工单，请尽快处理"),

    // 投诉建议通知模板 OPENTM410406756
    /** 投诉建议通知 */
    COMPLAINT_SUGGEST_NOTICE("您有被指派的投诉工单，请尽快处理"),


    // WEB端===========================================

    /** 投诉建议通知 OPENTM410406756 */
    WEB_COMPLAINT_SUGGEST_NOTICE("您有被指派的投诉工单，请尽快处理"),
    /** 新投诉工单提醒 OPENTM410406756 */
    WEB_NEW_COMPLAINT_ORDER("有新的投诉工单等待接单"),
    /** 入住审核通知  OPENTM417811573 */
    WEB_CHECK_IN_NOTICE("有新的住户入住申请，请尽快审核"),
    /** 访客申请通知  OPENTM418069702 */
    WEB_VISIT_AUDIT_APPLY("有新的访客申请，请尽快审核"),
    /** 维修任务提醒  OPENTM410578751 */
    WEB_REPAIR_TASK_REMINDER("您有被指派的报修工单，请尽快处理"),
    /** 新维修任务通知 OPENTM410578751 */
    WEB_NEW_REPORT_REPAIR_NOTICE("有新的报修工单等待接单"),
    /** 周界报警提醒 OPENTM418234526 */
    WEB_PERIMETER_ALARM_REMINDER("有新的周界报警，请尽快确认处理"),


    // TRTC===========================================
    EXTENSIONCALL("尊敬的用户，您的坐机有对应客户来电");

    /**
     *
     */
    private String value;

    TemplateHeaderEnum(String value) {
        this.value = value;
    }

    public String getHeaderValue(String... values) {
        return String.format(value, values);
    }
}
