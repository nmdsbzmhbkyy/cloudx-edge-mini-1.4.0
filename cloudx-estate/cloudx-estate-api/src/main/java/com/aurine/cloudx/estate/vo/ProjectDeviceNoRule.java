package com.aurine.cloudx.estate.vo;

import cn.hutool.core.util.StrUtil;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  项目设备编号规则对象
 * </p>
 * @author : 王良俊
 * @date : 2021-05-17 11:08:47
 */
public class ProjectDeviceNoRule {

    /**
     * 项目分段参数
     * */
    private String subSection;
    /**
     * 项目分段描述
     * */
    private String subSectionDesc;

    public int getStairNoLen() {
            // 除了最后一位其余加起来是梯口号长度
            String substring = getSubSection().substring(0, getSubSection().length() - 1);
            int stairNoLen = 0;
            for (int i = 0; i <= substring.length() - 1; i++) {
                stairNoLen += Integer.parseInt(substring.charAt(i) + "");
            }
            return stairNoLen;
    }

    public int getCellNoLen() {
        // 倒数第二位肯定是单元号长度
        return Integer.parseInt(getSubSection().substring(getSubSection().length() - 2, getSubSection().length()-1));
    }

    public int getRoomNoLen() {
        // 最后一位肯定是房屋编号
        return Integer.parseInt(getSubSection().substring(getSubSection().length() - 1));
    }

    public String getSubSection() {
        return StrUtil.isNotEmpty(subSection) ? subSection : "224";
    }

    public String getStairNoLenReg() {
        return getTheSpecifiedValueReg(Integer.toString(getStairNoLen()));
    }

    public String getCellNoLenReg() {
        return getTheSpecifiedValueReg(Integer.toString(getCellNoLen()));
    }

    public String getRoomNoLenReg() {
        return getTheSpecifiedValueReg(Integer.toString(getRoomNoLen()));
    }

    public String getSubSectionReg() {
        return getTheSpecifiedValueReg(getSubSection());
    }

    public void setSubSection(String subSection) {
        this.subSection = subSection;
    }

    public List<String> getSubSectionDesc() {
        String[] split = subSectionDesc.split(",");
        return Arrays.asList(split);
    }

    /**
     * <p>
     * 获取限制必须完全一样才能匹配的正则表达式
     * </p>
     *
     * @param value 所要限制的值
     */
    private String getTheSpecifiedValueReg(String value) {
        return  "^(?=" + value + "$)";
    }


}
