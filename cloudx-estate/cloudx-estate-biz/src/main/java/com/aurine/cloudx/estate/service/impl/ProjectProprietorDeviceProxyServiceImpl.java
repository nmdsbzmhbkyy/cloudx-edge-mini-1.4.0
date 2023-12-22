package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.dto.ProjectPersonDeviceDTO;
import com.aurine.cloudx.estate.dto.ProjectPersonLiftDTO;
import com.aurine.cloudx.estate.entity.ProjectPersonDevice;
import com.aurine.cloudx.estate.entity.ProjectPersonLiftPlanRel;
import com.aurine.cloudx.estate.entity.ProjectPersonLiftRel;
import com.aurine.cloudx.estate.entity.ProjectPersonPlanRel;
import com.aurine.cloudx.estate.mapper.ProjectPersonDeviceMapper;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.service.adapter.AbstractProjectPersonDeviceService;
import com.aurine.cloudx.estate.util.delay.TaskUtil;
import com.aurine.cloudx.estate.util.delay.constants.DelayTaskTopicEnum;
import com.aurine.cloudx.estate.util.delay.entity.PersonDelayTask;
import com.aurine.cloudx.estate.vo.ProjectDeviceLiftVo;
import com.aurine.cloudx.estate.vo.ProjectProprietorDeviceRecordVo;
import com.aurine.cloudx.estate.vo.ProjectProprietorDeviceSearchCondition;
import com.aurine.cloudx.estate.vo.ProjectProprietorDeviceVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>住户设备权限代理实现类</p>
 *
 * @ClassName: ProjectPersonDeviceServiceImpl
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/22 8:40
 * @Copyright:
 */
@Service
public class ProjectProprietorDeviceProxyServiceImpl extends ServiceImpl<ProjectPersonDeviceMapper, ProjectPersonDevice> implements ProjectProprietorDeviceProxyService {
//    @Resource
//    private WebProjectPersonDeviceService projectPersonDeviceService;
    @Resource
    private AbstractProjectPersonDeviceService abstractWebProjectPersonDeviceService;

    @Resource
    private ProjectPersonPlanRelService projectPersonPlanRelService;

    @Resource
    private ProjectRightDeviceService projectRightDeviceService;

    @Resource
    private ProjectEntityLevelCfgService projectEntityLevelCfgService;

    @Resource
    private TaskUtil taskUtil;

    @Resource
    private ProjectPersonLiftPlanRelService projectPersonLiftPlanRelService;

    @Resource
    private ProjectPersonLiftRelService projectPersonLiftRelService;




    /**
     * 保存住户权限配置
     *
     * @param projectProprietorDeviceVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(ProjectProprietorDeviceVo projectProprietorDeviceVo) {

        //保存住户电梯权限配置
        //saveWithLift(projectProprietorDeviceVo);
        /**获取住户电梯设备下的乘梯识别终端列表，
         * 凭证下发逻辑合并至原梯区口下发逻辑中，
         * 将设备集合传入ProjectPersonDeviceDTO
         */
        //List<String> liftChildDevice = projectPersonLiftRelService.childDeviceIdByLift(projectProprietorDeviceVo.getPersonId());


        //删除老旧方案
        //projectPersonPlanRelService.deleteByPersonId(projectProprietorDeviceVo.getPersonId());
        ProjectPersonPlanRel personPlanRel = projectPersonPlanRelService.getPoByPersonId(projectProprietorDeviceVo.getPersonId());
//        if(personPlanRel != null){
//            projectPersonPlanRelService.removeById(personPlanRel);
//        }

        //保存该用户使用的方案
        ProjectPersonPlanRel projectPersonPlanRel = new ProjectPersonPlanRel();
        if(personPlanRel != null){
            projectPersonPlanRel.setSeq(personPlanRel.getSeq());
        }
        projectPersonPlanRel.setPersonType(PersonTypeEnum.PROPRIETOR.code);
        BeanUtils.copyProperties(projectProprietorDeviceVo, projectPersonPlanRel);

        if (projectPersonPlanRel.getExpTime() == null) {
            projectPersonPlanRel.setExpTime(DateUtil.toLocalDateTime(DateUtil.parse("2199-01-01", "yyyy-MM-dd")));
        } else if (TaskUtil.isToday(projectPersonPlanRel.getExpTime())){
            taskUtil.addDelayTask(new PersonDelayTask(ProjectContextHolder.getProjectId(), projectPersonPlanRel.getPersonId(),
                    projectPersonPlanRel.getExpTime(), PersonTypeEnum.PROPRIETOR, DelayTaskTopicEnum.householderExp));
            // 如果是今天过期则添加到延时任务中
//            DelayTaskUtil.instance().addDelayTask(new DelayTask(projectPersonPlanRel.getExpTime(),
//                    ProjectContextHolder.getProjectId(), DelayTaskTopicEnum.visitorSendCert));
        }

        projectPersonPlanRelService.saveOrUpdate(projectPersonPlanRel);

        //获取设备id列表
        /*String[] deviceIdArray = projectProprietorDeviceVo.getDeviceIdArray();
        List<String> deviceIdList = new ArrayList<>();
        if (ArrayUtil.isNotEmpty(deviceIdArray)) {
            Collections.addAll(deviceIdList, deviceIdArray);
        }*/
//        projectRightDeviceService.authPersonCertmdiaDevice(deviceIdList, projectProprietorDeviceVo.getPersonId());
        //保存该用户可用的通行设备
        ProjectPersonDeviceDTO projectPersonDeviceDTO = new ProjectPersonDeviceDTO();
        BeanUtils.copyProperties(projectProprietorDeviceVo, projectPersonDeviceDTO);
        //电梯乘梯识别终端
        //projectPersonDeviceDTO.setLiftChildDevice(liftChildDevice);
        //电梯集合
        List<String> liftIdList;
        if(projectProprietorDeviceVo.getLifts() != null){
            liftIdList = projectProprietorDeviceVo.getLifts().stream().map(ProjectDeviceLiftVo::getDeviceId).collect(Collectors.toList());
            projectPersonDeviceDTO.setLiftIdList(liftIdList);
        }

//        projectPersonDeviceService.savePersonDevice(projectPersonDeviceDTO);
        abstractWebProjectPersonDeviceService.savePersonDevice(projectPersonDeviceDTO);
        if (TaskUtil.isToday(projectPersonDeviceDTO.getExpTime())) {
            taskUtil.addDelayTask(new PersonDelayTask(ProjectContextHolder.getProjectId(), projectPersonDeviceDTO.getPersonId(),
                    projectPersonDeviceDTO.getExpTime(), PersonTypeEnum.PROPRIETOR, DelayTaskTopicEnum.householderExp));
            /*DelayTaskUtil.instance().addDelayTask(new DelayTask(projectPersonDeviceDTO.getExpTime(),
                    ProjectContextHolder.getProjectId(), DelayTaskTopicEnum.expHouseholder));*/
        }

        return true;
    }

    /**
     * 保存住户权限配置
     *
     * @param projectProprietorDeviceVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveWithLift(ProjectProprietorDeviceVo projectProprietorDeviceVo) {
        //屏蔽电梯方案相关代码
        if(StringUtils.isBlank(projectProprietorDeviceVo.getLiftPlanId())){
            return true;
        }

        //删除老旧方案
        projectPersonLiftPlanRelService.deleteByPersonId(projectProprietorDeviceVo.getPersonId());

        //保存该用户使用的方案
        ProjectPersonLiftPlanRel projectPersonLiftPlanRel = new ProjectPersonLiftPlanRel();
        projectPersonLiftPlanRel.setPersonType(PersonTypeEnum.PROPRIETOR.code);
        BeanUtils.copyProperties(projectProprietorDeviceVo, projectPersonLiftPlanRel);

        if (projectPersonLiftPlanRel.getExpTime() == null) {
            projectPersonLiftPlanRel.setExpTime(DateUtil.toLocalDateTime(DateUtil.parse("2199-01-01", "yyyy-MM-dd")));
        } else if (TaskUtil.isToday(projectPersonLiftPlanRel.getExpTime())){
            //延迟任务暂时屏蔽
            /*taskUtil.addDelayTask(new PersonDelayTask(ProjectContextHolder.getProjectId(), projectPersonLiftPlanRel.getPersonId(),
                    projectPersonLiftPlanRel.getExpTime(), PersonTypeEnum.PROPRIETOR, DelayTaskTopicEnum.householderExp));*/
            // 如果是今天过期则添加到延时任务中
//            DelayTaskUtil.instance().addDelayTask(new DelayTask(projectPersonPlanRel.getExpTime(),
//                    ProjectContextHolder.getProjectId(), DelayTaskTopicEnum.visitorSendCert));
        }

        projectPersonLiftPlanRelService.save(projectPersonLiftPlanRel);

        List<ProjectDeviceLiftVo> liftVos = projectProprietorDeviceVo.getLifts();

        //人员电梯关系保存
        ProjectPersonLiftDTO projectPersonLiftDTO = new ProjectPersonLiftDTO();
        projectPersonLiftDTO.setPersonType(PersonTypeEnum.PROPRIETOR.code);
        BeanUtils.copyProperties(projectProprietorDeviceVo, projectPersonLiftDTO);
        projectPersonLiftRelService.savePersonDevice(projectPersonLiftDTO);

        return true;
    }

//    /**
//     * 根据住户ID保存住户权限配置,用于住户通行凭证的重置
//     *
//     * @param personId
//     * @return
//     */
//    @Override
//    public boolean saveByPersonId(String personId) {
//        ProjectProprietorDeviceVo vo = this.getVo(personId);
//        if(vo == null || StringUtils.isEmpty(vo.getPlanId())){
//            return true;
//        }
//        return this.save(vo);
//    }


    /**
     * 获取住户对应设备的权限配置
     *
     * @param personId
     * @return
     */
    @Override
    public ProjectProprietorDeviceVo getVo(String personId) {
        // 通过人员设备表获取人员类型，另外获取该人员使用的通行模板/方案(每个人只有一个通行方案)的基本信息(如方案id、生效时间、是否启用等)
        ProjectPersonDeviceDTO dto = abstractWebProjectPersonDeviceService.getDTOByPersonId(personId);
        ProjectProprietorDeviceVo vo = new ProjectProprietorDeviceVo();
        BeanUtils.copyProperties(dto, vo);
        return vo;
    }

    /**
     * 获取住户电梯权限配置
     *
     * @param personId
     * @return
     */
    @Override
    public List<ProjectPersonLiftRel> getPersonLift(String personId) {
        return projectPersonLiftRelService.listByPersonId(personId);
    }


    /**
     * 关闭通行权限
     *
     * @param personId 人员ID
     * @return
     */
    @Override
    public boolean disablePassRight(String personId) {
        projectPersonLiftRelService.disablePassRight(PersonTypeEnum.PROPRIETOR.code, personId);
        return abstractWebProjectPersonDeviceService.disablePassRight(PersonTypeEnum.PROPRIETOR.code, personId);
    }

    /**
     * 开启通行权限
     *
     * @param personId
     * @return
     */
    @Override
    public boolean enablePassRight(String personId) {
        projectPersonLiftRelService.enablePassRight(PersonTypeEnum.PROPRIETOR.code, personId);
        return abstractWebProjectPersonDeviceService.enablePassRight(PersonTypeEnum.PROPRIETOR.code, personId);
    }

    /**
     * 住户门禁权限查询
     *
     * @param page
     * @param searchCondition
     * @return
     */
    @Override
    public IPage<ProjectProprietorDeviceRecordVo> findPage(IPage<ProjectProprietorDeviceRecordVo> page, ProjectProprietorDeviceSearchCondition searchCondition) {

       /*
        //根据查询条件，分发查询
        boolean buildingNone = false;
        boolean parkingNone = false;

        if (StringUtils.isEmpty(searchCondition.getParkName()) && StringUtils.isEmpty(searchCondition.getParkRegionName()) && StringUtils.isEmpty(searchCondition.getParkId())) {
            parkingNone = true;
        }

        if (StringUtils.isEmpty(searchCondition.getBuildingName()) && StringUtils.isEmpty(searchCondition.getUnitName()) && StringUtils.isEmpty(searchCondition.getHouseName())) {
            buildingNone = true;
        }

        // 获取房屋地址组合
        String groupAddressHouse =
                (StrUtil.isNotBlank(searchCondition.getBuildingName()) ? searchCondition.getBuildingName() + "-" : "-") +
                (StrUtil.isNotBlank(searchCondition.getUnitName()) ? searchCondition.getUnitName() + "-": "-") +
                (StrUtil.isNotBlank(searchCondition.getHouseName()) ? searchCondition.getHouseName() : "");
        String groupAddressPark =
                (StrUtil.isNotBlank(searchCondition.getParkName()) ? searchCondition.getParkName() + "-" : "-") +
                (StrUtil.isNotBlank(searchCondition.getParkRegionName()) ? searchCondition.getParkRegionName() + "-" : "-") +
                (StrUtil.isNotBlank( searchCondition.getPlaceName()) ? searchCondition.getPlaceName() : "");
        //只查询楼栋+姓名+状态
        if (parkingNone && !buildingNone) {
            return this.baseMapper.findProprietorDeviceHousePage(page, searchCondition, groupAddressHouse, ProjectContextHolder.getProjectId());
            //只查询车位+姓名+状态
        } else if (!parkingNone && buildingNone) {
            return this.baseMapper.findProprietorDeviceParkingPage(page, searchCondition, groupAddressPark, ProjectContextHolder.getProjectId());
            //其他情况（同时查询或者都不查询楼栋和车位）
        } else {
            return this.baseMapper.findProprietorDevicePage(page, searchCondition, groupAddressPark, groupAddressHouse, ProjectContextHolder.getProjectId());
        }
        */
        searchCondition.setHouseSearch(false);
        searchCondition.setParkSearch(false);
        if (StrUtil.isNotBlank(searchCondition.getHouseName()) || StrUtil.isNotBlank(searchCondition.getUnitName()) || StrUtil.isNotBlank(searchCondition.getBuildingName())) {
            searchCondition.setHouseSearch(true);
        }
        if (StrUtil.isNotBlank(searchCondition.getParkName()) || StrUtil.isNotBlank(searchCondition.getParkRegionName()) || StrUtil.isNotBlank(searchCondition.getPlaceName())) {
            searchCondition.setParkSearch(true);
        }
        IPage<ProjectProprietorDeviceRecordVo> projectProprietorDeviceRecordVoIPage = this.baseMapper.fetchList(page,
                searchCondition, ProjectContextHolder.getProjectId(), projectEntityLevelCfgService.checkIsEnabled());
        return projectProprietorDeviceRecordVoIPage;


    }
}
