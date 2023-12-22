package com.aurine.cloudx.estate.service;
import com.aurine.cloudx.estate.vo.ProjectStaffRotaDetailFromVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aurine.cloudx.estate.entity.ProjectStaffRotaDetail;

/**
 * 值班明细(ProjectStaffRotaDetail)表服务接口
 *
 * @author guhl@aurine.cn
 * @since 2020-08-03 11:49:09
 */
public interface ProjectStaffRotaDetailService extends IService<ProjectStaffRotaDetail> {

    /**
     * 新增值班明细
     *
     * @param projectStaffRotaDetailFromVo
     * @return
     */
    Boolean saveProjectStaffRotaDetail(ProjectStaffRotaDetailFromVo projectStaffRotaDetailFromVo);

    /**
     * 更新值班明细
     *
     * @param projectStaffRotaDetailFromVo
     * @return
     */
    Boolean updateProjectStaffRotaDetail(ProjectStaffRotaDetailFromVo projectStaffRotaDetailFromVo);

}