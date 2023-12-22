
package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.HouseParkingHistoryActionEnum;
import com.aurine.cloudx.estate.constant.enums.PersonAttrTypeEnum;
import com.aurine.cloudx.estate.constant.enums.PlaceRelTypeEnum;
import com.aurine.cloudx.estate.entity.ProjectFrameInfo;
import com.aurine.cloudx.estate.entity.ProjectParkingPlace;
import com.aurine.cloudx.estate.entity.ProjectParkingPlaceHis;
import com.aurine.cloudx.estate.entity.ProjectPersonInfo;
import com.aurine.cloudx.estate.entity.ProjectPersonLabel;
import com.aurine.cloudx.estate.mapper.ProjectParkingPlaceMapper;
import com.aurine.cloudx.estate.service.ProjectFrameInfoService;
import com.aurine.cloudx.estate.service.ProjectParkingPlaceHisService;
import com.aurine.cloudx.estate.service.ProjectParkingPlaceManageService;
import com.aurine.cloudx.estate.service.ProjectParkingPlaceService;
import com.aurine.cloudx.estate.service.ProjectPersonAttrService;
import com.aurine.cloudx.estate.service.ProjectPersonInfoService;
import com.aurine.cloudx.estate.service.ProjectPersonLabelService;
import com.aurine.cloudx.estate.vo.ProjectHousePersonRelVo;
import com.aurine.cloudx.estate.vo.ProjectParkingPlaceManageRecordVo;
import com.aurine.cloudx.estate.vo.ProjectParkingPlaceManageSearchConditionVo;
import com.aurine.cloudx.estate.vo.ProjectParkingPlaceManageVo;
import com.aurine.cloudx.estate.vo.ProjectPersonAttrFormVo;
import com.aurine.cloudx.estate.vo.ProjectPersonAttrListVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 车位
 *
 * @author 王伟
 * @date 2020-05-08 10:24:42
 */
@Service
public class ProjectParkingPlaceManageServiceImpl extends ServiceImpl<ProjectParkingPlaceMapper, ProjectParkingPlace> implements ProjectParkingPlaceManageService {
    @Resource
    private ProjectPersonInfoService projectPersonInfoService;
    @Resource
    private ProjectFrameInfoService projectFrameInfoService;
    @Resource
    private ProjectParkingPlaceMapper projectParkingPlaceMapper;
    @Resource
    private ProjectParkingPlaceHisService projectParkingPlaceHisService;
    @Resource
    private ProjectParkingPlaceService projectParkingPlaceService;
    @Resource
    private ProjectPersonLabelService projectPersonLabelService;
    @Resource
    private ProjectPersonAttrService projectPersonAttrService;

    /**
     * 查询住户
     *
     * @param page
     * @param searchConditionVo 查询条件
     * @return
     */
    @Override
    public IPage<ProjectParkingPlaceManageRecordVo> findPage(IPage<ProjectParkingPlaceManageRecordVo> page, ProjectParkingPlaceManageSearchConditionVo searchConditionVo) {

        return projectParkingPlaceMapper.selectParkingPlaceManage(page, searchConditionVo);
    }

    /**
     * 迁入车主
     *
     * @param projectParkingPlaceManageVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String save(ProjectParkingPlaceManageVo projectParkingPlaceManageVo) {

        //如果当前车位已经被占用，提示异常
        boolean havPersonInPlace = this.checkPersonExits(projectParkingPlaceManageVo.getPlaceId());

        if (havPersonInPlace) {
            throw new RuntimeException("当前车位已被使用");
        }

        //检查人员是否存在，如果不存在则添加人员
        ProjectPersonInfo projectPersonInfo = new ProjectPersonInfo();
        ProjectPersonInfo personInfo = projectPersonInfoService.getByTelephone(projectParkingPlaceManageVo.getTelephone());

        String personId = "";

        if (personInfo == null) {
            // 人员不存在,则新增人员
            personId = UUID.randomUUID().toString().replaceAll("-", "");
            BeanUtils.copyProperties(projectParkingPlaceManageVo, projectPersonInfo);
            projectPersonInfo.setPersonId(personId);
            projectPersonInfoService.saveFromSystem(projectPersonInfo);
        } else {
            personId = personInfo.getPersonId();
            BeanUtils.copyProperties(projectParkingPlaceManageVo, personInfo);
            projectPersonInfoService.updateById(personInfo);
        }

        //存储人员标签
        projectPersonLabelService.addLabel(projectParkingPlaceManageVo.getTagArray(), personId);

        //通过车位id获取到车位归属信息，并将人员关系信息更新入当前车位归属信息中
        ProjectParkingPlace projectParkingPlace = projectParkingPlaceService.getById(projectParkingPlaceManageVo.getPlaceId());
        BeanUtils.copyProperties(projectParkingPlaceManageVo, projectParkingPlace);
        projectParkingPlace.setPersonId(personId);


        //记录历史数据
        ProjectParkingPlaceHis projectParkingPlaceHis = new ProjectParkingPlaceHis();
        BeanUtils.copyProperties(projectParkingPlaceManageVo, projectParkingPlaceHis);
        projectParkingPlaceHis.setAction(HouseParkingHistoryActionEnum.IN.code);
        projectParkingPlaceHis.setPersonId(personId);
        projectParkingPlaceHis.setRelType(projectParkingPlaceManageVo.getRelType());
        projectParkingPlaceHisService.save(projectParkingPlaceHis);

        this.updateById(projectParkingPlace);

        /**
         * 新增扩展属性
         */
        ProjectPersonAttrFormVo projectPersonAttrFormVo = new ProjectPersonAttrFormVo();
        projectPersonAttrFormVo.setProjectPersonAttrList(projectParkingPlaceManageVo.getProjectPersonAttrList());
        projectPersonAttrFormVo.setPersonId(personId);
        projectPersonAttrFormVo.setProjectId(ProjectContextHolder.getProjectId());
        projectPersonAttrFormVo.setType(PersonAttrTypeEnum.PLACE_MANAGE.code);
        projectPersonAttrService.updatePersonAttrList(projectPersonAttrFormVo);

        return personId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveBatch(List<ProjectParkingPlaceManageVo> parkingPlaceManageVoList) {
        List<ProjectParkingPlace> parkingPlaceList = new ArrayList<>();
        parkingPlaceManageVoList.forEach(projectParkingPlaceManageVo -> {
            //如果当前车位已经被占用，提示异常
            boolean havPersonInPlace = this.checkPersonExits(projectParkingPlaceManageVo.getPlaceId());

            if (havPersonInPlace) {
                throw new RuntimeException("当前车位已被使用");
            }

            //检查人员是否存在，如果不存在则添加人员
            ProjectPersonInfo projectPersonInfo = new ProjectPersonInfo();
            ProjectPersonInfo personInfo = projectPersonInfoService.getByTelephone(projectParkingPlaceManageVo.getTelephone());

            String personId = "";

            if (personInfo == null) {
                // 人员不存在,则新增人员
                personId = UUID.randomUUID().toString().replaceAll("-", "");
                BeanUtils.copyProperties(projectParkingPlaceManageVo, projectPersonInfo);
                projectPersonInfo.setPersonId(personId);
                projectPersonInfoService.saveFromSystem(projectPersonInfo);
            } else {
                personId = personInfo.getPersonId();
                // 这里是把已存在的人员信息覆盖Excel中的人员信息
                BeanUtils.copyProperties(personInfo, projectParkingPlaceManageVo);
            }

            //存储人员标签
            projectPersonLabelService.addLabel(projectParkingPlaceManageVo.getTagArray(), personId);

            //通过车位id获取到车位归属信息，并将人员关系信息更新入当前车位归属信息中
            ProjectParkingPlace projectParkingPlace = projectParkingPlaceService.getById(projectParkingPlaceManageVo.getPlaceId());
            BeanUtils.copyProperties(projectParkingPlaceManageVo, projectParkingPlace);
            projectParkingPlace.setPersonId(personId);

            //记录历史数据
            ProjectParkingPlaceHis projectParkingPlaceHis = new ProjectParkingPlaceHis();
            BeanUtils.copyProperties(projectParkingPlaceManageVo, projectParkingPlaceHis);
            projectParkingPlaceHis.setAction(HouseParkingHistoryActionEnum.IN.code);
            projectParkingPlaceHis.setPersonId(personId);
            projectParkingPlaceHis.setRelType(projectParkingPlaceManageVo.getRelType());
            projectParkingPlaceHisService.save(projectParkingPlaceHis);
            parkingPlaceList.add(projectParkingPlace);
        });
        return this.updateBatchById(parkingPlaceList);
    }

    /**
     * 更新车主信息
     *
     * @param projectParkingPlaceManageVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(ProjectParkingPlaceManageVo projectParkingPlaceManageVo) {

        //修改车位归属信息
        ProjectParkingPlace projectParkingPlace = new ProjectParkingPlace();

        //获取原车位对象
        projectParkingPlace = projectParkingPlaceService.getById(projectParkingPlaceManageVo.getPlaceId());
        projectParkingPlace.setPersonId(projectParkingPlaceManageVo.getPersonId());
        projectParkingPlace.setPersonName(projectParkingPlaceManageVo.getPersonName());
        projectParkingPlace.setHouseId(projectParkingPlaceManageVo.getHouseId());
        projectParkingPlace.setEffTime(projectParkingPlaceManageVo.getEffTime());
        projectParkingPlace.setExpTime(projectParkingPlaceManageVo.getExpTime());
        projectParkingPlace.setCheckInTime(projectParkingPlaceManageVo.getCheckInTime());
        projectParkingPlace.setWtdlrxm(projectParkingPlaceManageVo.getWtdlrxm());
        projectParkingPlace.setDlrlxdh(projectParkingPlaceManageVo.getDlrlxdh());
        projectParkingPlace.setDlrzjlx(projectParkingPlaceManageVo.getDlrzjlx());
        projectParkingPlace.setDlrzjhm(projectParkingPlaceManageVo.getDlrzjhm());

        projectParkingPlace.setRelType(projectParkingPlaceManageVo.getRelType());

        //修改人员信息
        ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getById(projectParkingPlace.getPersonId());
        BeanUtils.copyProperties(projectParkingPlaceManageVo, projectPersonInfo);
        projectPersonInfoService.updateById(projectPersonInfo);

        //存储人员标签
        projectPersonLabelService.addLabel(projectParkingPlaceManageVo.getTagArray(), projectParkingPlaceManageVo.getPersonId());


        /**
         * 扩展属性
         */
        //设置拓展属性
        ProjectPersonAttrFormVo projectPersonAttrFormVo = new ProjectPersonAttrFormVo();
        projectPersonAttrFormVo.setProjectPersonAttrList(projectParkingPlaceManageVo.getProjectPersonAttrList());
        projectPersonAttrFormVo.setPersonId(projectParkingPlaceManageVo.getPersonId());
        projectPersonAttrFormVo.setProjectId(ProjectContextHolder.getProjectId());
        projectPersonAttrFormVo.setType(PersonAttrTypeEnum.PLACE_MANAGE.code);
        projectPersonAttrService.updatePersonAttrList(projectPersonAttrFormVo);

        return this.updateById(projectParkingPlace);
    }


    /**
     * 获取
     *
     * @param id
     * @return
     */
    @Override
    public ProjectParkingPlaceManageVo getVoById(String id) {
        //获取车位信息
        ProjectParkingPlace projectParkingPlace = this.getById(id);

        //获取人员信息
        ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getById(projectParkingPlace.getPersonId());

        //拼装VO
        ProjectHousePersonRelVo projectHousePersonRelVo = new ProjectHousePersonRelVo();
        BeanUtils.copyProperties(projectPersonInfo, projectHousePersonRelVo);

        //转换人员标签
        List<ProjectPersonLabel> lableList = projectPersonLabelService.listByPersonId(projectParkingPlace.getPersonId());
        String[] lableArray = lableList.stream().map(ProjectPersonLabel::getLabelId).collect(Collectors.toList()).toArray(new String[lableList.size()]);
        projectHousePersonRelVo.setTagArray(lableArray);


        //拼装VO
        ProjectParkingPlaceManageVo projectParkingPlaceManageVo = new ProjectParkingPlaceManageVo();
        BeanUtils.copyProperties(projectHousePersonRelVo, projectParkingPlaceManageVo);
        BeanUtils.copyProperties(projectParkingPlace, projectParkingPlaceManageVo);

        //根据车位信息中的所属房屋，获取对应的buildingId/unitId
        ProjectFrameInfo houseInfo = projectFrameInfoService.getById(projectParkingPlace.getHouseId());
        if (BeanUtil.isNotEmpty(houseInfo)) {
            ProjectFrameInfo unitInfo = projectFrameInfoService.getById(houseInfo.getPuid());
            projectParkingPlaceManageVo.setUnitId(unitInfo.getEntityId());
            projectParkingPlaceManageVo.setBuildingId(unitInfo.getPuid());
        }

        //获取用户拓展属性
        List<ProjectPersonAttrListVo> projectPersonAttrListVos = projectPersonAttrService.getPersonAttrListVo(ProjectContextHolder.getProjectId(), PersonAttrTypeEnum.PLACE_MANAGE.code, projectPersonInfo.getPersonId());
        projectParkingPlaceManageVo.setProjectPersonAttrList(projectPersonAttrListVos);

        return projectParkingPlaceManageVo;
    }

    @Override
    public boolean checkPersonExits(String id) {
        ProjectParkingPlace projectParkingPlace = this.getById(id);
        return !StringUtils.isEmpty(projectParkingPlace.getPersonId());

    }

    @Override
    public String allocationPersonPublicParkingPlace(String parkId, String personId, String personName) {
        if (StrUtil.isBlank(parkId) || StrUtil.isBlank(personId) || StrUtil.isBlank(personName)) {
            return "";
        }
        List<ProjectParkingPlace> publicParkingPlaceList = this.list(new QueryWrapper<ProjectParkingPlace>().lambda()
                .eq(ProjectParkingPlace::getParkId, parkId).eq(ProjectParkingPlace::getPlaceName, ""));
        if (CollUtil.isNotEmpty(publicParkingPlaceList)) {
            List<ProjectParkingPlace> filterPlaceList = publicParkingPlaceList.stream()
                    .filter(projectParkingPlace -> StrUtil.isBlank(projectParkingPlace.getPersonId())).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(filterPlaceList)) {
                ProjectParkingPlace projectParkingPlace = filterPlaceList.get(0);
                projectParkingPlace.setPersonId(personId);
                projectParkingPlace.setPersonName(personName);
                this.updateById(projectParkingPlace);
                return projectParkingPlace.getPlaceId();
            }
        }
        throw new RuntimeException("无剩余公共车位可用");
    }

    @Override
    public String allocationPersonPublicParkingPlace(String parkId, String personId, String personName, String ruleId) {
        if (StrUtil.isBlank(parkId) || StrUtil.isBlank(personId) || StrUtil.isBlank(personName)) {
            return "";
        }
        List<ProjectParkingPlace> publicParkingPlaceList = this.list(new QueryWrapper<ProjectParkingPlace>().lambda()
                .eq(ProjectParkingPlace::getParkId, parkId).eq(ProjectParkingPlace::getRelType, PlaceRelTypeEnum.PUBLIC.code));
        if (CollUtil.isNotEmpty(publicParkingPlaceList)) {
            List<ProjectParkingPlace> filterPlaceList = publicParkingPlaceList.stream()
                    .filter(projectParkingPlace -> StrUtil.isBlank(projectParkingPlace.getPersonId())).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(filterPlaceList)) {
                ProjectParkingPlace projectParkingPlace = filterPlaceList.get(0);
                projectParkingPlace.setPersonId(personId);
                projectParkingPlace.setPersonName(personName);
                projectParkingPlace.setRuleId(ruleId);
                this.updateById(projectParkingPlace);
                return projectParkingPlace.getPlaceId();
            }
        }
        throw new RuntimeException("无剩余公共车位可用");
    }

    @Override
    public boolean updateRentTime(String placeId, String endTime) {
        if (StrUtil.isBlank(placeId) || StrUtil.isBlank(endTime)) {
            return false;
        }
        List<ProjectParkingPlace> placeList = this.list(new QueryWrapper<ProjectParkingPlace>()
                .lambda().eq(ProjectParkingPlace::getPlaceId, placeId)
                .eq(ProjectParkingPlace::getRelType, PlaceRelTypeEnum.RENT.code));
        if (CollUtil.isNotEmpty(placeList)) {
            ProjectParkingPlace place = placeList.get(0);
            LocalDateTime expTime = place.getExpTime();
            endTime = endTime + " 00:00:00";
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime newExpTime = LocalDateTime.parse(endTime, df);
            if (expTime.isBefore(newExpTime)) {
                ProjectParkingPlace projectParkingPlace = new ProjectParkingPlace();
                projectParkingPlace.setPlaceId(placeId);
                projectParkingPlace.setExpTime(newExpTime);
                return this.updateById(projectParkingPlace);
            } else {
                return true;
            }
        }
        return true;
    }


    @Override
    public String checkParkingPlaceIsCorrect(String parkingName, String parkRegionName, String placeName) {
        List<String> placeIdList = projectParkingPlaceMapper.listPlaceIdByAddress(parkingName + "-" + parkRegionName + "-" + placeName);
        if (CollUtil.isEmpty(placeIdList)) {
            throw new ExcelAnalysisException("请检查车场名称/车位区域/车位号是否填写错误，无法找到对应车位");
        }
        return placeIdList.get(0);
    }

}
