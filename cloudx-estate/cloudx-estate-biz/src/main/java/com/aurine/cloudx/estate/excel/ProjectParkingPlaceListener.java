package com.aurine.cloudx.estate.excel;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.CellExtra;
import com.aurine.cloudx.estate.constant.DeviceExcelConstant;
import com.aurine.cloudx.estate.constant.enums.ParkingManageRelExcelEnum;
import com.aurine.cloudx.estate.dict.DictResult;
import com.aurine.cloudx.estate.dict.DictUtil;
import com.aurine.cloudx.estate.entity.ProjectParkingPlace;
import com.aurine.cloudx.estate.entity.ProjectPersonInfo;
import com.aurine.cloudx.estate.excel.parking.ParkingManageRelExcel;
import com.aurine.cloudx.estate.service.ProjectFrameInfoService;
import com.aurine.cloudx.estate.service.ProjectParkingPlaceManageService;
import com.aurine.cloudx.estate.service.ProjectPersonInfoService;
import com.aurine.cloudx.estate.vo.ExcelResultVo;
import com.aurine.cloudx.estate.vo.ProjectParkingPlaceManageVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: ProjectParkingPlaceListener
 * @author: 王良俊 <>
 * @date:  2020年08月25日 上午11:46:32
 * @Copyright:
*/
public class ProjectParkingPlaceListener<T> extends AnalysisEventListener<T> {

    ProjectFrameInfoService projectFrameInfoService;
    ProjectParkingPlaceManageService projectParkingPlaceManageService;
    ProjectPersonInfoService projectPersonInfoService;

    /**
     * 一次读取多少条记录（影响内存占用）
     */
    private static final int BATCH_COUNT = 3000;

    /**
     * 车位和人关系的vo对象列表
     */
    private final List<ProjectParkingPlaceManageVo> parkingPlaceManageVoList = new ArrayList<>();

    /**
     * 导入错误信息列表
     */
    HashMap<Integer, String> errorMessageMap = new HashMap<>();

    /**
     * 导入数据列表
     */
    HashMap<Integer, Object> errorMap = new HashMap<>();

    HashMap<String,Object> parkingPlaceRelMap = new HashMap<>();

    HashMap<String,Object> parkingPlaceTelephoneMap = new HashMap<>();

    // 用来判断是否需要在查询地址的时候加上组团信息
    boolean isGroup;

    // private final AddressParseUtil addressParseUtil = new AddressParseUtil();

    /**
     * 住户迁入Excel表格枚举类型
     */
    private final ParkingManageRelExcelEnum parkingManageRelExcelEnum;

    /**
     * Excel处理结果
     */
    private final ExcelResultVo excelResultVo;

    RedisTemplate<String, String> redisTemplate;

    public ProjectParkingPlaceListener(boolean isGroup,
                                       ProjectFrameInfoService projectFrameInfoService,
                                       ProjectParkingPlaceManageService projectParkingPlaceManageService,
                                       ProjectPersonInfoService projectPersonInfoService,
                                       ParkingManageRelExcelEnum parkingManageRelExcelEnum,
                                       ExcelResultVo excelResultVo,
                                       RedisTemplate<String, String> redisTemplate) {
        this.projectFrameInfoService = projectFrameInfoService;
        this.projectParkingPlaceManageService = projectParkingPlaceManageService;
        this.projectPersonInfoService = projectPersonInfoService;
        this.parkingManageRelExcelEnum = parkingManageRelExcelEnum;
        this.excelResultVo = excelResultVo;
        this.redisTemplate = redisTemplate;
        this.isGroup = isGroup;
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
        ParkingManageRelExcel parkingManageRelExcel = new ParkingManageRelExcel();
        BeanUtil.copyProperties(data, parkingManageRelExcel);
        ProjectParkingPlaceManageVo parkingPlaceManageVo = new ProjectParkingPlaceManageVo();
        BeanUtil.copyProperties(parkingManageRelExcel, parkingPlaceManageVo);
        Integer currentRow = context.readRowHolder().getRowIndex();

        // 这里主要是对车位和住户是否能建立关系进行判断 (这里也对关联楼栋进行判断)
        handleOtherAttr(parkingPlaceManageVo, parkingManageRelExcel, currentRow);

        // 统一处理parkingManageRelExcel面需要使用字典进行转换的属性
        handleStrAttr(parkingPlaceManageVo, parkingManageRelExcel, currentRow);

        // 统一处理parkingManageRelExcel面需要使用字典进行转换的属性
        handleDictAttr(parkingPlaceManageVo, parkingManageRelExcel, currentRow);

        // 统一处理身份证格式验证
        handleIdCardAttr(parkingPlaceManageVo, parkingManageRelExcel, currentRow);

        // 统一处理时间格式验证
        handleTimeAttr(parkingPlaceManageVo, parkingManageRelExcel, currentRow);

        // 统一处理手机号格式验证
        handlePhoneNumberAttr(parkingPlaceManageVo, parkingManageRelExcel, currentRow);

        this.parkingPlaceManageVoList.add(parkingPlaceManageVo);

        if (parkingPlaceManageVoList.size() >= BATCH_COUNT) {
            saveData();
            parkingPlaceManageVoList.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        if (parkingPlaceManageVoList.size() > 0) {
            saveData();
            parkingPlaceManageVoList.clear();
        }
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
     * @param parkingPlaceManageVo  车位和人关系的vo对象
     * @param parkingManageRelExcel 车位和人的Excel对象
     * @param currentRow            当前行
     * @author: 王良俊
     */
    private void handleOtherAttr(ProjectParkingPlaceManageVo parkingPlaceManageVo, ParkingManageRelExcel parkingManageRelExcel, Integer currentRow)
            throws ExcelAnalysisException {
        ProjectPersonInfo personInfo = projectPersonInfoService.getByTelephone(parkingManageRelExcel.getTelephone());
        String placeId = projectParkingPlaceManageService.checkParkingPlaceIsCorrect(
                parkingManageRelExcel.getParkName()
                , parkingManageRelExcel.getRegionName()
                , parkingManageRelExcel.getPlaceCode());
        if (personInfo != null) {
            BeanUtil.copyProperties(personInfo, parkingPlaceManageVo);
        }
        List<ProjectParkingPlace> placeList = projectParkingPlaceManageService.list(new QueryWrapper<ProjectParkingPlace>().lambda()
                .eq(ProjectParkingPlace::getPlaceId, placeId));
        if (CollUtil.isNotEmpty(placeList) && StrUtil.isNotBlank(placeList.get(0).getPersonId())) {
            throw new ExcelAnalysisException("车位已占用");
        }
        parkingPlaceManageVo.setPlaceId(placeId);
        String houseAddressCh = parkingManageRelExcel.getHouseAddressCh();
        if (StrUtil.isNotBlank(houseAddressCh)) {
            String[] addressArr = houseAddressCh.split("-");
            if (addressArr.length < 3) {
                throw new ExcelAnalysisException("关联房屋地址格式错误未使用\"-\"分隔");
            } else {
                R<String> result = projectFrameInfoService.checkHouseIsCorrect(houseAddressCh, isGroup);
                if (R.ok().getCode() == result.getCode()) {
                    parkingPlaceManageVo.setHouseId(result.getData());
                } else {
                    throw new ExcelAnalysisException("楼栋/单元/房屋填写错误 系统无法找到对应房屋");
                }
            }
        }

        // 同一个车位不能同时归属于两个人
        if (parkingPlaceRelMap.get(placeId) == null) {
            parkingPlaceRelMap.put(placeId,"1");
        } else {
            throw new ExcelAnalysisException("车位已占用");
        }
        // 用来判断人员是否和手机号对应的不符 现在已经不需要了
        /*parkingPlaceTelephoneMap.computeIfAbsent(parkingManageRelExcel.getTelephone(), k -> parkingPlaceManageVo.getPersonName());
        if (!parkingPlaceTelephoneMap.get(parkingManageRelExcel.getTelephone()).equals(parkingPlaceManageVo.getPersonName())) {
            throw new ExcelAnalysisException("手机号重复");
        }*/
    }

    /**
     * <p>
     * 对这个对象中要用字典进行转换成code的属性进行处理
     * </p>
     *
     * @param parkingPlaceManageVo  车位和人关系的vo对象
     * @param parkingManageRelExcel 车位和人的Excel对象
     * @param currentRow            当前行
     * @author: 王良俊
     */
    private void handleDictAttr(ProjectParkingPlaceManageVo parkingPlaceManageVo, ParkingManageRelExcel parkingManageRelExcel, Integer currentRow) {

        // 性别
        DictResult genderResult = DictUtil.getCodeByLabel(DictUtil.GENDER_TYPE, parkingManageRelExcel.getGenderCh()
                , ParkingManageRelExcelEnum.PARKING_PUBLIC.getType().equals(parkingManageRelExcelEnum.getType())
                , "性别");
        if (genderResult.isSuccess()) {
            parkingPlaceManageVo.setGender(genderResult.getCode());
        }

        // 证件类型
        DictResult credentialTypeResult = DictUtil.getCodeByLabel(DictUtil.CREDENTIAL_TYPE, parkingManageRelExcel.getCredentialTypeCh()
                , false
                , "证件类型");
        if (credentialTypeResult.isSuccess()) {
            parkingPlaceManageVo.setCredentialType(credentialTypeResult.getCode());
        }

        // 归属类型
        DictResult relTypeResult = DictUtil.getCodeByLabel(DictUtil.ATTRIBUTION_TYPE, parkingManageRelExcel.getRelTypeCh()
                , true
                , "归属类型");
        if (relTypeResult.isSuccess()) {
            parkingPlaceManageVo.setRelType(relTypeResult.getCode());
        }

        // 委托代理人证件类型
        DictResult dlrzjlxResult = DictUtil.getCodeByLabel(DictUtil.CREDENTIAL_TYPE, parkingManageRelExcel.getDlrzjlxCh()
                , false
                , "委托代理人证件类型");
        if (dlrzjlxResult.isSuccess()) {
            parkingPlaceManageVo.setDlrzjlx(dlrzjlxResult.getCode());
        }

    }


    /**
     * <p>
     * 处理身份证相关的格式验证
     * </p>
     *
     * @param parkingPlaceManageVo  车位和人关系的vo对象
     * @param parkingManageRelExcel 车位和人的Excel对象
     * @param currentRow            当前行
     * @author: 王良俊
     */
    private void handleIdCardAttr(ProjectParkingPlaceManageVo parkingPlaceManageVo, ParkingManageRelExcel parkingManageRelExcel, Integer currentRow) {
        String credentialName = "居民身份证";

        // 证件号
        if (credentialName.equals(parkingManageRelExcel.getCredentialTypeCh())) {
            if (StrUtil.isNotBlank(parkingManageRelExcel.getCredentialNo()) && !IdcardUtil.isValidCard(parkingManageRelExcel.getCredentialNo())) {
                throw new ExcelAnalysisException("证件号：格式错误");
            }
        }

        // 委托代理人证件号
        if (credentialName.equals(parkingManageRelExcel.getDlrzjlxCh())) {
            if (StrUtil.isNotBlank(parkingManageRelExcel.getDlrzjhm()) && !IdcardUtil.isValidCard(parkingManageRelExcel.getDlrzjhm())) {
                throw new ExcelAnalysisException("委托代理人证件号：格式错误");
            }
        }

    }

    /**
     * <p>
     * 处理Excel中与时间有关的属性格式验证
     * </p>
     *
     * @param parkingPlaceManageVo  车位和人关系的vo对象
     * @param parkingManageRelExcel 车位和人的Excel对象
     * @param currentRow            当前行
     * @author: 王良俊
     */
    private void handleTimeAttr(ProjectParkingPlaceManageVo parkingPlaceManageVo, ParkingManageRelExcel parkingManageRelExcel, Integer currentRow) {
        // 租赁期限
        String rentTime = parkingManageRelExcel.getRentTime();
        LocalDateTime effTime = null;
        LocalDateTime expTime = null;
        LocalDateTime checkInTime;
        if (StrUtil.isNotEmpty(rentTime)) {
            rentTime = rentTime.replace(" ","");
            String[] timeRange = rentTime.split("至");
            if (ArrayUtil.isNotEmpty(timeRange) && timeRange.length == 2) {
                effTime = timeFormat("租赁期限", timeRange[0]);
                expTime = timeFormat("租赁期限", timeRange[1]);
                Duration between = Duration.between(effTime, expTime);
                if (between.toDays() < 0) {
                    throw new ExcelAnalysisException("租赁期限：起始时间不能晚于结束时间");
                }
                parkingPlaceManageVo.setEffTime(effTime);
                parkingPlaceManageVo.setExpTime(expTime);
            } else {
                throw new ExcelAnalysisException("租赁期限：时间格式错误");
            }
        } else {
            // 这里如果是租客则租赁期限是必填项
            if ("租赁".equals(parkingManageRelExcel.getRelTypeCh())) {
                throw new ExcelAnalysisException("租赁期限：未填写");
            }
        }

        // 启用时间
        if (StrUtil.isNotBlank(parkingManageRelExcel.getCheckInTimeStr())) {
            checkInTime = timeFormat("启用时间", parkingManageRelExcel.getCheckInTimeStr());
            parkingPlaceManageVo.setCheckInTime(checkInTime);
            if (effTime != null) {
                Duration effTimeCheckInBetween = Duration.between(effTime, checkInTime);
                Duration expTimeCheckInBetween = Duration.between(expTime, checkInTime);
                long l = effTimeCheckInBetween.toDays();
                if (l < 0) {
                    throw new ExcelAnalysisException("租赁期限：租赁起始时间不能晚于启用时间");
                } else if (expTimeCheckInBetween.toDays() > 0) {
                    throw new ExcelAnalysisException("租赁期限：租赁结束时间时间不能早于启用时间");
                }
            }
        }

    }

    /**
     * <p>
     * 处理Excel中与手机号有关的属性
     * </p>
     *
     * @param parkingPlaceManageVo  车位和人关系的vo对象
     * @param parkingManageRelExcel 车位和人的Excel对象
     * @param currentRow            当前行
     * @author: 王良俊
     */
    private void handlePhoneNumberAttr(ProjectParkingPlaceManageVo parkingPlaceManageVo, ParkingManageRelExcel parkingManageRelExcel, Integer currentRow) {
        if (!phoneValid(parkingManageRelExcel.getTelephone())) {
            throw new ExcelAnalysisException("手机号：格式错误");
        }
    }

    /**
     * <p>
     * 在这里统一对字符串类型的属性进行判断
     * </p>
     *
     * @param parkingPlaceManageVo  车位和人关系的vo对象
     * @param parkingManageRelExcel 车位和人的Excel对象
     * @param currentRow            当前行
     * @author: 王良俊
     */
    private void handleStrAttr(ProjectParkingPlaceManageVo parkingPlaceManageVo, ParkingManageRelExcel parkingManageRelExcel,
                             Integer currentRow) {
        // 车场名称 字段
        validStr("车场名称", parkingManageRelExcel.getParkName(), 1, 128);
        // 车场区域名称 字段
        validStr("车场区域名称", parkingManageRelExcel.getRegionName(), 1, 64);
        // 车位号 字段
        validStr("车位号", parkingManageRelExcel.getPlaceCode(), 1, 64);
        // 姓名 字段
        validStr("姓名", parkingManageRelExcel.getPersonName(), 1, 8);
        // 手机号 字段
        validStr("手机号", parkingManageRelExcel.getTelephone(), 1, 32);
        // 证件号 字段
        validStr("证件号", parkingManageRelExcel.getCredentialNo(), 0, 32);
        // 委托代理人姓名 字段
        validStr("委托代理人姓名", parkingManageRelExcel.getWtdlrxm(), 0, 8);
        // 委托代理人电话 字段
        validStr("委托代理人电话", parkingManageRelExcel.getDlrlxdh(), 0, 20);
        // 委托代理人证件号码 字段
        validStr("委托代理人证件号码", parkingManageRelExcel.getDlrzjhm(), 0, 32);
    }

    /**
     * <p>
     * 进行数据保存操作
     * </p>
     */
    protected void saveData() {
        if (CollUtil.isNotEmpty(parkingPlaceManageVoList)) {
            projectParkingPlaceManageService.saveBatch(parkingPlaceManageVoList);
        }
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
            String path = parkingManageRelExcelEnum.getType() + "-" + uuid;
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
     * @return LocalDateTime 本地日期时间对象
     * @throws ExcelAnalysisException Excel异常对象
     * @author: 王良俊
     */
    private LocalDateTime timeFormat(String columnName, String timeStr) {
        try {
            timeStr += " 00:00:00";
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return LocalDateTime.parse(timeStr, dateTimeFormatter);
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
    private boolean phoneValid(String phoneNumber) {
        String phoneRule = "^1(3\\d|4[5-9]|5[0-35-9]|6[567]|7[0-8]|8\\d|9[0-35-9])\\d{8}$";
        Pattern compile = Pattern.compile(phoneRule);
        Matcher matcher = compile.matcher(phoneNumber);
        return matcher.matches();
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

}
