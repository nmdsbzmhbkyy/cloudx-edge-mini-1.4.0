

package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.dto.ProjectHouseDTO;
import com.aurine.cloudx.estate.entity.ProjectHousePersonRel;
import com.aurine.cloudx.estate.entity.ProjectPersonInfo;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>住户服务接口</p>
 * @ClassName: ProjectHousePersonRelService
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/11 10:04
 * @Copyright:
 */
public interface ProjectHousePersonRelService extends IService<ProjectHousePersonRel> {

    /**
     * 查询住户
     * @param page
     * @param searchConditionVo     查询条件
     * @return
     */
    IPage<ProjectHousePersonRelRecordVo> findPage(IPage<ProjectHousePersonRelRecordVo> page, ProjectHousePersonRelSearchConditionVo searchConditionVo);





    /**
     * 更新住户信息
     * @param projectHousePersonRelVo
     * @return
     */
    boolean updateById(ProjectHousePersonRelVo projectHousePersonRelVo);


    /**
     * 获取
     * @param id
     * @return
     */
    ProjectHousePersonRelVo getVoById(String id);


    /**
     * 检查人员是否已存在于房屋内
     * @param personId
     * @param houseId
     * @return
     */
    boolean checkPersonExits(String personId, String houseId);

    /**
     * 根据住户ID获取所在的房屋
     * @param personId
     * @return
     */
    List<ProjectHouseDTO> listHouseByPersonId(@Param("personId") String personId);

    /**
     * 根据第三方编号获取人屋关系
     * @param relaCode
     * @return
     */
    ProjectHousePersonRel getByRelaCode(String relaCode);

    /**
     * 检查一个房间内是否存在业主
     * @param houseId
     * @return
     */
    boolean haveOwner(String houseId);






    void operateFocusAttr(ProjectHousePersonRelVo housePersonRelVo);

    void operateFocusAttr(List<ProjectHousePersonRelVo> housePersonRelVoList);

    /**
     *  根据房屋id查询房屋下成员信息状态（微信相关接口）
     * @return
     */
    List<ProjectHousePersonRelRecordVo> listByHouseId(String id);

    /**
     *  根据人员id查询人员房屋信息状态（微信相关接口）
     * @return
     */
    Page<ProjectHousePersonRelRecordVo> findPageById(Page page, String personId);

    /**
     *  根据人员id查询人员房屋信息状态（微信相关接口）
     * @return
     */
    Page<ProjectHousePersonRelRecordVo> filterPageById(Page page, String personId, String status);

    /**
     * 微信端申请迁入住户设置
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
     * @param id
     * @return
     */
    ProjectHouseParkPlaceInfoVo getInfo(String id);



}
