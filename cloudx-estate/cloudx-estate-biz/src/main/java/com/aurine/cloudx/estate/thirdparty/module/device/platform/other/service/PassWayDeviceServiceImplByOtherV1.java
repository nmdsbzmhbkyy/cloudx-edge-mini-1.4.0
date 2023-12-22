package com.aurine.cloudx.estate.thirdparty.module.device.platform.other.service;

import com.aurine.cloudx.estate.constant.enums.CertmediaTypeEnum;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectNotice;
import com.aurine.cloudx.estate.entity.ProjectRightDevice;
import com.aurine.cloudx.estate.service.ProjectDeviceInfoService;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.PassWayDeviceService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.model.RemoteOpenDoorResultModel;
import com.aurine.cloudx.estate.vo.zn.SetSupperPasscodeVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 置空 V1 版本 对接业务实现
 *
 * @ClassName:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-07-31 13:44
 * @Copyright:
 */
@Service
public class PassWayDeviceServiceImplByOtherV1 implements PassWayDeviceService {

    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;

    /**
     * 开门
     *
     * @param deviceId
     * @return
     */
    @Override
    public boolean openDoor(String deviceId) {
        return true;
    }

    @Override
    public boolean remoteOpenDoor(String deviceId, RemoteOpenDoorResultModel resultModel,String msgId) {
        return true;
    }

    @Override
    public boolean setZnSupperPasscode(ProjectDeviceInfo deviceInfo, SetSupperPasscodeVo vo, String msgId) {
        return false;
    }

    /**
     * 开门 带开门者信息
     *
     * @param deviceId
     * @param personId   人员id
     * @param personType 人员类型
     * @return
     */
    @Override
    public boolean openDoor(String deviceId, String personId, String personType) {
        return true;
    }


    /**
     * 添加通行凭证
     *
     * @param certList
     * @return
     */
    @Override
    public void addCert(List<ProjectRightDevice> certList) {
    }

    /**
     * 删除通行权限
     *
     * @param certList
     * @return
     */
    @Override
    public void delCert(List<ProjectRightDevice> certList) {
    }

    /**
     * 清空设备凭证
     *
     * @param deviceId          要清空的设备
     * @param certmediaTypeEnum 要清空的凭证类型枚举
     * @return
     */
    @Override
    public boolean clearCerts(String deviceId, CertmediaTypeEnum certmediaTypeEnum) {
        return true;
    }

    /**
     * 为梯口机、区口机发送消息
     *
     * @param deviceId
     * @param notice
     */
    @Override
    public boolean sendTextMessage(String deviceId, ProjectNotice notice) {
        return true;
    }

    @Override
    public void cleanTextMessage(String deviceId) {

    }

    /**
     * 获取版本
     *
     * @return
     */
    @Override
    public String getVersion() {
        return VersionEnum.V1.code;
    }

    /**
     * 获取平台类型
     *
     * @return
     */
    @Override
    public String getPlatform() {
        return PlatformEnum.OTHER.code;
    }


}
