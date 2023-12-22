package com.aurine.cloudx.estate.vo;


import com.aurine.cloudx.estate.entity.ProjectPerimeterAlarmRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 周界报警
 *
 * @author 邹宇
 * @date 2021-6-8 14:30:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectPerimeterAlarmRecordVo extends ProjectPerimeterAlarmRecord {

    @ApiModelProperty(value = "开始时间")
    private String beginTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "操作人")
    private String name;

}
