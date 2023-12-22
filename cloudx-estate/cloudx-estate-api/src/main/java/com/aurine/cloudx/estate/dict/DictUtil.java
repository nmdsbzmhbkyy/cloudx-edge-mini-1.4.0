package com.aurine.cloudx.estate.dict;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.exception.ExcelAnalysisException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName: DictUtil
 * @author: 王良俊 <>
 * @date: 2020年08月21日 上午09:33:21
 * @Copyright:
 */
public class DictUtil {

    /**
     * 是否是重点人员 无对应字典
     */
    public static final String IS_FOCUS_PERSON = "IS_FOCUS_PERSON";

    /**
     * 车牌号 省 无对应字典
     */
    public static final String PLATE_NUMBER_PROVINCE = "PLATENUMBER_PROVINCE";

    /**
     * 车牌号 字母 无对应字典
     */
    public static final String PLATE_NUMBER_ALPHABET = "PLATENUMBER_ALPHABET";

    /**
     * 住户类型 <span style="color:green">house_people_rel</span>
     */
    public static final String HOUSE_PEOPLE_REL = "HOUSE_PEOPLE_REL";

    /**
     * 人员类型 <span style="color:green">people_mobie_type</span>
     */
    public static final String PEOPLE_MOBILE_TYPE = "PEOPLE_MOBILE_TYPE";

    /**
     * 民族列表 <span style="color:green">nation_code</span>
     */
    public static final String NATION_CODE = "NATION_CODE";

    /**
     * 人员类别 <span style="color:green">people_tag</span>
     */
    public static final String PEOPLE_TAG = "PEOPLE_TAG";

    /**
     * 婚姻状况 <span style="color:green">marital_status</span>
     */
    public static final String MARITAL_STATUS = "MARITAL_STATUS";

    /**
     * 证件类型 <span style="color:green">credential_type</span>
     */
    public static final String CREDENTIAL_TYPE = "CREDENTIAL_TYPE";

    /**
     * 文化程度 <span style="color:green">education_code</span>
     */
    public static final String EDUCATION_CODE = "EDUCATION_CODE";

    /**
     * 单位类别 <span style="color:green">employer_type</span>
     */
    public static final String EMPLOYER_TYPE = "EMPLOYER_TYPE";

    /**
     * 特殊身份 <span style="color:green">special_identity</span>
     */
    public static final String SPECIAL_IDENTITY = "SPECIAL_IDENTITY";

    /**
     * 政治面貌 <span style="color:green">political_status</span>
     */
    public static final String POLITICAL_STATUS = "POLITICAL_STATUS";

    /**
     * 宗教信仰 <span style="color:green">religious_belief</span>
     */
    public static final String RELIGIOUS_BELIEF = "RELIGIOUS_BELIEF";

    /**
     * 户类型字典 <span style="color:green">residence_type</span>
     */
    public static final String RESIDENCE_TYPE = "RESIDENCE_TYPE";

    /**
     * 户口性质字典 <span style="color:green">residence_category</span>
     */
    public static final String RESIDENCE_CATEGORY = "RESIDENCE_CATEGORY";

    /**
     * 职业类别 <span style="color:green">occupation_type</span>
     */
    public static final String OCCUPATION_TYPE = "OCCUPATION_TYPE";

    /**
     * 性别 <span style="color:green">gender_type</span>
     */
    public static final String GENDER_TYPE = "GENDER_TYPE";

    /**
     * 住户类型 <span style="color:green">household_type</span>
     */
    public static final String HOUSEHOLD_TYPE = "HOUSEHOLD_TYPE";

    /**
     * 家属类型 <span style="color:green">member_type</span>
     */
    public static final String MEMBER_TYPE = "MEMBER_TYPE";
    /**
     * 国籍(国家字典) <span style="color:green">nationality_code</span>
     */
    public static final String NATIONALITY_CODE = "NATIONALITY_CODE";

    /**
     * 治安重点人员管理类别 <span style="color:green">focus_category</span>
     */
    public static final String FOCUS_CATEGORY = "FOCUS_CATEGORY";

    /**
     * 管控状态 <span style="color:green">focus_status</span>
     */
    public static final String FOCUS_STATUS = "FOCUS_STATUS";

    /**
     * 归属类型 <span style="color:green">attribution_type</span>
     */
    public static final String ATTRIBUTION_TYPE = "ATTRIBUTION_TYPE";

    /**
     * 车辆号牌种类 <span style="color:green">plate_type</span>
     */
    public static final String PLATE_TYPE = "PLATE_TYPE";

    /**
     * 车辆号牌颜色 <span style="color:green">plate_color</span>
     */
    public static final String PLATE_COLOR = "PLATE_COLOR";

    /**
     * 车辆号牌种类颜色关系对应 无字典 这是用车辆号牌类型获取其对应车牌颜色
     */
    public static final String PLATE_TYPE_COLOR = "PLATE_TYPE_COLOR";

    /**
     * 车身颜色 <span style="color:green">vehicle_color</span>
     */
    public static final String VEHICLE_COLOR = "VEHICLE_COLOR";

    /**
     * 机动车类型代码 <span style="color:green">vehicle_type</span>
     */
    public static final String VEHICLE_TYPE = "VEHICLE_TYPE";

    /**
     * 机动车类型代码 <span style="color:green">vehicle_type</span>
     */
    private static final Integer MAX_POWER_OF_TWO = 1073741824;

    /**
     * 是否是重点人员
     */
    private static Map<String, String> isFocusPersonMap;

    /**
     * 车牌号 省
     */
    private static Map<String, String> plateNumberProvinceMap;

    /**
     * 车牌号 字母
     */
    private static Map<String, String> plateNumberAlphabet;

    /**
     * 住户类型
     */
    private static Map<String, String> housePeopleRelMap;

    /**
     * 人员类型
     */
    private static Map<String, String> peopleMobileTypeMap;

    /**
     * 民族列表
     */
    private static Map<String, String> nationCodeMap;

    /**
     * 人员类别
     */
    private static Map<String, String> peopleTagMap;

    /**
     * 婚姻状况
     */
    private static Map<String, String> maritalStatusMap;

    /**
     * 证件类型
     */
    private static Map<String, String> credentialTypeMap;

    /**
     * 文化程度
     */
    private static Map<String, String> educationCodeMap;

    /**
     * 单位类别
     */
    private static Map<String, String> employerTypeMap;

    /**
     * 特殊身份
     */
    private static Map<String, String> specialIdentityMap;

    /**
     * 政治面貌
     */
    private static Map<String, String> politicalStatusMap;

    /**
     * 宗教信仰
     */
    private static Map<String, String> religiousBeliefMap;

    /**
     * 户类型
     */
    private static Map<String, String> residenceTypeMap;

    /**
     * 户口性质
     */
    private static Map<String, String> residenceCategoryMap;

    /**
     * 职业类别
     */
    private static Map<String, String> occupationTypeMap;

    /**
     * 性别
     */
    private static Map<String, String> genderTypeMap;

    /**
     * 住户类型
     */
    private static Map<String, String> householdTypeMap;

    /**
     * 家属类型
     */
    private static Map<String, String> memberTypeMap;

    /**
     * 治安重点人员管理类别
     */
    private static Map<String, String> focusCategoryMap;

    /**
     * 管控状态
     */
    private static Map<String, String> focusStatusMap;

    /**
     * 国籍（国家字典）
     */
    private static Map<String, String> nationalityCodeMap;

    /**
     * 归属类型
     */
    private static Map<String, String> attributionTypeMap;

    /**
     * 车辆号牌种类
     */
    private static Map<String, String> plateTypeMap;

    /**
     * 车辆号牌颜色
     */
    private static Map<String, String> plateColorMap;

    /**
     * 车辆号牌种类颜色
     */
    private static Map<String, String> plateTypeColorMap;

    /**
     * 车身颜色
     */
    private static Map<String, String> vehicleColorMap;

    /**
     * 机动车类型代码
     */
    private static Map<String, String> vehicleTypeMap;


    /**
     * <p>
     * 字典查询方法
     * </p>
     *
     * @param dictName 字典名
     * @param key      字典中的code
     * @return 字典中的label
     */
    public static String getLabel(String dictName, String key) {
        Map<String, String> dictMap = getDictMap(dictName);
        return dictMap.get(key);
    }

    /**
     * <p>
     * 使用中文获取对应字典的code
     * </p>
     *
     * @param dictName   字典
     * @param label      中文内容
     * @param required   是否是必填项
     * @param columnName 列名
     * @return 字典结果对象
     */
    public static DictResult getCodeByLabel(String dictName, String label, boolean required, String columnName) {
        Map<String, String> dictMap = getDictMap(dictName);
        List<String> keyList = dictMap.entrySet()
                .stream()
                .filter(kvEntry -> Objects.equals(kvEntry.getValue(), label))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        if (CollUtil.isNotEmpty(keyList)) {
            return DictResult.success(keyList.get(0));
        } else {
            if (!required && StrUtil.isBlank(label)) {
                return DictResult.success("");
            }
            if (StrUtil.isNotBlank(label)) {
                throw new ExcelAnalysisException(columnName + "：填写错误");
            } else {
                throw new ExcelAnalysisException(columnName + "：必填项未填");
            }
        }
    }

    /**
     * <p>
     * 使用中文获取对应字典的code
     * </p>
     *
     * @param dictName 字典
     * @param label    中文内容
     * @return 字典结果对象
     */
    public static String getCodeByLabel(String dictName, String label) {
        Map<String, String> dictMap = getDictMap(dictName);
        List<String> keyList = dictMap.entrySet()
                .stream()
                .filter(kvEntry -> Objects.equals(kvEntry.getValue(), label))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        return keyList.get(0);
    }

    /**
     * <p>
     * 根据字典常量获取对应字典的map数据
     * </p>
     *
     * @param dictName 字典名（常量）
     * @return 字典的Map类型数据 未找到对应字典返回空数据Map对象
     */
    public static Map<String, String> getDictMap(String dictName) {
        Map<String, String> dictMap = new HashMap<>(0);
        switch (dictName) {
            case IS_FOCUS_PERSON:
                dictMap = initIsFocusPerson();
                break;
            case HOUSE_PEOPLE_REL:
                dictMap = initHousePersonRel();
                break;
            case PEOPLE_MOBILE_TYPE:
                dictMap = initPeopleMobileType();
                break;
            case NATIONALITY_CODE:
                dictMap = initNationalityCode();
                break;
            case NATION_CODE:
                dictMap = initNationCode();
                break;
            case PEOPLE_TAG:
                dictMap = initPeopleTag();
                break;
            case MARITAL_STATUS:
                dictMap = initMaritalStatus();
                break;
            case CREDENTIAL_TYPE:
                dictMap = initCredentialType();
                break;
            case EDUCATION_CODE:
                dictMap = initEducationCode();
                break;
            case EMPLOYER_TYPE:
                dictMap = initEmployerType();
                break;
            case SPECIAL_IDENTITY:
                dictMap = initSpecialIdentity();
                break;
            case POLITICAL_STATUS:
                dictMap = initPoliticalStatus();
                break;
            case RELIGIOUS_BELIEF:
                dictMap = initReligiousBelief();
                break;
            case RESIDENCE_TYPE:
                dictMap = initResidenceType();
                break;
            case RESIDENCE_CATEGORY:
                dictMap = initResidenceCategory();
                break;
            case OCCUPATION_TYPE:
                dictMap = initOccupationType();
                break;
            case GENDER_TYPE:
                dictMap = initGenderType();
                break;
            case HOUSEHOLD_TYPE:
                dictMap = initHouseholdType();
                break;
            case MEMBER_TYPE:
                dictMap = initMemberType();
                break;
            case FOCUS_CATEGORY:
                dictMap = initFocusCategory();
                break;
            case FOCUS_STATUS:
                dictMap = initFocusStatus();
                break;
            case ATTRIBUTION_TYPE:
                dictMap = initAttributionType();
                break;
            case PLATE_COLOR:
                dictMap = initPlateColorMap();
                break;
            case PLATE_TYPE:
                dictMap = initPlateTypeMap();
                break;
            case PLATE_TYPE_COLOR:
                dictMap = initPlateTypeColorMap();
                break;
            case VEHICLE_COLOR:
                dictMap = initVehicleColorMap();
                break;
            case VEHICLE_TYPE:
                dictMap = initVehicleTypeMap();
                break;
            case PLATE_NUMBER_PROVINCE:
                dictMap = initPlateNumberProvinceMap();
                break;
            case PLATE_NUMBER_ALPHABET:
                dictMap = initPlateNumberAlphabetMap();
                break;
            default:
                break;
        }
        return dictMap;
    }

    /**
     * <p>
     * copy: com.google.common.collect.Maps.capacity
     * </p>
     *
    */
    private static int capacity(int expectedSize) {
        if (expectedSize < 3) {
            if (expectedSize <= 0) {
                return 0;
            }
            return expectedSize + 1;
        }
        if (expectedSize < MAX_POWER_OF_TWO) {
            return (int) ((float) expectedSize / 0.75F + 1.0F);
        }
        return Integer.MAX_VALUE;
    }

    /**
     * 初始化国籍字典
     */
    private static Map<String, String> initNationalityCode() {
        if (MapUtil.isEmpty(nationalityCodeMap)) {
            nationalityCodeMap = new HashMap<>(capacity(238));
            nationalityCodeMap.put("10", "南极洲");
            nationalityCodeMap.put("100", "保加利亚");
            nationalityCodeMap.put("104", "缅甸");
            nationalityCodeMap.put("108", "布隆迪");
            nationalityCodeMap.put("112", "白俄罗斯");
            nationalityCodeMap.put("116", "柬埔寨");
            nationalityCodeMap.put("12", "阿尔及利亚");
            nationalityCodeMap.put("120", "喀麦隆");
            nationalityCodeMap.put("124", "加拿大");
            nationalityCodeMap.put("132", "佛得角");
            nationalityCodeMap.put("136", "开曼群岛");
            nationalityCodeMap.put("140", "中非");
            nationalityCodeMap.put("144", "斯里兰卡");
            nationalityCodeMap.put("148", "乍得");
            nationalityCodeMap.put("152", "智利");
            nationalityCodeMap.put("156", "中国");
            nationalityCodeMap.put("158", "中国台湾");
            nationalityCodeMap.put("16", "美属萨摩亚");
            nationalityCodeMap.put("162", "圣诞岛");
            nationalityCodeMap.put("166", "科科斯(基林)");
            nationalityCodeMap.put("170", "哥伦比亚");
            nationalityCodeMap.put("174", "科摩罗");
            nationalityCodeMap.put("175", "马约特");
            nationalityCodeMap.put("178", "刚果");
            nationalityCodeMap.put("扎伊尔", "扎伊尔");
            nationalityCodeMap.put("184", "库克群岛");
            nationalityCodeMap.put("188", "哥斯达黎加");
            nationalityCodeMap.put("191", "克罗地亚");
            nationalityCodeMap.put("192", "古巴");
            nationalityCodeMap.put("196", "塞浦路斯");
            nationalityCodeMap.put("20", "安道尔");
            nationalityCodeMap.put("203", "捷克");
            nationalityCodeMap.put("204", "贝宁");
            nationalityCodeMap.put("208", "丹麦");
            nationalityCodeMap.put("212", "多米尼克");
            nationalityCodeMap.put("214", "多米尼加共和国");
            nationalityCodeMap.put("218", "厄瓜多尔");
            nationalityCodeMap.put("222", "萨尔瓦多");
            nationalityCodeMap.put("226", "赤道几内亚");
            nationalityCodeMap.put("231", "埃塞俄比亚");
            nationalityCodeMap.put("232", "厄立特里亚");
            nationalityCodeMap.put("233", "爱沙尼亚");
            nationalityCodeMap.put("234", "法罗群岛");
            nationalityCodeMap.put("238", "马尔维纳斯群岛(福克兰群岛)");
            nationalityCodeMap.put("239", "南乔治亚岛和南桑德韦奇岛");
            nationalityCodeMap.put("24", "安哥拉");
            nationalityCodeMap.put("242", "斐济");
            nationalityCodeMap.put("246", "芬兰");
            nationalityCodeMap.put("250", "法国");
            nationalityCodeMap.put("254", "法属圭亚那");
            nationalityCodeMap.put("258", "法属波利尼西亚");
            nationalityCodeMap.put("260", "法属南部领土");
            nationalityCodeMap.put("262", "吉布提");
            nationalityCodeMap.put("266", "加蓬");
            nationalityCodeMap.put("268", "格鲁吉亚");
            nationalityCodeMap.put("270", "冈比亚");
            nationalityCodeMap.put("276", "德国");
            nationalityCodeMap.put("28", "安提瓜和巴布达");
            nationalityCodeMap.put("288", "加纳");
            nationalityCodeMap.put("292", "直布罗陀");
            nationalityCodeMap.put("296", "基里巴斯");
            nationalityCodeMap.put("300", "希腊");
            nationalityCodeMap.put("304", "格陵兰");
            nationalityCodeMap.put("308", "格林纳达");
            nationalityCodeMap.put("31", "阿塞拜疆");
            nationalityCodeMap.put("312", "瓜德罗普");
            nationalityCodeMap.put("316", "关岛");
            nationalityCodeMap.put("32", "阿根廷");
            nationalityCodeMap.put("320", "危地马拉");
            nationalityCodeMap.put("324", "几内亚");
            nationalityCodeMap.put("328", "圭亚那");
            nationalityCodeMap.put("332", "海地");
            nationalityCodeMap.put("334", "赫德岛和麦克唐纳岛");
            nationalityCodeMap.put("336", "梵蒂冈");
            nationalityCodeMap.put("340", "洪都拉斯");
            nationalityCodeMap.put("344", "香港");
            nationalityCodeMap.put("348", "匈牙利");
            nationalityCodeMap.put("352", "冰岛");
            nationalityCodeMap.put("356", "印度");
            nationalityCodeMap.put("36", "澳大利亚");
            nationalityCodeMap.put("360", "印度尼西亚");
            nationalityCodeMap.put("364", "伊朗");
            nationalityCodeMap.put("368", "伊拉克");
            nationalityCodeMap.put("372", "爱尔兰");
            nationalityCodeMap.put("374", "巴勒斯坦");
            nationalityCodeMap.put("376", "以色列");
            nationalityCodeMap.put("380", "意大利");
            nationalityCodeMap.put("384", "科特迪瓦");
            nationalityCodeMap.put("388", "牙买加");
            nationalityCodeMap.put("392", "日本");
            nationalityCodeMap.put("398", "哈萨克斯坦");
            nationalityCodeMap.put("4", "阿富汗");
            nationalityCodeMap.put("40", "奥地利");
            nationalityCodeMap.put("400", "约旦");
            nationalityCodeMap.put("404", "肯尼亚");
            nationalityCodeMap.put("408", "朝鲜");
            nationalityCodeMap.put("410", "韩国");
            nationalityCodeMap.put("414", "科威特");
            nationalityCodeMap.put("417", "吉尔吉斯斯坦");
            nationalityCodeMap.put("418", "老挝");
            nationalityCodeMap.put("422", "黎巴嫩");
            nationalityCodeMap.put("426", "莱索托");
            nationalityCodeMap.put("428", "拉脱维亚");
            nationalityCodeMap.put("430", "利比里亚");
            nationalityCodeMap.put("434", "利比亚");
            nationalityCodeMap.put("438", "列支敦士登");
            nationalityCodeMap.put("44", "巴哈马");
            nationalityCodeMap.put("440", "立陶宛");
            nationalityCodeMap.put("442", "卢森堡");
            nationalityCodeMap.put("446", "澳门");
            nationalityCodeMap.put("450", "马达加斯加");
            nationalityCodeMap.put("454", "马拉维");
            nationalityCodeMap.put("458", "马来西亚");
            nationalityCodeMap.put("462", "马尔代夫");
            nationalityCodeMap.put("466", "马里");
            nationalityCodeMap.put("470", "马耳他");
            nationalityCodeMap.put("474", "马提尼克");
            nationalityCodeMap.put("478", "毛里塔尼亚");
            nationalityCodeMap.put("48", "巴林");
            nationalityCodeMap.put("480", "毛里求斯");
            nationalityCodeMap.put("484", "墨西哥");
            nationalityCodeMap.put("492", "摩纳哥");
            nationalityCodeMap.put("496", "蒙古");
            nationalityCodeMap.put("498", "摩尔多瓦");
            nationalityCodeMap.put("50", "孟加拉国");
            nationalityCodeMap.put("500", "蒙特塞拉特");
            nationalityCodeMap.put("504", "摩洛哥");
            nationalityCodeMap.put("508", "莫桑比克");
            nationalityCodeMap.put("51", "亚美尼亚");
            nationalityCodeMap.put("512", "阿曼");
            nationalityCodeMap.put("516", "纳米比亚");
            nationalityCodeMap.put("52", "巴巴多斯");
            nationalityCodeMap.put("520", "瑙鲁");
            nationalityCodeMap.put("524", "尼泊尔");
            nationalityCodeMap.put("528", "荷兰");
            nationalityCodeMap.put("530", "荷属安的列斯");
            nationalityCodeMap.put("533", "阿鲁巴");
            nationalityCodeMap.put("540", "新喀里多尼亚");
            nationalityCodeMap.put("548", "瓦努阿图");
            nationalityCodeMap.put("554", "新西兰");
            nationalityCodeMap.put("558", "尼加拉瓜");
            nationalityCodeMap.put("56", "比利时");
            nationalityCodeMap.put("562", "尼日尔");
            nationalityCodeMap.put("566", "尼日利亚");
            nationalityCodeMap.put("570", "纽埃");
            nationalityCodeMap.put("574", "诺福克岛");
            nationalityCodeMap.put("578", "挪威");
            nationalityCodeMap.put("580", "北马里亚纳");
            nationalityCodeMap.put("581", "美属太平洋各群岛(包括: 中途岛、约翰斯顿岛、 豪兰岛、 贝克岛和威克岛等)");
            nationalityCodeMap.put("583", "密克罗尼西亚");
            nationalityCodeMap.put("584", "马绍尔群岛");
            nationalityCodeMap.put("585", "贝劳");
            nationalityCodeMap.put("586", "巴基斯坦");
            nationalityCodeMap.put("591", "巴拿马");
            nationalityCodeMap.put("598", "巴布亚新几内亚");
            nationalityCodeMap.put("60", "百慕大");
            nationalityCodeMap.put("600", "巴拉圭");
            nationalityCodeMap.put("604", "秘鲁");
            nationalityCodeMap.put("608", "菲律宾");
            nationalityCodeMap.put("612", "皮特凯恩群岛");
            nationalityCodeMap.put("616", "波兰");
            nationalityCodeMap.put("620", "葡萄牙");
            nationalityCodeMap.put("624", "几内亚比绍");
            nationalityCodeMap.put("626", "东帝汶");
            nationalityCodeMap.put("630", "波多黎各");
            nationalityCodeMap.put("634", "卡塔尔");
            nationalityCodeMap.put("638", "留尼汪");
            nationalityCodeMap.put("64", "不丹");
            nationalityCodeMap.put("642", "罗马尼亚");
            nationalityCodeMap.put("643", "俄罗斯");
            nationalityCodeMap.put("646", "卢旺达");
            nationalityCodeMap.put("654", "圣赫勒拿");
            nationalityCodeMap.put("659", "圣基茨和尼维斯");
            nationalityCodeMap.put("660", "安圭拉");
            nationalityCodeMap.put("662", "圣卢西亚");
            nationalityCodeMap.put("666", "圣皮埃尔和密克隆");
            nationalityCodeMap.put("670", "圣文森特和格林纳丁斯");
            nationalityCodeMap.put("674", "圣马力诺");
            nationalityCodeMap.put("678", "圣多美和普林西比");
            nationalityCodeMap.put("68", "玻利维亚");
            nationalityCodeMap.put("682", "沙竺阿拉伯");
            nationalityCodeMap.put("686", "塞内加尔");
            nationalityCodeMap.put("690", "塞舌尔");
            nationalityCodeMap.put("694", "塞拉利昂");
            nationalityCodeMap.put("70", "波斯尼亚和黑塞哥维那");
            nationalityCodeMap.put("702", "新加坡");
            nationalityCodeMap.put("703", "斯洛伐克");
            nationalityCodeMap.put("704", "越南");
            nationalityCodeMap.put("705", "斯洛文尼亚");
            nationalityCodeMap.put("706", "索马里");
            nationalityCodeMap.put("710", "南非");
            nationalityCodeMap.put("716", "津巴布韦");
            nationalityCodeMap.put("72", "博茨瓦纳");
            nationalityCodeMap.put("724", "西班牙");
            nationalityCodeMap.put("732", "西撒哈拉");
            nationalityCodeMap.put("736", "苏丹");
            nationalityCodeMap.put("74", "布维岛");
            nationalityCodeMap.put("740", "苏里南");
            nationalityCodeMap.put("744", "斯瓦尔巴群岛");
            nationalityCodeMap.put("748", "斯威士兰");
            nationalityCodeMap.put("752", "瑞典");
            nationalityCodeMap.put("756", "瑞士");
            nationalityCodeMap.put("76", "巴西");
            nationalityCodeMap.put("760", "叙利亚");
            nationalityCodeMap.put("762", "塔吉克斯坦");
            nationalityCodeMap.put("764", "泰国");
            nationalityCodeMap.put("768", "多哥");
            nationalityCodeMap.put("772", "托克劳");
            nationalityCodeMap.put("776", "汤加");
            nationalityCodeMap.put("780", "特立尼达和多巴哥");
            nationalityCodeMap.put("784", "阿闻酋");
            nationalityCodeMap.put("788", "突尼斯");
            nationalityCodeMap.put("792", "土耳其");
            nationalityCodeMap.put("795", "土库曼斯坦");
            nationalityCodeMap.put("796", "特克斯科斯群岛");
            nationalityCodeMap.put("798", "图瓦卢");
            nationalityCodeMap.put("8", "阿尔巴尼亚");
            nationalityCodeMap.put("800", "乌干达");
            nationalityCodeMap.put("804", "乌克兰");
            nationalityCodeMap.put("807", "马斯顿");
            nationalityCodeMap.put("818", "埃及");
            nationalityCodeMap.put("826", "英国");
            nationalityCodeMap.put("834", "坦桑尼亚");
            nationalityCodeMap.put("84", "伯利兹");
            nationalityCodeMap.put("840", "美国");
            nationalityCodeMap.put("850", "美属维尔京群岛");
            nationalityCodeMap.put("854", "布基纳法索");
            nationalityCodeMap.put("858", "乌拉圭");
            nationalityCodeMap.put("86", "英属印度洋领土");
            nationalityCodeMap.put("860", "乌兹别克斯坦");
            nationalityCodeMap.put("862", "委内瑞拉");
            nationalityCodeMap.put("876", "瓦利斯和富图纳群岛");
            nationalityCodeMap.put("882", "西萨摩亚");
            nationalityCodeMap.put("887", "也门");
            nationalityCodeMap.put("891", "南斯拉夫");
            nationalityCodeMap.put("894", "赞比亚");
            nationalityCodeMap.put("90", "所罗门群岛");
            nationalityCodeMap.put("92", "英属维尔京群岛");
        }
        return nationalityCodeMap;
    }



    /*数据初始化方法*/

    /**
     * <p>
     * 初始化是否是重点人员字典
     * </p>
     */
    private static Map<String, String> initIsFocusPerson() {
        if (MapUtil.isEmpty(isFocusPersonMap)) {
            isFocusPersonMap = new HashMap<>(capacity(2));
            isFocusPersonMap.put("0", "否");
            isFocusPersonMap.put("1", "是");
        }
        return isFocusPersonMap;
    }

    /**
     * <p>
     * 初始化住户类型字典
     * </p>
     */
    private static Map<String, String> initHousePersonRel() {
        if (MapUtil.isEmpty(housePeopleRelMap)) {
            housePeopleRelMap = new HashMap<>(capacity(4));
            housePeopleRelMap.put("1", "自住");
            housePeopleRelMap.put("2", "租赁");
            housePeopleRelMap.put("3", "民宿");
            housePeopleRelMap.put("4", "其他");
        }
        return housePeopleRelMap;
    }

    /**
     * <p>
     * 初始化住户类型字典
     * </p>
     */
    private static Map<String, String> initPeopleMobileType() {
        if (MapUtil.isEmpty(peopleMobileTypeMap)) {
            peopleMobileTypeMap = new HashMap<>(capacity(2));
            peopleMobileTypeMap.put("1", "户籍人口");
            peopleMobileTypeMap.put("2", "流动人口");
        }
        return peopleMobileTypeMap;
    }

    /**
     * <p>
     * 初始化住户类型字典
     * </p>
     */
    private static Map<String, String> initNationCode() {
        if (MapUtil.isEmpty(nationCodeMap)) {
            nationCodeMap = new HashMap<>(capacity(56));
            nationCodeMap.put("1", "阿昌族");
            nationCodeMap.put("10", "东乡族");
            nationCodeMap.put("11", "侗族");
            nationCodeMap.put("12", "独龙族");
            nationCodeMap.put("13", "鄂伦春族");
            nationCodeMap.put("14", "俄罗斯族");
            nationCodeMap.put("15", "鄂温克族");
            nationCodeMap.put("16", "高山族");
            nationCodeMap.put("17", "仡佬族");
            nationCodeMap.put("18", "汉族");
            nationCodeMap.put("19", "哈尼族");
            nationCodeMap.put("2", "白族");
            nationCodeMap.put("20", "哈萨克族");
            nationCodeMap.put("21", "赫哲族");
            nationCodeMap.put("22", "回族");
            nationCodeMap.put("23", "基诺族");
            nationCodeMap.put("24", "景颇族");
            nationCodeMap.put("25", "京族");
            nationCodeMap.put("26", "柯尔克孜族");
            nationCodeMap.put("27", "拉祜族");
            nationCodeMap.put("28", "珞巴族");
            nationCodeMap.put("29", "傈僳族");
            nationCodeMap.put("3", "保安族");
            nationCodeMap.put("30", "黎族");
            nationCodeMap.put("31", "满族");
            nationCodeMap.put("32", "毛南族");
            nationCodeMap.put("33", "门巴族");
            nationCodeMap.put("34", "蒙古族");
            nationCodeMap.put("35", "苗族");
            nationCodeMap.put("36", "仫佬族");
            nationCodeMap.put("37", "纳西族");
            nationCodeMap.put("38", "怒族");
            nationCodeMap.put("39", "普米族");
            nationCodeMap.put("4", "布朗族");
            nationCodeMap.put("40", "羌族");
            nationCodeMap.put("41", "撒拉族");
            nationCodeMap.put("42", "畲族");
            nationCodeMap.put("43", "水族");
            nationCodeMap.put("44", "塔吉克族");
            nationCodeMap.put("45", "塔塔尔族");
            nationCodeMap.put("46", "土家族");
            nationCodeMap.put("47", "土族");
            nationCodeMap.put("48", "佤族");
            nationCodeMap.put("49", "维吾尔族");
            nationCodeMap.put("5", "布依族");
            nationCodeMap.put("50", "乌孜别克族");
            nationCodeMap.put("51", "锡伯族");
            nationCodeMap.put("52", "瑶族");
            nationCodeMap.put("53", "彝族");
            nationCodeMap.put("54", "裕固族");
            nationCodeMap.put("55", "藏族");
            nationCodeMap.put("56", "壮族");
            nationCodeMap.put("6", "朝鲜族");
            nationCodeMap.put("7", "达斡尔族");
            nationCodeMap.put("8", "傣族");
            nationCodeMap.put("9", "德昂族");
        }
        return nationCodeMap;
    }

    /**
     * <p>
     * 初始化人员标签字典
     * </p>
     */
    private static Map<String, String> initPeopleTag() {
        if (MapUtil.isEmpty(peopleTagMap)) {
            peopleTagMap = new HashMap<>(capacity(8));
            peopleTagMap.put("1", "普通人员");
            peopleTagMap.put("2", "知名人员");
            peopleTagMap.put("3", "残疾人员");
            peopleTagMap.put("4", "独居老人");
            peopleTagMap.put("5", "重点治疗人员");
            peopleTagMap.put("6", "安置帮教人员");
            peopleTagMap.put("7", "重点关注人员");
            peopleTagMap.put("8", "其他关注人员");
        }
        return peopleTagMap;
    }

    /**
     * <p>
     * 初始化婚姻状况字典
     * </p>
     */
    private static Map<String, String> initMaritalStatus() {
        if (MapUtil.isEmpty(maritalStatusMap)) {
            maritalStatusMap = new HashMap<>(capacity(8));
            maritalStatusMap.put("10", "未婚");
            maritalStatusMap.put("20", "已婚");
            maritalStatusMap.put("21", "初婚");
            maritalStatusMap.put("22", "再婚");
            maritalStatusMap.put("23", "复婚");
            maritalStatusMap.put("30", "丧偶");
            maritalStatusMap.put("40", "离婚");
            maritalStatusMap.put("90", "未说明的婚姻状况");
        }
        return maritalStatusMap;
    }


    /**
     * <p>
     * 初始化证件类型
     * </p>
     */
    private static Map<String, String> initCredentialType() {
        if (MapUtil.isEmpty(credentialTypeMap)) {
            credentialTypeMap = new HashMap<>(capacity(144));
            credentialTypeMap.put("111", "居民身份证");
            credentialTypeMap.put("112", "临时居民身份证");
            credentialTypeMap.put("113", "户口薄");
            credentialTypeMap.put("114", "中国人民解放军军官证");
            credentialTypeMap.put("115", "中国人民武装警察部队普官证");
            credentialTypeMap.put("116", "暂住证");
            credentialTypeMap.put("117", "出生医学证明");
            credentialTypeMap.put("121", "法官证");
            credentialTypeMap.put("123", "警官证");
            credentialTypeMap.put("125", "检察官证");
            credentialTypeMap.put("127", "律师证");
            credentialTypeMap.put("129", "记者证");
            credentialTypeMap.put("131", "工作证");
            credentialTypeMap.put("133", "学生证");
            credentialTypeMap.put("151", "出入证");
            credentialTypeMap.put("153", "临时出人证临时出人内部单位证");
            credentialTypeMap.put("155", "住宿证");
            credentialTypeMap.put("157", "医疗证");
            credentialTypeMap.put("159", "劳保证");
            credentialTypeMap.put("161", "献血证");
            credentialTypeMap.put("163", "保险单含“保险证”");
            credentialTypeMap.put("191", "会员证包括工会及各种协会会员证");
            credentialTypeMap.put("211", "离休证");
            credentialTypeMap.put("213", "退休证");
            credentialTypeMap.put("215", "老年证");
            credentialTypeMap.put("217", "残疾证");
            credentialTypeMap.put("219", "结婚证");
            credentialTypeMap.put("221", "离婚证");
            credentialTypeMap.put("223", "独生子女证");
            credentialTypeMap.put("225", "毕业证书");
            credentialTypeMap.put("227", "肄业证");
            credentialTypeMap.put("229", "结业证");
            credentialTypeMap.put("231", "学位证");
            credentialTypeMap.put("233", "军人通行证");
            credentialTypeMap.put("291", "证明信含介绍信等");
            credentialTypeMap.put("311", "持枪证");
            credentialTypeMap.put("313", "枪证");
            credentialTypeMap.put("315", "枪支(弹药)挽运许可证");
            credentialTypeMap.put("317", "砍伐证");
            credentialTypeMap.put("319", "准运证各种物资的运轴许可");
            credentialTypeMap.put("321", "准购证");
            credentialTypeMap.put("323", "粮油证");
            credentialTypeMap.put("325", "购煤证");
            credentialTypeMap.put("327", "购煤气证");
            credentialTypeMap.put("329", "房屋产权证");
            credentialTypeMap.put("331", "土地使用证");
            credentialTypeMap.put("333", "车辆通行证");
            credentialTypeMap.put("335", "机动车驾驶证");
            credentialTypeMap.put("337", "机动车行驶证");
            credentialTypeMap.put("339", "机动车登记证书");
            credentialTypeMap.put("341", "机动车年检合格证");
            credentialTypeMap.put("343", "春运临时检验合格证");
            credentialTypeMap.put("345", "飞机驾驶证");
            credentialTypeMap.put("347", "船舶驾驶证");
            credentialTypeMap.put("349", "船舶行驶证");
            credentialTypeMap.put("351", "自行车行驶证");
            credentialTypeMap.put("353", "汽车号牌");
            credentialTypeMap.put("355", "拖拉机牌");
            credentialTypeMap.put("357", "摩托车牌");
            credentialTypeMap.put("359", "船舶牌");
            credentialTypeMap.put("361", "三轮车牌");
            credentialTypeMap.put("363", "自行车牌");
            credentialTypeMap.put("391", "残疾人机动轮椅车");
            credentialTypeMap.put("411", "外交护照");
            credentialTypeMap.put("412", "公务护照");
            credentialTypeMap.put("413", "因公普通护照");
            credentialTypeMap.put("414", "普通护照");
            credentialTypeMap.put("415", "旅行证");
            credentialTypeMap.put("416", "出入境通行证");
            credentialTypeMap.put("417", "外国人出入境证");
            credentialTypeMap.put("418", "外国人旅行证");
            credentialTypeMap.put("419", "海员证");
            credentialTypeMap.put("420", "香港特别行政区护照");
            credentialTypeMap.put("421", "澳门特别行政区护照");
            credentialTypeMap.put("423", "澳门特别行政区旅行证");
            credentialTypeMap.put("511", "台湾居民来往大陆通行证");
            credentialTypeMap.put("512", "台湾居民来往大陆通行证(一次有效)");
            credentialTypeMap.put("513", "往来港澳通行证");
            credentialTypeMap.put("515", "前往港澳通行证");
            credentialTypeMap.put("516", "港澳同胞回乡证(通行卡)");
            credentialTypeMap.put("517", "大陆居民往来台湾通行证");
            credentialTypeMap.put("518", "因公往来香港澳门特别行政区通行证");
            credentialTypeMap.put("华侨回国定居证", "华侨回国定居证");
            credentialTypeMap.put("552", "台湾居民定居证");
            credentialTypeMap.put("553", "外国人永久居留证");
            credentialTypeMap.put("554", "外国人居留证");
            credentialTypeMap.put("555", "外国人临时居留证");
            credentialTypeMap.put("556", "人籍证书");
            credentialTypeMap.put("557", "出籍证书");
            credentialTypeMap.put("558", "复籍证书");
            credentialTypeMap.put("611", "外籍船员住宿证");
            credentialTypeMap.put("612", "随船工作证");
            credentialTypeMap.put("620", "海上值勤证(红色)");
            credentialTypeMap.put("621", "海上值勤证(蓝色)");
            credentialTypeMap.put("631", "出海船民证");
            credentialTypeMap.put("633", "出海船舶户口薄");
            credentialTypeMap.put("634", "出海船舶边防登记簿");
            credentialTypeMap.put("635", "搭靠台轮许可证");
            credentialTypeMap.put("636", "台湾居民登陆证");
            credentialTypeMap.put("637", "台湾船员登陆证");
            credentialTypeMap.put("638", "外国船员登陆证");
            credentialTypeMap.put("639", "对台劳务人员登轮作业证");
            credentialTypeMap.put("640", "合资船船员登陆证");
            credentialTypeMap.put("641", "合资船船员登轮作业证");
            credentialTypeMap.put("642", "粤港澳流动渔民证");
            credentialTypeMap.put("643", "粤港澳临时流动渔民证");
            credentialTypeMap.put("644", "粤港澳流动渔船户口簿");
            credentialTypeMap.put("645", "航行港澳船舶证明书");
            credentialTypeMap.put("646", "往来港澳小型船舶查验薄");
            credentialTypeMap.put("650", "劳务人员登轮作业证");
            credentialTypeMap.put("711", "边境管理区通行证");
            credentialTypeMap.put("721", "中朝鸭绿江、图们江水文作业证");
            credentialTypeMap.put("722", "朝中鸭绿江、图们江水文作业证");
            credentialTypeMap.put("723", "中朝流筏固定代表证");
            credentialTypeMap.put("724", "朝中流筏固定代表证");
            credentialTypeMap.put("725", "中朝鸭绿江、图们江船员证");
            credentialTypeMap.put("726", "朝中鸭绿江、图们江船员证");
            credentialTypeMap.put("727", "中朝边境地区公安总代表证");
            credentialTypeMap.put("728", "朝中边境地区公安总代表证");
            credentialTypeMap.put("729", "中朝边境地区公安副总代表证");
            credentialTypeMap.put("730", "朝中边境地区公安副总代表证");
            credentialTypeMap.put("731", "中朝边境地区公安代表证");
            credentialTypeMap.put("732", "朝中边境地区公安代表证");
            credentialTypeMap.put("733", "中朝边境地区出入境通行证(甲、乙种本)");
            credentialTypeMap.put("734", "朝中边境公务通行证");
            credentialTypeMap.put("735", "朝中边境住民国境通行证");
            credentialTypeMap.put("736", "中蒙边境地区出入境通行证(甲、乙种本)");
            credentialTypeMap.put("737", "蒙中边境地区出入境通行证");
            credentialTypeMap.put("738", "中缅边境地区出入境通行证");
            credentialTypeMap.put("739", "缅甸中国边境通行证");
            credentialTypeMap.put("740", "云南省边境地区境外边民人出境证");
            credentialTypeMap.put("741", "中尼边境地区出入境通行证");
            credentialTypeMap.put("742", "尼中边境地区出入境通行证");
            credentialTypeMap.put("743", "中越边境地区出入境通行证");
            credentialTypeMap.put("744", "越中边境地区出入境通行证");
            credentialTypeMap.put("745", "中老边境地区出入境通行证");
            credentialTypeMap.put("746", "老中边境地区出入境通行证");
            credentialTypeMap.put("747", "中印边境地区出入境通行证");
            credentialTypeMap.put("748", "印中边境地区出入境通行证");
            credentialTypeMap.put("761", "深圳市过境耕作证");
            credentialTypeMap.put("765", "贸易证");
            credentialTypeMap.put("771", "铁路员工证");
            credentialTypeMap.put("781", "机组人员证");
            credentialTypeMap.put("990", "其他");
        }
        return credentialTypeMap;
    }

    /**
     * <p>
     * 初始化文化程度字典
     * </p>
     */
    private static Map<String, String> initEducationCode() {
        if (MapUtil.isEmpty(educationCodeMap)) {
            educationCodeMap = new HashMap<>(capacity(40));
            educationCodeMap.put("10", "研究生教育");
            educationCodeMap.put("11", "博士研究生毕业");
            educationCodeMap.put("12", "博士研究生结业");
            educationCodeMap.put("13", "博士研究生肄业");
            educationCodeMap.put("14", "硕士研究生毕业");
            educationCodeMap.put("15", "硕士研究生结业");
            educationCodeMap.put("16", "硕士研究生肄业");
            educationCodeMap.put("17", "研究生班毕业");
            educationCodeMap.put("18", "研究生班结业");
            educationCodeMap.put("19", "研究生班肄业");
            educationCodeMap.put("20", "大学本科教育");
            educationCodeMap.put("21", "大学本科毕业");
            educationCodeMap.put("22", "大学本科结业");
            educationCodeMap.put("23", "大学本科肄业");
            educationCodeMap.put("28", "大学普通班毕业");
            educationCodeMap.put("30", "大学专科教育");
            educationCodeMap.put("31", "大学专科毕业");
            educationCodeMap.put("32", "大学专科结业");
            educationCodeMap.put("33", "大学专科肄业");
            educationCodeMap.put("40", "中等职业教育");
            educationCodeMap.put("41", "中等专科毕业");
            educationCodeMap.put("42", "中等专科结业");
            educationCodeMap.put("43", "中等专科肄业");
            educationCodeMap.put("44", "职业高中毕业");
            educationCodeMap.put("45", "职业高中结业");
            educationCodeMap.put("46", "职业高中肄业");
            educationCodeMap.put("47", "技工学校毕业");
            educationCodeMap.put("48", "技工学校结业");
            educationCodeMap.put("49", "技工学校肄业");
            educationCodeMap.put("60", "普通高级中学教育");
            educationCodeMap.put("61", "普通高中毕业");
            educationCodeMap.put("62", "普通高中毕业");
            educationCodeMap.put("63", "普通高中肄业");
            educationCodeMap.put("70", "初级中学教育");
            educationCodeMap.put("71", "初中毕业");
            educationCodeMap.put("73", "初中肄业");
            educationCodeMap.put("80", "小学教育");
            educationCodeMap.put("81", "小学毕业");
            educationCodeMap.put("83", "小学肄业");
            educationCodeMap.put("90", "其它");
        }
        return educationCodeMap;
    }

    /**
     * <p>
     * 初始化单位类别字典
     * </p>
     */
    private static Map<String, String> initEmployerType() {
        if (MapUtil.isEmpty(employerTypeMap)) {
            employerTypeMap = new HashMap<>(capacity(4));
            employerTypeMap.put("1", "机关团体");
            employerTypeMap.put("2", "事业单位");
            employerTypeMap.put("3", "企业单位");
            employerTypeMap.put("9", "其他");
        }
        return employerTypeMap;
    }


    /**
     * <p>
     * 初始化特殊身份字典
     * </p>
     */
    private static Map<String, String> initSpecialIdentity() {
        if (MapUtil.isEmpty(specialIdentityMap)) {
            specialIdentityMap = new HashMap<>(capacity(25));
            specialIdentityMap.put("01", "人大代表");
            specialIdentityMap.put("02", "政协委员");
            specialIdentityMap.put("03", "民主党员");
            specialIdentityMap.put("04", "中高干部");
            specialIdentityMap.put("05", "高知高工");
            specialIdentityMap.put("06", "公安民警");
            specialIdentityMap.put("07", "其他民警");
            specialIdentityMap.put("08", "检法干部");
            specialIdentityMap.put("09", "工商税务");
            specialIdentityMap.put("10", "海关稽查");
            specialIdentityMap.put("11", "保安警卫");
            specialIdentityMap.put("12", "外交官员");
            specialIdentityMap.put("13", "外交家属");
            specialIdentityMap.put("14", "外商职员");
            specialIdentityMap.put("15", "华侨人士");
            specialIdentityMap.put("16", "侨眷侨属");
            specialIdentityMap.put("17", "留学人员");
            specialIdentityMap.put("18", "外籍人员");
            specialIdentityMap.put("19", "港澳台人员");
            specialIdentityMap.put("20", "宗教界人士");
            specialIdentityMap.put("21", "邪教人员");
            specialIdentityMap.put("22", "民族分裂分子");
            specialIdentityMap.put("23", "民运人员");
            specialIdentityMap.put("24", "未成年人");
            specialIdentityMap.put("99", "其他");
        }
        return specialIdentityMap;
    }

    /**
     * <p>
     * 初始化政治面貌字典
     * </p>
     */
    private static Map<String, String> initPoliticalStatus() {
        if (MapUtil.isEmpty(politicalStatusMap)) {
            politicalStatusMap = new HashMap<>(capacity(13));
            politicalStatusMap.put("01", "中国共产党党员");
            politicalStatusMap.put("02", "中国共产党预备党员");
            politicalStatusMap.put("03", "中国共产主义青年团团员");
            politicalStatusMap.put("04", "中国国民党革命委员会会员");
            politicalStatusMap.put("05", "中国民主同盟会员");
            politicalStatusMap.put("06", "中国民主建国会会员");
            politicalStatusMap.put("07", "中国民主促进会会员");
            politicalStatusMap.put("08", "中国农工民主党党员");
            politicalStatusMap.put("09", "中国致公党党员");
            politicalStatusMap.put("10", "九三学社社员");
            politicalStatusMap.put("11", "台湾民主自治同盟盟员");
            politicalStatusMap.put("12", "无党派民主人士");
            politicalStatusMap.put("13", "群众");
        }
        return politicalStatusMap;
    }

    /**
     * <p>
     * 初始化宗教信仰字典
     * </p>
     */
    private static Map<String, String> initReligiousBelief() {
        if (MapUtil.isEmpty(religiousBeliefMap)) {
            religiousBeliefMap = new HashMap<>(capacity(9));
            religiousBeliefMap.put("00", "无宗教信仰");
            religiousBeliefMap.put("10", "佛教");
            religiousBeliefMap.put("20", "喇嘛教");
            religiousBeliefMap.put("30", "道教");
            religiousBeliefMap.put("40", "天主教");
            religiousBeliefMap.put("50", "基督教");
            religiousBeliefMap.put("60", "东正教");
            religiousBeliefMap.put("70", "伊斯兰教");
            religiousBeliefMap.put("99", "其他");
        }
        return religiousBeliefMap;
    }

    /**
     * <p>
     * 初始化户类型字典
     * </p>
     */
    private static Map<String, String> initResidenceType() {
        if (MapUtil.isEmpty(residenceTypeMap)) {
            residenceTypeMap = new HashMap<>(capacity(2));
            residenceTypeMap.put("10", "家庭户");
            residenceTypeMap.put("20", "集体户");
        }
        return residenceTypeMap;
    }

    /**
     * <p>
     * 初始化户口性质字典
     * </p>
     */
    private static Map<String, String> initResidenceCategory() {
        if (MapUtil.isEmpty(residenceCategoryMap)) {
            residenceCategoryMap = new HashMap<>(capacity(8));
            residenceCategoryMap.put("10", "家庭户口");
            residenceCategoryMap.put("11", "非农业家庭户口");
            residenceCategoryMap.put("12", "农业家庭户口");
            residenceCategoryMap.put("20", "集体户口");
            residenceCategoryMap.put("21", "非农业集体户口");
            residenceCategoryMap.put("22", "农业集体户口");
            residenceCategoryMap.put("30", "未落常住户口");
            residenceCategoryMap.put("90", "其他户口");
        }
        return residenceCategoryMap;
    }


    /**
     * <p>
     * 初始化职业类别字典
     * </p>
     */
    private static Map<String, String> initOccupationType() {
        if (MapUtil.isEmpty(occupationTypeMap)) {
            occupationTypeMap = new HashMap<>(capacity(13));
            occupationTypeMap.put("11", "国家公务人员");
            occupationTypeMap.put("13", "专业技术人员");
            occupationTypeMap.put("17", "职员");
            occupationTypeMap.put("21", "企业管理人员");
            occupationTypeMap.put("24", "工人");
            occupationTypeMap.put("27", "农民");
            occupationTypeMap.put("31", "学生");
            occupationTypeMap.put("37", "现役军人");
            occupationTypeMap.put("51", "自由职业者");
            occupationTypeMap.put("54", "个体经营者");
            occupationTypeMap.put("70", "无业人员");
            occupationTypeMap.put("80", "离退休人员");
            occupationTypeMap.put("90", "其他");
        }
        return occupationTypeMap;
    }


    /**
     * <p>
     * 初始化性别字典
     * </p>
     */
    private static Map<String, String> initGenderType() {
        if (MapUtil.isEmpty(genderTypeMap)) {
            genderTypeMap = new HashMap<>(capacity(4));
            genderTypeMap.put("0", "未知的性别");
            genderTypeMap.put("1", "男性");
            genderTypeMap.put("2", "女性");
            genderTypeMap.put("9", "未说明的性别");
        }
        return genderTypeMap;
    }

    /**
     * <p>
     * 初始化住户类型字典
     * </p>
     */
    private static Map<String, String> initHouseholdType() {
        if (MapUtil.isEmpty(householdTypeMap)) {
            householdTypeMap = new HashMap<>(capacity(3));
            householdTypeMap.put("1", "业主");
            householdTypeMap.put("2", "家属");
            householdTypeMap.put("3", "租客");
        }
        return householdTypeMap;
    }

    /**
     * <p>
     * 初始化家属类型字典
     * </p>
     */
    private static Map<String, String> initMemberType() {
        if (MapUtil.isEmpty(memberTypeMap)) {
            memberTypeMap = new HashMap<>(capacity(8));
            memberTypeMap.put("1", "配偶");
            memberTypeMap.put("2", "子");
            memberTypeMap.put("3", "女");
            memberTypeMap.put("4", "孙子、孙女或外孙子、外孙女");
            memberTypeMap.put("5", "父母");
            memberTypeMap.put("6", "祖父母或外祖父母");
            memberTypeMap.put("7", "兄、弟、姐、妹");
            memberTypeMap.put("8", "其他");
        }
        return memberTypeMap;
    }

    /**
     * <p>
     * 初始化治安重点人员管理类别字典
     * </p>
     */
    private static Map<String, String> initFocusCategory() {
        if (MapUtil.isEmpty(focusCategoryMap)) {
            focusCategoryMap = new HashMap<>();

        }
        return focusCategoryMap;
    }

    /**
     * <p>
     * 初始化管控状态字典
     * </p>
     */
    private static Map<String, String> initFocusStatus() {
        if (MapUtil.isEmpty(focusStatusMap)) {
            focusStatusMap = new HashMap<>();
        }
        return focusStatusMap;

    }

    /**
     * <p>
     * 初始化归属类型字典
     * </p>
     */
    private static Map<String, String> initAttributionType() {
        if (MapUtil.isEmpty(attributionTypeMap)) {
            attributionTypeMap = new HashMap<>(capacity(2));
            attributionTypeMap.put("1", "产权");
            attributionTypeMap.put("2", "租赁");
        }
        return attributionTypeMap;
    }

    /**
     * <p>
     * 初始化机动车类型代字典
     * </p>
     */
    private static Map<String, String> initPlateTypeMap() {
        if (MapUtil.isEmpty(plateTypeMap)) {
            plateTypeMap = new HashMap<>(capacity(25));
            plateTypeMap.put("01", "大型汽车号牌");
            plateTypeMap.put("02", "小型汽车号牌");
            plateTypeMap.put("03", "使馆汽车号牌");
            plateTypeMap.put("04", "领馆汽车号牌");
            plateTypeMap.put("05", "境外汽车号牌");
            plateTypeMap.put("06", "两、三轮摩托车号牌");
            plateTypeMap.put("07", "轻便摩托车号牌");
            plateTypeMap.put("08", "使馆摩托车号牌");
            plateTypeMap.put("09", "领馆摩托车号牌");
            plateTypeMap.put("10", "境外摩托车号牌");
            plateTypeMap.put("11", "外籍摩托车号牌");
            plateTypeMap.put("12", "农用运输车号牌");
            plateTypeMap.put("13", "外籍摩托车号牌");
            plateTypeMap.put("14", "拖拉机号牌");
            plateTypeMap.put("15", "挂车号牌");
            plateTypeMap.put("16", "教练汽车号牌");
            plateTypeMap.put("17", "教练摩托车号牌");
            plateTypeMap.put("18", "试验汽车号牌");
            plateTypeMap.put("19", "试验摩托车号牌");
            plateTypeMap.put("20", "临时入境汽车号牌");
            plateTypeMap.put("21", "临时入境摩托车号牌");
            plateTypeMap.put("22", "临时行驶车号牌");
            plateTypeMap.put("23", "警用汽车号牌");
            plateTypeMap.put("24", "警用摩托号牌");
            plateTypeMap.put("99", "其他号牌");
        }
        return plateTypeMap;
    }

    /**
     * <p>
     * 初始化车辆号牌颜色字典
     * </p>
     */
    private static Map<String, String> initPlateColorMap() {
        if (MapUtil.isEmpty(plateColorMap)) {
            plateColorMap = new HashMap<>(capacity(6));
            plateColorMap.put("1", "黄底黑字");
            plateColorMap.put("2", "蓝底白字");
            plateColorMap.put("3", "黑底白字");
            plateColorMap.put("4", "黑底白字、红\"使\"字");
            plateColorMap.put("5", "黑底白字、红\"领\"字");
            plateColorMap.put("6", "黄底黑字黑框线");
        }
        return plateColorMap;
    }

    /**
     * <p>
     * 初始化车辆号牌种类颜色字典
     * </p>
     */
    private static Map<String, String> initPlateTypeColorMap() {
        if (MapUtil.isEmpty(plateTypeColorMap)) {
            plateTypeColorMap = new HashMap<>(capacity(25));
            plateTypeColorMap.put("01", "1");
            plateTypeColorMap.put("02", "2");
            plateTypeColorMap.put("03", "4");
            plateTypeColorMap.put("04", "5");
            plateTypeColorMap.put("05", "");
            plateTypeColorMap.put("06", "1");
            plateTypeColorMap.put("07", "2");
            plateTypeColorMap.put("08", "4");
            plateTypeColorMap.put("09", "5");
            plateTypeColorMap.put("10", "3");
            plateTypeColorMap.put("11", "3");
            plateTypeColorMap.put("12", "1");
            plateTypeColorMap.put("13", "3");
            plateTypeColorMap.put("14", "1");
            plateTypeColorMap.put("15", "1");
            plateTypeColorMap.put("16", "1");
            plateTypeColorMap.put("17", "1");
            plateTypeColorMap.put("18", "");
            plateTypeColorMap.put("19", "");
            plateTypeColorMap.put("20", "");
            plateTypeColorMap.put("21", "");
            plateTypeColorMap.put("22", "");
            plateTypeColorMap.put("23", "");
            plateTypeColorMap.put("24", "");
            plateTypeColorMap.put("99", "");
        }
        return plateTypeColorMap;
    }

    /**
     * <p>
     * 初始化车身颜色字典
     * </p>
     */
    private static Map<String, String> initVehicleColorMap() {
        if (MapUtil.isEmpty(vehicleColorMap)) {
            vehicleColorMap = new HashMap<>(capacity(11));
            vehicleColorMap.put("A", "白");
            vehicleColorMap.put("B", "灰");
            vehicleColorMap.put("C", "黄");
            vehicleColorMap.put("D", "粉");
            vehicleColorMap.put("E", "红");
            vehicleColorMap.put("F", "紫");
            vehicleColorMap.put("G", "绿");
            vehicleColorMap.put("H", "蓝");
            vehicleColorMap.put("I", "棕");
            vehicleColorMap.put("J", "黑");
            vehicleColorMap.put("Z", "其他");
        }
        return vehicleColorMap;
    }

    /**
     * <p>
     * 初始化机动车类型代码字典
     * </p>
     */
    private static Map<String, String> initVehicleTypeMap() {
        if (MapUtil.isEmpty(vehicleTypeMap)) {
            vehicleTypeMap = new HashMap<>(capacity(166));
            vehicleTypeMap.put("B10", "重型半挂车");
            vehicleTypeMap.put("B11", "重型普通半挂车");
            vehicleTypeMap.put("B12", "重型厢式半挂车");
            vehicleTypeMap.put("B13", "重型罐式半挂车");
            vehicleTypeMap.put("B14", "重型平扳半挂车");
            vehicleTypeMap.put("B15", "重型集装箱半挂车");
            vehicleTypeMap.put("B16", "重型自卸半挂车");
            vehicleTypeMap.put("B17", "重型特殊结构半挂车");
            vehicleTypeMap.put("B18", "重型仓栅式半挂车");
            vehicleTypeMap.put("B19", "重型旅居半挂车");
            vehicleTypeMap.put("B1A", "重型专项作业半挂车");
            vehicleTypeMap.put("B1B", "重型低平板半挂车");
            vehicleTypeMap.put("B20", "中型半挂车");
            vehicleTypeMap.put("B21", "中型普通半挂车");
            vehicleTypeMap.put("B22", "中型厢式半挂车");
            vehicleTypeMap.put("B23", "中型罐式半挂车");
            vehicleTypeMap.put("B24", "中型平板半挂车");
            vehicleTypeMap.put("B25", "中型集装箱半挂车");
            vehicleTypeMap.put("中型自卸半挂车", "中型自卸半挂车");
            vehicleTypeMap.put("B27", "中型特殊结构半桂车");
            vehicleTypeMap.put("B28", "中型仓栅式半挂车");
            vehicleTypeMap.put("B29", "中型旅居半挂车");
            vehicleTypeMap.put("B2A", "中型专项作业半挂车");
            vehicleTypeMap.put("B2B", "中型低平板半挂车");
            vehicleTypeMap.put("B30", "轻型半挂车");
            vehicleTypeMap.put("B31", "轻型普通半挂车");
            vehicleTypeMap.put("B32", "轻型厢式半挂车");
            vehicleTypeMap.put("B33", "轻型罐式半挂车");
            vehicleTypeMap.put("B34", "轻型平扳半挂车");
            vehicleTypeMap.put("B35", "轻型自卸半挂车");
            vehicleTypeMap.put("B36", "轻型仓栅式半挂车");
            vehicleTypeMap.put("B37", "轻型旅居半挂车");
            vehicleTypeMap.put("B38", "轻型专项作业半挂车");
            vehicleTypeMap.put("B39", "轻型低平板半挂车");
            vehicleTypeMap.put("D11", "无轨电车");
            vehicleTypeMap.put("D12", "有轨电车");
            vehicleTypeMap.put("G10", "重型全挂车");
            vehicleTypeMap.put("G11", "重型普通全挂车");
            vehicleTypeMap.put("G12", "重型厢式全挂车");
            vehicleTypeMap.put("G13", "重型罐式全挂车");
            vehicleTypeMap.put("G14", "重型平板全挂车");
            vehicleTypeMap.put("G15", "重型集装箱全挂车");
            vehicleTypeMap.put("G16", "重型自卸全挂车");
            vehicleTypeMap.put("G17", "重型仓栅式全挂车");
            vehicleTypeMap.put("G18", "重型旅居全挂车");
            vehicleTypeMap.put("G19", "重型专项作业全挂车");
            vehicleTypeMap.put("G20", "中型全挂车");
            vehicleTypeMap.put("G21", "中型普通全挂车");
            vehicleTypeMap.put("G22", "中型厢式全挂车");
            vehicleTypeMap.put("G23", "中型罐式全挂车");
            vehicleTypeMap.put("G24", "中型平扳全挂车");
            vehicleTypeMap.put("G25", "中型集装箱全挂车");
            vehicleTypeMap.put("G26", "中型自卸全挂车");
            vehicleTypeMap.put("G27", "中型仓栅式全挂车");
            vehicleTypeMap.put("G28", "中型旅居全挂车");
            vehicleTypeMap.put("G29", "中型专项作业全挂车");
            vehicleTypeMap.put("G30", "轻型全挂车");
            vehicleTypeMap.put("G31", "轻型普通全挂车");
            vehicleTypeMap.put("G32", "轻型厢式全挂车");
            vehicleTypeMap.put("G33", "轻型罐式全挂车");
            vehicleTypeMap.put("G34", "轻型平板全挂车");
            vehicleTypeMap.put("G35", "轻型自卸全挂车");
            vehicleTypeMap.put("轻型仓栅式全挂车", "轻型仓栅式全挂车");
            vehicleTypeMap.put("G37", "轻型旅居全佳车");
            vehicleTypeMap.put("G38", "轻型号项作业全挂车");
            vehicleTypeMap.put("H10", "重型货车");
            vehicleTypeMap.put("H11", "重型普通货车");
            vehicleTypeMap.put("H12", "重型厢式货车");
            vehicleTypeMap.put("H13", "重型封闭货车");
            vehicleTypeMap.put("H14", "重型罐式货车");
            vehicleTypeMap.put("H15", "重型平板货车");
            vehicleTypeMap.put("H16", "重型集装厢车");
            vehicleTypeMap.put("H17", "重型自卸货车");
            vehicleTypeMap.put("H18", "重型特殊结构货车");
            vehicleTypeMap.put("H19", "重型仓栅式货车");
            vehicleTypeMap.put("H20", "中型货车");
            vehicleTypeMap.put("H21", "中型普通货车");
            vehicleTypeMap.put("H22", "中型厢式货车");
            vehicleTypeMap.put("H23", "中型封闭货车");
            vehicleTypeMap.put("H24", "中型罐式货车");
            vehicleTypeMap.put("H25", "中型平板货车");
            vehicleTypeMap.put("H26", "中型集装厢车");
            vehicleTypeMap.put("H27", "中型自卸货车");
            vehicleTypeMap.put("H28", "中型特殊结构货车");
            vehicleTypeMap.put("H29", "中型仓栅式货车");
            vehicleTypeMap.put("H30", "轻型货车");
            vehicleTypeMap.put("H31", "轻型普通货车");
            vehicleTypeMap.put("H32", "轻型厢式货车");
            vehicleTypeMap.put("H33", "轻型封闭货车");
            vehicleTypeMap.put("H34", "轻型罐式货车");
            vehicleTypeMap.put("H35", "轻型平板货车");
            vehicleTypeMap.put("H37", "轻型自卸货车");
            vehicleTypeMap.put("H38", "轻型特殊结构货车");
            vehicleTypeMap.put("H39", "轻型仓栅式货车");
            vehicleTypeMap.put("H40", "微型货车");
            vehicleTypeMap.put("H41", "微型普通货车");
            vehicleTypeMap.put("H42", "微到厢式货车");
            vehicleTypeMap.put("H43", "微型封闭货车");
            vehicleTypeMap.put("H44", "微型罐式货车");
            vehicleTypeMap.put("H45", "微观自卸货车");
            vehicleTypeMap.put("H46", "微型特殊结构货车");
            vehicleTypeMap.put("H47", "微型仓栅式货车");
            vehicleTypeMap.put("H50", "低速货车");
            vehicleTypeMap.put("H51", "普通低速货车");
            vehicleTypeMap.put("H52", "厢式低速货车");
            vehicleTypeMap.put("H53", "罐式低速货车");
            vehicleTypeMap.put("H54", "自卸低速货车");
            vehicleTypeMap.put("H55", "仓栅式低速货车");
            vehicleTypeMap.put("J11", "轮式装载机械");
            vehicleTypeMap.put("J12", "轮式挖割机械");
            vehicleTypeMap.put("J13", "轮式平地机械");
            vehicleTypeMap.put("K10", "大型客车");
            vehicleTypeMap.put("K11", "大型普通客车");
            vehicleTypeMap.put("K12", "大型双层客车");
            vehicleTypeMap.put("K13", "大型卧铺客车");
            vehicleTypeMap.put("K14", "大型铰接客车");
            vehicleTypeMap.put("K15", "大型越野客车");
            vehicleTypeMap.put("K16", "大型轿车");
            vehicleTypeMap.put("K17", "大型专用客车");
            vehicleTypeMap.put("K20", "中型客车");
            vehicleTypeMap.put("K21", "中型普通客车");
            vehicleTypeMap.put("K22", "中型双层客车");
            vehicleTypeMap.put("K23", "中型卧铺客车");
            vehicleTypeMap.put("K24", "中型铰接客车");
            vehicleTypeMap.put("K25", "中型越野客车");
            vehicleTypeMap.put("K27", "中型专用客车");
            vehicleTypeMap.put("K30", "小型客车");
            vehicleTypeMap.put("K31", "小型普通客车");
            vehicleTypeMap.put("K32", "小型越野客车");
            vehicleTypeMap.put("K33", "小型轿车");
            vehicleTypeMap.put("K34", "小型专用客车");
            vehicleTypeMap.put("K40", "微型客车");
            vehicleTypeMap.put("K41", "微型普通客车");
            vehicleTypeMap.put("K42", "微型越野客车");
            vehicleTypeMap.put("K43", "微型轿车");
            vehicleTypeMap.put("M10", "三轮摩托车");
            vehicleTypeMap.put("M11", "普通正三轮摩托车");
            vehicleTypeMap.put("M12", "轻便正三轮摩托车");
            vehicleTypeMap.put("M13", "正三轮载客摩托车");
            vehicleTypeMap.put("M14", "正三轮载货摩托车");
            vehicleTypeMap.put("M15", "侧三轮摩托车");
            vehicleTypeMap.put("M20", "二轮摩托车");
            vehicleTypeMap.put("M21", "普通二轮摩托车");
            vehicleTypeMap.put("M22", "轻便二轮摩托车");
            vehicleTypeMap.put("N11", "三轮汽车");
            vehicleTypeMap.put("Q10", "重型牵引车");
            vehicleTypeMap.put("Q11", "重型半挂牵引车");
            vehicleTypeMap.put("Q12", "重型全挂牵引车");
            vehicleTypeMap.put("Q20", "中型牵引车");
            vehicleTypeMap.put("Q21", "中型半挂牵引车");
            vehicleTypeMap.put("Q22", "中型全挂牵引车");
            vehicleTypeMap.put("Q30", "轻型牵引车");
            vehicleTypeMap.put("Q31", "轻型半挂牵引车");
            vehicleTypeMap.put("Q32", "轻型全挂牵引车");
            vehicleTypeMap.put("T11", "大型轮式拖拉机");
            vehicleTypeMap.put("T20", "小型拖拉机");
            vehicleTypeMap.put("T21", "小型轮式拖拉机");
            vehicleTypeMap.put("T22", "手扶拖拉机");
            vehicleTypeMap.put("T23", "手扶变形运输机");
            vehicleTypeMap.put("X99", "其他");
            vehicleTypeMap.put("Z11", "大型专项作业车");
            vehicleTypeMap.put("Z21", "中型专项作业车");
            vehicleTypeMap.put("Z31", "小型专项作业车");
            vehicleTypeMap.put("Z41", "微型专项作业车");
            vehicleTypeMap.put("Z51", "重型专项作业车");
            vehicleTypeMap.put("Z71", "轻型专项作业车");
        }
        return vehicleTypeMap;
    }

    /**
     * <p>
     * 初始化车牌号 省 字典
     * </p>
     */
    private static Map<String, String> initPlateNumberProvinceMap() {
        if (MapUtil.isEmpty(plateNumberProvinceMap)) {
            plateNumberProvinceMap = new HashMap<>(capacity(32));
            plateNumberProvinceMap.put("京", "京");
            plateNumberProvinceMap.put("湘", "湘");
            plateNumberProvinceMap.put("津", "津");
            plateNumberProvinceMap.put("鄂", "鄂");
            plateNumberProvinceMap.put("沪", "沪");
            plateNumberProvinceMap.put("粤", "粤");
            plateNumberProvinceMap.put("渝", "渝");
            plateNumberProvinceMap.put("琼", "琼");
            plateNumberProvinceMap.put("翼", "翼");
            plateNumberProvinceMap.put("川", "川");
            plateNumberProvinceMap.put("晋", "晋");
            plateNumberProvinceMap.put("贵", "贵");
            plateNumberProvinceMap.put("辽", "辽");
            plateNumberProvinceMap.put("云", "云");
            plateNumberProvinceMap.put("吉", "吉");
            plateNumberProvinceMap.put("陕", "陕");
            plateNumberProvinceMap.put("黑", "黑");
            plateNumberProvinceMap.put("甘", "甘");
            plateNumberProvinceMap.put("苏", "苏");
            plateNumberProvinceMap.put("青", "青");
            plateNumberProvinceMap.put("浙", "浙");
            plateNumberProvinceMap.put("皖", "皖");
            plateNumberProvinceMap.put("藏", "藏");
            plateNumberProvinceMap.put("闽", "闽");
            plateNumberProvinceMap.put("蒙", "蒙");
            plateNumberProvinceMap.put("赣", "赣");
            plateNumberProvinceMap.put("桂", "桂");
            plateNumberProvinceMap.put("鲁", "鲁");
            plateNumberProvinceMap.put("宁", "宁");
            plateNumberProvinceMap.put("豫", "豫");
            plateNumberProvinceMap.put("新", "新");
            plateNumberProvinceMap.put("冀","冀");
        }
        return plateNumberProvinceMap;
    }

    /**
     * <p>
     * 初始化车牌号 省 字典
     * </p>
     */
    private static Map<String, String> initPlateNumberAlphabetMap() {
        if (MapUtil.isEmpty(plateNumberAlphabet)) {
            plateNumberAlphabet = new HashMap<>(capacity(25));
            plateNumberAlphabet.put("A", "A");
            plateNumberAlphabet.put("B", "B");
            plateNumberAlphabet.put("C", "C");
            plateNumberAlphabet.put("D", "D");
            plateNumberAlphabet.put("E", "E");
            plateNumberAlphabet.put("F", "F");
            plateNumberAlphabet.put("G", "G");
            plateNumberAlphabet.put("H", "H");
            plateNumberAlphabet.put("J", "J");
            plateNumberAlphabet.put("K", "K");
            plateNumberAlphabet.put("L", "L");
            plateNumberAlphabet.put("M", "M");
            plateNumberAlphabet.put("N", "N");
            plateNumberAlphabet.put("O", "O");
            plateNumberAlphabet.put("P", "P");
            plateNumberAlphabet.put("Q", "Q");
            plateNumberAlphabet.put("R", "R");
            plateNumberAlphabet.put("S", "S");
            plateNumberAlphabet.put("T", "T");
            plateNumberAlphabet.put("U", "U");
            plateNumberAlphabet.put("V", "V");
            plateNumberAlphabet.put("W", "W");
            plateNumberAlphabet.put("X", "X");
            plateNumberAlphabet.put("Y", "Y");
            plateNumberAlphabet.put("Z", "Z");
        }
        return plateNumberAlphabet;
    }

}
