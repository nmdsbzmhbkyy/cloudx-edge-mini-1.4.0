package com.aurine.cloudx.estate.service;
import com.aurine.cloudx.estate.entity.ProjectInfo;
import com.aurine.cloudx.estate.vo.ProjectEmployerInfoFromVo;
import com.aurine.cloudx.estate.vo.ProjectEmployerInfoPageVo;
import com.aurine.cloudx.estate.vo.ProjectInfoPageVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aurine.cloudx.estate.entity.ProjectEmployerInfo;
import org.apache.ibatis.annotations.Param;

/**
 * 项目实有单位信息(ProjectEmployerInfo)表服务接口
 *
 * @author guhl@aurine.cn
 * @since 2020-08-25 14:58:44
 */
public interface ProjectEmployerInfoService extends IService<ProjectEmployerInfo> {

    /**
     * 分页查询实有单位信息
     *
     * @param page
     * @param projectEmployerInfoPageVo
     * @return
     */
    Page<ProjectEmployerInfoPageVo> pageEmployer(Page page, @Param("query") ProjectEmployerInfoPageVo projectEmployerInfoPageVo);

    /**
     * 新增实有单位信息
     *
     * @param projectEmployerInfoFromVo
     * @return
     */
    Boolean saveEmployerInfo(ProjectEmployerInfoFromVo projectEmployerInfoFromVo);

    /**
     * 删除实有单位信息
     *
     * @param houseId
     * @return
     */
    Boolean removeByHouseId(String houseId);

    /**
     * 更新实有单位信息
     *
     * @param projectEmployerInfoFromVo
     * @return
     */
    Boolean updateEmployerInfo(ProjectEmployerInfoFromVo projectEmployerInfoFromVo);

    /**
     * 通过id查询实有单位信息
     *
     * @param houseId
     * @return
     */
    ProjectEmployerInfoFromVo getByHouseId(String houseId);

    /**
     * 通过统一社会信用代码获取实有单位
     *
     * @param socialCreditCode
     * @return
     */
    ProjectEmployerInfo getBySocialCreditCode(String socialCreditCode, String employerId);
}