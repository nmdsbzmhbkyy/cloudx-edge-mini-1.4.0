package com.aurine.cloudx.estate.vo;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 周界报警
 *
 * @author 邹宇
 * @date 2021-6-8 14:30:55
 */
@Data
public class AppPerimeterAlarmEventFromVo extends Page {


    @ApiModelProperty(value = "0:未处理  1:已处理  2:我处理 3:全部")
    private String processType;
    @ApiModelProperty(value = "processType状态为2 携带处理人Id UserId")
    private String deviceRegionId;


}
