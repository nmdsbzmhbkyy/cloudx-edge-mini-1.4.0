package com.aurine.cloudx.estate.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * (ProjectRepairRecordResultFormVo)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/9/2 14:00
 */

@ApiModel("报修处理结果表单视图")
@Data
public class ProjectRepairRecordResultFormVo {

        /**
         * 报修单号，uuid
         */
        @ApiModelProperty(value = "报修单号，uuid", required = true)
        private String repairId;
        /**
         * 回复内容
         */
        @ApiModelProperty(value = "回复内容")
        private String remark;
        /**
         * 完成图片
         */
        @ApiModelProperty(value = "完成图片")
        private String donePicPath;

        @ApiModelProperty(value="完成图片2")
        private String donePicPath2;

        @ApiModelProperty(value="完成图片3")
        private String donePicPath3;

        @ApiModelProperty(value="完成图片4")
        private String donePicPath4;

        @ApiModelProperty(value="完成图片5")
        private String donePicPath5;

        @ApiModelProperty(value="完成图片6")
        private String donePicPath6;

        /**
         * 维修时间
         */
        @ApiModelProperty(value = "维修时间")
        private LocalDateTime maintainTime;

        /**
         * 维修费用
         */
        @ApiModelProperty(value = "维修费用")
        private BigDecimal feeAmount;

        /**
         * 维修类型
         */
        @ApiModelProperty(value = "维修类型")
        private String maintainType;

    }
