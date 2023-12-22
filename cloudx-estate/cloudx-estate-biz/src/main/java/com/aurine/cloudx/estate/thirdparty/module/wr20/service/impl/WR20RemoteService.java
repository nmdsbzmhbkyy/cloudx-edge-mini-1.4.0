package com.aurine.cloudx.estate.thirdparty.module.wr20.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.thirdparty.business.entity.constant.ThirdPartyBusinessPlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.ErrorMessageConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant.HuaweiErrorEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.dto.HuaweiRespondDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.remote.factory.HuaweiRemoteDeviceOperateServiceFactory;
import com.aurine.cloudx.estate.thirdparty.module.wr20.constant.WR20ActionConstant;
import com.aurine.cloudx.estate.thirdparty.module.wr20.constant.WR20ObjNameConstant;
import com.aurine.cloudx.estate.thirdparty.module.wr20.constant.WR20ServiceEnum;
import com.aurine.cloudx.estate.thirdparty.module.wr20.entity.dto.WR20VisitorObj;
import com.aurine.cloudx.estate.thirdparty.module.wr20.util.WR20Util;
import com.aurine.cloudx.estate.util.DockModuleConfigUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * <p>WR20连接remote服务</p>
 *
 * @ClassName: WR20RemoteServiceImpl
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-12-16 10:30
 * @Copyright:
 */
@Component
@Slf4j
public class WR20RemoteService {
    @Resource
    private WR20Util wr20Util;
    @Resource
    private DockModuleConfigUtil dockModuleConfigUtil;


    /**
     * 查询住户
     *
     * @param projectId
     * @param param     数据对象
     */
    public HuaweiRespondDTO queryTenement(int projectId, JSONObject param) {
        return HuaweiRemoteDeviceOperateServiceFactory.getInstance(
                wr20Util.getConfig(projectId).getVersion()).objectManage(wr20Util.getConfig(projectId),
                dockModuleConfigUtil.getWr20Config(projectId).getThirdCode(),
                WR20ServiceEnum.TENEMENT_MANAGER.code,
                WR20ActionConstant.GET,
                WR20ObjNameConstant.TENEMENT_INFO,
                param,
                getOtherParam());
    }

    /**
     * 查询住户 同步
     *
     * @param projectId
     * @param param     数据对象
     */
    public HuaweiRespondDTO queryTenementSync(int projectId, JSONObject param) {
        return HuaweiRemoteDeviceOperateServiceFactory.getInstance(wr20Util.getConfig(projectId).getVersion())
                .objectManageSync(wr20Util.getConfig(projectId),
                        null,
                        dockModuleConfigUtil.getWr20Config(projectId).getThirdCode(),
                        WR20ServiceEnum.TENEMENT_MANAGER.code,
                        WR20ActionConstant.GET,
                        WR20ObjNameConstant.TENEMENT_INFO,
                        param,
                        getOtherParam());
    }

    /**
     * 查询员工 同步
     *
     * @param projectId
     * @param param     数据对象
     */
    public HuaweiRespondDTO queryWorkSync(int projectId, JSONObject param) {
        return HuaweiRemoteDeviceOperateServiceFactory.getInstance(wr20Util.getConfig(projectId).getVersion())
                .objectManageSync(wr20Util.getConfig(projectId),
                        null,
                        dockModuleConfigUtil.getWr20Config(projectId).getThirdCode(),
                        WR20ServiceEnum.WORKER_MANAGER.code,
                        WR20ActionConstant.GET,
                        WR20ObjNameConstant.WORKER_INFO,
                        param,
                        getOtherParam());
    }

    /**
     * 同步住户
     *
     * @param projectId
     * @param param     数据对象
     */
    public HuaweiRespondDTO syncTenement(int projectId, JSONObject param) {
        return HuaweiRemoteDeviceOperateServiceFactory.getInstance(
                wr20Util.getConfig(projectId).getVersion()).objectManage(wr20Util.getConfig(projectId),
                dockModuleConfigUtil.getWr20Config(projectId).getThirdCode(),
                WR20ServiceEnum.TENEMENT_MANAGER.code,
                WR20ActionConstant.SYNC,
                WR20ObjNameConstant.TENEMENT_INFO,
                param,
                getOtherParam());
    }

    /**
     * 查询全部员工
     *
     * @param projectId
     * @param param     数据对象
     */
    public HuaweiRespondDTO syncStaff(int projectId, JSONObject param) {
        return HuaweiRemoteDeviceOperateServiceFactory.getInstance(
                wr20Util.getConfig(projectId).getVersion()).objectManage(wr20Util.getConfig(projectId),
                dockModuleConfigUtil.getWr20Config(projectId).getThirdCode(),
                WR20ServiceEnum.WORKER_MANAGER.code,
                WR20ActionConstant.GET,
                WR20ObjNameConstant.WORKER_INFO,
                param,
                getOtherParam());
    }

    /**
     * 同步框架
     *
     * @param projectId
     * @param param     数据对象
     */
    public HuaweiRespondDTO syncFrame(int projectId, JSONObject param) {


        return HuaweiRemoteDeviceOperateServiceFactory.getInstance(
                wr20Util.getConfig(projectId).getVersion()).objectManage(wr20Util.getConfig(projectId),
                dockModuleConfigUtil.getWr20Config(projectId).getThirdCode(),
                WR20ServiceEnum.FRAME_INFO_MANAGER.code,
                WR20ActionConstant.SYNC,
                WR20ObjNameConstant.FRAME_INFO,
                param,
                getOtherParam());
    }

    /**
     * 同步设备
     *
     * @param projectId
     * @param param     数据对象
     */
    public HuaweiRespondDTO syncDevice(int projectId, JSONObject param, String deviceType) {
        return this.handleResult(HuaweiRemoteDeviceOperateServiceFactory.getInstance(
                wr20Util.getConfig(projectId).getVersion()).objectManage(wr20Util.getConfig(projectId),
                dockModuleConfigUtil.getWr20Config(projectId).getThirdCode(),
                WR20ServiceEnum.DEVICE_INFO_MANAGER.code,
                WR20ActionConstant.SYNC,
                WR20ObjNameConstant.DEVICE_INFO,
                param,
                getOtherParam(deviceType)));
    }

    /**
     * 调用权限配置方法
     *
     * @param projectId
     * @param param     数据对象
     */
    public HuaweiRespondDTO permission(int projectId, JSONObject param, PersonTypeEnum personTypeEnum) {
        if (personTypeEnum == PersonTypeEnum.PROPRIETOR) {//住户
            return this.handleResult(HuaweiRemoteDeviceOperateServiceFactory.getInstance(
                    wr20Util.getConfig(projectId).getVersion()).objectManage(wr20Util.getConfig(projectId),
                    dockModuleConfigUtil.getWr20Config(projectId).getThirdCode(),
                    WR20ServiceEnum.TENEMENT_MANAGER.code,
                    WR20ActionConstant.PREMISSION,
                    WR20ObjNameConstant.TENEMENT_INFO,
                    param,
                    getOtherParam()));
        } else if (personTypeEnum == PersonTypeEnum.STAFF) {//员工
            return this.handleResult(HuaweiRemoteDeviceOperateServiceFactory.getInstance(
                    wr20Util.getConfig(projectId).getVersion()).objectManage(wr20Util.getConfig(projectId),
                    dockModuleConfigUtil.getWr20Config(projectId).getThirdCode(),
                    WR20ServiceEnum.WORKER_MANAGER.code,
                    WR20ActionConstant.PREMISSION,
                    WR20ObjNameConstant.WORKER_INFO,
                    param,
                    getOtherParam()));
        } else {
            return null;
        }
    }

    /**
     * 调用启用、停用住户、员工的方法
     * 由于WR20没有员工通行开关接口，需要转换为删除操作。
     *
     * @param projectId
     * @param param          数据对象
     * @param personTypeEnum 住户，员工
     * @param isActive       是否激活
     */
    public HuaweiRespondDTO passCtrl(int projectId, JSONObject param, PersonTypeEnum personTypeEnum, boolean isActive) {

        if (personTypeEnum == PersonTypeEnum.PROPRIETOR) {//住户
            HuaweiRespondDTO respondDTO = HuaweiRemoteDeviceOperateServiceFactory.getInstance(
                    wr20Util.getConfig(projectId).getVersion()).objectManage(wr20Util.getConfig(projectId),
                    dockModuleConfigUtil.getWr20Config(projectId).getThirdCode(),
                    WR20ServiceEnum.TENEMENT_MANAGER.code,
                    WR20ActionConstant.PASS_CTRL,
                    WR20ObjNameConstant.TENEMENT_INFO,
                    param,
                    getOtherParam());
            return this.handleResult(respondDTO);
        } else {//员工 无此接口，通过perssion接口，设备为空来实现开启关闭
//            return null;
//            return HuaweiRemoteDeviceOperateServiceFactory.getInstance(
////                    wr20Util.getConfig(projectId).getVersion()).objectManage(wr20Util.getConfig(projectId),
////                    dockModuleConfigUtil.getWr20Config(projectId).getThirdCode(),
////                    WR20ServiceEnum.WORKER_MANAGER.code,
////                    isActive ? WR20ActionConstant.ADD : WR20ActionConstant.DELETE,
////                    null,
////                    param,
////                    null);
            HuaweiRespondDTO respondDTO = HuaweiRemoteDeviceOperateServiceFactory.getInstance(
                    wr20Util.getConfig(projectId).getVersion()).objectManage(wr20Util.getConfig(projectId),
                    dockModuleConfigUtil.getWr20Config(projectId).getThirdCode(),
                    WR20ServiceEnum.TENEMENT_MANAGER.code,
                    WR20ActionConstant.PASS_CTRL,
                    WR20ObjNameConstant.TENEMENT_INFO,
                    param,
                    getOtherParam());
            return this.handleResult(respondDTO);
        }

    }

    /**
     * 增加卡
     *
     * @param projectId
     * @param param     数据对象
     */
    public HuaweiRespondDTO addCard(int projectId, JSONObject param, String uid) {
        HuaweiRespondDTO respondDTO = HuaweiRemoteDeviceOperateServiceFactory.getInstance(
                wr20Util.getConfig(projectId).getVersion()).objectManage(wr20Util.getConfig(projectId),
                dockModuleConfigUtil.getWr20Config(projectId).getThirdCode(),
                WR20ServiceEnum.CERT_CARD.code,
                WR20ActionConstant.ADD,
                WR20ObjNameConstant.CARD_OBJ,
                param,
                getOtherParam(null, uid));

        return handleResult(respondDTO);
    }

    /**
     * 删除卡
     *
     * @param projectId
     * @param param     数据对象
     */
    public HuaweiRespondDTO delCard(int projectId, JSONObject param) {
        HuaweiRespondDTO respondDTO = HuaweiRemoteDeviceOperateServiceFactory.getInstance(
                wr20Util.getConfig(projectId).getVersion()).objectManage(wr20Util.getConfig(projectId),
                dockModuleConfigUtil.getWr20Config(projectId).getThirdCode(),
                WR20ServiceEnum.CERT_CARD.code,
                WR20ActionConstant.DELETE,
                WR20ObjNameConstant.CARD_OBJ,
                param,
                getOtherParam());

        return handleResult(respondDTO);
    }

    /**
     * 增加卡
     *
     * @param projectId
     * @param param     数据对象
     */
    public void addFace(int projectId, JSONObject param, String uid) {
        HuaweiRespondDTO respondDTO = HuaweiRemoteDeviceOperateServiceFactory.getInstance(
                wr20Util.getConfig(projectId).getVersion()).objectManage(wr20Util.getConfig(projectId),
                dockModuleConfigUtil.getWr20Config(projectId).getThirdCode(),
                WR20ServiceEnum.CERT_FACE.code,
                WR20ActionConstant.ADD,
                WR20ObjNameConstant.FACE_OBJ,
                param,
                getOtherParam(null, uid));

        handleResult(respondDTO);
//        respondDTO.getBodyObj().getString("");
//        return "";
    }

    /**
     * 删除指定人脸
     *
     * @param projectId
     * @param param     数据对象
     */
    public HuaweiRespondDTO delFace(int projectId, JSONObject param) {
        HuaweiRespondDTO respondDTO = HuaweiRemoteDeviceOperateServiceFactory.getInstance(
                wr20Util.getConfig(projectId).getVersion()).objectManage(wr20Util.getConfig(projectId),
                dockModuleConfigUtil.getWr20Config(projectId).getThirdCode(),
                WR20ServiceEnum.CERT_FACE.code,
                WR20ActionConstant.DELETE_BY_ID,
                WR20ObjNameConstant.FACE_OBJ,
                param,
                getOtherParam());

        return handleResult(respondDTO);
    }


    /**
     * 保存住户
     *
     * @param projectId
     * @param param
     * @param uid       4.0 住户关系id
     * @return
     */
    public String saveHousePersonRel(int projectId, JSONObject param, String uid) {

        HuaweiRespondDTO respondDTO = HuaweiRemoteDeviceOperateServiceFactory.getInstance(
                VersionEnum.V1).objectManage(wr20Util.getConfig(projectId),
                dockModuleConfigUtil.getWr20Config(projectId).getThirdCode(),
                WR20ServiceEnum.TENEMENT_MANAGER.code,
                WR20ActionConstant.ADD,
                WR20ObjNameConstant.TENEMENT_INFO,
                param,
                getOtherParam(null, uid));

        handleResult(respondDTO);

        if (respondDTO.getErrorEnum() == HuaweiErrorEnum.SUCCESS) {
            log.info("[WR20] 添加住户成功：{}", respondDTO);
        }
        return "";
    }

    /**
     * 修改住户
     *
     * @param projectId
     * @param param
     * @param uid       4.0 住户关系id
     * @return
     */
    public String updateHousePersonRel(int projectId, JSONObject param, String uid) {

        HuaweiRespondDTO respondDTO = HuaweiRemoteDeviceOperateServiceFactory.getInstance(
                VersionEnum.V1).objectManage(wr20Util.getConfig(projectId),
                dockModuleConfigUtil.getWr20Config(projectId).getThirdCode(),
                WR20ServiceEnum.TENEMENT_MANAGER.code,
                WR20ActionConstant.UPDATE,
                WR20ObjNameConstant.TENEMENT_INFO,
                param,
                getOtherParam(null, uid));

        handleResult(respondDTO);

        if (respondDTO.getErrorEnum() == HuaweiErrorEnum.SUCCESS) {
            log.info("[WR20] 修改住户成功：{}", respondDTO);
        }
        return "";
    }

    /**
     * 迁出住户
     *
     * @param projectId
     * @param param
     * @param uid       4.0 住户关系id
     * @return
     */
    public String removeHousePersonRel(int projectId, JSONObject param, String uid) {

        HuaweiRespondDTO respondDTO = HuaweiRemoteDeviceOperateServiceFactory.getInstance(
                VersionEnum.V1).objectManage(wr20Util.getConfig(projectId),
                dockModuleConfigUtil.getWr20Config(projectId).getThirdCode(),
                WR20ServiceEnum.TENEMENT_MANAGER.code,
                WR20ActionConstant.DELETE,
                WR20ObjNameConstant.TENEMENT_INFO,
                param,
                getOtherParam(null, uid));
        handleResult(respondDTO);

        if (respondDTO.getErrorEnum() == HuaweiErrorEnum.SUCCESS) {
            log.info("[WR20] 删除住户成功：{}", respondDTO);
        }
        return "";
    }


    /**
     * 保存员工
     *
     * @param projectId
     * @param param
     * @param uid
     * @return
     */
    public String saveStaff(int projectId, JSONObject param, String uid) {

        HuaweiRespondDTO respondDTO = HuaweiRemoteDeviceOperateServiceFactory.getInstance(
                VersionEnum.V1).objectManage(wr20Util.getConfig(projectId),
                dockModuleConfigUtil.getWr20Config(projectId).getThirdCode(),
                WR20ServiceEnum.WORKER_MANAGER.code,
                WR20ActionConstant.ADD,
                WR20ObjNameConstant.WORKER_INFO,
                param,
                getOtherParam(null, uid));

        handleResult(respondDTO);

        if (respondDTO.getErrorEnum() == HuaweiErrorEnum.SUCCESS) {
            log.info("[WR20] 添加员工成功：{}", respondDTO);
        }
        return "";
    }

    /**
     * 删除员工
     *
     * @param projectId
     * @param param
     * @param uid
     * @return
     */
    public String removeStaff(int projectId, JSONObject param, String uid) {

        HuaweiRespondDTO respondDTO = HuaweiRemoteDeviceOperateServiceFactory.getInstance(
                VersionEnum.V1).objectManage(wr20Util.getConfig(projectId),
                dockModuleConfigUtil.getWr20Config(projectId).getThirdCode(),
                WR20ServiceEnum.WORKER_MANAGER.code,
                WR20ActionConstant.DELETE,
                WR20ObjNameConstant.WORKER_INFO,
                param,
                getOtherParam(null, uid));
        handleResult(respondDTO);

        if (respondDTO.getErrorEnum() == HuaweiErrorEnum.SUCCESS) {
            log.info("[WR20] 删除员工成功：{}", respondDTO);
        }
        return "";
    }

    /**
     * 登记访客
     *
     * @param projectId
     * @param visitorObj
     * @param uid
     * @return
     */
    public String addVisitor(int projectId, WR20VisitorObj visitorObj, String uid) {

        HuaweiRespondDTO respondDTO = HuaweiRemoteDeviceOperateServiceFactory.getInstance(
                VersionEnum.V1).objectManage(wr20Util.getConfig(projectId),
                dockModuleConfigUtil.getWr20Config(projectId).getThirdCode(),
                WR20ServiceEnum.VISITOR_MANAGER.code,
                WR20ActionConstant.ADD,
                WR20ObjNameConstant.VISITOR_INFO,
                JSONObject.parseObject(JSONObject.toJSONString(visitorObj)),
                getOtherParam(null, uid));
        handleResult(respondDTO);

        if (respondDTO.getErrorEnum() == HuaweiErrorEnum.SUCCESS) {
            log.info("[WR20] 添加访客成功：{}", respondDTO);
        }
        return "";
    }

    /**
     * 迁出访客
     *
     * @param projectId
     * @param param
     * @param uid
     * @return
     */
    public String delVisitor(int projectId, JSONObject param, String uid) {

        HuaweiRespondDTO respondDTO = HuaweiRemoteDeviceOperateServiceFactory.getInstance(
                VersionEnum.V1).objectManage(wr20Util.getConfig(projectId),
                dockModuleConfigUtil.getWr20Config(projectId).getThirdCode(),
                WR20ServiceEnum.VISITOR_MANAGER.code,
                WR20ActionConstant.DELETE,
                WR20ObjNameConstant.VISITOR_INFO,
                param,
                getOtherParam(null, uid));
        handleResult(respondDTO);

        if (respondDTO.getErrorEnum() == HuaweiErrorEnum.SUCCESS) {
            log.info("[WR20] 删除访客成功：{}", respondDTO);
        }
        return "";
    }


    /**
     * 重新下发凭证
     *
     * @param projectId
     * @param param
     * @param uid
     * @return
     */
    public String redown(int projectId, JSONObject param, String uid) {

        HuaweiRespondDTO respondDTO = HuaweiRemoteDeviceOperateServiceFactory.getInstance(
                VersionEnum.V1).objectManage(wr20Util.getConfig(projectId),
                dockModuleConfigUtil.getWr20Config(projectId).getThirdCode(),
                WR20ServiceEnum.DEVICE_INFO_MANAGER.code,
                WR20ActionConstant.REDOWN,
                WR20ObjNameConstant.DEVICE_INFO,
                param,
                getOtherParam(null, uid));
        handleResult(respondDTO);

        if (respondDTO.getErrorEnum() == HuaweiErrorEnum.SUCCESS) {
            log.info("[WR20] 重新下发凭证成功：{}", respondDTO);
        }
        return "";
    }

    /**
     * 一房多人 迁入
     *
     * @param projectId
     * @param param
     * @param uid
     * @return
     */
    public String checkin(int projectId, JSONObject param, String uid) {

        HuaweiRespondDTO respondDTO = HuaweiRemoteDeviceOperateServiceFactory.getInstance(
                VersionEnum.V1).objectManage(wr20Util.getConfig(projectId),
                dockModuleConfigUtil.getWr20Config(projectId).getThirdCode(),
                WR20ServiceEnum.TENEMENT_MANAGER.code,
                WR20ActionConstant.CHECKIN,
                WR20ObjNameConstant.TENEMENT_INFO,
                param,
                getOtherParam(null, uid));
        handleResult(respondDTO);

        if (respondDTO.getErrorEnum() == HuaweiErrorEnum.SUCCESS) {
            log.info("[WR20] 重新下发凭证成功：{}", respondDTO);
        }
        return "";
    }

    /**
     * 一房多人 迁出
     *
     * @param projectId
     * @param param
     * @param uid
     * @return
     */
    public String checkout(int projectId, JSONObject param, String uid) {

        HuaweiRespondDTO respondDTO = HuaweiRemoteDeviceOperateServiceFactory.getInstance(
                VersionEnum.V1).objectManage(wr20Util.getConfig(projectId),
                dockModuleConfigUtil.getWr20Config(projectId).getThirdCode(),
                WR20ServiceEnum.TENEMENT_MANAGER.code,
                WR20ActionConstant.CHECKOUT,
                WR20ObjNameConstant.TENEMENT_INFO,
                param,
                getOtherParam(null, uid));
        handleResult(respondDTO);

        if (respondDTO.getErrorEnum() == HuaweiErrorEnum.SUCCESS) {
            log.info("[WR20] 重新下发凭证成功：{}", respondDTO);
        }
        return "";
    }


    private HuaweiRespondDTO handleResult(HuaweiRespondDTO respondDTO) {
        if (respondDTO.getErrorEnum() != HuaweiErrorEnum.SUCCESS) {
            log.error("[WR20] 操作失败：{}", respondDTO.getErrorMsg());
            if (respondDTO.getErrorMsg().indexOf("The device is not online") >= 0) {
                throw new RuntimeException(ErrorMessageConstant.ERROR_WR20_GATEWAY_OFFLINE);
            } else {
                throw new RuntimeException(ErrorMessageConstant.ERROR);
            }
        }
        return respondDTO;
    }

    private JSONObject getOtherParam(String deviceType) {
        JSONObject otherParamsObj = new JSONObject();
        otherParamsObj.put("source", ThirdPartyBusinessPlatformEnum.WR20.code);
        otherParamsObj.put("sourceVersion", VersionEnum.V1.code);

        if (StringUtils.isNotEmpty(deviceType)) {
            otherParamsObj.put("wr20DeviceType", deviceType);
        }
        return otherParamsObj;
    }

    private JSONObject getOtherParam(String deviceType, String uuid) {
        JSONObject otherParamsObj = this.getOtherParam(deviceType);

        if (StringUtils.isNotEmpty(uuid)) {
            otherParamsObj.put("uuid", uuid);
        }

        return otherParamsObj;
    }

    private JSONObject getOtherParam() {
        return this.getOtherParam(null);
    }


}
