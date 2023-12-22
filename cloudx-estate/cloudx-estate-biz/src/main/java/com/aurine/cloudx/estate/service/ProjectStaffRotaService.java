package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.vo.ProjectStaffRotaFromVo;
import com.aurine.cloudx.estate.vo.ProjectStaffRotaPageVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aurine.cloudx.estate.entity.ProjectStaffRota;

import java.util.List;

/**
 * 项目员工值班表信息(ProjectStaffRota)表服务接口
 *
 * @author makejava
 * @since 2020-08-03 11:48:49
 */
public interface ProjectStaffRotaService extends IService<ProjectStaffRota> {

    /**
     * 查询所有员工值班信息
     *
     * @return
     */
    List<ProjectStaffRotaPageVo> listStaffRota();

    /**
     * 新增员工值班表
     *
     * @param projectStaffRotaFromVo
     * @return
     */
    Boolean saveProjectStaffRota(ProjectStaffRotaFromVo projectStaffRotaFromVo);

    /**
     * 更新员工值班表
     *
     * @param projectStaffRotaFromVo
     * @return
     */
    Boolean updateProjectStaffRota(ProjectStaffRotaFromVo projectStaffRotaFromVo);

    /**
     * 删除员工值班表
     *
     * @param rotaId
     * @return
     */
    Boolean removeByRotaId(String rotaId);
}