package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.DeviceExcelConstant;
import com.aurine.cloudx.estate.constant.enums.HouseParkingHistoryActionEnum;
import com.aurine.cloudx.estate.constant.enums.ParkingManageRelExcelEnum;
import com.aurine.cloudx.estate.constant.enums.PersonAttrTypeEnum;
import com.aurine.cloudx.estate.constant.enums.PlaceRelTypeEnum;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.excel.ProjectParkingPlaceListener;
import com.aurine.cloudx.estate.excel.parking.ParkingManageRelExcel;
import com.aurine.cloudx.estate.mapper.ProjectParkingPlaceMapper;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.thirdparty.module.parking.factory.ParkingFactoryProducer;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.api.client.util.Lists;
import com.pig4cloud.pigx.common.core.util.R;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * (WebthisImpl)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/12/8 16:36
 */
@Service
@Primary
public class ProjectParkingPlaceManageServiceImpl extends ServiceImpl<ProjectParkingPlaceMapper, ProjectParkingPlace> implements ProjectParkingPlaceManageService {


    @Resource
    private ProjectPersonInfoService projectPersonInfoService;
    @Resource
    private ProjectFrameInfoService projectFrameInfoService;
    @Resource
    private ProjectParkingPlaceHisService projectParkingPlaceHisService;
    @Resource
    private ProjectParCarRegisterService projectParCarRegisterService;
    @Resource
    private ProjectEntityLevelCfgService projectEntityLevelCfgService;
    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    private ProjectParkingPlaceMapper projectParkingPlaceMapper;
    @Resource
    private ProjectParkingPlaceService projectParkingPlaceService;
    @Resource
    private ProjectPersonLabelService projectPersonLabelService;
    @Resource
    private ProjectPersonAttrService projectPersonAttrService;
    @Resource
    private ProjectParkRegionService projectParkRegionService;

    /**
     * 迁出车主
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeParkingPlaceManageById(String id) {
        //获取车位信息
        ProjectParkingPlace projectParkingPlace = this.getById(id);

        boolean isRegister = projectParCarRegisterService.checkHasRegister(id);
        if (isRegister) {
            throw new RuntimeException("车位下已有车辆禁止迁出");
        }

        /**
         * 迁出车位，如果该车位绑定了车辆，则需要调用接口，删除车辆
         * @author: 王伟
         * @since: 2020-09-21
         *
         */
        List<ProjectParCarRegister> carRegisterList = projectParCarRegisterService.list(new QueryWrapper<ProjectParCarRegister>().lambda().eq(ProjectParCarRegister::getParkPlaceId, projectParkingPlace.getPlaceId()));
        if (CollUtil.isNotEmpty(carRegisterList)) {
            ParkingFactoryProducer.getFactory(projectParkingPlace.getParkId()).getParkingService().removeCar(carRegisterList.get(0));
        }

        //记录历史数据
        ProjectParkingPlaceHis projectParkingPlaceHis = new ProjectParkingPlaceHis();
        BeanUtils.copyProperties(projectParkingPlace, projectParkingPlaceHis);
        projectParkingPlaceHis.setAction(HouseParkingHistoryActionEnum.OUT.code);
        projectParkingPlaceHis.setPersonId(projectParkingPlace.getPersonId());
        projectParkingPlaceHis.setRelType(projectParkingPlace.getRelType());
        projectParkingPlaceHisService.save(projectParkingPlaceHis);

        //清空对应车位下的人员信息
        projectParkingPlace.setPersonId("");
        projectParkingPlace.setPersonName("");
//        projectParkingPlace.setRelType("0");
        projectParkingPlace.setEffTime(null);
        projectParkingPlace.setExpTime(null);
        return this.updateById(projectParkingPlace);
    }


    @Override
    public R importExcel(MultipartFile file, String type) {
        ExcelResultVo excelResultVo = new ExcelResultVo();
        ParkingManageRelExcelEnum parkingManageRelExcelEnum = ParkingManageRelExcelEnum.getEnum(type);
        try {
            if (parkingManageRelExcelEnum == null) {
                return R.failed("不存在该类型的文件");
            }
            EasyExcel.read(file.getInputStream(), ParkingManageRelExcel.class,
                    new ProjectParkingPlaceListener<ParkingManageRelExcel>(
                            projectEntityLevelCfgService.checkIsEnabled(),
                            projectFrameInfoService,
                            this,
                            projectPersonInfoService,
                            parkingManageRelExcelEnum,
                            excelResultVo,
                            redisTemplate))
                    .sheet().doRead();
        } catch (IOException e) {
            return R.failed("文件读取异常");
        } catch (Exception e) {
//            throw e;
            return R.failed(e.getMessage());
        }
        return R.ok(excelResultVo);
    }

    @Override
    public void errorExcel(String name, HttpServletResponse httpServletResponse) throws IOException {
        String dataString = redisTemplate.opsForValue().get(name);
        String[] keys = name.split("-");
        ParkingManageRelExcelEnum parkingManageRelExcelEnum = ParkingManageRelExcelEnum.getEnum(keys[0]);
        List data = JSONUtil.toList(JSONUtil.parseArray(dataString), parkingManageRelExcelEnum.getClazz());
        String excelPath = DeviceExcelConstant.XLSX_PATH + parkingManageRelExcelEnum.getName();
        ClassPathResource classPathResource = new ClassPathResource(excelPath);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        String fileName = "失败名单:" + parkingManageRelExcelEnum.getName();
        httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        EasyExcel.write(httpServletResponse.getOutputStream(), parkingManageRelExcelEnum.getClazz())
                .withTemplate(classPathResource.getStream()).sheet(0).doFill(data);


    }

    @Override
    public void modelExcel(String policeStatus, HttpServletResponse httpServletResponse) throws IOException {
        ParkingManageRelExcelEnum parkingManageRelExcelEnum = ParkingManageRelExcelEnum.getEnum(policeStatus);
        List<ParkingManageRelExcel> data = Lists.newArrayList();
        ParkingManageRelExcel parkingManageRelExcel = new ParkingManageRelExcel();
        parkingManageRelExcel.setParkName("");
        data.add(parkingManageRelExcel);
        String excelPath = DeviceExcelConstant.XLSX_PATH + parkingManageRelExcelEnum.getName();
        ClassPathResource classPathResource = new ClassPathResource(excelPath);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        String fileName = parkingManageRelExcelEnum.getName();
        httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        EasyExcel.write(httpServletResponse.getOutputStream(), parkingManageRelExcelEnum
                .getClazz()).withTemplate(classPathResource.getStream()).sheet(0).doFill(data);

    }

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
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_UNCOMMITTED)
    public synchronized String allocationPersonPublicParkingPlace(String parkId, String personId, String personName) {
        if (StrUtil.isBlank(parkId) || StrUtil.isBlank(personId) || StrUtil.isBlank(personName)) {
            return "";
        }
        int count = this.count(new QueryWrapper<ProjectParkingPlace>().lambda()
                .eq(ProjectParkingPlace::getParkId, parkId)
                .eq(ProjectParkingPlace::getPlaceName, ""));
        if (count < 1000) {
            projectParkRegionService.initPublicParkingPlace(parkId, ProjectContextHolder.getProjectId());
        }
        List<ProjectParkingPlace> remainingParkList = this.list(new QueryWrapper<ProjectParkingPlace>().lambda()
                .eq(ProjectParkingPlace::getParkId, parkId)
                .eq(ProjectParkingPlace::getPlaceName, "")
                .and(i -> i.isNull(ProjectParkingPlace::getPersonId).or().eq(ProjectParkingPlace::getPersonId, ""))
                .last("limit 1")
        );

        if (CollUtil.isNotEmpty(remainingParkList)) {
            ProjectParkingPlace projectParkingPlace = remainingParkList.get(0);
            projectParkingPlace.setPersonId(personId);
            projectParkingPlace.setPersonName(personName);
            this.updateById(projectParkingPlace);
            return projectParkingPlace.getPlaceId();
        }
        throw new RuntimeException("无剩余公共车位可用");
    }

    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_UNCOMMITTED)
    public synchronized String allocationPersonPublicParkingPlace(String parkId, String personId, String personName, String ruleId) {
        if (StrUtil.isBlank(parkId) || StrUtil.isBlank(personId) || StrUtil.isBlank(personName)) {
            return "";
        }
        List<ProjectParkingPlace> remainingParkList = this.list(new QueryWrapper<ProjectParkingPlace>().lambda()
                .eq(ProjectParkingPlace::getParkId, parkId)
                .eq(ProjectParkingPlace::getPlaceName, "")
                .and(i -> i.isNull(ProjectParkingPlace::getPersonId).or().eq(ProjectParkingPlace::getPersonId, ""))
                .last("limit 1")
        );
        if (CollUtil.isNotEmpty(remainingParkList)) {
            ProjectParkingPlace projectParkingPlace = remainingParkList.get(0);
            projectParkingPlace.setPersonId(personId);
            projectParkingPlace.setPersonName(personName);
            projectParkingPlace.setRuleId(ruleId);
            this.updateById(projectParkingPlace);
            return projectParkingPlace.getPlaceId();
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
