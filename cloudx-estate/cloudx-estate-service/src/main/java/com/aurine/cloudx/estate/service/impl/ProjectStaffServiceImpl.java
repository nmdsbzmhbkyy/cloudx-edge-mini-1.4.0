
package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.PersonConstant;
import com.aurine.cloudx.estate.constant.enums.AttendanceStatusEnum;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.dto.ProjectStaffDTO;
import com.aurine.cloudx.estate.entity.ProjectAttendance;
import com.aurine.cloudx.estate.entity.ProjectShiftConf;
import com.aurine.cloudx.estate.entity.ProjectStaff;
import com.aurine.cloudx.estate.entity.ProjectStaffShiftDetail;
import com.aurine.cloudx.estate.mapper.ProjectStaffMapper;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codingapi.tx.annotation.TxTransaction;
import com.pig4cloud.pigx.admin.api.dto.CxUserDTO;
import com.pig4cloud.pigx.admin.api.entity.SysDept;
import com.pig4cloud.pigx.admin.api.feign.RemoteDeptService;
import com.pig4cloud.pigx.admin.api.feign.RemoteUserService;
import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目员工信息表
 *
 * @author lingang
 * @date 2020-05-11 13:38:09
 */
@Service
public class ProjectStaffServiceImpl extends ServiceImpl<ProjectStaffMapper, ProjectStaff> implements ProjectStaffService {
    @Resource
    private RemoteUserService userRemote;
    @Resource
    private ProjectPersonAttrService projectPersonAttrService;
    @Resource
    private ProjectRightDeviceService projectRightDeviceService;
    @Resource
    private ProjectPersonDeviceService projectPersonDeviceService;
    @Resource
    private ProjectStaffShiftDetailService projectStaffShiftDetailService;

    @Resource
    private ProjectAttendanceService projectAttendanceService;

    @Resource
    private RemoteDeptService remoteDeptService;

    @Resource
    private ProjectShiftConfService projectShiftConfService;

    @Override
    @TxTransaction(isStart = true)
    @Transactional(rollbackFor = Exception.class)
    public boolean saveStaff(ProjectStaffDTO entity) {
        CxUserDTO user = parseUser(entity);

        // 如果用户不存在则新增用户，如果用户已存在则只更新用户信息
        if (entity.getUserId() == null) {
            user.setPassword(PersonConstant.PASSWORD);
            user.setDeptId(entity.getDepartmentId());
            user.setUsername(entity.getMobile());
            user.setPhone(entity.getMobile());

            R<Integer> r = userRemote.saveUserRole(user);

            if (r.getCode() == CommonConstants.SUCCESS) {
                entity.setUserId(r.getData());
                this.save(entity);
            }
        } else {
            R<Boolean> r = userRemote.editUserRole(user);

            if (r.getCode() == CommonConstants.SUCCESS) {
                this.save(entity);
            }
        }
        ProjectPersonAttrFormVo projectPersonAttrFormVo = new ProjectPersonAttrFormVo();
        projectPersonAttrFormVo.setProjectPersonAttrList(entity.getProjectPersonAttrListVos());
        projectPersonAttrFormVo.setPersonId(entity.getStaffId());
        projectPersonAttrFormVo.setProjectId(entity.getProjectId());
        projectPersonAttrFormVo.setType(PersonTypeEnum.STAFF.code);
        return projectPersonAttrService.updatePersonAttrList(projectPersonAttrFormVo);

    }

    @Override
    @TxTransaction(isStart = true)
    @Transactional(rollbackFor = Exception.class)
    public boolean editStaff(ProjectStaffDTO entity) {
        CxUserDTO user = parseUser(entity);
        R<Boolean> r = userRemote.editUserRole(user);
        if (r.getCode() == CommonConstants.SUCCESS) {
            ProjectPersonAttrFormVo projectPersonAttrFormVo = new ProjectPersonAttrFormVo();
            projectPersonAttrFormVo.setProjectPersonAttrList(entity.getProjectPersonAttrListVos());
            projectPersonAttrFormVo.setPersonId(entity.getStaffId());
            projectPersonAttrFormVo.setProjectId(entity.getProjectId());
            projectPersonAttrFormVo.setType(PersonTypeEnum.STAFF.code);
            projectPersonAttrService.updatePersonAttrList(projectPersonAttrFormVo);
            return this.updateById(entity);
        } else {
            return false;
        }
    }


    @Override
    public boolean removeByUserId(Integer userId, Integer projectId) {
        return super.remove(Wrappers.lambdaQuery(ProjectStaff.class).eq(ProjectStaff::getUserId, userId).eq(ProjectStaff::getProjectId, projectId));
    }

    @Override
    public ProjectStaffVo getStaffAttrById(String id) {
        ProjectStaff projectStaff = baseMapper.selectById(id);
        ProjectStaffVo projectStaffVo = new ProjectStaffVo();
        if (BeanUtil.isNotEmpty(projectStaff)) {
            BeanUtils.copyProperties(projectStaff, projectStaffVo);
            List<ProjectPersonAttrListVo> projectPersonAttrList = projectPersonAttrService.getPersonAttrListVo(projectStaff.getProjectId(), PersonTypeEnum.STAFF.code, id);
            projectStaffVo.setProjectPersonAttrListVos(projectPersonAttrList);
            return projectStaffVo;
        } else {
            return null;
        }
    }

    @Override
    public ProjectStaffVo getStaffByOwner() {
        Integer userId = SecurityUtils.getUser().getId();
        LambdaQueryWrapper<ProjectStaff> wrapper = Wrappers.lambdaQuery(ProjectStaff.class)
                .eq(ProjectStaff::getUserId, userId)
                .eq(ProjectStaff::getProjectId, ProjectContextHolder.getProjectId());
        ProjectStaff projectStaff = baseMapper.selectOne(wrapper);
        ProjectStaffVo projectStaffVo = new ProjectStaffVo();
        if (BeanUtil.isNotEmpty(projectStaff)) {
            BeanUtils.copyProperties(projectStaff, projectStaffVo);
            List<ProjectPersonAttrListVo> projectPersonAttrList = projectPersonAttrService.getPersonAttrListVo(projectStaff.getProjectId(), PersonTypeEnum.STAFF.code, projectStaff.getStaffId());
            projectStaffVo.setProjectPersonAttrListVos(projectPersonAttrList);
            R<SysDept> sysDeptR = remoteDeptService.getById(projectStaff.getDepartmentId());
            projectStaffVo.setDeptName(sysDeptR.getData().getName());
            return projectStaffVo;
        } else {
            return null;
        }
    }

    @Override
    public Page<ProjectStaffListVo> pageAll(Page page, String name,String deptId) {
        return baseMapper.pageAll(page, name,deptId, ProjectContextHolder.getProjectId());
    }

    @Override

    public void updatePhoneByUserId(String phone, Integer userId) {
        baseMapper.updatePhoneByUserId(phone, userId);
    }

    @Override
    @SqlParser(filter = true)
    public void updateUserIdByPhone(String phone, Integer userId) {
        baseMapper.updateUserIdByPhone(phone, userId);
    }

    @Override
    public List<String> getStaffIdByRegionId(String regionId) {
        return baseMapper.getStaffIdByRegionId(regionId);
    }

    @Override
    public List<StaffRegionVo> getManagerRegionStaffList() {
        return baseMapper.getManagerRegionStaffIdList(ProjectContextHolder.getProjectId());
    }

    @Override
    public Page<ProjectStaffListVo> staffPage(Page page, String staffId, String name) {
        ProjectStaff projectStaff = this.getById(staffId);
        if (projectStaff.getGrade().equals("") || projectStaff.getGrade().equals("1")) {
            return null;
        }
        return baseMapper.getStaffPage(page, projectStaff.getGrade(), projectStaff.getDepartmentId(), staffId, name, ProjectContextHolder.getProjectId());
    }

    @Override
    public ProjectAttendance getWorkTime(String staffId, LocalDate date) {
        Integer year = date.getYear();
        Integer month = date.getMonthValue();
        Integer day = date.getDayOfMonth();
        ProjectAttendance projectAttendance = new ProjectAttendance();
        ProjectStaffShiftDetail staffShiftDetail = getStaffShiftDetail(staffId, year, month);
        // 没有排班计划
        if (StrUtil.isBlank(staffShiftDetail.getStaffId())) {
            projectAttendance.setResult(AttendanceStatusEnum.NONE.code);
            return projectAttendance;
        }
        JSONObject obj = JSONUtil.parseObj(staffShiftDetail);
        String shiftName = (String) obj.get("day" + day);
        if (StrUtil.isBlank(shiftName) || "休息".equals(shiftName) || "排休".equals(shiftName)) {
//            throw new RuntimeException("休息");
            projectAttendance.setResult(AttendanceStatusEnum.REST.code);
            return projectAttendance;
        }
        ProjectShiftConf shiftConfVo = projectShiftConfService.getOne(new QueryWrapper<ProjectShiftConf>().lambda()
                .eq(ProjectShiftConf::getShiftName, shiftName));
//        if (ObjectUtil.isEmpty(shiftConfVo) || shiftConfVo.getShiftTimeBegin1() == null || shiftConfVo.getShiftTimeEnd1() == null) {
//            return projectAttendance;
//        }
        String[] shiftTimeBeginArr = shiftConfVo.getShiftTimeBegin1().split(":");
        String[] shiftTimeEndArr = shiftConfVo.getShiftTimeEnd1().split(":");
        LocalTime workTime = LocalTime.of(Integer.parseInt(shiftTimeBeginArr[0]), Integer.parseInt(shiftTimeBeginArr[1]));
        LocalTime offWorkTime = LocalTime.of(Integer.parseInt(shiftTimeEndArr[0]), Integer.parseInt(shiftTimeEndArr[1]));
        projectAttendance.setWorkTime(workTime);
        projectAttendance.setOffworkTime(offWorkTime);
        return projectAttendance;
    }

    public List<ProjectAttendance> getSchedulingPlan(String staffId, LocalDate date) {
        Integer year = date.getYear();
        Integer month = date.getMonthValue();
        Integer day = date.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
        List<ProjectAttendance> schedulingPlanList = new ArrayList<>();
        ProjectStaffShiftDetail staffShiftDetail = getStaffShiftDetail(staffId, year, month);
        JSONObject obj = JSONUtil.parseObj(staffShiftDetail);
        String attenDate = String.format("%s-%s-", year, month < 10 ? "0" + month : month);
        List<ProjectAttendance> attendanceList = projectAttendanceService.list(new QueryWrapper<ProjectAttendance>().lambda()
                .eq(ProjectAttendance::getStaffId, staffId)
                .likeRight(ProjectAttendance::getAttenDate, attenDate));
        for (Integer i = 1; i <= day; i++) {
            Boolean isContinue = false;
            ProjectAttendance attendance = new ProjectAttendance();
            attendance.setStaffId(staffId);
            LocalDate newDate = LocalDate.of(year, month, i);
            attendance.setAttenDate(newDate);
            String shiftName = (String) obj.get("day" + i);
            for (ProjectAttendance projectAttendance : attendanceList) {
                //i - 1   解决目前该接口返回的考勤信息是昨天及昨天以前的信息，需要修改为今天及今天以前的信息
                if (i - 1 >= LocalDate.now().getDayOfMonth()) {
                    break;
                }
                if (newDate.equals(projectAttendance.getAttenDate())) {
                    isContinue = true;
                    schedulingPlanList.add(projectAttendance);
                    break;
                }
            }
            if (isContinue) {
                continue;
            }
            if (StrUtil.isBlank(shiftName) || "休息".equals(shiftName) || "排休".equals(shiftName)) {
                attendance.setResult(AttendanceStatusEnum.REST.code);
            } else {
                ProjectShiftConf shiftConfVo = projectShiftConfService.getOne(new QueryWrapper<ProjectShiftConf>().lambda()
                        .eq(ProjectShiftConf::getShiftName, shiftName));
                if (ObjectUtil.isNotEmpty(shiftConfVo) && shiftConfVo.getShiftTimeBegin1() != null && shiftConfVo.getShiftTimeEnd1() != null) {
                    shiftConfVo.getShiftTimeBegin1().split(":");
                    String[] shiftTimeBeginArr = shiftConfVo.getShiftTimeBegin1().split(":");
                    String[] shiftTimeEndArr = shiftConfVo.getShiftTimeEnd1().split(":");
                    LocalTime workTime = LocalTime.of(Integer.parseInt(shiftTimeBeginArr[0]), Integer.parseInt(shiftTimeBeginArr[1]));
                    LocalTime offWorkTime = LocalTime.of(Integer.parseInt(shiftTimeEndArr[0]), Integer.parseInt(shiftTimeEndArr[1]));
                    attendance.setWorkTime(workTime);
                    attendance.setOffworkTime(offWorkTime);
                    if (newDate.isBefore(LocalDate.now())) {
                        attendance.setResult(AttendanceStatusEnum.ABSENTEEISM.code);
                    }
                }
            }
            schedulingPlanList.add(attendance);
        }
        return schedulingPlanList;
    }

    private ProjectStaffShiftDetail getStaffShiftDetail(String staffId, Integer year, Integer month) {
        Integer day = 31;
        JSONObject projectStaffShiftDetailObj = JSONUtil.createObj();
        ProjectStaffShiftDetail projectStaffShiftDetail = new ProjectStaffShiftDetail();
        List<ProjectStaffShiftDetail> staffShiftDetailList = projectStaffShiftDetailService.list(new QueryWrapper<ProjectStaffShiftDetail>().lambda()
                .eq(ProjectStaffShiftDetail::getStaffId, staffId)
                .eq(ProjectStaffShiftDetail::getPlanYear, year)
                .eq(ProjectStaffShiftDetail::getPlanMonth, month));
        if (ObjectUtil.isEmpty(staffShiftDetailList)) {
            return projectStaffShiftDetail;
        }
        for (ProjectStaffShiftDetail staffShiftDetail : staffShiftDetailList) {
            JSONObject obj = JSONUtil.parseObj(staffShiftDetail);
            for (Integer i = 1; i <= day; i++) {
                String shiftName = (String) obj.get("day" + i);
                if (shiftName != null && !"".equals(shiftName) && !"休息".equals(shiftName) && !"排休".equals(shiftName)) {
                    projectStaffShiftDetailObj.set("day" + i, shiftName);
                }
            }
        }
        projectStaffShiftDetail = JSONUtil.toBean(projectStaffShiftDetailObj, ProjectStaffShiftDetail.class);
        projectStaffShiftDetail.setStaffId(staffId);
        projectStaffShiftDetail.setPlanYear(String.valueOf(year));
        projectStaffShiftDetail.setPlanMonth(String.valueOf(month));
        return projectStaffShiftDetail;
    }

    public Page<ProjectStaffShiftDetailPageVo> getDeptSchedulingPage(Page page, Integer deptId, Integer year, Integer month) {
        return baseMapper.getDeptSchedulingPage(page, deptId, year, month);
    }

    public List<ProjectStaffShiftDetailPageVo> getDeptScheduling(Integer deptId) {
        return baseMapper.getDeptScheduling(deptId);
    }

    @Override
    public List<String> selectId(Integer projectId) {
        return baseMapper.selectId(projectId);
    }

    @Override
    public List<String> getStaffIdListByMenuId(Integer menuId) {

        return baseMapper.getStaffIdListByMenuId(menuId, ProjectContextHolder.getProjectId());
    }

    /**
     * staff to user
     *
     * @param staff
     * @return
     */
    protected CxUserDTO parseUser(ProjectStaffDTO staff) {
        CxUserDTO user = new CxUserDTO();

        user.setUserId(staff.getUserId());
        user.setAvatar(staff.getPicUrl());
        user.setSex(staff.getSex());
        user.setCredentialType(staff.getCredentialType());
        user.setCredentialNo(staff.getCredentialNo());
        user.setTrueName(staff.getStaffName());
        user.setNewRoleId(staff.getNewRoleId());
        user.setRoleId(staff.getOldRoleId());
        user.setRoleExpTime(staff.getRoleExpTime());

        return user;
    }
}
