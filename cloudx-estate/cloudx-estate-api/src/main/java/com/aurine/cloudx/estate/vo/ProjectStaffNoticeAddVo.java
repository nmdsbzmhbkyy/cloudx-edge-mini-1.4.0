package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectStaffNotice;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Title: ProjectStaffNoticeAddVo
 * Description: 新增信息发布
 *
 * @author guhl@aurine.cn
 * @version 1.0.0
 * @date 2020/7/06 13:48
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("新增信息发布Vo")
public class ProjectStaffNoticeAddVo extends ProjectStaffNotice {
    /**
     * 用户列表
     */
    @ApiModelProperty("用户id列表")
    List <String> userIds;


    @ApiModelProperty("App小程序携带参数 0")
    String noticeType;
}
