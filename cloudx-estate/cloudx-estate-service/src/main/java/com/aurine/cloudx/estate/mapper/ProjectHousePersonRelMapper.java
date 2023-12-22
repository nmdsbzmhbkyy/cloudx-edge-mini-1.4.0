

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.dto.ProjectHouseDTO;
import com.aurine.cloudx.estate.entity.ProjectHousePersonRel;
import com.aurine.cloudx.estate.entity.ProjectStaff;
import com.aurine.cloudx.estate.vo.ProjectHouseAddressVo;
import com.aurine.cloudx.estate.vo.ProjectHouseHisRecordVo;
import com.aurine.cloudx.estate.vo.ProjectHousePersonRelRecordVo;
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
                                                @Param("phone") String phone,
                                                @Param("personId") String personId,
                                                @Param("projectId") Integer projectId);

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
                                                      @Param("houseId") String houseId,@Param("phone") String phone,
                                                      @Param("auditStatus") String[] auditStatus, @Param("projectId") Integer projectId);

    List<ProjectHouseDTO> listHouseByPersonId(@Param("personId") String personId, @Param("projectId") Integer projectId);

    Page<ProjectHousePersonRelRecordVo> filterPageById(Page page,@Param("personId") String personId, @Param("projectId") Integer projectId,@Param("status") String status);

    /**
     * 给拥有住户审核模块的员工发送消息
     *
     * @return
     */
    List<String> getProjectStaff(@Param("menuId")Integer menuId,@Param("projectId")Integer projectId);

    /**
     * 获取房屋地址
     * @param houseId
     */
    ProjectHouseAddressVo getAddress(String houseId);

    /**
     * 获取项目名称
     * @param projectId
     */
    String getProjectName(Integer projectId);
}
