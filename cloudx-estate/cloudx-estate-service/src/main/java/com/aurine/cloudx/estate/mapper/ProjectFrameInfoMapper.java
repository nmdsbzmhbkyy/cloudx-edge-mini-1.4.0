

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.dto.ProjectFrameCountDTO;
import com.aurine.cloudx.estate.entity.ProjectFrameInfo;
import com.aurine.cloudx.estate.vo.ProjectDeviceSelectTreeVo;
import com.aurine.cloudx.estate.vo.ProjectFrameInfoVo;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 框架
 *
 * @author 王伟
 * @date 2020-05-07 14:00:08
 */
@Mapper
public interface ProjectFrameInfoMapper extends BaseMapper<ProjectFrameInfo> {

    /**
     * <p>
     *  根据组团id获取组团数据
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
    public List<ProjectFrameCountDTO> listFrameAndPersonCounts(@Param("entityId") String entityId, @Param("tenantId")  Integer tenantId,  @Param("projectId") Integer projectId);

    /**
     * <p>
     *  根据房屋地址获取到房屋id
     * </p>
     *
     * @param address 房屋地址 楼栋-单元-房屋
     * @return 房屋ID列表 正常情况长度为1
    */
    List<String> getHouseIdByAddressNotGroup(String address);

    /**
     * <p>
     *  根据房屋地址获取到房屋id(开启组团)
     * </p>
     *
     * @param address 房屋地址 楼栋-单元-房屋
     * @return 房屋ID列表 正常情况长度为1
    */
    List<String> getHouseIdByAddressGroup(String address);

    List<ProjectFrameInfo> getByLevel(@Param("level")Integer level);

    List<ProjectFrameInfo> listByPuid(String puid);

    /**
     * 根据项目编号和框架号，查询数量
     * @param frameCode
     * @param projectId
     * @return
     */
    Integer countByProjectIdAndCode(@Param("frameCode")String frameCode,@Param("projectId")Integer projectId);


    /**
     * 通过框架号获取框架对象
     * @param frameCode
     * @param projectId
     * @return
     */
    @SqlParser(filter = true)
    ProjectFrameInfoVo getByCode(@Param("frameCode")String frameCode,@Param("projectId")Integer projectId);

    @SqlParser(filter = true)
    Integer saveFrame(@Param("frameInfo") ProjectFrameInfoVo frameInfo);

    /**
     * 根据框架号id获取到叶子节点中房屋的统计
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
    IPage<String> getCheckedIndoorId(IPage iPage,@Param("ids") List<String> ids);

    /**
     * 根据父级id获取楼栋单元及存在人屋关系的房屋
     * @param puid
     * @return
     */
    List<ProjectDeviceSelectTreeVo> getFrameInfoByPerson(@Param("puid") String puid);

    /**
     * 获取所有楼栋单元及存在人屋关系的房屋
     * @return
     */
    List<ProjectDeviceSelectTreeVo> getAllFrameInfosOnPerson();
}
