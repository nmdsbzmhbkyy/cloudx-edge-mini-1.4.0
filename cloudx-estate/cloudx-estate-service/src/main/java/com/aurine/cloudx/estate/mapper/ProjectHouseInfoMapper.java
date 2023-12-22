

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.dto.ProjectHouseDTO;
import com.aurine.cloudx.estate.entity.ProjectHouseInfo;
import com.aurine.cloudx.estate.vo.ProjectDeviceSelectTreeVo;
import com.aurine.cloudx.estate.vo.ProjectHouseHisRecordVo;
import com.aurine.cloudx.estate.vo.ProjectHouseInfoVo;
import com.aurine.cloudx.estate.vo.ProjectHouseResidentVo;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 房屋
 *
 * @author 王伟
 * @date 2020-05-08 16:29:18
 */
@Mapper
public interface ProjectHouseInfoMapper extends BaseMapper<ProjectHouseInfo> {
    IPage<ProjectHouseInfoVo> select(IPage<?> page, @Param("buildingId") String buildingId, @Param("buildingName") String buildingName, @Param("unitId") String unitId, @Param("unitName") String unitName, @Param("houseName") String houseName, @Param("isUse") int isUse);

    /**
     * <p>
     * 根据房屋id获取到房屋住户信息
     * </p>
     *
     * @param houseId 房屋id
     * @return
     * @throws
     */
    IPage<ProjectHouseResidentVo> getHouseResidents(IPage<?> page, @Param("houseId") String houseId, @Param("projectId") int projectId);

    /**
     * <p>
     * 根据房屋id获取到房屋住户信息
     * </p>
     *
     * @param houseId 房屋id
     * @return
     * @throws
     */
    List<ProjectHouseResidentVo> getHouseResidents(@Param("houseId") String houseId, @Param("projectId") int projectId);

    IPage<ProjectHouseHisRecordVo> getHouseRecord(IPage<?> page, @Param("houseId") String houseId, @Param("projectId") int projectId);

    List<ProjectHouseDTO> getHouseRecordWithPerson(@Param("buildingId") String buildingId, @Param("unitId") String unitId, @Param("isUse") int isUse);

    Integer countHouseRecordWithPerson(@Param("buildingId") String buildingId, @Param("unitId") String unitId, @Param("isUse") int isUse);

    /**
     * 统计自住的住户
     *
     * @return
     */
    Integer countLive();

    /**
     * 统计租用的住户
     *
     * @return
     */
    Integer countSublet();

    /**
     * 统计商用的房屋
     *
     * @return
     */
    Integer countCommercial();

    /**
     * 统计闲置的房屋
     *
     * @return
     */
    Integer countFree();

    List<String> getRemoveAbleHouseIdList(List<String> houseIdList);

    List<ProjectHouseResidentVo> getHouseResidentsWithoutStatus(String houseId);

    ProjectHouseInfoVo getHouseInfoVo(@Param("id") String id);

    /**
     * 通过框架号获取楼栋对象
     *
     * @param houseCode
     * @param projectId
     * @return
     */
    @SqlParser(filter = true)
    ProjectHouseInfoVo getByCode(@Param("houseCode") String houseCode, @Param("projectId") Integer projectId);

    @SqlParser(filter = true)
    Integer saveHouse(@Param("houseInfo") ProjectHouseInfoVo houseInfo);

    IPage<ProjectDeviceSelectTreeVo> findIndoorByName(IPage page, @Param("name") String name);

    List<ProjectHouseResidentVo> getHousePerson(@Param("houseId") String houseId, @Param("phone") String phone);
}
