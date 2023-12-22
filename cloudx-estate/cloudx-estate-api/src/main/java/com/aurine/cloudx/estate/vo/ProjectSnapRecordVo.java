

package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectSnapRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 访客列表VO
 *
 * @author 王良俊
 * @date 2020-06-04 11:51:11
 */
@Data
public class ProjectSnapRecordVo extends ProjectSnapRecord {

    /**
     * 事件类型名
     */
    @ApiModelProperty(value = "事件类型名")
    private String eventTypeName;

    /**
     * 事件子类型名
     */
    @ApiModelProperty(value = "事件子类型名")
    private String eventSubTypeName;

    /**
     * 开始时间，查询条件
     */
    @ApiModelProperty(value = "开始时间，查询条件")
    private LocalDateTime eventTimeBegin;

    /**
     * 结束时间，查询条件
     */
    @ApiModelProperty(value = "结束时间，查询条件")
    private LocalDateTime eventTimeEnd;
}
