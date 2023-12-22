package com.aurine.cloudx.estate.dto.qrPasscode;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.aurine.cloudx.estate.entity.ProjectQrPasscodeRecord;
import com.fasterxml.jackson.databind.util.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import jdk.nashorn.internal.parser.DateParser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.BeanUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

@Data
public class QrPasscodePageDto {
    /**
     * 通行人员名称
     */
    @ApiModelProperty(name = "通行人员名称", notes = "")
    private String passenger;
    /**
     * 通行人员电话
     */
    @ApiModelProperty(name = "通行人员电话", notes = "")
    private String phone;
    /**
     * 通行人员电话号码
     */
    @ApiModelProperty(name = "通行人员电话号码", notes = "")
    private String credentialNo;
    /**
     * 放行开始时间
     */
    @ApiModelProperty(name = "放行开始时间", notes = "")
    private Long startTime;
    /**
     * 放行结束时间
     */
    @ApiModelProperty(name = "放行结束时间", notes = "")
    private Long endTime;

    /**
     * 放行开始时间
     */
    @ApiModelProperty(name = "放行开始时间", notes = "")
    private String startTimeStr;
    /**
     * 放行结束时间
     */
    @ApiModelProperty(name = "放行结束时间", notes = "")
    private String endTimeStr;
    /**
     * 可通行次数
     */
    @ApiModelProperty(name = "可通行次数", notes = "")
    private Integer times;
    /**
     * 二维码校验字符串
     */
    @ApiModelProperty(name = "二维码校验字符串", notes = "")
    private String uniqueCode;
//    /**
//     * 创建时间
//     */
//    @ApiModelProperty(value = "创建时间")
//    private Date createTime;
//    /**
//     * 更新时间
//     */
//    @ApiModelProperty(value = "更新时间")
//    private Date updateTime;


    public void initTimeStr() {
        if (Objects.nonNull(this.startTime)) {
            this.startTimeStr = DateUtil.format(new Date(this.startTime*1000),DatePattern.NORM_DATETIME_PATTERN);
        }
        if (Objects.nonNull(this.endTime)) {
            this.endTimeStr = DateUtil.format(new Date(this.endTime*1000),DatePattern.NORM_DATETIME_PATTERN);
        }
    }

}
