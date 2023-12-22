

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectRightDevice;
import com.aurine.cloudx.estate.vo.CertVo;
import com.aurine.cloudx.estate.vo.ProjectRightDeviceOptsAccessSearchConditionVo;
import com.aurine.cloudx.estate.vo.ProjectRightDeviceOptsAccessVo;
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
    List<CertVo> listCertByPersonId(@Param("personId") String personId);

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
     * @param certmediaId
     * @return
     */
    @SqlParser(filter = true)
    List<ProjectRightDevice> listByDeviceIdAndCertmediaId(@Param("deviceId") String deviceId,@Param("certMediaId") String certMediaId);
}
