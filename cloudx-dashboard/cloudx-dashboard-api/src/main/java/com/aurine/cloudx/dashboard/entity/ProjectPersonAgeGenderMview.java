package com.aurine.cloudx.dashboard.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 住户性别与年龄
 *
 * @ClassName: ProjectComplaintRecordMview
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-29 14:37
 * @Copyright:
 */
@Data
@TableName("PROJECT_PERSON_AGE_GENDER_MVIEW")
public class ProjectPersonAgeGenderMview extends BaseDashboardEntity {


    /**
     * 住户数
     */
    @ApiModelProperty("住户数")
    @TableField("CNT_TOTAL")
    private Long total;
    /**
     * unknownFemale
     */
    @ApiModelProperty("unknownFemale")
    @TableField("CNT_UNKNOWN_FEMALE")
    private Long unknownFemale;
    /**
     * unknownMale
     */
    @ApiModelProperty("unknownMale")
    @TableField("CNT_UNKNOWN_MALE")
    private Long unknownMale;
    /**
     * unknownUnknown
     */
    @ApiModelProperty("unknownUnknown")
    @TableField("CNT_UNKNOWN_UNKNOWN")
    private Long unknownUnknown;

    /**
     * 0-9岁女性
     */
    @ApiModelProperty("0-9岁女性")
    @TableField("CNT_0_9_FEMALE")
    private Long female0to9;
    /**
     * 10-19岁女性
     */
    @ApiModelProperty("10-19岁女性")
    @TableField("CNT_10_19_FEMALE")
    private Long female10to19;

    /**
     * 20-29岁女性
     */
    @ApiModelProperty("20-29岁女性")
    @TableField("CNT_20_29_FEMALE")
    private Long female20to29;
    /**
     * 30-39岁女性
     */
    @ApiModelProperty("30-39岁女性")
    @TableField("CNT_30_39_FEMALE")
    private Long female30to39;
    /**
     * 40-49岁女性
     */
    @ApiModelProperty("40-49岁女性")
    @TableField("CNT_40_49_FEMALE")
    private Long female40to49;
    /**
     * 50-59岁女性
     */
    @ApiModelProperty("50-59岁女性")
    @TableField("CNT_50_59_FEMALE")
    private Long female50to59;
    /**
     * 60-69岁女性
     */
    @ApiModelProperty("60-69岁女性")
    @TableField("CNT_60_69_FEMALE")
    private Long female60to69;
    /**
     * 70-79岁女性
     */
    @ApiModelProperty("70-79岁女性")
    @TableField("CNT_70_79_FEMALE")
    private Long female70to79;
    /**
     * 80-89岁女性
     */
    @ApiModelProperty("80-89岁女性")
    @TableField("CNT_80_89_FEMALE")
    private Long female80to89;
    /**
     * 90-99岁女性
     */
    @ApiModelProperty("90-99岁女性")
    @TableField("CNT_90_99_FEMALE")
    private Long female90to99;
    /**
     * 100岁女性
     */
    @ApiModelProperty("100岁女性")
    @TableField("CNT_100_FEMALE")
    private Long female100;

    /**
     * 0-9岁男性
     */
    @ApiModelProperty("0-9岁男性")
    @TableField("CNT_0_9_MALE")
    private Long male0to9;

    /**
     * 10-19岁男性
     */
    @ApiModelProperty("10-19岁男性")
    @TableField("CNT_10_19_MALE")
    private Long male10to19;

    /**
     * 20-29岁男性
     */
    @ApiModelProperty("20-29岁男性")
    @TableField("CNT_20_29_MALE")
    private Long male20to29;
    /**
     * 30-39岁男性
     */
    @ApiModelProperty("30-39岁男性")
    @TableField("CNT_30_39_MALE")
    private Long male30to39;
    /**
     * 40-49岁男性
     */
    @ApiModelProperty("40-49岁男性")
    @TableField("CNT_40_49_MALE")
    private Long male40to49;
    /**
     * 50-59岁男性
     */
    @ApiModelProperty("50-59岁男性")
    @TableField("CNT_50_59_MALE")
    private Long male50to59;
    /**
     * 60-69岁男性
     */
    @ApiModelProperty("60-69岁男性")
    @TableField("CNT_60_69_MALE")
    private Long male60to69;
    /**
     * 70-79岁男性
     */
    @ApiModelProperty("70-79岁男性")
    @TableField("CNT_70_79_MALE")
    private Long male70to79;
    /**
     * 80-89岁男性
     */
    @ApiModelProperty("80-89岁男性")
    @TableField("CNT_80_89_MALE")
    private Long male80to89;
    /**
     * 90-99岁男性
     */
    @ApiModelProperty("90-99岁男性")
    @TableField("CNT_90_99_MALE")
    private Long male90to99;
    /**
     * 100岁男性
     */
    @ApiModelProperty("100岁男性")
    @TableField("CNT_100_MALE")
    private Long male100;


    /**
     * 0-9岁未知性别
     */
    @ApiModelProperty("0-9岁未知性别")
    @TableField("CNT_0_9_UNKNOWN")
    private Long unknown0to9;

    /**
     * 10-19岁未知性别
     */
    @ApiModelProperty("10-19岁未知性别")
    @TableField("CNT_10_19_UNKNOWN")
    private Long unknown10to19;

    /**
     * 20-29岁未知性别
     */
    @ApiModelProperty("20-29岁未知性别")
    @TableField("CNT_20_29_UNKNOWN")
    private Long unknown20to29;
    /**
     * 30-39岁未知性别
     */
    @ApiModelProperty("30-39岁未知性别")
    @TableField("CNT_30_39_UNKNOWN")
    private Long unknown30to39;
    /**
     * 40-49岁未知性别
     */
    @ApiModelProperty("40-49岁未知性别")
    @TableField("CNT_40_49_UNKNOWN")
    private Long unknown40to49;
    /**
     * 50-59岁未知性别
     */
    @ApiModelProperty("50-59岁未知性别")
    @TableField("CNT_50_59_UNKNOWN")
    private Long unknown50to59;
    /**
     * 60-69岁未知性别
     */
    @ApiModelProperty("60-69岁未知性别")
    @TableField("CNT_60_69_UNKNOWN")
    private Long unknown60to69;
    /**
     * 70-79岁未知性别
     */
    @ApiModelProperty("70-79岁未知性别")
    @TableField("CNT_70_79_UNKNOWN")
    private Long unknown70to79;
    /**
     * 80-89岁未知性别
     */
    @ApiModelProperty("80-89岁未知性别")
    @TableField("CNT_80_89_UNKNOWN")
    private Long unknown80to89;
    /**
     * 90-99岁未知性别
     */
    @ApiModelProperty("90-99岁未知性别")
    @TableField("CNT_90_99_UNKNOWN")
    private Long unknown90to99;
    /**
     * 100岁未知性别
     */
    @ApiModelProperty("100岁未知性别")
    @TableField("CNT_100_UNKNOWN")
    private Long unknown100;

}
