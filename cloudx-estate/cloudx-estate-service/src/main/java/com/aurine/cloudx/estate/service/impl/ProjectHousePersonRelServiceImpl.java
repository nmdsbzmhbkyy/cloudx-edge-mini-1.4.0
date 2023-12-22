
package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.*;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.feign.RemoteFaceResourcesService;
import com.aurine.cloudx.estate.feign.RemoteHousePersonRelService;
import com.aurine.cloudx.estate.feign.RemoteProjectPersonDeviceService;
import com.aurine.cloudx.estate.util.NoticeUtil;
import com.aurine.cloudx.estate.constant.DeviceExcelConstant;
import com.aurine.cloudx.estate.constant.IsFocusPersonConstants;
import com.aurine.cloudx.estate.constant.ProjectConfigConstant;
import com.aurine.cloudx.estate.dto.ProjectHouseDTO;
import com.aurine.cloudx.estate.mapper.ProjectHousePersonRelMapper;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.admin.api.feign.RemoteUserService;
import com.pig4cloud.pigx.common.core.exception.ValidateCodeException;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 住户
 *
 * @author pigx code generator
 * @date 2020-05-11 08:17:43
 */
@Service
@Slf4j
public class ProjectHousePersonRelServiceImpl extends ServiceImpl<ProjectHousePersonRelMapper, ProjectHousePersonRel> implements ProjectHousePersonRelService {
    // 人员信息
    @Resource
    private ProjectPersonInfoService projectPersonInfoService;
    @Resource
    private ProjectHousePersonRelMapper projectHousePersonRelMapper;

    @Resource
    private ProjectPersonLabelService projectPersonLabelService;

    @Resource
    private ProjectPersonAttrService projectPersonAttrService;

    @Resource
    private ProjectFocusPersonAttrService projectFocusPersonAttrService;
    @Resource
    private ProjectConfigService projectConfigService;

    @Resource
    private ProjectPersonDeviceService projectPersonDeviceService;


    @Resource
    private ProjectParkingPlaceService projectParkingPlaceService;

    @Resource
    private ProjectParCarRegisterService projectParCarRegisterService;

    @Resource
    private RemoteUserService remoteUserService;

    @Resource
    private RemoteHousePersonRelService remoteHousePersonRelService;
    @Resource
    private RemoteFaceResourcesService remoteFaceResourcesService;
    @Resource
    private RemoteProjectPersonDeviceService remoteProjectPersonDeviceService;
    @Resource
    private NoticeUtil noticeUtil;
    private int one;

    // 用户审核菜单ID


    private final Integer userVerifyMenuId = 10689;


    /**
     * 查询住户
     *
     * @param page
     * @param searchConditionVo 查询条件
     * @return
     */
    @Override
    public IPage<ProjectHousePersonRelRecordVo> findPage(IPage<ProjectHousePersonRelRecordVo> page, ProjectHousePersonRelSearchConditionVo searchConditionVo) {
        // 查询住户信息，如果是租客，且租赁时间到期，则不显示。
        // 数据采用伪删除，0迁出，1迁入
        return projectHousePersonRelMapper.select(
                page,
                searchConditionVo.getBuildingName(),
                searchConditionVo.getUnitName(),
                searchConditionVo.getHouseName(),
                searchConditionVo.getPersonName(),
                searchConditionVo.getBuildingId(),
                searchConditionVo.getUnitId(),
                searchConditionVo.getHouseId(),
                searchConditionVo.getAuditStatus(),
                searchConditionVo.getPhone(),
                searchConditionVo.getPersonId(),
                ProjectContextHolder.getProjectId()
        );
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public R request(ProjectHousePersonRelRequestVo projectHousePersonRelRequestVo) {
        ProjectPersonInfo personInfo = projectPersonInfoService.getByTelephone(projectHousePersonRelRequestVo.getTelephone());
        String personId;

        //检查当前项目下业主信息是否存在
        if (ObjectUtil.isNotEmpty(personInfo)) {

            personId = personInfo.getPersonId();
            //检查该人员是否已经存在当前房屋内
            int num = this.count(new QueryWrapper<ProjectHousePersonRel>().lambda()
                    .eq(ProjectHousePersonRel::getPersonId, personId)
                    .eq(ProjectHousePersonRel::getHouseId, projectHousePersonRelRequestVo.getHouseId())
                    .ne(ProjectHousePersonRel::getAuditStatus, AuditStatusEnum.notPass.code));
            boolean havePersonInHouse = num != 0;
            if (havePersonInHouse) {
                return R.failed("该用户已经在当前房屋下了");
            }
            //查询是否存在手机账号对应的userId 更新对应用户信息
            R<SysUser> requestUser = remoteUserService.user(projectHousePersonRelRequestVo.getTelephone());
            if (requestUser.getCode() == 0 && ObjectUtil.isNotEmpty(requestUser.getData())) {
                personInfo.setUserId(requestUser.getData().getUserId());
            }

            personInfo.setPersonName(projectHousePersonRelRequestVo.getPersonName());
            personInfo.setCredentialType("111");

            projectPersonInfoService.updateById(personInfo);
            // 判断当前用户是否在未通过
            ProjectHousePersonRel notPassHousePersonrel = this.getOne(new QueryWrapper<ProjectHousePersonRel>().lambda()
                    .eq(ProjectHousePersonRel::getPersonId, personId)
                    .eq(ProjectHousePersonRel::getHouseId, projectHousePersonRelRequestVo.getHouseId())
                    .eq(ProjectHousePersonRel::getAuditStatus, AuditStatusEnum.notPass.code));
            if (ObjectUtil.isNotEmpty(notPassHousePersonrel)) {
                notPassHousePersonrel.setAuditStatus(AuditStatusEnum.inAudit.code);
                notPassHousePersonrel.setHouseholdType(projectHousePersonRelRequestVo.getHouseholdType());
                notPassHousePersonrel.setMemberType(projectHousePersonRelRequestVo.getMemberType());
                notPassHousePersonrel.setRentStartTime(projectHousePersonRelRequestVo.getRentStartTime());
                notPassHousePersonrel.setRentStopTime(projectHousePersonRelRequestVo.getRentStopTime());

                //给拥有住户审核模块的员工发送消息
                sendAssignNotice(projectHousePersonRelRequestVo);
                return R.ok(this.updateById(notPassHousePersonrel));
            }
        } else {
            // 人员不存在,则新增人员
            personInfo = new ProjectPersonInfo();
            //查询是否存在手机账号对应的userId 更新对应用户信息
            R<SysUser> requestUser = remoteUserService.user(projectHousePersonRelRequestVo.getTelephone());
            if (requestUser.getCode() == 0 && ObjectUtil.isNotEmpty(requestUser.getData())) {
                personInfo.setUserId(requestUser.getData().getUserId());
            }
            personId = UUID.randomUUID().toString().replaceAll("-", "");
            BeanUtils.copyProperties(projectHousePersonRelRequestVo, personInfo);
            personInfo.setPersonId(personId);
            personInfo.setTelephone(projectHousePersonRelRequestVo.getTelephone());
            personInfo.setCredentialPicBack(null);
            personInfo.setCredentialPicFront(null);
            personInfo.setPStatus("1");
            personInfo.setCredentialType("111");

            projectPersonInfoService.saveFromSystem(personInfo);
        }

        //将人员信息植入人屋关系中
        ProjectHousePersonRel projectHousePersonRel = new ProjectHousePersonRel();
        BeanUtils.copyProperties(projectHousePersonRelRequestVo, projectHousePersonRel);
        ProjectConfig config = projectConfigService.getConfig();
        if (ProjectConfigConstant.SYSTEM_IDENTITY.equals(config.getAuthAudit())) {
            projectHousePersonRel.setAuditStatus(AuditStatusEnum.pass.code);
            // 该房屋是否存在业主
            ProjectHousePersonRel HousePersonRel = this.getOne(Wrappers.lambdaQuery(ProjectHousePersonRel.class)
                    .eq(ProjectHousePersonRel::getHouseId, projectHousePersonRel.getHouseId())
                    .eq(ProjectHousePersonRel::getAuditStatus, AuditStatusEnum.pass.code)
                    .eq(ProjectHousePersonRel::getHouseholdType, HouseHoldTypeEnum.OWNER.code));
            if (HouseHoldTypeEnum.OWNER.code.equals(projectHousePersonRel.getHouseholdType()) && ObjectUtil.isNotEmpty(HousePersonRel)) {
                remoteHousePersonRelService.removeById(HousePersonRel.getRelaId());
            }
        } else {
            projectHousePersonRel.setAuditStatus(AuditStatusEnum.inAudit.code);
        }
        projectHousePersonRel.setPersonId(personId);
        projectHousePersonRel.setOrigin(OriginTypeEnum.WECHAT.code);

        //租凭开始若为空则设置为当前时间
        if (HouseHoldTypeEnum.TENANT.code.equals(projectHousePersonRel.getHouseholdType()) && ObjectUtil.isNull(projectHousePersonRel.getRentStartTime())) {
            projectHousePersonRel.setRentStartTime(LocalDateTime.now());
        }
        if (StringUtils.isBlank(projectHousePersonRel.getHousePeopleRel())) {
            projectHousePersonRel.setHousePeopleRel(HousePersonRelTypeEnum.PROPRIETOR.code);
        }
        this.save(projectHousePersonRel);

        if (projectHousePersonRel.getAuditStatus().equals(AuditStatusEnum.inAudit.code)) {
            //给拥有住户审核模块的员工发送消息
            sendAssignNotice(projectHousePersonRelRequestVo);
        }else if (projectHousePersonRel.getAuditStatus().equals(AuditStatusEnum.pass.code)){
            //参照平台修改权限配置
            remoteProjectPersonDeviceService.refreshByPersonId(personId,PersonTypeEnum.PROPRIETOR);
        }

        return R.ok();
    }

    @Override
    public List<ProjectHouseHisRecordVo> findByName(String name) {
        return baseMapper.findByName(name, ProjectContextHolder.getProjectId());
    }


    @Override
    public ProjectHousePersonRelVo checkHouseRel(String houseId, String personName, String houseHoldType, String personId) {
        ProjectHousePersonRelVo projectHousePersonRelVo = new ProjectHousePersonRelVo();
        //如果是业主
        if (houseHoldType.equals(HouseHoldTypeEnum.OWNER.code)) {
            //当前房屋下的业主
            ProjectHousePersonRel projectHousePersonRel = getOne(Wrappers.lambdaQuery(ProjectHousePersonRel.class)
                    .eq(ProjectHousePersonRel::getHouseId, houseId)
                    .eq(ProjectHousePersonRel::getHouseholdType, houseHoldType)
                    .eq(ProjectHousePersonRel::getAuditStatus, AuditStatusEnum.pass.code)
                    .ne(ProjectHousePersonRel::getPersonId, personId));
            if (BeanUtil.isNotEmpty(projectHousePersonRel)) {
                ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getById(projectHousePersonRel.getPersonId());
                BeanUtils.copyProperties(projectPersonInfo, projectHousePersonRelVo);
                projectHousePersonRelVo.setHouseholdType(houseHoldType);
                return projectHousePersonRelVo;
            } else {
                return null;
            }
        } else {
            //当前房屋下家属或租客集合
            List<ProjectHousePersonRel> list = list(Wrappers.lambdaQuery(ProjectHousePersonRel.class)
                    .eq(ProjectHousePersonRel::getHouseId, houseId)
                    .eq(ProjectHousePersonRel::getAuditStatus, AuditStatusEnum.pass.code)
                    .ne(ProjectHousePersonRel::getHouseholdType, HouseHoldTypeEnum.OWNER.code)
                    .ne(ProjectHousePersonRel::getPersonId, personId));
            List<String> personIds = list.stream().map(ProjectHousePersonRel::getPersonId).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(personIds)) {
                List<ProjectPersonInfo> projectPersonInfos = projectPersonInfoService.listByIds(personIds);
                String pid = null;
                for (ProjectPersonInfo projectPersonInfo : projectPersonInfos) {
                    if (projectPersonInfo.getPersonName().equals(personName)) {
                        pid = projectPersonInfo.getPersonId();
                    }
                }
                if (pid != null) {
                    ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getById(pid);
                    ProjectHousePersonRel housePersonRel = getOne(Wrappers.lambdaQuery(ProjectHousePersonRel.class)
                            .eq(ProjectHousePersonRel::getPersonId, pid)
                            .eq(ProjectHousePersonRel::getHouseId, houseId));
                    BeanUtils.copyProperties(projectPersonInfo, projectHousePersonRelVo);
                    projectHousePersonRelVo.setHouseholdType(housePersonRel.getHouseholdType());
                    return projectHousePersonRelVo;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
    }


    @Override
    public IPage<ProjectHousePersonRelRecordVo> pageIdentity(IPage<ProjectHousePersonRelRecordVo> page, ProjectHousePersonRelSearchConditionVo searchConditionVo) {
        return projectHousePersonRelMapper.pageIdentity(
                page,
                searchConditionVo.getBuildingName(),
                searchConditionVo.getUnitName(),
                searchConditionVo.getHouseName(),
                searchConditionVo.getPersonName(),
                searchConditionVo.getHouseId(),
                searchConditionVo.getPhone(),
                searchConditionVo.getAuditStatus(),
                ProjectContextHolder.getProjectId());
    }


    /**
     * 更新住户信息
     *
     * @param projectHousePersonRelVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(ProjectHousePersonRelVo projectHousePersonRelVo) {

        //更新住户-房屋信息
        ProjectHousePersonRel projectHousePersonRel = new ProjectHousePersonRel();
        BeanUtils.copyProperties(projectHousePersonRelVo, projectHousePersonRel);

        //修改人员信息
        if (ObjectUtil.isNotEmpty(projectHousePersonRel.getPersonId())) {
            ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getById(projectHousePersonRel.getPersonId());
            BeanUtils.copyProperties(projectHousePersonRelVo, projectPersonInfo);
            projectPersonInfoService.updateById(projectPersonInfo);

            //更新人员标签
            projectPersonLabelService.addLabel(projectHousePersonRelVo.getTagArray(), projectPersonInfo.getPersonId());
        }


        // 更新重点人员信息
        this.operateFocusAttr(projectHousePersonRelVo);

        //设置拓展属性
        ProjectPersonAttrFormVo projectPersonAttrFormVo = new ProjectPersonAttrFormVo();
        projectPersonAttrFormVo.setProjectPersonAttrList(projectHousePersonRelVo.getProjectPersonAttrList());
        projectPersonAttrFormVo.setPersonId(projectHousePersonRelVo.getPersonId());
        projectPersonAttrFormVo.setProjectId(ProjectContextHolder.getProjectId());
        projectPersonAttrFormVo.setType(PersonTypeEnum.PROPRIETOR.code);
        projectPersonAttrService.updatePersonAttrList(projectPersonAttrFormVo);
        return this.updateById(projectHousePersonRel);

    }

    /**
     * 获取
     *
     * @param id
     * @return
     */
    @Override
    public ProjectHousePersonRelVo getVoById(String id) {


        //获取人屋关系信息
        ProjectHousePersonRel projectHousePersonRel = this.getById(id);

        //获取人员信息
        ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getById(projectHousePersonRel.getPersonId());

        //拼装VO
        ProjectHousePersonRelVo projectHousePersonRelVo = new ProjectHousePersonRelVo();

        //转换tag
        projectHousePersonRelVo.setTagArray(StringUtils.split(projectPersonInfo.getTag(), ','));

        //转换人员标签
        List<ProjectPersonLabel> lableList = projectPersonLabelService.listByPersonId(projectHousePersonRel.getPersonId());
        String[] lableArray = lableList.stream().map(ProjectPersonLabel::getLabelId).collect(Collectors.toList()).toArray(new String[lableList.size()]);
        projectHousePersonRelVo.setTagArray(lableArray);

        // 获取重点人员信息（如果有的话）
        ProjectFocusPersonAttr focusPersonAttr = projectFocusPersonAttrService.getFocusPersonAttrByPersonId(projectHousePersonRel.getPersonId());

        // 这里focusPersonAttr因为没有personId会把projectHousePersonRelVo中的personId覆盖掉变成了没有personId
        BeanUtil.copyProperties(focusPersonAttr, projectHousePersonRelVo);
        BeanUtils.copyProperties(projectPersonInfo, projectHousePersonRelVo);
        BeanUtils.copyProperties(projectHousePersonRel, projectHousePersonRelVo);
        String focusCategory = focusPersonAttr.getFocusCategory();
        if (StrUtil.isNotBlank(focusCategory)) {
            String[] focusCategoryArr = focusCategory.split(",");
            projectHousePersonRelVo.setFocusCategoryArr(focusCategoryArr);
        }

        //获取用户拓展属性
        List<ProjectPersonAttrListVo> projectPersonAttrListVos = projectPersonAttrService
                .getPersonAttrListVo(ProjectContextHolder.getProjectId(), PersonTypeEnum.PROPRIETOR.code, id);
        projectHousePersonRelVo.setProjectPersonAttrList(projectPersonAttrListVos);
        projectHousePersonRelVo.setCredentialPicBack(projectHousePersonRel.getCredentialPicBack());
        projectHousePersonRelVo.setCredentialPicFront(projectHousePersonRel.getCredentialPicFront());
        return projectHousePersonRelVo;
    }


    @Override
    public boolean checkPersonExits(String personId, String houseId) {
        int num = this.count(new QueryWrapper<ProjectHousePersonRel>().lambda()
                .eq(ProjectHousePersonRel::getPersonId, personId).eq(ProjectHousePersonRel::getHouseId, houseId));
        return num != 0;
    }

    /**
     * 根据住户ID获取所在的房屋
     *
     * @param personId
     * @return
     */
    @Override
    public List<ProjectHouseDTO> listHouseByPersonId(String personId) {
//        ProjectHouseDTO projectHouseDTO;
//        List<ProjectHouseDTO> dtolist = new ArrayList<>();
//        ProjectFrameInfo houseInfo;
//        ProjectFrameInfo unitInfo;
//        ProjectFrameInfo buildingInfo;
//
//        List<ProjectHousePersonRel> housePersonRelsList = this.list(new QueryWrapper<ProjectHousePersonRel>().lambda().eq(ProjectHousePersonRel::getPersonId, personId));
//
//        for (ProjectHousePersonRel housePersonRel : housePersonRelsList) {
//            projectHouseDTO = new ProjectHouseDTO();
//
//            projectHouseDTO.setHouseId(housePersonRel.getHouseId());
//            houseInfo = projectFrameInfoService.getById(housePersonRel.getHouseId());
//            unitInfo = projectFrameInfoService.getById(houseInfo.getPuid());
////            buildingInfo =  projectFrameInfoService.getById(unitInfo.getPuid());
//
//            projectHouseDTO.setUnitId(houseInfo.getPuid());
//            projectHouseDTO.setBuildingId(unitInfo.getPuid());
//
//            dtolist.add(projectHouseDTO);
//        }
        return baseMapper.listHouseByPersonId(personId, ProjectContextHolder.getProjectId());
    }

    /**
     * 根据第三方编号获取人屋关系
     *
     * @param relaCode
     * @return
     */
    @Override
    public ProjectHousePersonRel getByRelaCode(String relaCode) {
        List<ProjectHousePersonRel> housePersonRelList = this.list(new QueryWrapper<ProjectHousePersonRel>().lambda().eq(ProjectHousePersonRel::getRelaCode, relaCode));
        if (CollectionUtil.isNotEmpty(housePersonRelList)) {
            return housePersonRelList.get(0);
        }
        return null;
    }

    /**
     * 检查一个房间内是否存在业主
     *
     * @param houseId
     * @return
     */
    @Override
    public boolean haveOwner(String houseId) {
        int count = count(new QueryWrapper<ProjectHousePersonRel>().lambda()
                .eq(ProjectHousePersonRel::getHouseId, houseId)
                .eq(ProjectHousePersonRel::getHouseholdType, HouseHoldTypeEnum.OWNER.code)
                .eq(ProjectHousePersonRel::getAuditStatus, AuditStatusEnum.pass.code)
        );
        return count >= 1;
    }

    /**
     * <p>
     * 对重点人员信息进行更新或删除或添加操作
     * </p>
     *
     * @param housePersonRelVo 人物关系vo对象
     * @author: 王良俊
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void operateFocusAttr(ProjectHousePersonRelVo housePersonRelVo) {
        if (IsFocusPersonConstants.YES.equals(housePersonRelVo.getIsFocusPerson())) {
            String[] focusCategoryArr = housePersonRelVo.getFocusCategoryArr();
            String focusCategory = "";
            if (ArrayUtil.isNotEmpty(focusCategoryArr)) {
                focusCategory = focusCategoryArr.toString();
                focusCategory = focusCategory.substring(1, focusCategory.length() - 1);
            }
            ProjectFocusPersonAttr focusPersonAttr = new ProjectFocusPersonAttr();
            BeanUtil.copyProperties(housePersonRelVo, focusPersonAttr);
            focusPersonAttr.setFocusCategory(focusCategory);
            projectFocusPersonAttrService.saveOrUpdateFocusPersonAttrByPersonId(focusPersonAttr);
        } else {
            if (StrUtil.isNotBlank(housePersonRelVo.getPersonId())) {
                projectFocusPersonAttrService.removeFocusPersonAttrByPersonId(housePersonRelVo.getPersonId());
            }
        }
    }

    /**
     * <p>
     * 对重点人员信息进行更新或删除或添加操作（批量操作）
     * </p>
     *
     * @param housePersonRelVoList 人物关系vo对象列表
     * @author: 王良俊
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void operateFocusAttr(List<ProjectHousePersonRelVo> housePersonRelVoList) {
        List<ProjectFocusPersonAttr> focusPersonAttrList = new ArrayList<>();
        List<String> removeFocusPersonIdList = new ArrayList<>();
        for (ProjectHousePersonRelVo housePersonRelVo : housePersonRelVoList) {
            if (IsFocusPersonConstants.YES.equals(housePersonRelVo.getIsFocusPerson())) {
                String[] focusCategoryArr = housePersonRelVo.getFocusCategoryArr();
                String focusCategory = "";
                if (ArrayUtil.isNotEmpty(focusCategoryArr)) {
                    focusCategory = focusCategoryArr.toString();
                    focusCategory = focusCategory.substring(1, focusCategory.length() - 1);
                }
                ProjectFocusPersonAttr focusPersonAttr = new ProjectFocusPersonAttr();
                BeanUtil.copyProperties(housePersonRelVo, focusPersonAttr);
                focusPersonAttr.setFocusCategory(focusCategory);
                focusPersonAttr.setPersonId(housePersonRelVo.getPersonId());
                focusPersonAttrList.add(focusPersonAttr);
            } else {
                if (StrUtil.isNotBlank(housePersonRelVo.getPersonId())) {
                    removeFocusPersonIdList.add(housePersonRelVo.getPersonId());
                }
            }
        }
        // 这里要改成批量操作的方法
        projectFocusPersonAttrService.saveOrUpdateFocusPersonAttrByPersonId(focusPersonAttrList);
        projectFocusPersonAttrService.removeFocusPersonAttrByPersonId(removeFocusPersonIdList);

    }


    @Override
    public List<ProjectHousePersonRelRecordVo> listByHouseId(String id) {
        return baseMapper.findPageByHouseId(id, ProjectContextHolder.getProjectId());
    }

    @Override
    public Page<ProjectHousePersonRelRecordVo> findPageById(Page page, String personId) {
        return baseMapper.findPageById(page, personId, ProjectContextHolder.getProjectId());
    }

    @Override
    public Page<ProjectHousePersonRelRecordVo> filterPageById(Page page, String personId, String status) {
        return baseMapper.filterPageById(page, personId, ProjectContextHolder.getProjectId(), status);
    }

    @Override
    public ProjectHousePersonRel findByPersonIdAndHouseId(String personId, String houseId) {
        return getOne(Wrappers.lambdaQuery(ProjectHousePersonRel.class)
                .eq(ProjectHousePersonRel::getPersonId, personId)
                .eq(ProjectHousePersonRel::getHouseId, houseId)
                .eq(ProjectHousePersonRel::getAuditStatus, AuditStatusEnum.pass.code));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R requestAgain(ProjectHousePersonRelRequestAgainVo projectHousePersonRel) {

        ProjectHousePersonRel thisProjectHousePersonRel = getById(projectHousePersonRel.getRelaId());
        if (ObjectUtil.isNotEmpty(projectHousePersonRel.getRentStartTime())) {
            thisProjectHousePersonRel.setRentStartTime(projectHousePersonRel.getRentStartTime());
        }
        if (ObjectUtil.isNotEmpty(projectHousePersonRel.getRentStopTime())) {
            thisProjectHousePersonRel.setRentStopTime(projectHousePersonRel.getRentStopTime());
        }
        //重新申请重置审核状态
        thisProjectHousePersonRel.setAuditStatus(AuditStatusEnum.inAudit.code);
        thisProjectHousePersonRel.setAuditReason("");
        updateById(thisProjectHousePersonRel);
        ProjectPersonInfo personInfo = projectPersonInfoService.getById(thisProjectHousePersonRel.getPersonId());
        personInfo.setCredentialPicFront(projectHousePersonRel.getCredentialPicFront());
        personInfo.setCredentialPicBack(projectHousePersonRel.getCredentialPicBack());
        personInfo.setPersonName(projectHousePersonRel.getPersonName());
        projectPersonInfoService.updateById(personInfo);

        return R.ok();
    }




    @Override
    public ProjectHouseParkPlaceInfoVo getInfo(String id) {
        //获取房屋人员关系表
        ProjectHousePersonRel projectHousePersonRel = baseMapper.selectById(id);
        //获取人员信息
        ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getById(projectHousePersonRel.getPersonId());
        //设置人员信息
        ProjectHouseParkPlaceInfoVo parkPlaceInfoVo = new ProjectHouseParkPlaceInfoVo();
        parkPlaceInfoVo.setPersonId(projectPersonInfo.getPersonId());
        parkPlaceInfoVo.setGender(projectPersonInfo.getGender());
        parkPlaceInfoVo.setPersonName(projectPersonInfo.getPersonName());
        parkPlaceInfoVo.setTelephone(projectPersonInfo.getTelephone());
        parkPlaceInfoVo.setPeopleTypeCode(projectPersonInfo.getPeopleTypeCode());
        parkPlaceInfoVo.setHouseholdType(projectHousePersonRel.getHouseholdType());

        //获取关联的停车场信息
        List<ProjectParkingPlace> parkingPlaces = projectParkingPlaceService.getByPersonId(projectHousePersonRel.getPersonId());
        //存在可能没有关联停车场的情况 故这里做一层非空判断
        if (ObjectUtil.isNotEmpty(parkingPlaces) && parkingPlaces.size() > 0) {
            //获取停车场id列表
            List<String> placeIds = parkingPlaces.stream().map(e -> e.getPlaceId()).collect(Collectors.toList());
            //获取与停车场关联的车辆信息
            List<ProjectParCarRegister> parCarRegisters = projectParCarRegisterService.list(
                    Wrappers.lambdaQuery(ProjectParCarRegister.class).in(ProjectParCarRegister::getParkPlaceId, placeIds));
            parkPlaceInfoVo.setParkingPlaces(parkingPlaces);
            parkPlaceInfoVo.setParCarRegisters(parCarRegisters);
        }

        return parkPlaceInfoVo;
    }

    /**
     * 发送住户审核消息
     */
    private void sendAssignNotice(ProjectHousePersonRelRequestVo projectHousePersonRelRequestVo) {
        try {
            Integer projectId = ProjectContextHolder.getProjectId();
            List<String> staffId = baseMapper.getProjectStaff(userVerifyMenuId, projectId);
            ProjectHouseAddressVo projectHouseAddressVo = baseMapper.getAddress(projectHousePersonRelRequestVo.getHouseId());
            //处理房屋地址的方法
            List<String> address = getAddress(projectHouseAddressVo.getAddress());
            String personName = StrUtil.isEmpty(projectHousePersonRelRequestVo.getPersonName()) ? "" : projectHousePersonRelRequestVo.getPersonName();
            String telephone = StrUtil.isEmpty(projectHousePersonRelRequestVo.getTelephone()) ? "" : projectHousePersonRelRequestVo.getTelephone();
            if (CollectionUtils.isNotEmpty(staffId)) {
                noticeUtil.send(true, "入住审核通知",
                        "有新的住户入住申请，请尽快审核" + "<br/>" +
                                "住户姓名：" + personName  + "<br/>" +
                                "住户手机号：" + telephone  + "<br/>" +
                                "房屋地址：" + address.get(0)  + "<br/>" +
                                "房间：" + address.get(1), staffId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("消息发送异常");
        }
    }

    /**
     * 处理房屋地址的方法
     *
     * @param oldAddress
     * @return
     */
    private List<String> getAddress(String oldAddress) {
        int one = oldAddress.lastIndexOf("-");
        int two = oldAddress.lastIndexOf("-", one - 1);
        int three = oldAddress.lastIndexOf("-", two - 1);

        int length = oldAddress.length() - (oldAddress.replaceAll("-","")).length();

        String newAddress;
        StringBuilder sb = new StringBuilder(oldAddress);
        if(length >= 3){
            newAddress = sb.replace(one, one + 1, "").replace(two, two + 1, "").replace(three, three + 1, " ").toString();
        }else{
            newAddress = sb.replace(one, one + 1, "").replace(two, two + 1, "").toString();
        }
        Integer projectId = ProjectContextHolder.getProjectId();
        String projectName = baseMapper.getProjectName(projectId);
        String address = projectName + " " + newAddress;
        String house = oldAddress.substring(one + 1);
        List<String> list = new ArrayList<>();
        list.add(address);
        list.add(house);
        return list;
    }
}
