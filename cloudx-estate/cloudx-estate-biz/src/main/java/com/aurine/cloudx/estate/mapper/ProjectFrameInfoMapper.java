

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.dto.ProjectFrameCountDTO;
import com.aurine.cloudx.estate.entity.ProjectFrameInfo;
import com.aurine.cloudx.estate.openapi.ToOpenApi;
import com.aurine.cloudx.estate.openapi.enums.OpenApiServiceNameEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenPushSubscribeCallbackTypeEnum;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 框架
 *
 * @author 王伟
 * @date 2020-05-07 14:00:08
 */

@ToOpenApi(serviceType = OpenPushSubscribeCallbackTypeEnum.OPERATE, serviceName = OpenApiServiceNameEnum.FRAME_INFO)
@Mapper
//@CacheNamespace
public interface ProjectFrameInfoMapper extends BaseMapper<ProjectFrameInfo> {

    /**
     * <p>
     * 根据组团id获取组团数据
     * </p>
     *
     * @param entityId 组团id
     * @return
     * @throws
     */
    public String getFrameInfoById(String entityId);

    /**
     * 通过组团数据获取其下的层级实体数据信息以及住户数量信息
     *
     * @param entityId
     * @return
     */
    public List<ProjectFrameCountDTO> listFrameAndPersonCounts(@Param("entityId") String entityId, @Param("tenantId") Integer tenantId, @Param("projectId") Integer projectId);

    /**
     * <p>
     * 根据房屋地址获取到房屋id
     * </p>
     *
     * @param address 房屋地址 楼栋-单元-房屋
     * @return 房屋ID列表 正常情况长度为1
     */
    List<String> getHouseIdByAddressNotGroup(String address);

    /**
     * <p>
     * 根据房屋地址获取到房屋id(开启组团)
     * </p>
     *
     * @param address 房屋地址 楼栋-单元-房屋
     * @return 房屋ID列表 正常情况长度为1
     */
    List<String> getHouseIdByAddressGroup(String address);

    /**
    * <p>
    * 启用组团版
    * 用于初始化，房屋ID和房屋地址之间的map映射关系（用于住户导入的时候获取房屋ID使用）
    * </p>
    *
    * @param projectId 所要生成的项目ID
    * @author: 王良俊
    */
    List<ProjectHouseAddressVo> getHouseIDAndAddressInfoGroup(@Param("projectId") Integer projectId);

    List<ProjectHouseAddressVo> getHouseIDAndAddressInfo(@Param("projectId") Integer projectId);

    List<ProjectFrameInfo> getByLevel(@Param("level") Integer level);

    List<ProjectFrameInfo> listByPuid(String puid);

    /**
     * 根据项目编号和框架号，查询数量
     *
     * @param frameCode
     * @param projectId
     * @return
     */
    Integer countByProjectIdAndCode(@Param("frameCode") String frameCode, @Param("projectId") Integer projectId);


    /**
     * 通过框架号获取框架对象
     *
     * @param frameCode
     * @param projectId
     * @return
     */
    @SqlParser(filter = true)
    ProjectFrameInfoVo getByCode(@Param("frameCode") String frameCode, @Param("projectId") Integer projectId);

    @SqlParser(filter = true)
    Integer saveFrame(@Param("frameInfo") ProjectFrameInfoVo frameInfo);

    /**
     * 根据框架号id获取到叶子节点中房屋的统计
     *
     * @param ids
     * @return
     */
    Integer getCountByIndoor(@Param("ids") List<String> ids);

    /**
     * 根据框架号id获取到叶子节点中房屋的id
     *
     * @param iPage
     * @param ids
     * @return
     */
    IPage<String> getCheckedIndoorId(IPage iPage, @Param("ids") List<String> ids);

    /**
     * 根据父级id获取楼栋单元及存在人屋关系的房屋
     *
     * @param puid
     * @return
     */
    List<ProjectDeviceSelectTreeVo> getFrameInfoByPerson(@Param("puid") String puid);

    /**
     * 获取所有楼栋单元及存在人屋关系的房屋
     *
     * @return
     */
    List<ProjectDeviceSelectTreeVo> getAllFrameInfosOnPerson();

    /**
     * 根据实例id，自下而上获取上层全部节点的名称组合
     * DEMO：组团7-组团6-组团5-组团4-楼栋-单元-房屋
     *
     * @param entityId
     * @param separator 连接符
     * @return
     */
    @SqlParser(filter = true)
    String getFullFrameNameByEntityId(@Param("entityId") String entityId, @Param("separator") String separator, @Param("beginLevel") Integer beginLevel, @Param("endLevel") Integer endLevel);

    List<String> listHouseNameByBuildingId(@Param("buildingId") String buildingId);

    /**
     * 统计项目的楼栋数
     * @param projectId
     * @return
     */
    Integer countFrameInfo(@Param("projectId") Integer projectId);

    /**
     * <p>根据房屋名，单元名，楼栋名获取地址信息（房屋、单元或是楼栋的ID）</p>
     *
     * @param buildingName 楼栋名
     * @author: 王良俊
     */
    String getBuildingId(@Param("buildingName") String buildingName);

    /**
     * <p>获取项目的所有组团信息</p>
     *
     * @author: 王良俊
     */
    List<FrameInfo> getAllFrameInfo();

    String getHouseFullNameByHouseId(@Param("houseId") String houseId);
}
