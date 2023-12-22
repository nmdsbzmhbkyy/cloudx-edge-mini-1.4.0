package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.dto.ProjectHouseDTO;
import com.aurine.cloudx.estate.entity.ProjectHousePersonRel;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.data.repository.query.Param;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>住户服务接口</p>
 *
 * @ClassName: ProjectHousePersonRelService
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/11 10:04
 * @Copyright:
 */
public interface ProjectHousePersonRelService extends IService<ProjectHousePersonRel> {

    /**
     * 审核
     *
     * @param projectHousePersonRel
     * @return
     */
    ProjectHousePersonRel verify(ProjectHousePersonRelVo projectHousePersonRel);

    /**
     * 迁入住户
     *
     * @param projectHousePersonRelVo
     * @return
     */
    ProjectHousePersonRel saveRel(ProjectHousePersonRelVo projectHousePersonRelVo);

    /**
     * 微信迁入住户
     *
     * @param projectHousePersonRelVo
     * @return
     */
    ProjectHousePersonRel wechatSaveRel(ProjectHousePersonRelVo projectHousePersonRelVo);

    /**
     * 根据第三方ID,保存或更新住户信息
     *
     * @param projectHousePersonRelVo
     * @return
     * @author: 王伟
     * @since: 2020-12-23 18:08
     */
    String saveOrUpdateRelByWR20(ProjectHousePersonRelVo projectHousePersonRelVo);


    /**
     * 批量审核通过
     *
     * @param relaIds
     * @return
     */
    boolean passAll(List<String> relaIds);

    /**
     * 迁入住户(批量)
     *
     * @param projectHousePersonRelVoList
     * @return
     */
    List<ProjectHousePersonRelVo> saveRelBatch(List<ProjectHousePersonRelVo> projectHousePersonRelVoList);

    /**
     * 根据人员id，获取该人员的所有住户关系
     *
     * @param personId
     * @return
     */
    List<ProjectHousePersonRel> listHousePersonByPersonId(String personId);

    List<String> getAddress(String oldAddress);


    /**
     * 迁出住户
     *
     * @param id
     * @return
     */
    boolean removeHousePersonRelById(String id);

    /**
     * 审核迁出住户
     *
     * @param id
     * @return
     */
    boolean verifyRemoveHousePersonRelById(String id);

    /**
     * 批量迁出住户
     *
     * @param ids
     * @return
     */
    boolean removeAll(List<String> ids);

    /**
     * 批量导入住户
     *
     * @param file excel文件
     * @return 导入结果
     * @author: 王良俊
     */
    R importExcel(MultipartFile file, String type);

    /**
     * 获取失败名单
     *
     * @param name 文件名
     * @return 导入结果
     * @author: 许亮亮
     */
    void errorExcel(String name, HttpServletResponse httpServletResponse) throws IOException;

    /**
     * 获取模板文件
     *
     * @return 导入结果
     * @author: 许亮亮
     */
    void modelExcel(String type, HttpServletResponse httpServletResponse) throws IOException;

    void removeByRelId(String id);

    /**
     * 查询住户
     *
     * @param page
     * @param searchConditionVo 查询条件
     * @return
     */
    IPage<ProjectHousePersonRelRecordVo> findPage(IPage<ProjectHousePersonRelRecordVo> page, ProjectHousePersonRelSearchConditionVo searchConditionVo);

    /**
     * 查询住户
     *
     * @param page
     * @param searchConditionVo 查询条件
     * @return
     */
    IPage<ProjectHousePersonRelRecordVo> findPageAll(IPage<ProjectHousePersonRelRecordVo> page, ProjectHousePersonRelSearchConditionVo searchConditionVo);

    /**
     * 更新住户信息
     *
     * @param projectHousePersonRelVo
     * @return
     */
    boolean updateById(ProjectHousePersonRelVo projectHousePersonRelVo);

    /**
     * 获取 身份认证详情
     *
     * @param id
     * @return
     */
    ProjectHousePersonRelVo getHousePersonVerifyInfo(String id);
    /**
     * 获取
     *
     * @param id
     * @return
     */
    ProjectHousePersonRelVo getVoById(String id);

    /**
     * 检查人员是否已存在于房屋内
     *
     * @param personId
     * @param houseId
     * @return
     */
    boolean checkPersonExits(String personId, String houseId);

    /**
     * 根据住户ID获取所在的房屋
     *
     * @param personId
     * @return
     */
    List<ProjectHouseDTO> listHouseByPersonId(@Param("personId") String personId);

    /**
     * 根据第三方编号获取人屋关系
     *
     * @param relaCode
     * @return
     */
    ProjectHousePersonRel getByRelaCode(String relaCode);

    /**
     * 检查一个房间内是否存在业主
     *
     * @param houseId
     * @return
     */
    boolean haveOwner(String houseId);


    void operateFocusAttr(ProjectHousePersonRelVo housePersonRelVo);

    void operateFocusAttr(List<ProjectHousePersonRelVo> housePersonRelVoList);

    /**
     * 根据房屋id查询房屋下成员信息状态（微信相关接口）
     *
     * @return
     */
    List<ProjectHousePersonRelRecordVo> listByHouseId(String id);

    /**
     * 根据人员id查询人员房屋信息状态（微信相关接口）
     *
     * @return
     */
    Page<ProjectHousePersonRelRecordVo> findPageById(Page page, String personId);

    /**
     * 根据人员id查询人员房屋信息状态（微信相关接口）
     *
     * @return
     */
    Page<ProjectHousePersonRelRecordVo> filterPageById(Page page, String personId, String status);

    /**
     * 微信端申请迁入住户设置
     *
     * @param projectHousePersonRel
     * @return
     */
    R request(ProjectHousePersonRelRequestVo projectHousePersonRel);

    /**
     * 根据人名查询房屋关系表
     *
     * @param name
     * @return
     */
    List<ProjectHouseHisRecordVo> findByName(String name);


    /**
     * 查看是否存在业主或同名家属
     *
     * @param houseId
     * @param personName
     * @param houseHoldType
     * @param personId
     * @return
     */
    ProjectHousePersonRelVo checkHouseRel(String houseId, String personName, String houseHoldType, String personId);


    /**
     * 身份认证分页
     *
     * @param page
     * @param searchConditionVo
     * @return
     */
    IPage<ProjectHousePersonRelRecordVo> pageIdentity(IPage<ProjectHousePersonRelRecordVo> page, ProjectHousePersonRelSearchConditionVo searchConditionVo);

    ProjectHousePersonRel findByPersonIdAndHouseId(String personId, String houseId);

    R requestAgain(ProjectHousePersonRelRequestAgainVo projectHousePersonRel);


    /**
     * 根据房屋人员关系id获取人员信息及关联的停车场车辆信息
     *
     * @param id
     * @return
     */
    ProjectHouseParkPlaceInfoVo getInfo(String id);

    /**
     * 获取不同住户类型的人数，忽略手机号相同的住户。
     * 1 业主（产权人） 2 家属 3 租客
     * @param houseHoldType
     * @return
     */
    Integer countHousePersonRel(String houseHoldType);


    /**
     * <p>
     * 判断住户是否已经预登记房屋了
     * </p>
     *
     * @param houseId 房屋ID
     * @param telephone 住户手机号
     * @return 是否已预登记
     * @author: 王良俊
     */
    ProjectHousePersonRel checkHasPreRegister(String telephone, String houseId);

    /**
    * <p>
    * 获取当前项目所有房屋每个住户数量的（正常某个住户在该房屋的数量是1）
    * </p>
    *
    * @author: 王良俊
    */
    List<ProjectHousePersonNumVo> getHousePersonNumMapping();

    /**
    * <p>
    * 获取当前项目所有房屋每个住户数量的（正常某个住户在该房屋的数量是1）
    * </p>
    *
    * @author: 王良俊
     * @return
    */
    Long incrementPersonNum(String redisKey, String houseId, String personId);

    /**
    * <p>
    * 会从数据库中重新获取到人屋数量关系数据（会删除原有key下存储的数据，如果有的话）
    * </p>
    *
    * @param redisKey redis的键
    * @author: 王良俊
    */
    void initHousePersonNumMapping(String redisKey);

    /**
    * <p>
    * 删除缓存在redis里面的人屋关系数据
    * </p>
    *
    * @param redisKey redis的键
    * @author: 王良俊
    */
    void deleteHousePersonNumCache(String redisKey);


    Integer countByOff();

    /**
     * 重新迁入住户
     *
     * @param housePersonRelId
     * @return
     */
    boolean reSaveRel(String housePersonRelId);

    String findSaveFace(String relaId);

    /**
     * 根据houseid获取房屋下的人员数据
     *
     * @param houseId
     * @return
     */
    List<ProjectHousePersonRel> getHousePersonRelListByHouseId(String houseId);

    /**
     * 根据personId获取有几个房屋（已通过的房屋）
     * @param personId
     * @return
     */
    Integer countHouseByPersonId(String personId);

    String getVisitorHouseName(String visitHouseId);
}
