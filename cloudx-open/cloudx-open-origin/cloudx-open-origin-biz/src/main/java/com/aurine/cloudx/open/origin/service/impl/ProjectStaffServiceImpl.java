package com.aurine.cloudx.open.origin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.open.common.entity.vo.StaffInfoVo;
import com.aurine.cloudx.open.origin.constant.PersonConstant;
import com.aurine.cloudx.open.origin.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.open.origin.dto.ProjectStaffDTO;
import com.aurine.cloudx.open.origin.entity.ProjectAttendance;
import com.aurine.cloudx.open.origin.entity.ProjectShiftConf;
import com.aurine.cloudx.open.origin.entity.ProjectStaff;
import com.aurine.cloudx.open.origin.entity.ProjectStaffShiftDetail;
import com.aurine.cloudx.open.origin.mapper.ProjectStaffMapper;
import com.aurine.cloudx.open.origin.service.*;
import com.aurine.cloudx.open.origin.vo.*;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.seata.spring.annotation.GlobalTransactional;
import com.pig4cloud.pigx.admin.api.dto.CxUserDTO;
import com.pig4cloud.pigx.admin.api.dto.UserInfo;
import com.pig4cloud.pigx.admin.api.feign.RemoteUserService;
import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 项目员工信息表
 *
 * @author lingang
 * @date 2020-05-11 13:38:09
 */
@Service
@Primary
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
    private ProjectInspectPlanShiftStaffService projectInspectPlanShiftStaffService;
    @Resource
    private ProjectInspectPlanShift3Service projectInspectPlanShift3Service;
    @Resource
    private RemoteUserService remoteUserService;
    @Resource
    private ProjectStaffShiftDetailService projectStaffShiftDetailService;
    @Resource
    private ProjectAttendanceService projectAttendanceService;
    @Resource
    private ProjectShiftConfService projectShiftConfService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @GlobalTransactional
    public boolean removeStaff(String id) {
        ProjectStaff staff = this.getById(id);


        R<Boolean> r = userRemote.removeUserRole(staff.getUserId(), staff.getRoleId());


        if (r.getCode() == CommonConstants.SUCCESS) {
            /**
             * 删除员工也一并删除权限，凭证
             * @author: 王伟
             * @since : 2020-09-21
             */
            //迁出介质权限
            projectRightDeviceService.removeCertDeviceAuthorize(id);

            //更新权限
            projectPersonDeviceService.refreshByPersonId(id, PersonTypeEnum.STAFF);
            // 删除拓展属性值
            projectPersonAttrService.removePersonAttrList(id);
            boolean removeResult = this.removeById(id);
            if (removeResult) {
                projectInspectPlanShift3Service.removePerson(id);
            }
            return removeResult;
        } else {
            throw new RuntimeException(r.getMsg());
//            return false;
        }
    }

    /**
     * 根据员工第三方id，保存或更新员工，用于WR20
     *
     * @param entity
     * @return
     */
    @Override
    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public String saveOrUpdateStaffByStaffCode(ProjectStaffDTO entity) {

//        //如果员工已存在，不进行同步，直接跳过
//        List<ProjectStaff> staffList = this.list(new QueryWrapper<ProjectStaff>().lambda().eq(ProjectStaff::getStaffCode, entity.getStaffCode()));
//        if (CollUtil.isNotEmpty(staffList)) {
//            return staffList.get(0).getStaffId();
//        }


        //获取用户账号信息
        R<UserInfo> requestUser = remoteUserService.info(entity.getMobile(), SecurityConstants.FROM_IN);
        if (requestUser.getCode() == 0 && ObjectUtil.isNotEmpty(requestUser.getData())) {
            entity.setUserId(requestUser.getData().getSysUser().getUserId());
        }
        CxUserDTO user = parseUser(entity);

        // 如果用户不存在则新增用户，如果用户已存在则只更新用户信息
        if (entity.getUserId() == null) {
            user.setPassword(PersonConstant.PASSWORD);
            user.setDeptId(entity.getDepartmentId());
            user.setUsername(entity.getMobile());
            user.setPhone(entity.getMobile());

            R<Integer> r = userRemote.saveUserRoleWithForm(user, SecurityConstants.FROM_IN);//WR20同步数据无授权信息

            if (r.getCode() == CommonConstants.SUCCESS) {
                entity.setUserId(r.getData());
                return this.saveStaffByCode(entity);
            }
        } else {
            R<Boolean> r = userRemote.editUserRoleWithForm(user, SecurityConstants.FROM_IN);

            if (r.getCode() == CommonConstants.SUCCESS) {
                return this.saveStaffByCode(entity);
            }
        }

        return "";
    }

    private String saveStaffByCode(ProjectStaffDTO entity) {
        String uid = "";
        List<ProjectStaff> staffList = this.list(new QueryWrapper<ProjectStaff>().lambda()
                .eq(ProjectStaff::getStaffCode, entity.getStaffCode())
                .eq(ProjectStaff::getProjectId, entity.getProjectId())
        );
        if (CollUtil.isNotEmpty(staffList)) {
            uid = staffList.get(0).getStaffId();
        } else {
            uid = UUID.randomUUID().toString().replace("-", "");
            entity.setStaffId(uid);
            this.save(entity);
        }
        return uid;
    }


    @Override
    @GlobalTransactional
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
    @GlobalTransactional
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


        return baseMapper.removeByUserId(userId, projectId);
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
        if (SecurityUtils.getAuthentication() == null) {
            return null;
        }
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
            return projectStaffVo;
        } else {
            return null;
        }
    }

    @Override
    public Page<ProjectStaffListVo> pageAll(Page page, String name) {
        return baseMapper.pageAll(page, name, ProjectContextHolder.getProjectId());
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
        if ("".equals(projectStaff.getGrade()) || "1".equals(projectStaff.getGrade())) {
            return null;
        }
        return baseMapper.getStaffPage(page, projectStaff.getGrade(), projectStaff.getDepartmentId(), staffId, name);
    }

    @Override
    public ProjectStaff getStaffByUserId(Integer userId, Integer deptId) {
        return baseMapper.getStaffByUserId(userId, deptId);
    }

    /**
     * 根据部门id获取当前部门下为管理员的员工
     *
     * @param deptId
     * @return
     */
    @Override
    public List<SysUserVo> getUserVosByDeptId(Integer deptId) {
        baseMapper.getStaffByUserId(1, deptId);
        return this.baseMapper.getUserVosByDeptId(deptId, "%管理员");
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
//        user.setRoleExpTime(staff.getRoleExpTime());

        return user;
    }

    ProjectStaffShiftDetail getStaffShiftDetail(String staffId, Integer year, Integer month) {
        Integer day = 31;
        JSONObject projectStaffShiftDetailObj = JSONUtil.createObj();
        List<ProjectStaffShiftDetail> staffShiftDetailList = projectStaffShiftDetailService.list(new QueryWrapper<ProjectStaffShiftDetail>().lambda()
                .eq(ProjectStaffShiftDetail::getStaffId, staffId)
                .eq(ProjectStaffShiftDetail::getPlanYear, year)
                .eq(ProjectStaffShiftDetail::getPlanMonth, month));
        for (ProjectStaffShiftDetail staffShiftDetail : staffShiftDetailList) {
            JSONObject obj = JSONUtil.parseObj(staffShiftDetail);
            for (Integer i = 1; i <= day; i++) {
                String shiftName = (String) obj.get("day" + i);
                if (shiftName != null && !"".equals(shiftName) && !"休息".equals(shiftName) && !"排休".equals(shiftName)) {
                    projectStaffShiftDetailObj.set("day" + i, shiftName);
                }
            }
        }
        ProjectStaffShiftDetail projectStaffShiftDetail = JSONUtil.toBean(projectStaffShiftDetailObj, ProjectStaffShiftDetail.class);
        projectStaffShiftDetail.setStaffId(staffId);
        projectStaffShiftDetail.setPlanYear(String.valueOf(year));
        projectStaffShiftDetail.setPlanMonth(String.valueOf(month));
        return projectStaffShiftDetail;
    }

    @Override
    public List<ProjectAttendance> getSchedulingPlan(String staffId, LocalDate date) {
        Integer year = date.getYear();
        Integer month = date.getMonthValue();
        Integer day = date.getMonth().maxLength();
        List<ProjectAttendance> schedulingPlanList = new ArrayList<>();
        ProjectStaffShiftDetail staffShiftDetail = getStaffShiftDetail(staffId, year, month);
        if (ObjectUtil.isEmpty(staffShiftDetail)) {
            throw new RuntimeException("未设置排班计划");
        }
        JSONObject obj = JSONUtil.parseObj(staffShiftDetail);
        String attenDate = String.format("%s-%s-", year, month < 10 ? "0" + month : month);
        List<ProjectAttendance> attendanceList = projectAttendanceService.list(new QueryWrapper<ProjectAttendance>().lambda()
                .eq(ProjectAttendance::getStaffId, staffId)
                .likeRight(ProjectAttendance::getAttenDate, attenDate));
        //获取排班计划
        List<ProjectStaffShiftDetail> staffShiftDetailList = projectStaffShiftDetailService.list(new QueryWrapper<ProjectStaffShiftDetail>().lambda()
                .eq(ProjectStaffShiftDetail::getStaffId, staffId)
                .eq(ProjectStaffShiftDetail::getPlanYear, year)
                .eq(ProjectStaffShiftDetail::getPlanMonth, month));
        for (Integer i = 1; i <= day; i++) {
            Boolean isContinue = false;
            ProjectAttendance attendance = new ProjectAttendance();
            attendance.setStaffId(staffId);

            LocalDate newDate = LocalDate.of(year, month, i);
            attendance.setAttenDate(newDate);
            String shiftName = (String) obj.get("day" + i);
            ProjectShiftConf shiftConfVo = new ProjectShiftConf();
            if (StringUtil.isNotEmpty(shiftName)) {
                //获取班次的时间
                shiftConfVo = projectShiftConfService.getOne(new QueryWrapper<ProjectShiftConf>().lambda()
                        .eq(ProjectShiftConf::getShiftName, shiftName));
            }

            boolean isRest = shiftName == null || "".equals(shiftName) || "休息".equals(shiftName) || "排休".equals(shiftName);
            for (ProjectAttendance projectAttendance : attendanceList) {
                if (newDate.equals(projectAttendance.getAttenDate())) {
                    isContinue = true;
                    if (CollectionUtil.isNotEmpty(staffShiftDetailList)) {
                        //在排班记录除外的时间打卡把打卡设置成休息
                        if (newDate.isEqual(projectAttendance.getAttenDate()) && (isRest)) {
                            projectAttendance.setResult("0");
                        }
                    }
                    schedulingPlanList.add(projectAttendance);
                    break;
                }
            }
            if (isContinue) {
                continue;
            }
            //排休并且排班考勤不为空
            if (isRest && CollectionUtil.isNotEmpty(staffShiftDetailList)) {
                attendance.setResult("0");
            } else if (CollectionUtil.isEmpty(staffShiftDetailList)) {
                //如果没有排班考勤
                if (newDate.isBefore(LocalDate.now())) {
                    attendance.setResult("5");
                }
            } else {

                if (ObjectUtil.isNotEmpty(shiftConfVo) && shiftConfVo.getShiftTimeBegin1() != null && shiftConfVo.getShiftTimeEnd1() != null) {
                    LocalTime workTime = LocalTime.parse(shiftConfVo.getShiftTimeBegin1(), DateTimeFormatter.ofPattern("HH:mm"));
                    LocalTime offWorkTime = LocalTime.parse(shiftConfVo.getShiftTimeEnd1(), DateTimeFormatter.ofPattern("HH:mm"));
                    attendance.setWorkTime(workTime);
                    attendance.setOffworkTime(offWorkTime);
                    if (newDate.isBefore(LocalDate.now())) {
                        attendance.setResult("5");
                    }
                }
            }
            schedulingPlanList.add(attendance);
        }
        return schedulingPlanList;
    }

    @Override
    public List<ProjectAttendance> getSchedulingPlanVo(String staffId, ProjectAttendance projectAttendanceMap) {
        LocalDate date = projectAttendanceMap.getAttenDate();
        Integer year = date.getYear();
        Integer month = date.getMonthValue();
        Integer day = date.getMonth().maxLength();
        List<ProjectAttendance> schedulingPlanList = new ArrayList<>();
        ProjectStaffShiftDetail staffShiftDetail = getStaffShiftDetail(staffId, year, month);
        if (ObjectUtil.isEmpty(staffShiftDetail)) {
            throw new RuntimeException("未设置排班计划");
        }
        JSONObject obj = JSONUtil.parseObj(staffShiftDetail);
        String attenDate = String.format("%s-%s", year, month < 10 ? "0" + month : month);
        List<ProjectAttendance> attendanceList = projectAttendanceService.list(new QueryWrapper<ProjectAttendance>().lambda()
                .eq(ProjectAttendance::getStaffId, staffId)
                .likeRight(ProjectAttendance::getAttenDate, attenDate));

        List<ProjectStaffShiftDetail> staffShiftDetailList = projectStaffShiftDetailService.list(new QueryWrapper<ProjectStaffShiftDetail>().lambda()
                .eq(ProjectStaffShiftDetail::getStaffId, staffId)
                .eq(ProjectStaffShiftDetail::getPlanYear, year)
                .eq(ProjectStaffShiftDetail::getPlanMonth, month));
        for (Integer i = 1; i <= day; i++) {
            Boolean isContinue = false;
            ProjectAttendance attendance = new ProjectAttendance();
            attendance.setStaffId(staffId);
            attendance.setStaffName(projectAttendanceMap.getStaffName());
            attendance.setDeptName(projectAttendanceMap.getDeptName());
            LocalDate newDate = LocalDate.of(year, month, i);
            attendance.setAttenDate(newDate);
            String shiftName = (String) obj.get("day" + i);
            ProjectShiftConf shiftConfVo = new ProjectShiftConf();
            if (StringUtil.isNotEmpty(shiftName)) {
                shiftConfVo = projectShiftConfService.getOne(new QueryWrapper<ProjectShiftConf>().lambda()
                        .eq(ProjectShiftConf::getShiftName, shiftName));
            }
            boolean isRest = shiftName == null || "".equals(shiftName) || "休息".equals(shiftName) || "排休".equals(shiftName);
            for (ProjectAttendance projectAttendance : attendanceList) {
                if (newDate.equals(projectAttendance.getAttenDate())) {
                    isContinue = true;
                    if (CollectionUtil.isNotEmpty(staffShiftDetailList)) {
                        if (newDate.isEqual(projectAttendance.getAttenDate()) && isRest) {
                            projectAttendance.setResult("0");
                        }
                    }
                    schedulingPlanList.add(projectAttendance);
                    break;
                }
            }
            if (isContinue) {
                continue;
            }
            if (isRest && CollectionUtil.isNotEmpty(staffShiftDetailList)) {
                attendance.setResult("0");
            } else if (CollectionUtil.isEmpty(staffShiftDetailList)) {
                //如果没有排班考勤
                if (newDate.isBefore(LocalDate.now())) {
                    attendance.setResult("5");
                }
            } else {
                if (ObjectUtil.isNotEmpty(shiftConfVo) && shiftConfVo.getShiftTimeBegin1() != null && shiftConfVo.getShiftTimeEnd1() != null) {
                    LocalTime workTime = LocalTime.parse(shiftConfVo.getShiftTimeBegin1(), DateTimeFormatter.ofPattern("HH:mm"));
                    LocalTime offWorkTime = LocalTime.parse(shiftConfVo.getShiftTimeEnd1(), DateTimeFormatter.ofPattern("HH:mm"));
                    attendance.setWorkTime(workTime);
                    attendance.setOffworkTime(offWorkTime);
                    if (newDate.isBefore(LocalDate.now())) {
                        attendance.setResult("5");
                    }
                }
            }
            schedulingPlanList.add(attendance);
        }
        return schedulingPlanList;
    }

    @Override
    public List<ProjectStaff> getStaffIdByProjectId(Integer projectId) {
        return getBaseMapper().selectList(new QueryWrapper<ProjectStaff>().lambda().eq(ProjectStaff::getProjectId, projectId));
    }

    @Override
    public void updateEmployeeAvatar(AppFaceHeadPortraitVo appFaceHeadPortraitVo) {

        this.update(null, new UpdateWrapper<ProjectStaff>()
                .lambda()
                .eq(ProjectStaff::getStaffId, appFaceHeadPortraitVo.getPersonId())
                .set(ProjectStaff::getPicUrl, appFaceHeadPortraitVo.getHeadPortraitUrl())
        );
    }

    @Override
    public Boolean updatePhoneById(Integer userId, String phone) {
        //先判断是不是员工
        if (!CollUtil.isNotEmpty(baseMapper.getStaffListByUserId(userId))) {
            return Boolean.FALSE;
        }
        try {
            baseMapper.updatePhoneByUserId(phone, userId);
        } catch (Exception ex) {

            ex.printStackTrace();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    public List<String> getStaffIdListByMenuId(Integer menuId) {
        Integer projectId = ProjectContextHolder.getProjectId();
        return baseMapper.getStaffIdListByMenuId(menuId, projectId);
    }

    @Override
    public Integer getDepartmentId(Integer projectId) {
        return baseMapper.getDepartmentId(projectId);
    }

    @Override
    public Integer getRoleId(Integer projectId) {
        return baseMapper.getRoleId(projectId);
    }

    @Override
    public Page<StaffInfoVo> page(Page page, StaffInfoVo vo) {
        ProjectStaff po = new ProjectStaff();
        BeanUtils.copyProperties(vo, po);

        return baseMapper.page(page, po);
    }

}

