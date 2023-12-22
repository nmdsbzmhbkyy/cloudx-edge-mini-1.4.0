package com.aurine.cloudx.push.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.push.constant.Constants;
import com.aurine.cloudx.push.constant.TemplateEnum;
import com.aurine.cloudx.push.constant.TemplateHeaderEnum;
import com.aurine.cloudx.push.entity.*;
import com.aurine.cloudx.push.template.MiniProgram;
import com.aurine.cloudx.push.template.TemplateData;
import com.aurine.cloudx.push.template.TemplateMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>推送信息到公众号工具类</p>
 *
 * @ClassName: PushPublicAccountUtil
 * @author: 邹宇
 * @date: 2021-8-24 15:26:49
 * @Copyright:
 */
@Slf4j
public class PushWxUtil {

    /**
     * 发送消息
     *
     * @param templateId
     * @param unionIds
     * @param data
     * @return
     */
    public static boolean sendWxMessage(String templateId, List<String> unionIds, Map<String, Object> data) {
        if (TemplateEnum.getEnum(templateId) == null) {
            log.info("没有模板templateId:{}", templateId);
            return false;
        }
        if (CollectionUtils.isEmpty(unionIds)) {
            log.info("unionId为空:{}", unionIds);
            return false;
        }
        String url = String.format(Constants.TEMPLATE_SEND_MESSAGE, Constants.ACCESS_TOKEN);
        for (String unionId : unionIds) {

            String code = TemplateEnum.getEnum(templateId).getCode();
            //获取模板
            TemplateMessage templateMessage = getTemplateMessage(templateId,unionId,data,code);
            String result = HttpClientUtils.doJsonPost(url, JSON.toJSONString(templateMessage), null);
            log.info("result = " + result);
            JSONObject resObj = JSONObject.parseObject(result);
            if (!("0").equals(resObj.getString("errcode"))) {
                log.info("发送失败");
                log.error("resObj = " + resObj.getString("errcode"));
            } else {
                log.info("发送成功");
            }
        }
        return true;
    }

    /**
     * 获取模板的方法
     * @param templateId
     * @param unionId
     * @param data
     * @param code
     * @return
     */
    private static TemplateMessage getTemplateMessage(String templateId,String unionId, Map<String, Object> data,String code) {
        TemplateMessage templateMessage = null;
        switch (code) {
            case "1":
                HouseAddEntity houseAddEntity = JSONObject.parseObject(JSON.toJSONString(data.get("HouseAddEntity")), HouseAddEntity.class);
                if (houseAddEntity != null) {
                    templateMessage = processTemplate(templateId, unionId, houseAddEntity);
                }
                KinAddEntity kinAddEntity = JSONObject.parseObject(JSON.toJSONString(data.get("KinAddEntity")), KinAddEntity.class);
                if (kinAddEntity != null) {
                    templateMessage = processTemplate(templateId, unionId, kinAddEntity);
                }
                TenantAddEntity tenantAddEntity = JSONObject.parseObject(JSON.toJSONString(data.get("TenantAddEntity")), TenantAddEntity.class);
                if (tenantAddEntity != null) {
                    templateMessage = processTemplate(templateId, unionId, tenantAddEntity);
                }
                VehicleRegistrationEntity vehicleRegistrationEntity = JSONObject.parseObject(JSON.toJSONString(data.get("VehicleRegistrationEntity")), VehicleRegistrationEntity.class);
                if (tenantAddEntity != null) {
                    templateMessage = processTemplate(templateId, unionId, vehicleRegistrationEntity);
                }
                InviteVisitorsEntity inviteVisitorsEntity = JSONObject.parseObject(JSON.toJSONString(data.get("InviteVisitorsEntity")), InviteVisitorsEntity.class);
                if (tenantAddEntity != null) {
                    templateMessage = processTemplate(templateId, unionId, inviteVisitorsEntity);
                }
                break;
            case "2":
                ComplaintSuggestEntity complaintSuggestEntity = JSONObject.parseObject(JSON.toJSONString(data.get("ComplaintSuggestEntity")), ComplaintSuggestEntity.class);
                templateMessage = processTemplate(templateId, unionId, complaintSuggestEntity);
                break;
            case "3":
                VisitAuditRemarkEntity visitAuditRemarkEntity = JSONObject.parseObject(JSON.toJSONString(data.get("VisitAuditRemarkEntity")), VisitAuditRemarkEntity.class);
                templateMessage = processTemplate(templateId, unionId, visitAuditRemarkEntity);
                break;
            case "4":
                ComplaintStatusEntity complaintStatusEntity = JSONObject.parseObject(JSON.toJSONString(data.get("ComplaintStatusEntity")), ComplaintStatusEntity.class);
                templateMessage = processTemplate(templateId, unionId, complaintStatusEntity);
                break;
            case "5":
                RepairServiceEntity repairServiceEntity = JSONObject.parseObject(JSON.toJSONString(data.get("RepairServiceEntity")), RepairServiceEntity.class);
                templateMessage = processTemplate(templateId, unionId, repairServiceEntity);
                break;
            case "6":
                ReportRepairStatusEntity reportRepairStatusEntity = JSONObject.parseObject(JSON.toJSONString(data.get("ReportRepairStatusEntity")), ReportRepairStatusEntity.class);
                templateMessage = processTemplate(templateId, unionId, reportRepairStatusEntity);
                break;
            case "7":
                ExtensionCallEntity extensionCallEntity = JSONObject.parseObject(JSON.toJSONString(data.get("ExtensionCallEntity")), ExtensionCallEntity.class);
                templateMessage = processTemplate(templateId, unionId, extensionCallEntity);
                break;
            default:
                break;
        }
        return templateMessage;
    }

    /**
     * 验证unionId的方法
     *
     * @param unionId
     * @return
     */
    private static boolean verify(String unionId) {
        if (UserUtil.getUserMap().get(unionId) == null) {
            UserUtil.updateUserInfoToMap();
            log.info("user = " + UserUtil.getUserMap().get(unionId));
        }
        if (UserUtil.getUserMap().get(unionId) == null) {
            log.error("没有此unionId");
            return false;
        }
        return true;
    }

    /**
     * 我的房屋-添加房屋
     *
     * @param templateId
     * @param unionId
     * @param houseAddEntity
     * @return
     */
    private static TemplateMessage processTemplate(String templateId, String unionId, HouseAddEntity houseAddEntity) {
        if (!verify(unionId)) {
            return null;
        }

        Map<String, TemplateData> map = new HashMap<>();
        if (houseAddEntity.getResult()) {
            map.put("first", new TemplateData(TemplateHeaderEnum.HOUSE_ADD_SUCCESS.getHeaderValue(houseAddEntity.getHouseLocation()), null));
            map.put("keyword1", new TemplateData("已通过", null));
        } else {
            map.put("first", new TemplateData(TemplateHeaderEnum.HOUSE_ADD_FAILED.getHeaderValue(houseAddEntity.getHouseLocation()), null));
            map.put("keyword1", new TemplateData("未通过", null));
            map.put("keyword3", new TemplateData(houseAddEntity.getRemarks(), null));
        }
        map.put("keyword2", new TemplateData(houseAddEntity.getAuditTime(), null));
        String remark = houseAddEntity.getRemark();
        if (StrUtil.isNotEmpty(remark)) {
            map.put("remark", new TemplateData(remark, null));
        }

        TemplateMessage templateMessage = new TemplateMessage();
        templateMessage.setTouser(UserUtil.getUserMap().get(unionId));
        templateMessage.setTemplate_id(templateId);
        templateMessage.setData(map);

        return templateMessage;
    }

    /**
     * 我的房屋-添加家属
     *
     * @param templateId
     * @param unionId
     * @param kinAddEntity
     * @return
     */
    private static TemplateMessage processTemplate(String templateId, String unionId, KinAddEntity kinAddEntity) {
        if (!verify(unionId)) {
            return null;
        }

        Map<String, TemplateData> map = new HashMap<>();
        if (kinAddEntity.getResult()) {
            map.put("first", new TemplateData(TemplateHeaderEnum.KIN_ADD_SUCCESS.getHeaderValue(kinAddEntity.getKinName(),kinAddEntity.getHouseLocation()), null));
            map.put("keyword1", new TemplateData("已通过", null));
        } else {
            map.put("first", new TemplateData(TemplateHeaderEnum.KIN_ADD_FAILED.getHeaderValue(kinAddEntity.getKinName(),kinAddEntity.getHouseLocation()), null));
            map.put("keyword1", new TemplateData("未通过", null));
            map.put("keyword3", new TemplateData(kinAddEntity.getRemarks(), null));
        }
        map.put("keyword2", new TemplateData(kinAddEntity.getAuditTime(), null));
        String remark = kinAddEntity.getRemark();
        if (StrUtil.isNotEmpty(remark)) {
            map.put("remark", new TemplateData(remark, null));
        }

        TemplateMessage templateMessage = new TemplateMessage();
        templateMessage.setTouser(UserUtil.getUserMap().get(unionId));
        templateMessage.setTemplate_id(templateId);
        templateMessage.setData(map);

        return templateMessage;
    }

    /**
     * 我的房屋-添加租客
     *
     * @param templateId
     * @param unionId
     * @param tenantAddEntity
     * @return
     */
    private static TemplateMessage processTemplate(String templateId, String unionId, TenantAddEntity tenantAddEntity) {
        if (!verify(unionId)) {
            return null;
        }

        Map<String, TemplateData> map = new HashMap<>();
        if (tenantAddEntity.getResult()) {
            map.put("first", new TemplateData(TemplateHeaderEnum.TENANT_ADD_SUCCESS.getHeaderValue(tenantAddEntity.getTenantName(),tenantAddEntity.getHouseLocation()), null));
            map.put("keyword1", new TemplateData("已通过", null));
        } else {
            map.put("first", new TemplateData(TemplateHeaderEnum.TENANT_ADD_FAILED.getHeaderValue(tenantAddEntity.getTenantName(),tenantAddEntity.getHouseLocation()), null));
            map.put("keyword1", new TemplateData("未通过", null));
            map.put("keyword3", new TemplateData(tenantAddEntity.getRemarks(), null));
        }
        map.put("keyword2", new TemplateData(tenantAddEntity.getAuditTime(), null));
        String remark = tenantAddEntity.getRemark();
        if (StrUtil.isNotEmpty(remark)) {
            map.put("remark", new TemplateData(remark, null));
        }

        TemplateMessage templateMessage = new TemplateMessage();
        templateMessage.setTouser(UserUtil.getUserMap().get(unionId));
        templateMessage.setTemplate_id(templateId);
        templateMessage.setData(map);
        return templateMessage;
    }


    /**
     * 车辆管理
     *
     * @param templateId
     * @param unionId
     * @param vehicleRegistrationEntity
     * @return
     */
    private static TemplateMessage processTemplate(String templateId, String unionId, VehicleRegistrationEntity vehicleRegistrationEntity) {
        if (!verify(unionId)) {
            return null;
        }

        Map<String, TemplateData> map = new HashMap<>();
        if (vehicleRegistrationEntity.getResult()) {
            map.put("first", new TemplateData(TemplateHeaderEnum.VEHICLE_REGISTRATION_SUCCESS.getHeaderValue(vehicleRegistrationEntity.getNumberPlates()), null));
            map.put("keyword1", new TemplateData("已通过", null));
        } else {
            map.put("first", new TemplateData(TemplateHeaderEnum.VEHICLE_REGISTRATION_FAILED.getHeaderValue(vehicleRegistrationEntity.getNumberPlates()), null));
            map.put("keyword1", new TemplateData("未通过", null));
            map.put("keyword3", new TemplateData(vehicleRegistrationEntity.getRemarks(), null));
        }
        map.put("keyword2", new TemplateData(vehicleRegistrationEntity.getAuditTime(), null));
        String remark = vehicleRegistrationEntity.getRemark();
        if (StrUtil.isNotEmpty(remark)) {
            map.put("remark", new TemplateData(remark, null));
        }

        TemplateMessage templateMessage = new TemplateMessage();
        templateMessage.setTouser(UserUtil.getUserMap().get(unionId));
        templateMessage.setTemplate_id(templateId);
        templateMessage.setData(map);
        return templateMessage;
    }


    /**
     * 访客邀约
     *
     * @param templateId
     * @param unionId
     * @param inviteVisitorsEntity
     * @return
     */
    private static TemplateMessage processTemplate(String templateId, String unionId, InviteVisitorsEntity inviteVisitorsEntity) {
        if (!verify(unionId)) {
            return null;
        }

        Map<String, TemplateData> map = new HashMap<>();
        if (inviteVisitorsEntity.getResult()) {
            map.put("first", new TemplateData(TemplateHeaderEnum.INVITE_VISITORS_SUCCESS.getHeaderValue(inviteVisitorsEntity.getInvitorName()), null));
            map.put("keyword1", new TemplateData("已通过", null));
        } else {
            map.put("first", new TemplateData(TemplateHeaderEnum.INVITE_VISITORS_FAILED.getHeaderValue(inviteVisitorsEntity.getInvitorName()), null));
            map.put("keyword1", new TemplateData("未通过", null));
            map.put("keyword3", new TemplateData(inviteVisitorsEntity.getRemarks(), null));
        }
        map.put("keyword2", new TemplateData(inviteVisitorsEntity.getAuditTime(), null));
        String remark = inviteVisitorsEntity.getRemark();
        if (StrUtil.isNotEmpty(remark)) {
            map.put("remark", new TemplateData(remark, null));
        }

        TemplateMessage templateMessage = new TemplateMessage();
        templateMessage.setTouser(UserUtil.getUserMap().get(unionId));
        templateMessage.setTemplate_id(templateId);
        templateMessage.setData(map);
        return templateMessage;
    }


    /**
     * 投诉建议
     *
     * @param templateId
     * @param unionId
     * @param complaintSuggestEntity
     * @return
     */
    private static TemplateMessage processTemplate(String templateId, String unionId, ComplaintSuggestEntity complaintSuggestEntity) {
        if (!verify(unionId)) {
            return null;
        }
        Map<String, TemplateData> map = new HashMap<>();

        map.put("first", new TemplateData(TemplateHeaderEnum.COMPLAINT_SUGGEST_NOTICE.getHeaderValue(), null));
        map.put("keyword1", new TemplateData(complaintSuggestEntity.getContacts(), null));
        map.put("keyword2", new TemplateData(complaintSuggestEntity.getPhoneNumber(), null));
        map.put("keyword3", new TemplateData(complaintSuggestEntity.getRoomNo(), null));
        map.put("keyword4", new TemplateData(complaintSuggestEntity.getContents(), null));
        map.put("keyword5", new TemplateData(complaintSuggestEntity.getTime(), null));
        String remark = complaintSuggestEntity.getRemark();
        if (StrUtil.isNotEmpty(remark)) {
            map.put("remark", new TemplateData(remark, null));
        }
        TemplateMessage templateMessage = new TemplateMessage();
        templateMessage.setTouser(UserUtil.getUserMap().get(unionId));
        templateMessage.setTemplate_id(templateId);
        templateMessage.setData(map);
        return templateMessage;
    }

    /**
     * 访客预约来访审核
     *
     * @param templateId
     * @param unionId
     * @param visitAuditRemarkEntity
     * @return
     */
    private static TemplateMessage processTemplate(String templateId, String unionId, VisitAuditRemarkEntity visitAuditRemarkEntity) {
        if (!verify(unionId)) {
            return null;
        }
        Map<String, TemplateData> map = new HashMap<>();

        map.put("first", new TemplateData(TemplateHeaderEnum.VISIT_AUDIT_REMARK.getHeaderValue(), null));
        map.put("keyword1", new TemplateData(visitAuditRemarkEntity.getVisitorName(), null));
        map.put("keyword2", new TemplateData(visitAuditRemarkEntity.getVisitDate(), null));
        map.put("keyword3", new TemplateData(visitAuditRemarkEntity.getVisitReason(), null));

        String remark = visitAuditRemarkEntity.getRemark();
        if (StrUtil.isNotEmpty(remark)) {
            map.put("remark", new TemplateData(remark, null));
        }
        TemplateMessage templateMessage = new TemplateMessage();
        templateMessage.setTouser(UserUtil.getUserMap().get(unionId));
        templateMessage.setTemplate_id(templateId);
        templateMessage.setData(map);
        return templateMessage;
    }


    /**
     * 投诉处理进展
     *
     * @param templateId
     * @param unionId
     * @param complaintStatusEntity
     * @return
     */
    private static TemplateMessage processTemplate(String templateId, String unionId, ComplaintStatusEntity complaintStatusEntity) {
        if (!verify(unionId)) {
            return null;
        }
        Map<String, TemplateData> map = new HashMap<>();

        if (complaintStatusEntity.isItDone()) {
            map.put("first", new TemplateData(TemplateHeaderEnum.COMPLAINT_STATUS_COMPLETE.getHeaderValue(), null));
        } else {
            map.put("first", new TemplateData(TemplateHeaderEnum.COMPLAINT_STATUS_CHANGE.getHeaderValue(), null));
        }
        map.put("keyword1", new TemplateData(complaintStatusEntity.getComplainantsRoomNo(), null));
        map.put("keyword2", new TemplateData(complaintStatusEntity.getComplaintTheme(), null));
        map.put("keyword3", new TemplateData(complaintStatusEntity.getComplaintTime(), null));
        map.put("keyword4", new TemplateData(complaintStatusEntity.getCurrentProgress(), null));

        String remark = complaintStatusEntity.getRemark();
        if (StrUtil.isNotEmpty(remark)) {
            map.put("remark", new TemplateData(remark, null));
        }
        TemplateMessage templateMessage = new TemplateMessage();
        templateMessage.setTouser(UserUtil.getUserMap().get(unionId));
        templateMessage.setTemplate_id(templateId);
        templateMessage.setData(map);
        return templateMessage;
    }

    /**
     * 报修服务
     *
     * @param templateId
     * @param unionId
     * @param repairServiceEntity
     * @return
     */
    private static TemplateMessage processTemplate(String templateId, String unionId, RepairServiceEntity repairServiceEntity) {
        if (!verify(unionId)) {
            return null;
        }
        Map<String, TemplateData> map = new HashMap<>();
        map.put("first", new TemplateData(TemplateHeaderEnum.WEB_REPAIR_TASK_REMINDER.getHeaderValue(), null));
        map.put("keyword1", new TemplateData(repairServiceEntity.getContacts(), null));
        map.put("keyword2", new TemplateData(repairServiceEntity.getPhoneNumber(), null));
        map.put("keyword3", new TemplateData(repairServiceEntity.getDeliveryTime(), null));
        map.put("keyword4", new TemplateData(repairServiceEntity.getRepairContents(), null));

        String remark = repairServiceEntity.getRemark();
        if (StrUtil.isNotEmpty(remark)) {
            map.put("remark", new TemplateData(remark, null));
        }
        TemplateMessage templateMessage = new TemplateMessage();
        templateMessage.setTouser(UserUtil.getUserMap().get(unionId));
        templateMessage.setTemplate_id(templateId);
        templateMessage.setData(map);
        return templateMessage;
    }


    /**
     * 报修处理进展
     *
     * @param templateId
     * @param unionId
     * @param reportRepairStatusEntity
     * @return
     */
    private static TemplateMessage processTemplate(String templateId, String unionId, ReportRepairStatusEntity reportRepairStatusEntity) {
        if (!verify(unionId)) {
            return null;
        }
        Map<String, TemplateData> map = new HashMap<>();

        if (reportRepairStatusEntity.isItDone()) {
            map.put("first", new TemplateData(TemplateHeaderEnum.REPORT_REPAIR_STATUS_COMPLETE.getHeaderValue(), null));
        } else {
            map.put("first", new TemplateData(TemplateHeaderEnum.REPORT_REPAIR_STATUS_CHANGE.getHeaderValue(), null));
        }
        map.put("keyword1", new TemplateData(reportRepairStatusEntity.getReportRepairLocation(), null));
        map.put("keyword2", new TemplateData(reportRepairStatusEntity.getReportRepairTheme(), null));
        map.put("keyword3", new TemplateData(reportRepairStatusEntity.getReportRepairTime(), null));
        map.put("keyword4", new TemplateData(reportRepairStatusEntity.getCurrentProgress(), null));
        String estimatedCompletionDate = reportRepairStatusEntity.getEstimatedCompletionDate();
        if(StrUtil.isNotEmpty(estimatedCompletionDate)){
            map.put("keyword5", new TemplateData(reportRepairStatusEntity.getEstimatedCompletionDate(), null));
        }

        String remark = reportRepairStatusEntity.getRemark();
        if (StrUtil.isNotEmpty(remark)) {
            map.put("remark", new TemplateData(remark, null));
        }
        TemplateMessage templateMessage = new TemplateMessage();
        templateMessage.setTouser(UserUtil.getUserMap().get(unionId));
        templateMessage.setTemplate_id(templateId);
        templateMessage.setData(map);
        return templateMessage;
    }


    /**
     * 分机来电
     *
     * @param templateId
     * @param unionId
     * @param extensionCallEntity
     * @return
     */
    private static TemplateMessage processTemplate(String templateId, String unionId, ExtensionCallEntity extensionCallEntity) {
        if (!verify(unionId)) {
            return null;
        }
        Map<String, TemplateData> map = new HashMap<>();
        map.put("first", new TemplateData(TemplateHeaderEnum.EXTENSIONCALL.getHeaderValue(), null));
        map.put("keyword1", new TemplateData(extensionCallEntity.getCallerNumber(), null));
        map.put("keyword2", new TemplateData(extensionCallEntity.getCallerTime(), null));
        map.put("keyword3", new TemplateData(extensionCallEntity.getExtensionNumber(), null));


        String remark = extensionCallEntity.getRemark();
        if (StrUtil.isNotEmpty(remark)) {
            map.put("remark", new TemplateData(remark, null));
        }
        TemplateMessage templateMessage = new TemplateMessage();
        templateMessage.setTouser(UserUtil.getUserMap().get(unionId));
        templateMessage.setTemplate_id(templateId);
        templateMessage.setData(map);
//        templateMessage.setUrl("icloudobs.aurine.cn/wxp/pages/ownerpage/owner-main");
        MiniProgram miniProgram = new MiniProgram();
        miniProgram.setPath("pages/ownerpage/owner-main");
        miniProgram.setAppid("wxbb287801e93a2878");
        templateMessage.setMiniprogram(miniProgram);
        //templateMessage.setAppid("wxbb287801e93a2878");
        return templateMessage;
    }
}
