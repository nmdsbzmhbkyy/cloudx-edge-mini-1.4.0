

package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.common.entity.vo.HousePersonInfoVo;
import com.aurine.cloudx.open.origin.dto.ProjectHouseDTO;
import com.aurine.cloudx.open.origin.entity.ProjectHousePersonChangeHis;
import com.aurine.cloudx.open.origin.entity.ProjectHousePersonRel;
import com.aurine.cloudx.open.origin.vo.*;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 住户
 *
 * @author pigx code generator
 * @date 2020-05-11 08:17:43
 */
@Mapper
public interface ProjectHousePersonRelMapper extends BaseMapper<ProjectHousePersonRel> {
    /**
     * @param page
     * @param buildingName 楼栋名称
     * @param unitName     单元名称
     * @param houseName    房屋名称
     * @param personName   住户名称
     * @return
     */
    IPage<ProjectHousePersonRelRecordVo> select(IPage<?> page, @Param("buildingName") String buildingName, @Param("unitName") String unitName,
                                                @Param("houseName") String houseName, @Param("personName") String personName,
                                                @Param("buildingId") String buildingId, @Param("unitId") String unitId,
                                                @Param("houseId") String houseId, @Param("auditStatus") String[] auditStatus,
                                                @Param("housePeopleRel") String housePeopleRel,
                                                @Param("personId") String personId,
                                                @Param("projectId") Integer projectId
    );


    IPage<ProjectHousePersonRelRecordVo> selectAll(IPage<?> page, @Param("buildingName") String buildingName, @Param("unitName") String unitName,
                                                   @Param("houseName") String houseName, @Param("personName") String personName,
                                                   @Param("buildingId") String buildingId, @Param("unitId") String unitId,
                                                   @Param("houseId") String houseId, @Param("auditStatus") String[] auditStatus,
                                                   @Param("housePeopleRel") String housePeopleRel,
                                                   @Param("personId") String personId,
                                                   @Param("projectId") Integer projectId
    );


    /**
     * 根据房屋id查询(微信接口相关)
     *
     * @param houseId
     * @return
     */
    List<ProjectHousePersonRelRecordVo> findPageByHouseId(@Param("houseId") String houseId, @Param("projectId") Integer projectId);

    /**
     * 根据人员id查询(微信接口相关)
     *
     * @param page
     * @param personId
     * @return
     */
    Page<ProjectHousePersonRelRecordVo> findPageById(Page page, @Param("personId") String personId, @Param("projectId") Integer projectId);

    /**
     * 根据人名查询房屋人员信息
     *
     * @param name
     * @return
     */
    List<ProjectHouseHisRecordVo> findByName(@Param("name") String name, @Param("projectId") Integer projectId);
//    IPage<ProjectHousePersonRelRecodeVo> select(IPage<?> page,String buildingName,String houseName,String unitName,String personName);


    /**
     * 身份认证分页
     *
     * @param page
     * @param buildingName
     * @param unitName
     * @param houseName
     * @param personName
     * @param auditStatus
     * @return
     */
    IPage<ProjectHousePersonRelRecordVo> pageIdentity(IPage<?> page, @Param("buildingName") String buildingName, @Param("unitName") String unitName,
                                                      @Param("houseName") String houseName, @Param("personName") String personName,
                                                      @Param("houseId") String houseId, @Param("phone") String phone,
                                                      @Param("auditStatus") String[] auditStatus, @Param("projectId") Integer projectId);

    List<ProjectHouseDTO> listHouseByPersonId(@Param("personId") String personId, @Param("projectId") Integer projectId);

    /**
     * 根据住户类型查询当前项目下的人数
     *
     * @param houseHoldType
     * @return
     */
    int countByHouseHoldType(@Param("houseHoldType") String houseHoldType, @Param("projectId") Integer projectId);

    Page<ProjectHousePersonRelRecordVo> filterPageById(Page page, @Param("personId") String personId, @Param("projectId") Integer projectId, @Param("status") String status);


    /**
     * <p>
     * 获取当前项目所有房屋每个住户数量的（正常某个住户在该房屋的数量是1）
     * </p>
     *
     * @author: 王良俊
     */
    @SqlParser(filter = true)
    List<ProjectHousePersonNumVo> getHousePersonNumMapping(@Param("projectId") Integer projectId, @Param("tenantId") Integer tenantId);

    /**
     * 获取房屋下所有人员的信息
     *
     * @param houseId
     * @return
     */
    List<ProjectHousePersonRelVo> getHousePersonRel(@Param("houseId") String houseId);

    /**
     * 给员工发送消息
     *
     * @return
     */
    List<String> getProjectStaff(@Param("menuId") Integer menuId, @Param("projectId") Integer projectId);

    /**
     * 获取房屋地址
     *
     * @param houseId
     */
    ProjectHouseAddressVo getAddress(String houseId);


    /**
     * 获取项目名称
     * @param projectId
     */
    String getProjectName(Integer projectId);



    Integer countByOff();


    /**
     * 获取被替换的用户
     *
     * @param houseId
     * @param personName
     * @param personId
     * @return
     */
    ProjectHousePersonRelVo getReplaceUser(@Param("houserId") String houseId, @Param("personName") String personName, @Param("personId") String personId, @Param("houseHoldType") String houseHoldType);



    ProjectHousePersonChangeHis getToReplace(@Param("personId") String personId, @Param("houserId") String houseId);

    String findSaveFace(@Param("relaId") String relaId);

    /**
     * 多条件分页查询
     *
     * @param page
     * @param po
     * @return
     */
    Page<HousePersonInfoVo> page(Page page, @Param("query") ProjectHousePersonRel po);
}
