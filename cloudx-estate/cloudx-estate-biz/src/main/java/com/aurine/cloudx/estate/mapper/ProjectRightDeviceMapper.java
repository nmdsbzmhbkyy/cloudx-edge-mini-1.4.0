

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.dto.OpenApiProjectBlacklistFaceStatusDto;
import com.aurine.cloudx.estate.entity.ProjectRightDevice;
import com.aurine.cloudx.estate.openapi.ToOpenApi;
import com.aurine.cloudx.estate.openapi.enums.OpenApiServiceNameEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenPushSubscribeCallbackTypeEnum;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 权限设备关系表，记录权限（认证介质）的下发状态
 *
 * @author 王良俊
 * @date 2020-05-21 09:52:28
 */
@ToOpenApi(serviceType = OpenPushSubscribeCallbackTypeEnum.OPERATE, serviceName = OpenApiServiceNameEnum.RIGHT_DEVICE_REL)
@Mapper
public interface ProjectRightDeviceMapper extends BaseMapper<ProjectRightDevice> {

    /**
     * <p>
     *  根据设备id查询设备介质权限记录
     * </p>
     *
     * @param deviceId 设备id
     * @return
     * @throws
    */
    @Deprecated
    Page<ProjectRightDeviceOptsAccessVo> pageByDeviceId(Page page,
                                                        @Param("query") ProjectRightDeviceOptsAccessSearchConditionVo query,
                                                        @Param("deviceId") String deviceId);

    /**
     * <p>
     *  根据用户ID获取这个用户所有介质的vo对象包含介质ID和介质类型
     * </p>
     *
     * @param personId 人员ID
     * @return 所有介质vo对象
    */
    List<ProjectRightDevice> listCertByPersonId(@Param("personId") String personId);

    /**
     * <p>
     *  根据用户ID集合获取这些用户所有介质的vo对象包含介质ID和介质类型
     * </p>
     *
     * @param personIdList 人员ID集合
     * @return 所有介质vo对象
    */
    List<ProjectRightDevice> listCertByPersonIdList(@Param("personIdList") List<String> personIdList);


    /**
     * <p>
     *  根据黑名单人脸id获取id对应的黑名单人脸介质的vo对象
     * </p>
     *
     * @param blacklistFaceId project_face_blacklist 的faceId
     * @return 黑名单人脸id所有对应的黑名单人脸介质的vo对象
     */
    List<ProjectRightDevice> listCertByBlacklistByFaceId(@Param("blacklistFaceId") String blacklistFaceId);
    /**
     * <p>
     *  获取所有黑名单人脸介质的vo对象
     * </p>
     *
     * @return 所有黑名单人脸介质的vo对象
     */
    List<ProjectRightDevice> listCertByBlacklist();


    /**
     * 根据设备id统计资质凭证数量
     *
     * @param projectId
     * @return
     */
    @SqlParser(filter=true)
    List<ProjectCertStateVo> countByDeviceId(@Param("deviceId")String deviceId,@Param("projectId") Integer projectId) ;




    /**
     * 根据设备id统计失败的数量
     *
     * @param projectId
     * @param deviceIdList
     * @return
     */
    @SqlParser(filter=true)
    Integer countFailByDeviceId(@Param("deviceIdList")List<String> deviceIdList,@Param("projectId") Integer projectId) ;


    /**
     * <p>
     *  进行批量更新介质下载状态 （与项目无关）
     * </p>
     *
     * @param certIdList 介质ID列表 不能为空
     * @param deviceId 要更新状态介质所属设备的ID
     * @param status 要更新的状态
    */
    @SqlParser(filter = true)
    void updateStateByIds(@Param("certIdList") List<String> certIdList, @Param("deviceId") String deviceId, @Param("status") String status);

    /**
     * 根据设备id和凭证id，查询凭证列表
     * 当设备id为空时，查询该凭证的所有数据
     * @param deviceId
     * @param certMediaId
     * @return
     */
    @SqlParser(filter = true)
    List<ProjectRightDevice> listByDeviceIdAndCertmediaId(@Param("deviceId") String deviceId,@Param("certMediaId") String certMediaId);

    /**
     * <p>
     * 根据介质ID获取房屋编号
     * </p>
     *
     * @param certId 介质ID
     * @author: 王良俊
    */
    List<String> getHouseCodeByCertId(@Param("certId") String certId);

    /**
    * <p>
    * 查询授权管理的分页数据
    * </p>
    *
    * @param page 分页信息
    * @param query 查询条件
    * @author: 王良俊
    */
    @SqlParser(filter = true)
    Page<ProjectPersonRightStatusVo> pagePersonRightStatus(Page page, @Param("query") PersonRightStatusSearchCondition query, @Param("projectId") Integer projectId,@Param("isGroup") boolean isGroup);

    /**
    * <p>
    * 根据人员ID获取到这个人的分页数据
    * </p>
    *
    * @param page 分页条件
    * @param query 查询条件
    * @author: 王良俊
    */
    Page<ProjectRightStatusVo> pageRightStatus(Page page, @Param("query") RightStatusSearchCondition query, @Param("projectId") Integer projectId);

    /**
    * <p>
    * 统计下发异常的人员ID集合
    * </p>
    *
    * @author: 王良俊
    */
    List<String> getExceptionPersonIdList(@Param("projectId") Integer projectId);

    /**
     * 更新凭证状态
     *
     * @param uidList
     * @param status
     */
    @SqlParser(filter = true)
    void updateCerts(@Param("uidList") List<String> uidList, @Param("status") String status);

    Page<ProjectRightDeviceVo> page(@Param("page") Page page, @Param("query") ProjectRightDeviceOptsAccessSearchConditionVo query, @Param("deviceId") String deviceId);

    Integer isDeviceByHuawei(@Param("id") String id);

    List<ProjectRightDevice> getCertMultiUserList(@Param("personId") String personId, @Param("capabilities") String capabilities);

    List<String> getHouseCodeByUnit(@Param("certMediaId") String certMediaId, @Param("deviceId") String deviceId);

    /**
     * 根据第三方人脸id查询人脸黑名单下发状态
     * @param thirdFaceId
     * @return
     */

    List<OpenApiProjectBlacklistFaceStatusDto> selectFaceBlacklistStatusDto(@Param("thirdFaceId") String thirdFaceId);

}
