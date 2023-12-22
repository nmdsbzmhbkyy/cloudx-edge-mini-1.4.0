package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.constant.enums.CertmediaTypeEnum;
import com.aurine.cloudx.estate.constant.enums.PassRightCertDownloadStatusEnum;
import com.aurine.cloudx.estate.entity.ProjectPersonDevice;
import com.aurine.cloudx.estate.entity.ProjectRightDevice;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

/**
 * 权限设备关系表，记录权限（认证介质）的下发状态
 *
 * @author 王良俊
 * @date 2020-05-21 09:52:28
 */
public interface ProjectRightDeviceService extends IService<ProjectRightDevice> {


    /**
     * <p>
     * 启用
     * </p>
     *
     * @param personId 人员id
     * @return 处理结果
     * @author: 王良俊
     */
    boolean active(String personId);

    /**
     * <p>
     * 禁用
     * </p>
     *
     * @param personId 人员id
     * @return 处理结果
     * @author: 王良俊
     */
    boolean deactive(String personId);

    /**
     * <p>
     * 在移除了某个介质后根据介质id删除这个介质对应的设备关系
     * </p>
     *
     * @param certmediaId 介质id
     * @return 处理结果
     * @author: 王良俊
     */
    boolean removeCertmedia(String certmediaId);

    /**
     * <p>
     * 根据介质id列表删除介质关系（这个方法在介质的service中调用，用于访客增加或减少哪些介质的权限移除以及其他页面介质实时删除）
     * </p>
     *
     * @param certmediaIds 介质id列表
     * @return 处理结果
     * @author: 王良俊
     */
    boolean removeCertmedias(List<String> certmediaIds);

    /**
     * <p>
     * 在配置了不同新的设备列表后更新介质和设备的关系
     * </p>
     *
     * @param personId 人员id
     * @return 处理结果
     * @author: 王良俊
     */
    boolean saveRelationship(List<String> deviceIds, String personId);

    /**
     * <p>
     * 根据黑名单人脸id添加人脸黑名单介质和设备的关系
     *
     * </p>
     * @param blacklistFaceId 人脸黑名单faceId(project_blacklist_attr的faceId,project_face_resources的faceId)
     * @return 处理结果
     * @author: 顾文豪
     */
    boolean saveFaceBlacklistByFaceId(String blacklistFaceId);

    /**
     * <p>
     * 根据设备id添加人脸黑名单介质和设备的关系
     *
     * </p>
     * @param deviceId 设备id
     * @return 处理结果
     * @author: 顾文豪
     */
    boolean saveFaceBlacklistByDeviceId(String deviceId);

    /**
     * <p>
     * 删除人脸黑名单介质和设备的关系
     *
     * </p>
     * @param blacklistFaceId 人脸黑名单faceId(project_blacklist_attr的faceId,project_face_resources的faceId)
     * @return 处理结果
     * @author: 顾文豪
     */
    boolean delFaceBlacklistByFaceId(String blacklistFaceId);

    /**
     * <p>
     * 在配置了不同新的设备列表后更新介质和设备的关系
     * </p>
     *
     * @param personId 人员id
     * @return 处理结果
     * @author: 王良俊
     */

    void saveRelationshipProxy(String planId, String personId,Integer projectId);


    /**
     * 下发或删除操作
     *
     * @param rightDeviceList
     * @return
     * @author: 王伟
     */
    public boolean remoteInterfaceByDevices(List<ProjectRightDevice> rightDeviceList, boolean isAdd);

    /**
     * <p>
     * 重新下发介质
     * </p>
     *
     * @param rightDeviceIdList
     * @return 处理结果
     * @author: 王良俊
     */
    boolean resendBatch(List<String> rightDeviceIdList);


    /**
     * 根据设备ID和下载状态，重新对凭证进行下发操作
     *
     * @param deviceId
     * @param passRightCertDownloadStatusEnum
     * @return
     * @author: 王伟
     * @since 2020-09-10
     */
    boolean resendByDeviceId(String deviceId, PassRightCertDownloadStatusEnum passRightCertDownloadStatusEnum);

    /**
     * <p>
     * 在配置了不同新的设备列表后更新介质和设备的关系
     * </p>
     *
     * @param personId 人员id
     * @return 处理结果
     * @author: 王良俊
     */
    boolean removeCertDeviceAuthorize(String personId);

    /**
     * <p>
     * 删除这些人员的介质
     * </p>
     *
     * @param personIdList 人员ID集合
    */
    boolean removeCertDeviceAuthorize(List<String> personIdList);

    /**
     * 根据设备删除凭证，仅限于删除设备接口调用
     *
     * @param deviceId
     * @return
     * @since： 2020-09-03
     * @author: 王伟
     */
    boolean removeByDevice(String deviceId);

    /**
     * <p>
     * 根据传入的介质id和设备id下发操作
     * </p>
     *
     * @param personId  介质id列表
     * @param deviceIds 设备id列表
     * @return 处理结果
     * @author: 王良俊
     */
    boolean authPersonCertmdiaDevice(List<String> deviceIds, String personId);


    /**
     * <p>
     * 新添加的设备要进行权限下发操作
     * </p>
     *
     * @param deviceId  新添加的设备id
     * @param personIds 需要授权该设备的人员id列表
     * @return 处理结果
     * @author: 王良俊
     */
    boolean authNewDeviceById(String deviceId, List<String> personIds);

    /**
     * 统一新增凭证到设备的方法
     * 注意：这里的凭证都是设备上不存在的
     *
     * @param rightDeviceList
     * @return
     * @author: 王伟
     * @since 2020-08-18
     */
    boolean addCerts(List<ProjectRightDevice> rightDeviceList);

    /**
     * 统一新增凭证到设备的方法
     * 注意：这里的凭证都是设备上不存在的
     *
     * @param rightDeviceList
     * @return
     * @author: 王伟
     * @since 2020-08-18
     */
    boolean addCertsAndCardHis(List<ProjectRightDevice> rightDeviceList,boolean isCardHis);

    /**
     * 统一新增凭证到设备的方法
     * 注意：这里的凭证都是设备上不存在的
     *
     * @param rightDeviceList
     * @return
     * @author: 王伟
     * @since 2020-08-18
     */
    boolean addCerts(List<ProjectRightDevice> rightDeviceList, boolean changeStatus, boolean transactional,boolean isCardHis);

    /**
     * 统一新增凭证到设备的方法
     * 注意：这里的凭证都是设备上不存在的
     *
     * @param rightDeviceList
     * @return
     * @author: 王伟
     * @since 2020-08-18
     */
    boolean addCerts(List<ProjectRightDevice> rightDeviceList, boolean changeStatus);

    /**
     * 统一删除凭证到设备的方法
     *
     * @param rightDeviceList
     * @return
     * @author: 王伟
     * @since 2020-08-18
     */
    boolean delCerts(List<ProjectRightDevice> rightDeviceList);


    /**
     * 统一删除凭证到设备的方法
     *
     * @param rightDeviceList
     * @param changeStatus    是否变更当前系统的数据状态
     * @return
     * @author: 王伟
     * @since 2020-09-14
     */
    boolean delCerts(List<ProjectRightDevice> rightDeviceList, boolean changeStatus);


    /**
     * 统一删除凭证到设备的方法
     *
     * @param rightDeviceList
     * @param changeStatus    是否变更当前系统的数据状态
     * @return
     * @author: 王伟
     * @since 2020-09-14
     */
    boolean delCerts(List<ProjectRightDevice> rightDeviceList, boolean changeStatus,boolean tansactional );

    /**
     * 清空设备凭证
     * 不引起设备数据状态变动，仅清空设备
     * 注意，该接口会导致设备和系统数据不同
     * 通过重新下发业务可以恢复
     *
     * @param deviceId          要清空的设备
     * @param certmediaTypeEnum 要清空的凭证类型枚举
     * @return
     */
    boolean clearCerts(String deviceId, CertmediaTypeEnum certmediaTypeEnum);

    /**
     * 统一冻结凭证到设备的方法
     *
     * @param rightDeviceList
     * @return
     * @author: 王伟
     * @since 2020-08-18
     */
    boolean freezeCerts(List<ProjectRightDevice> rightDeviceList);

    /**
     * 获取需要添加的关系列表
     *
     * @param rightDeviceList
     * @return
     * @author: 王伟
     * @since 2020-08-20
     */
    List<ProjectRightDevice> getAddList(List<ProjectRightDevice> rightDeviceList);

    /**
     * 获取需要删除的关系列表
     *
     * @param rightDeviceList
     * @return
     * @author: 王伟
     * @since 2020-08-20
     */
    List<ProjectRightDevice> getDelList(List<ProjectRightDevice> rightDeviceList);

    /**
     * 改变设备权限关联配置，根据原有数据自动分析差异并进行业务处理
     *
     * @param rightDeviceList
     * @return
     * @author: 王伟
     * @since 2020-08-20
     */
    boolean changeRightDevice(List<ProjectRightDevice> rightDeviceList);

    /**
     * 根据人员设备列表，变更凭证设备列表，适用于通行权限配置业务
     *
     * @return
     * @author: 王伟
     * @since 2020-08-20
     */
    boolean changeByPersonDevice(List<ProjectPersonDevice> newPersonDeviceList, List<ProjectPersonDevice> removePersonDeviceList, String personId);

    /**
     * <p>
     * 根据介质类型重新下载该设备上这个类型的介质
     * </p>
     *
     * @param deviceId 要进行对应操作的设备ID
     * @param certType 介质类型
     * @return 操作结果
     * @author: 王良俊
     */
    boolean redownloadCertByType(String deviceId, String certType);

    /**
     * 保存或修改卡下发状态
     *
     * @param cardNo
     * @param deviceIdList
     */
    void saveOrUpdateCard(String cardNo, List<String> deviceIdList, PassRightCertDownloadStatusEnum downloadStatusEnum);
    /**
     * 移除凭证在所有设备下的记录，用于凭证全部删除成功时调用。
     *
     * @param certId
     */
    void removeCert(String certId);

    /**
     * 移除凭证在所有设备下的记录，用于凭证全部删除成功时调用。
     *
     * @param certCode
     */
    void removeCertByCode(String certCode);

    /**
     * 保存或修改人脸下发状态
     *
     * @param faceId
     * @param deviceIdList
     * @param downloadStatusEnum
     */
    void saveOrUpdateFace(String faceId, List<String> deviceIdList, PassRightCertDownloadStatusEnum downloadStatusEnum);

    /**
     * <p>
     * 根据介质ID获取到这个人员的房屋编号
     * </p>
     * 
     * @param certId 介质ID
     * @author: 王良俊
    */
    List<String> getHouseCode(String certId);


    /**
     * 根据凭证id列表、设备id和状态 修改凭证的下载状态
     * 与项目id不关联，
     *
     * @param certIdList
     * @param statusEnum
     * @return
     */
    boolean updateStateByIds(List<String> certIdList, String deviceId, PassRightCertDownloadStatusEnum statusEnum);

    /**
     * <p>
     * 返回存储人脸、指纹、卡片的id列表
     * </p>
     *
     * @param personId 人员id
     * @return 返回介质id列表（所有介质的）
     * @author: 王良俊
     */
    List<ProjectRightDevice> listCertInfoByPersonId(String personId);


    /**
     * <p>
     * 根据人员ID获取这些人的介质vo对象
     * </p>
     *
     * @param personIdList 人员ID集合
     * @author: 王良俊
    */
    List<ProjectRightDevice> listCertInfoByPersonId(List<String> personIdList);

    /**
     * <p>
     * 根据人脸黑名单faceId获取介质vo对象
     * </p>
     *
     * @param blacklistFaceId 人脸黑名单faceId
     * @return 返回人脸黑名单介质vo对象
     * @author: 顾文豪
     */
    List<ProjectRightDevice> listCertByBlacklistByFaceId(String blacklistFaceId);

    /**
     * <p>
     * 根据设备id插叙你设备权限记录
     * </p>
     *
     * @param deviceId 设备id
     * @param query    查询条件
     * @param page     分页对象
     * @return 分页数据
     * @author: 王良俊
     */
    Page<ProjectRightDeviceVo> pageByDeviceId(Page page, ProjectRightDeviceOptsAccessSearchConditionVo query, String deviceId);

    /**
     * <p>
     * 分页查询授权状态 (授权管理页)
     * </p>
     *
     * @param searchCondition 查询条件
     * @author: 王良俊
     */
    Page<ProjectPersonRightStatusVo> pagePersonRightStatus(Page page, PersonRightStatusSearchCondition searchCondition);


    /**
     * <p>
     * 根据设备id统计资质数量
     * </p>
     */
    List<ProjectCertStateVo>  countDeviceId(String deviceId);


    /**
     * <p>
     * 根据设备id统计下载失败的数量
     * </p>
     */
    Integer countFailNumbs(List<String> deviceIdList);


    /**
     * <p>
     * 重新下发失败凭证
     * </p>
     *
     * @param deviceIdList
     * @return 处理结果
     */
    boolean resendFailCert(List<String> deviceIdList);

    /**
    * <p>
    * 根据人员ID获取这个人的所有介质下发情况
    * </p>
    *
    * @param page 分页条件
    * @param page 查询条件
    * @author: 王良俊
    */
    Page<ProjectRightStatusVo> pageRightStatus(Page page, RightStatusSearchCondition query);

    /**
    * <p>
    * 根据人员ID重新下发该人员下发失败的介质
    * </p>
    *
    * @param personId 人员ID
    * @author: 王良俊
    */
    boolean resendFailedByPersonId(String personId);

    /**
    * <p>
    * 统计介质下发失败的人员数量
    * </p>
    *
    * @author: 王良俊
    */
    Integer countFailedPersonNUm();

    /**
    * <p>
    * 重新下发所有下发失败的介质
    * </p>
    *
    * @author: 王良俊
    */
    boolean resendAllFailed();


    /**
     * <p>过滤出可以进行介质各类操作的rightDevice列表</p>
     *
     * @param
     * @return
     * @author: 王良俊
     */
    public List<ProjectRightDevice> filterRightDeviceList(List<ProjectRightDevice> rightDeviceList);

    List<String> getHouseCodeByUnit(String certMediaId, String deviceId);
    List<ProjectRightDevice> getCertMultiUserList(String personId);
}