package com.aurine.cloudx.estate.excel;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.CellExtra;
import com.aurine.cloudx.estate.address.Address;
import com.aurine.cloudx.estate.address.AddressParseUtil;
import com.aurine.cloudx.estate.constant.enums.HousePersonRelExcelEnum;
import com.aurine.cloudx.estate.dict.DictResult;
import com.aurine.cloudx.estate.dict.DictUtil;
import com.aurine.cloudx.estate.entity.ProjectPersonInfo;
import com.aurine.cloudx.estate.excel.person.HousePersonRelExcel;
import com.aurine.cloudx.estate.service.ProjectFrameInfoService;
import com.aurine.cloudx.estate.service.ProjectHousePersonRelService;
import com.aurine.cloudx.estate.service.ProjectPersonInfoService;
import com.aurine.cloudx.estate.vo.ExcelResultVo;
import com.aurine.cloudx.estate.vo.ProjectHousePersonRelVo;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: ProjectHousePersonRelListener
 * @author: 王良俊 <>
 * @date: 2020年08月14日 下午15:32:05
 * @Copyright:
 */
public class ProjectHousePersonRelListener<T> extends AnalysisEventListener<T> {

    ProjectFrameInfoService projectFrameInfoService;
    ProjectHousePersonRelService projectHousePersonRelService;
    ProjectPersonInfoService projectPersonInfoService;

    /**
     * 一次读取多少条记录（影响内存占用）
     */
    private static final int BATCH_COUNT = 3000;

    /**
     * 最后要写入数据库的列表
     */
    private final List<ProjectHousePersonRelVo> housePersonRelVoList = new ArrayList<>();

    /**
     * 最后需要设置增值服务的人屋关系
     */
    private final List<ProjectHousePersonRelVo> personRelVoList = new ArrayList<>();
    /**
     * 导入错误信息列表
     */
    HashMap<Integer, String> errorMessageMap = new HashMap<>();

    /**
     * 导入数据列表
     */
    HashMap<Integer, Object> errorMap = new HashMap<>();

    /**
     * 住户迁入Excel表格枚举类型
     */
    private final HousePersonRelExcelEnum housePersonRelExcelEnum;

    /**
     * Excel处理结果
     */
    private final ExcelResultVo excelResultVo;

    /**
     * 已存在房屋户主
     */
    HashMap<String, Object> houseOwnerHashMap = new HashMap<>();

    /**
     * 一个房屋下，只能有一个电话号码
     */
    HashMap<String, Object> houseTelephoneHashMap = new HashMap<>();

    /**
     * 一个手机号对应的住户名不能是不一样的
     */
    HashMap<String, Object> telephonePersonHashMap = new HashMap<>();

    RedisTemplate<String, String> redisTemplate;

    String uuid = UUID.randomUUID().toString().replaceAll("-", "");
    String addressRedisKey = "address" + uuid;
    String housePersonRedisKey = "housePerson" + uuid;

    // 用来判断是否需要在查询地址的时候加上组团信息
    boolean isGroup;

    public ProjectHousePersonRelListener(boolean isGroup,
                                         ProjectFrameInfoService projectFrameInfoService,
                                         ProjectHousePersonRelService projectHousePersonRelService,
                                         ProjectPersonInfoService projectPersonInfoService,
                                         HousePersonRelExcelEnum housePersonRelExcelEnum,
                                         ExcelResultVo excelResultVo,
                                         RedisTemplate<String, String> redisTemplate) {
        this.projectFrameInfoService = projectFrameInfoService;
        this.projectHousePersonRelService = projectHousePersonRelService;
        this.projectPersonInfoService = projectPersonInfoService;
        this.housePersonRelExcelEnum = housePersonRelExcelEnum;
        this.excelResultVo = excelResultVo;
        this.redisTemplate = redisTemplate;
        this.isGroup = isGroup;
    }

    /**
     * <p>
     * 读取每一行excel都会执行这个方法
     * </p>
     *
     * @param context 上下文 获取行数信息
     * @param data    每一行的数据
     */
    @Override
    public void invoke(T data, AnalysisContext context) {
        HousePersonRelExcel housePersonRelExcel = new HousePersonRelExcel();
        BeanUtil.copyProperties(data, housePersonRelExcel);
        ProjectHousePersonRelVo housePersonRelVo = new ProjectHousePersonRelVo();
        BeanUtil.copyProperties(housePersonRelExcel, housePersonRelVo);
        Integer currentRow = context.readRowHolder().getRowIndex();

        // 统一处理housePersonRelExcel面需要使用字典进行转换的属性
        handleStrAttr(housePersonRelVo, housePersonRelExcel, currentRow);

        // 统一处理housePersonRelExcel面需要使用字典进行转换的属性
        handleDictAttr(housePersonRelVo, housePersonRelExcel, currentRow);

        // 统一处理身份证格式验证
        handleIdCardAttr(housePersonRelVo, housePersonRelExcel, currentRow);

        // 统一处理时间格式验证
        handleTimeAttr(housePersonRelVo, housePersonRelExcel, currentRow);

        // 统一处理地址格式验证
        handleAddressAttr(housePersonRelVo, housePersonRelExcel, currentRow);

        // 统一处理手机号格式验证
        handlePhoneNumberAttr(housePersonRelVo, housePersonRelExcel, currentRow);

        // 这里主要是对住户与房屋之间是否合理进行判断
        handleOtherAttr(housePersonRelVo, housePersonRelExcel, currentRow);
        this.housePersonRelVoList.add(housePersonRelVo);

        if (housePersonRelVoList.size() >= BATCH_COUNT) {
            saveData();
            personRelVoList.addAll(housePersonRelVoList);
            housePersonRelVoList.clear();

            // 这里因为无法知道具体哪些住户导入失败了，就在每次导入完成后重新获取最新的人屋关系
            projectHousePersonRelService.initHousePersonNumMapping(housePersonRedisKey);

        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        if (housePersonRelVoList.size() > 0) {
            saveData();
            personRelVoList.addAll(housePersonRelVoList);
            excelResultVo.setProjectHousePersonRelVos(personRelVoList);
            housePersonRelVoList.clear();
            projectFrameInfoService.deleteHouseAddressCache(addressRedisKey);
            projectHousePersonRelService.deleteHousePersonNumCache(housePersonRedisKey);
        }
        setErrorException();
    }

    @Override
    public void invokeHead(Map<Integer, CellData> headMap, AnalysisContext context) {
        super.invokeHead(headMap, context);
    }

    @Override
    public void extra(CellExtra extra, AnalysisContext context) {
        super.extra(extra, context);
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
        Integer row;
        String errorMessage;
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
     * 判断是否有下一行的数据
     * </p>
     *
     * @param context 上下文对象
     */
    @Override
    public boolean hasNext(AnalysisContext context) {
        return super.hasNext(context);
    }

    /**
     * <p>
     * 做最后的判断工作
     * </p>
     *
     * @param housePersonRelVo    人屋关系vo对象
     * @param housePersonRelExcel 人屋关系Excel对象
     * @param currentRow          当前行
     * @author: 王良俊
     */
    private void handleOtherAttr(ProjectHousePersonRelVo housePersonRelVo, HousePersonRelExcel housePersonRelExcel, Integer currentRow) throws ExcelAnalysisException {


        // 这里检查住户房屋地址是否有问题
        /*String houseId = projectFrameInfoService.checkHouseIsCorrect("住户房屋地址",
                housePersonRelExcel.getBuildingName() + "-" + housePersonRelExcel.getUnitName() + "-" + housePersonRelExcel.getHouseNo(), isGroup);*/
        String houseId = projectFrameInfoService.checkHouseIsCorrect( addressRedisKey,
                housePersonRelExcel.getBuildingName() + "-" + housePersonRelExcel.getUnitName() + "-" + housePersonRelExcel.getHouseNo());

        if (StrUtil.isEmpty(houseId)) {
            throw new ExcelAnalysisException("无法找到该房屋");
        }
        ProjectPersonInfo personInfo = projectPersonInfoService.getByTelephone(housePersonRelVo.getTelephone());
        if (personInfo != null) {
            Long personNum = projectHousePersonRelService.incrementPersonNum(housePersonRedisKey, houseId, personInfo.getPersonId());
            if (personNum != 1L) {
                throw new ExcelAnalysisException("住户已存在");
            }

            BeanUtil.copyProperties(personInfo, housePersonRelVo);
        }

        // 现在已经不需要了
        /*telephonePersonHashMap.computeIfAbsent(housePersonRelExcel.getTelephone(), k -> housePersonRelExcel.getPersonName());
        if (!telephonePersonHashMap.get(housePersonRelExcel.getTelephone()).equals(housePersonRelExcel.getPersonName())) {
            throw new ExcelAnalysisException("手机号重复");
        }*/

        /**
         * 同一个屋子，电话相同的住户只能出现一次
         * @author: 王伟
         * @since :2020-09-28
         */
        if (this.houseTelephoneHashMap.get(houseId + "_" + housePersonRelExcel.getTelephone()) != null) {
            throw new ExcelAnalysisException("住户重复");
        }
        this.houseTelephoneHashMap.put(houseId + "_" + housePersonRelExcel.getTelephone(), "1");

        if ("业主".equals(housePersonRelExcel.getHouseholdTypeCh())) {

            if (projectHousePersonRelService.haveOwner(houseId) || houseOwnerHashMap.get(houseId) != null) {
                throw new ExcelAnalysisException("业主已存在");
            } else {
                houseOwnerHashMap.put(houseId, "1");
            }
        }
        housePersonRelVo.setHouseId(houseId);
    }

    /**
     * <p>
     * 对这个对象中要用字典进行转换成code的属性进行处理
     * </p>
     *
     * @param housePersonRelVo    人屋关系vo对象
     * @param housePersonRelExcel 人屋关系Excel对象
     * @param currentRow          当前行
     * @author: 王良俊
     */
    private void handleDictAttr(ProjectHousePersonRelVo housePersonRelVo, HousePersonRelExcel housePersonRelExcel, Integer currentRow) {

        // 性别
        DictResult genderResult = DictUtil.getCodeByLabel(DictUtil.GENDER_TYPE, housePersonRelExcel.getGenderCh(),
                HousePersonRelExcelEnum.RESIDENT_PUBLIC.getType().equals(housePersonRelExcelEnum.getType()), "性别");
        if (genderResult.isSuccess()) {
            housePersonRelVo.setGender(genderResult.getCode());
        }

        // 国籍
        DictResult nationalityResult = DictUtil.getCodeByLabel(DictUtil.NATIONALITY_CODE, housePersonRelExcel.getNationalityNameCh()
                , HousePersonRelExcelEnum.RESIDENT_PUBLIC.getType().equals(housePersonRelExcelEnum.getType()), "国籍");
        if (nationalityResult.isSuccess()) {
            housePersonRelVo.setNationalityCode(nationalityResult.getCode());
        }

        // 证件类型（住户）
        DictResult credentialTypeResult = DictUtil.getCodeByLabel(DictUtil.CREDENTIAL_TYPE, housePersonRelExcel.getCredentialTypeCh()
                , HousePersonRelExcelEnum.RESIDENT_PUBLIC.getType().equals(housePersonRelExcelEnum.getType()), "证件类型");
        if (credentialTypeResult.isSuccess()) {
            housePersonRelVo.setCredentialType(credentialTypeResult.getCode());
        }

        // 是否是重点人员
        DictResult focusResult = DictUtil.getCodeByLabel(DictUtil.IS_FOCUS_PERSON, housePersonRelExcel.getIsFocusPersonCh(),
                true, "是否重点人员");
        if (focusResult.isSuccess()) {
            housePersonRelVo.setIsFocusPerson(focusResult.getCode());
        }

        // 住户类型
        DictResult householdTypeResult = DictUtil.getCodeByLabel(DictUtil.HOUSEHOLD_TYPE, housePersonRelExcel.getHouseholdTypeCh()
                , true, "住户类型");
        if (householdTypeResult.isSuccess()) {
            housePersonRelVo.setHouseholdType(householdTypeResult.getCode());
        }

        // 家庭成员关系
        DictResult memberTypeResult = DictUtil.getCodeByLabel(DictUtil.MEMBER_TYPE, housePersonRelExcel.getMemberTypeCh()
                , "家属".equals(housePersonRelExcel.getHouseholdTypeCh()), "关系");
        if (memberTypeResult.isSuccess()) {
            housePersonRelVo.setMemberType(memberTypeResult.getCode());
        }

        // 民族
        DictResult nationResult = DictUtil.getCodeByLabel(DictUtil.NATION_CODE, housePersonRelExcel.getNationCh()
                , false, "民族");
        if (nationResult.isSuccess()) {
            housePersonRelVo.setNationCode(nationResult.getCode());
        }

        // 文化程度
        DictResult educationResult = DictUtil.getCodeByLabel(DictUtil.EDUCATION_CODE, housePersonRelExcel.getEducationCh()
                , false, "文化程度");
        if (educationResult.isSuccess()) {
            housePersonRelVo.setEducationCode(educationResult.getCode());
        }

        // 特殊身份
        DictResult specialIdentityResult = DictUtil.getCodeByLabel(DictUtil.SPECIAL_IDENTITY, housePersonRelExcel.getSpecialIdentityCh()
                , false, "特殊身份");
        if (specialIdentityResult.isSuccess()) {
            housePersonRelVo.setSpecialIdentity(specialIdentityResult.getCode());
        }

        // 政治面貌
        DictResult politicalStatusResult = DictUtil.getCodeByLabel(DictUtil.POLITICAL_STATUS, housePersonRelExcel.getPoliticalStatusCh()
                , false, "政治面貌");
        if (politicalStatusResult.isSuccess()) {
            housePersonRelVo.setPoliticalStatus(politicalStatusResult.getCode());
        }

        // 宗教信仰
        DictResult religiousBeliefResult = DictUtil.getCodeByLabel(DictUtil.RELIGIOUS_BELIEF, housePersonRelExcel.getReligiousBeliefCh()
                , false, "宗教信仰");
        if (religiousBeliefResult.isSuccess()) {
            housePersonRelVo.setReligiousBelief(religiousBeliefResult.getCode());
        }

        // 婚姻状况
        DictResult maritalStatusCodeResult = DictUtil.getCodeByLabel(DictUtil.MARITAL_STATUS, housePersonRelExcel.getMaritalStatusCodeCh()
                , false, "婚姻状况");
        if (maritalStatusCodeResult.isSuccess()) {
            housePersonRelVo.setMaritalStatusCode(maritalStatusCodeResult.getCode());
        }

        // 配偶证件类型
        DictResult spouseIdTypeResult = DictUtil.getCodeByLabel(DictUtil.CREDENTIAL_TYPE, housePersonRelExcel.getSpouseIdTypeCh()
                , false, "配偶证件类型");
        if (spouseIdTypeResult.isSuccess()) {
            housePersonRelVo.setSpouseIdType(spouseIdTypeResult.getCode());
        }

        // 人员类别
        DictResult peopleTypeResult = DictUtil.getCodeByLabel(DictUtil.PEOPLE_MOBILE_TYPE, housePersonRelExcel.getPeopleTypeCh()
                , false, "人员类别");
        if (peopleTypeResult.isSuccess()) {
            housePersonRelVo.setPeopleTypeCode(peopleTypeResult.getCode());
        }

        // 户类型
        DictResult hlxResult = DictUtil.getCodeByLabel(DictUtil.RESIDENCE_TYPE, housePersonRelExcel.getHlxCh()
                , false, "户类型");
        if (hlxResult.isSuccess()) {
            housePersonRelVo.setHlx(hlxResult.getCode());
        }

        // 户口性质分类
        DictResult hkxzflResult = DictUtil.getCodeByLabel(DictUtil.RESIDENCE_CATEGORY, housePersonRelExcel.getHkxzflCh()
                , false, "户口性质分类");
        if (hkxzflResult.isSuccess()) {
            housePersonRelVo.setHkxz(hkxzflResult.getCode());
        }

        // 单位类别
        DictResult employerTypeResult = DictUtil.getCodeByLabel(DictUtil.EMPLOYER_TYPE, housePersonRelExcel.getEmployerTypeCh()
                , false, "单位类别");
        if (employerTypeResult.isSuccess()) {
            housePersonRelVo.setEmployerType(employerTypeResult.getCode());
        }

        // 代理人证件类型
        DictResult dlrzjlxResult = DictUtil.getCodeByLabel(DictUtil.CREDENTIAL_TYPE, housePersonRelExcel.getDlrzjlxCh()
                , false, "代理人证件类型");
        if (dlrzjlxResult.isSuccess()) {
            housePersonRelVo.setDlrzjlx(dlrzjlxResult.getCode());
        }

        // 重点人员管理类别
        String focusCategoryCh = housePersonRelExcel.getFocusCategoryCh();
        StringBuilder focusCategory = new StringBuilder();
        if (StrUtil.isNotEmpty(focusCategoryCh)) {
            String[] focusCategoryArr = focusCategoryCh.split("，");
            if (ArrayUtil.isNotEmpty(focusCategoryArr)) {
                Arrays.stream(focusCategoryArr).forEach(category -> {
                    DictResult focusCategoryResult = DictUtil.getCodeByLabel(DictUtil.FOCUS_CATEGORY, category
                            , false, "重点人员管理类别");
                    if (focusCategoryResult.isSuccess()) {
                        focusCategory.append(",").append(focusCategoryResult.getCode());
                    }
                });
                housePersonRelVo.setFocusCategory(focusCategory.toString());
            }
        }
        housePersonRelVo.setFocusCategory(focusCategory.toString());

        // 治安重点人员管理类别 管理地一
        DictResult focusCategory1Result = DictUtil.getCodeByLabel(DictUtil.FOCUS_CATEGORY, housePersonRelExcel.getFocusCategory1Ch()
                , false, "治安重点人员管理类别");
        if (focusCategory1Result.isSuccess()) {
            housePersonRelVo.setFocusCategory1(focusCategory1Result.getCode());
        }

        // 管控状态 管理地一
        DictResult status1Result = DictUtil.getCodeByLabel(DictUtil.FOCUS_STATUS, housePersonRelExcel.getStatus1Ch()
                , false, "管控状态");
        if (status1Result.isSuccess()) {
            housePersonRelVo.setStatus1(status1Result.getCode());
        }

        // 管控民警证件类型 管理地一
        DictResult policeIdType1Result = DictUtil.getCodeByLabel(DictUtil.CREDENTIAL_TYPE, housePersonRelExcel.getPoliceIdType1Ch()
                , false, "管控民警证件类型");
        if (policeIdType1Result.isSuccess()) {
            housePersonRelVo.setPoliceIdType1(policeIdType1Result.getCode());
        }

        // 治安重点人员管理类别 管理地二
        DictResult focusCategory2Result = DictUtil.getCodeByLabel(DictUtil.FOCUS_CATEGORY, housePersonRelExcel.getFocusCategory2Ch()
                , false, "治安重点人员管理类别");
        if (focusCategory2Result.isSuccess()) {
            housePersonRelVo.setStatus1(focusCategory2Result.getCode());
        }

        // 管控状态 管理地二
        DictResult status2Result = DictUtil.getCodeByLabel(DictUtil.FOCUS_STATUS, housePersonRelExcel.getStatus2Ch()
                , false, "管控状态");
        if (status2Result.isSuccess()) {
            housePersonRelVo.setStatus2(status2Result.getCode());
        }

        // 管控民警证件类型 管理地二
        DictResult policeIdType2Result = DictUtil.getCodeByLabel(DictUtil.CREDENTIAL_TYPE, housePersonRelExcel.getPoliceIdType2Ch()
                , false, "管控民警证件类型");
        if (policeIdType2Result.isSuccess()) {
            housePersonRelVo.setPoliceIdType2(policeIdType2Result.getCode());
        }
    }

    /**
     * <p>
     * 处理身份证相关的格式验证
     * </p>
     *
     * @param housePersonRelVo    人屋关系vo对象
     * @param housePersonRelExcel 人屋关系Excel对象
     * @param currentRow          当前行
     * @author: 王良俊
     */
    private void handleIdCardAttr(ProjectHousePersonRelVo housePersonRelVo, HousePersonRelExcel housePersonRelExcel, Integer currentRow) {
        String credentialName = "居民身份证";
        // 住户身份证
        if (credentialName.equals(housePersonRelExcel.getCredentialTypeCh())) {
            if (StrUtil.isNotBlank(housePersonRelExcel.getCredentialNo()) && !IdcardUtil.isValidCard(housePersonRelExcel.getCredentialNo())) {
                throw new ExcelAnalysisException("证件号：格式错误");
            }
        }
        // 配偶身份证格式验证
        if (credentialName.equals(housePersonRelExcel.getCredentialTypeCh())) {
            if (StrUtil.isNotBlank(housePersonRelExcel.getSpouseIdNo()) && !IdcardUtil.isValidCard(housePersonRelExcel.getSpouseIdNo())) {
                throw new ExcelAnalysisException("配偶证件号：格式错误");
            }
        }
        // 代理人身份证格式验证
        if (credentialName.equals(housePersonRelExcel.getDlrzjlxCh())) {
            if (StrUtil.isNotBlank(housePersonRelExcel.getDlrzjhm()) && !IdcardUtil.isValidCard(housePersonRelExcel.getDlrzjhm())) {
                throw new ExcelAnalysisException("代理人证件号：格式错误");
            }
        }
        // 管控民警身份证格式验证
        if (credentialName.equals(housePersonRelExcel.getPoliceIdType1Ch())) {
            if (StrUtil.isNotBlank(housePersonRelExcel.getPoliceIdNo1()) && !IdcardUtil.isValidCard(housePersonRelExcel.getPoliceIdNo1())) {
                throw new ExcelAnalysisException("管控民警证件号：格式错误");
            }
        }
        // 管控民警身份证格式验证
        if (credentialName.equals(housePersonRelExcel.getPoliceIdType2Ch())) {
            if (StrUtil.isNotBlank(housePersonRelExcel.getPoliceIdNo2()) && !IdcardUtil.isValidCard(housePersonRelExcel.getPoliceIdNo2())) {
                throw new ExcelAnalysisException("管控民警2证件号：格式错误");
            }
        }
    }

    /**
     * <p>
     * 处理Excel中与时间有关的属性格式验证
     * </p>
     *
     * @param housePersonRelVo    人屋关系vo对象
     * @param housePersonRelExcel 人屋关系Excel对象
     * @param currentRow          当前行
     * @author: 王良俊
     */
    private void handleTimeAttr(ProjectHousePersonRelVo housePersonRelVo, HousePersonRelExcel housePersonRelExcel, Integer currentRow) {
        String rentTimeRange = housePersonRelExcel.getRentTimeRange();
        // 租赁开始时间
        LocalDateTime startTime = null;
        // 租赁结束时间
        LocalDateTime stopTime = null;
        // 入住时间
        LocalDateTime checkInTime = null;
        if (StrUtil.isNotEmpty(rentTimeRange)) {
            rentTimeRange = rentTimeRange.replace(" ", "");
            String[] timeRange = rentTimeRange.split("至");
            if (ArrayUtil.isNotEmpty(timeRange) && timeRange.length == 2) {
                startTime = timeFormat("租赁日期", timeRange[0]);
                stopTime = timeFormat("租赁日期", timeRange[1]);
                housePersonRelVo.setRentStartTime(startTime);
                housePersonRelVo.setRentStopTime(stopTime);
            } else {
                throw new ExcelAnalysisException("租赁日期：时间格式错误");
            }
        } else {
            // 这里如果是租客则租赁时间是必填项
            if ("租客".equals(housePersonRelExcel.getHouseholdTypeCh())) {
                throw new ExcelAnalysisException("租赁日期：未填写");
            }
        }

        // 入住时间转换
        if (StrUtil.isNotBlank(housePersonRelExcel.getCheckInTimeStr())) {
            checkInTime = timeFormat("入住时间", housePersonRelExcel.getCheckInTimeStr());
            housePersonRelVo.setCheckInTime(checkInTime);
        }

        if (startTime != null && checkInTime != null) {
            Duration effTimeCheckInBetween = Duration.between(startTime, checkInTime);
            Duration expTimeCheckInBetween = Duration.between(stopTime, checkInTime);
            long l = effTimeCheckInBetween.toDays();
            if (l < 0) {
                throw new ExcelAnalysisException("租赁日期：租赁起始时间不能晚于入住时间");
            } else if (expTimeCheckInBetween.toDays() > 0) {
                throw new ExcelAnalysisException("租赁日期：租赁结束时间时间不能早于入住时间");
            }
        }

        // 生日转换
        if (StrUtil.isNotBlank(housePersonRelExcel.getBirthStr())) {
            housePersonRelVo.setBirth(timeFormat("生日", housePersonRelExcel.getBirthStr()));
        }

        // 入境时间转换
        if (StrUtil.isNotBlank(housePersonRelExcel.getEntryTimeStr())) {
            housePersonRelVo.setEntryTime(timeFormat("入境时间", housePersonRelExcel.getEntryTimeStr()));
        }

    }

    /**
     * <p>
     * 处理Excel中与地址有关的属性
     * </p>
     *
     * @param housePersonRelVo    人屋关系vo对象
     * @param housePersonRelExcel 人屋关系Excel对象
     * @param currentRow          当前行
     * @author: 王良俊
     */
    private void handleAddressAttr(ProjectHousePersonRelVo housePersonRelVo, HousePersonRelExcel housePersonRelExcel, Integer currentRow) {

        // 住户的户籍信息
        String domicileInfo = housePersonRelExcel.getDomicileInfo();

        if (StrUtil.isNotBlank(domicileInfo)) {
            String[] domicileArr = domicileInfo.split("-");
            Address address = AddressParseUtil.parseAddressNameToCode("户籍", domicileArr, AddressParseUtil.ADDRESS, true);
            // 这里如果地址有问题的话就会直接抛异常所以获取到的肯定有地址
            housePersonRelVo.setProvinceCode(address.getProvinceCode());
            housePersonRelVo.setCityCode(address.getCityCode());
            housePersonRelVo.setCountyCode(address.getCountyCode());
            housePersonRelVo.setStreetCode(address.getStreetCode());
        } else if (HousePersonRelExcelEnum.RESIDENT_PUBLIC.getType().equals(housePersonRelExcelEnum.getType())) {
            throw new ExcelAnalysisException("户籍信息未填写");
        }

        // 配偶籍贯信息
        String spouseDomicile = housePersonRelExcel.getSpouseDomicile();
        if (StrUtil.isNotBlank(spouseDomicile)) {
            String[] spouseDomicileArr = spouseDomicile.split("-");
            Address address = AddressParseUtil.parseAddressNameToCode("配偶籍贯", spouseDomicileArr, AddressParseUtil.HOMETOWN, false);
            // 这里如果地址有问题的话就会直接抛异常所以获取到的肯定有地址
            housePersonRelVo.setSpouseProvince(address.getProvinceCode());
            housePersonRelVo.setSpouseCity(address.getCityCode());
            housePersonRelVo.setSpouseCounty(address.getCountyCode());
        }

        // 单位地址信息
        String employerLocation = housePersonRelExcel.getEmployerLocation();
        if (StrUtil.isNotBlank(employerLocation)) {
            String[] employerLocationArr = employerLocation.split("-");
            Address address = AddressParseUtil.parseAddressNameToCode("单位地址", employerLocationArr, AddressParseUtil.ADDRESS, false);
            // 这里如果地址有问题的话就会直接抛异常所以获取到的肯定有地址
            housePersonRelVo.setEmployerProvinceCode(address.getProvinceCode());
            housePersonRelVo.setEmployerCityCode(address.getCityCode());
            housePersonRelVo.setEmployerCountyCode(address.getCountyCode());
            housePersonRelVo.setEmployerStreetCode(address.getStreetCode());
        }

        // 管理地一地址信息
        String manage1Location = housePersonRelExcel.getManage1Location();
        if (StrUtil.isNotBlank(manage1Location)) {
            String[] manage1LocationArr = manage1Location.split("-");
            Address address = AddressParseUtil.parseAddressNameToCode("管理地一地址", manage1LocationArr, AddressParseUtil.ADDRESS, false);
            // 这里如果地址有问题的话就会直接抛异常所以获取到的肯定有地址
            housePersonRelVo.setProvince1(address.getProvinceCode());
            housePersonRelVo.setCity1(address.getCityCode());
            housePersonRelVo.setCounty1(address.getCountyCode());
            housePersonRelVo.setStreet1(address.getStreetCode());
        }

        // 管理地二地址信息
        String manage2Location = housePersonRelExcel.getManage2Location();
        if (StrUtil.isNotBlank(manage2Location)) {
            String[] manage2LocationArr = manage2Location.split("-");
            Address address = AddressParseUtil.parseAddressNameToCode("管理地二地址", manage2LocationArr, AddressParseUtil.ADDRESS, false);
            // 这里如果地址有问题的话就会直接抛异常所以获取到的肯定有地址
            housePersonRelVo.setProvince2(address.getProvinceCode());
            housePersonRelVo.setCity2(address.getCityCode());
            housePersonRelVo.setCounty2(address.getCountyCode());
            housePersonRelVo.setStreet2(address.getStreetCode());
        }

    }

    /**
     * <p>
     * 处理Excel中与手机号有关的属性
     * </p>
     *
     * @param housePersonRelVo    人屋关系vo对象
     * @param housePersonRelExcel 人屋关系Excel对象
     * @param currentRow          当前行
     * @author: 王良俊
     */
    private void handlePhoneNumberAttr(ProjectHousePersonRelVo housePersonRelVo, HousePersonRelExcel housePersonRelExcel, Integer currentRow) {

        String telephone = housePersonRelExcel.getTelephone();
        if (StrUtil.isEmpty(telephone)) {
            throw new ExcelAnalysisException("住户手机号：必填项未填写");
        }
        if (!validPhone(telephone)) {
            throw new ExcelAnalysisException("住户手机号：格式错误");
        }

        String spousePhone = housePersonRelExcel.getSpousePhone();
        if (!validPhone(spousePhone)) {
            throw new ExcelAnalysisException("配偶联系电话：格式错误");
        }

    }

    /**
     * <p>
     * 在这里统一对字符串类型的属性进行判断
     * </p>
     *
     * @param housePersonRelVo    人屋关系vo对象
     * @param housePersonRelExcel 人屋关系Excel对象
     * @param currentRow          当前行
     * @author: 王良俊
     */
    private void handleStrAttr(ProjectHousePersonRelVo housePersonRelVo, HousePersonRelExcel housePersonRelExcel, Integer currentRow) {
        // 判断是否必填
        // 公安和系统的必填情况不一样
        int currentMinLength = 0;
        if (HousePersonRelExcelEnum.RESIDENT_PUBLIC.getType().equals(housePersonRelExcelEnum.getType())) {
            currentMinLength = 1;
        }
        // 人口登记类型 字段
        validStr("人口登记类型", housePersonRelExcel.getRegisterType(), currentMinLength, 20);
        // 住户证件号 字段
        validStr("住户证件号", housePersonRelExcel.getCredentialNo(), currentMinLength, 32);
        // 户籍详细地址 字段
        validStr("户籍详细地址", housePersonRelExcel.getAddress(), currentMinLength, 64);

        // 楼栋 字段
        validStr("楼栋", housePersonRelExcel.getBuildingName(), 1, 32);
        // 单元 字段
        validStr("单元", housePersonRelExcel.getUnitName(), 1, 32);
        // 房号 字段
        validStr("房号", housePersonRelExcel.getHouseNo(), 1, 32);
        // 姓名 字段
        validStr("姓名", housePersonRelExcel.getPersonName(), 1, 8);
        // 曾用名 字段
        validStr("曾用名", housePersonRelExcel.getOldName(), 0, 64);
        // 居住方式 字段
        validStr("居住方式", housePersonRelExcel.getResideWay(), 0, 20);
        // 人员登记类型 字段
        validStr("人员登记类型", housePersonRelExcel.getPersonRegType(), 0, 20);
        // 居住事由 字段
        validStr("居住事由", housePersonRelExcel.getResideReason(), 0, 64);
        // 外文名 字段
        validStr("外文名", housePersonRelExcel.getNameEng(), 0, 64);
        // 外文姓 字段
        validStr("外文姓", housePersonRelExcel.getSurnameEng(), 0, 32);
        // 配偶姓名 字段
        validStr("配偶姓名", housePersonRelExcel.getSpouseName(), 0, 8);
        // 单位名称 字段
        validStr("单位名称", housePersonRelExcel.getEmployer(), 0, 64);
        // 职业类别 字段
        validStr("职业类别", housePersonRelExcel.getZylb(), 0, 20);
        // 单位联系电话 字段
        validStr("单位联系电话", housePersonRelExcel.getEmployerPhone(), 0, 20);
        // 单位法定代表人 字段
        validStr("单位法定代表人", housePersonRelExcel.getEmployerOwner(), 0, 32);
        // 单位详细地址 字段
        validStr("单位详细地址", housePersonRelExcel.getEmployerAddress(), 0, 64);
        // 房屋产权证号 字段
        validStr("房屋产权证号", housePersonRelExcel.getFwcqzh(), 0, 32);
        // 委托代理人姓名 字段
        validStr("委托代理人姓名", housePersonRelExcel.getWtdlrxm(), 0, 8);
        // 代理人联系电话 字段
        validStr("代理人联系电话", housePersonRelExcel.getWtdlrxm(), 0, 20);
        // 管理地一详细地址 字段
        validStr("管理地一详细地址", housePersonRelExcel.getAddress1(), 0, 128);
        // 管控地一联系方式 字段
        validStr("管控地一联系方式", housePersonRelExcel.getPhone1(), 0, 20);
        // 管控地一管控事由 字段
        validStr("管控地一管控事由", housePersonRelExcel.getReason1(), 0, 128);
        // 管控地一管控民警姓名 字段
        validStr("管控地一管控民警姓名", housePersonRelExcel.getPoliceName1(), 0, 8);
        // 管控地一管控民警联系电话 字段
        validStr("管控地一管控民警联系电话", housePersonRelExcel.getPolicePhone1(), 0, 20);
        // 管理地二详细地址 字段
        validStr("管理地二详细地址", housePersonRelExcel.getAddress2(), 0, 128);
        // 管控地二联系方式 字段role
        validStr("管控地二联系方式", housePersonRelExcel.getPhone2(), 0, 20);
        // 管控地二管控事由 字段
        validStr("管控地二管控事由", housePersonRelExcel.getReason2(), 0, 128);
        // 管控地二管控民警姓名 字段
        validStr("管控地二管控民警姓名", housePersonRelExcel.getPoliceName2(), 0, 8);
        // 管控地二管控民警联系电话 字段
        validStr("管控地二管控民警联系电话", housePersonRelExcel.getPolicePhone2(), 0, 20);

    }

    /**
     * <p>
     * 进行数据保存操作
     * </p>
     */
    protected void saveData() {
        if (CollUtil.isNotEmpty(housePersonRelVoList)) {
            projectHousePersonRelService.saveRelBatch(housePersonRelVoList);
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

    /**
     * @author: 许亮亮
     */
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
            String path = housePersonRelExcelEnum.getType() + "-" + uuid;
            List objects = new ArrayList<>(errorMap.values());
            String data = JSONUtil.toJsonStr(objects);
            redisTemplate.opsForValue().set(path, data);
            redisTemplate.expire(path, 2, TimeUnit.HOURS);
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
     * @return LocalDateTime 日期时间对象
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
     * @return 格式是否正确
     * @author: 王良俊
     */
    private boolean validPhone(String phoneNumber) {
//        if (StringUtils.isEmpty(phoneNumber)) return true;
//        return (StringUtils.isNumeric(phoneNumber) && phoneNumber.length() == 11);

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

}
