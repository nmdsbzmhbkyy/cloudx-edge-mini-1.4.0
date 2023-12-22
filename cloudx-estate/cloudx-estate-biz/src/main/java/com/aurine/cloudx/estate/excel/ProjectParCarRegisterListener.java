package com.aurine.cloudx.estate.excel;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.ParCarRegisterExcelEnum;
import com.aurine.cloudx.estate.constant.enums.PlaceRelTypeEnum;
import com.aurine.cloudx.estate.dict.DictResult;
import com.aurine.cloudx.estate.dict.DictUtil;
import com.aurine.cloudx.estate.entity.ProjectParCarRegister;
import com.aurine.cloudx.estate.entity.ProjectPersonInfo;
import com.aurine.cloudx.estate.excel.parking.ParCarRegisterExcel;
import com.aurine.cloudx.estate.service.ProjectParCarRegisterService;
import com.aurine.cloudx.estate.service.ProjectParkBillingRuleService;
import com.aurine.cloudx.estate.service.ProjectParkingInfoService;
import com.aurine.cloudx.estate.service.ProjectPersonInfoService;
import com.aurine.cloudx.estate.vo.ExcelResultVo;
import com.aurine.cloudx.estate.vo.ProjectParCarRegisterVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: ProjectParCarRegisterListener
 * @author: 王良俊 <>
 * @date: 2020年09月02日 下午04:19:13
 * @Copyright:
 */
public class ProjectParCarRegisterListener<T> extends AnalysisEventListener<T> {

    ProjectParCarRegisterService projectParCarRegisterService;
    ProjectPersonInfoService projectPersonInfoService;
    ProjectParkingInfoService projectParkingInfoService;
    ProjectParkBillingRuleService projectParkBillingRuleService;

    /**
     * 导入错误信息列表
     */
    HashMap<Integer, String> errorMessageMap = new HashMap<>();

    /**
     * 导入数据列表
     */
    HashMap<Integer, Object> errorMap = new HashMap<>();

    /**
     * 同手机号对应的人名不能不一样
     */
    HashMap<String, Object> parCarRegisterTelephoneRelHashMap = new HashMap<>();

    /**
     * 一辆车(车牌号)只能同时登记一个车位
     */
    HashMap<String, Object> parCarRegisterPlateNumberHashMap = new HashMap<>();

    private final Integer projectId = ProjectContextHolder.getProjectId();

    private final Integer tenantId = TenantContextHolder.getTenantId();

    private final Integer operatorId = SecurityUtils.getUser().getId();

    /**
     * 住户迁入Excel表格枚举类型
     */
    private final ParCarRegisterExcelEnum parCarRegisterExcelEnum;

    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(50);


    String uuid = UUID.randomUUID().toString().replaceAll("-", "");

    String parkingRuleRedisKey = "parkingRule" + uuid;

    String parkingRedisKey = "parking" + uuid;

    String personInfoRedisKey = "personInfo" + uuid;

    /**
     * Excel处理结果
     */
    private final ExcelResultVo excelResultVo;

    RedisTemplate<String, String> redisTemplate;

    public ProjectParCarRegisterListener(ProjectParCarRegisterService projectParCarRegisterService,
                                         ProjectPersonInfoService projectPersonInfoService,
                                         ProjectParkingInfoService projectParkingInfoService,
                                         ProjectParkBillingRuleService projectParkBillingRuleService,
                                         ParCarRegisterExcelEnum parCarRegisterExcelEnum,
                                         ExcelResultVo excelResultVo,
                                         RedisTemplate<String, String> redisTemplate,
                                         ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        this.projectParCarRegisterService = projectParCarRegisterService;
        this.projectPersonInfoService = projectPersonInfoService;
        this.projectParkingInfoService = projectParkingInfoService;
        this.projectParkBillingRuleService = projectParkBillingRuleService;
        this.parCarRegisterExcelEnum = parCarRegisterExcelEnum;
        this.excelResultVo = excelResultVo;
        this.redisTemplate = redisTemplate;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    }

    /**
     * <p>
     * 每一行excel都会执行这个方法
     * </p>
     *
     * @param context 上下文 获取行数信息
     * @param data    每一行的数据
     */
    @Override
    public void invoke(T data, AnalysisContext context) {
        // 这里通过线程回调来进行成功失败记录
        System.out.println("线程ID：" + Thread.currentThread().getId());
        ParCarRegisterExcel parCarRegisterExcel = new ParCarRegisterExcel();
        BeanUtil.copyProperties(data, parCarRegisterExcel);
        ProjectParCarRegisterVo parCarRegisterVo = new ProjectParCarRegisterVo();
        BeanUtil.copyProperties(parCarRegisterExcel, parCarRegisterVo);
        Integer currentRow = context.readRowHolder().getRowIndex();

        // 统一处理parCarRegisterExcel面需要使用字典进行转换的属性
        handleStrAttr(parCarRegisterVo, parCarRegisterExcel, currentRow);

        // 统一处理parCarRegisterExcel面需要使用字典进行转换的属性
        handleDictAttr(parCarRegisterVo, parCarRegisterExcel, currentRow);

        // 统一处理时间格式验证
        handleTimeAttr(parCarRegisterVo, parCarRegisterExcel, currentRow);

        // 统一处理手机号格式验证
        handlePhoneNumberAttr(parCarRegisterVo, parCarRegisterExcel, currentRow);

        // 统一处理数值类型的验证
        handleNumberAttr(parCarRegisterVo, parCarRegisterExcel, currentRow);

        // 这里主要是对车位和住户是否能建立关系进行判断 (这里也对关联楼栋进行判断)
        handleOtherAttr(parCarRegisterVo, parCarRegisterExcel, currentRow);
        parCarRegisterVo.setSource("register");
        if (parCarRegisterVo.getPayment() == null) {
            parCarRegisterVo.setPayment(new BigDecimal("0"));
        }

        fixedThreadPool.execute(() -> {
            ProjectContextHolder.setProjectId(projectId);
            TenantContextHolder.setTenantId(tenantId);
            try {
                // 这里因为在子线程中无法获取到当前用户ID会导致空指针异常，所以需要手动传入
                projectParCarRegisterService.saveCarRegister(parCarRegisterVo, operatorId);
            } catch (Exception e) {
                saveErrorMessageMap(currentRow, e.getMessage());
                Object object = context.readRowHolder().getCurrentRowAnalysisResult();
                if (ObjectUtil.isNotEmpty(object)) {
                    saveErrorMap(currentRow, object);
                }
                e.printStackTrace();
            }
        });
//        projectParCarRegisterService.saveCarRegister(parCarRegisterVo);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        fixedThreadPool.shutdown();
        // 这里等待所有线程任务执行完成
        while (true) {
            if (fixedThreadPool.isTerminated()) {
                break;
            }
        }
        // 完全导入完成需要删除这些临时缓存的数据
        projectParkBillingRuleService.deleteParkingRuleTmpCache(this.parkingRuleRedisKey);
        projectPersonInfoService.deletePersonInfoTmpCache(this.personInfoRedisKey);
        projectParkingInfoService.deleteParkTmpCache(this.parkingRedisKey);
        setErrorException();
    }

    /**
     * <p>
     * 处理异常
     * </p>
     *
     * @param context   上下文
     * @param exception 异常对象
     * @author: 许亮亮
     */
    @Override
    public void onException(Exception exception, AnalysisContext context) {
        int row;
        String errorMessage = "";
        if (exception instanceof ExcelDataConvertException) {
            ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException) exception;
            row = excelDataConvertException.getRowIndex();
            errorMessage = "第" + excelDataConvertException.getColumnIndex() + "列数据解析异常";
        } else {
            errorMessage = exception.getMessage();
            row = context.readRowHolder().getRowIndex();
        }
        saveErrorMessageMap(row, errorMessage);
        Object object = context.readRowHolder().getCurrentRowAnalysisResult();
        if (ObjectUtil.isNotEmpty(object)) {
            saveErrorMap(row, object);
        }
        exception.printStackTrace();
    }

    /**
     * <p>
     * 做最后的判断工作
     * </p>
     *
     * @param parCarRegisterVo    车辆登记的vo对象
     * @param parCarRegisterExcel 车辆登记的Excel对象
     * @param currentRow          当前行
     * @author: 王良俊
     */
    private void handleOtherAttr(ProjectParCarRegisterVo parCarRegisterVo, ParCarRegisterExcel parCarRegisterExcel, Integer currentRow)
            throws ExcelAnalysisException {

        ProjectPersonInfo personInfo = projectPersonInfoService.getByTelephone(this.personInfoRedisKey, parCarRegisterExcel.getTelephone());

        if (personInfo != null) {
            parCarRegisterExcel.setCarOwner(personInfo.getPersonName());
        } else {
            personInfo = new ProjectPersonInfo();
            personInfo.setTelephone(parCarRegisterExcel.getTelephone());
            personInfo.setPersonName(parCarRegisterExcel.getCarOwner());
            projectPersonInfoService.addNewPersonInfoToTheCache(this.personInfoRedisKey, parCarRegisterExcel.getTelephone(), personInfo);
        }
        parCarRegisterVo.setPersonName(parCarRegisterExcel.getCarOwner());

        String parkId = projectParkingInfoService.getParkIdByParkName(this.parkingRedisKey, parCarRegisterExcel.getParkNameCh(), ProjectContextHolder.getProjectId());
        if (StrUtil.isNotEmpty(parkId)) {
            // 这里查询车场名称获取车场ID
            parCarRegisterVo.setParkId(parkId);
            parCarRegisterVo.setParkName(parCarRegisterExcel.getParkNameCh());
            parCarRegisterVo.setRelType(PlaceRelTypeEnum.PUBLIC.code);
        } else {
            throw new ExcelAnalysisException("车场名称：未找到停车场" + parCarRegisterExcel.getParkNameCh());
        }
        // 收费方式 这里未填写默认为免费车
        if (StrUtil.isBlank(parCarRegisterExcel.getRuleTypeCh())) {

            String ruleId = projectParkBillingRuleService.getRuleIdByRuleName(this.parkingRuleRedisKey, parCarRegisterVo.getParkId(), "免费车");
            if (StrUtil.isNotEmpty(ruleId)) {
                parCarRegisterVo.setRuleId(ruleId);
            } else {
                throw new ExcelAnalysisException("收费方式：该车场公共区域没有免费车收费方式，请填写指定收费方式");
            }
        } else {
            String ruleId = projectParkBillingRuleService.getRuleIdByRuleName(this.parkingRuleRedisKey, parCarRegisterVo.getParkId(), parCarRegisterExcel.getRuleTypeCh());
            if (StrUtil.isNotEmpty(ruleId)) {
                parCarRegisterVo.setRuleId(ruleId);
//                parCarRegisterVo.setMonthlyRent(ruleList.get(0).getMonthlyRent());

            } else {
                throw new ExcelAnalysisException("收费方式：未找到名为" + parCarRegisterExcel.getRuleTypeCh() + "的收费方式");
            }
        }

        // 这里对可能出现的重复情况进行判断

        // 现在已经不需要了
        /*parCarRegisterTelephoneRelHashMap.computeIfAbsent(parCarRegisterExcel.getTelephone(), k -> parCarRegisterExcel.getCarOwner());
        if (!parCarRegisterTelephoneRelHashMap.get(parCarRegisterExcel.getTelephone()).equals(parCarRegisterExcel.getCarOwner())) {
            throw new ExcelAnalysisException("手机号重复");
        }*/

        if (parCarRegisterPlateNumberHashMap.get(parCarRegisterExcel.getPlateNumber()) != null) {
            throw new ExcelAnalysisException("车牌号已登记车位");
        } else {
            parCarRegisterPlateNumberHashMap.put(parCarRegisterExcel.getPlateNumber(), "1");
        }

    }

    /**
     * <p>
     * 对这个对象中要用字典进行转换成code的属性进行处理
     * </p>
     *
     * @param parCarRegisterVo    车辆登记的vo对象
     * @param parCarRegisterExcel 车辆登记的Excel对象
     * @param currentRow          当前行
     * @author: 王良俊
     */
    private void handleDictAttr(ProjectParCarRegisterVo parCarRegisterVo, ParCarRegisterExcel parCarRegisterExcel, Integer currentRow) {

        // 车辆号牌种类
        DictResult plateTypeResult = DictUtil.getCodeByLabel(DictUtil.PLATE_TYPE, parCarRegisterExcel.getPlateTypeCh(),
                false,
                "车辆号牌种类");
        if (plateTypeResult.isSuccess()) {
            parCarRegisterVo.setPlateType(plateTypeResult.getCode());
        }
        String colorCode = DictUtil.getLabel(DictUtil.PLATE_TYPE_COLOR, parCarRegisterVo.getPlateType());
        parCarRegisterVo.setPlateColor(colorCode);
        // 车辆类型
        DictResult vehicleTypeResult = DictUtil.getCodeByLabel(DictUtil.VEHICLE_TYPE, parCarRegisterExcel.getVehicleTypeCh(),
                false,
                "车辆类型");
        if (plateTypeResult.isSuccess()) {
            parCarRegisterVo.setVehicleType(vehicleTypeResult.getCode());
        }
        // 车辆颜色
        if (StrUtil.isNotEmpty(parCarRegisterExcel.getVehicleColorCh())) {
            DictResult vehicleColorResult = DictUtil.getCodeByLabel(DictUtil.VEHICLE_COLOR, parCarRegisterExcel.getVehicleColorCh().replace("色", ""),
                    false,
                    "车辆颜色");
            if (plateTypeResult.isSuccess()) {
                parCarRegisterVo.setVehicleType(vehicleColorResult.getCode());
            }
        }
    }

    /**
     * <p>
     * 处理Excel中与时间有关的属性格式验证
     * </p>
     *
     * @param parCarRegisterVo    车辆登记的vo对象
     * @param parCarRegisterExcel 车辆登记的Excel对象
     * @param currentRow          当前行
     * @author: 王良俊
     */
    private void handleTimeAttr(ProjectParCarRegisterVo parCarRegisterVo, ParCarRegisterExcel parCarRegisterExcel, Integer currentRow) {

        // 有效期
        String validPeriodRange = parCarRegisterExcel.getValidPeriod();
        // 租赁开始时间
        LocalDate startTime = null;
        // 租赁结束时间
        LocalDate endTime = null;
        if (StrUtil.isBlank(validPeriodRange)) {
            startTime = LocalDate.now();
            endTime = timeFormat("有效期", "2035-01-01");
        } else if (StrUtil.isNotEmpty(validPeriodRange)) {
            validPeriodRange = validPeriodRange.replace(" ", "");
            String[] timeRange = validPeriodRange.split("至");
            if (ArrayUtil.isNotEmpty(timeRange) && timeRange.length == 2) {
                startTime = timeFormat("有效期", timeRange[0]);
                endTime = timeFormat("有效期", timeRange[1]);
            } else {
                throw new ExcelAnalysisException("有效期：未按照要求格式填写");
            }
        }
        parCarRegisterVo.setStartTime(startTime);
        parCarRegisterVo.setEndTime(endTime);
    }

    /**
     * <p>
     * 处理Excel中与手机号有关的属性
     * </p>
     *
     * @param parCarRegisterVo    车辆登记的vo对象
     * @param parCarRegisterExcel 车辆登记的Excel对象
     * @param currentRow          当前行
     * @author: 王良俊
     */
    private void handlePhoneNumberAttr(ProjectParCarRegisterVo parCarRegisterVo, ParCarRegisterExcel parCarRegisterExcel, Integer currentRow) {
        // 手机号格式校验
        String telephone = parCarRegisterExcel.getTelephone();
        if (!validPhone(telephone)) {
            throw new ExcelAnalysisException("手机号：格式错误");
        }
    }

    /**
     * <p>
     * 在这里统一对字符串类型的属性进行判断
     * </p>
     *
     * @param parCarRegisterVo    车辆登记的vo对象
     * @param parCarRegisterExcel 车辆登记的Excel对象
     * @param currentRow          当前行
     * @author: 王良俊
     */
    private void handleStrAttr(ProjectParCarRegisterVo parCarRegisterVo, ParCarRegisterExcel parCarRegisterExcel,
                               Integer currentRow) {
        // 车场名称
        if (StrUtil.isBlank(parCarRegisterExcel.getParkNameCh())) {
            throw new ExcelAnalysisException("车场名称：必填项未填写");
        }
        // 手机号
        if (StrUtil.isBlank(parCarRegisterExcel.getTelephone())) {
            throw new ExcelAnalysisException("手机号：必填项未填写");
        }
        // 车位类型
        if (StrUtil.isBlank(parCarRegisterExcel.getParkingSpaceTypeCh())) {
            throw new ExcelAnalysisException("车位类型：必填项未填写");
        }
        // 对车牌号进行格式校验并赋值
        parCarRegisterVo.setPlateNumber(validPlateNumber("车牌号", parCarRegisterExcel.getPlateNumber()));

        // 在登记的接口中已经判断了这里就不需要判断了
//        int registerNum = projectParCarRegisterService.count(new QueryWrapper<ProjectParCarRegister>().lambda()
//                .eq(ProjectParCarRegister::getPlateNumber, parCarRegisterVo.getPlateNumber()));
//        if (registerNum != 0) {
//            throw new ExcelAnalysisException("车牌号：车牌号已被登记过了");
//        }

        // 车位类型判断
        if (!"公共".equals(parCarRegisterExcel.getParkingSpaceTypeCh())) {
            throw new ExcelAnalysisException("车位类型：只支持公共车位导入");
        }
        // 车辆信息标识
        validStr("车辆信息标识", parCarRegisterExcel.getCarVinInfo(), 0, 128);
        // 车辆中文品牌名称
        validStr("车辆中文品牌名称", parCarRegisterExcel.getBrandName(), 0, 64);
        // 车辆型号
        validStr("车辆型号", parCarRegisterExcel.getVehicleModel(), 0, 64);
        // 车辆简要情况
        validStr("车辆简要情况", parCarRegisterExcel.getRemark(), 0, 128);
        // 车主
        validStr("车主", parCarRegisterExcel.getCarOwner(), 1, 8);

    }

    /**
     * <p>
     * 在这里统一对数值类型的属性进行判断
     * </p>
     *
     * @param parCarRegisterVo    车辆登记的vo对象
     * @param parCarRegisterExcel 车辆登记的Excel对象
     * @param currentRow          当前行
     * @author: 王良俊
     */
    private void handleNumberAttr(ProjectParCarRegisterVo parCarRegisterVo, ParCarRegisterExcel parCarRegisterExcel, Integer currentRow) {

        // 车辆长度
        validInt("车辆长度", parCarRegisterExcel.getLength(), 0, 32767);
        // 车辆宽度
        validInt("车辆宽度", parCarRegisterExcel.getWidth(), 0, 32767);
        // 车辆高度
        validInt("车辆高度", parCarRegisterExcel.getHeight(), 0, 32767);

    }

    /**
     * @author: 许亮亮
     */
    private void saveErrorMessageMap(Integer row, String message) {
        if (errorMessageMap.get(row) == null) {
            errorMessageMap.put(row, message);
        } else {
            message = errorMessageMap.get(row) + "," + message;
            errorMessageMap.replace(row, message);
        }
    }

    private void saveErrorMap(Integer row, Object object) {
        if (errorMap.get(row) == null) {
            errorMap.put(row, object);
        } else {
            errorMap.replace(row, object);
        }
    }

    /**
     * 保存错误文件信息
     *
     * @author: 许亮亮
     */
    private void setErrorException() {

        if (errorMessageMap.size() > 0) {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String path = parCarRegisterExcelEnum.getType() + "-" + uuid;
            List objects = new ArrayList<>(errorMap.values());
            String data = JSONUtil.toJsonStr(objects);
            redisTemplate.opsForValue().set(path, data);
            excelResultVo.setDescribe(errorMessageMap);
            excelResultVo.setPath(path);
        }
    }


    /**
     * <p>
     * 对时间格式进行转换如果格式错误则抛出ExcelAnalysisException异常
     * </p>
     *
     * @param columnName 所在列的列名
     * @param timeStr    要进行转换的时间字符串 （这里都是 yyyy-MM-dd格式的因为对象里面都是localDateTime所以转成yyyy-MM-dd HH:mm:ss格式）
     * @return LocalDate 日期对象
     * @throws ExcelAnalysisException Excel异常对象
     * @author: 王良俊
     */
    private LocalDate timeFormat(String columnName, String timeStr) {
        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(timeStr, dateTimeFormatter);
        } catch (Exception e) {
            throw new ExcelAnalysisException(columnName + "：时间格式错误");
        }
    }

    /**
     * <p>
     * 对手机号格式进行校验
     * </p>
     *
     * @param phoneNumber 要进行格式校验的手机号
     * @return 是否格式正确
     * @author: 王良俊
     */
    private boolean validPhone(String phoneNumber) {
        String phoneRule = "^1(3\\d|4[5-9]|5[0-35-9]|6[567]|7[0-8]|8\\d|9[0-35-9])\\d{8}$";
        Pattern compile = Pattern.compile(phoneRule);
        if (StrUtil.isNotBlank(phoneNumber)) {
            Matcher matcher = compile.matcher(phoneNumber);
            return matcher.matches();
        }
        return true;
    }

    /**
     * <p>
     * 对字符串长度进行判断 如果最小长度不为0则默认当做必填项
     * </p>
     *
     * @param columnName Excel中对应的列名
     * @param value      需要进行判断的字符串
     * @param minLength  字符串最小长度
     * @throws ExcelAnalysisException Excel异常类
     * @author: 王良俊
     */
    private void validStr(String columnName, String value, int minLength, int maxLength) {
        if (StrUtil.isBlank(value)) {
            if (minLength > 0) {
                throw new ExcelAnalysisException(columnName + "：必填项未填写");
            }
        } else if (value.length() < minLength) {
            throw new ExcelAnalysisException(columnName + "：长度最短为" + minLength);
        } else if (value.length() > maxLength) {
            throw new ExcelAnalysisException(columnName + "：长度不可超过" + maxLength);
        }
    }

    /**
     * <p>
     * 对字符串长度进行判断 如果最小长度不为0则默认当做必填项
     * </p>
     *
     * @param columnName Excel中对应的列名
     * @param value      需要进行判断的字符串
     * @param min        数值最小值
     * @param max        数值最大值
     * @throws ExcelAnalysisException Excel异常类
     * @author: 王良俊
     */
    private void validInt(String columnName, Integer value, int min, int max) {
        if (value == null) {
            if (min > 0) {
                throw new ExcelAnalysisException(columnName + "：必填项未填写");
            }
        } else if (value < min) {
            throw new ExcelAnalysisException(columnName + "：数值不可小于" + min);
        } else if (value > max) {
            throw new ExcelAnalysisException(columnName + "：数值不可大于" + max);
        }
    }

    /**
     * <p>
     * 对字符串长度进行判断 如果最小长度不为0则默认当做必填项
     * </p>
     *
     * @param columnName Excel中对应的列名
     * @throws ExcelAnalysisException Excel异常类
     * @author: 王良俊
     */
    private String validPlateNumber(String columnName, String plateNumber) {
        /**
         * 车牌号支持不带空格的情况
         * @since: 2020-09-30
         * @author: 王伟
         */

        if (StringUtil.isNotEmpty(plateNumber)) {
            plateNumber = plateNumber.replaceAll(" ", "");
            if ((plateNumber.trim().length() == 7 || plateNumber.trim().length() == 8) && !plateNumber.trim().contains(" ")) {//闽A12345
                StringBuilder sb = new StringBuilder(plateNumber.trim());
                sb.insert(2, " ");
                plateNumber = sb.toString();
            }
        }

        String plateNumberOtherRule = "^[0-9a-zA-Z]+$";
        Pattern compile = Pattern.compile(plateNumberOtherRule);
        // 这里对车牌号进行校验
        String plateNumberProvince = "";
        String plateNumberAlphabet = "";
        String plateNumberOther = "";
        if (StrUtil.isNotBlank(plateNumber)) {
            // 这里获取到的车牌号格式正常来说应该是 闽A 123456
            String[] plateNumberArrType1 = plateNumber.split(" ");
            if (plateNumberArrType1.length == 2) {
                // 第一个是空字符所以从1开始获取
                String[] plateNumberOne = plateNumberArrType1[0].split("");
                if (plateNumberArrType1[0].length() == 2) {
                    plateNumberProvince = plateNumberOne[0];
                    plateNumberAlphabet = plateNumberOne[1];
                }
                plateNumberOther = plateNumberArrType1[1];
            } else {
                // 如果未用空格分隔则在这里处理 这里的8是不包括空格车牌号应该有的长度
                if (plateNumber.length() == 8) {
                    String[] plateNumberArrType2 = plateNumber.split("");
                    plateNumberProvince = plateNumberArrType2[0];
                    plateNumberAlphabet = plateNumberArrType2[1];
                    plateNumberOther = plateNumber.substring(2, 5);
                } else if (plateNumber.length() == 9) {
                    String[] plateNumberArrType2 = plateNumber.split("");
                    plateNumberProvince = plateNumberArrType2[0];
                    plateNumberAlphabet = plateNumberArrType2[1];
                    plateNumberOther = plateNumber.substring(2, 6);
                }
            }
        }
        // 对车牌号是否填写正确进行判断
        if (StrUtil.isNotBlank(plateNumberProvince) && StrUtil.isNotBlank(plateNumberAlphabet) && StrUtil.isNotBlank(plateNumberOther)) {
            DictResult plateNumberProvinceResult = DictUtil.getCodeByLabel(DictUtil.PLATE_NUMBER_PROVINCE, plateNumberProvince, true, columnName);
            DictResult plateNumberAlphabetResult = DictUtil.getCodeByLabel(DictUtil.PLATE_NUMBER_ALPHABET, plateNumberAlphabet, true, columnName);
            if (plateNumberProvinceResult.isSuccess()) {
                plateNumberProvince = plateNumberProvinceResult.getCode();
            }
            if (plateNumberAlphabetResult.isSuccess()) {
                plateNumberAlphabet = plateNumberAlphabetResult.getCode();
            }
            Matcher matcher = compile.matcher(plateNumberOther);
            if (matcher.matches() && (plateNumberOther.length() == 5 || plateNumberOther.length() == 6)) {
                return plateNumberProvince + plateNumberAlphabet + plateNumberOther.toUpperCase();
            }
        }
        throw new ExcelAnalysisException(columnName + "：格式错误请检查是否填写正确");
    }

}
