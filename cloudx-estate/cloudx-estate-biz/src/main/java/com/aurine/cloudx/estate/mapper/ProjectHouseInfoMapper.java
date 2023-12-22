

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.dto.ProjectHouseDTO;
import com.aurine.cloudx.estate.entity.ProjectHouseInfo;
import com.aurine.cloudx.estate.openapi.ToOpenApi;
import com.aurine.cloudx.estate.openapi.enums.OpenApiServiceNameEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenPushSubscribeCallbackTypeEnum;
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
@ToOpenApi(serviceType = OpenPushSubscribeCallbackTypeEnum.OPERATE, serviceName = OpenApiServiceNameEnum.HOUSE_INFO)
@Mapper
public interface ProjectHouseInfoMapper extends BaseMapper<ProjectHouseInfo> {
    IPage<ProjectHouseInfoVo> select(IPage<?> page, @Param("buildingId") String buildingId, @Param("buildingName") String buildingName, @Param("unitId") String unitId, @Param("unitName") String unitName, @Param("houseName") String houseName, @Param("isUse") Integer isUse);

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

    Integer putBatch(@Param("houseId")String houseId,@Param("houseDesginId")String houseDesginId);


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
    List<ProjectHouseInfoVo> getByUnitId(@Param("unitId") String unitId, @Param("projectId") Integer projectId);

    @SqlParser(filter = true)
    Integer saveHouse(@Param("houseInfo") ProjectHouseInfoVo houseInfo);

    IPage<ProjectDeviceSelectTreeVo> findIndoorByName(IPage page, @Param("name") String name);

    Integer getCount(String id);
    /**
    * @Author hjj
    * @Description 根据楼栋ID获取楼层框架号
    * @Date  2022/3/8
    * @Param
    * @return
    **/
    List<Integer> getFloorByBuildingId(@Param("buildingId") String buildingId);

    /**
     * @Author hjj
     * @Description 根据项目ID获取楼层框架号
     * @Date  2022/3/8
     * @Param
     * @return
     **/
    List<Integer> getFloorByProjectId(@Param("projectId") Integer projectId);

    /**
     * 查询人员所在小区拥有的房屋信息
     *
     * @param id
     * @param projectId
     * @return
     */
    List<ProjectHouseInfoVo> getHouseInfo(@Param("id") Integer id,@Param("projectId") Integer projectId);
    /**
     * 获取房屋地址
     *
     * @param houseId
     * @return
     */
    List<String> getAddress(@Param("houseId") String houseId);

    List<ProjectHouseInfoVo> getByBuildingId(@Param("buildingId") String buildingId);
    /**
     * 获取房屋属性
     *
     * @param personId
     * @return
     */
    List<String> getHosueParam(@Param("personId") String personId);

    List<String> getHouseId(String phone);

    /**
     * 根据楼栋和人员，查询房屋信息
     *
     * @param personId
     * @param buildingId
     * @return
     */
    List<ProjectHouseInfo> getHouseInfoByPersonAndBuild(@Param("personId") String personId,@Param("buildingId") String buildingId,@Param("unitId") String unitId,@Param("houseId") String houseId);

}
