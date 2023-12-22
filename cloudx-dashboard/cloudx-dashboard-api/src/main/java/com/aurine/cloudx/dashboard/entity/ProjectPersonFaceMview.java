package com.aurine.cloudx.dashboard.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 人脸覆盖率
 * @ClassName: ProjectComplaintRecordMview
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-29 14:37
 * @Copyright:
 */
@Data
@TableName("PROJECT_PERSON_FACE_MVIEW")
public class ProjectPersonFaceMview extends BaseDashboardEntity {


    /**
     * 总人数
     */
    @ApiModelProperty("总人数")
    @TableField("CNT_TOTAL")
    private Long total;

    /**
     * 有车人数
     */
    @ApiModelProperty("有车人数")
    @TableField("CNT_PERSON_FACE")
    private Long personFace;

}
