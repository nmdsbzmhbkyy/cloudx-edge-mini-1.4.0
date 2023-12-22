

package com.aurine.cloudx.estate.thirdparty.module.device.platform;


import com.aurine.cloudx.estate.constant.enums.CertmediaTypeEnum;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectNotice;
import com.aurine.cloudx.estate.entity.ProjectRightDevice;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.model.RemoteOpenDoorResultModel;
import com.aurine.cloudx.estate.vo.zn.SetSupperPasscodeVo;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 统一 区口机 接口
 *
 * @ClassName: GateDeviceService
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-07-28 15:29
 * @Copyright:
 */
public interface PassWayDeviceService extends BaseService {

    /**
     * 开门
     *
     * @param deviceId 设备id
     * @return
     */
    boolean openDoor(String deviceId);

    /**
     * @param deviceId
     * @param resultModel
     * @return boolean
     * @throws //
     * @description 平台校验开门接口
     * @author cyw
     * @time 2023/6/12 15:58
     */
    boolean remoteOpenDoor(String deviceId, RemoteOpenDoorResultModel resultModel,String msgId);

    /**
     * \设置zn多门控制器超级密码
     * @param vo
     * @param msgId
     * @return
     */

    boolean setZnSupperPasscode(ProjectDeviceInfo deviceInfo, SetSupperPasscodeVo vo, String msgId);

    /**
     * 开门 带开门者信息
     * @param deviceId      必填
     * @param personId      人员id
     * @param personType    人员类型
     * @return
     */
    boolean openDoor(String deviceId, String personId, String personType);


    /**
     * 添加通行凭证
     *
     * @param certList
     * @return
     */
    void addCert(List<ProjectRightDevice> certList);

//    boolean addCerts();

    /**
     * 删除通行权限
     *
     * @param certList
     * @return
     */
    void delCert(List<ProjectRightDevice> certList);


    /**
     * 清空设备凭证
     *
     * @param deviceId          要清空的设备
     * @param certmediaTypeEnum 要清空的凭证类型枚举
     * @return
     */
    boolean clearCerts(String deviceId, CertmediaTypeEnum certmediaTypeEnum);

    /**
     * 为梯口机、区口机发送消息
     *
     * @param notice
     */
    boolean sendTextMessage(String deviceId, ProjectNotice notice);

    /**
     * 清空梯口/区口消息
     *
     * @param deviceId
     */
    void cleanTextMessage(String deviceId);


}
