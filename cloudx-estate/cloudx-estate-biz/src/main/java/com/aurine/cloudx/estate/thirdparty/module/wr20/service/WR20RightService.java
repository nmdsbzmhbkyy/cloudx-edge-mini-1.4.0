

package com.aurine.cloudx.estate.thirdparty.module.wr20.service;


import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.entity.ProjectCard;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectFaceResources;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant.HuaweiEventEnum;

import java.util.List;

/**
 * WR20 权限接口
 *
 * @ClassName: WR20FrameService
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-04 10:53
 * @Copyright:
 */
public interface WR20RightService extends BaseService {

    /**
     * 增加卡片
     *
     * @param card 卡片信息
     * @return
     */
    boolean addCard(int projectId, ProjectCard card);

    /**
     * 删除卡片
     *
     * @param card 卡片信息
     * @return
     */
    boolean delCard(int projectId, ProjectCard card);

    /**
     * 增加面部识别
     *
     * @param face 人脸识别
     * @return
     */
    boolean addFace(int projectId, ProjectFaceResources face);

    /**
     * 删除全部面部识别
     *
     * @param face 人脸识别
     * @return
     */
    boolean delFace(int projectId, ProjectFaceResources face);

    /**
     * 生成下载中的下发记录
     *
     * @return
     */
    void createDownloadingCert(int projectId, HuaweiEventEnum eventEnum, List<String> deviceCodeList, ProjectCard card, ProjectFaceResources face,boolean thirdCode);

    /**
     * 获取住户\员工的权限列表，返回第三方设备ID集合
     * @param personId
     * @param personTypeEnum
     * @return
     */
    List<ProjectDeviceInfo> getPersonRightList(String personId, PersonTypeEnum personTypeEnum);




}
