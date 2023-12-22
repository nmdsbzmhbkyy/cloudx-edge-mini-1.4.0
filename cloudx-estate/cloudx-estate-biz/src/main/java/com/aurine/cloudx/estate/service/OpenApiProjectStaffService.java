package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.dto.OpenApiProjectStaffDto;
import com.aurine.cloudx.estate.entity.ProjectStaff;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * 项目员工信息表(仅针对于openApi调用)
 *
 * @author weirm
 * @date 2020-05-11 13:38:09
 */
public interface OpenApiProjectStaffService extends IService<ProjectStaff> {


    /**
     * 开放平台 复合接口
     * 涉及 新增员工信息，有人脸、卡时保存人脸、卡信息 下发用户绑定设备是保存设备权限
     *
     * @param projectStaffDto
     * @return
     */
    R<OpenApiProjectStaffDto> saveStaff(OpenApiProjectStaffDto projectStaffDto);

    /**
     * 修改人员
     *
     * @param projectStaffDto
     * @return
     */
    R<OpenApiProjectStaffDto> updateStaff(OpenApiProjectStaffDto projectStaffDto);
    /**
     * 删除员工
     *
     * @param id
     * @return
     */
    R<String> removeStaff(String id);

    /**
     * 根据部门id查询部门下员工总人数
     *
     * @param deptId 部门Id
     * @return
     */
    Integer getStaffCountByDeptId(Integer deptId);

}
