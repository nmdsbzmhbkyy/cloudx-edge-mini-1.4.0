package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectNotice;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Title: ProjectNoticeVo
 * Description: 消息发送视图
 *
 * @author xull@aurine.cn
 * @version 1.0.0
 * @date 2020/5/21 13:11
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "消息发送分页查询视图")
public class ProjectNoticeVo extends ProjectNotice {
    /**
     * 下发失败数
     */
    @ApiModelProperty("下发失败数")
    private Integer countFailNum;


}
