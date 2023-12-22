package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.DeviceExcelConstant;
import com.aurine.cloudx.estate.constant.ParCarCancelledConstant;
import com.aurine.cloudx.estate.constant.enums.*;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.excel.ProjectParCarRegisterCheckNumListener;
import com.aurine.cloudx.estate.excel.ProjectParCarRegisterListener;
import com.aurine.cloudx.estate.excel.parking.ParCarRegisterExcel;
import com.aurine.cloudx.estate.excel.parking.ParCarRegisterExportExcel;
import com.aurine.cloudx.estate.mapper.ProjectParCarRegisterMapper;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.thirdparty.module.parking.factory.ParkingFactoryProducer;
import com.aurine.cloudx.estate.util.ExcelExportUtil;
import com.aurine.cloudx.estate.util.WebSocketNotifyUtil;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 车辆登记
 *
 * @author 王伟
 * @date 2020-07-08 14:10:32
 */
@Service
public class ProjectParCarRegisterServiceImpl extends ServiceImpl<ProjectParCarRegisterMapper, ProjectParCarRegister> implements ProjectParCarRegisterService {

    @Resource
    ProjectPersonInfoService projectPersonInfoService;
    @Resource
    ProjectCarInfoService projectCarInfoService;
    @Resource
    ProjectParkingPlaceService projectParkingPlaceService;
    @Resource
    ProjectParkRegionService projectParkRegionService;
    @Resource
    ProjectParkingInfoService projectParkingInfoService;
    @Resource
    ProjectParkingPlaceManageService projectParkingPlaceManageService;
    @Resource
    ProjectParkBillingRuleService projectParkBillingRuleService;
    @Resource
    ProjectParkBillingInfoService projectParkBillingInfoService;
    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    ProjectParkingPlaceHisService projectParkingPlaceHisService;

    @Resource
    private ProjectCarPreRegisterService projectCarPreRegisterService;

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Resource
    private ProjectWebSocketService projectWebSocketService;


    /**
     * 保存车辆登记、车辆、以及新的人员信息
     *
     * @param parCarRegisterVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveCarRegister(ProjectParCarRegisterVo parCarRegisterVo) {
        int registNum = this.count(new QueryWrapper<ProjectParCarRegister>().lambda()
                .eq(ProjectParCarRegister::getPlateNumber, parCarRegisterVo.getPlateNumber()));
        //获取车位信息
        ProjectParkingPlace projectParkingPlaceServiceOne = projectParkingPlaceService.getOne(new LambdaQueryWrapper<ProjectParkingPlace>()
                .eq(ProjectParkingPlace::getPlaceId, parCarRegisterVo.getParkPlaceId()));


        if (registNum != 0) {
            throw new RuntimeException("车牌号已被登记");
        }

        BigDecimal payMent = new BigDecimal(0);//租赁费用
        parCarRegisterVo.setPlateNumber(parCarRegisterVo.getPlateNumber().toUpperCase());
        // 1、校验用户是否已存在，存在记录id，不存在新增并记录id
        ProjectPersonInfo personInfo = projectPersonInfoService.getByTelephone(parCarRegisterVo.getTelephone());
        String personId = "";

        if (personInfo == null) {
            // 人员不存在,则新增人员
            personInfo = new ProjectPersonInfo();
            personId = UUID.randomUUID().toString().replaceAll("-", "");
            BeanUtils.copyProperties(parCarRegisterVo, personInfo);
            personInfo.setPersonId(personId);
            projectPersonInfoService.saveFromSystem(personInfo);

        } else {
            // 否则，获取人员信息
            personId = personInfo.getPersonId();
        }

        parCarRegisterVo.setPersonId(personId);


        // 2、校验车牌是否已存在，不存在则新增车辆信息
        ProjectCarInfo carInfo = projectCarInfoService.getCarByPlateNumber(parCarRegisterVo.getPlateNumber());
        if (carInfo == null) {
            carInfo = new ProjectCarInfo();
            BeanUtils.copyProperties(parCarRegisterVo, carInfo);
            projectCarInfoService.saveCar(carInfo);
        } else {
            // 更新车辆信息
            parCarRegisterVo.setCarUid(carInfo.getCarUid());
            BeanUtils.copyProperties(parCarRegisterVo, carInfo);
            projectCarInfoService.updateById(carInfo);
        }

        // 如果来源是审核则不添加记录
        if (StrUtil.isNotEmpty(parCarRegisterVo.getSource()) && !"audit".equals(parCarRegisterVo.getSource())) {
            // 保存车辆登记记录
            Integer userId = SecurityUtils.getUser().getId();
            ProjectCarPreRegister projectCarPreRegister = new ProjectCarPreRegister();
            projectCarPreRegister.setPersonId(personId);
            projectCarPreRegister.setAuditor(userId);
            projectCarPreRegister.setOperator(userId);
            projectCarPreRegister.setCommitTime(LocalDateTime.now());
            projectCarPreRegister.setAuditStatus(AuditStatusEnum.pass.code);
            projectCarPreRegister.setPlateNumber(parCarRegisterVo.getPlateNumber());
            projectCarPreRegisterService.save(projectCarPreRegister);

        } else {
            projectCarPreRegisterService.update(new UpdateWrapper<ProjectCarPreRegister>().lambda()
                    .eq(ProjectCarPreRegister::getPersonId, personId)
                    .eq(ProjectCarPreRegister::getPlateNumber, parCarRegisterVo.getPlateNumber())
                    .set(ProjectCarPreRegister::getAuditStatus, CarAuditStatusEnum.pass.code)
                    .set(ProjectCarPreRegister::getAuditor, SecurityUtils.getUser().getId())
            );
        }


        parCarRegisterVo.setCarUid(carInfo.getCarUid());
        ProjectParCarRegister carRegister = new ProjectParCarRegister();
        BeanUtils.copyProperties(parCarRegisterVo, carRegister);

        // 3、其他业务
        // 如果是公共停车区域，给随机分配一个没被占用的公共车位
        if (parCarRegisterVo.getRelType().equals(PlaceRelTypeEnum.PUBLIC.code)) {
            String placeId = projectParkingPlaceManageService
                    .allocationPersonPublicParkingPlace(
                            parCarRegisterVo.getParkId(),
                            parCarRegisterVo.getPersonId(),
                            parCarRegisterVo.getPersonName());
            carRegister.setParkPlaceId(placeId);
        } else {
            projectParkingPlaceServiceOne.setRuleId(parCarRegisterVo.getRuleId());
            projectParkingPlaceService.update(projectParkingPlaceServiceOne);
        }

        // 存储停车区域车位号 冗余存储
        List<ProjectParkingInfo> projectParkingInfoList = projectParkingInfoService
                .list(new QueryWrapper<ProjectParkingInfo>().lambda()
                        .eq(ProjectParkingInfo::getParkId, carRegister.getParkId())
                        .eq(ProjectParkingInfo::getProjectId, ProjectContextHolder.getProjectId()));
        if (CollUtil.isNotEmpty(projectParkingInfoList)) {
            ProjectParkingInfo parkingInfo = projectParkingInfoList.get(0);
            //存储车位计费规则
            ProjectParkingPlace placeInfo = projectParkingPlaceService.getById(carRegister.getParkPlaceId());
            ProjectParkRegion region = projectParkRegionService.getById(placeInfo.getParkRegionId());
            if (StrUtil.isBlank(placeInfo.getPlaceName())) {
                carRegister.setParkPlaceName(parkingInfo.getParkName());
            } else {
                carRegister.setParkPlaceName(parkingInfo.getParkName() + "-" + region.getParkRegionName() + "-" + placeInfo.getPlaceName());
            }
            // 因为一位多车调整 收费规则现在存储在车辆登记表
//            placeInfo.setRuleId(parCarRegisterVo.getRuleId());
//            projectParkingPlaceService.updateById(placeInfo);
        }

        carRegister.setIsCancelled(ParCarCancelledConstant.NOT_CANCEL);
        carRegister.setCarUid(carInfo.getCarUid());

        // 如果是公共区域的月租车，记录缴纳月租金额
/*        if (parCarRegisterVo.getRelType().equals(PlaceRelTypeEnum.PUBLIC.code)) {
            ProjectParkBillingRule rule = projectParkBillingRuleService.getById(parCarRegisterVo.getRuleId());
            if (rule.getRuleType().equals(ParkingRuleTypeEnum.MONTH.code)) {
                projectParkBillingInfoService.monthlyPay(
                        parCarRegisterVo.getParkId(),
                        parCarRegisterVo.getPlateNumber(),
                        parCarRegisterVo.getPayment());
                payMent = parCarRegisterVo.getPayment();
            }
        }*/


        //只要是月租车 就生成缴费
        ProjectParkBillingRule rule = projectParkBillingRuleService.getById(parCarRegisterVo.getRuleId());
        if (rule.getRuleType().equals(ParkingRuleTypeEnum.MONTH.code)) {
            projectParkBillingInfoService.monthlyPay(
                    parCarRegisterVo.getParkId(),
                    parCarRegisterVo.getPlateNumber(),
                    parCarRegisterVo.getPayment());
            payMent = parCarRegisterVo.getPayment();
        }


//        remoteParkingService.addCar(carRegister);
        try {
            ParkingFactoryProducer.getFactory(carRegister.getParkId()).getParkingService().addCar(carRegister);
        } catch (Exception e) {
            // 取消车位和人的关联
            projectParkingPlaceService.cleanParkingPlace(carRegister.getParkPlaceId());
            throw e;
        }
        ParkingFactoryProducer.getFactory(carRegister.getParkId()).getParkingService()
                .extraDate(carRegister, carRegister.getPlateNumber(), payMent, parCarRegisterVo.getStartTime().atStartOfDay(),
                        DateUtil.parseLocalDateTime(parCarRegisterVo.getEndTime().toString() + " 23:59:59"));
        boolean save = this.save(carRegister);
        WebSocketNotifyUtil.sendMessgae(ProjectContextHolder.getProjectId().toString(), JSONObject.toJSONString(projectWebSocketService.findNumByProjectId()));
        return save;
    }

    /**
     * 保存车辆登记、车辆、以及新的人员信息
     *
     * @param parCarRegisterVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveCarRegister(ProjectParCarRegisterVo parCarRegisterVo, Integer operatorId) {
        int registNum = this.count(new QueryWrapper<ProjectParCarRegister>().lambda()
                .eq(ProjectParCarRegister::getPlateNumber, parCarRegisterVo.getPlateNumber()));
        //获取车位信息
        ProjectParkingPlace projectParkingPlaceServiceOne = projectParkingPlaceService.getOne(new LambdaQueryWrapper<ProjectParkingPlace>()
                .eq(ProjectParkingPlace::getPlaceId, parCarRegisterVo.getParkPlaceId()));


        if (registNum != 0) {
            throw new RuntimeException("车牌号已被登记");
        }

        BigDecimal payMent = new BigDecimal(0);//租赁费用
        parCarRegisterVo.setPlateNumber(parCarRegisterVo.getPlateNumber().toUpperCase());
        // 1、校验用户是否已存在，存在记录id，不存在新增并记录id
        ProjectPersonInfo personInfo = projectPersonInfoService.getByTelephone(parCarRegisterVo.getTelephone());
        String personId = "";

        if (personInfo == null) {
            // 人员不存在,则新增人员
            personInfo = new ProjectPersonInfo();
            personId = UUID.randomUUID().toString().replaceAll("-", "");
            BeanUtils.copyProperties(parCarRegisterVo, personInfo);
            personInfo.setPersonId(personId);
            projectPersonInfoService.saveFromSystem(personInfo);

        } else {
            // 否则，获取人员信息
            personId = personInfo.getPersonId();
        }

        parCarRegisterVo.setPersonId(personId);


        // 2、校验车牌是否已存在，不存在则新增车辆信息
        ProjectCarInfo carInfo = projectCarInfoService.getCarByPlateNumber(parCarRegisterVo.getPlateNumber());
        if (carInfo == null) {
            carInfo = new ProjectCarInfo();
            BeanUtils.copyProperties(parCarRegisterVo, carInfo);
            projectCarInfoService.saveCar(carInfo);
        } else {
            // 更新车辆信息
            parCarRegisterVo.setCarUid(carInfo.getCarUid());
            BeanUtils.copyProperties(parCarRegisterVo, carInfo);
            projectCarInfoService.updateById(carInfo);
        }

        // 如果来源是审核则不添加记录
        if (StrUtil.isNotEmpty(parCarRegisterVo.getSource()) && !"audit".equals(parCarRegisterVo.getSource())) {
            // 保存车辆登记记录
            ProjectCarPreRegister projectCarPreRegister = new ProjectCarPreRegister();
            projectCarPreRegister.setPersonId(personId);
            projectCarPreRegister.setAuditor(operatorId);
            projectCarPreRegister.setOperator(operatorId);
            projectCarPreRegister.setCommitTime(LocalDateTime.now());
            projectCarPreRegister.setAuditStatus(AuditStatusEnum.pass.code);
            projectCarPreRegister.setPlateNumber(parCarRegisterVo.getPlateNumber());
            projectCarPreRegisterService.save(projectCarPreRegister);

        } else {
            projectCarPreRegisterService.update(new UpdateWrapper<ProjectCarPreRegister>().lambda()
                    .eq(ProjectCarPreRegister::getPersonId, personId)
                    .eq(ProjectCarPreRegister::getPlateNumber, parCarRegisterVo.getPlateNumber())
                    .set(ProjectCarPreRegister::getAuditStatus, CarAuditStatusEnum.pass.code)
                    .set(ProjectCarPreRegister::getAuditor, operatorId)
            );
        }


        parCarRegisterVo.setCarUid(carInfo.getCarUid());
        ProjectParCarRegister carRegister = new ProjectParCarRegister();
        BeanUtils.copyProperties(parCarRegisterVo, carRegister);

        // 3、其他业务
        // 如果是公共停车区域，给随机分配一个没被占用的公共车位
        if (parCarRegisterVo.getRelType().equals(PlaceRelTypeEnum.PUBLIC.code)) {
            String placeId = projectParkingPlaceManageService
                    .allocationPersonPublicParkingPlace(
                            parCarRegisterVo.getParkId(),
                            parCarRegisterVo.getPersonId(),
                            parCarRegisterVo.getPersonName());
            carRegister.setParkPlaceId(placeId);
        } else {
            projectParkingPlaceServiceOne.setRuleId(parCarRegisterVo.getRuleId());
            projectParkingPlaceService.update(projectParkingPlaceServiceOne);
        }

        // 存储停车区域车位号 冗余存储
        List<ProjectParkingInfo> projectParkingInfoList = projectParkingInfoService
                .list(new QueryWrapper<ProjectParkingInfo>().lambda()
                        .eq(ProjectParkingInfo::getParkId, carRegister.getParkId())
                        .eq(ProjectParkingInfo::getProjectId, ProjectContextHolder.getProjectId()));
        if (CollUtil.isNotEmpty(projectParkingInfoList)) {
            ProjectParkingInfo parkingInfo = projectParkingInfoList.get(0);
            //存储车位计费规则
            ProjectParkingPlace placeInfo = projectParkingPlaceService.getById(carRegister.getParkPlaceId());
            ProjectParkRegion region = projectParkRegionService.getById(placeInfo.getParkRegionId());
            if (StrUtil.isBlank(placeInfo.getPlaceName())) {
                carRegister.setParkPlaceName(parkingInfo.getParkName());
            } else {
                carRegister.setParkPlaceName(parkingInfo.getParkName() + "-" + region.getParkRegionName() + "-" + placeInfo.getPlaceName());
            }
            // 因为一位多车调整 收费规则现在存储在车辆登记表
//            placeInfo.setRuleId(parCarRegisterVo.getRuleId());
//            projectParkingPlaceService.updateById(placeInfo);
        }

        carRegister.setIsCancelled(ParCarCancelledConstant.NOT_CANCEL);
        carRegister.setCarUid(carInfo.getCarUid());

        // 如果是公共区域的月租车，记录缴纳月租金额
        if (parCarRegisterVo.getRelType().equals(PlaceRelTypeEnum.PUBLIC.code)) {
            ProjectParkBillingRule rule = projectParkBillingRuleService.getById(parCarRegisterVo.getRuleId());
            if (rule.getRuleType().equals(ParkingRuleTypeEnum.MONTH.code)) {
                projectParkBillingInfoService.monthlyPay(
                        parCarRegisterVo.getParkId(),
                        parCarRegisterVo.getPlateNumber(),
                        parCarRegisterVo.getPayment());
                payMent = parCarRegisterVo.getPayment();
            }
        }


//        remoteParkingService.addCar(carRegister);
        try {
            ParkingFactoryProducer.getFactory(carRegister.getParkId()).getParkingService().addCar(carRegister);
        } catch (Exception e) {
            // 取消车位和人的关联
            projectParkingPlaceService.cleanParkingPlace(carRegister.getParkPlaceId());
            throw e;
        }
        ParkingFactoryProducer.getFactory(carRegister.getParkId()).getParkingService()
                .extraDate(carRegister, carRegister.getPlateNumber(), payMent, parCarRegisterVo.getStartTime().atStartOfDay(), parCarRegisterVo.getEndTime().atStartOfDay());
        return this.save(carRegister);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveCarRegisterBatch(List<ProjectParCarRegisterVo> projectParCarRegisterVoList) {
        List<ProjectParCarRegisterVo> parCarRegisterVoList = projectPersonInfoService.initParCarRegisterPersonId(projectParCarRegisterVoList);
        projectCarInfoService.saveOrUpdateCarByRegisterVoList(parCarRegisterVoList);
        //TODO: 这里不知道会不会有问题就先用for循环 分配公共车位了
        List<ProjectParCarRegister> parCarRegisterList = new ArrayList<>();
        parCarRegisterVoList.forEach(parCarRegisterVo -> {
            // 这里已经为车位设置了收费规则 ruleId
            // 虽然私有和租赁车位变成可以支持一位多车，但是公共车位仍然是一位一车 所以收费规则仍然存在车位表应该是没问题的
            String placeId = projectParkingPlaceManageService.allocationPersonPublicParkingPlace(
                    parCarRegisterVo.getParkId(),
                    parCarRegisterVo.getPersonId(),
                    parCarRegisterVo.getPersonName(),
                    parCarRegisterVo.getRuleId()
            );
            // 这里将已经分配的公共车位id存入车辆登记对象
            ProjectParCarRegister parCarRegister = new ProjectParCarRegister();
            BeanUtil.copyProperties(parCarRegisterVo, parCarRegister);
            parCarRegister.setParkPlaceId(placeId);
            parCarRegister.setCarUid(parCarRegisterVo.getCarUid());
            parCarRegisterVo.setParkPlaceId(placeId);
            parCarRegisterList.add(parCarRegister);
            // Excel导入固定是这个车位所以直接写死了
            parCarRegister.setParkPlaceName(parCarRegisterVo.getParkName() + "-公共车位");
            parCarRegisterVo.setParkPlaceName(parCarRegisterVo.getParkName() + "-公共车位");
            parCarRegister.setIsCancelled(ParCarCancelledConstant.NOT_CANCEL);
        });

        //TODO: 这里只能for循环了
        parCarRegisterVoList.forEach(parCarRegisterVo -> {
            BigDecimal payMent = new BigDecimal(0);
            ProjectParkBillingRule rule = projectParkBillingRuleService.getById(parCarRegisterVo.getRuleId());
            if (rule.getRuleType().equals(ParkingRuleTypeEnum.MONTH.code)) {
                projectParkBillingInfoService.monthlyPay(
                        parCarRegisterVo.getParkId(),
                        parCarRegisterVo.getPlateNumber(),
                        parCarRegisterVo.getPayment());
                payMent = parCarRegisterVo.getPayment();
            }
            ParkingFactoryProducer.getFactory(parCarRegisterVo.getParkId()).getParkingService().addCar(parCarRegisterVo);
            ParkingFactoryProducer.getFactory(parCarRegisterVo.getParkId()).getParkingService().extraDate(parCarRegisterVo,
                    parCarRegisterVo.getPlateNumber(), payMent, parCarRegisterVo.getStartTime().atStartOfDay(),
                    parCarRegisterVo.getEndTime().atStartOfDay());

        });
        return this.saveBatch(parCarRegisterList);
    }

    /**
     * 撤销车辆注销
     *
     * @param registerId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelCarRegister(String registerId) {
        // 1、还原车位数据，清空车位占用和车位计费规则
        ProjectParCarRegister carRegister = this.getById(registerId);
        projectParkingPlaceService.cancelParkingPlace(carRegister.getParkPlaceId());
        // 这里删除车辆
        projectCarInfoService.remove(new QueryWrapper<ProjectCarInfo>().lambda().eq(ProjectCarInfo::getPlateNumber, carRegister.getPlateNumber()));
        // 调用接口
        ParkingFactoryProducer.getFactory(carRegister.getParkId()).getParkingService().removeCar(carRegister);

        // 2、删除注册数据
        return this.removeById(registerId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelCarRegister(List<String> registerIdList) {
        boolean cancelPlaceResult = false;
        boolean removeCarResult = false;
        if (CollUtil.isNotEmpty(registerIdList)) {
            List<ProjectParCarRegister> cancelList = this.list(new QueryWrapper<ProjectParCarRegister>()
                    .lambda().in(ProjectParCarRegister::getRegisterId, registerIdList));
            if (CollUtil.isNotEmpty(cancelList)) {
                List<String> placeIdList = cancelList.stream().map(ProjectParCarRegister::getParkPlaceId).collect(Collectors.toList());
                Set<String> plateNumberSet = cancelList.stream().map(ProjectParCarRegister::getPlateNumber).collect(Collectors.toSet());
                // 重置车位和删除车辆
                cancelPlaceResult = projectParkingPlaceService.cancelParkingPlace(placeIdList);
                removeCarResult = projectCarInfoService.remove(new QueryWrapper<ProjectCarInfo>().lambda().in(ProjectCarInfo::getPlateNumber, plateNumberSet));
//                this.remove(new QueryWrapper<ProjectParCarRegister>().lambda().in(ProjectParCarRegister::getRegisterId, registerIdList));
                cancelList.forEach(carRegister -> {
                    ParkingFactoryProducer.getFactory(carRegister.getParkId()).getParkingService().removeCar(carRegister);
                });

                // 对人进行判断有哪些是需要进行删除的
                List<ProjectCarInfo> carInfoList = projectCarInfoService.list(new QueryWrapper<ProjectCarInfo>().lambda()
                        .in(ProjectCarInfo::getPlateNumber, plateNumberSet));
                Set<String> personIdSet = carInfoList.stream().map(ProjectCarInfo::getPersonId).collect(Collectors.toSet());
                if (CollUtil.isNotEmpty(personIdSet)) {
                    projectPersonInfoService.checkPersonAssets(new ArrayList<>(personIdSet));
                }
                if (CollUtil.isNotEmpty(plateNumberSet)) {
                    // 删除车辆登记历史
                    projectCarPreRegisterService.remove(new QueryWrapper<ProjectCarPreRegister>().lambda().in(ProjectCarPreRegister::getPlateNumber, plateNumberSet));
                    this.remove(new LambdaQueryWrapper<ProjectParCarRegister>().in(ProjectParCarRegister::getPlateNumber, plateNumberSet));
                }
            }
        }
        return cancelPlaceResult && removeCarResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delay(ProjectParCarRegisterVo carRegisterVo) {
        LocalDate endTime = carRegisterVo.getEndTime();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String localTime = df.format(endTime);
        //取消车位同步延期
//        projectParkingPlaceManageService.updateRentTime(carRegisterVo.getParkPlaceId(), localTime);

        ProjectParkBillingRule rule = projectParkBillingRuleService.getById(carRegisterVo.getRuleId());
        if (rule.getRuleType().equals(ParkingRuleTypeEnum.MONTH.code) && carRegisterVo.getPayment().compareTo(BigDecimal.ZERO) != 0) {
            projectParkBillingInfoService.monthlyPay(carRegisterVo.getParkId(), carRegisterVo.getPlateNumber(), carRegisterVo.getPayment());
        }

        // 调用车场对接接口
        ProjectParCarRegister carRegister = this.getById(carRegisterVo.getRegisterId());
        ParkingFactoryProducer.getFactory(carRegisterVo.getParkId()).getParkingService().extraDate(carRegister, carRegisterVo.getPlateNumber(), carRegisterVo.getPayment(), carRegisterVo.getStartTime().atStartOfDay(),
                DateUtil.parseLocalDateTime(carRegisterVo.getEndTime().toString() + " 23:59:59"));
//        remoteParkingService.extraDate(vo.getRegisterId(), vo.getPlateNumber(), vo.getPayment(), vo.getStartTime().atStartOfDay(), vo.getEndTime().atStartOfDay());

        List<ProjectParCarRegister> list = this.list(new LambdaQueryWrapper<ProjectParCarRegister>().eq(ProjectParCarRegister::getParkPlaceId, carRegister.getParkPlaceId()));
        for (ProjectParCarRegister projectParCarRegister : list) {
            projectParCarRegister.setEndTime(carRegisterVo.getEndTime());
        }

        return this.updateBatchById(list);
    }

    @Override
    public R importExcel(MultipartFile file, String type) {
        ExcelResultVo excelResultVo = new ExcelResultVo();
        ParCarRegisterExcelEnum parCarRegisterExcelEnum = ParCarRegisterExcelEnum.getEnum(type);
        ExcelPlaceNumResultVo excelPlaceNumResultVo = new ExcelPlaceNumResultVo();
        try {
            EasyExcel.read(file.getInputStream(), ParCarRegisterExcel.class,
                    new ProjectParCarRegisterCheckNumListener<>(
                            projectParkingInfoService,
                            projectParkingPlaceService,
                            excelPlaceNumResultVo
                    )).sheet().doRead();
            if (excelPlaceNumResultVo.isContinueAble()) {
                EasyExcel.read(file.getInputStream(), ParCarRegisterExcel.class,
                        new ProjectParCarRegisterListener<>(
                                this,
                                projectPersonInfoService,
                                projectParkingInfoService,
                                projectParkBillingRuleService,
                                parCarRegisterExcelEnum,
                                excelResultVo,
                                redisTemplate,
                                threadPoolTaskExecutor
                        )).sheet().doRead();
            }
        } catch (IOException e) {
            return R.failed("文件读取异常");
        } catch (Exception e) {
//            throw e;
            return R.failed(e.getMessage());
        }
        if (!excelPlaceNumResultVo.isContinueAble()) {
            return R.ok(excelPlaceNumResultVo);
        } else {
            return R.ok(excelResultVo);
        }
    }


    @Override
    public void errorExcel(String name, HttpServletResponse httpServletResponse) throws IOException {
        String dataString = redisTemplate.opsForValue().get(name);
        String[] keys = name.split("-");
        ParCarRegisterExcelEnum parCarRegisterExcelEnum = ParCarRegisterExcelEnum.getEnum(keys[0]);
        List data = JSONUtil.toList(JSONUtil.parseArray(dataString), parCarRegisterExcelEnum.getClazz());
        String excelPath = DeviceExcelConstant.XLSX_PATH + parCarRegisterExcelEnum.getName();
        ClassPathResource classPathResource = new ClassPathResource(excelPath);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        String fileName = "失败名单:" + parCarRegisterExcelEnum.getName();
        httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        EasyExcel.write(httpServletResponse.getOutputStream(), parCarRegisterExcelEnum.getClazz())
                .withTemplate(classPathResource.getStream()).sheet(0).doFill(data);

    }

    @Override
    public void modelExcel(String policeStatus, HttpServletResponse httpServletResponse) throws IOException {
        ParCarRegisterExcelEnum parCarRegisterExcelEnum = ParCarRegisterExcelEnum.getEnum(policeStatus);
        List<ParCarRegisterExcel> data = new ArrayList<>();
        ParCarRegisterExcel parCarRegisterExcel = new ParCarRegisterExcel();
        parCarRegisterExcel.setParkNameCh("");
        data.add(parCarRegisterExcel);
        String excelPath = DeviceExcelConstant.XLSX_PATH + parCarRegisterExcelEnum.getName();
        ClassPathResource classPathResource = new ClassPathResource(excelPath);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        String fileName = parCarRegisterExcelEnum.getName();
        httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        EasyExcel.write(httpServletResponse.getOutputStream(), parCarRegisterExcelEnum
                .getClazz()).withTemplate(classPathResource.getStream()).sheet(0).doFill(data);
    }

    @Override
    public boolean isAlreadyAMultiCar() {
        return this.baseMapper.getMultiCarNum() > 0;
    }

    /**
     * 返回车位的有效期和收费方式
     *
     * @param projectParCarRegister
     * @return
     */
    @Override
    public ProjectParCarRegister getValidityByPlaceId(ProjectParCarRegisterVo projectParCarRegister) {
//        ProjectParkingPlaceHis one = projectParkingPlaceHisService.getOne(new LambdaQueryWrapper<ProjectParkingPlaceHis>()
//                .eq(ProjectParkingPlaceHis::getPlaceId, projectParCarRegister.getParkPlaceId()));

        ProjectParCarRegister projectParCarRegisterOne = this.getOne(new LambdaQueryWrapper<ProjectParCarRegister>()
                .eq(ProjectParCarRegister::getParkPlaceId, projectParCarRegister.getParkPlaceId())
                .last("LIMIT 1"));

        if (projectParCarRegisterOne != null) {
//            projectParCarRegister.setExpTime(one.getExpTime());
            projectParCarRegister.setRuleId(projectParCarRegisterOne.getRuleId());
            projectParCarRegister.setStartTime(projectParCarRegisterOne.getStartTime());
            projectParCarRegister.setEndTime(projectParCarRegisterOne.getEndTime());
        }
        return projectParCarRegister;
    }

    @Override
    public Map<String, Integer> getRegisterAndExpire() {
        int registerCount = this.count(new LambdaQueryWrapper<ProjectParCarRegister>().eq(ProjectParCarRegister::getProjectId, ProjectContextHolder.getProjectId()));

        int ExpireCount = this.count(new LambdaQueryWrapper<ProjectParCarRegister>().eq(ProjectParCarRegister::getProjectId, ProjectContextHolder.getProjectId())
                .lt(ProjectParCarRegister::getEndTime, LocalDate.now()));
        Map<String, Integer> map = new HashMap<>();
        map.put("registerCount", registerCount);
        map.put("ExpireCount", ExpireCount);
        return map;
    }
    /**
     * 分页查询信息
     *
     * @param page
     * @param searchConditionVo
     * @return
     */
    @Override
    public Page<ProjectParCarRegisterRecordVo> pageCarRegister(Page<ProjectParCarRegisterRecordVo> page, ProjectParCarRegisterSeachConditionVo searchConditionVo) {
        return this.baseMapper.pageCarRegister(page, searchConditionVo, ProjectContextHolder.getProjectId());
    }

    /**
     * 导出数据
     *
     * @param page
     * @param searchConditionVo
     * @return
     */
    @Override
    public Boolean pageCarRegisterExport(Page<ProjectParCarRegisterRecordVo> page, ProjectParCarRegisterSeachConditionVo searchConditionVo, HttpServletResponse httpServletResponse) {

        List<ParCarRegisterExportExcel> parCarRegisterExcels = this.baseMapper.pageCarRegisterExport(searchConditionVo, ProjectContextHolder.getProjectId());
        List<ParCarRegisterExportExcel> collect = parCarRegisterExcels.stream().map(e -> {
            if (StrUtil.isNotEmpty(e.getRelType())) {
                e.setRelType(RelTypeListEnum.getByCode(e.getRelType()).value);
            }
            return e;
        }).collect(Collectors.toList());
        ExcelExportUtil excelExportUtil = new ExcelExportUtil();
        excelExportUtil.exportExcel("车位登记表", ParCarRegisterExportExcel.class, collect, httpServletResponse);
        return true;
    }


    /**
     * 更新车辆、以及新的人员信息
     *
     * @param parCarRegisterVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateCarRegister(ProjectParCarRegisterVo parCarRegisterVo) {
        // 更新人员信息
        ProjectPersonInfo personInfo = new ProjectPersonInfo();
        personInfo.setPersonId(parCarRegisterVo.getPersonId());
        personInfo.setPersonName(parCarRegisterVo.getPersonName());
        personInfo.setTelephone(parCarRegisterVo.getTelephone());
        projectPersonInfoService.updateById(personInfo);

        // 更新车位信息
        ProjectParkingPlace projectParkingPlace = new ProjectParkingPlace();
        projectParkingPlace.setPlaceId(parCarRegisterVo.getParkPlaceId());
        projectParkingPlace.setPersonName(parCarRegisterVo.getPersonName());
        projectParkingPlaceService.updateById(projectParkingPlace);

        // 更新车辆信息
        ProjectCarInfo car = new ProjectCarInfo();
        BeanUtils.copyProperties(parCarRegisterVo, car);
        projectCarInfoService.updateById(car);

        // 更新登记信息（车牌号）
        ProjectParCarRegister projectParCarRegister = new ProjectParCarRegister();
        projectParCarRegister.setRegisterId(parCarRegisterVo.getRegisterId());
        projectParCarRegister.setPlateNumber(parCarRegisterVo.getPlateNumber().toUpperCase());
        return this.updateById(projectParCarRegister);
    }


    /**
     * 通过id获取注册信息VO,包含车辆信息、人员信息、车位信息、缴费信息等
     *
     * @param id
     * @return
     */
    @Override
    public ProjectParCarRegisterVo getVo(String id) {
        ProjectParCarRegisterVo vo = new ProjectParCarRegisterVo();

        ProjectParCarRegister carRegPo = getById(id);//注册对象、
        String ruleId = carRegPo.getRuleId();
        ProjectCarInfo carInfo = projectCarInfoService.getCar(carRegPo.getCarUid());// 车辆信息
        ProjectPersonInfo personInfo = projectPersonInfoService.getById(carInfo.getPersonId());//人员信息
        BeanUtils.copyProperties(carRegPo, vo);
        BeanUtils.copyProperties(carInfo, vo);
        ProjectParkingPlace projectParkingPlace = projectParkingPlaceService.getById(vo.getParkPlaceId());
        LocalDateTime voCreateTime = vo.getCreateTime();
        BeanUtils.copyProperties(projectParkingPlace, vo);
        vo.setCreateTime(voCreateTime);
        // 如果车辆登记表有收费规则ID则使用车辆登记表的数据（这里会被车位表中已废弃的收费规则ID替换）
        if (StrUtil.isNotEmpty(ruleId)) {
            vo.setRuleId(ruleId);
        }
        vo.setPersonName(personInfo.getPersonName());
        vo.setTelephone(personInfo.getTelephone());
        // 这里remark会被上面的personInfo中的remark覆盖导致车辆信息的remark丢失
        vo.setRemark(carInfo.getRemark());

        return vo;
    }

    @Override
    public boolean checkHasRegister(String placeId) {
        List<ProjectParCarRegister> parCarRegisterList = this.list(new QueryWrapper<ProjectParCarRegister>().lambda().eq(ProjectParCarRegister::getParkPlaceId, placeId));
        return parCarRegisterList.size() > 0;
    }


}
