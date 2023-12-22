package com.aurine.cloudx.estate.address;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName: AddressParseUtil
 * @author: 王良俊 <>
 * @date: 2020年08月19日 下午07:58:24
 * @Copyright:
 */
public class AddressParseUtil {

    private static Map<String, Map<String, String>> addressMap = new HashMap<>();

    // 户籍/地址 包含 省、市、县（区）、街
    public static final String ADDRESS = "ADDRESS";
    // 籍贯 包含省、市、县（区）
    public static final String HOMETOWN = "HOMETOWN";


    static {
        initDictMap();
    }

    private static void initDictMap() {
        try {
            ResourceLoader resourceLoader = new DefaultResourceLoader();
            Resource resource = resourceLoader.getResource("classpath:/json/addressCode.json");
            addressMap = new ObjectMapper().readValue(resource.getInputStream(), new TypeReference<Map<String, Map<String, String>>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Address parseAddressNameToCode(String columnName, String[] addressArr, String type, boolean required) {

        int addressLength = 4;

        int hometownLength = 3;

        if (ArrayUtil.isEmpty(addressArr) || StrUtil.isEmpty(type) || StrUtil.isEmpty(columnName)) {
            return null;
        }
        // 这两个地区地址json中只有这两个地址
        if (!required) {
          addressLength = 0;
          hometownLength = 0;
        } else if (("澳门特别行政区".equals(addressArr[0]) || "台湾省".equals(addressArr[0]))) {
            addressLength = 1;
            hometownLength = 1;
        } else if (("香港特别行政区".equals(addressArr[0]))) {
            addressLength = 2;
            hometownLength = 2;
        }
        Address address = new Address();
        for (String addressName : addressArr) {
            address.addAddress(addressName);
        }
        if (ADDRESS.equals(type)) {
            return parseAddressNameToCode(columnName, address, addressLength);
        } else if (HOMETOWN.equals(type)) {
            return parseAddressNameToCode(columnName, address, hometownLength);
        } else {
            return null;
        }
    }


    /**
     * <p>
     * 给地址用的精确到街，用于获取地区code
     * </p>
     *
     * @param address 地址对象 只需填入地址名
     * @param minSize 如果只要省则为 1， 省、市、县为 3
     * @return 地址对象
     */
    private static Address parseAddressNameToCode(String columnName, Address address, Integer minSize) {

        address.clearAddressCode();
        int addressNum = address.countAddressNum();
        if (addressNum < minSize) {
            throw new ExcelAnalysisException(columnName + "：未填写完全/地址填写错误");
        }
        if (MapUtil.isEmpty(addressMap) || addressMap.get("city") == null) {
            initDictMap();
        }
        Map<String, String> province = addressMap.get("city");
        StringBuilder errorAddress = new StringBuilder();
        String provinceCode = getCode(province, address.getProvinceName());
        errorAddress.append(address.getProvinceName());
        if (StrUtil.isNotBlank(provinceCode)) {
            address.setProvinceCode(provinceCode);
            Map<String, String> cityMap = addressMap.get(provinceCode);
            String cityCode = getCode(cityMap, address.getCityName());
            errorAddress.append("-").append(address.getCityName());
            if (StrUtil.isNotBlank(cityCode)) {
                address.setCityCode(cityCode);
                Map<String, String> countryMap = addressMap.get(cityCode);
                String countryCode = getCode(countryMap, address.getCountyName());
                errorAddress.append("-").append(address.getCountyName());
                if (StrUtil.isNotBlank(countryCode)) {
                    address.setCountyCode(countryCode);
                    Map<String, String> streetMap = addressMap.get(countryCode);
                    String streetCode = getCode(streetMap, address.getStreetName());
                    errorAddress.append("-").append(address.getStreetName());
                    if (StrUtil.isNotBlank(streetCode)) {
                        address.setStreetCode(streetCode);
                        address.setSuccess(true);
                    } else {
                        if (minSize <= 3 && addressNum <= 3) {
                            return address;
                        }
                        throw new ExcelAnalysisException(columnName + "：街道填写错误，未找到 " + errorAddress);
                    }
                } else {
                    if (minSize <= 2 && addressNum <= 2) {
                        return address;
                    }
                    throw new ExcelAnalysisException(columnName + "：县/区填写错误，未找到 " + errorAddress);
                }
            } else {
                // 这里如果地址数不是1则说明后面还有地址可能出现填写错误的情况
                if (minSize <= 1 && addressNum <= 1) {
                    return address;
                }
                throw new ExcelAnalysisException(columnName + "：城市填写错误，未找到 " + errorAddress);
            }
        } else {
            if (minSize == 0 && addressNum == 0) {
                return address;
            }
            throw new ExcelAnalysisException(columnName + "：省份填写错误，未找到 " + errorAddress);
        }
        return address;
    }

    /**
     * <p>
     * 用于获取地区名
     * </p>
     *
     * @param provinceCode 省代码
     * @param cityCode     市代码
     * @param countyCode   县/区代码
     * @param streetCode   街道代码
     * @return 地址对象
     */
    public static Address parseAddressCodeToName(String provinceCode, String cityCode, String countyCode, String streetCode) {
        Address address = new Address();
        if (StrUtil.isNotBlank(provinceCode)) {
            Map<String, String> provinceMap = addressMap.get("city");
            String provinceName = getName(provinceMap, provinceCode);
            address.setProvinceName(provinceName);
            if (StrUtil.isBlank(cityCode)) {
                return address;
            }
            Map<String, String> cityMap = addressMap.get(provinceCode);
            address.setCityName(getName(cityMap, cityCode));
            if (StrUtil.isBlank(countyCode)) {
                return address;
            }
            Map<String, String> countyMap = addressMap.get(cityCode);
            address.setCountyName(getName(countyMap, countyCode));
            if (StrUtil.isBlank(streetCode)) {
                return address;
            }
            Map<String, String> streetMap = addressMap.get(countyCode);
            address.setStreetName(getName(streetMap, streetCode));
        }
        address.setProvinceCode(provinceCode);
        address.setCityCode(cityCode);
        address.setCountyCode(countyCode);
        address.setStreetCode(streetCode);
        return address;
    }

    public static String addressToString(Address address) {
        StringBuilder result = new StringBuilder();

        if (StringUtils.isNotEmpty(address.getProvinceName())) result.append(address.getProvinceName());
        if (StringUtils.isNotEmpty(address.getProvinceName())) result.append(address.getProvinceName());
        if (StringUtils.isNotEmpty(address.getProvinceName())) result.append(address.getProvinceName());
        if (StringUtils.isNotEmpty(address.getProvinceName())) result.append(address.getProvinceName());

        return result.toString();
    }

    /**
     * <p>
     * 根据市/区/县...等名称称获取到其对应的code
     * </p>
     *
     * @param addressMap  这里的map已经可以直接获取到对应地址的code了
     * @param addressName 地址名
     * @return 对应 市区
     */
    private static String getCode(Map<String, String> addressMap, String addressName) {
        if (MapUtil.isNotEmpty(addressMap)) {
            List<String> provinceCodeList = addressMap.entrySet()
                    .stream()
                    .filter(kvEntry -> Objects.equals(kvEntry.getValue(), addressName))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            if (CollUtil.isNotEmpty(provinceCodeList)) {
                return provinceCodeList.get(0);
            }
        }
        return "";
    }

    /**
     * <p>
     * 根据code获取到名称
     * </p>
     *
     * @param addressMap  对应地区所处的map
     * @param addressCode 要获取名称的code
     * @return 地区名
     */
    private static String getName(Map<String, String> addressMap, String addressCode) {
        return addressMap.get(addressCode);
    }
}
